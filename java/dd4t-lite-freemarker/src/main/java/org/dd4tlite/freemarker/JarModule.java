package org.dd4tlite.freemarker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Jar Module
 *
 * @author nic
 */
public class JarModule {

    static private Log log = LogFactory.getLog(JarModule.class);

    private JarFile jarFile;
    private File deployDir;

    static final String RESOURCE_PREFIX_NAME = "META-INF/resources";
    private static final int BUFFER_SIZE = 4096;

    public static Set<JarModule> findAllModules(ServletContext servletContext, File deployDir) throws IOException {

        log.info("Using resource deploy dir: " + deployDir);
        Set<JarModule> jarModules = new HashSet<>();
        for ( String jarResource : servletContext.getResourcePaths("/WEB-INF/lib") ) {
            File jarFile = new File(servletContext.getRealPath(jarResource));
            JarModule jarModule = new JarModule(jarFile, deployDir);
            if ( jarModule.hasResources() ) {
                jarModules.add(jarModule);
            }
        }
        return jarModules;
    }

    public JarModule(File jarFile, File deployDir) throws IOException {
        this.jarFile = new JarFile(jarFile);
        this.deployDir = deployDir;
    }

    public String getName() {
        return this.jarFile.getName();
    }

    public boolean hasResources() {
        return this.jarFile.getJarEntry(RESOURCE_PREFIX_NAME) != null;
    }

    public void deployResources() throws IOException {

        Enumeration<JarEntry> entries = this.jarFile.entries();
        while ( entries.hasMoreElements() ) {
            JarEntry entry = entries.nextElement();
            if ( entry.getName().startsWith(RESOURCE_PREFIX_NAME) ) {
                String filePath = this.deployDir.getAbsolutePath() + File.separator + entry.getName().replace(RESOURCE_PREFIX_NAME, "");
                if ( entry.isDirectory() ) {
                    File dir = new File(filePath);
                    dir.mkdirs();
                }
                else {
                    this.extractFile(this.jarFile.getInputStream(entry), filePath);
                }
            }
        }
    }

    private void extractFile(InputStream inputStream, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = inputStream.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
        inputStream.close();
    }
}


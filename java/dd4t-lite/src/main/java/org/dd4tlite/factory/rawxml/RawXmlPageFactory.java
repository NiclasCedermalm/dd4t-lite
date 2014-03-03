package org.dd4tlite.factory.rawxml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dd4tlite.factory.PageFactory;
import org.dd4tlite.model.Page;
import org.dd4tlite.model.impl.xml.XmlPage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * Raw XML Page Factory.
 * Just reads an page from a raw XML representation (without SDL Tridion Broker).
 *
 * @author nic
 */
public class RawXmlPageFactory implements PageFactory {

    static private Log log = LogFactory.getLog(RawXmlPageFactory.class);

    private String rootDirectory;
    private Map<Class<?>, Object> dependencies = null;

    public RawXmlPageFactory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public RawXmlPageFactory(String rootDirectory, Map<Class<?>, Object> dependencies) {
        this(rootDirectory);
        this.dependencies = dependencies;
    }

    @Override
    public Page getPage(String url) throws Exception {

        File page = new File(rootDirectory + url);
        if ( page.exists() ) {
            InputStream inputStream = new FileInputStream(page);
            File xmlFile = new File(url);
            String path = xmlFile.getParent();
            String filename = xmlFile.getName();

            try {
                return XmlPage.load(path, filename, inputStream, this.dependencies);
            }
            catch ( Exception e ) {
                log.error("Could not load XML page!", e);
            }
        }
        return null;

    }
}
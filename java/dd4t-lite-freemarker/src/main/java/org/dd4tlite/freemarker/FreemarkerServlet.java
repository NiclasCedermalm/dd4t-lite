package org.dd4tlite.freemarker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * FreemarkerServlet
 *
 * @author nic
 */
public class FreemarkerServlet extends freemarker.ext.servlet.FreemarkerServlet {

    static private Log log = LogFactory.getLog(FreemarkerServlet.class);

    @Override
    public void init() throws ServletException {
        super.init();

        this.deployModuleResources();
    }

    protected void deployModuleResources() throws ServletException {

        // TODO: Clear the module deploy dir first
        // TODO: Have the module path configurable!!!!!
        try {

            Set<JarModule> jarModules = JarModule.findAllModules(this.getServletContext(), new File("../work/modules"));
            for ( JarModule jarModule : jarModules ) {
                log.info("Deploying JAR module: " + jarModule.getName());
                jarModule.deployResources();
            }
        }
        catch ( IOException e ) {
            throw new ServletException("Could not deploy JAR modules.", e);
        }
    }

}

/*
 *  Copyright 2014 Niclas Cedermalm
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.dd4tlite.factory.rawxml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dd4tlite.factory.PageFactory;
import org.dd4tlite.model.Page;
import org.dd4tlite.model.impl.xml.XmlPage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
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
    private Map<Class<?>, Object> dependencies;

    public RawXmlPageFactory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
        if ( this.dependencies == null ) {
            this.dependencies = new HashMap<Class<?>,Object>();
        }
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

            //try {
                return XmlPage.load(path, filename, inputStream, this.dependencies);
            //}
            //catch ( Exception e ) {
            //    log.error("Could not load XML page!", e);
                // TODO: Throw an exception here...??
            //}
        }
        return null;

    }
}

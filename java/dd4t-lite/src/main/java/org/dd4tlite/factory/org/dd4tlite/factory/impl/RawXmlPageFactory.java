package org.dd4tlite.factory.org.dd4tlite.factory.impl;

import org.dd4tlite.factory.PageFactory;
import org.dd4tlite.model.Page;
import org.dd4tlite.model.impl.xml.XmlPage;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Raw XML Page Factory.
 * Just reads an page from a raw XML representation (without SDL Tridion Broker).
 *
 * @author nic
 */
public class RawXmlPageFactory implements PageFactory {

    private String rootDirectory;

    public RawXmlPageFactory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    @Override
    public Page getPage(String url) throws Exception {

        File page = new File(rootDirectory + url);
        if ( page.exists() ) {
            InputStream inputStream = new FileInputStream(page);
            Serializer serializer = new Persister();
            XmlPage xmlPage = new XmlPage(url);
            return serializer.read(xmlPage, inputStream);
        }
        return null;

    }
}

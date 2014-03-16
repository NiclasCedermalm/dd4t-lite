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
package org.dd4tlite.model.impl.xml;

import org.dd4tlite.model.ComponentPresentation;
import org.dd4tlite.model.Page;
import org.dd4tlite.model.Region;
import org.dd4tlite.model.Template;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.XmlUtils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * XML Page
 * @author nic
 */
@Root(name="page")
public class XmlPage extends XmlItem implements Page {

    private String path;
    private String filename;

    @ElementList (required = false, type=XmlRegion.class)
    private List<Region> regions;

    @Element
    private XmlTemplate template;

    protected XmlPage(String path, String filename) {
        this.path = path;
        this.filename = filename;
    }

    static public Page load(String path, String filename, InputStream inputStream) throws Exception{
        return load(path, filename, inputStream, new HashMap());
    }

    static public Page load(String path, String filename, InputStream inputStream, Map initialSessionData) throws Exception {
        XmlPage page = new XmlPage(path, filename);
        initialSessionData.put(Page.class, page);
        Serializer serializer = XmlUtils.createSerializer(initialSessionData);
        try {
            return serializer.read(page, inputStream); // TODO: Strict mode or not???
        }
        finally {
            XmlUtils.closeSession(serializer);
        }
    }

    static public Page load(String path, String filename, String xmlContent) throws Exception {
        return load(path, filename, xmlContent, new HashMap());
    }

    static public Page load(String path, String filename, String xmlContent, Map initialSessionData) throws Exception {
        XmlPage page = new XmlPage(path, filename);
        initialSessionData.put(Page.class, page);
        Serializer serializer = XmlUtils.createSerializer(initialSessionData);
        try {
            return serializer.read(page, xmlContent); // TODO: Strict mode or not???
        }
        finally {
            XmlUtils.closeSession(serializer);
        }
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public List<ComponentPresentation> getComponentPresentations() {
        ArrayList<ComponentPresentation> componentPresentations = new ArrayList<>();
        for ( Region region : this.regions ) {
            for ( ComponentPresentation cp : region.getComponentPresentations() ) {
                componentPresentations.add(cp);
            }
        }
        return componentPresentations;
    }

    @Override
    public Template getPageTemplate() {
        return this.template;
    }

    @Override
    public List<Region> getRegions() {
        return this.regions;
    }

    @Override
    public Region getRegion(String title) {
        for ( Region region : this.regions ) {
            if ( region.getName().equals(title) ) {
                return region;
            }
        }
        return null;
    }

}

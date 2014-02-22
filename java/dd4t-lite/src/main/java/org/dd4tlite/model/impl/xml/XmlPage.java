package org.dd4tlite.model.impl.xml;

import org.dd4tlite.model.ComponentPresentation;
import org.dd4tlite.model.Page;
import org.dd4tlite.model.Region;
import org.dd4tlite.model.Template;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    public XmlPage(String url) {
        File xmlFile = new File(url);
        this.path = xmlFile.getParent();
        this.filename = xmlFile.getName();

    }
    public XmlPage(String path, String filename) {
        this.path = path;
        this.filename = filename;
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

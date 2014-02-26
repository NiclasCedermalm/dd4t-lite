package org.dd4tlite.model.impl.xml;

import org.dd4tlite.model.ComponentPresentation;
import org.dd4tlite.model.Region;
import org.dd4tlite.model.Template;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.Date;
import java.util.List;

/**
 * @author nic
 */
public class XmlRegion implements Region {

    @Attribute
    private String name;

    @ElementList (required = false, type=XmlComponentPresentation.class)
    private List<ComponentPresentation> componentPresentations;

    @Element (required = false)
    private XmlTemplate template;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<ComponentPresentation> getComponentPresentations() {
        return this.componentPresentations;
    }

    @Override
    public Template getTemplate() {
        return this.template;
    }

}

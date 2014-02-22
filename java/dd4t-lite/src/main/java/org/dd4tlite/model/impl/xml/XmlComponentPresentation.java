package org.dd4tlite.model.impl.xml;

import org.dd4tlite.model.Component;
import org.dd4tlite.model.ComponentPresentation;
import org.dd4tlite.model.Template;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import java.util.Date;

/**
 * XML Component Presentation
 *
 * @author nic
 */
public class XmlComponentPresentation implements ComponentPresentation {

    @Attribute (required = false)
    private boolean dynamic = false;

    @Element
    private XmlComponent component;
    @Element
    private XmlTemplate template;

    @Override
    public boolean isDynamic() {
        return this.dynamic;
    }

    @Override
    public Template getComponentTemplate() {
       return this.template;
    }

    @Override
    public Component getComponent() {
        return this.component;
    }

}

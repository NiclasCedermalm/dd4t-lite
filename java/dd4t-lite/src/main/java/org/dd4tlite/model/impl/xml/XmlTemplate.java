package org.dd4tlite.model.impl.xml;

import org.dd4tlite.model.Template;
import org.simpleframework.xml.Attribute;

import java.util.Date;

/**
 * @author nic
 */
public class XmlTemplate extends XmlItem implements Template {

    @Attribute
    private String viewName;

    @Override
    public String getViewName() {
        return this.viewName;
    }

}

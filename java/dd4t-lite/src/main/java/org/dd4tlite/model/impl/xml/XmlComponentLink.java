package org.dd4tlite.model.impl.xml;

import org.simpleframework.xml.Text;

/**
 * @author nic
 */
public class XmlComponentLink implements XmlValue {

    @Text
    private String link;

    @Override
    public Object getValue() {
        // TODO: Resolve the link here!!!
        return this.link;
    }
}

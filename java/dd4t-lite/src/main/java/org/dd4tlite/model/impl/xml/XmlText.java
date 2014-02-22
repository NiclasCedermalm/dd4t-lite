package org.dd4tlite.model.impl.xml;

import org.simpleframework.xml.Text;

/**
 * @author nic
 */
public class XmlText implements XmlValue {

    @Text
    protected String text;


    @Override
    public Object getValue() {
        return this.text;
    }
}

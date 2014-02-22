package org.dd4tlite.model.impl.xml;

import org.simpleframework.xml.Text;

/**
 * XML Multimedia Link
 * @author nic
 */
public class XmlMultimediaLink implements XmlValue {

    @Text
    protected String link;

    @Override
    public Object getValue() {
        if ( link.startsWith("tcm:") ) {
            // TODO: Resolve link via Tridion broker
            return this.link;
        }
        else return this.link;
    }
}

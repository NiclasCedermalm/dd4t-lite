package org.dd4tlite.model.impl.xml;

import org.dd4tlite.model.Keyword;
import org.simpleframework.xml.Attribute;

/**
 * XML Keyword value
 *
 * @author nic
 */
public class XmlKeywordValue implements Keyword, XmlValue {

    @Attribute(name = "taxonomyId")
    private String taxonomyId;

    @Attribute(name = "path")
    private String path;

    @Attribute(name = "key")
    private String key;

    @Override
    public String getTaxonomyId() {
        return this.taxonomyId;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public Object getValue() {
        return this;
    }

    @Override
    public String toString() {
        return "Keyword: " + key + " (ID: " + taxonomyId + ")";
    }
}

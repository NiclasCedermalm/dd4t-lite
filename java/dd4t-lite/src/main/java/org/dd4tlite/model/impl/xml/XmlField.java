package org.dd4tlite.model.impl.xml;

import org.dd4tlite.model.Field;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import java.util.List;

/**
 * XML Field
 *
 * @author nic
 */
public class XmlField implements Field {

    @Attribute
    private String name;

    @Attribute
    private String type;

    @Attribute (required = false)
    private String xpath;

    @Element
    private XmlValues values;

    @Override
    public String getName() {
       return this.name;
    }

    @Override
    public FieldType getType() {
        return FieldType.valueOf(this.type);
    }

    @Override
    public List<? extends Object> getValues() {
        return this.values.getValues();
    }

    @Override
    public String getXPath() {
        return this.xpath;
    }

}

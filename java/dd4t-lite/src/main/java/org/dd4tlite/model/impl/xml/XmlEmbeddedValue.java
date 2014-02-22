package org.dd4tlite.model.impl.xml;

import org.dd4tlite.model.EmbeddedField;
import org.dd4tlite.model.Field;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * XML Embedded Value
 * @author nic
 */
public class XmlEmbeddedValue implements EmbeddedField, XmlValue {

    @ElementList(entry = "field", inline = true, type=XmlField.class)
    private List<Field> fields;

    @Override
    public List<Field> getFields() {
        return this.fields;
    }

    @Override
    public Object getValue() {
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for ( Field field : this.getFields() ) {
            sb.append("  ");
            sb.append(field.getName());
            sb.append("=");
            sb.append(field.getValues());
            sb.append("\n");
        }
        return sb.toString();
    }

}

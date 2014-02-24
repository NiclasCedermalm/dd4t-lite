package org.dd4tlite.model.impl.xml;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;

import java.util.ArrayList;
import java.util.List;

/**
 * XML Values. Manages all possible XML values on fields.
 * @author nic
 */
public class XmlValues {

    @ElementListUnion({
            @ElementList(entry="text", inline=true, type=XmlText.class),
            @ElementList(entry="xhtml", inline=true, type=XmlXhtmlField.class),
            @ElementList(entry="number", inline=true, type=XmlNumber.class),
            @ElementList(entry="keyword", inline=true, type=XmlKeywordValue.class),
            @ElementList(entry="date", inline=true, type=XmlDateField.class),
            @ElementList(entry="multimedia", inline=true, type=XmlMultimediaLink.class),
            @ElementList(entry="componentLink", inline=true, type=XmlComponentLink.class),
            @ElementList (entry="embedded",inline=true, type=XmlEmbeddedValue.class)
    })
    private List<XmlValue> values;

    public List<Object> getValues() {

        ArrayList<Object> convertedValues = new ArrayList<>();
        for ( XmlValue value : values ) {
            convertedValues.add(value.getValue());
        }
        return convertedValues;
    }
}

/*
 *  Copyright 2014 Niclas Cedermalm
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.dd4tlite.model.impl.xml;

import org.simpleframework.xml.Element;
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
            //@ElementList(entry="ecl", inline=true, type=XmlEclValue.class),
            @ElementList(entry="componentLink", inline=true, type=XmlComponentLink.class),
            @ElementList(entry="embedded", inline=true, type=XmlEmbeddedValue.class),
            @ElementList(entry="empty", inline=true, type=XmlEmptyValue.class)
    })
    private List<XmlValue> values;

    public List<Object> getValues() {

        ArrayList<Object> convertedValues = new ArrayList<>();
        for ( XmlValue value : values ) {
            if ( !(value instanceof XmlEmptyValue) ) {
                convertedValues.add(value.getValue());
            }
        }
        return convertedValues;
    }
}

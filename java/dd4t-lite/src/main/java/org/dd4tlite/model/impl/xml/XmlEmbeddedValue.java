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

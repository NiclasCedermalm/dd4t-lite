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

import org.dd4tlite.model.Component;
import org.dd4tlite.model.Field;
import org.dd4tlite.model.Schema;
import org.simpleframework.xml.ElementList;

import java.util.List;
import java.util.Map;

/**
 * @author nic
 */
public class XmlComponent extends XmlItem implements Component {


    @ElementList (required = false, type=XmlField.class)
    private List<Field> content;


    @Override
    public Schema getSchema() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Field> getContent() {
       return this.content;
    }

    @Override
    public Field getContent(String fieldName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


}

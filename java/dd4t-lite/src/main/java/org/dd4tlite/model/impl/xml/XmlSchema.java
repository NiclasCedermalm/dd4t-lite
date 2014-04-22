/*
 * Copyright 2014 Niclas Cedermalm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dd4tlite.model.impl.xml;

import org.dd4tlite.model.Schema;
import org.simpleframework.xml.Attribute;

/**
 * XmlSchema
 *
 * @author nic
 */
public class XmlSchema extends XmlItem implements Schema {

    @Attribute
    private String rootElementName;

    @Override
    public String getRootElementName() {
        return this.rootElementName;
    }
}

/**
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

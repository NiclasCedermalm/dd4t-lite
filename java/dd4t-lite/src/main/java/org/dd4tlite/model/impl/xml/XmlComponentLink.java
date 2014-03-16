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

import org.dd4tlite.model.Page;
import org.dd4tlite.service.LinkResolver;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.Commit;
import org.simpleframework.xml.core.Validate;

import java.util.Map;

/**
 * XML Component Link
 *
 * @author nic
 */
public class XmlComponentLink implements XmlValue {

    @Text
    private String link;

    private LinkResolver linkResolver;

    private Page page;

    @Commit
    public void initialize(Map map) {
        this.linkResolver = (LinkResolver) map.get(LinkResolver.class);
        this.page = (Page) map.get(Page.class);
    }

    @Override
    public Object getValue() {
        if ( this.linkResolver != null ) {
            return this.linkResolver.resolveLink(this.link, this.page);
        }
        else {
            return this.link;
        }
    }
}

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

import org.dd4tlite.util.DateUtils;
import org.simpleframework.xml.core.Commit;

/**
 * XML XHTML Field
 * @author nic
 */
public class XmlXhtmlField extends XmlText {

    @Commit
    private void convert() throws Exception {
        // TODO: Process the XHTML and resolve links etc


    }
}

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

import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.Commit;

import java.math.BigDecimal;

/**
 * @author nic
 */
public class XmlNumber implements XmlValue {

    @Text
    private String numberString;

    private BigDecimal number;


    @Commit
    private void convert() {
        this.number = new BigDecimal(this.numberString);
    }

    @Override
    public Object getValue() {
        return this.number;
    }
}

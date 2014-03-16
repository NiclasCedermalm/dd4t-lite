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
package org.dd4tlite.model;

import java.util.List;
import java.util.Map;

/**
 * @author nic
 */
public interface Component extends Item {

    public enum ComponentType {
        Normal, Multimedia
    }

    public Schema getSchema();

    /**
     * Get the content
     *
     * @return a map of field objects representing the content
     */
    public List<Field> getContent();

    public Field getContent(String fieldName);

}

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

package org.dd4tlite.freemarker;

import java.util.Map;

/**
 * Region Data.
 * Is used to render different regions (e.g. XPM, SmartTarget etc).
 *
 * @author nic
 */
public class RegionData {

    private String name;
    private String type;
    private final Map<String,String> componentTypes;
    private final int minOccurs;
    private final int maxOccurs;

    // Type + name needs here to execute region macro for XPM + smarttarget
    // <@.vars["test"]

    public RegionData(String name, String type, Map<String, String> componentTypes, int minOccurs, int maxOccurs) {
        this.name = name;
        this.type = type;
        this.componentTypes = componentTypes;
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Map<String, String> getComponentTypes() {
        return componentTypes;
    }

    public int getMinOccurs() {
        return minOccurs;
    }

    public int getMaxOccurs() {
        return maxOccurs;
    }
}

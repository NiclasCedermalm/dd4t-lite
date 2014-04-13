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

import org.dd4tlite.model.ComponentPresentationType;
import org.dd4tlite.model.EditableRegionConstraint;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * XmlEditableRegionConstraint
 *
 * @author nic
 */
public class XmlEditableRegionConstraint implements EditableRegionConstraint {

    @Attribute
    private int minOccurs;

    @Attribute
    private int maxOccurs;

    @ElementList(required = false, type=XmlComponentPresentationType.class)
    private List<ComponentPresentationType> componentPresentationTypes;

    @Override
    public int getMinOccurs() {
        return this.minOccurs;
    }

    @Override
    public int getMaxOccurs() {
        return this.maxOccurs;
    }

    @Override
    public List<ComponentPresentationType> getComponentPresentationTypes() {
        return this.componentPresentationTypes;
    }
}

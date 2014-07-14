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

import org.dd4tlite.model.*;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * XmlRegion
 * @author nic
 */
public class XmlRegion implements Region {

    @Attribute
    private String name;

    @ElementList (required = false, type=XmlComponentPresentation.class)
    private List<ComponentPresentation> componentPresentations = new ArrayList<ComponentPresentation>();

    @Element (required = false)
    private XmlTemplate template;


    @ElementListUnion({
            @ElementList(entry="editableRegionConstraint", inline=false, type=XmlEditableRegionConstraint.class, required = false)
            // Here can other possible regions be specified
    })
    private List<Constraint> constraints;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<ComponentPresentation> getComponentPresentations() {
        return this.componentPresentations;
    }

    @Override
    public Template getTemplate() {
        return this.template;
    }

    @Override
    public List<Constraint> getConstraints() {
        return this.constraints;
    }

    @Override
    public <T extends Constraint> T getConstraint(Class<T> type) {
        for ( Constraint constraint : this.constraints ) {
            for ( Class<?> constraintInterface : constraint.getClass().getInterfaces() ) {
                if (constraintInterface.isAssignableFrom(type)) {
                    return (T) constraint;
                }
            }
        }
        return null;
    }

}

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
import org.dd4tlite.model.ComponentPresentation;
import org.dd4tlite.model.Template;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import java.util.Date;

/**
 * XML Component Presentation
 *
 * @author nic
 */
public class XmlComponentPresentation implements ComponentPresentation {

    // NEEDED? Better to create a new XML element for dynamic CPs: <dynamicComponentPresentation componentId="" templateId=""/>
    @Attribute (required = false)
    private boolean dynamic = false;

    @Element
    private XmlComponent component;
    @Element
    private XmlTemplate template;

    @Override
    public boolean isDynamic() {
        return this.dynamic;
    }

    @Override
    public Template getComponentTemplate() {
       return this.template;
    }

    @Override
    public Component getComponent() {
        return this.component;
    }

}

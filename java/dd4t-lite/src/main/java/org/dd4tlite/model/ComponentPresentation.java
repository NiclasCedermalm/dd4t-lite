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
package org.dd4tlite.model;

/**
 * Component Presentation
 *
 * @author nic
 */
public interface ComponentPresentation {

    // TODO: How much of schema information is needed really?
    // Basically view name + component fields
    // Plus the data needed to generate XPM + be able to query SmartTarget (to limit what component presentations to be shown...)

    public boolean isDynamic();

    public Template getComponentTemplate();

    public Component getComponent();

    public Region getInnerRegion();

    /**
     * The unique identity of the component presentation on the page
     * @return identity
     */
    public String getId();

}

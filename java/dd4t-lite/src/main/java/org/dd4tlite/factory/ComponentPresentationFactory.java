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
package org.dd4tlite.factory;

import org.dd4tlite.model.Component;

/**
 * Component Factory.
 * Interface to query dynamic component presentations.
 *
 * @author nic
 */
public interface ComponentPresentationFactory {


    // TODO: How should this be abstracted? Just component ID and template ID? But I do not know the template ID then...???
    // If I know the template Id I might already know the view name? Or...??
    // Comp Id + Template Id maybe make more sense when having dynamic components on a page

    public Component getComponentPresentation(String componentId);
}

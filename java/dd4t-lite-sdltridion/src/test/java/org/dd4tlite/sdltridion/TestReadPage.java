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
package org.dd4tlite.sdltridion;

import com.tridion.data.*;
import com.tridion.storage.StorageManagerFactory;
import com.tridion.storage.StorageTypeMapping;
import com.tridion.storage.dao.PageDAO;
import org.dd4tlite.factory.PageFactory;
import org.dd4tlite.model.*;
import org.dd4tlite.sdltridion.factory.SDLTridionBrokerPageFactory;
import org.junit.Test;

import java.util.Collection;

/**
 * TestReadPage
 *
 * @author nic
 */
public class TestReadPage {

    @Test
    public void testReadPageFromBroker() throws Exception {

        PageFactory pageFactory = new SDLTridionBrokerPageFactory(5);
        Page page = pageFactory.getPage("/dd4t_lite_test.html");
        System.out.println("Page ID: " + page.getId());
        System.out.println("Path: " + page.getPath());
        System.out.println("Filename: " + page.getFilename());
        System.out.println("Page view name: " + page.getPageTemplate().getViewName());
        System.out.println("Regions:");
        for ( Region region : page.getRegions() ) {
            System.out.println("Region: " + region.getName());
            for (ComponentPresentation cp : region.getComponentPresentations() ) {
                System.out.println("Component ID: " + cp.getComponent().getId());
                System.out.println("Component view name: " + cp.getComponentTemplate().getViewName());
                System.out.println("Fields:");
                for ( Field field : cp.getComponent().getContent() ) {
                    System.out.println("Field name=" + field.getName() + ", type=" + field.getType());
                    for ( Object value: field.getValues() ) {
                        System.out.println("Value: " + value);
                    }
                }
            }
        }
    }

}

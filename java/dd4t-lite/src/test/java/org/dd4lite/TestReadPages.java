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
package org.dd4lite;

import org.dd4tlite.factory.PageFactory;
import org.dd4tlite.factory.rawxml.RawXmlPageFactory;
import org.dd4tlite.model.ComponentPresentation;
import org.dd4tlite.model.Field;
import org.dd4tlite.model.Page;
import org.dd4tlite.model.Region;
import org.dd4tlite.service.LinkResolver;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author nic
 */
public class TestReadPages {

    private PageFactory pageFactory;

    static class TestLinkResolver implements LinkResolver {

        @Override
        public String resolveLink(String id, Page fromPage) {
            return "http:/" + fromPage.getPath() + "/link.to." + id + ".html";
        }
    }

    @Before
    public void initialize() {
        HashMap<Class<?>,Object> dependencies = new HashMap<>();
        dependencies.put(LinkResolver.class, new TestLinkResolver());
        this.pageFactory = new RawXmlPageFactory("src/test/resources", dependencies);
    }

    @Test
    public void testReadPage1() throws Exception {
        Page page = this.pageFactory.getPage("/testPages/test_page1.xml");
        System.out.println("Page ID: " + page.getId() + " Title: " + page.getTitle());
        System.out.println("Revision Date: " + page.getRevisionDate());
        System.out.println("Path: " + page.getPath());
        System.out.println("Filename: " + page.getFilename());
        System.out.println("Regions:");
        for ( Region region : page.getRegions() ) {
            System.out.println("Region: " + region.getName());
            System.out.println("Region view name: " + region.getTemplate().getViewName());
            for (ComponentPresentation cp : region.getComponentPresentations() ) {
                System.out.println("Is dynamic: " + cp.isDynamic());
                System.out.println("View name: " + cp.getComponentTemplate().getViewName());
                System.out.println("Component ID: " + cp.getComponent().getId());
                for ( Field field : cp.getComponent().getContent() ) {
                    System.out.println("Field name=" + field.getName() + ", type=" + field.getType() + ", is multivalue=" + field.isMultiValue());
                    for ( Object value: field.getValues() ) {
                        System.out.println("Value: " + value);
                    }
                }
                System.out.println("Component Metadata:");
                for ( Field field : cp.getComponent().getMetadata() ) {
                    System.out.println("Field name=" + field.getName() + ", type=" + field.getType());
                    for ( Object value: field.getValues() ) {
                        System.out.println("Value: " + value);
                    }
                }
                System.out.println("Component Template Metadata:");
                for ( Field field : cp.getComponentTemplate().getMetadata() ) {
                    System.out.println("Field name=" + field.getName() + ", type=" + field.getType());
                    for ( Object value: field.getValues() ) {
                        System.out.println("Value: " + value);
                    }
                }
            }
        }
        System.out.println("Page view name: " + page.getPageTemplate().getViewName());
        System.out.println("Page metadata:");
        for ( Field field : page.getMetadata() ) {
            System.out.println("Field name=" + field.getName() + ", type=" + field.getType());
            for ( Object value: field.getValues() ) {
                System.out.println("Value: " + value);
            }
        }
    }
}

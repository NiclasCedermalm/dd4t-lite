package org.dd4lite;

import org.dd4tlite.factory.PageFactory;
import org.dd4tlite.factory.org.dd4tlite.factory.impl.RawXmlPageFactory;
import org.dd4tlite.model.ComponentPresentation;
import org.dd4tlite.model.Field;
import org.dd4tlite.model.Page;
import org.dd4tlite.model.Region;
import org.junit.Before;
import org.junit.Test;

/**
 * @author nic
 */
public class TestReadPages {

    private PageFactory pageFactory;

    @Before
    public void initialize() {
        this.pageFactory = new RawXmlPageFactory("src/test/resources");
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
                    System.out.println("Field name=" + field.getName() + ", type=" + field.getType());
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

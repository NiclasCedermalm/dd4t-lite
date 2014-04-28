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

import org.dd4tlite.model.*;
import org.dd4tlite.util.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Freemarker Render Helper
 *
 * @author nic
 */
public class FreemarkerRenderHelper {


    public String renderRegion(Region region, String layout) {


        // TODO: Always use <@region for regions and then use layout to control the layout (which the viewname of the region template)?

        StringBuilder sb = new StringBuilder();

        String regionViewName = "region";
        if ( region.getTemplate() != null ) {
            regionViewName = region.getTemplate().getViewName();
        }
        sb.append("<@");
        sb.append(regionViewName);
        sb.append(" name=\"");
        sb.append(region.getName());
        sb.append("\" layout=\"");
        sb.append(layout);
        sb.append("\"");

        EditableRegionConstraint xpmConstraint = region.getConstraint(EditableRegionConstraint.class);
        if ( xpmConstraint != null ) {
            sb.append(" minOccurs=");
            sb.append(xpmConstraint.getMinOccurs());
            sb.append(" maxOccurs=");
            sb.append(xpmConstraint.getMaxOccurs());
            sb.append(" componentTypes={");
            List<ComponentPresentationType> cpTypes = xpmConstraint.getComponentPresentationTypes();
            for (int i = 0; i < cpTypes.size(); i++) {
                ComponentPresentationType cpType = cpTypes.get(i);
                sb.append("\"");
                sb.append(cpType.getSchemaId());
                sb.append("\": \"");
                sb.append(cpType.getTemplateId());
                sb.append("\"");
                if (i < cpTypes.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append("}");
        }
        sb.append(">");

        for ( ComponentPresentation cp : region.getComponentPresentations() ) {
            sb.append(this.renderComponentPresentation(cp));
        }
        sb.append("</@");
        sb.append(regionViewName);
        sb.append(">");

        return sb.toString();
    }

    public String renderInnerRegion(Page page, String componentId, String layout) {

        for ( ComponentPresentation cp : page.getComponentPresentations() ) {
            if ( cp.getComponent().getId().equals(componentId) ) {
                Region innerRegion = cp.getInnerRegion();
                if ( innerRegion != null ) {
                    return renderRegion(innerRegion, layout);
                }
            }
        }

        return "";
    }

    public String renderComponentPresentation(ComponentPresentation componentPresentation) {

        StringBuilder sb = new StringBuilder();

        sb.append("<@component metadata=");
        sb.append("{\"ComponentID\": \"");
        sb.append(componentPresentation.getComponent().getId());
        if ( componentPresentation.getComponent().getTitle().startsWith("ecl:") ) { // If a ECL component
            sb.append("\", \"EclID\": \"");
            sb.append(componentPresentation.getComponent().getContent("eclId").getValues().iterator().next());
        }
        sb.append("\", \"ComponentModified\": \"");
        sb.append(this.date2xml(componentPresentation.getComponent().getRevisionDate()));
        sb.append("\", \"ComponentTemplateID\": \"");
        sb.append(componentPresentation.getComponentTemplate().getId());
        sb.append("\", \"ComponentTemplateModified\": \"");
        sb.append(this.date2xml(componentPresentation.getComponentTemplate().getRevisionDate()));
        sb.append("\", \"IsRepositoryPublished\": false, \"ContentName\": \"");
        sb.append(componentPresentation.getComponent().getSchema().getRootElementName());
        sb.append("\"}>\n");
        sb.append("<@managedComponent>");
        sb.append("<@");
        sb.append(componentPresentation.getComponentTemplate().getViewName());

        for (Field field : componentPresentation.getComponent().getContent()) {
            if (!field.isMultiValue() && field.getValues().size() == 0) {
                continue;
            }
            if (field.getType() != Field.FieldType.Xhtml) {
                sb.append(" ");
                sb.append(field.getName());
                sb.append("=");
                sb.append(this.renderFieldValue(field));
            }
        }
        // If metadata values
        //
        if ( componentPresentation.getComponent().getMetadata() != null) {

            // Output metadata fields as well
            //
            for (Field field : componentPresentation.getComponent().getMetadata()) {
                if (!field.isMultiValue() && field.getValues().size() == 0) {
                    continue;
                }
                if (field.getType() != Field.FieldType.Xhtml) {
                    sb.append(" ");
                    sb.append(field.getName());
                    sb.append("=");
                    sb.append(this.renderFieldValue(field));
                }
            }
        }

        sb.append(">\n");
        for (Field field : componentPresentation.getComponent().getContent()) {
            if (!field.isMultiValue() && field.getValues().size() == 0) {
                continue;
            }
            if (field.getType() == Field.FieldType.Xhtml) {
                sb.append("<@param name=\"");
                sb.append(field.getName());
                sb.append("\">\n");
                sb.append(field.getValues().get(0)); // TODO: Handle multi-value case here!!!
                sb.append("\n</@param>\n");
            }
        }

        sb.append("</@");
        sb.append(componentPresentation.getComponentTemplate().getViewName());
        sb.append(">\n");
        sb.append("</@managedComponent></@component>\n");

        // TODO: Make it cleaner with all managed components & components. Too much cake on cake...

        return sb.toString();
    }

    public String renderFieldValue(Field field) {

        StringBuilder sb = new StringBuilder();

        if ( field.isMultiValue() ) {
            sb.append("[");
            for ( int i=0; i < field.getValues().size(); i++ ) {
                Object value = field.getValues().get(i);
                renderSingleFieldValue(field, value, sb);
                if ( i < field.getValues().size()-1) {
                    sb.append(",");
                }
            }
            sb.append("]");
        }
        else {
            if ( field.getValues().size() > 0 ) {
                Object value = field.getValues().iterator().next();
                renderSingleFieldValue(field, value, sb);
            }
        }
        return sb.toString();
    }

    private void renderSingleFieldValue(Field field, Object value, StringBuilder sb) {
        if ( field.getType() == Field.FieldType.Number ) {
            sb.append(value);
        }
        else if ( field.getType() == Field.FieldType.Date ) {
            sb.append(date2xml((Date) value )); // TODO: How to render the date fields?? As objects?
        }
        else {
            sb.append("\"");
            sb.append(value);
            sb.append("\"");
        }
    }

    public String date2xml(Date date) {
        return DateUtils.date2xml(date);
    }

}

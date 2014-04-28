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

import freemarker.core.Environment;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateModelException;

import java.util.*;

/**
 * TemplateHelper
 *
 * @author nic
 */

public class TemplateHelper {

    private Map<String, List<Template>> templates = new HashMap<String, List<Template>>();
    //private String currentRegion = null;
    private Map<String, Object> templateBodyParameters = null;
    private Stack<Map<String,Object>> templateBodyParametersStack = new Stack<Map<String, Object>>();
    private Stack<Map<String,Object>> componentMetadataStack = new Stack<>();
    private static DefaultObjectWrapper objectWrapper = new DefaultObjectWrapper();


    public void setBodyParameter(String name, Object value) throws TemplateModelException {
        if ( this.templateBodyParameters == null ) {
            this.templateBodyParameters = new HashMap<String,Object>();
        }
        this.templateBodyParameters.put(name, value);
    }

    public Object getBodyParameter(String name) {
        if ( this.templateBodyParameters != null ) {
            return this.templateBodyParameters.get(name);
        }
        return null;
    }

    public void applyBodyParameters() throws TemplateModelException {
        if ( this.templateBodyParameters != null ) {
            for ( String name : this.templateBodyParameters.keySet() ) {
                Environment.getCurrentEnvironment().setLocalVariable(name, objectWrapper.wrap(this.templateBodyParameters.get(name)));
            }
            this.templateBodyParameters.clear();
            this.templateBodyParameters = null;
        }
    }

    public void pushBodyParameters() {
        this.templateBodyParametersStack.push(this.templateBodyParameters);
        this.templateBodyParameters = null;
    }

    public void popBodyParameters() {
        this.templateBodyParameters = this.templateBodyParametersStack.pop();
    }

    public void setRegion(String name) throws TemplateModelException {
        //currentRegion = name;
        this.setCurrentRegion(name);
        this.setBodyParameter(name, new ArrayList<Object>());
    }

    protected void setCurrentRegion(String name) throws TemplateModelException {
        this.setBodyParameter("__current_region__", name);
    }

    protected String getCurrentRegion() {
        return (String) this.getBodyParameter("__current_region__");
    }

    public void setRegionData(String type,
                              Map<String,String> componentTypes,
                              int minOccurs,
                              int maxOccurs) throws TemplateModelException {
        String currentRegion = this.getCurrentRegion();
        this.setBodyParameter(currentRegion + "_data",
                new RegionData(currentRegion, type, componentTypes, minOccurs, maxOccurs));
    }

    public List<Object> getComponents(String region) {
        return (List<Object>) this.templateBodyParameters.get(region);
    }

    public RegionData getRegionData(String region) {
        String currentRegion = this.getCurrentRegion();
        return (RegionData) this.templateBodyParameters.get(currentRegion + "_data");
    }

    // TODO: Refactor and make the code cleaner...

    public void addComponent(Object component) throws TemplateModelException {
        if ( component == null || (component instanceof String && ((String) component).trim().isEmpty()) ) {
            // Ignore adding empty components to the region
            //
            return;
        }
        String currentRegion = this.getCurrentRegion();
        if ( currentRegion == null ) {
            this.setRegion("main");
        }

        RegionData regionData = (RegionData) this.templateBodyParameters.get(currentRegion + "_data");
        List<Object> regionList = (List<Object>) this.templateBodyParameters.get(currentRegion);
        if ( regionData.getMaxOccurs() == -1 || regionList.size() < regionData.getMaxOccurs() ) {
            regionList.add(component);
        }
    }

    public void pushComponentMetadata(Map<String,Object> metadata) {
        this.componentMetadataStack.push(metadata);
    }

    public void popComponentMetadata() {
        this.componentMetadataStack.pop();
    }

    public Map<String,Object> getComponentMetadata() {
        return this.componentMetadataStack.peek();
    }



}

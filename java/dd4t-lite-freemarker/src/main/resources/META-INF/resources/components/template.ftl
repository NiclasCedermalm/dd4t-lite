
<#--
   Macro for defining body parameters
-->
<#macro param name>
    <#local value>
        <@managedField name=name>
            <#nested>
        </@managedField>
    </#local>
    ${template.setBodyParameter(name, value)}
</#macro>

<#--
    Macro for defining page regions
    TODO: Improve regions to have more automatic support for region data so it is not needed to be
          managed manually
-->
<#macro region name componentTypes={} minOccurs=-1 maxOccurs=-1 layout=null>
    ${template.setRegion(name)}
    ${template.setRegionData('xpmRegion', componentTypes, minOccurs, maxOccurs)}
    <#nested>
    <#if layout != null>
        <@.vars[layout] components=template.getComponents(name) regionData=template.getRegionData(name)/>
    </#if>
</#macro>

<#macro bundledRegion name layout>

</#macro>

<#--
    Macro for defining a managed page region. Is used by components to define
    where the managed region area are.
-->
<#macro managedRegion regionData>
    <#if regionData == null>
        <#nested>
    <#else>
        <@.vars[regionData.type] regionData=regionData>
            <#nested>
        </@>
    </#if>
</#macro>

<#macro managedComponent>
    <#-- TODO: Decouple this further to support XPM/SmartTarget variations -->
    <#local metadata = template.getComponentMetadata()/>
    <#-- TODO: Have the XPM enable/disable configurable -->
    <#if metadata?size gt 0>
        <#if metadata["EclID"]??>
            <#local componentId = metadata["EclID"]>
        <#else>
            <#local componentId = metadata["ComponentID"]>
        </#if>
        <span>
            <!-- Start Component Presentation: {"ComponentID" : "${componentId}", "ComponentModified" : "${metadata["ComponentModified"]}", "ComponentTemplateID" : "${metadata["ComponentTemplateID"]}", "ComponentTemplateModified" : "${metadata["ComponentTemplateModified"]}", "IsRepositoryPublished" : ${metadata["IsRepositoryPublished"]?c} } -->
            <#nested>
        </span>
    <#else>
        <#nested>
    </#if>
</#macro>

<#macro managedField name>
    <#local metadata = template.getComponentMetadata()/>
    <#-- TODO: Have the XPM enable/disable configurable -->
    <#if metadata?size gt 0>
        <span>
            <!-- Start Component Field: {"XPath":"tcm:Content/custom:${metadata["ContentName"]}/custom:${name}[1]"} -->
            <#nested>
        </span>
    <#else>
        <#nested>
    </#if>

</#macro>

<#--
    Macro for defining a region manageable via XPM
-->
<#macro xpmRegion regionData>
 <#-- TODO: Have the XPM enable/disable configurable -->

 <!-- Start Region: {
  title: "${regionData.name}",
  allowedComponentTypes: [
    <#list regionData.componentTypes?keys as schemaUri>
        <#if schemaUri_index gt 0>,</#if>
        { schema: "${schemaUri}", template: "${regionData.componentTypes[schemaUri]}" }
    </#list>
    ]
    <#if regionData.minOccurs gt -1>
        ,minOccurs: ${regionData.minOccurs}
    </#if>
     <#if regionData.maxOccurs gt -1>
        ,maxOccurs: ${regionData.maxOccurs}
    </#if>
   }
   -->
 <#nested>
</#macro>

<#--
    TODO: Add schemaId and mappings as attributes here. This so XPM markup can be generated for non-RTF fields

<#--
    Macro for defining a component in a region or directly on a page
-->
<#macro component metadata={}>
    ${template.pushBodyParameters()}
    <#local value>
        ${template.pushComponentMetadata(metadata)}
        <#--<#attempt>-->
        <#nested>
        <#--#recover>
        COMPONENT FAILED. Error: ${.error}
        </#attempt-->
    ${template.popComponentMetadata()}
    </#local>
    ${template.popBodyParameters()}
    ${template.addComponent(value)}
</#macro>

<#--
    Definition for null
-->
<#assign null="__NULL__" />

<#----------------------------------------------------
 #  DD4T REGION
 #  - Renders a specific region read from DD4T
 #---------------------------------------------------->

<#macro dd4tRegion name layout>
   <#local pageRegion=dd4tPage.getRegion(name)>
   <#assign renderedRegion = dd4t.renderRegion(pageRegion, layout)?interpret>
   <@renderedRegion/>
</#macro>


<#----------------------------------------------------
 #  DD4T INNER REGION
 #  - Renders an inner region read from DD4T
 #---------------------------------------------------->
<#macro dd4tInnerRegion layout>
    <#local metadata = template.getComponentMetadata()/>
    <#assign renderedRegion = dd4t.renderInnerRegion(dd4tPage, metadata["ComponentID"], layout)?interpret>
    <@renderedRegion/>
</#macro>


<#-- TODO: Make a macro for DD4T component presentations as well -->
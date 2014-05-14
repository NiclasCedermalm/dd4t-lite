
DD4T Lite
=============

DD4T Lite is lightweight version of [DD4T](https://code.google.com/p/dynamic-delivery-4-tridion) with minimal dependencies. It provides a similar but simplified version of the DD4T model.
DD4T Lite is designed to be fully decoupled from SDL Tridion (compile time anyway), so it will be easy to unit test etc.
The idea of DD4T Lite is to focus on the core mechanism of DD4T which basically is being a ORM framework. DD4T is often positioned as MVC framework, but I believe the
power of DD4T first of all is to provide an nice abstraction to published content. So that has been my focus when developing the DD4T Lite framework plus simplify concepts and
decouple from 3PP dependencies as much as possible.

Another weak spot of DD4T is the extremely verbose XML format, which tends to be hideous for large pages. So this I have improving as well to minimize the information needed
published from SDL Tridion CMS. Right now DD4T Lite has only support for XML, but JSON etc is easy to add-on later. Of simplicity the XML implementation is part of the core project, but it
will be refactored into a seperate module when the framework grows.
DD4T Lite is currently only implemented in Java, but a C# version
will be implemented as soon I got time (or if I got volunteers to do that :-) ).

DD4T Lite supports different page region models. Right now the "component based region"-model is implemented where regions are first-class citizens on
a SDL Tridion page. Template based regions (i.e. when having metadata on templates to indicate what region a certain template belongs to) will be supported soon. The big advantage of
using component based regions is that is much easier to reuse component templates to be used in other regions and provide a more flexible approach of using page regions.
The advantage of having regions as components is that you open up the possibility to manage the region as an entity.
It allows you to:

 * Control look & feel of a region (by using component templates)
 * Define constraints, such as configure what component types allowed in the region (used for XPM)
 * Annotate regions with metadata to control behaviour etc
 * Easily configure if the region is a pure XPM region or a SmartTarget region

This unleash some new interesting capabilities to page regions, which can be handy in more complex setups when for example doing container containers (e.g. a carousel).
There is also an TCM extension to SDL Tridion that make sure that added components is added to the correct region (based on its contraints). This can sometimes be a problem
when using XPM where XPM do not know where to put the component on the page (often when it is the first component in a region). The extension triggers on page save events.

CMS Setup
----------

1. Create a folder in your SDL Tridion instance where you want to store your DD4T Lite template building blocks
2. Copy files to cms/lib & cms/upload according to the missingfiles.txt files
3. Setup TcmAssembly to your Tridion instance
4. Open the Visual Studio project under cms/dd4t-lite-building-blocks
5. Setup the post-build event to use your directory for DD4T Lite templates
6. Compile the code (ctrl-shift-B in Visual Studio and make sure that the C# building blocks are created under your specified folder
7. Create a parameter schema called 'DD4T Lite Template Parameters' (or something else) with one mandatory text parameter:
    - viewName (maps to an ASPX/Razor/JSP/JSF/Freemarker/etc view)
8. Assign all C# DD4T Lite TBBs (Page, Region, Component) with that parameter schema
9. For each component template you need at least the following TBBs:
    - DD4T Lite Component Template
    - Extract Binaries from Html
    - Default Finish Actions
10. For each page template you need at least the following TBBs:
    - Extract Components from Page
    - DD4T Lite Page Template
    - Default Finish Actions

Creating Component Templates
-----------------------------
T.B.D.

Creating Page Templates
--------------------------
T.B.D.

Using "Component Based Regions"
----------------------------------

Component based regions is a new concept that allows regions to be first class citizens on a Tridion page. The region is normal Tridion components
are are used as markers to indicate which components that belongs to a specific region. A region can have its own look&feel and behaviour, example box, carousel etc.
This is controlled by the template associated with the region component.
To the region constraints can be defined. Normally it is constraints for Tridion Experience manager, i.e. what component types (schema+template) that are allowed in the region and min/max occurs.

To set this up the following new schemas are needed be created:

 * DD4T Lite Component Type - Embedded schema with root element name: ComponentType. Fields:
    - schemaId - mandatory number
    - templateId - mandatory number
    - description - optional text
 * DD4T Lite Region. It needs the following fields:
    - name - mandatory text
    - minOccurs - optional number
    - maxOccurs - optional number
    - componentTypes - optional embedded schema of type 'DD4T Lite Component Type. Allow multiple values

Setup region component templates for each region type (normally you only need one). Basically they only need one CT: DD4T Lite Region Template.
Then it is just to add the components below the different region components on the page.
All components under a region marker (until a new marker is found) will be placed in the region.

### Inner Regions

Inner regions are also supported. This gives the possibility for components to acts as "containers" for other components. The component can
define an inner region where components can be placed. To set this up, you need to do the following steps:

1. Define a new metadata schema called 'DD4T Lite Component Template Metadata' (or similar). It needs at least the following field:
    - innerRegion - component link to a 'DD4T Lite Region'
2. Setup region settings for the inner region
3. Use the metadata schema for the component templates for those component presentations that will act as containers
4. Place the components under the container component on the Tridion page.
5. All components that matches the region contraints will be added to the inner region

Using "Template Based Regions"
----------------------------------
T.B.D.


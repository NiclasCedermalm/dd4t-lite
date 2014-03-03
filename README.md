
DD4T Lite
=============

DD4T Lite is leightweight version of DD4T with minimal dependencies.


CMS Setup
----------

1. Create a folder in your SDL Tridion instance where you want to store your DD4T Lite template building blocks
2. Copy files to cms/lib & cms/upload according to the missingfiles.txt files
3. Setup TcmAssembly to your Tridion instance
4. Open the Visual Studio project under cms/dd4t-lite-building-blocks
5. Setup the post-build event to use your directory for DD4T Lite templates
6. Compile the code (ctrl-shift-B in Visual Studio and make sure that the C# building blocks are created under your specified folder
7. Create a parameter schema called 'DD4T Lite Template Parameters' (or something else) with one mandatory text parameter:
    - viewName
8. Assign all C# TBBs with that parameter schema

Creating Component Templates
-----------------------------
T.B.D.

Creating Page Templates
--------------------------
T.B.D.

Using "Component Based Regions"
----------------------------------
T.B.D.

Using "Template Based Regions"
----------------------------------
T.B.D.


WebSiteAnalyser is used to extract below mentioned fields using jsoup library and create the html table
 filled with all the retreived values.
 Following fields are retreived from html document:
  1. Html version
  2. Page title
  3. No of headings in html document
  4. internal, external and inaccesible links
  5. Check if html doc page contain loginForm or not.


 
Steps to generate new war file:

1. Import the project in eclipse by selecting File->Import->Exisiting Maven Projects
2. Right click on the Projec, Run as -> Maven Clean
3. Right click on the Projec, Run as -> Maven Install (This command tells Maven to build all the modules, and to install it in the local repository.)
4. Look in the target subdirectory, you should find the build output(WebSiteAnalyser.war) and the final library or application that was being built.



Execute the WebSiteAnalyser.war file in tomcat:
1. Put the WebSiteAnalyser.war file in tomcat webapps directory 
2. Start the tomcat
3. Goto  http://localhost:8080/WebSiteAnalyser/
4. Enter the Url in the textarea and press Send button
5. Result will be displayed in http://localhost:8080/WebSiteAnalyser/urlAnalyser
6. Use back button or http://localhost:8080/WebSiteAnalyser/ to check other Url
7. Images of sreenshots are also added(WebAnalyser1.PNG, WebAnalyser2.PNG).


Assumptions/Decision:
1. Doctype string is extracted as Html version which may in some cases dont contain Html version.
2. External links are counted by reading a[href] tags.
3. Internal links are counted by reading link[href] tags. 
4. Broken external links are counted as Inaccessible links.
5. [src] links are not taken into account as internal links due to unclear requirement.
6. Broken [src] links also not taken into account as internal links due to unclear requirement.
7. Login form elements contain the keyword login.

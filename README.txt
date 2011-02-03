(c) 2009-2010 Mads Mohr Christensen

This program shipps with a custom appassembler-maven-plugin.
http://jira.codehaus.org/browse/MAPPASM-88

Icons by Mark James, http://www.famfamfam.com/lab/icons/silk/

How to build:

* First you need to install java6 and maven2.
* If you need to run the webservice and results web-app then you would also need to install mysql5 and a servlet container like tomcat6.
* Then you need to configure you maven2 installation: Use the supplied resources/settings.xml as inspiration.
* Next you need to install the custom appassembler-maven-plugin: run the install.sh script in uploader/lib
* Then build it using mvn package.
* Create the database: resources/wca_live_dev.sql.
* Deploy the two web-apps.

That's basically it.

Questions can be sent to me at hr.mohr@gmail.com

Enjoy!

#!/bin/sh
mvn install:install-file -DgroupId=org.codehaus.mojo.appassembler -DartifactId=appassembler -Dversion=1.1-SNAPSHOT -Dpackaging=pom -Dfile=appassembler-1.1-SNAPSHOT.pom

mvn install:install-file -DgroupId=org.codehaus.mojo.appassembler -DartifactId=appassembler-booter -Dversion=1.1-SNAPSHOT -Dpackaging=jar -Dfile=appassembler-booter-1.1-SNAPSHOT.jar -DpomFile=appassembler-booter-1.1-SNAPSHOT.pom

mvn install:install-file -DgroupId=org.codehaus.mojo.appassembler -DartifactId=appassembler-model -Dversion=1.1-SNAPSHOT -Dpackaging=jar -Dfile=appassembler-model-1.1-SNAPSHOT.jar -DpomFile=appassembler-model-1.1-SNAPSHOT.pom

mvn install:install-file -DgroupId=org.codehaus.mojo -DartifactId=appassembler-maven-plugin -Dversion=1.1-SNAPSHOT -Dpackaging=jar -Dfile=appassembler-maven-plugin-1.1-SNAPSHOT.jar -DpomFile=appassembler-maven-plugin-1.1-SNAPSHOT.pom

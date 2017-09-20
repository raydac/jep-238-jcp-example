Since JDK 9 there is risk that the new versions of JDK will not be fully compatible on level of API with old versions, so that [JEP-238 "Multi-Release JAR Files"](http://openjdk.java.net/jeps/238) was implemented in JDK.   
Hervé Boutemy [made nice example how to build a maven project with support of JEP-238](https://github.com/hboutemy/maven-jep238) but how to be with cases when we need make small changes in classes between JDKs? To keep separate class for each JDK is direct way to forget sometime to make changes or fix in all classes and all in all it breaks DRY principle.  
Many years ago we already had similar issue with J2ME when we had to make small changes in classes for API provided by different vendors, for such purposes I developed [Java Comment Preprocessor](https://github.com/raydac/java-comment-preprocessor) which can work with Maven. The Preprocessor keeps its directives inside commentaries.

I have written the example to show how to organize maven project which build JAR file supports JEP-238 without duplication classes.   
The Maven project contains two modules:
* The `main` module contains all project sources and tests marked with JCP directives.
* The `java9` module contains only pom.xml file with special parameters for preprocessing.   
The Project uses [the Maven toolchains plugin](http://maven.apache.org/plugins/maven-toolchains-plugin/) and `toolchains.xml` must be created in `.m2` folder and must provide SDKs for JDK 8 and JDK 9
```xml
<?xml version="1.0" encoding="UTF8"?>
<toolchains>
  <toolchain>
     <type>jdk</type>
     <provides>
         <version>8</version>
         <vendor>oracle</vendor>
         <id>default</id>
     </provides>
     <configuration>
        <jdkHome>/home/path/to/jdk1.8</jdkHome>
     </configuration>
  </toolchain>
  <toolchain>
    <type>jdk</type>
    <provides>    
	<version>9</version>
        <vendor>oracle</vendor>
	<id>default</id>
    </provides>
    <configuration>
	<jdkHome>/home/path/to/jdk1.9</jdkHome>
    </configuration>
  </toolchain>
</toolchains>
```
The Resulted JAR `multiversion.jar` will be placed in root target folder and can be started with just `java -jar multiversion.jar`
For JDK 8 it should show something like the text
```
Hello Good Old Java!
OnlyJava9Class is not in scope
```
And for JDK 9 and great it should show something like that
```
Hello New Java 9+181 !
OnlyJava9Class is in scope
```
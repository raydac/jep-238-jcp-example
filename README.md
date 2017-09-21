Since JDK 9 we have real risk that the new versions of JDK will be less and less compatible each other for small changes in API and it will hurt developers more and more (because Oracle is planning to make new releases of JDK much often). As a some solution for the problem, the Java community provided [JEP-238 "Multi-Release JAR Files"](http://openjdk.java.net/jeps/238) which has been implemented since JDK 9. It allows combine versions of classes for different JDK  in the same JAR and JVM will be using apropriate versions in runtime. Classes are saved in special subfolders inside `META-INF` folder of JAR file so that it works transparently but we still have the main problem with duplication of business logic in many copies of classes during development phase.   

Hervé Boutemy [made nice example how to bring support of JEP-238 in a maven project](https://github.com/hboutemy/maven-jep238), his approach provides severa modules each of them contains classes for its version JDK and then they collected into single multi-release JAR, but how to be with cases when we need only small changes in classes? To keep separate class for each JDK is too expensive and it is very easy to forget make some important changes in all versions, it breaks DRY principle.  

Many years ago I already run into similar problem with incompatibility of "standard" API implemented by different vendors, it was with J2ME platform, I resolved the problem through development of special tool [Java Comment Preprocessor](https://github.com/raydac/java-comment-preprocessor) (which at present can work with Maven as a plugin). The Preprocessor keeps its directives inside commentaries and allows to inject small changes into classes without full duplication of code. (for instance team of [Postgres-JDBC driver](https://github.com/pgjdbc/pgjdbc) uses this preprocessor).

I have written the example to show how to organize maven project which build a multiversion JAR file which supports JEP-238 but without duplication of classes for preprocessing of same classes for different conditions (for JDK 8 and JDK 9 in the example).   
The Maven project contains two modules:
* The `main` module contains all project sources and tests marked with JCP directives.
* The `java9` module contains only pom.xml file with special parameters for preprocessing and links to source folders in the `main` project.
   
The Project uses [the Maven toolchains plugin](http://maven.apache.org/plugins/maven-toolchains-plugin/) and `toolchains.xml` file should be created in `.m2` folder to provide desired paths to JDK 8 and JDK 9
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
And for JDK 9 and greater it should show something like that
```
Hello New Java 9+181 !
OnlyJava9Class is in scope
```

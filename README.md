Since JDK 9 we have real risk that the new versions of JDK will be less and less compatible each other for small changes in API and it will hurt developers more and more (because Oracle is planning to make new releases of JDK much often). As a some solution for the problem, the Java community provided [JEP-238 "Multi-Release JAR Files"](http://openjdk.java.net/jeps/238) which has been implemented since JDK 9. It allows combine versions of classes for different JDKs in single JAR file and JVM will choose apropriate versions of classes in runtime. Classes are saved in special sub-folders inside `META-INF` so that it works transparently but we still have the main problem with duplication of business logic in many copies of classes during development phase.   

Hervé Boutemy [made just nice example how to bring support of JEP-238 in a maven project](https://github.com/hboutemy/maven-jep238), his approach is to create multi-module project and split JDK dependent classes between modules and then collect compiled classes from each module into single multi-release JAR, but how to be with cases when we need only small changes in classes? To keep separate class for each JDK is too expensive and it is very easy to forget make some important changes in all versions, it breaks DRY principle.  

Many years ago I already run into similar problem with incompatibility of "standard" API implemented by different vendors, it was with J2ME platform, I resolved the problem through development of special tool [Java Comment Preprocessor](https://github.com/raydac/java-comment-preprocessor) (which at present can work with Maven as a plugin). The Preprocessor keeps its directives inside commentaries and allows to inject small changes into classes without full duplication of code. (for instance team of [Postgres-JDBC driver](https://github.com/pgjdbc/pgjdbc) uses this preprocessor).

I have written the example to show how to organize maven project which build a multi-version JAR file which supports JEP-238 but without duplication of classes because it makes preprocessing of set of the same classes but with different parameters under different JDKs (for JDK 8 and JDK 9 in the example). On start the example was a multi-module one but then I decided to improve it and made optimization to make single-module project, so that now it is just a maven single module project generating single JAR as artifact. I have writte comments inside pom.xml files and hope they are no so complex for understanding.   

__NB! The Project uses `maven-invoker-plugin` which sensetive to Maven home folder and may not work with bundled versions of Maven in some IDEs, in the case you should use external non-bundled version of Maven!__

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
	<version>11</version>
        <vendor>oracle</vendor>
	<id>default</id>
    </provides>
    <configuration>
	<jdkHome>/home/path/to/jdk11</jdkHome>
    </configuration>
  </toolchain>
</toolchains>
```

The Project has predefined default build goals so that you can just use `mvn` to build it. As theresult there will be `jep-238-jcp-example-1.0.0-SNAPSHOT.jar` in the target folder, it is a multi-version JAR file which can be executed under JDK 8 and JDK 9 and JVM will be using different classes in each case. The JAR has defined main class name in its manifest so that it can be started in command line with easy command  
`java -jar jep-238-jcp-example-1.0.0-SNAPSHOT.jar`

After start under JDK 8 it should show something like the text
```
Hello Good Old Java!
Class uses new JDK9 API is not in scope
```
And after start under JDK 9 and newer, it should show something like that
```
Hello New Java 9+181 !
Class uses new JDK9 API is in scope
```

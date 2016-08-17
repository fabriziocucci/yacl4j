[![Maven Central](https://img.shields.io/maven-central/v/com.github.fabriziocucci/yacl4j.svg)](http://search.maven.org/#search|ga|1|com.github.fabriziocucci.yacl4j)
[![License](https://img.shields.io/github/license/fabriziocucci/yacl4j.svg)](https://github.com/fabriziocucci/yacl4j/blob/master/LICENSE)
[![Travis CI](https://img.shields.io/travis/fabriziocucci/yacl4j/master.svg)](https://travis-ci.org/fabriziocucci/yacl4j)

# yacl4j
**yacl4j** is a configuration library for Java highly inspired by [cfg4j](https://github.com/cfg4j/cfg4j) and [Spring Boot](https://github.com/spring-projects/spring-boot).

## Only the brave...
yacl4j is still very **experimental**. If you need something mature I strongly encourage you to check:
- [cfg4j](https://github.com/cfg4j/cfg4j)
- [OWNER](https://github.com/lviggiano/owner)

## Overview
yacl4j:
- is open source;
- is easy to use and extend;
- is heavily based on the well-known and battle-tested [Jackson](https://github.com/FasterXML/jackson) library;
- supports hierarchical configurations by design;
- supports configurations in YAML and Properties format;
- supports configurations from file, classpath, system properties, environment variables and user-defined sources;
- supports placeholders resolution.

## Dependecy
Set up your favorite dependency management tool:

#### Maven
```xml
<dependencies>
  <dependency>
    <groupId>com.github.fabriziocucci</groupId>
    <artifactId>yacl4j-core</artifactId>
    <version>0.2.0</version>
  </dependency>
</dependencies>
```

#### Gradle
```groovy
dependencies {
  compile group: "com.github.fabriziocucci", name:"yacl4j-core", version: "0.2.0"
}
```

## Usage
yacl4j API is really small and can be easily described with an example:

#### Configuration definition
```java
interface MyConfiguration {
  
  String getProperty();
  
  MyNestedConfiguration getMyNestedConfiguration();
  
  interface MyNestedConfiguration {
    String getNestedProperty();
  }
  
}
```

#### Configuration instantiation
```java
MyConfiguration myConfiguration = ConfigurationBuilder.newBuilder() // #0
    .source().file(new File("some-path/application.yaml"))          // #1
    .source().fileFromClasspath("application.yaml")                 // #2
    .source().fileFromPath("/Users/yacl4j/application.yaml")        // #3
    .source().systemProperties()                                    // #4
    .source().environmentVariables()                                // #5
    .source(new MyCustomConfigurationSource());                     // #6
    .build(MyConfiguration.class);                                  // #7
```

In the previous example:
- at line 0, we are creating a new ConfigurationBuilder;
- at lines 1-6, we are adding 6 configuration sources, in increasing order of priority;
- at line 7, we are building the configuration bean based on the MyConfiguration interface;
- if one property is defined in multiple sources, the source with higher priority win.

## Properties support? Yes, but...
yacl4j supports hierarchical configurations by design. Properties are not really hierarchical, so yacl4j leverages the [Json Pointer RFC](https://tools.ietf.org/html/rfc6901) to transform properties-based configurations into hierarchical ones.

Let's consider an example:

```properties
java.runtime.name=Java(TM) SE Runtime Environment
java.runtime.version=1.8.0_77-b03
java.vm.name=Java HotSpot(TM) 64-Bit Server VM
java.vm.vendor=Oracle Corporation
java.vm.version=25.77-b03
```

The above configuration becomes:

```yaml
java.runtime.name: Java(TM) SE Runtime Environment
java.runtime.version: 1.8.0_77-b03
java.vm.name: Java HotSpot(TM) 64-Bit Server VM
java.vm.vendor: Oracle Corporation
java.vm.version: 25.77-b03
```

Mmmmm...that doesn't look really "hierarchical" to me! Let's change a little bit the properties:

```properties
java/runtime/name=Java(TM) SE Runtime Environment
java/runtime/version=1.8.0_77-b03
java/vm/name=Java HotSpot(TM) 64-Bit Server VM
java/vm/vendor=Oracle Corporation
java/vm/version=25.77-b03
```

Hey, did we just replace all '.' with '/' ? Yes, indeed! This is because yacl4j is currently based on the [Json Pointer RFC](https://tools.ietf.org/html/rfc6901) with one simple exception: the leading '/' is optional.

The above configuration becomes: 

```yaml
java:
  runtime:
    name: Java(TM) SE Runtime Environment
    version: 1.8.0_77-b03
  vm:
    name: Java HotSpot(TM) 64-Bit Server VM
    vendor: Oracle Corporation
    version: 25.77-b03
```

Better, right? So, it's really easy to transform flat [Properties](https://docs.oracle.com/javase/8/docs/api/java/util/Properties.html) into hierarchical configurations.

My suggestion? Just use YAML-based configurations if you can!

## License
yacl4j is released under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).

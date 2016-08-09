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
    <version>0.0.2-SNAPSHOT</version>
  </dependency>
</dependencies>
```

#### Gradle
```groovy
dependencies {
  compile group: "com.github.fabriziocucci", name:"yacl4j-core", version: "0.0.2-SNAPSHOT"
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
    .build(MyConfiguration.class)                                   // #7
```

In the previous example:
- at line 0, we are creating a new ConfigurationBuilder;
- at lines 1-6, we are adding 6 configuration sources, in increasing order of priority;
- at line 7, we are building the configuration bean based on the MyConfiguration interface;
- if one property is defined in multiple sources, the source with higher priority win.

## Properties support? Yes, but...
yacl4j supports hierarchical configurations by design. Properties are not really hierarchical, so yacl4j leverages common conventions to transform properties-based configuration into hierarchical ones.

Let's consider an example:

```properties
java.runtime.name=Java(TM) SE Runtime Environment
java.runtime.version=1.8.0_77-b03
java.vm.version=25.77-b03
java.vm.vendor=Oracle Corporation
java.vm.name=Java HotSpot(TM) 64-Bit Server VM
```

The above configuration is transformed as follows:

```yaml
java:
  runtime:
    name: Java(TM) SE Runtime Environment
    version: 1.8.0_77-b03
  vm:
    version: 25.77-b03
    vendor: Oracle Corporation
    name: Java HotSpot(TM) 64-Bit Server VM
```

So why bother? Everything looks good so far. Yes, but...life is complicated! Consider the following example:

```properties
java.vendor=Oracle Corporation
java.vendor.url.bug=http://bugreport.sun.com/bugreport/
```

This is a nasty case where a set of properties cannot be mapped to a [JsonNode](https://fasterxml.github.io/jackson-databind/javadoc/2.8/com/fasterxml/jackson/databind/JsonNode.html). In the corresponding tree, 'java.vendor' should be both a [ValueNode](https://fasterxml.github.io/jackson-databind/javadoc/2.8/com/fasterxml/jackson/databind/node/ValueNode.html) and an [ObjectNode](https://fasterxml.github.io/jackson-databind/javadoc/2.8/com/fasterxml/jackson/databind/node/ObjectNode.html) which, of course, is not possible.


Since those unfortunate cases can be easily avoided in brand new properties-based configurations and they are quite rare in existing one (e.g. system properties), we accept the limitation and handle the situation by just dropping any ambiguous node discovered during the construction of the tree.


My suggestion? Just use YAML-based configurations if you can!

## License
yacl4j is released under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).
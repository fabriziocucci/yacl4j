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
- supports configurations in Yaml, Json and Properties format;
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
    <version>0.9.1</version>
  </dependency>
</dependencies>
```

#### Gradle
```groovy
dependencies {
  compile group: "com.github.fabriziocucci", name:"yacl4j-core", version: "0.9.1"
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
    .source().fromFile(new File("some-path/application.yaml"))      // #1
    .source().fromFileOnClasspath("application.yaml")               // #2
    .source().fromFileOnPath("/Users/yacl4j/application.yaml")      // #3
    .source().fromSystemProperties()                                // #4
    .source().fromEnvironmentVariables()                            // #5
    .source(new MyCustomConfigurationSource());                     // #6
    .build(MyConfiguration.class);                                  // #7
```

In the previous example:
- at line 0, we are creating a new ConfigurationBuilder;
- at lines 1-6, we are adding 6 configuration sources, in increasing order of priority;
- at line 7, we are building the configuration bean based on the MyConfiguration interface;
- if one property is defined in multiple sources, the source with higher priority win.

## Default values? 42

yacl4j is heavily based on Jackson (at this stage) and, unfortunately, Jackson does not support default values in interfaces...[yet](https://github.com/FasterXML/jackson-modules-base/issues/23). Disappointed? A bit. In trouble? No way. You just need to be a little bit more verbose:

```java
public class MyConfiguration {
  
  private String property = "42";
  
  public String getProperty() {
    return property;
  }
  
}
```

## Placeholders support? ${Yes}
yacl4j supports placeholders resolution with the syntax ${relaxed-json-pointer}.

#### Simple property placeholder

Let's consider an example:

```yaml
greeting: Hello ${name}
name: yacl4j
```

The above configuration becomes:

```yaml
greeting: Hello yacl4j
name: yacl4j
```

#### Object property placeholder

Let's consider an example:

```yaml
greeting: Hello ${person/name}
person:
  name: yacl4j
```

The above configuration becomes:

```yaml
greeting: Hello yacl4j
person:
  name: yacl4j
```

#### Array element placeholder

Let's consider an example:

```yaml
greeting: Hello ${persons/0}
persons:
  - yacl4j
```

The above configuration becomes:

```yaml
greeting: Hello yacl4j
persons:
  - yacl4j
```

#### Object placeholder

Let's consider an example:

```yaml
object: 
  property: value
myObject: ${object}
```

The above configuration becomes:

```yaml
object: 
  property: value
myObject:
  object: 
    property: value
```

#### Array placeholder

Let's consider an example:

```yaml
array: 
  - value
myArray: ${array} 
```

The above configuration becomes:

```yaml
array:
  - value
myArray:
  array:
    - value
```

#### Other placeholders

For a list of all supported placeholders check the [PlaceholderResolver test](https://github.com/fabriziocucci/yacl4j/blob/master/yacl4j-core/src/test/java/com/yacl4j/core/placeholder/NonRecursivePlaceholderResolverTest.java).

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

## Optional configuration sources

In some cases, you may want to specify configuration sources as optional.

#### Optional file

The classic use case is loading the configuration from one or multiple files that can optionally exist. There are three convenient methods for this:

* optional `File`:

```java
MyConfiguration myConfiguration = ConfigurationBuilder.newBuilder()
    .optionalSource().fromFile(new File("some-path/application.yaml"))
    .build(MyConfiguration.class);
```

* optional file on classpath:

```java
MyConfiguration myConfiguration = ConfigurationBuilder.newBuilder()
    .optionalSource().fromFileOnClasspath("application.yaml")
    .build(MyConfiguration.class);
```

* optional file on path:

```java
MyConfiguration myConfiguration = ConfigurationBuilder.newBuilder()
    .optionalSource().fromFileOnPath("/Users/yacl4j/application.yaml"))
    .build(MyConfiguration.class);
```

#### Optional configuration source

Now suppose you have defined a custom configuration source and you want it to be optional.

You just need to:

1. throw a `ConfigurationSourceNotAvailableException` in your `ConfigurationSource` implementation when appropriate, e.g.

```java
public class MyConfigurationSource implements ConfigurationSource {

  @Override
  public JsonNode getConfiguration() {
    if (isSourceAvailable()) {
      // ...
    } else {
      throw new ConfigurationSourceNotAvailableException();
    }
  }
  
}
```

2. use the `optionalSource` method on the `ConfigurationBuilder` which accepts a `ConfigurationSource` as parameter, e.g.

```java
MyConfiguration myConfiguration = ConfigurationBuilder.newBuilder()
    .optionalSource(new MyConfigurationSource())
    .build(MyConfiguration.class);
```

If, for some reason, you configuration source eagerly checks the availability of the source while it is being instantiated, you can:

1. throw a `ConfigurationSourceNotAvailableException` in the constructor or factory of your `ConfigurationSource` implementation, e.g.

```java
public class MyConfigurationSource implements ConfigurationSource {

  public MyConfigurationSource() {
    if (isSourceAvailable()) {
      // ...
    } else {
      throw new ConfigurationSourceNotAvailableException();
    }
  }
		
}
```

2. use the `optionalSource` method on the `ConfigurationBuilder` which accepts a `Supplier<ConfigurationSource>` as parameter, e.g. 

```java
MyConfiguration myConfiguration = ConfigurationBuilder.newBuilder()
    .optionalSource(() -> new MyConfigurationSource())
    .build(MyConfiguration.class);
```

## License
yacl4j is released under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).

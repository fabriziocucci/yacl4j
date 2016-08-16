# Change Log
All notable changes to this project will be documented in this file. This project adheres to [Semantic Versioning](http://semver.org/).

## [0.2.0](https://github.com/fabriziocucci/yacl4j/compare/yacl4j-0.1.0...yacl4j-0.2.0)
### Changed
- Re-thought '.' character handling in case of Java Properties. This impacts the way in which properties are added to the configuration tree and the way in which placeholder need to be specified in order to be correctly resolved.

## [0.1.0](https://github.com/fabriziocucci/yacl4j/compare/yacl4j-0.0.1...yacl4j-0.1.0)
### Changed
- Re-designed ConfigurationBuilder API. This impacts how the configuration sources are specified.
### Added
- Introduced ConfigurationSource for properties-based configurations
- Introduced ConfigurationSource for environment variables
- Introduced support for configuration files from path
- Introduced support for configuration files from classpath

## 0.0.1
### Added
- Introduced ConfigurationSource for yaml-based configurations
- Introduced ConfigurationSource for system properties
- Introduced non-recursive PlaceholdersResolver
- Introduced support for JDK 8 datatypes (with Jackson Jdk8Module)
- Introduced support for deserialization through interfaces (with Jackson MrBeanModule)
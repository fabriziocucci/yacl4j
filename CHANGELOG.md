# Change Log
All notable changes to this project will be documented in this file. This project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased](https://github.com/fabriziocucci/yacl4j/compare/yacl4j-0.0.1...HEAD)
### Changed
- ConfigurationBuilder API
### Added
- a ConfigurationSource for properties-based configurations
- a ConfigurationSource for environment variables
- support for configuration files from path
- support for configuration files from classpath

## 0.0.1
### Added
- a ConfigurationSource for yaml-based configurations
- a ConfigurationSource for system properties
- a non-recursive PlaceholdersResolver
- support for JDK 8 datatypes (with Jackson Jdk8Module)
- support for deserialization through interfaces (with Jackson MrBeanModule)
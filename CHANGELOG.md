# Change Log
All notable changes to this project will be documented in this file. This project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased](https://github.com/fabriziocucci/yacl4j/compare/yacl4j-0.8.0...HEAD)
### Added
- Added possibility to set a custom placeholder resolver.
### Fixed
- Fixed wrong handling of null values during placeholder resolution.

## [yacl4j-0.8.0](https://github.com/fabriziocucci/yacl4j/compare/yacl4j-0.7.0...yacl4j-0.8.0)
### Added
- Added optional configuration sources. [\#3](https://github.com/fabriziocucci/yacl4j/issues/3)
### Changed
- Improved ConfigurationSourceBuilder API by adding "from" as prefix of the builder methods.

## [0.7.0](https://github.com/fabriziocucci/yacl4j/compare/yacl4j-0.6.0...yacl4j-0.7.0)
### Changed
- Repackaged Jackson-related dependencies.

## [0.6.0](https://github.com/fabriziocucci/yacl4j/compare/yacl4j-0.5.0...yacl4j-0.6.0)
### Changed
- Empty files are treated as empty configurations.

## [0.5.0](https://github.com/fabriziocucci/yacl4j/compare/yacl4j-0.4.0...yacl4j-0.5.0)
### Added
- Introduced http module.

## [0.4.0](https://github.com/fabriziocucci/yacl4j/compare/yacl4j-0.3.0...yacl4j-0.4.0)
### Changed
- Aligned ConfigurationSource for environment variables to the one for system properties, i.e. environment variables specified as json pointers are interpreted as such. 
- The FileConfigurationSource is now based on the InputStream abstraction. The only visible side effect of this change is the possibility to load configuration files embedded in some other jar on the classpath.

## [0.3.0](https://github.com/fabriziocucci/yacl4j/compare/yacl4j-0.2.0...yacl4j-0.3.0)
### Added
- Introduced ConfigurationSource for json-based configurations.

## [0.2.0](https://github.com/fabriziocucci/yacl4j/compare/yacl4j-0.1.0...yacl4j-0.2.0)
### Changed
- Re-thought '.' character handling in case of Java Properties. This impacts the way in which properties are added to the configuration tree and the way in which placeholders need to be specified in order to be correctly resolved.

## [0.1.0](https://github.com/fabriziocucci/yacl4j/compare/yacl4j-0.0.1...yacl4j-0.1.0)
### Added
- Introduced ConfigurationSource for properties-based configurations.
- Introduced ConfigurationSource for environment variables.
- Introduced support for configuration files from path.
- Introduced support for configuration files from classpath.
### Changed
- Re-designed ConfigurationBuilder API. This impacts how the configuration sources are specified.

## 0.0.1
### Added
- Introduced ConfigurationSource for yaml-based configurations.
- Introduced ConfigurationSource for system properties.
- Introduced non-recursive PlaceholdersResolver.
- Introduced support for JDK 8 datatypes (with Jackson Jdk8Module).
- Introduced support for deserialization through interfaces (with Jackson MrBeanModule).
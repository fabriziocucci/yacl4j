package com.yacl4j.core.source.optional;

import java.io.File;

import com.yacl4j.core.ConfigurationBuilder;
import com.yacl4j.core.source.FileConfigurationSource;

public class OptionalConfigurationSourceBuilder {
	
	private final ConfigurationBuilder configurationBuilder;
	
	public OptionalConfigurationSourceBuilder(ConfigurationBuilder configurationBuilder) {
		this.configurationBuilder = configurationBuilder;
	}

	public ConfigurationBuilder fromFile(File file) {
		return configurationBuilder.source(OptionalConfigurationSource.build(() -> FileConfigurationSource.fromFile(file)));
	}
	
	public ConfigurationBuilder fromFileOnClasspath(String filename) {
		return configurationBuilder.source(OptionalConfigurationSource.build(() -> FileConfigurationSource.fromFileOnClasspath(filename)));
	}
	
	public ConfigurationBuilder fromFileOnPath(String filename) {
		return configurationBuilder.source(OptionalConfigurationSource.build(() -> FileConfigurationSource.fromFileOnPath(filename)));
	}
	
}

package com.yacl4j.core.source;

import java.io.File;

import com.yacl4j.core.ConfigurationBuilder;

public class ConfigurationSourceBuilder {
	
	private final ConfigurationBuilder configurationBuilder;

	public ConfigurationSourceBuilder(ConfigurationBuilder configurationBuilder) {
		this.configurationBuilder = configurationBuilder;
	}

	public ConfigurationBuilder file(File file) {
		return this.configurationBuilder.source(FileConfigurationSource.fromFile(file));
	}
	
	public ConfigurationBuilder fileFromClasspath(String filename) {
		return this.configurationBuilder.source(FileConfigurationSource.fromFileOnClasspath(filename));
	}
	
	public ConfigurationBuilder fileFromPath(String filename) {
		return this.configurationBuilder.source(FileConfigurationSource.fromFileOnPath(filename));
	}
	
	public ConfigurationBuilder systemProperties() {
		return this.configurationBuilder.source(new SystemPropertiesConfigurationSource());
	}
	
	public ConfigurationBuilder environmentVariables() {
		return this.configurationBuilder.source(new EnvironmentVariablesConfigurationSource());
	}
	
}
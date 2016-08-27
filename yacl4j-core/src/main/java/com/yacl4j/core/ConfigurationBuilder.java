package com.yacl4j.core;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.yacl4j.core.placeholder.NonRecursivePlaceholderResolver;
import com.yacl4j.core.source.ConfigurationSourceBuilder;
import com.yacl4j.core.util.ConfigurationUtils;

public class ConfigurationBuilder {

	private final List<ConfigurationSource> configurationSources;
	private final PlaceholderResolver placeholderResolver;
	
	private ConfigurationBuilder() {
		this.configurationSources = new LinkedList<>();
		this.placeholderResolver = new NonRecursivePlaceholderResolver();
	}
	
	public static ConfigurationBuilder newBuilder() {
		return new ConfigurationBuilder();
	}
	
	public ConfigurationSourceBuilder source() {
		return new ConfigurationSourceBuilder(this);
	}
	
	public ConfigurationBuilder source(ConfigurationSource configurationSource) {
		this.configurationSources.add(configurationSource);
		return this;
	}
	
	public <T> T build(Class<T> applicationConfigurationClass) {
		JsonNode applicationConfiguration = mergeConfigurationSources();
		this.placeholderResolver.resolvePlaceholders(applicationConfiguration);
		return ConfigurationUtils.toValue(applicationConfiguration, applicationConfigurationClass);
	}
	
	private JsonNode mergeConfigurationSources() {
		return configurationSources.stream()
				.map(ConfigurationSource::getConfiguration)
				.reduce(ConfigurationUtils.emptyConfiguration(), ConfigurationUtils::merge);
	}
	
}

package com.yacl4j.core;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import com.yacl4j.core.placeholder.NonRecursivePlaceholderResolver;
import com.yacl4j.core.source.ConfigurationSourceBuilder;
import com.yacl4j.core.source.optional.OptionalConfigurationSource;
import com.yacl4j.core.source.optional.OptionalConfigurationSourceBuilder;
import com.yacl4j.core.util.ConfigurationUtils;

import yacl4j.repackaged.com.fasterxml.jackson.databind.JsonNode;

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
	
	public OptionalConfigurationSourceBuilder optionalSource() {
		return new OptionalConfigurationSourceBuilder(this);
	}
	
	public ConfigurationBuilder optionalSource(ConfigurationSource configurationSource) {
		return source(OptionalConfigurationSource.build(configurationSource));
	}
	
	public ConfigurationBuilder optionalSource(Supplier<ConfigurationSource> configurationSourceFactoryO) {
		return source(OptionalConfigurationSource.build(configurationSourceFactoryO));
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

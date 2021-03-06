package com.yacl4j.core;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.yacl4j.core.placeholder.NonRecursivePlaceholderResolver;
import com.yacl4j.core.source.ConfigurationSourceBuilder;
import com.yacl4j.core.source.optional.OptionalConfigurationSource;
import com.yacl4j.core.source.optional.OptionalConfigurationSourceBuilder;
import com.yacl4j.core.util.ConfigurationUtils;

import yacl4j.repackaged.com.fasterxml.jackson.databind.JsonNode;

public class ConfigurationBuilder {

	private final List<ConfigurationSource> configurationSources;
	private PlaceholderResolver placeholderResolver;
	private Optional<ValueDecoder> valueDecoder;
	
	private ConfigurationBuilder() {
		this.configurationSources = new LinkedList<>();
		this.placeholderResolver = new NonRecursivePlaceholderResolver();
		this.valueDecoder = Optional.empty();
	}
	
	public static ConfigurationBuilder newBuilder() {
		return new ConfigurationBuilder();
	}
	
	public ConfigurationBuilder placeholderResolver(PlaceholderResolver placeholderResolver) {
		this.placeholderResolver = placeholderResolver;
		return this;
	}
	
	public ConfigurationBuilder valueDecoder(ValueDecoder valueDecoder) {
		this.valueDecoder = Optional.of(valueDecoder);
		return this;
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
	
	public ConfigurationBuilder optionalSource(Supplier<ConfigurationSource> configurationSourceFactory) {
		return source(OptionalConfigurationSource.build(configurationSourceFactory));
	}
	
	public <T> T build(Class<T> configurationClass) {
		JsonNode configuration = mergeConfigurationSources();
		this.placeholderResolver.resolvePlaceholders(configuration);
		this.valueDecoder.ifPresent(valueDecoder -> valueDecoder.decodeValues(configuration));
		return ConfigurationUtils.toValue(configuration, configurationClass);
	}
	
	private JsonNode mergeConfigurationSources() {
		return configurationSources.stream()
				.map(ConfigurationSource::getConfiguration)
				.reduce(ConfigurationUtils.emptyConfiguration(), ConfigurationUtils::merge);
	}
	
}

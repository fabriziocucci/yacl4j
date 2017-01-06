package com.yacl4j.core.source.optional;

import java.util.function.Supplier;

import com.yacl4j.core.ConfigurationSource;
import com.yacl4j.core.util.ConfigurationUtils;

import yacl4j.repackaged.com.fasterxml.jackson.databind.JsonNode;

public class OptionalConfigurationSource implements ConfigurationSource {

	private static final EmptyConfigurationSource EMPTY_CONFIGURATION_SOURCE = new EmptyConfigurationSource();
	
	private final ConfigurationSource configurationSource;

	private OptionalConfigurationSource(ConfigurationSource configurationSource) {
		this.configurationSource = configurationSource;
	}
	
	@Override
	public JsonNode getConfiguration() {
		try {
			return configurationSource.getConfiguration();
		} catch (ConfigurationSourceNotAvailableException configurationSourceNotFoundException) {
			return EMPTY_CONFIGURATION_SOURCE.getConfiguration();
		}
	}
	
	public static OptionalConfigurationSource build(ConfigurationSource configurationSource) {
		return build(() -> configurationSource);
	}
	
	public static OptionalConfigurationSource build(Supplier<ConfigurationSource> configurationSourceFactory) {
		try {
			return new OptionalConfigurationSource(configurationSourceFactory.get());
		} catch (ConfigurationSourceNotAvailableException configurationSourceNotAvailableException) {
			return new OptionalConfigurationSource(EMPTY_CONFIGURATION_SOURCE);
		}
	}
	
	private static class EmptyConfigurationSource implements ConfigurationSource {

		private static final JsonNode EMPTY_CONFIGURATION = ConfigurationUtils.emptyConfiguration();
		
		@Override
		public JsonNode getConfiguration() {
			return EMPTY_CONFIGURATION;
		}

	}
	
}

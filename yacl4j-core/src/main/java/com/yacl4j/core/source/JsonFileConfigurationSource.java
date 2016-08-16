package com.yacl4j.core.source;

import java.io.File;

import com.fasterxml.jackson.databind.JsonNode;
import com.yacl4j.core.ConfigurationSource;
import com.yacl4j.core.util.JacksonUtils;

class JsonFileConfigurationSource implements ConfigurationSource {

	private final File configurationFile;
	
	JsonFileConfigurationSource(File configurationFile) {
		this.configurationFile = configurationFile;
	}

	@Override
	public JsonNode getConfiguration() {
		try {
			return JacksonUtils.jsonObjectMapper().readTree(configurationFile);
		} catch (Exception e) {
			throw new IllegalStateException("Unable to read configuration file", e);
		}
	}

}

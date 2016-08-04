package com.yacl4j.core.source;

import java.io.File;

import com.fasterxml.jackson.databind.JsonNode;
import com.yacl4j.core.ConfigurationSource;
import com.yacl4j.core.util.JacksonUtils;

public class YamlFileConfigurationSource implements ConfigurationSource {
	
	private final File configurationFile;
	
	public YamlFileConfigurationSource(File configurationFile) {
		this.configurationFile = configurationFile;
	}

	@Override
	public JsonNode getConfiguration() {
		try {
			return JacksonUtils.yamlObjectMapper().readTree(configurationFile);
		} catch (Exception e) {
			throw new IllegalStateException("Unable to read configuration file", e);
		}
	}

}

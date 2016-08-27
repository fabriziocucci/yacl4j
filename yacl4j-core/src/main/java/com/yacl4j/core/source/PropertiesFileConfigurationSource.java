package com.yacl4j.core.source;

import java.io.File;

import com.fasterxml.jackson.databind.JsonNode;
import com.yacl4j.core.ConfigurationSource;
import com.yacl4j.core.util.ConfigurationUtils;

class PropertiesFileConfigurationSource implements ConfigurationSource {

	private final File configurationFile;
	
	PropertiesFileConfigurationSource(File configurationFile) {
		this.configurationFile = configurationFile;
	}

	@Override
	public JsonNode getConfiguration() {
		return ConfigurationUtils.Properties.fromFile(configurationFile);
	}
	
}

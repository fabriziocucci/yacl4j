package com.yacl4j.core.source;

import java.io.File;

import com.fasterxml.jackson.databind.JsonNode;
import com.yacl4j.core.ConfigurationSource;
import com.yacl4j.core.util.JsonConfigurationUtils;

class JsonFileConfigurationSource implements ConfigurationSource {

	private final File configurationFile;
	
	JsonFileConfigurationSource(File configurationFile) {
		this.configurationFile = configurationFile;
	}

	@Override
	public JsonNode getConfiguration() {
		return JsonConfigurationUtils.fromFile(configurationFile);
	}

}

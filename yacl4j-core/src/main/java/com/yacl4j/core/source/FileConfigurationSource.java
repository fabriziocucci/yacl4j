package com.yacl4j.core.source;

import java.io.File;
import java.util.function.Function;

import com.fasterxml.jackson.databind.JsonNode;
import com.yacl4j.core.ConfigurationSource;

class FileConfigurationSource implements ConfigurationSource {

	private final File configurationFile;
	private final Function<File, JsonNode> configurationFileReader;
	
	public FileConfigurationSource(File configurationFile, Function<File, JsonNode> configurationFileReader) {
		this.configurationFile = configurationFile;
		this.configurationFileReader = configurationFileReader;
	}

	@Override
	public JsonNode getConfiguration() {
		return configurationFileReader.apply(configurationFile);
	}
	
}

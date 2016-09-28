package com.yacl4j.core.source;

import java.io.File;
import java.net.URL;
import java.util.function.Function;

import com.fasterxml.jackson.databind.JsonNode;
import com.yacl4j.core.ConfigurationSource;
import com.yacl4j.core.util.ConfigurationUtils;
import com.yacl4j.core.util.FileUtils;

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
	
	static FileConfigurationSource fromFileOnClasspath(String filename) {
		URL configurationUrl = FileConfigurationSource.class.getClassLoader().getResource(filename);
		if (configurationUrl != null) {
			return selectFileConfigurationSource(FileUtils.toFile(configurationUrl));
		} else {
			throw new IllegalStateException("Unable to find file on classpath: " + filename);
		}
	}
	
	static FileConfigurationSource fromFileOnPath(String filename) {
		File configurationFile = new File(filename);
		if (configurationFile.exists()) {
			return selectFileConfigurationSource(configurationFile);
		} else {
			throw new IllegalStateException("Unable to find file on path: " + filename);
		}
	}
	
	static FileConfigurationSource fromFile(File file) {
		return selectFileConfigurationSource(file);
	}
	
	private static FileConfigurationSource selectFileConfigurationSource(File file) {
		if (file.getName().endsWith(".properties")) {
			return new FileConfigurationSource(file, ConfigurationUtils.Properties::fromFile);
		} else if (file.getName().endsWith(".yaml")) {
			return new FileConfigurationSource(file, ConfigurationUtils.Yaml::fromFile);
		} else if (file.getName().endsWith(".json")) {
			return new FileConfigurationSource(file, ConfigurationUtils.Json::fromFile);
		} else {
			throw new IllegalStateException("Configuration format not supported: " + file);
		}
	}
	
}

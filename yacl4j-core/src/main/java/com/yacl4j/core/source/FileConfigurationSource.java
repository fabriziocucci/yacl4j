package com.yacl4j.core.source;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.function.Function;

import com.yacl4j.core.ConfigurationSource;
import com.yacl4j.core.util.ConfigurationUtils;

import yacl4j.repackaged.com.fasterxml.jackson.databind.JsonNode;

class FileConfigurationSource implements ConfigurationSource {

	private final InputStream configurationInputStream;
	private final Function<InputStream, JsonNode> configurationInputStreamReader;
	
	FileConfigurationSource(InputStream configurationInputStream, Function<InputStream, JsonNode> configurationInputStreamReader) {
		this.configurationInputStream = configurationInputStream;
		this.configurationInputStreamReader = configurationInputStreamReader;
	}

	@Override
	public JsonNode getConfiguration() {
		return configurationInputStreamReader.apply(configurationInputStream);
	}
	
	static FileConfigurationSource fromFileOnClasspath(String filename) {
		InputStream configurationInputStream = FileConfigurationSource.class.getClassLoader().getResourceAsStream(filename);
		if (configurationInputStream != null) {
			return selectFileConfigurationSource(filename, configurationInputStream);
		} else {
			throw new IllegalStateException("Unable to find file on classpath: " + filename);
		}
	}
	
	static FileConfigurationSource fromFileOnPath(String filename) {
		try {
			FileInputStream fileInputStream = new FileInputStream(filename);
			return selectFileConfigurationSource(filename, fileInputStream);
		} catch (FileNotFoundException fileNotFoundException) {
			throw new IllegalStateException("Unable to find file on path: " + filename);
		}
	}
	
	static FileConfigurationSource fromFile(File file) {
		return FileConfigurationSource.fromFileOnPath(file.getAbsolutePath());
	}
	
	private static FileConfigurationSource selectFileConfigurationSource(String filename, InputStream configurationInputStream) {
		if (filename.endsWith(".properties")) {
			return new FileConfigurationSource(configurationInputStream, ConfigurationUtils.Properties::fromInputStream);
		} else if (filename.endsWith(".yaml")) {
			return new FileConfigurationSource(configurationInputStream, ConfigurationUtils.Yaml::fromInputStream);
		} else if (filename.endsWith(".json")) {
			return new FileConfigurationSource(configurationInputStream, ConfigurationUtils.Json::fromInputStream);
		} else {
			throw new IllegalStateException("Configuration format not supported: " + filename);
		}
	}
	
}

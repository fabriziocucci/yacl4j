package com.yacl4j.core.source;

import java.io.File;
import java.net.URL;
import java.util.function.Function;

import com.yacl4j.core.ConfigurationSource;
import com.yacl4j.core.util.FileUtils;

public class FileConfigurationSource {
	
	private final String filename;
	private final Function<File, ConfigurationSource> configurationSourceConstructor;
	
	public FileConfigurationSource(String filename, Function<File, ConfigurationSource> configurationSourceSupplier) {
		this.filename = filename;
		this.configurationSourceConstructor = configurationSourceSupplier;
	}

	public static FileConfigurationSource load(String filename) {
		if (filename.endsWith(".yaml")) {
			return new FileConfigurationSource(filename, (file) -> new YamlFileConfigurationSource(file));
		} else {
			throw new IllegalStateException("Configuration format not supported: " + filename);
		}
	}

	public ConfigurationSource fromClasspath() {
		URL fileUrl = this.getClass().getClassLoader().getResource(filename);
		if (fileUrl != null) {
			return configurationSourceConstructor.apply(FileUtils.toFile(fileUrl));
		} else {
			throw new IllegalStateException("Unable to find file on classpath: " + filename);
		}
	}
	
	public ConfigurationSource fromPath() {
		File file = new File(filename);
		if (file.exists()) {
			return configurationSourceConstructor.apply(file);
		} else {
			throw new IllegalStateException("Unable to find file on path: " + filename);
		}
	}
	
}

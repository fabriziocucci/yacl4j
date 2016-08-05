package com.yacl4j.core.source;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

class PropertiesFileConfigurationSource extends AbstractPropertiesConfigurationSource {

	private final File configurationFile;
	
	PropertiesFileConfigurationSource(File configurationFile) {
		this.configurationFile = configurationFile;
	}

	@Override
	protected Properties getProperties() {
		try (FileInputStream fileInputStream = new FileInputStream(configurationFile)) {
			Properties properties = new Properties();
			properties.load(fileInputStream);
			return properties;
		} catch (Exception e) {
			throw new IllegalStateException("Unable to load properties from file: " + configurationFile, e);
		}
	}
	
}

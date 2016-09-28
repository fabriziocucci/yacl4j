package com.yacl4j.core.util;

import java.io.InputStream;
import java.io.StringReader;
import java.util.Map;
import java.util.Properties;

class PropertiesUtils {

	private PropertiesUtils() { }
	
	static Properties fromInputStream(InputStream inputStream) {
		try {
			Properties properties = new Properties();
			properties.load(inputStream);
			return properties;
		} catch (Exception exception) {
			throw new IllegalStateException("Unable to load properties from input stream", exception);
		}
	}
	
	static Properties fromString(String string) {
		try (StringReader stringReader = new StringReader(string)) {
			Properties properties = new Properties();
			properties.load(stringReader);
			return properties;
		} catch (Exception exception) {
			throw new IllegalStateException("Unable to load properties from string: " + string, exception);
		}
	}
	
	static Properties fromMap(Map<?, ?> map) {
		Properties properties = new Properties();
		properties.putAll(map);
		return properties;
	}
	
}

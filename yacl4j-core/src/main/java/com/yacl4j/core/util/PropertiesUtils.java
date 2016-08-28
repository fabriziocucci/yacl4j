package com.yacl4j.core.util;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Map;
import java.util.Properties;

class PropertiesUtils {

	private PropertiesUtils() { }
	
	static Properties fromFile(File file) {
		try (FileReader fileInputStream = new FileReader(file)) {
			Properties properties = new Properties();
			properties.load(fileInputStream);
			return properties;
		} catch (Exception exception) {
			throw new IllegalStateException("Unable to load properties from file: " + file, exception);
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

package com.yacl4j.core.util;

import java.io.File;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YamlConfigurationUtils {

	private YamlConfigurationUtils() { }
	
	private static final ObjectMapper YAML_OBJECT_MAPPER = JacksonUtils.objectMapper(new YAMLFactory());
	
	public static JsonNode fromFile(File configuration) {
		try {
			return YAML_OBJECT_MAPPER.readTree(configuration);
		} catch (Exception exception) {
			throw new IllegalStateException(exception);
		}
	}
	
	public static JsonNode fromString(String configuration) {
		try {
			return YAML_OBJECT_MAPPER.readTree(configuration);
		} catch (Exception exception) {
			throw new IllegalStateException(exception);
		}
	}
	
}

package com.yacl4j.core.util;

import java.io.File;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConfigurationUtils {

	private JsonConfigurationUtils() { }
	
	private static final ObjectMapper JSON_OBJECT_MAPPER = JacksonUtils.objectMapper(null);
	
	public static JsonNode fromFile(File configuration) {
		try {
			return JSON_OBJECT_MAPPER.readTree(configuration);
		} catch (Exception exception) {
			throw new IllegalStateException(exception);
		}
	}
	
	public static JsonNode fromString(String configuration) {
		try {
			return JSON_OBJECT_MAPPER.readTree(configuration);
		} catch (Exception exception) {
			throw new IllegalStateException(exception);
		}
	}
	
}

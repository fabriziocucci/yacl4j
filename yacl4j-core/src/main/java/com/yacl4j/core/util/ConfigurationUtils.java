package com.yacl4j.core.util;

import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class ConfigurationUtils {

	private ConfigurationUtils() { }
	
	private static final ObjectMapper DEFAULT_OBJECT_MAPPER = JacksonUtils.objectMapper(null);
	
	public static ObjectNode emptyConfiguration() {
		return DEFAULT_OBJECT_MAPPER.createObjectNode();
	}
	
	public static JsonNode fromString(String configuration) {
		try {
			return DEFAULT_OBJECT_MAPPER.readValue(configuration, JsonNode.class);
		} catch (Exception exception) {
			return TextNode.valueOf(configuration);
		}
	}
	
	public static JsonNode fromMap(Map<?, ?> configuration) {
		return DEFAULT_OBJECT_MAPPER.valueToTree(configuration);
	}
	
	public static <T> T toValue(JsonNode configuration, Class<T> configurationClass) {
		try {
			return DEFAULT_OBJECT_MAPPER.treeToValue(configuration, configurationClass);
		} catch (Exception exception) {
			throw new IllegalStateException(exception);
		}
	}
	
	public static JsonNode merge(JsonNode mainNode, JsonNode updateNode) {
	    Iterator<String> fieldNames = updateNode.fieldNames();
	    while (fieldNames.hasNext()) {
	        String fieldName = fieldNames.next();
	        JsonNode jsonNode = mainNode.get(fieldName);
	        if (jsonNode != null && jsonNode.isObject()) {
	            merge(jsonNode, updateNode.get(fieldName));
	        } else {
	            if (mainNode instanceof ObjectNode) {
	                JsonNode value = updateNode.get(fieldName);
	                ((ObjectNode) mainNode).set(fieldName, value);
	            }
	        }
	    }
	    return mainNode;
	}
	
}

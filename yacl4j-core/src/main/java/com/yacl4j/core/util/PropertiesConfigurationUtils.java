package com.yacl4j.core.util;

import java.io.File;
import java.util.Enumeration;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class PropertiesConfigurationUtils {

	private PropertiesConfigurationUtils() { }
	
	public static JsonNode fromFile(File configuration) {
		return fromProperties(PropertiesUtils.fromFile(configuration));
	}
	
	public static JsonNode fromString(String configuration) {
		return fromProperties(PropertiesUtils.fromString(configuration));
	}
	
	public static JsonNode fromProperties(Properties properties) {
		ObjectNode configuration = ConfigurationUtils.emptyConfiguration();
		Enumeration<?> propertyNames = properties.propertyNames();
		while (propertyNames.hasMoreElements()) {
			String propertyKey = (String) propertyNames.nextElement();
			JsonPointer propertyKeyAsJsonPointer = JsonPointerUtils.fromProperty(propertyKey);
			addNode(configuration, propertyKeyAsJsonPointer, properties.getProperty(propertyKey));
		}
		return configuration;
	}
	
	private static void addNode(ObjectNode root, JsonPointer keyAsJsonPointer, String valueAsString) {
		ObjectNode currentNode = root;
		for (JsonPointer head : JsonPointerUtils.heads(keyAsJsonPointer)) {
			JsonNode headAsJsonNode = root.at(head);
			if (headAsJsonNode.isMissingNode() || headAsJsonNode.isValueNode()) {
				currentNode = currentNode.putObject(head.last().getMatchingProperty());
			} else {
				currentNode = (ObjectNode) headAsJsonNode;					
			}
		}
		if (!currentNode.has(keyAsJsonPointer.last().getMatchingProperty())) {
			JsonNode propertyValueAsJsonNode = ConfigurationUtils.fromString(valueAsString);
			currentNode.set(keyAsJsonPointer.last().getMatchingProperty(), propertyValueAsJsonNode);
		}
	}
	
}

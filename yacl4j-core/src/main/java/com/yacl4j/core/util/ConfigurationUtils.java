package com.yacl4j.core.util;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.mrbean.MrBeanModule;

public class ConfigurationUtils {

	private ConfigurationUtils() { }
	
	private static final ObjectMapper DEFAULT_OBJECT_MAPPER = Yaml.YAML_OBJECT_MAPPER;
	
	private static ObjectMapper objectMapper(JsonFactory jsonFactory) {
		return new ObjectMapper(jsonFactory)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.registerModule(new MrBeanModule())
				.registerModule(new Jdk8Module());
	}
	
	public static JsonNode emptyConfiguration() {
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
	
	public static class Yaml {
		
		private Yaml() { }
		
		private static final ObjectMapper YAML_OBJECT_MAPPER = objectMapper(new YAMLFactory());
		
		public static JsonNode fromInputStream(InputStream configuration) {
			try {
				return (configuration.available() > 0) ?
						YAML_OBJECT_MAPPER.readTree(configuration) :
						emptyConfiguration();
			} catch (Exception exception) {
				throw new IllegalStateException(exception);
			}
		}
		
		public static JsonNode fromString(String configuration) {
			try {
				return (configuration.isEmpty()) ?
						emptyConfiguration() :
						YAML_OBJECT_MAPPER.readTree(configuration);
			} catch (Exception exception) {
				throw new IllegalStateException(exception);
			}
		}
		
	}
	
	public static class Json {
		
		private Json() { }
		
		private static final ObjectMapper JSON_OBJECT_MAPPER = objectMapper(null);
		
		public static JsonNode fromInputStream(InputStream configuration) {
			try {
				return (configuration.available() > 0) ?
						JSON_OBJECT_MAPPER.readTree(configuration) :
						emptyConfiguration();
			} catch (Exception exception) {
				throw new IllegalStateException(exception);
			}
		}
		
		public static JsonNode fromString(String configuration) {
			try {
				return (configuration.isEmpty()) ?
						emptyConfiguration() :
						JSON_OBJECT_MAPPER.readTree(configuration);
			} catch (Exception exception) {
				throw new IllegalStateException(exception);
			}
		}
		
	}
	
	public static class Properties {
		
		private Properties() { }
		
		public static JsonNode fromInputStream(InputStream configuration) {
			return fromProperties(PropertiesUtils.fromInputStream(configuration));
		}
		
		public static JsonNode fromString(String configuration) {
			return fromProperties(PropertiesUtils.fromString(configuration));
		}
		
		public static JsonNode fromMap(Map<String, String> map) {
			return fromProperties(PropertiesUtils.fromMap(map));
		}
		
		public static JsonNode fromProperties(java.util.Properties properties) {
			ObjectNode configuration = (ObjectNode) ConfigurationUtils.emptyConfiguration();
			for (String propertyKey : properties.stringPropertyNames()) {
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
	
}

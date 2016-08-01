package com.yacl4j.core.util;

import java.util.Iterator;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.mrbean.MrBeanModule;

public class JacksonUtils {
	
	private JacksonUtils() { }
	
	private static final SilentObjectMapper YAML_OBJECT_MAPPER = new SilentObjectMapper(new YAMLFactory())
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.registerModule(new MrBeanModule())
			.registerModule(new Jdk8Module());
	
	public static SilentObjectMapper yamlObjectMapper() {
		return YAML_OBJECT_MAPPER;
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
	
	public static Optional<JsonPointer> compileJsonPointer(String jsonPointerAsString) {
		try {
			return Optional.of(JsonPointer.compile(jsonPointerAsString));
		} catch(IllegalArgumentException illegalArgumentException) {
			return Optional.empty();
		}
	}
	
	@SuppressWarnings("serial")
	public static class SilentObjectMapper extends ObjectMapper {
		
		private SilentObjectMapper(JsonFactory jsonFactory) {
			super(jsonFactory);
		}
		
		@Override
		public SilentObjectMapper configure(DeserializationFeature f, boolean state) {
			super.configure(f, state);
			return this;
		}
		
		@Override
		public SilentObjectMapper registerModule(Module module) {
			super.registerModule(module);
			return this;
		}
		
		public JsonNode readValueAsJsonNode(String content) {
			try {
				return readValue(content, JsonNode.class);
			} catch (Exception exception) {
				return TextNode.valueOf(content);
			}
		}
		
		public <T> T treeToValueUnchecked(JsonNode applicationConfiguration, Class<T> applicationConfigurationClass) {
			try {
				return treeToValue(applicationConfiguration, applicationConfigurationClass);
			} catch (JsonProcessingException e) {
				throw new IllegalStateException("Unable to build configuration", e);
			}
		}
		
	}
	
}

package com.yacl4j.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.mrbean.MrBeanModule;

class JacksonUtils {
	
	private JacksonUtils() { }
	
	static ObjectMapper objectMapper(JsonFactory jsonFactory) {
		return new ObjectMapper(jsonFactory)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.registerModule(new MrBeanModule())
				.registerModule(new Jdk8Module());
	}
	
}

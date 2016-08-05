package com.yacl4j.core.source;

import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.yacl4j.core.ConfigurationSource;
import com.yacl4j.core.util.JacksonUtils;

class EnvironmentVariablesConfigurationSource implements ConfigurationSource {

	@Override
	public JsonNode getConfiguration() {
		HashMap<String, String> environmentVariables = new HashMap<>(System.getenv());
		return JacksonUtils.yamlObjectMapper().valueToTree(environmentVariables);
	}

}

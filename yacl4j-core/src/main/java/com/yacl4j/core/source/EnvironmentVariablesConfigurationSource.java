package com.yacl4j.core.source;

import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.yacl4j.core.ConfigurationSource;
import com.yacl4j.core.util.ConfigurationUtils;

class EnvironmentVariablesConfigurationSource implements ConfigurationSource {

	@Override
	public JsonNode getConfiguration() {
		return ConfigurationUtils.fromMap(new HashMap<>(System.getenv()));
	}

}

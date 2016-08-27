package com.yacl4j.core.source;

import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.yacl4j.core.ConfigurationSource;
import com.yacl4j.core.util.PropertiesConfigurationUtils;

class SystemPropertiesConfigurationSource implements ConfigurationSource {

	@Override
	public JsonNode getConfiguration() {
		return PropertiesConfigurationUtils.fromProperties((Properties) System.getProperties().clone());
	}
	
}

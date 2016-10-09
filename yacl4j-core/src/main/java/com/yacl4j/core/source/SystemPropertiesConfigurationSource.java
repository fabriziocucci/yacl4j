package com.yacl4j.core.source;

import com.fasterxml.jackson.databind.JsonNode;
import com.yacl4j.core.ConfigurationSource;
import com.yacl4j.core.util.ConfigurationUtils;

class SystemPropertiesConfigurationSource implements ConfigurationSource {

	@Override
	public JsonNode getConfiguration() {
		return ConfigurationUtils.Properties.fromProperties(System.getProperties());
	}
	
}

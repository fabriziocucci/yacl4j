package com.yacl4j.core.source;

import com.yacl4j.core.ConfigurationSource;
import com.yacl4j.core.util.ConfigurationUtils;

import yacl4j.repackaged.com.fasterxml.jackson.databind.JsonNode;

class SystemPropertiesConfigurationSource implements ConfigurationSource {

	@Override
	public JsonNode getConfiguration() {
		return ConfigurationUtils.Properties.fromProperties(System.getProperties());
	}
	
}

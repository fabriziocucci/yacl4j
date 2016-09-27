package org.yacl4j.http;

import java.util.function.Function;

import javax.ws.rs.client.Invocation;

import com.fasterxml.jackson.databind.JsonNode;
import com.yacl4j.core.ConfigurationSource;

class HttpConfigurationSource implements ConfigurationSource {
	
	private final Invocation invocation;
	private final Function<String, JsonNode> configurationConverter;
	
	HttpConfigurationSource(Invocation invocation, Function<String, JsonNode> configurationConverter) {
		this.invocation = invocation;
		this.configurationConverter = configurationConverter;
	}

	@Override
	public JsonNode getConfiguration() {
		String configuration = invocation.invoke(String.class);
		return configurationConverter.apply(configuration);
	}

}

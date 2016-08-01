package com.yacl4j.core;

import com.fasterxml.jackson.databind.JsonNode;

public interface PlaceholderResolver {

	void resolvePlaceholders(JsonNode applicationConfiguration);
	
}
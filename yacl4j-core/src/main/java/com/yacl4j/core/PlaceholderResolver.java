package com.yacl4j.core;

import yacl4j.repackaged.com.fasterxml.jackson.databind.JsonNode;

public interface PlaceholderResolver {

	void resolvePlaceholders(JsonNode applicationConfiguration);
	
}
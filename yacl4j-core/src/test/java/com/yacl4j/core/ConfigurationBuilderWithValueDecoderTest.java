package com.yacl4j.core;

import static com.yacl4j.test.TestUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import yacl4j.repackaged.com.fasterxml.jackson.databind.JsonNode;
import yacl4j.repackaged.com.fasterxml.jackson.databind.node.TextNode;

public class ConfigurationBuilderWithValueDecoderTest {

	@Test
	public void testConfigurationBuilderWithValueDecoderThatReverseStrings() throws IOException {
	
		String configurationAsString = String.join(System.getProperty("line.separator")
				, "string: I'm a string"
				, "number: 42");
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFile(createConfigurationFile(configurationAsString, ".yaml"))
				.valueDecoder(new ReverseStringValueDecoder())
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.at("/string").asText(), is(equalTo("gnirts a m'I")));
		assertThat(configuration.at("/number").asInt(), is(equalTo(42)));
	}
	
	private static class ReverseStringValueDecoder extends AbstractNodeTransformer implements ValueDecoder {

		@Override
		public void decodeValues(JsonNode applicationConfiguration) {
			transform(applicationConfiguration);
		}

		@Override
		protected JsonNode transformValueNode(JsonNode root, JsonNode currentNode) {
			if (currentNode.isTextual()) {
				return TextNode.valueOf(new StringBuilder(currentNode.textValue()).reverse().toString());
			} else {
				return currentNode;
			}
		}
		
	}
	
}

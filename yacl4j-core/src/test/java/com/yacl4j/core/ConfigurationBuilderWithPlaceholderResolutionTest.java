package com.yacl4j.core;

import static com.yacl4j.test.TestUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import yacl4j.repackaged.com.fasterxml.jackson.databind.JsonNode;

@RunWith(JMockit.class)
public class ConfigurationBuilderWithPlaceholderResolutionTest {

	@Test
	public void testConfigurationBuilderWithNoOpPlaceholderResolver() throws IOException {
		
		String configurationAsString = String.join(System.getProperty("line.separator")
				, "greeting: hello ${name}"
				, "name: world");
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFile(createConfigurationFile(configurationAsString, ".yaml"))
				.placeholderResolver(config -> { })
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.at("/greeting").asText(), is(equalTo("hello ${name}")));
	}
	
	@Test
	public void testConfigurationBuilderWithSinglePlaceholderResolvedFromTheSameSource() throws IOException {
		
		String configurationAsString = String.join(System.getProperty("line.separator")
				, "greeting: hello ${name}"
				, "name: world");
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFile(createConfigurationFile(configurationAsString, ".yaml"))
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.at("/greeting").asText(), is(equalTo("hello world")));
	}
	
	@Test
	public void testConfigurationBuilderWithSinglePlaceholderResolvedFromSourceWithHigherPriority() throws IOException {
		
		String configurationAsString = String.join(System.getProperty("line.separator")
				, "greeting: hello ${name}"
				, "name: world");
		
		new MockUp<System>() {
			@Mock
			Properties getProperties() {
				Properties properties = new Properties();
				properties.setProperty("name", "wow");
				return properties;
			}
		};
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFile(createConfigurationFile(configurationAsString, ".yaml"))
				.source().fromSystemProperties()
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.at("/greeting").asText(), is(equalTo("hello wow")));
	}
	
}

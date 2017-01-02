package com.yacl4j.core;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import yacl4j.repackaged.com.fasterxml.jackson.databind.JsonNode;

@RunWith(JMockit.class)
public class ConfigurationBuilderWithMultipleSourcesTest {

	@Test
	public void testConfigurationBuilderWithYamlFileAndSystemPropertiesWhenAllPropertiesAreOverridden() throws Exception {
		
		// yaml file configuration
		
		String configurationAsString = String.join(System.getProperty("line.separator")
				, "property: file.value"
				, "nested:"
				, "  property: file.nested.value");
		
		// system properties configuration
		
		new MockUp<System>() {
			@Mock
			Properties getProperties() {
				Properties properties = new Properties();
				properties.setProperty("property", "system.property.value");
				properties.setProperty("nested/property", "system.property.nested.value");
				return properties;
			}
		};
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().file(createConfigurationFile(configurationAsString, ".yaml"))
				.source().systemProperties()
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.at("/property").asText(), is(equalTo("system.property.value")));
		assertThat(configuration.at("/nested/property").asText(), is(equalTo("system.property.nested.value")));
	}
	
	@Test
	public void testConfigurationBuilderWithYamlFileAndSystemPropertiesWhenNoPropertyIsOverridden() throws Exception {
		
		// yaml file configuration
		
		String configurationAsString = String.join(System.getProperty("line.separator")
				, "property1: value1"
				, "nested:"
				, "  property1: nested.value1");
		
		// system properties configuration
		
		new MockUp<System>() {
			@Mock
			Properties getProperties() {
				Properties properties = new Properties();
				properties.setProperty("property2", "value2");
				properties.setProperty("nested/property2", "nested.value2");
				return properties;
			}
		};
		
		JsonNode configurationWithYamlFileAndSystemProperties = ConfigurationBuilder.newBuilder()
				.source().file(createConfigurationFile(configurationAsString, ".yaml"))
				.source().systemProperties()
				.build(JsonNode.class);
		
		assertThat(configurationWithYamlFileAndSystemProperties, is(notNullValue()));
		assertThat(configurationWithYamlFileAndSystemProperties.at("/property1").asText(), is(equalTo("value1")));
		assertThat(configurationWithYamlFileAndSystemProperties.at("/nested/property1").asText(), is(equalTo("nested.value1")));
		assertThat(configurationWithYamlFileAndSystemProperties.at("/property2").asText(), is(equalTo("value2")));
		assertThat(configurationWithYamlFileAndSystemProperties.at("/nested/property2").asText(), is(equalTo("nested.value2")));
	}
	
	///////////
	// UTILS //
	///////////
	
	private static File createConfigurationFile(String configuration, String fileExtension) throws IOException {
		File configurationFile = File.createTempFile("application", fileExtension);
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configurationFile))) {
			bufferedWriter.write(configuration);
			return configurationFile;
		}
	}
	
}

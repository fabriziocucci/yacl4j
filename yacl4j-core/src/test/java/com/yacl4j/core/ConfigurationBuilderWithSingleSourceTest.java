package com.yacl4j.core;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.yacl4j.core.source.optional.ConfigurationSourceNotAvailableException;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import yacl4j.repackaged.com.fasterxml.jackson.databind.JsonNode;

@RunWith(JMockit.class)
public class ConfigurationBuilderWithSingleSourceTest {
	
	//////////
	// file //
	//////////
	
	@Test(expected=ConfigurationSourceNotAvailableException.class)
	public void testConfigurationBuilderWhenFileFormatIsNotSupported() {
		ConfigurationBuilder.newBuilder()
				.source().fromFile(new File("application.unknown"))
				.build(JsonNode.class);
	}
	
	@Test(expected=ConfigurationSourceNotAvailableException.class)
	public void testConfigurationBuilderWhenYamlFileDoesNotExist() {
		ConfigurationBuilder.newBuilder()
				.source().fromFile(new File("i-dont-exist.yaml"))
				.build(JsonNode.class);
	}
	
	@Test(expected=ConfigurationSourceNotAvailableException.class)
	public void testConfigurationBuilderWhenJsonFileDoesNotExist() {
		ConfigurationBuilder.newBuilder()
				.source().fromFile(new File("i-dont-exist.json"))
				.build(JsonNode.class);
	}
	
	@Test(expected=ConfigurationSourceNotAvailableException.class)
	public void testConfigurationBuilderWhenPropertiesFileDoesNotExist() {
		ConfigurationBuilder.newBuilder()
				.source().fromFile(new File("i-dont-exist.properties"))
				.build(JsonNode.class);
	}
	
	@Test
	public void testConfigurationBuilderWhenYamlFileIsEmpty() throws IOException {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFile(createConfigurationFile("", ".yaml"))
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}
	
	@Test
	public void testConfigurationBuilderWhenJsonFileIsEmpty() throws IOException {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFile(createConfigurationFile("", ".json"))
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}
	
	@Test
	public void testConfigurationBuilderWhenPropertiesFileIsEmpty() throws IOException {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFile(createConfigurationFile("", ".properties"))
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}
	
	@Test
	public void testConfigurationBuilderWhenYamlFileContainsOnlyWhiteSpaces() throws IOException {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFile(createConfigurationFile("   ", ".yaml"))
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}
	
	@Test
	public void testConfigurationBuilderWhenJsonFileContainsOnlyWhiteSpaces() throws IOException {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFile(createConfigurationFile("   ", ".json"))
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}
	
	@Test
	public void testConfigurationBuilderWhenPropertiesFileContainsOnlyWhiteSpaces() throws IOException {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFile(createConfigurationFile("   ", ".properties"))
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}
	
	@Test
	public void testConfigurationBuilderWhenYamlFileContainsOnlyComments() throws IOException {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFile(createConfigurationFile("#comment", ".yaml"))
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}
	
	// JSON format does not support comments!
	
	@Test
	public void testConfigurationBuilderWhenPropertiesFileContainsOnlyComments() throws IOException {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFile(createConfigurationFile("#comment", ".properties"))
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}
	
	@Test
	public void testConfigurationBuilderWithYamlFile() throws IOException {
		
		String configurationAsString = String.join(System.getProperty("line.separator")
				, "property: value"
				, "nested:"
				, "    property: nested.value");
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFile(createConfigurationFile(configurationAsString, ".yaml"))
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.at("/property").asText(), is(equalTo("value")));
		assertThat(configuration.at("/nested/property").asText(), is(equalTo("nested.value")));
	}
	
	@Test
	public void testConfigurationBuilderWithJsonFile() throws IOException {
		
		String configurationAsString = String.join(System.getProperty("line.separator")
				, "{"
				, "    \"property\": \"value\","
				, "    \"nested\": {"
				, "        \"property\": \"nested.value\""
				, "    }"
				, "}");
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFile(createConfigurationFile(configurationAsString, ".json"))
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.at("/property").asText(), is(equalTo("value")));
		assertThat(configuration.at("/nested/property").asText(), is(equalTo("nested.value")));
	}
	
	@Test
	public void testConfigurationBuilderWithPropertiesFile() throws IOException {
		
		String configurationAsString = String.join(System.getProperty("line.separator")
				, "property=value"
				, "nested/property=nested.value");
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFile(createConfigurationFile(configurationAsString, ".properties"))
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.at("/property").asText(), is(equalTo("value")));
		assertThat(configuration.at("/nested/property").asText(), is(equalTo("nested.value")));
	}
	
	//////////////////
	// fileFromPath //
	//////////////////
	
	@Test(expected=ConfigurationSourceNotAvailableException.class)
	public void testConfigurationBuilderWhenYamlFileFromPathDoesNotExist() {
		ConfigurationBuilder.newBuilder()
				.source().fromFileOnPath("i-dont-exist.yaml")
				.build(JsonNode.class);
	}
	
	@Test(expected=ConfigurationSourceNotAvailableException.class)
	public void testConfigurationBuilderWhenJsonFileFromPathDoesNotExist() {
		ConfigurationBuilder.newBuilder()
				.source().fromFileOnPath("i-dont-exist.json")
				.build(JsonNode.class);
	}
	
	@Test(expected=ConfigurationSourceNotAvailableException.class)
	public void testConfigurationBuilderWhenPropertiesFileFromPathDoesNotExist() {
		ConfigurationBuilder.newBuilder()
				.source().fromFileOnPath("i-dont-exist.properties")
				.build(JsonNode.class);
	}
	
	@Test
	public void testConfigurationBuilderWhenYamlFileFromPathIsEmpty() throws IOException {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFileOnPath(createConfigurationFile("", ".yaml").getAbsolutePath())
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}
	
	@Test
	public void testConfigurationBuilderWhenJsonFileFromPathIsEmpty() throws IOException {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFileOnPath(createConfigurationFile("", ".json").getAbsolutePath())
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}
	
	@Test
	public void testConfigurationBuilderWhenPropertiesFileFromPathIsEmpty() throws IOException {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFileOnPath(createConfigurationFile("", ".properties").getAbsolutePath())
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}
	
	@Test
	public void testConfigurationBuilderWithYamlFileFromPath() throws IOException {
		
		String configurationAsString = String.join(System.getProperty("line.separator")
				, "property: value"
				, "nested:"
				, "    property: nested.value");
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFileOnPath(createConfigurationFile(configurationAsString, ".yaml").getAbsolutePath())
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.at("/property").asText(), is(equalTo("value")));
		assertThat(configuration.at("/nested/property").asText(), is(equalTo("nested.value")));
	}
	
	@Test
	public void testConfigurationBuilderWithJsonFileFromPath() throws IOException {
		
		String configurationAsString = String.join(System.getProperty("line.separator")
				, "{"
				, "    \"property\": \"value\","
				, "    \"nested\": {"
				, "        \"property\": \"nested.value\""
				, "    }"
				, "}");
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFileOnPath(createConfigurationFile(configurationAsString, ".yaml").getAbsolutePath())
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.at("/property").asText(), is(equalTo("value")));
		assertThat(configuration.at("/nested/property").asText(), is(equalTo("nested.value")));
	}
	
	@Test
	public void testConfigurationBuilderWithPropertiesFileFromPath() throws IOException {
		
		String configurationAsString = String.join(System.getProperty("line.separator")
				, "property=value"
				, "nested/property=nested.value");
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFileOnPath(createConfigurationFile(configurationAsString, ".properties").getAbsolutePath())
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.at("/property").asText(), is(equalTo("value")));
		assertThat(configuration.at("/nested/property").asText(), is(equalTo("nested.value")));
	}
	
	///////////////////////
	// fileFromClasspath //
	///////////////////////
	
	@Test(expected=ConfigurationSourceNotAvailableException.class)
	public void testConfigurationBuilderWhenYamlFileFromClasspathDoesNotExist() {
		ConfigurationBuilder.newBuilder()
				.source().fromFileOnClasspath("i-dont-exist.yaml")
				.build(JsonNode.class);
	}
	
	@Test(expected=ConfigurationSourceNotAvailableException.class)
	public void testConfigurationBuilderWhenJsonFileFromClasspathDoesNotExist() {
		ConfigurationBuilder.newBuilder()
				.source().fromFileOnClasspath("i-dont-exist.json")
				.build(JsonNode.class);
	}
	
	@Test(expected=ConfigurationSourceNotAvailableException.class)
	public void testConfigurationBuilderWhenPropertiesFileFromClasspathDoesNotExist() {
		ConfigurationBuilder.newBuilder()
				.source().fromFileOnClasspath("i-dont-exist.properties")
				.build(JsonNode.class);
	}
	
	@Test
	public void testConfigurationBuilderWhenYamlFileFromClasspathIsEmpty() {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFileOnClasspath("im-empty.yaml")
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}
	
	@Test
	public void testConfigurationBuilderWhenJsonFileFromClasspathIsEmpty() {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFileOnClasspath("im-empty.json")
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}
	
	@Test
	public void testConfigurationBuilderWhenPropertiesFileFromClasspathIsEmpty() {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFileOnClasspath("im-empty.properties")
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}
	
	@Test
	public void testConfigurationBuilderWithYamlFileFromClasspath() {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFileOnClasspath("application.yaml")
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.at("/property").asText(), is(equalTo("value")));
		assertThat(configuration.at("/nested/property").asText(), is(equalTo("nested.value")));
	}
	
	@Test
	public void testConfigurationBuilderWithJsonFileFromClasspath() {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFileOnClasspath("application.json")
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.at("/property").asText(), is(equalTo("value")));
		assertThat(configuration.at("/nested/property").asText(), is(equalTo("nested.value")));
	}
	
	@Test
	public void testConfigurationBuilderWithPropertiesFileFromClasspath() {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromFileOnClasspath("application.properties")
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.at("/property").asText(), is(equalTo("value")));
		assertThat(configuration.at("/nested/property").asText(), is(equalTo("nested.value")));
	}
	
	//////////////////////
	// systemProperties //
	//////////////////////
	
	@Test
	public void testConfigurationBuilderWithSystemProperties() {
		
		new MockUp<System>() {
			@Mock
			Properties getProperties() {
				Properties properties = new Properties();
				properties.setProperty("property", "value");
				properties.setProperty("nested/property", "nested.value");
				return properties;
			}
		};
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromSystemProperties()
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.at("/property").asText(), is(equalTo("value")));
		assertThat(configuration.at("/nested/property").asText(), is(equalTo("nested.value")));
	}

	//////////////////////////
	// environmentVariables //
	//////////////////////////
	
	@Test
	public void testConfigurationBuilderWithEnvironmentVariables() {
		
		new MockUp<System>() {
			@Mock
			Map<String,String> getenv() {
				Map<String, String> environmentVariables = new HashMap<>();
				environmentVariables.put("property", "value");
				environmentVariables.put("nested.property", "nested.value");
				return environmentVariables;
			}
		};
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fromEnvironmentVariables()
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.at("/property").asText(), is(equalTo("value")));
		assertThat(configuration.at("/nested.property").asText(), is(equalTo("nested.value")));
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

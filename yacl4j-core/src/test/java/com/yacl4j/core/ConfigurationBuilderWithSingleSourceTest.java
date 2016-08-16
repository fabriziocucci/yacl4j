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

import com.fasterxml.jackson.databind.JsonNode;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class ConfigurationBuilderWithSingleSourceTest {
	
	//////////
	// file //
	//////////
	
	@Test(expected=IllegalStateException.class)
	public void testConfigurationBuilderWhenFileFormatIsNotSupported() {
		ConfigurationBuilder.newBuilder()
				.source().file(new File("application.unknown"))
				.build(JsonNode.class);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testConfigurationBuilderWhenYamlFileDoesNotExist() {
		ConfigurationBuilder.newBuilder()
				.source().file(new File("i-dont-exist.yaml"))
				.build(JsonNode.class);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testConfigurationBuilderWhenPropertiesFileDoesNotExist() {
		ConfigurationBuilder.newBuilder()
				.source().file(new File("i-dont-exist.properties"))
				.build(JsonNode.class);
	}
	
	@Test
	public void testConfigurationBuilderWithYamlFile() throws IOException {
		
		String configurationAsString = String.join(System.getProperty("line.separator")
				, "property: value"
				, "nested:"
				, "  property: nested.value");
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().file(createConfigurationFile(configurationAsString, ".yaml"))
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
				.source().file(createConfigurationFile(configurationAsString, ".properties"))
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.at("/property").asText(), is(equalTo("value")));
		assertThat(configuration.at("/nested/property").asText(), is(equalTo("nested.value")));
	}
	
	//////////////////
	// fileFromPath //
	//////////////////
	
	@Test(expected=IllegalStateException.class)
	public void testConfigurationBuilderWhenYamlFileFromPathDoesNotExist() {
		ConfigurationBuilder.newBuilder()
				.source().fileFromPath("i-dont-exist.yaml")
				.build(JsonNode.class);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testConfigurationBuilderWhenPropertiesFileFromPathDoesNotExist() {
		ConfigurationBuilder.newBuilder()
				.source().fileFromPath("i-dont-exist.properties")
				.build(JsonNode.class);
	}
	
	@Test
	public void testConfigurationBuilderWithYamlFileFromPath() throws IOException {
		
		String configurationAsString = String.join(System.getProperty("line.separator")
				, "property: value"
				, "nested:"
				, "  property: nested.value");
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fileFromPath(createConfigurationFile(configurationAsString, ".yaml").getAbsolutePath())
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
				.source().fileFromPath(createConfigurationFile(configurationAsString, ".properties").getAbsolutePath())
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.at("/property").asText(), is(equalTo("value")));
		assertThat(configuration.at("/nested/property").asText(), is(equalTo("nested.value")));
	}
	
	///////////////////////
	// fileFromClasspath //
	///////////////////////
	
	@Test(expected=IllegalStateException.class)
	public void testConfigurationBuilderWhenYamlFileFromClasspathDoesNotExist() {
		ConfigurationBuilder.newBuilder()
				.source().fileFromClasspath("i-dont-exist.yaml")
				.build(JsonNode.class);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testConfigurationBuilderWhenPropertiesFileFromClasspathDoesNotExist() {
		ConfigurationBuilder.newBuilder()
				.source().fileFromClasspath("i-dont-exist.properties")
				.build(JsonNode.class);
	}
	
	@Test
	public void testConfigurationBuilderWithYamlFileFromClasspath() {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fileFromClasspath("application.yaml")
				.build(JsonNode.class);
		
		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.at("/property").asText(), is(equalTo("value")));
		assertThat(configuration.at("/nested/property").asText(), is(equalTo("nested.value")));
	}
	
	@Test
	public void testConfigurationBuilderWithPropertiesFileFromClasspath() {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.source().fileFromClasspath("application.properties")
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
				.source().systemProperties()
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
				.source().environmentVariables()
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

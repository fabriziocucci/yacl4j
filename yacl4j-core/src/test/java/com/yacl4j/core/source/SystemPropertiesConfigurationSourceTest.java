package com.yacl4j.core.source;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.yacl4j.core.util.JacksonUtils;
import com.yacl4j.core.util.JsonPointerUtils;

public class SystemPropertiesConfigurationSourceTest {

	private final SystemPropertiesConfigurationSource systemPropertiesConfigurationSource = new SystemPropertiesConfigurationSource();
	
	private Properties systemProperties;
	
	@Before
	public void cloneSystemProperties() {
		this.systemProperties = (Properties) System.getProperties().clone();
	}
	
	@After
	public void restoreSystemProperties() {
		System.setProperties(systemProperties);
	}
	
	@Test
	public void testSimpleSystemProperty() {
		
		String propertyKey = "property";
		String propertyValue = "value";
		System.setProperty(propertyKey, propertyValue);
		
		JsonNode configuration = systemPropertiesConfigurationSource.getConfiguration();
		
		String actualPropertyValue = configuration.get(propertyKey).asText();
		assertThat(actualPropertyValue, is(equalTo(propertyValue)));
	}
	
	@Test
	public void testNestedSystemProperty() {
		
		String propertyKey = "/nested/property";
		String propertyValue = "value";
		System.setProperty(propertyKey, propertyValue);
		
		JsonNode configuration = systemPropertiesConfigurationSource.getConfiguration();
		
		String actualPropertyValue = configuration.at(JsonPointerUtils.fromProperty(propertyKey).get()).asText();
		assertThat(actualPropertyValue, is(equalTo(propertyValue)));
	}
	
	@Test
	public void testNestedSystemPropertiesWithCommonHead() {
		
		String property1Key = "/nested/property1";
		String property1Value = "value1";
		System.setProperty(property1Key, property1Value);
		
		String property2Key = "/nested/property2";
		String property2Value = "value2";
		System.setProperty(property2Key, property2Value);
		
		JsonNode configuration = systemPropertiesConfigurationSource.getConfiguration();
		
		String actualProperty1Value = configuration.at(JsonPointerUtils.fromProperty(property1Key).get()).asText();
		assertThat(actualProperty1Value, is(equalTo(property1Value)));
		
		String actualProperty2Value = configuration.at(JsonPointerUtils.fromProperty(property2Key).get()).asText();
		assertThat(actualProperty2Value, is(equalTo(property2Value)));
	}
	
	@Test
	public void testSystemPropertyWhenTheValueIsAnObject() {
		
		String propertyKey = "object";
		JsonNode propertyValue = JacksonUtils.yamlObjectMapper().readValueAsJsonNode("key: value");
		System.setProperty(propertyKey, propertyValue.toString());
		
		JsonNode configuration = systemPropertiesConfigurationSource.getConfiguration();
		
		JsonNode actualPropertyValue = configuration.at(JsonPointerUtils.fromProperty(propertyKey).get());
		assertThat(actualPropertyValue, is(equalTo(propertyValue)));
	}
	
	@Test
	public void testSystemPropertyWhenTheValueIsAnArray() {
		
		String propertyKey = "array";
		JsonNode propertyValue = JacksonUtils.yamlObjectMapper().readValueAsJsonNode("- element");
		System.setProperty(propertyKey, propertyValue.toString());
		
		JsonNode configuration = systemPropertiesConfigurationSource.getConfiguration();
		
		JsonNode actualPropertyValue = configuration.at(JsonPointerUtils.fromProperty(propertyKey).get());
		assertThat(actualPropertyValue, is(equalTo(propertyValue)));
	}
	
}

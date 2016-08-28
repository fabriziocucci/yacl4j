package com.yacl4j.core.source;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.databind.JsonNode;
import com.yacl4j.core.util.ConfigurationUtils;
import com.yacl4j.core.util.JsonPointerUtils;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class SystemPropertiesConfigurationSourceTest {

	private final SystemPropertiesConfigurationSource systemPropertiesConfigurationSource = new SystemPropertiesConfigurationSource();
	
	@Test
	public void testSimpleSystemProperty() {
		
		String systemPropertyKey = "property";
		String systemPropertyValue = "value";
		
		new MockUp<System>() {
			@Mock
	        Properties getProperties() {
	        	Properties properties = new Properties();
	        	properties.setProperty(systemPropertyKey, systemPropertyValue);
	            return properties;
	        }
	    };
		
		JsonNode configuration = systemPropertiesConfigurationSource.getConfiguration();
		
		String actualPropertyValue = configuration.get(systemPropertyKey).asText();
		assertThat(actualPropertyValue, is(equalTo(systemPropertyValue)));
	}
	
	@Test
	public void testNestedSystemProperty() {
		
		String systemPropertyKey = "/nested/property";
		String systemPropertyValue = "value";
		
		new MockUp<System>() {
			@Mock
	        Properties getProperties() {
	        	Properties properties = new Properties();
	        	properties.setProperty(systemPropertyKey, systemPropertyValue);
	            return properties;
	        }
	    };
		
		JsonNode configuration = systemPropertiesConfigurationSource.getConfiguration();
		
		String actualPropertyValue = configuration.at(JsonPointerUtils.fromProperty(systemPropertyKey)).asText();
		assertThat(actualPropertyValue, is(equalTo(systemPropertyValue)));
	}
	
	@Test
	public void testNestedSystemPropertiesWithCommonHead() {
		
		String systemProperty1Key = "/nested/property1";
		String systemProperty1Value = "value1";
		
		String systemProperty2Key = "/nested/property2";
		String systemProperty2Value = "value2";
		
		new MockUp<System>() {
			@Mock
	        Properties getProperties() {
	        	Properties properties = new Properties();
	        	properties.setProperty(systemProperty1Key, systemProperty1Value);
	        	properties.setProperty(systemProperty2Key, systemProperty2Value);
	            return properties;
	        }
	    };
		
		JsonNode configuration = systemPropertiesConfigurationSource.getConfiguration();
		
		String actualProperty1Value = configuration.at(JsonPointerUtils.fromProperty(systemProperty1Key)).asText();
		assertThat(actualProperty1Value, is(equalTo(systemProperty1Value)));
		
		String actualProperty2Value = configuration.at(JsonPointerUtils.fromProperty(systemProperty2Key)).asText();
		assertThat(actualProperty2Value, is(equalTo(systemProperty2Value)));
	}
	
	@Test
	public void testSystemPropertyWhenTheValueIsAnObject() {
		
		String systemPropertyKey = "object";
		JsonNode systemPropertyValue = ConfigurationUtils.Yaml.fromString("key: value");
		
		new MockUp<System>() {
			@Mock
	        Properties getProperties() {
	        	Properties properties = new Properties();
	        	properties.setProperty(systemPropertyKey, systemPropertyValue.toString());
	            return properties;
	        }
	    };
		
		JsonNode configuration = systemPropertiesConfigurationSource.getConfiguration();
		
		JsonNode actualPropertyValue = configuration.at(JsonPointerUtils.fromProperty(systemPropertyKey));
		assertThat(actualPropertyValue, is(equalTo(systemPropertyValue)));
	}
	
	@Test
	public void testSystemPropertyWhenTheValueIsAnArray() {
		
		String systemPropertyKey = "array";
		JsonNode systemPropertyValue = ConfigurationUtils.Yaml.fromString("- element");
		
		new MockUp<System>() {
			@Mock
	        Properties getProperties() {
	        	Properties properties = new Properties();
	        	properties.setProperty(systemPropertyKey, systemPropertyValue.toString());
	            return properties;
	        }
	    };
		
		JsonNode configuration = systemPropertiesConfigurationSource.getConfiguration();
		
		JsonNode actualPropertyValue = configuration.at(JsonPointerUtils.fromProperty(systemPropertyKey));
		assertThat(actualPropertyValue, is(equalTo(systemPropertyValue)));
	}
	
}

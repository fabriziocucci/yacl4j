package com.yacl4j.core.source;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.yacl4j.core.util.ConfigurationUtils;
import com.yacl4j.core.util.JsonPointerUtils;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import yacl4j.repackaged.com.fasterxml.jackson.databind.JsonNode;

@RunWith(JMockit.class)
public class EnvironmentVariablesConfigurationSourceTest {

	private final EnvironmentVariablesConfigurationSource environmentVariablesConfigurationSource = new EnvironmentVariablesConfigurationSource();
	
	@Test
	public void testSimpleEnvironmentVariable() {

		String environmentVariableKey = "key";
		String environmentVariableValue = "value";
		
		new MockUp<System>() {
			@Mock
			Map<String,String> getenv() {
				Map<String, String> environmentVariables = new HashMap<>();
				environmentVariables.put(environmentVariableKey, environmentVariableValue);
				return environmentVariables;
			}
		};
		
		JsonNode configuration = environmentVariablesConfigurationSource.getConfiguration();
		
		String actualEnvironmentVariableValue = configuration.at(JsonPointerUtils.fromProperty(environmentVariableKey)).asText();
		assertThat(actualEnvironmentVariableValue, is(equalTo(environmentVariableValue)));
	}
	
	@Test
	public void testNestedEnvironmentVariable() {

		String environmentVariableKey = "/nested/key";
		String environmentVariableValue = "value";
		
		new MockUp<System>() {
			@Mock
			Map<String,String> getenv() {
				Map<String, String> environmentVariables = new HashMap<>();
				environmentVariables.put(environmentVariableKey, environmentVariableValue);
				return environmentVariables;
			}
		};
		
		JsonNode configuration = environmentVariablesConfigurationSource.getConfiguration();
		
		String actualEnvironmentVariableValue = configuration.at(JsonPointerUtils.fromProperty(environmentVariableKey)).asText();
		assertThat(actualEnvironmentVariableValue, is(equalTo(environmentVariableValue)));
	}
	
	@Test
	public void testNestedEnvironmentVariablesWithCommonHead() {
		
		String environmentVariable1Key = "/nested/property1";
		String environmentVariable1Value = "value1";
		
		String environmentVariable2Key = "/nested/property2";
		String environmentVariable2Value = "value2";
		
		new MockUp<System>() {
			@Mock
			Map<String,String> getenv() {
				Map<String, String> environmentVariables = new HashMap<>();
				environmentVariables.put(environmentVariable1Key, environmentVariable1Value);
				environmentVariables.put(environmentVariable2Key, environmentVariable2Value);
				return environmentVariables;
			}
		};
		
		JsonNode configuration = environmentVariablesConfigurationSource.getConfiguration();
		
		String actualEnvironmentVariable1Value = configuration.at(JsonPointerUtils.fromProperty(environmentVariable1Key)).asText();
		assertThat(actualEnvironmentVariable1Value, is(equalTo(environmentVariable1Value)));
		
		String actualEnvironmentVariable2Value = configuration.at(JsonPointerUtils.fromProperty(environmentVariable2Key)).asText();
		assertThat(actualEnvironmentVariable2Value, is(equalTo(environmentVariable2Value)));
	}
	
	@Test
	public void testEnvironmentVariableWhenTheValueIsAnObject() {
		
		String environmentVariableKey = "object";
		JsonNode environmentVariableValue = ConfigurationUtils.Yaml.fromString("key: value");
		
		new MockUp<System>() {
			@Mock
			Map<String,String> getenv() {
				Map<String, String> environmentVariables = new HashMap<>();
				environmentVariables.put(environmentVariableKey, environmentVariableValue.toString());
				return environmentVariables;
			}
		};
		
		JsonNode configuration = environmentVariablesConfigurationSource.getConfiguration();
		
		JsonNode actualEnvironmentVariableValue = configuration.at(JsonPointerUtils.fromProperty(environmentVariableKey));
		assertThat(actualEnvironmentVariableValue, is(equalTo(environmentVariableValue)));
	}
	
	@Test
	public void testEnvironmentVariablesWhenTheValueIsAnArray() {
		
		String environmentVariableKey = "array";
		JsonNode environmentVariableValue = ConfigurationUtils.Yaml.fromString("- element");
		
		new MockUp<System>() {
			@Mock
			Map<String,String> getenv() {
				Map<String, String> environmentVariables = new HashMap<>();
				environmentVariables.put(environmentVariableKey, environmentVariableValue.toString());
				return environmentVariables;
			}
		};
		
		JsonNode configuration = environmentVariablesConfigurationSource.getConfiguration();
		
		JsonNode actualEnvironmentVariableValue = configuration.at(JsonPointerUtils.fromProperty(environmentVariableKey));
		assertThat(actualEnvironmentVariableValue, is(equalTo(environmentVariableValue)));
	}
	
}

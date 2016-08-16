package com.yacl4j.core.source;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.databind.JsonNode;
import com.yacl4j.core.util.JsonPointerUtils;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

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
	    
	    String actualEnvironmentVariableValue = configuration.at(JsonPointerUtils.fromProperty(environmentVariableKey).get()).asText();
		assertThat(actualEnvironmentVariableValue, is(equalTo(environmentVariableValue)));
	}

}

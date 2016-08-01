package com.yacl4j.core.placeholder;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.yacl4j.core.util.JacksonUtils;

public class NonRecursivePlaceholderResolverTest {
	
	private final NonRecursivePlaceholderResolver localPlaceholderResolver = new NonRecursivePlaceholderResolver();
	
	@Test
	public void testThatNothingHappensWhenTheConfigurationIsEmpty() {
		
		// ObjectMapper doesn't like empty files so we are using a MissingNode to represent an empty configuration
		// ('readTree' with an empty file results in a 'JsonMappingException: No content to map due to end-of-input')
		JsonNode applicationConfiguration = MissingNode.getInstance();
		
		localPlaceholderResolver.resolvePlaceholders(applicationConfiguration);
		
		assertThat(applicationConfiguration.size(), is(equalTo(0)));
	}
	
	/////////////////////////
	// ARRAY ELEMENT TESTS //
	/////////////////////////
	
	@Test
	public void testThatPlaceholderInArrayElementResolveToTheValueOfAnotherArrayElement() throws Exception {
		
		String configuration = String.join(System.getProperty("line.separator")
				, "- e0"
				, "- ${0}");
		
		JsonNode applicationConfiguration = JacksonUtils.yamlObjectMapper().readTree(configuration);
		
		localPlaceholderResolver.resolvePlaceholders(applicationConfiguration);
		
		String actualValue = applicationConfiguration.get(1).asText();
		String expectedValue = applicationConfiguration.get(0).asText();;
		assertThat(actualValue, is(equalTo(expectedValue)));
	}
	
	@Test
	public void testThatPlaceholderInArrayElementResolveToTheValueOfAnObjectProperty() throws Exception {
		
		String configuration = String.join(System.getProperty("line.separator")
				, "element0: e0"
				, "array:"
				, "  - ${element0}");
		
		JsonNode applicationConfiguration = JacksonUtils.yamlObjectMapper().readTree(configuration);
		
		localPlaceholderResolver.resolvePlaceholders(applicationConfiguration);
		
		assertThat(applicationConfiguration.get("array").get(0).asText(), is(equalTo(applicationConfiguration.get("element0").asText())));
	}
	
	@Test
	public void testThatMultipleButEqualPlaceholdersInArrayElementResolveToTheValueOfTheCorrespondingObjectProperty() throws Exception {
		
		String configuration = String.join(System.getProperty("line.separator")
				, "element0: e0"
				, "array:"
				, "  - ${element0},${element0}");
		
		JsonNode applicationConfiguration = JacksonUtils.yamlObjectMapper().readTree(configuration);
		
		localPlaceholderResolver.resolvePlaceholders(applicationConfiguration);
		
		String actualValue = applicationConfiguration.get("array").get(0).asText();
		String expectedValue = String.join(",", applicationConfiguration.get("element0").asText(), applicationConfiguration.get("element0").asText());
		assertThat(actualValue, is(equalTo(expectedValue)));
	}
	
	@Test
	public void testThatMultipleButDifferentPlaceholdersInArrayElementResolveToTheValueOfTheCorrespondingObjectProperties() throws Exception {
		
		String configuration = String.join(System.getProperty("line.separator")
				, "element0: e0"
				, "element1: e1"
				, "array:"
				, "  - ${element0},${element1}");
		
		JsonNode applicationConfiguration = JacksonUtils.yamlObjectMapper().readTree(configuration);
		
		localPlaceholderResolver.resolvePlaceholders(applicationConfiguration);
		
		String actualValue = applicationConfiguration.get("array").get(0).asText();
		String expectedValue = String.join(",", applicationConfiguration.get("element0").asText(), applicationConfiguration.get("element1").asText());
		assertThat(actualValue, is(equalTo(expectedValue)));
	}
	
	@Test
	public void testThatPlaceholderInArrayElementResolveToAnArray() throws Exception {
		
		String configuration = String.join(System.getProperty("line.separator")
				, "array0:"
				, "  - e0"
				, "array1:"
				, "  - ${array0}");
		
		JsonNode applicationConfiguration = JacksonUtils.yamlObjectMapper().readTree(configuration);
		
		localPlaceholderResolver.resolvePlaceholders(applicationConfiguration);
		
		JsonNode actualValue = applicationConfiguration.get("array1").get(0);
		JsonNode expectedValue = applicationConfiguration.get("array0");
		assertThat(actualValue, is(equalTo(expectedValue)));
	}
	
	@Test
	public void testThatPlaceholderInArrayElementResolveToAnObject() throws Exception {
		
		String configuration = String.join(System.getProperty("line.separator")
				, "object:"
				, "  property: value"
				, "array:"
				, "  - ${object}");
		
		JsonNode applicationConfiguration = JacksonUtils.yamlObjectMapper().readTree(configuration);
		
		localPlaceholderResolver.resolvePlaceholders(applicationConfiguration);
		
		JsonNode actualValue = applicationConfiguration.get("array").get(0);
		JsonNode expectedValue = applicationConfiguration.get("object");
		assertThat(actualValue, is(equalTo(expectedValue)));
	}
	
	@Test
	public void testThatPlaceholderInArrayElementDoesNotResolveToAnArrayWhenItIsNotTheOnlyPlaceholder() throws Exception {
		
		String configuration = String.join(System.getProperty("line.separator")
				, "property: value"
				, "array0:"
				, "  - e"
				, "array1:"
				, "  - ${property},${array0}");
		
		JsonNode applicationConfiguration = JacksonUtils.yamlObjectMapper().readTree(configuration);
		
		localPlaceholderResolver.resolvePlaceholders(applicationConfiguration);
		
		JsonNode actualValue = applicationConfiguration.get("array1").get(0);
		JsonNode expectedValue = TextNode.valueOf(String.join(",", applicationConfiguration.get("property").asText(), "${array0}"));
		assertThat(actualValue, is(equalTo(expectedValue)));
	}
	
	@Test
	public void testThatPlaceholderInArrayElementDoesNotResolveToAnObjectWhenItIsNotTheOnlyPlaceholder() throws Exception {
		
		String configuration = String.join(System.getProperty("line.separator")
				, "property: value"
				, "object:"
				, "  property: value"
				, "array:"
				, "  - ${property},${object}");
		
		JsonNode applicationConfiguration = JacksonUtils.yamlObjectMapper().readTree(configuration);
		
		localPlaceholderResolver.resolvePlaceholders(applicationConfiguration);
		
		JsonNode actualValue = applicationConfiguration.get("array").get(0);
		JsonNode expectedValue = TextNode.valueOf(String.join(",", applicationConfiguration.get("property").asText(), "${object}"));
		assertThat(actualValue, is(equalTo(expectedValue)));
	}
	
	///////////////////////////
	// OBJECT PROPERTY TESTS //
	///////////////////////////
	
	@Test
	public void testThatPlaceholderInObjectPropertyResolveToTheValueOfAnotherObjectProperty() throws Exception {
		
		String configuration = String.join(System.getProperty("line.separator")
				, "element0: e0"
				, "element1: ${element0}");
		
		JsonNode applicationConfiguration = JacksonUtils.yamlObjectMapper().readTree(configuration);
		
		localPlaceholderResolver.resolvePlaceholders(applicationConfiguration);
		
		String actualValue = applicationConfiguration.get("element1").asText();
		String expectedValue = applicationConfiguration.get("element0").asText();
		assertThat(actualValue, is(equalTo(expectedValue)));
	}
	
	@Test
	public void testThatPlaceholderInObjectPropertyResolveToTheValueOfAnArrayElement() throws Exception {
		
		String configuration = String.join(System.getProperty("line.separator")
				, "array:"
				, "  - e0"
				, "element1: ${array/0}");
		
		JsonNode applicationConfiguration = JacksonUtils.yamlObjectMapper().readTree(configuration);
		
		localPlaceholderResolver.resolvePlaceholders(applicationConfiguration);
		
		String actualValue = applicationConfiguration.get("element1").asText();
		String expectedValue = applicationConfiguration.get("array").get(0).asText();
		assertThat(actualValue, is(equalTo(expectedValue)));
	}
	
	@Test
	public void testThatPlaceholderInObjectPropertyResolveToAnArray() throws Exception {
		
		String configuration = String.join(System.getProperty("line.separator")
				, "array:"
				, "  - e0"
				, "element1: ${array}");
		
		JsonNode applicationConfiguration = JacksonUtils.yamlObjectMapper().readTree(configuration);
		
		localPlaceholderResolver.resolvePlaceholders(applicationConfiguration);
		
		JsonNode actualValue = applicationConfiguration.get("element1");
		JsonNode expectedValue = applicationConfiguration.get("array");
		assertThat(actualValue, is(equalTo(expectedValue)));
	}
	
	@Test
	public void testThatPlaceholderInObjectPropertyResolveToAnObject() throws Exception {
		
		String configuration = String.join(System.getProperty("line.separator")
				, "object:"
				, "  property0: v0"
				, "element1: ${object}");
		
		JsonNode applicationConfiguration = JacksonUtils.yamlObjectMapper().readTree(configuration);
		
		localPlaceholderResolver.resolvePlaceholders(applicationConfiguration);
		
		JsonNode actualValue = applicationConfiguration.get("element1");
		JsonNode expectedValue = applicationConfiguration.get("object");
		assertThat(actualValue, is(equalTo(expectedValue)));
	}
	
	@Test
	public void testThatPlaceholderInObjectPropertyDoesNotResolveToAnArrayWhenItIsNotTheOnlyPlaceholder() throws Exception {
		
		String configuration = String.join(System.getProperty("line.separator")
				, "property: value"
				, "array:"
				, "  - e"
				, "object:"
				, "  property: ${property},${array}");
		
		JsonNode applicationConfiguration = JacksonUtils.yamlObjectMapper().readTree(configuration);
		
		localPlaceholderResolver.resolvePlaceholders(applicationConfiguration);
		
		JsonNode actualValue = applicationConfiguration.get("object").get("property");
		JsonNode expectedValue = TextNode.valueOf(String.join(",", applicationConfiguration.get("property").asText(), "${array}"));
		assertThat(actualValue, is(equalTo(expectedValue)));
	}
	
	@Test
	public void testThatPlaceholderInObjectPropertyDoesNotResolveToAnObjectWhenItIsNotTheOnlyPlaceholder() throws Exception {
		
		String configuration = String.join(System.getProperty("line.separator")
				, "property: value"
				, "object0:"
				, "  property: value"
				, "object1:"
				, "  property: ${property},${object0}");
		
		JsonNode applicationConfiguration = JacksonUtils.yamlObjectMapper().readTree(configuration);
		
		localPlaceholderResolver.resolvePlaceholders(applicationConfiguration);
		
		JsonNode actualValue = applicationConfiguration.get("object1").get("property");
		JsonNode expectedValue = TextNode.valueOf(String.join(",", applicationConfiguration.get("property").asText(), "${object0}"));
		assertThat(actualValue, is(equalTo(expectedValue)));
	}
	
}

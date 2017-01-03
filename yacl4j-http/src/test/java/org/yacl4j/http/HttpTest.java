package org.yacl4j.http;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.ClassRule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.yacl4j.core.ConfigurationBuilder;

import yacl4j.repackaged.com.fasterxml.jackson.databind.JsonNode;

public class HttpTest {

	@ClassRule
	public static final WireMockRule WIRE_MOCK_RULE = new WireMockRule(8080);
	
	@Test
	public void testThatYamlConfigurationIsRetrieved() {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
			.source(HttpConfigurationSourceBuilder.newBuilder("http://localhost:8080/http/yaml").yaml())
			.build(JsonNode.class);
		
		assertThat(configuration.at("/http").asText(), is(equalTo("yaml")));
	}
	
	@Test
	public void testThatJsonConfigurationIsRetrieved() {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
			.source(HttpConfigurationSourceBuilder.newBuilder("http://localhost:8080/http/json").json())
			.build(JsonNode.class);
		
		assertThat(configuration.at("/http").asText(), is(equalTo("json")));
	}
	
	@Test
	public void testThatPropertiesConfigurationIsRetrieved() {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
			.source(HttpConfigurationSourceBuilder.newBuilder("http://localhost:8080/http/properties").properties())
			.build(JsonNode.class);
		
		assertThat(configuration.at("/http").asText(), is(equalTo("properties")));
	}
	
}

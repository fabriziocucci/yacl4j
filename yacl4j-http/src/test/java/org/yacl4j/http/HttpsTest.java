package org.yacl4j.http;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.net.ssl.SSLContext;

import org.glassfish.jersey.SslConfigurator;
import org.junit.ClassRule;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.yacl4j.core.ConfigurationBuilder;

public class HttpsTest {

	@ClassRule
	public static final WireMockRule WIRE_MOCK_RULE = new WireMockRule(wireMockConfig()
		    .httpsPort(8443)
		    .keystorePath("src/test/resources/test.jks")
		    .keystorePassword("changeit"));
	
	@Test
	public void testThatYamlConfigurationIsRetrieved() {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
			.source(HttpConfigurationSourceBuilder.newBuilder("https://localhost:8443/https/yaml").sslContext(sslContext()).yaml())
			.build(JsonNode.class);
		
		assertThat(configuration.at("/https").asText(), is(equalTo("yaml")));
	}
	
	@Test
	public void testThatJsonConfigurationIsRetrieved() {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
			.source(HttpConfigurationSourceBuilder.newBuilder("https://localhost:8443/https/json").sslContext(sslContext()).json())
			.build(JsonNode.class);
		
		assertThat(configuration.at("/https").asText(), is(equalTo("json")));
	}
	
	@Test
	public void testThatPropertiesConfigurationIsRetrieved() {
		
		JsonNode configuration = ConfigurationBuilder.newBuilder()
			.source(HttpConfigurationSourceBuilder.newBuilder("https://localhost:8443/https/properties").sslContext(sslContext()).properties())
			.build(JsonNode.class);
		
		assertThat(configuration.at("/https").asText(), is(equalTo("properties")));
	}
	
	private static SSLContext sslContext() {
		return SslConfigurator.newInstance()
				.trustStoreFile("src/test/resources/test.jks")
				.trustStorePassword("changeit")
				.createSSLContext();
	}
	
}

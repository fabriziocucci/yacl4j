package org.yacl4j.http;

import java.util.Objects;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;

import com.yacl4j.core.ConfigurationSource;
import com.yacl4j.core.util.ConfigurationUtils;

public class HttpConfigurationSourceBuilder {

	private String uri;
	private String mediaType;
	private ClientBuilder clientBuilder;
	
	private HttpConfigurationSourceBuilder(String uri) {
		this.uri = uri;
		this.mediaType = MediaType.WILDCARD;
		this.clientBuilder = ClientBuilder.newBuilder();
	}
	
	public static HttpConfigurationSourceBuilder newBuilder(String uri) {
		return new HttpConfigurationSourceBuilder(uri);
	}
	
	public HttpConfigurationSourceBuilder target(String uri) {
		this.uri = Objects.requireNonNull(uri);
		return this;
	}
	
	public HttpConfigurationSourceBuilder mediaType(String mediaType) {
		if (mediaType != null) {
			this.mediaType = mediaType;
		} else {
			this.mediaType = MediaType.WILDCARD;
		}
		return this;
	}
	
	public HttpConfigurationSourceBuilder sslContext(SSLContext sslContext) {
		if (sslContext != null) {
			this.clientBuilder = ClientBuilder.newBuilder().sslContext(sslContext);
		} else {
			this.clientBuilder = ClientBuilder.newBuilder();
		}
		return this;
	}

	public ConfigurationSource yaml() {
		return new HttpConfigurationSource(buildInvocation(), ConfigurationUtils.Yaml::fromString);
	}

	public ConfigurationSource json() {
		return new HttpConfigurationSource(buildInvocation(), ConfigurationUtils.Json::fromString);
	}

	public ConfigurationSource properties() {
		return new HttpConfigurationSource(buildInvocation(), ConfigurationUtils.Properties::fromString);
	}
	
	private Invocation buildInvocation() {
		return clientBuilder.build()
				.target(uri)
				.request()
				.accept(mediaType)
				.buildGet();
	}
	
}

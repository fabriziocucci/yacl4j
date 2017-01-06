package com.yacl4j.core;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.yacl4j.core.source.optional.ConfigurationSourceNotAvailableException;

import mockit.integration.junit4.JMockit;
import yacl4j.repackaged.com.fasterxml.jackson.databind.JsonNode;

@RunWith(JMockit.class)
public class ConfigurationBuilderWithSingleOptionalSourceTest {

	///////////////////
	// optional file //
	///////////////////

	@Test
	public void testConfigurationBuilderWhenOptionalYamlFileDoesNotExists() {

		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.optionalSource().fromFile(new File("i-dont-exist.yaml"))
				.build(JsonNode.class);

		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}

	@Test
	public void testConfigurationBuilderWhenOptionalJsonFileDoesNotExists() {

		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.optionalSource().fromFile(new File("i-dont-exist.json"))
				.build(JsonNode.class);

		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}

	@Test
	public void testConfigurationBuilderWhenOptionalPropertiesFileDoesNotExists() {

		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.optionalSource().fromFile(new File("i-dont-exist.properties"))
				.build(JsonNode.class);

		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}

	//////////////////////////////////
	// optional file from classpath //
	//////////////////////////////////

	@Test
	public void testConfigurationBuilderWhenOptionalYamlFileFromClasspathDoesNotExists() {

		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.optionalSource().fromFileOnClasspath("i-dont-exist.yaml")
				.build(JsonNode.class);

		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}

	@Test
	public void testConfigurationBuilderWhenOptionalJsonFileFromClasspathDoesNotExists() {

		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.optionalSource().fromFileOnClasspath("i-dont-exist.json")
				.build(JsonNode.class);

		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}

	@Test
	public void testConfigurationBuilderWhenOptionalPropertiesFileFromClasspathDoesNotExists() {

		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.optionalSource().fromFileOnClasspath("i-dont-exist.properties")
				.build(JsonNode.class);

		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}

	/////////////////////////////
	// optional file from path //
	/////////////////////////////

	@Test
	public void testConfigurationBuilderWhenOptionalYamlFileFromPathDoesNotExists() {

		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.optionalSource().fromFileOnPath("i-dont-exist.yaml")
				.build(JsonNode.class);

		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}

	@Test
	public void testConfigurationBuilderWhenOptionalJsonFileFromPathDoesNotExists() {

		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.optionalSource().fromFileOnPath("i-dont-exist.json")
				.build(JsonNode.class);

		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}

	@Test
	public void testConfigurationBuilderWhenOptionalPropertiesFileFromPathDoesNotExists() {

		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.optionalSource().fromFileOnPath("i-dont-exist.properties")
				.build(JsonNode.class);

		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}

	/////////////////////////////////////////
	// optional eager configuration source //
	/////////////////////////////////////////
	
	private static class EagerConfigurationSource implements ConfigurationSource {

		private EagerConfigurationSource() {
			throw new ConfigurationSourceNotAvailableException();
		}
		
		@Override
		public JsonNode getConfiguration() {
			return null;
		}
		
	}
	
	@Test(expected=ConfigurationSourceNotAvailableException.class)
	public void testConfigurationBuilderWhenOptionalEagerConfigurationSourceIsNotAvailableAndInstanceIsProvided() {
		ConfigurationBuilder.newBuilder()
				.optionalSource(new EagerConfigurationSource())
				.build(JsonNode.class);
	}
	
	@Test
	public void testConfigurationBuilderWhenOptionalEagerConfigurationSourceIsNotAvailableAndFactoryIdProvided() {

		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.optionalSource(() -> new EagerConfigurationSource())
				.build(JsonNode.class);

		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}
	
	/////////////////////////////////////////
	// optional lazy configuration source //
	/////////////////////////////////////////
	
	private static class LazyConfigurationSource implements ConfigurationSource {
		
		@Override
		public JsonNode getConfiguration() {
			throw new ConfigurationSourceNotAvailableException();
		}
		
	}

	@Test
	public void testConfigurationBuilderWhenOptionalLazyConfigurationSourceIsNotAvailableAndInstanceIsProvided() {

		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.optionalSource(new LazyConfigurationSource())
				.build(JsonNode.class);

		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}
	
	@Test
	public void testConfigurationBuilderWhenOptionalLazyConfigurationSourceIsNotAvailableAndFactoryIdProvided() {

		JsonNode configuration = ConfigurationBuilder.newBuilder()
				.optionalSource(() -> new LazyConfigurationSource())
				.build(JsonNode.class);

		assertThat(configuration, is(notNullValue()));
		assertThat(configuration.fields().hasNext(), is(false));
	}
	
}

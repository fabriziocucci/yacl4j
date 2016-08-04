package com.yacl4j.core;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

public class ConfigurationBuilderTest {

	public static interface ConfigurationForSourceTest {

		public String getField();
		public int getInteger();
		public NestedApplicationConfiguration getNestedApplicationConfiguration();
		
		static interface NestedApplicationConfiguration {
			public String getNestedField();
		}
		
	}
	
	@Test
	public void testConfigurationBuilderWhenYamlFileIsTheSourceAndNoOverrideIsProvided() throws IOException {
		
		String configuration = String.join(System.getProperty("line.separator")
				, "field: Hi, I'm a field!"
				, "integer: 42"
				, "nestedApplicationConfiguration:"
				, "  nestedField: Hi, I'm a NESTED field!");
		
		ConfigurationBuilder configurationBuilder = ConfigurationBuilder.newBuilder()
				.source().file(createConfigurationFile(configuration));
		
		ConfigurationForSourceTest applicationConfiguration = configurationBuilder.build(ConfigurationForSourceTest.class);
		
		assertThat(applicationConfiguration, is(notNullValue()));
		assertThat(applicationConfiguration.getField(), is(equalTo("Hi, I'm a field!")));
		assertThat(applicationConfiguration.getInteger(), is(equalTo(42)));
		
		assertThat(applicationConfiguration.getNestedApplicationConfiguration(), is(notNullValue()));
		assertThat(applicationConfiguration.getNestedApplicationConfiguration().getNestedField(), is(equalTo("Hi, I'm a NESTED field!")));
	}
	
	@Test
	public void testConfigurationBuilderWhenYamlFileIsTheSourceAndSystemPropertiesOverride() throws IOException {
		
		String configuration = String.join(System.getProperty("line.separator")
				, "field: Hi, I'm a field!"
				, "integer: 42"
				, "nestedApplicationConfiguration:"
				, "  nestedField: Hi, I'm a NESTED field!");
		
		ConfigurationBuilder configurationBuilder = ConfigurationBuilder.newBuilder()
				.source().file(createConfigurationFile(configuration))
				.source().systemProperties();
		
		System.setProperty("field", "Hi, I'm a field...OVERRIDDEN!");
		System.setProperty("integer", "22");
		System.setProperty("nestedApplicationConfiguration.nestedField", "Hi, I'm a NESTED field...OVERRIDDEN!");
		ConfigurationForSourceTest applicationConfiguration = configurationBuilder.build(ConfigurationForSourceTest.class);
		
		assertThat(applicationConfiguration, is(notNullValue()));
		assertThat(applicationConfiguration.getField(), is(equalTo(System.getProperty("field"))));
		assertThat(applicationConfiguration.getInteger(), is(equalTo(Integer.parseInt(System.getProperty("integer")))));
		
		assertThat(applicationConfiguration.getNestedApplicationConfiguration(), is(notNullValue()));
		assertThat(applicationConfiguration.getNestedApplicationConfiguration().getNestedField(), is(equalTo(System.getProperty("nestedApplicationConfiguration.nestedField"))));
	}
	
	public static interface ConfigurationForPlaceholderTest {

		public String getName();
		public String getGreeting();
		
	};
	
	@Test
	public void testThatPropertyPlaceholderIsCorrectlyResolvedWhenSystemPropertiesSourceIsNotUsed() throws IOException {
		
		String configuration = String.join(System.getProperty("line.separator")
				, "greeting: Hello ${name}"
				, "name: World");
		
		ConfigurationBuilder configurationBuilder = ConfigurationBuilder.newBuilder()
				.source().file(createConfigurationFile(configuration));
		
		ConfigurationForPlaceholderTest applicationConfiguration = configurationBuilder.build(ConfigurationForPlaceholderTest.class);
		
		assertThat(applicationConfiguration, is(notNullValue()));
		assertThat(applicationConfiguration.getName(), is(equalTo("World")));
		assertThat(applicationConfiguration.getGreeting(), is(equalTo("Hello World")));
	}
	
	@Test
	public void testThatPropertyPlaceholderIsCorrectlyResolvedWhenSystemPropertiesSourceIsUsed() throws IOException {
		
		String configuration = String.join(System.getProperty("line.separator")
				, "greeting: Hello ${name}"
				, "name: World");
		
		ConfigurationBuilder configurationBuilder = ConfigurationBuilder.newBuilder()
				.source().file(createConfigurationFile(configuration))
				.source().systemProperties();
		
		System.setProperty("name", "Config");
		ConfigurationForPlaceholderTest applicationConfiguration = configurationBuilder.build(ConfigurationForPlaceholderTest.class);
		
		assertThat(applicationConfiguration, is(notNullValue()));
		assertThat(applicationConfiguration.getName(), is(equalTo("Config")));
		assertThat(applicationConfiguration.getGreeting(), is(equalTo("Hello Config")));
	}
	
	///////////
	// UTILS //
	///////////
	
	private static File createConfigurationFile(String configuration) throws IOException {
		File configurationFile = File.createTempFile("application", ".yaml");
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configurationFile))) {
			bufferedWriter.write(configuration);
			return configurationFile;
		}
	}
	
}

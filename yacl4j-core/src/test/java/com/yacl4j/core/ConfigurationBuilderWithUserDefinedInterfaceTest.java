package com.yacl4j.core;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import com.yacl4j.core.ConfigurationBuilderWithUserDefinedInterfaceTest.ApplicationConfiguration.NestedApplicationConfiguration;

public class ConfigurationBuilderWithUserDefinedInterfaceTest {

	public static interface ApplicationConfiguration {

		public int getIntField();
		public boolean getBooleanField();
		public String getStringField();
		
		public NestedApplicationConfiguration getNestedApplicationConfiguration();
		
		static interface NestedApplicationConfiguration {
			
			public int getIntField();
			public boolean getBooleanField();
			public String getStringField();
			
		}
		
	}
	
	@Test
	public void testConfigurationBuilderWithYamlFileAndUserDefinedInterface() throws Exception {
		
		String configurationAsString = String.join(System.getProperty("line.separator")
				, "intField: 42"
				, "booleanField: true"
				, "stringField: yacl4j is so cool"
				, "nestedApplicationConfiguration:"
				, "  intField: 84"
				, "  booleanField: false"
				, "  stringField: yacl4j is so simple");
		
		ApplicationConfiguration applicationConfiguration = ConfigurationBuilder.newBuilder()
				.source().fromFile(createConfigurationFile(configurationAsString, ".yaml"))
				.build(ApplicationConfiguration.class);
		
		assertThat(applicationConfiguration, is(notNullValue()));
		assertThat(applicationConfiguration.getIntField(), is(equalTo(42)));
		assertThat(applicationConfiguration.getBooleanField(), is(equalTo(true)));
		assertThat(applicationConfiguration.getStringField(), is(equalTo("yacl4j is so cool")));
		
		NestedApplicationConfiguration nestedApplicationConfiguration = applicationConfiguration.getNestedApplicationConfiguration();
		
		assertThat(nestedApplicationConfiguration, is(notNullValue()));
		assertThat(nestedApplicationConfiguration.getIntField(), is(equalTo(84)));
		assertThat(nestedApplicationConfiguration.getBooleanField(), is(equalTo(false)));
		assertThat(nestedApplicationConfiguration.getStringField(), is(equalTo("yacl4j is so simple")));
	}
	
	///////////
	// UTILS //
	///////////
	
	private static File createConfigurationFile(String configuration, String fileExtension) throws IOException {
		File configurationFile = File.createTempFile("application", fileExtension);
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configurationFile))) {
			bufferedWriter.write(configuration);
			return configurationFile;
		}
	}
	
}

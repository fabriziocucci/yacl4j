package com.yacl4j.core.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public class ConfigurationUtilsTest {

	@Test
	public void testMergeWhenNonOverlappingTreesAreMerged() throws JsonProcessingException, IOException {
		
		JsonNode mainNode = ConfigurationUtils.Yaml.fromString("hello: yaml");
		JsonNode updateNode = ConfigurationUtils.Yaml.fromString("goodbye: json");
		
		JsonNode merge = ConfigurationUtils.merge(mainNode, updateNode);
		assertThat(merge.get("hello"), is(equalTo(mainNode.get("hello"))));
		assertThat(merge.get("goodbye"), is(equalTo(updateNode.get("goodbye"))));
	}
	
	@Test
	public void testMergeWhenOverlappingTreesAreMerged() throws JsonProcessingException, IOException {
		
		JsonNode mainNode = ConfigurationUtils.Yaml.fromString("hello: yaml");
		JsonNode updateNode = ConfigurationUtils.Yaml.fromString("hello: json");
		
		JsonNode merge = ConfigurationUtils.merge(mainNode, updateNode);
		assertThat(merge.get("hello"), is(equalTo(updateNode.get("hello"))));
	}
	
}

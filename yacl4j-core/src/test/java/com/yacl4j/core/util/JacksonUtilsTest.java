package com.yacl4j.core.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.yacl4j.core.util.JacksonUtils;

public class JacksonUtilsTest {

	@Test
	public void testMergeWhenNonOverlappingTreesAreMerged() throws JsonProcessingException, IOException {
		
		JsonNode mainNode = JacksonUtils.yamlObjectMapper().readTree("hello: yaml");
		JsonNode updateNode = JacksonUtils.yamlObjectMapper().readTree("goodbye: json");
		
		JsonNode merge = JacksonUtils.merge(mainNode, updateNode);
		assertThat(merge.get("hello"), is(equalTo(mainNode.get("hello"))));
		assertThat(merge.get("goodbye"), is(equalTo(updateNode.get("goodbye"))));
	}
	
	@Test
	public void testMergeWhenOverlappingTreesAreMerged() throws JsonProcessingException, IOException {
		
		JsonNode mainNode = JacksonUtils.yamlObjectMapper().readTree("hello: yaml");
		JsonNode updateNode = JacksonUtils.yamlObjectMapper().readTree("hello: json");
		
		JsonNode merge = JacksonUtils.merge(mainNode, updateNode);
		assertThat(merge.get("hello"), is(equalTo(updateNode.get("hello"))));
	}
	
}

package com.yacl4j.core;

import java.util.Iterator;
import java.util.Map.Entry;

import yacl4j.repackaged.com.fasterxml.jackson.databind.JsonNode;
import yacl4j.repackaged.com.fasterxml.jackson.databind.node.ArrayNode;
import yacl4j.repackaged.com.fasterxml.jackson.databind.node.MissingNode;
import yacl4j.repackaged.com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class AbstractNodeTransformer {
	
	protected void transform(JsonNode configuration) {
		transform(configuration, configuration, MissingNode.getInstance(), "");
	}
	
	private void transform(JsonNode root, JsonNode currentNode, JsonNode parentNode, String fieldOrIndexInParent) {
		switch (currentNode.getNodeType()) {
			case OBJECT: transformObjectNode(root, currentNode, parentNode); break;
			case ARRAY: transformArrayNode(root, currentNode, parentNode); break;
			default: transformValueNodeBasedOnParent(root, currentNode, parentNode, fieldOrIndexInParent); break;
		}
	}
	
	private void transformObjectNode(JsonNode root, JsonNode currentNode, JsonNode parentNode) {
		Iterator<Entry<String, JsonNode>> fields = currentNode.fields();
		while (fields.hasNext()) {
			Entry<String, JsonNode> childEntry = fields.next();
			transform(root, childEntry.getValue(), currentNode, childEntry.getKey());
		}
	}
	
	private void transformArrayNode(JsonNode root, JsonNode currentNode, JsonNode parentNode) {
		for (int i = 0; i < currentNode.size(); i++) {
			transform(root, currentNode.get(i), currentNode, Integer.toString(i));
		}
	}
	
	private void transformValueNodeBasedOnParent(JsonNode root, JsonNode currentNode, JsonNode parentNode, String fieldOrIndexInParent) {
		switch (parentNode.getNodeType()) {
			case OBJECT: ((ObjectNode)parentNode).set(fieldOrIndexInParent, transformValueNode(root, currentNode)); break;
			case ARRAY: ((ArrayNode)parentNode).set(Integer.valueOf(fieldOrIndexInParent), transformValueNode(root, currentNode)); break;
			default: break;
		}
	}
	
	abstract protected JsonNode transformValueNode(JsonNode root, JsonNode currentNode);
	
}

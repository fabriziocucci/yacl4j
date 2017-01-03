package com.yacl4j.core.placeholder;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yacl4j.core.PlaceholderResolver;
import com.yacl4j.core.util.JsonPointerUtils;

import yacl4j.repackaged.com.fasterxml.jackson.core.JsonPointer;
import yacl4j.repackaged.com.fasterxml.jackson.databind.JsonNode;
import yacl4j.repackaged.com.fasterxml.jackson.databind.node.ArrayNode;
import yacl4j.repackaged.com.fasterxml.jackson.databind.node.MissingNode;
import yacl4j.repackaged.com.fasterxml.jackson.databind.node.ObjectNode;
import yacl4j.repackaged.com.fasterxml.jackson.databind.node.TextNode;

public class NonRecursivePlaceholderResolver implements PlaceholderResolver {
	
	private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("(\\$\\{(.*?)\\})");
	
	public void resolvePlaceholders(JsonNode applicationConfiguration) {
		resolve(applicationConfiguration, applicationConfiguration, MissingNode.getInstance(), "");
	}
	
	private void resolve(JsonNode root, JsonNode currentNode, JsonNode parentNode, String fieldOrIndexInParent) {
		switch (currentNode.getNodeType()) {
			case OBJECT: resolveInObjectNode(root, currentNode, parentNode); break;
			case ARRAY: resolveInArrayNode(root, currentNode, parentNode); break;
			default: resolveInValueNodeBasedOnParent(root, currentNode, parentNode, fieldOrIndexInParent); break;
		}
	}
	
	private void resolveInObjectNode(JsonNode root, JsonNode currentNode, JsonNode parentNode) {
		Iterator<Entry<String, JsonNode>> fields = currentNode.fields();
		while (fields.hasNext()) {
			Entry<String, JsonNode> childEntry = fields.next();
			resolve(root, childEntry.getValue(), currentNode, childEntry.getKey());
		}
	}
	
	private void resolveInArrayNode(JsonNode root, JsonNode currentNode, JsonNode parentNode) {
		for (int i = 0; i < currentNode.size(); i++) {
			resolve(root, currentNode.get(i), currentNode, Integer.toString(i));
		}
	}
	
	private void resolveInValueNodeBasedOnParent(JsonNode root, JsonNode currentNode, JsonNode parentNode, String fieldOrIndexInParent) {
		switch (parentNode.getNodeType()) {
			case OBJECT: ((ObjectNode)parentNode).set(fieldOrIndexInParent, resolveInValueNode(root, currentNode)); break;
			case ARRAY: ((ArrayNode)parentNode).set(Integer.valueOf(fieldOrIndexInParent), resolveInValueNode(root, currentNode)); break;
			default: break;
		}
	}
	
	private JsonNode resolveInValueNode(JsonNode root, JsonNode currentNode) {
		List<JsonPointerPlaceholder> matches = findPlaceholders(currentNode);
		if (matches.size() == 1 && matches.get(0).placeholder.equals(currentNode.textValue())) {
			return resolveValueNodeByDereferencingJsonNode(root, currentNode, matches.get(0));
		} else {
			return resolveValueNodeByDereferencingValueNodes(root, currentNode, matches);
		}
	}
	
	private JsonNode resolveValueNodeByDereferencingJsonNode(JsonNode root, JsonNode currentNode, JsonPointerPlaceholder match) {
		JsonNode replacementNode = root.at(match.jsonPointer);
		if (replacementNode.isMissingNode()) {
			return currentNode;
		} else {
			return replacementNode;
		}
	}
	
	private JsonNode resolveValueNodeByDereferencingValueNodes(JsonNode root, JsonNode currentNode, List<JsonPointerPlaceholder> matches) {
		String resolvedValue = currentNode.asText();
		for (JsonPointerPlaceholder match : matches) {
			JsonNode replacementNode = root.at(match.jsonPointer);
			if (!replacementNode.isMissingNode() && !replacementNode.isContainerNode()) {
				resolvedValue = resolvedValue.replaceAll(Pattern.quote(match.placeholder), Matcher.quoteReplacement(replacementNode.asText()));
			}
		}
		return TextNode.valueOf(resolvedValue);
	}
	
	private List<JsonPointerPlaceholder> findPlaceholders(JsonNode jsonNode) {
		List<JsonPointerPlaceholder> jsonPointerPlaceholders = new LinkedList<>();
		Matcher matcher = PLACEHOLDER_PATTERN.matcher(jsonNode.asText());
		while (matcher.find()) {
			JsonPointerPlaceholder.parseJsonPointerPlaceholder(matcher)
				.ifPresent(jsonPointerPlaceholders::add);
		}
		return jsonPointerPlaceholders;
	}
	
	private static class JsonPointerPlaceholder {
		
		private String placeholder;
		private JsonPointer jsonPointer;
		
		private JsonPointerPlaceholder(String placeholder, JsonPointer jsonPointer) {
			this.placeholder = placeholder;
			this.jsonPointer = jsonPointer;
		}
		
		private static Optional<JsonPointerPlaceholder> parseJsonPointerPlaceholder(Matcher matcher) {
			if (matcher.groupCount() == 2) {
				String placeholder = matcher.group(1);
				String property = matcher.group(2);
				JsonPointer jsonPointer = JsonPointerUtils.fromProperty(property);
				return Optional.of(new JsonPointerPlaceholder(placeholder, jsonPointer));
			}
			return Optional.empty();
		}
		
	}
	
}

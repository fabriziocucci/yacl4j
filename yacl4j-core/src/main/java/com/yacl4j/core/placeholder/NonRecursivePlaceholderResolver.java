package com.yacl4j.core.placeholder;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yacl4j.core.AbstractNodeTransformer;
import com.yacl4j.core.PlaceholderResolver;
import com.yacl4j.core.util.JsonPointerUtils;

import yacl4j.repackaged.com.fasterxml.jackson.core.JsonPointer;
import yacl4j.repackaged.com.fasterxml.jackson.databind.JsonNode;
import yacl4j.repackaged.com.fasterxml.jackson.databind.node.TextNode;

public class NonRecursivePlaceholderResolver extends AbstractNodeTransformer implements PlaceholderResolver {
	
	private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("(\\$\\{(.*?)\\})");
	
	@Override
	public void resolvePlaceholders(JsonNode applicationConfiguration) {
		transform(applicationConfiguration);
	}
	
	@Override
	protected JsonNode transformValueNode(JsonNode root, JsonNode currentNode) {
		List<JsonPointerPlaceholder> matches = findPlaceholders(currentNode);
		if (matches.isEmpty()) {
			return currentNode;
		} else if (matches.size() == 1 && matches.get(0).placeholder.equals(currentNode.textValue())) {
			return resolveValueNodeByDereferencingJsonNode(root, currentNode, matches.get(0));
		} else {
			return resolveValueNodeByDereferencingValueNodes(root, currentNode, matches);
		}
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

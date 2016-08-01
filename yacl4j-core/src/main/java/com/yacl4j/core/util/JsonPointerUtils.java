package com.yacl4j.core.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerUtils {

	private static final JsonPointer EMPTY_JSON_POINTER = JsonPointer.compile("");
	
	public static Optional<JsonPointer> fromProperty(String property) {
		try {
			String jsonPointerAsString = "/" + property.replaceAll("\\.", "/");
			return Optional.of(JsonPointer.compile(jsonPointerAsString));
		} catch(IllegalArgumentException illegalArgumentException) {
			return Optional.empty();
		}
	}
	
	public static List<JsonPointer> heads(JsonPointer jsonPointer) {
		LinkedList<JsonPointer> heads = new LinkedList<>();
		while (!jsonPointer.equals(EMPTY_JSON_POINTER)) {
			jsonPointer = jsonPointer.head();
			heads.addFirst(jsonPointer);
		}
		return heads;
	}
	
}

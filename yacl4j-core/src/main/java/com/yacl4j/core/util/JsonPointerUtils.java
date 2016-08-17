package com.yacl4j.core.util;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerUtils {

	private static final JsonPointer EMPTY_JSON_POINTER = JsonPointer.compile("");

	public static JsonPointer fromProperty(String property) {
		if (property.startsWith("/") || property.isEmpty()) {
			return JsonPointer.compile(property);
		} else {
			return JsonPointer.compile("/" + property);
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

package com.yacl4j.core.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class FileUtils {

	private FileUtils() { }
	
	public static File toFile(URL url) {
		try {
			return Paths.get(url.toURI()).toFile();
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}
	}
	
}

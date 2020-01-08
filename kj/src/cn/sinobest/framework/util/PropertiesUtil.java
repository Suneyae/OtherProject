package cn.sinobest.framework.util;

import cn.sinobest.framework.comm.Environment;
import cn.sinobest.framework.comm.exception.AppException;
import java.io.InputStream;
import java.net.URL;

public class PropertiesUtil {
	public static InputStream getResourceAsStream(String resource) {
		String stripped = resource.startsWith("/") ? resource.substring(1) : resource;

		InputStream stream = null;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader != null) {
			stream = classLoader.getResourceAsStream(stripped);
		}
		if (stream == null) {
			stream = Environment.class.getResourceAsStream(resource);
		}
		if (stream == null) {
			stream = Environment.class.getClassLoader().getResourceAsStream(stripped);
		}
		if (stream == null) {
			throw new AppException(resource + " not found");
		}
		return stream;
	}

	public static URL getResourcePath(String resource) {
		String stripped = resource.startsWith("/") ? resource.substring(1) : resource;

		URL stream = null;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader != null) {
			stream = classLoader.getResource(stripped);
		}
		if (stream == null) {
			stream = Environment.class.getResource(resource);
		}
		if (stream == null) {
			stream = Environment.class.getClassLoader().getResource(stripped);
		}
		if (stream == null) {
			throw new AppException(resource + " not found");
		}
		return stream;
	}
}
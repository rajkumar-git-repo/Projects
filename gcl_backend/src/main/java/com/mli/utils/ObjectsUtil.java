package com.mli.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public final class ObjectsUtil {

	public static <T> boolean isNull(T t) {
		return Objects.isNull(t);
	}

	public static <T> boolean isNotNull(T t) {
		return !isNull(t);
	}

	public static <T> boolean isNullOrEmpty(Collection<T> t) {
		return isNull(t) || t.isEmpty();
	}

	public static <K, V> boolean isNullOrEmpty(Map<K, V> t) {
		return isNull(t) || t.isEmpty();
	}
}

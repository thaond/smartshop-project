package com.google.gson;

import com.google.gson.annotations.Exclude;

/**
 * @author VoMinhTam
 * 
 */
final class ExcludeAnnotationSerializationExclusionStrategy implements
		ExclusionStrategy {

	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

	public boolean shouldSkipField(FieldAttributes f) {
		Exclude annotation = f.getAnnotation(Exclude.class);
		if (annotation == null) {
			return false;
		}
		return true;
	}
}

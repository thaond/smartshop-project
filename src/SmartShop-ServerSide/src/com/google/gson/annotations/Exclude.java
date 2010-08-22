package com.google.gson.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author VoMinhTam
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Exclude {
	/**
	 * If {@code true}, the field marked with this annotation is excluded out in
	 * the JSON while serializing. If {@code false}, the field marked with this
	 * annotation is skipped from the serialized output. Defaults to {@code
	 * true}.
	 * 
	 * @since 1.4
	 */
	public boolean serialize() default false;

	/**
	 * If {@code true}, the field marked with this annotation is excluded from deserialization
	 * from the JSON. If {@code false}, the field marked with this annotation is
	 * skipped during deserialization. Defaults to {@code true}.
	 * 
	 * @since 1.4
	 */
	public boolean deserialize() default false;
}

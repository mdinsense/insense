package com.ensense.insense.core.analytics.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Tags {
	
	 String value() default "default";
	
}
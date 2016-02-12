package com.ensense.insense.data.analytics.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Tags {
	
	 String value() default "default";
	
}
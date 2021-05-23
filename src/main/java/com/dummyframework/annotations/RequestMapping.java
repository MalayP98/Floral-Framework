package com.dummyframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.dummyframework.utils.RequestMethod;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping {

  String value() default "";

  RequestMethod method() default RequestMethod.GET;

  String consumes() default "application/json";

  String produces() default "application/json";

}

package tech.itpark.http.annotation;

import org.springframework.core.annotation.AliasFor;
import tech.itpark.http.enums.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(httpMethod = HttpMethod.GET)
public @interface GetMapping {
    @AliasFor(annotation = RequestMapping.class)
    String path() default "";

    @AliasFor(annotation = RequestMapping.class)
    String value() default "";
}

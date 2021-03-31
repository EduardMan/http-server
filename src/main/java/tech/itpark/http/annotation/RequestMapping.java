package tech.itpark.http.annotation;

import org.springframework.core.annotation.AliasFor;
import tech.itpark.http.enums.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    HttpMethod httpMethod();

    @AliasFor("value")
    String path() default "";

    @AliasFor("path")
    String value() default "";
}

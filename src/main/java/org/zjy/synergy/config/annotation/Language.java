package org.zjy.synergy.config.annotation;

import org.zjy.service.base.PropertiesService;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by junyan Zhang on 14-3-16.
 */

@Documented
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RUNTIME)
public @interface Language {
    int value() default PropertiesService.EN;
}

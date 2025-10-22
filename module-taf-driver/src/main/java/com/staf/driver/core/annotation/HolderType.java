package com.staf.driver.core.annotation;

import com.staf.driver.core.Type;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HolderType {
    Type type() default Type.SELENIDE;
}

package com.popcorn.jrp.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation để kiểm tra role của user
 * Sử dụng trên method trong Controller
 *
 * Example:
 * @RequireRole({"admin"})
 * @RequireRole({"candidate", "employer"})
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    /**
     * Danh sách các role được phép truy cập
     * @return array of role names
     */
    String[] value();
}

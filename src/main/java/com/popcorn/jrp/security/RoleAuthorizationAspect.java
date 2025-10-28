package com.popcorn.jrp.security;

import com.popcorn.jrp.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Aspect để tự động kiểm tra role trước khi thực thi method
 */
@Aspect
@Component
@Slf4j
public class RoleAuthorizationAspect {

    /**
     * Intercept tất cả methods có @RequireRole annotation
     */
    @Before("@annotation(com.popcorn.jrp.security.RequireRole)")
    public void checkRole(JoinPoint joinPoint) {
        // Lấy authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Unauthorized access attempt - No authentication found");
            throw new CustomException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        // Lấy annotation từ method
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequireRole requireRole = method.getAnnotation(RequireRole.class);

        if (requireRole == null) {
            return;
        }

        // Lấy danh sách role yêu cầu
        String[] requiredRoles = requireRole.value();

        // Lấy authorities của user hiện tại
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Kiểm tra user có ít nhất 1 role yêu cầu
        boolean hasRole = false;
        for (String role : requiredRoles) {
            String requiredAuthority = "ROLE_" + role.toUpperCase();
            hasRole = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(auth -> auth.equals(requiredAuthority));

            if (hasRole) {
                log.debug("Access granted for user {} with role {}",
                        authentication.getName(), role);
                break;
            }
        }

        if (!hasRole) {
            String userRoles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));

            log.warn("Access denied for user {} with roles [{}]. Required roles: [{}]",
                    authentication.getName(),
                    userRoles,
                    String.join(", ", requiredRoles));

            throw new CustomException(
                    HttpStatus.FORBIDDEN,
                    "Access denied. Required roles: " + String.join(", ", requiredRoles)
            );
        }
    }
}

// ========================================
// EXAMPLE USAGE
// ========================================

/*

// Example 1: Admin only
@GetMapping("/admin/users")
@RequireRole({"admin"})
public ResponseEntity<?> getAllUsers() {
    return ResponseEntity.ok("List of all users");
}

// Example 2: Multiple roles
@PostMapping("/jobs")
@RequireRole({"employer", "admin"})
public ResponseEntity<?> createJob() {
    return ResponseEntity.ok("Job created");
}

// Example 3: Candidate only
@PostMapping("/applications")
@RequireRole({"candidate"})
public ResponseEntity<?> applyJob() {
    return ResponseEntity.ok("Application submitted");
}

// Example 4: All authenticated users
@GetMapping("/profile")
@RequireRole({"admin", "candidate", "employer"})
public ResponseEntity<?> getProfile() {
    return ResponseEntity.ok("User profile");
}

*/

// ========================================
// 4. POM.XML DEPENDENCY (cần thêm nếu chưa có)
// ========================================

/*

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>

*/

// ========================================
// 5. APPLICATION.YML CONFIGURATION (optional)
// ========================================

/*

# Enable AOP
spring:
  aop:
    auto: true
    proxy-target-class: true

# Logging for debugging
logging:
  level:
    com.popcorn.jrp.security.RoleAuthorizationAspect: DEBUG

*/
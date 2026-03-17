# Security Module

A Spring Boot security auto-configuration module that provides header-based pre-authentication with role-based access
control (RBAC).

## Overview

This module implements a stateless, header-driven authentication mechanism suitable for microservices architectures. It
integrates seamlessly with Spring Security and Spring Boot's auto-configuration system, allowing applications to
authenticate requests based on custom HTTP headers.

## Features

- **Header-Based Authentication**: Extracts user identity and roles from request headers
- **Pre-Authentication Filter**: Integrates with Spring Security's pre-authentication framework
- **Stateless Operation**: Uses `STATELESS` session creation policy for microservices compatibility
- **Configurable Public Paths**: Define URL patterns that bypass authentication requirements
- **Method-Level Security**: Supports `@PreAuthorize` and `@Secured` annotations via `@EnableMethodSecurity`
- **Auto-Configuration**: Automatically configured via Spring Boot's autoconfiguration mechanism

## Installation

Add this module as a dependency to your Spring Boot project:

```xml

<dependency>
    <groupId>com.ic.springboot</groupId>
    <artifactId>security</artifactId>
    <version>1.1.3</version>
</dependency>
```

### Parent POM

This module inherits from the parent POM:

```xml

<parent>
    <groupId>com.ic.springboot</groupId>
    <artifactId>parent</artifactId>
    <version>0.0.1</version>
</parent>
```

The parent POM manages dependency versions and Spring Boot configurations, ensuring compatibility across all modules in
the project.

## Configuration

Configure the security module using application properties in your `application.yml`:

```yaml
ic:
  security:
    public-paths:
      - /actuator/health
      - /actuator/health/live
      - /actuator/health/readiness
      - /api/public/**
      - /api/auth/**
      - /swagger-ui/**
      - /v3/api-docs/**
```

Or in `application.properties`:

```properties
ic.security.public-paths[0]=/actuator/health
ic.security.public-paths[1]=/actuator/health/live
ic.security.public-paths[2]=/actuator/health/readiness
ic.security.public-paths[3]=/api/public/**
ic.security.public-paths[4]=/api/auth/**
ic.security.public-paths[5]=/swagger-ui/**
ic.security.public-paths[6]=/v3/api-docs/**
```

### Configuration Properties

| Property                   | Type           | Description                             | Default    |
|----------------------------|----------------|-----------------------------------------|------------|
| `ic.security.public-paths` | `List<String>` | URL patterns exempt from authentication | Empty list |

## Authentication Headers

The `HeaderAuthenticationFilter` expects the following request headers:

| Header            | Description                                          | Required |
|-------------------|------------------------------------------------------|----------|
| `X-User-Id`       | Unique user identifier                               | Yes      |
| `X-User-Username` | Username                                             | Yes      |
| `X-User-Roles`    | Comma-separated roles (e.g., `ROLE_USER,ROLE_ADMIN`) | Yes      |

### Example Request

```bash
curl -X GET http://localhost:8080/api/users \
  -H "X-User-Id: 12345" \
  -H "X-User-Username: john.doe" \
  -H "X-User-Roles: ROLE_USER,ROLE_ADMIN"
```

## Usage

### Basic Setup

The security configuration is auto-enabled. Simply add the dependency and configure public paths in your
`application.yml` or `application.properties`.

### Role-Based Access Control

Use Spring Security's authorization annotations in your controllers or services:

```java

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public User getUser(@PathVariable Long id) {
        // Only users with ROLE_USER can access this endpoint
        return userService.findById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        // Only users with ROLE_ADMIN can access this endpoint
        userService.deleteById(id);
    }
}
```

### Accessing User Information

Access the authenticated user information via Spring Security's context:

```java
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
String username = auth.getName();
Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
```

## Architecture

### Components

1. **SecurityConfig**: Central Spring Security configuration
    - Disables CSRF (stateless API)
    - Enables method-level security
    - Configures authorization rules
    - Registers the header authentication filter

2. **HeaderAuthenticationFilter**: Request filter that extracts authentication from headers
    - Implements `OncePerRequestFilter` for single execution per request
    - Validates required headers
    - Creates `PreAuthenticatedAuthenticationToken` from header values

3. **SecurityProperties**: Configuration properties class
    - Binds to `ic.security.*` configuration namespace
    - Provides list of public (unauthenticated) paths

## Build

Build the module using Maven:

```bash
mvn clean package
```

## Dependencies

- **Spring Boot 3.x**: Web and Security starters
- **Spring Security**: Authentication and authorization framework
- **Java 17+**: Minimum supported JDK version

## Security Considerations

⚠️ **Important**: Header-based authentication assumes:

- **Trusted Network**: Headers are only added by trusted intermediaries (API Gateway, Load Balancer)
- **HTTPS Enforcement**: Use HTTPS in production to prevent header spoofing
- **Gateway Validation**: The upstream API Gateway/Proxy should validate user identity before adding headers
- **No Direct Access**: Applications should not be directly exposed to untrusted clients

## Example Application Configuration

Complete example `application.yml` with security module configuration:

```yaml
spring:
  application:
    name: attendance-service
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
  datasource:
    url: jdbc:mysql://localhost:3306/attendance
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jackson:
    serialization:
      write-dates-as-timestamps: false

ic:
  security:
    public-paths:
      - /actuator/health
      - /actuator/health/live
      - /actuator/health/readiness
      - /api/auth/login
      - /api/auth/register
      - /swagger-ui/**
      - /v3/api-docs/**

server:
  port: 8080
  servlet:
    context-path: /api

logging:
  level:
    root: INFO
    com.ic.security: DEBUG
  pattern:
    console: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
```

Minimal configuration example:

```yaml
spring:
  application:
    name: attendance-service

ic:
  security:
    public-paths:
      - /actuator/health
      - /api/auth/login

server:
  port: 8080
```

## Troubleshooting

### Requests returning 401 Unauthorized

Ensure all required headers are present and correctly formatted:

```bash
curl -i -X GET http://localhost:8080/api/users \
  -H "X-User-Id: 12345" \
  -H "X-User-Username: john.doe" \
  -H "X-User-Roles: USER"
```

### 403 Forbidden on Allowed Endpoints

Verify that user roles match the `@PreAuthorize` requirements. The `ROLE_` prefix is automatically added by the filter,
so use role names without the prefix in headers (e.g., `USER` becomes `ROLE_USER` internally).

### Public Paths Not Working

Confirm the path patterns in `ic.security.public-paths` match your URL structure. Use wildcards (`**`) appropriately.

## License

This module is part of the attendance-service project.

## Support

For issues or questions, contact the development team.


# Auth Server

## Overview

The Auth Server is responsible for validating subscriber details during registration. Upon successful validation, it generates a JWT token. If the token already exists and is not expired, the same token is returned; otherwise, a new token is generated.

## Architecture

- **Spring Boot**: The core framework used to build the Auth Server.
- **Spring Security**: For security and authentication.
- **JWT**: JSON Web Tokens for authentication.
- **Eureka Client**: Registers with the Eureka Server to discover other services.

## Features

- **Validation**: Validates subscriber details during registration.
- **JWT Token Generation**: Generates JWT tokens upon successful validation.
- **Token Management**: Checks token expiration and regenerates if necessary.


## Configuration

Configuration for the Auth Server is done through `application.yml` or `application.properties`. Below is an example configuration in `application.yml`:

```yaml
server:
  port: 8300

spring:
  application:
    name: auth-server

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
    fetchRegistry: true
    registerWithEureka: true

jwt:
  secret: your-secret-key
  expiration: 3600

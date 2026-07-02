package com.hacnation.gateway.unit;

import com.hacnation.gateway.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.web.server.ServerHttpSecurity;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    @Test
    void securityConfig_shouldExist() {
        SecurityConfig config = new SecurityConfig();
        assertNotNull(config);
    }

    @Test
    void securityWebFilterChain_shouldNotBeNull() {
        SecurityConfig config = new SecurityConfig();
        ServerHttpSecurity http = ServerHttpSecurity.http();
        assertNotNull(config.securityWebFilterChain(http));
    }
}

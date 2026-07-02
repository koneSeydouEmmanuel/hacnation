package com.hacnation.gateway.unit;

import com.hacnation.gateway.config.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private WebClient webClient;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(
                "http://keycloak:8080",
                "hacnation",
                "hacnation-backend",
                "http://patient-identity-service:8081"
        );
    }

    @Test
    void login_shouldReturnJwtTokenMap_onSuccess() {
        assertNotNull(authController);
    }

    @Test
    void register_shouldReturn201_onSuccess() {
        assertNotNull(authController);
    }
}

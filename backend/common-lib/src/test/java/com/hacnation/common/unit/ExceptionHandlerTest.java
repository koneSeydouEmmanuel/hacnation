package com.hacnation.common.unit;

import com.hacnation.common.exception.BusinessException;
import com.hacnation.common.exception.ErrorResponse;
import com.hacnation.common.exception.GlobalExceptionHandler;
import com.hacnation.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleResourceNotFoundException_shouldReturn404() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Patient", "123");

        ResponseEntity<ErrorResponse> response = handler.handleNotFound(ex);

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains("Patient"));
    }

    @Test
    void handleBusinessException_shouldReturnSpecifiedStatus() {
        BusinessException ex = new BusinessException("Erreur metier test", 409);

        ResponseEntity<ErrorResponse> response = handler.handleBusiness(ex);

        assertEquals(409, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().getStatus());
        assertEquals("Erreur metier test", response.getBody().getMessage());
    }

    @Test
    void handleGenericException_shouldReturn500() {
        Exception ex = new Exception("Internal error");

        ResponseEntity<ErrorResponse> response = handler.handleGeneral(ex);

        assertEquals(500, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
    }
}

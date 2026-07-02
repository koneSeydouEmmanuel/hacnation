package com.hacnation.common.unit;

import com.hacnation.common.dto.ConsultationDto;
import com.hacnation.common.dto.PatientDto;
import com.hacnation.common.dto.RdvDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void patientDto_shouldValidateNonNullableFields() {
        PatientDto dto = new PatientDto();
        Set<ConstraintViolation<PatientDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void rdvDto_shouldValidateRequiredFields() {
        RdvDto dto = new RdvDto();
        Set<ConstraintViolation<RdvDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void consultationDto_shouldValidateRequiredFields() {
        ConsultationDto dto = new ConsultationDto();
        Set<ConstraintViolation<ConsultationDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }
}

package com.hacnation.patientidentity.infrastructure.adapter.in.rest;

import com.hacnation.common.dto.PatientDto;
import com.hacnation.patientidentity.application.service.CreatePatientUseCase;
import com.hacnation.patientidentity.application.service.SearchPatientUseCase;
import com.hacnation.patientidentity.application.service.UpdatePatientUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final SearchPatientUseCase searchPatientUseCase;
    private final CreatePatientUseCase createPatientUseCase;
    private final UpdatePatientUseCase updatePatientUseCase;

    public PatientController(SearchPatientUseCase searchPatientUseCase,
                             CreatePatientUseCase createPatientUseCase,
                             UpdatePatientUseCase updatePatientUseCase) {
        this.searchPatientUseCase = searchPatientUseCase;
        this.createPatientUseCase = createPatientUseCase;
        this.updatePatientUseCase = updatePatientUseCase;
    }

    @PostMapping
    public ResponseEntity<PatientDto> createPatient(@RequestBody PatientDto dto) {
        PatientDto created = createPatientUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<Page<PatientDto>> searchPatients(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nom").ascending());
        Page<PatientDto> result = searchPatientUseCase.searchPatients(q, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatient(@PathVariable String id) {
        PatientDto patient = searchPatientUseCase.getPatient(id);
        return ResponseEntity.ok(patient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDto> updatePatient(@PathVariable String id,
                                                     @RequestBody PatientDto dto) {
        PatientDto updated = updatePatientUseCase.updatePatient(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PatientDto> deletePatient(@PathVariable String id) {
        PatientDto deleted = updatePatientUseCase.deletePatient(id);
        return ResponseEntity.ok(deleted);
    }
}

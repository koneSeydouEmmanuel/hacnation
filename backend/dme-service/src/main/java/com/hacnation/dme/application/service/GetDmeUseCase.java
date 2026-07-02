package com.hacnation.dme.application.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.dme.domain.model.DossierMedical;
import com.hacnation.dme.domain.port.DmeRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetDmeUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetDmeUseCase.class);

    private final DmeRepositoryPort dmeRepository;
    private final ObjectMapper objectMapper;

    public GetDmeUseCase(DmeRepositoryPort dmeRepository) {
        this.dmeRepository = dmeRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public Map<String, Object> execute(String patientId) {
        DossierMedical dme = findByPatientId(patientId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", dme.getId());
        result.put("patientId", dme.getPatientId());
        result.put("antecedents", parseJsonArray(dme.getAntecedents()));
        result.put("notesCliniques", parseJsonArray(dme.getNotesCliniques()));
        result.put("documents", parseJsonArray(dme.getDocuments()));
        result.put("createdAt", dme.getCreatedAt());
        result.put("updatedAt", dme.getUpdatedAt());
        return result;
    }

    @Transactional
    public DossierMedical createDme(String patientId) {
        DossierMedical dme = new DossierMedical();
        dme.setPatientId(patientId);
        dme.setAntecedents("[]");
        dme.setNotesCliniques("[]");
        dme.setDocuments("[]");
        DossierMedical saved = dmeRepository.save(dme);
        log.info("DME cree pour le patient: {}", patientId);
        return saved;
    }

    public DossierMedical findByPatientId(String patientId) {
        return dmeRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("DossierMedical", patientId));
    }

    private List<Map<String, Object>> parseJsonArray(String json) {
        try {
            if (json == null || json.isBlank() || json.equals("[]")) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(json,
                    new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            log.warn("Erreur lors du parsing JSON: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            log.error("Erreur lors de la serialisation JSON: {}", e.getMessage());
            return "[]";
        }
    }

    @Transactional
    public Map<String, Object> addAntecedent(String patientId, Map<String, Object> antecedent) {
        DossierMedical dme = findByPatientId(patientId);
        List<Map<String, Object>> antecedents = parseJsonArray(dme.getAntecedents());
        antecedents.add(antecedent);
        dme.setAntecedents(toJson(antecedents));
        dmeRepository.save(dme);
        log.info("Antecedent ajoute au DME du patient: {}", patientId);
        return execute(patientId);
    }

    @Transactional
    public Map<String, Object> addNoteClinique(String patientId, Map<String, Object> note) {
        DossierMedical dme = findByPatientId(patientId);
        List<Map<String, Object>> notes = parseJsonArray(dme.getNotesCliniques());
        notes.add(note);
        dme.setNotesCliniques(toJson(notes));
        dmeRepository.save(dme);
        log.info("Note clinique ajoutee au DME du patient: {}", patientId);
        return execute(patientId);
    }

    @Transactional
    public Map<String, Object> addDocument(String patientId, Map<String, Object> document) {
        DossierMedical dme = findByPatientId(patientId);
        List<Map<String, Object>> documents = parseJsonArray(dme.getDocuments());
        documents.add(document);
        dme.setDocuments(toJson(documents));
        dmeRepository.save(dme);
        log.info("Document ajoute au DME du patient: {}", patientId);
        return execute(patientId);
    }
}

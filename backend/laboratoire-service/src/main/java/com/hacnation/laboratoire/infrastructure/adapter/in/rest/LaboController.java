package com.hacnation.laboratoire.infrastructure.adapter.in.rest;

import com.hacnation.laboratoire.application.service.ProcessAnalysisUseCase;
import com.hacnation.laboratoire.application.service.ValidateResultUseCase;
import com.hacnation.laboratoire.domain.model.DemandeAnalyse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/labo")
public class LaboController {

    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList("jpg", "jpeg", "png", "dicom", "dcm", "pdf"));
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    private final ProcessAnalysisUseCase processAnalysisUseCase;
    private final ValidateResultUseCase validateResultUseCase;
    private final ObjectMapper objectMapper;

    public LaboController(ProcessAnalysisUseCase processAnalysisUseCase,
                          ValidateResultUseCase validateResultUseCase,
                          ObjectMapper objectMapper) {
        this.processAnalysisUseCase = processAnalysisUseCase;
        this.validateResultUseCase = validateResultUseCase;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/demandes")
    public ResponseEntity<?> getDemandes(
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) String patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        if (patientId != null) {
            List<DemandeAnalyse> demandes = processAnalysisUseCase.getDemandesByPatient(patientId);
            return ResponseEntity.ok(Map.of("content", demandes, "totalElements", demandes.size()));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<DemandeAnalyse> demandesPage = processAnalysisUseCase.getDemandesEnAttente(pageable);
        return ResponseEntity.ok(demandesPage);
    }

    @GetMapping("/demandes/{id}")
    public ResponseEntity<DemandeAnalyse> getDemandeById(@PathVariable String id, HttpServletRequest request) {
        DemandeAnalyse demande = processAnalysisUseCase.getDemandeById(id);

        String userId = request.getHeader("X-User-Id");
        String roles = request.getHeader("X-User-Roles");

        if (userId != null && (roles != null && roles.contains("ADMIN"))) {
            return ResponseEntity.ok(demande);
        }

        if (userId != null && (userId.equals(demande.getMedecinPrescripteurId()) || userId.equals(demande.getPatientId()))) {
            return ResponseEntity.ok(demande);
        }

        if (demande.getStatut() == com.hacnation.common.enums.StatutPrescription.EN_ATTENTE) {
            return ResponseEntity.ok(demande);
        }

        return ResponseEntity.status(403).build();
    }

    @PostMapping("/demandes/{id}/fichiers")
    public ResponseEntity<Map<String, Object>> uploadFichier(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file) {

        if (file.getSize() > MAX_FILE_SIZE) {
            Map<String, Object> error = new java.util.LinkedHashMap<>();
            error.put("error", "Fichier trop volumineux. Taille maximale : 10 Mo");
            return ResponseEntity.status(413).body(error);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ?
            originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase() : "";

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            Map<String, Object> error = new java.util.LinkedHashMap<>();
            error.put("error", "Format de fichier non accepte. Formats autorises : JPG, PNG, DICOM, PDF");
            return ResponseEntity.badRequest().body(error);
        }

        try {
            DemandeAnalyse demande = processAnalysisUseCase.getDemandeById(id);

            Path uploadDir = Paths.get("uploads/labo/" + demande.getId());
            Files.createDirectories(uploadDir);

            String filename = System.currentTimeMillis() + "_" + originalFilename;
            Path filePath = uploadDir.resolve(filename);
            Files.write(filePath, file.getBytes());

            String currentFichiers = demande.getFichiers();
            java.util.List<String> fichiersList;
            if (currentFichiers != null && !currentFichiers.isEmpty()) {
                fichiersList = new java.util.ArrayList<>(Arrays.asList(objectMapper.readValue(currentFichiers, String[].class)));
            } else {
                fichiersList = new java.util.ArrayList<>();
            }
            fichiersList.add(filePath.toString());
            demande.setFichiers(objectMapper.writeValueAsString(fichiersList));
            processAnalysisUseCase.saisirResultats(id, demande.getResultats() != null ? demande.getResultats() : "", demande.getLaborantinId());

            Map<String, Object> response = new java.util.LinkedHashMap<>();
            response.put("message", "Fichier uploade avec succes");
            response.put("filename", filename);
            response.put("path", filePath.toString());
            return ResponseEntity.status(201).body(response);
        } catch (IOException e) {
            Map<String, Object> error = new java.util.LinkedHashMap<>();
            error.put("error", "Erreur lors de l'upload: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PostMapping("/demandes/{id}/resultats")
    public ResponseEntity<DemandeAnalyse> saisirResultats(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {

        String resultats = body.get("resultats");
        String laborantinId = body.get("laborantinId");

        if (resultats == null || laborantinId == null) {
            return ResponseEntity.badRequest().build();
        }

        DemandeAnalyse updated = processAnalysisUseCase.saisirResultats(id, resultats, laborantinId);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/demandes/{id}/valider")
    public ResponseEntity<DemandeAnalyse> validerResultats(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {

        String validateurId = body.get("validateurId");

        if (validateurId == null) {
            return ResponseEntity.badRequest().build();
        }

        DemandeAnalyse updated = validateResultUseCase.validerResultats(id, validateurId);
        return ResponseEntity.ok(updated);
    }
}

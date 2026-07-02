package com.hacnation.dme.application.service;

import com.hacnation.dme.domain.model.DossierMedical;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class UpdateDmeUseCase {

    private static final Logger log = LoggerFactory.getLogger(UpdateDmeUseCase.class);

    private final GetDmeUseCase getDmeUseCase;

    public UpdateDmeUseCase(GetDmeUseCase getDmeUseCase) {
        this.getDmeUseCase = getDmeUseCase;
    }

    public void addNoteCliniqueFromConsultation(String patientId, String consultationId, String medecinId, String statut) {
        Map<String, Object> note = new LinkedHashMap<>();
        note.put("type", "CONSULTATION");
        note.put("consultationId", consultationId);
        note.put("date", LocalDateTime.now().toString());
        note.put("statut", statut);
        note.put("medecinId", medecinId);
        getDmeUseCase.addNoteClinique(patientId, note);
        log.info("Note de consultation terminee ajoutee au DME du patient: {}", patientId);
    }

    public void addDocumentFromLab(String patientId, String analyseId, String prescriptionId, String statut) {
        Map<String, Object> document = new LinkedHashMap<>();
        document.put("type", "LAB_RESULT");
        document.put("analyseId", analyseId);
        document.put("prescriptionId", prescriptionId);
        document.put("date", LocalDateTime.now().toString());
        document.put("statut", statut);
        getDmeUseCase.addDocument(patientId, document);
        log.info("Resultat de laboratoire ajoute au DME du patient: {}", patientId);
    }

    public void addNoteFromPharmacy(String patientId, String ordonnanceId, String prescriptionId, String medicamentId, Integer quantite, String statut) {
        Map<String, Object> note = new LinkedHashMap<>();
        note.put("type", "DRUG_DISPENSED");
        note.put("ordonnanceId", ordonnanceId);
        note.put("prescriptionId", prescriptionId);
        note.put("medicamentId", medicamentId);
        note.put("quantite", quantite);
        note.put("date", LocalDateTime.now().toString());
        note.put("statut", statut);
        getDmeUseCase.addNoteClinique(patientId, note);
        log.info("Delivrance de medicament ajoutee au DME du patient: {}", patientId);
    }

    public void createDmeForPatient(String patientId) {
        getDmeUseCase.createDme(patientId);
        log.info("DME cree automatiquement pour le patient: {}", patientId);
    }
}

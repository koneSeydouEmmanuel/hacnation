package com.hacnation.reportingadmin.application.service;

import com.hacnation.reportingadmin.domain.port.inbound.CrmUseCase;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CrmServiceImpl implements CrmUseCase {

    private static final Logger log = LoggerFactory.getLogger(CrmServiceImpl.class);

    @Override
    public List<Map<String, Object>> getInteractionHistorique(String patientId) {
        log.info("Recuperation historique interactions patient: {}", patientId);
        return List.of(
                createInteraction("RDV_CREE", "Rendez-vous cree", "Cardiologie", "2025-06-15T09:00:00", "CONFIRME"),
                createInteraction("RDV_RAPPELE", "Rappel RDV envoye (SMS)", "Cardiologie", "2025-06-19T08:00:00", "INFO"),
                createInteraction("CHECK_IN", "Arrivee a l'accueil", "Cardiologie", "2025-06-20T08:50:00", "PRESENT"),
                createInteraction("CONSULTATION", "Consultation avec Dr. Diallo", "Cardiologie", "2025-06-20T09:15:00", "TERMINE"),
                createInteraction("PRESCRIPTION", "Prescription: Aspirine 100mg + ECG", "Pharmacie/Labo", "2025-06-20T09:45:00", "ACTIF"),
                createInteraction("NOTIFICATION", "Resultats analyses disponibles", "Laboratoire", "2025-06-21T14:30:00", "LIVRE"),
                createInteraction("FACTURE_CREE", "Facture #FCT-2025-0842", "Caisse", "2025-06-20T10:00:00", "PAYEE"),
                createInteraction("PAIEMENT", "Paiement recu: 25 000 FCFA", "Caisse", "2025-06-20T10:05:00", "VALIDE"),
                createInteraction("RDV_CREE", "Rendez-vous de suivi cree", "Cardiologie", "2025-09-20T09:00:00", "PLANIFIE"),
                createInteraction("NOTIFICATION", "Rappel RDV suivi cardiologie", "Cardiologie", "2025-09-18T08:00:00", "ENVOYE")
        );
    }

    @Override
    public Map<String, Object> getFidelitePatient(String patientId) {
        log.info("Recuperation fidelite patient: {}", patientId);

        Map<String, Object> fidelite = new LinkedHashMap<>();
        fidelite.put("patientId", patientId);
        fidelite.put("nombreVisites", 12);
        fidelite.put("ancienneteMois", 18);
        fidelite.put("derniereVisite", "2025-06-20T09:15:00");
        fidelite.put("pointsFidelite", 850);
        fidelite.put("niveauFidelite", "OR");

        Map<String, Object> avantages = new LinkedHashMap<>();
        avantages.put("reductionConsultation", "10%");
        avantages.put("prioriteFileAttente", true);
        avantages.put("accesRapportsSMS", true);
        avantages.put("prochainNiveau", "PLATINE (a 1000 points)");
        fidelite.put("avantages", avantages);

        return fidelite;
    }

    private Map<String, Object> createInteraction(String type, String description, String lieu, String dateHeure, String statut) {
        Map<String, Object> interaction = new LinkedHashMap<>();
        interaction.put("type", type);
        interaction.put("description", description);
        interaction.put("lieu", lieu);
        interaction.put("dateHeure", dateHeure);
        interaction.put("statut", statut);
        return interaction;
    }
}

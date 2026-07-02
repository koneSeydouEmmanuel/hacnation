package com.hacnation.reportingadmin.application.service;

import com.hacnation.reportingadmin.domain.port.inbound.ReportingUseCase;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ReportingServiceImpl implements ReportingUseCase {

    private static final Logger log = LoggerFactory.getLogger(ReportingServiceImpl.class);

    @Override
    public Map<String, Object> getDashboardFiles() {
        log.info("Generation rapport dashboard files d'attente");

        Map<String, Object> data = new LinkedHashMap<>();

        Map<String, Integer> patientsParService = new LinkedHashMap<>();
        patientsParService.put("Medecine Generale", 12);
        patientsParService.put("Pediatrie", 8);
        patientsParService.put("Cardiologie", 5);
        patientsParService.put("Dermatologie", 3);
        patientsParService.put("ORL", 6);
        data.put("patientsEnAttente", patientsParService);

        data.put("tempsAttenteMoyen", 28);
        data.put("tempsAttenteMax", 95);

        return data;
    }

    @Override
    public Map<String, Object> getDashboardConsultations(LocalDate dateDebut, LocalDate dateFin) {
        log.info("Generation rapport consultations du {} au {}", dateDebut, dateFin);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("nombreConsultations", 487);

        Map<String, Integer> parMedecin = new LinkedHashMap<>();
        parMedecin.put("Dr. Kone", 85);
        parMedecin.put("Dr. Traore", 72);
        parMedecin.put("Dr. Diallo", 68);
        parMedecin.put("Dr. Coulibaly", 91);
        parMedecin.put("Dr. Bamba", 63);
        parMedecin.put("Dr. Cisse", 108);
        data.put("parMedecin", parMedecin);

        Map<String, Integer> parService = new LinkedHashMap<>();
        parService.put("Medecine Generale", 156);
        parService.put("Pediatrie", 98);
        parService.put("Cardiologie", 64);
        parService.put("Dermatologie", 45);
        parService.put("ORL", 72);
        parService.put("Gynecologie", 52);
        data.put("parService", parService);

        return data;
    }

    @Override
    public Map<String, Object> getDashboardRecettes(LocalDate dateDebut, LocalDate dateFin) {
        log.info("Generation rapport recettes du {} au {}", dateDebut, dateFin);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("recettesTotales", 18500000);

        Map<String, BigDecimal> parModePaiement = new LinkedHashMap<>();
        parModePaiement.put("ESPECES", new BigDecimal("7200000"));
        parModePaiement.put("ORANGE_MONEY", new BigDecimal("4500000"));
        parModePaiement.put("MTN_MONEY", new BigDecimal("3800000"));
        parModePaiement.put("WAVE", new BigDecimal("2200000"));
        parModePaiement.put("CARTE_BANCAIRE", new BigDecimal("800000"));
        data.put("parModePaiement", parModePaiement);

        data.put("facturesEnAttente", 142);
        data.put("tauxRecouvrement", 78.5);

        return data;
    }

    @Override
    public Map<String, Object> getDashboardStocks() {
        log.info("Generation rapport stocks pharmacie");

        Map<String, Object> data = new LinkedHashMap<>();

        data.put("rupturesStock", 13);
        data.put("prochesPeremption", 47);
        data.put("valeurStock", 125000000);

        return data;
    }

    @Override
    public Map<String, Object> getDashboardUrgences(LocalDate date) {
        log.info("Generation rapport urgences pour le {}", date);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("admissionsUrgences", 34);

        Map<String, Integer> parNiveauTriage = new LinkedHashMap<>();
        parNiveauTriage.put("NIVEAU_1_CRITIQUE", 5);
        parNiveauTriage.put("NIVEAU_2_TRES_URGENT", 8);
        parNiveauTriage.put("NIVEAU_3_URGENT", 12);
        parNiveauTriage.put("NIVEAU_4_STANDARD", 7);
        parNiveauTriage.put("NIVEAU_5_NON_URGENT", 2);
        data.put("parNiveauTriage", parNiveauTriage);

        data.put("tempsPriseEnChargeMoyen", 18);

        return data;
    }
}

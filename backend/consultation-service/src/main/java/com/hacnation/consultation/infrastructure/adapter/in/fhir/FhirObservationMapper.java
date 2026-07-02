package com.hacnation.consultation.infrastructure.adapter.in.fhir;

import com.hacnation.consultation.domain.model.Consultation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class FhirObservationMapper {

    private static final Logger log = LoggerFactory.getLogger(FhirObservationMapper.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String LOINC_SYSTEM = "http://loinc.org";

    public Bundle toFhirBundle(List<Consultation> consultations, String searchUrl) {
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.SEARCHSET);

        Bundle.BundleLinkComponent selfLink = bundle.addLink();
        selfLink.setRelation("self");
        selfLink.setUrl(searchUrl);

        int totalObservations = 0;
        for (Consultation consultation : consultations) {
            List<Observation> observations = toFhirObservations(consultation);
            totalObservations += observations.size();
            for (Observation obs : observations) {
                bundle.addEntry()
                        .setResource(obs)
                        .setFullUrl("Observation/" + obs.getId());
            }
        }
        bundle.setTotal(totalObservations);

        return bundle;
    }

    public List<Observation> toFhirObservations(Consultation consultation) {
        List<Observation> observations = new ArrayList<>();
        String patientId = consultation.getPatientId();
        String constJson = consultation.getConstantes();

        if (constJson == null || constJson.isBlank()) {
            return observations;
        }

        Map<String, String> constantesMap;
        try {
            constantesMap = objectMapper.readValue(constJson, new TypeReference<Map<String, String>>() {});
        } catch (Exception e) {
            log.warn("Failed to parse constantes JSON for consultation {}: {}", consultation.getId(), e.getMessage());
            return observations;
        }

        Date effectiveDate = null;
        if (consultation.getDate() != null) {
            effectiveDate = Date.from(consultation.getDate().atZone(ZoneId.systemDefault()).toInstant());
        } else if (consultation.getCreatedAt() != null) {
            effectiveDate = Date.from(consultation.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant());
        }

        for (Map.Entry<String, String> entry : constantesMap.entrySet()) {
            String key = entry.getKey().trim().toLowerCase();
            String value = entry.getValue().trim();

            if (key.equals("ta")) {
                String[] parts = value.split("/");
                if (parts.length == 2) {
                    try {
                        observations.add(buildVitalObservation(
                                patientId, consultation.getId(), effectiveDate,
                                "8480-6", "Systolic blood pressure", parts[0].trim(),
                                "mm[Hg]", "systolic-" + consultation.getId()));
                        observations.add(buildVitalObservation(
                                patientId, consultation.getId(), effectiveDate,
                                "8462-4", "Diastolic blood pressure", parts[1].trim(),
                                "mm[Hg]", "diastolic-" + consultation.getId()));
                    } catch (NumberFormatException e) {
                        log.warn("Invalid numeric value in TA: {}", value);
                    }
                }
            } else if (key.equals("pouls")) {
                observations.add(buildVitalObservation(
                        patientId, consultation.getId(), effectiveDate,
                        "8867-4", "Heart rate", value,
                        "/min", "heartrate-" + consultation.getId()));
            } else if (key.equals("temperature")) {
                observations.add(buildVitalObservation(
                        patientId, consultation.getId(), effectiveDate,
                        "8310-5", "Body temperature", value,
                        "Cel", "temp-" + consultation.getId()));
            } else if (key.equals("poids")) {
                observations.add(buildVitalObservation(
                        patientId, consultation.getId(), effectiveDate,
                        "29463-7", "Body weight", value,
                        "kg", "weight-" + consultation.getId()));
            } else if (key.equals("taille")) {
                observations.add(buildVitalObservation(
                        patientId, consultation.getId(), effectiveDate,
                        "8302-2", "Body height", value,
                        "cm", "height-" + consultation.getId()));
            } else if (key.equals("spo2")) {
                observations.add(buildVitalObservation(
                        patientId, consultation.getId(), effectiveDate,
                        "2708-6", "Oxygen saturation in Arterial blood", value,
                        "%", "spo2-" + consultation.getId()));
            }
        }

        return observations;
    }

    private Observation buildVitalObservation(String patientId, String consultationId, Date effectiveDate,
                                               String loincCode, String display, String value,
                                               String unit, String obsIdSuffix) {
        Observation observation = new Observation();
        observation.setId(obsIdSuffix);
        observation.setStatus(Observation.ObservationStatus.FINAL);

        CodeableConcept code = new CodeableConcept();
        code.addCoding()
                .setSystem(LOINC_SYSTEM)
                .setCode(loincCode)
                .setDisplay(display);
        observation.setCode(code);

        observation.setSubject(new Reference("Patient/" + patientId));

        if (effectiveDate != null) {
            observation.setEffective(new DateTimeType(effectiveDate));
        }

        Quantity quantity = new Quantity();
        quantity.setValue(new java.math.BigDecimal(value));
        quantity.setUnit(unit);
        quantity.setSystem("http://unitsofmeasure.org");
        quantity.setCode(unit);
        observation.setValue(quantity);

        CodeableConcept category = new CodeableConcept();
        category.addCoding()
                .setSystem("http://terminology.hl7.org/CodeSystem/observation-category")
                .setCode("vital-signs")
                .setDisplay("Vital Signs");
        observation.setCategory(List.of(category));

        return observation;
    }
}

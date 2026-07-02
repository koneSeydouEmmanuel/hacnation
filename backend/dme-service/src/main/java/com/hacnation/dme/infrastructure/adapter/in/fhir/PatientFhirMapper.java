package com.hacnation.dme.infrastructure.adapter.in.fhir;

import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class PatientFhirMapper {

    public Patient toFhirPatient(com.hacnation.dme.domain.model.Patient entity) {
        Patient fhirPatient = new Patient();
        fhirPatient.setId(entity.getId());

        fhirPatient.addName()
                .setFamily(entity.getNom())
                .addGiven(entity.getPrenom());

        if (entity.getDateNaissance() != null) {
            fhirPatient.setBirthDate(Date.from(
                    entity.getDateNaissance().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
        }

        if (entity.getSexe() != null) {
            switch (entity.getSexe().toUpperCase()) {
                case "M": fhirPatient.setGender(Enumerations.AdministrativeGender.MALE); break;
                case "F": fhirPatient.setGender(Enumerations.AdministrativeGender.FEMALE); break;
                default: fhirPatient.setGender(Enumerations.AdministrativeGender.UNKNOWN); break;
            }
        }

        if (entity.getTelephone() != null) {
            ContactPoint contact = fhirPatient.addTelecom();
            contact.setSystem(ContactPoint.ContactPointSystem.PHONE);
            contact.setValue(entity.getTelephone());
            contact.setUse(ContactPoint.ContactPointUse.MOBILE);
        }

        if (entity.getEmail() != null) {
            ContactPoint contact = fhirPatient.addTelecom();
            contact.setSystem(ContactPoint.ContactPointSystem.EMAIL);
            contact.setValue(entity.getEmail());
        }

        if (entity.getAdresse() != null) {
            Address address = fhirPatient.addAddress();
            address.setText(entity.getAdresse());
            address.setUse(Address.AddressUse.HOME);
        }

        Identifier identifier = fhirPatient.addIdentifier();
        identifier.setSystem("urn:oid:hacnation:patient:id");
        identifier.setValue(entity.getId());

        return fhirPatient;
    }

    public Bundle toFhirBundle(List<com.hacnation.dme.domain.model.Patient> entities, String searchUrl) {
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.SEARCHSET);
        bundle.setTotal(entities.size());

        Bundle.BundleLinkComponent selfLink = bundle.addLink();
        selfLink.setRelation("self");
        selfLink.setUrl(searchUrl);

        for (com.hacnation.dme.domain.model.Patient entity : entities) {
            bundle.addEntry()
                    .setResource(toFhirPatient(entity))
                    .setFullUrl("Patient/" + entity.getId());
        }

        return bundle;
    }
}

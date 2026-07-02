package com.hacnation.rdv.infrastructure.adapter.in.fhir;

import com.hacnation.common.enums.StatutRdv;
import com.hacnation.rdv.domain.model.RendezVous;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
public class FhirAppointmentMapper {

    public Appointment toFhirAppointment(RendezVous entity) {
        Appointment appointment = new Appointment();
        appointment.setId(entity.getId());

        appointment.setStatus(mapStatut(entity.getStatut()));

        if (entity.getMotif() != null) {
            appointment.setDescription(entity.getMotif());
        }

        if (entity.getPatientId() != null) {
            Reference patientRef = new Reference("Patient/" + entity.getPatientId());
            patientRef.setType("Patient");
            appointment.addParticipant()
                    .setActor(patientRef)
                    .setStatus(Appointment.ParticipationStatus.ACCEPTED);
        }

        if (entity.getPraticienId() != null) {
            Reference practitionerRef = new Reference("Practitioner/" + entity.getPraticienId());
            practitionerRef.setType("Practitioner");
            appointment.addParticipant()
                    .setActor(practitionerRef)
                    .setStatus(Appointment.ParticipationStatus.ACCEPTED);
        }

        if (entity.getDateHeure() != null) {
            Date startDate = Date.from(entity.getDateHeure().atZone(ZoneId.systemDefault()).toInstant());
            appointment.setStart(startDate);
            appointment.setEnd(Date.from(entity.getDateHeure().plusMinutes(30)
                    .atZone(ZoneId.systemDefault()).toInstant()));
        }

        if (entity.getService() != null) {
            CodeableConcept serviceType = new CodeableConcept();
            serviceType.setText(entity.getService());
            appointment.setServiceType(List.of(serviceType));
        }

        if (entity.getCreatedAt() != null) {
            appointment.setCreated(Date.from(entity.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
        }

        return appointment;
    }

    public Bundle toFhirBundle(List<RendezVous> entities, String searchUrl) {
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.SEARCHSET);
        bundle.setTotal(entities.size());

        Bundle.BundleLinkComponent selfLink = bundle.addLink();
        selfLink.setRelation("self");
        selfLink.setUrl(searchUrl);

        for (RendezVous entity : entities) {
            bundle.addEntry()
                    .setResource(toFhirAppointment(entity))
                    .setFullUrl("Appointment/" + entity.getId());
        }

        return bundle;
    }

    private Appointment.AppointmentStatus mapStatut(StatutRdv statut) {
        if (statut == null) {
            return Appointment.AppointmentStatus.BOOKED;
        }
        switch (statut) {
            case EN_ATTENTE:
                return Appointment.AppointmentStatus.BOOKED;
            case CONFIRME:
                return Appointment.AppointmentStatus.BOOKED;
            case HONORE:
                return Appointment.AppointmentStatus.FULFILLED;
            case ANNULE:
                return Appointment.AppointmentStatus.CANCELLED;
            case ABSENT:
                return Appointment.AppointmentStatus.NOSHOW;
            default:
                return Appointment.AppointmentStatus.BOOKED;
        }
    }
}

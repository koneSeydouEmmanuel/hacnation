package com.hacnation.rdv.domain.port;

import java.time.LocalDateTime;

public interface RdvEventPublisherPort {

    void publishRdvCreated(String rdvId, String patientId, String service, LocalDateTime dateHeure);

    void publishRdvStatusChanged(String rdvId, String patientId, String statut);
}

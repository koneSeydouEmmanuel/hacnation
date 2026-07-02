package com.hacnation.accueil.domain.port;

import com.hacnation.common.events.AdmissionEvent;

public interface AdmissionEventPublisherPort {

    void publishAdmissionEvent(AdmissionEvent event);
}

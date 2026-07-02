package com.hacnation.prescription.domain.port;

import com.hacnation.common.events.PrescriptionEvent;

public interface PrescriptionEventPublisherPort {

    void publishPrescriptionEvent(PrescriptionEvent event);
}

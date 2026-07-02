package com.hacnation.consultation.domain.port;

import com.hacnation.common.events.ConsultationEvent;

public interface ConsultationEventPublisherPort {

    void publishConsultationEvent(ConsultationEvent event);
}

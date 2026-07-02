package com.hacnation.pharmacie.domain.port;

import com.hacnation.common.events.NotificationEvent;
import com.hacnation.common.events.PharmacyEvent;

public interface PharmacyEventPublisherPort {

    void publishPharmacyEvent(PharmacyEvent event);

    void publishNotificationEvent(NotificationEvent event);
}

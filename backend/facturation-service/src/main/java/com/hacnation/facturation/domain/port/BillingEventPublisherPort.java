package com.hacnation.facturation.domain.port;

import com.hacnation.common.events.BillingEvent;
import com.hacnation.common.events.NotificationEvent;

public interface BillingEventPublisherPort {

    void publishBillingEvent(BillingEvent event);

    void publishNotificationEvent(NotificationEvent event);
}

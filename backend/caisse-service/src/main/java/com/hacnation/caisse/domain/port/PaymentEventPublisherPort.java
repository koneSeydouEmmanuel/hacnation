package com.hacnation.caisse.domain.port;

import com.hacnation.common.events.BillingEvent;
import com.hacnation.common.events.NotificationEvent;

public interface PaymentEventPublisherPort {

    void publishBillingEvent(BillingEvent event);

    void publishNotificationEvent(NotificationEvent event);
}

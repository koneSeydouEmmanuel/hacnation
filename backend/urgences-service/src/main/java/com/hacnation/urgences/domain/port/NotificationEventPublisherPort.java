package com.hacnation.urgences.domain.port;

import com.hacnation.common.events.NotificationEvent;

public interface NotificationEventPublisherPort {

    void publishNotificationEvent(NotificationEvent event);
}

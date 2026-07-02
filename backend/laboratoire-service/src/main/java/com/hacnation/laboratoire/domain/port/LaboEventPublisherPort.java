package com.hacnation.laboratoire.domain.port;

import com.hacnation.common.events.LabEvent;
import com.hacnation.common.events.NotificationEvent;

public interface LaboEventPublisherPort {

    void publishLabEvent(LabEvent event);

    void publishNotificationEvent(NotificationEvent event);
}

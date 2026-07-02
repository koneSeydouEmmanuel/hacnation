package com.hacnation.fileattente.domain.port;

public interface QueueEventPublisherPort {

    void publishCheckIn(String patientId, String rdvId, String service, Integer position);

    void publishCallNext(String patientId, String service);
}

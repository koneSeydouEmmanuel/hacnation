package com.hacnation.reportingadmin.domain.port.inbound;

import java.util.List;
import java.util.Map;

public interface CrmUseCase {

    List<Map<String, Object>> getInteractionHistorique(String patientId);
    Map<String, Object> getFidelitePatient(String patientId);
}

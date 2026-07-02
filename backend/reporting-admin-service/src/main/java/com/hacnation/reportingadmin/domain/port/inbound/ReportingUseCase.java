package com.hacnation.reportingadmin.domain.port.inbound;

import java.time.LocalDate;
import java.util.Map;

public interface ReportingUseCase {

    Map<String, Object> getDashboardFiles();
    Map<String, Object> getDashboardConsultations(LocalDate dateDebut, LocalDate dateFin);
    Map<String, Object> getDashboardRecettes(LocalDate dateDebut, LocalDate dateFin);
    Map<String, Object> getDashboardStocks();
    Map<String, Object> getDashboardUrgences(LocalDate date);
}

package com.hacnation.reportingadmin.infrastructure.adapter.inbound;

import com.hacnation.reportingadmin.domain.port.inbound.ReportingUseCase;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reporting")
public class ReportingController {

    private final ReportingUseCase reportingUseCase;

    public ReportingController(ReportingUseCase reportingUseCase) {
        this.reportingUseCase = reportingUseCase;
    }

    @GetMapping("/dashboard/files")
    public ResponseEntity<Map<String, Object>> dashboardFiles() {
        return ResponseEntity.ok(reportingUseCase.getDashboardFiles());
    }

    @GetMapping("/dashboard/consultations")
    public ResponseEntity<Map<String, Object>> dashboardConsultations(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(reportingUseCase.getDashboardConsultations(dateDebut, dateFin));
    }

    @GetMapping("/dashboard/recettes")
    public ResponseEntity<Map<String, Object>> dashboardRecettes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(reportingUseCase.getDashboardRecettes(dateDebut, dateFin));
    }

    @GetMapping("/dashboard/stocks")
    public ResponseEntity<Map<String, Object>> dashboardStocks() {
        return ResponseEntity.ok(reportingUseCase.getDashboardStocks());
    }

    @GetMapping("/dashboard/urgences")
    public ResponseEntity<Map<String, Object>> dashboardUrgences(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(reportingUseCase.getDashboardUrgences(date));
    }

    @GetMapping("/dashboard/global")
    public ResponseEntity<Map<String, Object>> dashboardGlobal() {
        Map<String, Object> global = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        global.put("files", reportingUseCase.getDashboardFiles());
        global.put("consultations", reportingUseCase.getDashboardConsultations(today.minusMonths(1), today));
        global.put("recettes", reportingUseCase.getDashboardRecettes(today.minusMonths(1), today));
        global.put("stocks", reportingUseCase.getDashboardStocks());
        global.put("urgences", reportingUseCase.getDashboardUrgences(today));
        return ResponseEntity.ok(global);
    }
}

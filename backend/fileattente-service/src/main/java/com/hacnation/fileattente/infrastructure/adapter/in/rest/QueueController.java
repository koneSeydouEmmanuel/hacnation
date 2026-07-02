package com.hacnation.fileattente.infrastructure.adapter.in.rest;

import com.hacnation.common.dto.FileAttenteDto;
import com.hacnation.fileattente.application.service.CallNextUseCase;
import com.hacnation.fileattente.application.service.CheckInUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/queue")
public class QueueController {

    private final CheckInUseCase checkInUseCase;
    private final CallNextUseCase callNextUseCase;

    public QueueController(CheckInUseCase checkInUseCase,
                           CallNextUseCase callNextUseCase) {
        this.checkInUseCase = checkInUseCase;
        this.callNextUseCase = callNextUseCase;
    }

    @PostMapping("/checkin")
    public ResponseEntity<FileAttenteDto> checkin(@RequestBody Map<String, Object> body) {
        String patientId = (String) body.get("patientId");
        String rdvId = (String) body.get("rdvId");
        String service = (String) body.get("service");
        String patientNom = (String) body.get("patientNom");
        String patientPrenom = (String) body.get("patientPrenom");

        FileAttenteDto dto = checkInUseCase.execute(patientId, rdvId, service, patientNom, patientPrenom);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/checkin-qr")
    public ResponseEntity<FileAttenteDto> checkinQr(@RequestBody Map<String, String> body) {
        String qrData = body.get("qrData");
        if (qrData == null || qrData.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        FileAttenteDto dto = checkInUseCase.checkinByQr(qrData);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<FileAttenteDto>> getFileAttente(@RequestParam String service) {
        List<FileAttenteDto> file = checkInUseCase.getFileAttente(service);
        return ResponseEntity.ok(file);
    }

    @GetMapping("/{patientId}/position")
    public ResponseEntity<Map<String, Object>> getPosition(@PathVariable String patientId) {
        Map<String, Object> info = checkInUseCase.getPositionInfo(patientId);
        return ResponseEntity.ok(info);
    }

    @PostMapping("/call-next")
    public ResponseEntity<FileAttenteDto> callNext(@RequestBody Map<String, String> body) {
        String service = body.get("service");
        FileAttenteDto dto = callNextUseCase.execute(service);
        return ResponseEntity.ok(dto);
    }
}

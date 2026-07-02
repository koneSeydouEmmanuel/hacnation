package com.hacnation.notification.infrastructure.adapter.inbound;

import com.hacnation.common.dto.NotificationDto;
import com.hacnation.notification.domain.port.inbound.NotificationUseCase;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationUseCase notificationUseCase;

    public NotificationController(NotificationUseCase notificationUseCase) {
        this.notificationUseCase = notificationUseCase;
    }

    @GetMapping("/historique")
    public ResponseEntity<List<NotificationDto>> getHistorique(@RequestParam String patientId) {
        return ResponseEntity.ok(notificationUseCase.getHistorique(patientId));
    }

    @PostMapping("/envoyer")
    public ResponseEntity<NotificationDto> envoyer(@RequestBody Map<String, String> body) {
        String patientId = body.get("patientId");
        String canal = body.get("canal");
        String contenu = body.get("contenu");
        String destinataire = body.get("destinataire");

        NotificationDto dto = notificationUseCase.envoyer(patientId, canal, contenu, destinataire);
        return ResponseEntity.ok(dto);
    }
}

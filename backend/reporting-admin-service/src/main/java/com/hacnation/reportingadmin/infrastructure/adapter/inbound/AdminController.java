package com.hacnation.reportingadmin.infrastructure.adapter.inbound;

import com.hacnation.reportingadmin.domain.model.ActeMedical;
import com.hacnation.reportingadmin.domain.model.ServiceMedical;
import com.hacnation.reportingadmin.domain.model.Tarif;
import com.hacnation.reportingadmin.domain.model.Utilisateur;
import com.hacnation.reportingadmin.domain.port.inbound.AdminUseCase;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminUseCase adminUseCase;

    public AdminController(AdminUseCase adminUseCase) {
        this.adminUseCase = adminUseCase;
    }

    @GetMapping("/utilisateurs")
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs() {
        return ResponseEntity.ok(adminUseCase.getAllUtilisateurs());
    }

    @PostMapping("/utilisateurs")
    public ResponseEntity<Utilisateur> createUtilisateur(@RequestBody Utilisateur utilisateur) {
        return ResponseEntity.ok(adminUseCase.createUtilisateur(utilisateur));
    }

    @GetMapping("/utilisateurs/{id}")
    public ResponseEntity<Utilisateur> getUtilisateur(@PathVariable String id) {
        return ResponseEntity.ok(adminUseCase.getUtilisateurById(id));
    }

    @PutMapping("/utilisateurs/{id}")
    public ResponseEntity<Utilisateur> updateUtilisateur(@PathVariable String id, @RequestBody Utilisateur utilisateur) {
        return ResponseEntity.ok(adminUseCase.updateUtilisateur(id, utilisateur));
    }

    @DeleteMapping("/utilisateurs/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable String id) {
        adminUseCase.deleteUtilisateur(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/services")
    public ResponseEntity<List<ServiceMedical>> getAllServices() {
        return ResponseEntity.ok(adminUseCase.getAllServices());
    }

    @PostMapping("/services")
    public ResponseEntity<ServiceMedical> createService(@RequestBody ServiceMedical service) {
        return ResponseEntity.ok(adminUseCase.createService(service));
    }

    @PutMapping("/services/{id}")
    public ResponseEntity<ServiceMedical> updateService(@PathVariable String id, @RequestBody ServiceMedical service) {
        return ResponseEntity.ok(adminUseCase.updateService(id, service));
    }

    @DeleteMapping("/services/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable String id) {
        adminUseCase.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/actes")
    public ResponseEntity<List<ActeMedical>> getAllActes() {
        return ResponseEntity.ok(adminUseCase.getAllActes());
    }

    @PostMapping("/actes")
    public ResponseEntity<ActeMedical> createActe(@RequestBody ActeMedical acte) {
        return ResponseEntity.ok(adminUseCase.createActe(acte));
    }

    @PutMapping("/actes/{id}")
    public ResponseEntity<ActeMedical> updateActe(@PathVariable String id, @RequestBody ActeMedical acte) {
        return ResponseEntity.ok(adminUseCase.updateActe(id, acte));
    }

    @DeleteMapping("/actes/{id}")
    public ResponseEntity<Void> deleteActe(@PathVariable String id) {
        adminUseCase.deleteActe(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tarifs")
    public ResponseEntity<List<Tarif>> getAllTarifs() {
        return ResponseEntity.ok(adminUseCase.getAllTarifs());
    }

    @PostMapping("/tarifs")
    public ResponseEntity<Tarif> createTarif(@RequestBody Tarif tarif) {
        return ResponseEntity.ok(adminUseCase.createTarif(tarif));
    }

    @PutMapping("/tarifs/{id}")
    public ResponseEntity<Tarif> updateTarif(@PathVariable String id, @RequestBody Tarif tarif) {
        return ResponseEntity.ok(adminUseCase.updateTarif(id, tarif));
    }

    @DeleteMapping("/tarifs/{id}")
    public ResponseEntity<Void> deleteTarif(@PathVariable String id) {
        adminUseCase.deleteTarif(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/audit")
    public ResponseEntity<List<Map<String, Object>>> getAuditTrail(
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin,
            @RequestParam(required = false) String service) {
        return ResponseEntity.ok(adminUseCase.getAuditTrailFiltre(action, userId, dateDebut, dateFin, service));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealth() {
        return ResponseEntity.ok(adminUseCase.getHealthStatus());
    }
}

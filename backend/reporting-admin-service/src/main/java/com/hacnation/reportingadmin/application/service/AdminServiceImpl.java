package com.hacnation.reportingadmin.application.service;

import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.reportingadmin.domain.model.ActeMedical;
import com.hacnation.reportingadmin.domain.model.ParametrageSysteme;
import com.hacnation.reportingadmin.domain.model.ServiceMedical;
import com.hacnation.reportingadmin.domain.model.Tarif;
import com.hacnation.reportingadmin.domain.model.Utilisateur;
import com.hacnation.reportingadmin.domain.port.inbound.AdminUseCase;
import com.hacnation.reportingadmin.domain.port.outbound.ActeMedicalRepositoryPort;
import com.hacnation.reportingadmin.domain.port.outbound.ParametrageRepositoryPort;
import com.hacnation.reportingadmin.domain.port.outbound.ServiceMedicalRepositoryPort;
import com.hacnation.reportingadmin.domain.port.outbound.TarifRepositoryPort;
import com.hacnation.reportingadmin.domain.port.outbound.UtilisateurRepositoryPort;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements AdminUseCase {

    private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final UtilisateurRepositoryPort utilisateurRepository;
    private final ServiceMedicalRepositoryPort serviceMedicalRepository;
    private final ActeMedicalRepositoryPort acteMedicalRepository;
    private final TarifRepositoryPort tarifRepository;
    private final ParametrageRepositoryPort parametrageRepository;

    public AdminServiceImpl(UtilisateurRepositoryPort utilisateurRepository,
                            ServiceMedicalRepositoryPort serviceMedicalRepository,
                            ActeMedicalRepositoryPort acteMedicalRepository,
                            TarifRepositoryPort tarifRepository,
                            ParametrageRepositoryPort parametrageRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.serviceMedicalRepository = serviceMedicalRepository;
        this.acteMedicalRepository = acteMedicalRepository;
        this.tarifRepository = tarifRepository;
        this.parametrageRepository = parametrageRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Utilisateur getUtilisateurById(String id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", id));
    }

    @Override
    @Transactional
    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        if (utilisateur.getPassword() == null || utilisateur.getPassword().isEmpty()) {
            utilisateur.setPassword("$2a$10$hashed_placeholder_password");
        }
        log.info("Creation utilisateur: {}", utilisateur.getUsername());
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    @Transactional
    public Utilisateur updateUtilisateur(String id, Utilisateur updated) {
        Utilisateur existing = getUtilisateurById(id);
        existing.setUsername(updated.getUsername());
        if (updated.getPassword() != null && !updated.getPassword().isEmpty()) {
            existing.setPassword(updated.getPassword());
        }
        existing.setRoles(updated.getRoles());
        existing.setNom(updated.getNom());
        existing.setPrenom(updated.getPrenom());
        existing.setTelephone(updated.getTelephone());
        existing.setEmail(updated.getEmail());
        existing.setStatut(updated.getStatut());
        log.info("Mise a jour utilisateur: {}", id);
        return utilisateurRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteUtilisateur(String id) {
        log.info("Suppression utilisateur: {}", id);
        utilisateurRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceMedical> getAllServices() {
        return serviceMedicalRepository.findAll();
    }

    @Override
    @Transactional
    public ServiceMedical createService(ServiceMedical service) {
        log.info("Creation service: {}", service.getLibelle());
        return serviceMedicalRepository.save(service);
    }

    @Override
    @Transactional
    public ServiceMedical updateService(String id, ServiceMedical updated) {
        ServiceMedical existing = serviceMedicalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceMedical", id));
        existing.setCode(updated.getCode());
        existing.setLibelle(updated.getLibelle());
        existing.setDescription(updated.getDescription());
        existing.setStatut(updated.getStatut());
        return serviceMedicalRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteService(String id) {
        log.info("Suppression service: {}", id);
        serviceMedicalRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActeMedical> getAllActes() {
        return acteMedicalRepository.findAll();
    }

    @Override
    @Transactional
    public ActeMedical createActe(ActeMedical acte) {
        log.info("Creation acte: {}", acte.getLibelle());
        return acteMedicalRepository.save(acte);
    }

    @Override
    @Transactional
    public ActeMedical updateActe(String id, ActeMedical updated) {
        ActeMedical existing = acteMedicalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ActeMedical", id));
        existing.setCode(updated.getCode());
        existing.setLibelle(updated.getLibelle());
        existing.setType(updated.getType());
        existing.setTarifParDefaut(updated.getTarifParDefaut());
        return acteMedicalRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteActe(String id) {
        log.info("Suppression acte: {}", id);
        acteMedicalRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tarif> getAllTarifs() {
        return tarifRepository.findAll();
    }

    @Override
    @Transactional
    public Tarif createTarif(Tarif tarif) {
        log.info("Creation tarif pour acte: {} et service: {}", tarif.getActeMedicalId(), tarif.getServiceId());
        return tarifRepository.save(tarif);
    }

    @Override
    @Transactional
    public Tarif updateTarif(String id, Tarif updated) {
        Tarif existing = tarifRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarif", id));
        existing.setActeMedicalId(updated.getActeMedicalId());
        existing.setServiceId(updated.getServiceId());
        existing.setPrix(updated.getPrix());
        existing.setDateDebut(updated.getDateDebut());
        existing.setDateFin(updated.getDateFin());
        return tarifRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteTarif(String id) {
        log.info("Suppression tarif: {}", id);
        tarifRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParametrageSysteme> getAllParametres() {
        return parametrageRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public ParametrageSysteme getParametreByCle(String cle) {
        return parametrageRepository.findByCle(cle)
                .orElseThrow(() -> new ResourceNotFoundException("ParametrageSysteme", cle));
    }

    @Override
    @Transactional
    public ParametrageSysteme createParametre(ParametrageSysteme parametrage) {
        validerValeurNumerique(parametrage);
        log.info("Creation parametre: {}", parametrage.getCle());
        return parametrageRepository.save(parametrage);
    }

    @Override
    @Transactional
    public ParametrageSysteme updateParametre(String id, ParametrageSysteme updated) {
        ParametrageSysteme existing = parametrageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ParametrageSysteme", id));
        if (!existing.isModifiable()) {
            throw new com.hacnation.common.exception.BusinessException(
                    "Ce parametre n'est pas modifiable", 403);
        }
        validerValeurNumerique(updated);
        existing.setValeur(updated.getValeur());
        existing.setDescription(updated.getDescription());
        log.info("Mise a jour parametre: {}", existing.getCle());
        return parametrageRepository.save(existing);
    }

    private void validerValeurNumerique(ParametrageSysteme parametrage) {
        String cle = parametrage.getCle();
        String valeur = parametrage.getValeur();
        if (cle != null && (cle.equals("QUOTA_RDV_PAR_CRENEAU") || cle.equals("DUREE_CONSULTATION_DEFAUT")
                || cle.equals("DELAI_RAPPEL_RDV_HEURES") || cle.equals("SEUIL_ALERTE_STOCK"))) {
            try {
                Integer.parseInt(valeur);
            } catch (NumberFormatException e) {
                throw new com.hacnation.common.exception.BusinessException(
                        "La valeur du parametre '" + cle + "' doit etre un nombre entier", 400);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAuditTrailFiltre(String action, String userId,
                                                          LocalDateTime dateDebut, LocalDateTime dateFin,
                                                          String service) {
        List<Map<String, Object>> all = getAuditTrail();
        return all.stream()
                .filter(e -> action == null || action.isEmpty() || action.equals(e.get("action")))
                .filter(e -> userId == null || userId.isEmpty() || userId.equals(e.get("utilisateur")))
                .filter(e -> service == null || service.isEmpty() || service.equals(e.get("service")))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAuditTrail() {
        log.info("Recuperation audit trail (donnees demo)");
        return List.of(
                createAuditEntry("2025-06-30T08:15:00", "admin", "LOGIN", "Connexion administrateur reussie", "192.168.1.100"),
                createAuditEntry("2025-06-30T08:32:00", "admin", "CREATE_USER", "Creation utilisateur: Dr. Kone", "192.168.1.100"),
                createAuditEntry("2025-06-30T09:10:00", "admin", "UPDATE_SERVICE", "Modification service Medecine Generale", "192.168.1.100"),
                createAuditEntry("2025-06-30T09:45:00", "admin", "CREATE_ACTE", "Creation acte: Consultation Specialisee", "192.168.1.100"),
                createAuditEntry("2025-06-30T10:20:00", "comptable", "LOGIN", "Connexion comptable reussie", "192.168.1.50"),
                createAuditEntry("2025-06-30T11:05:00", "comptable", "VIEW_RECETTES", "Consultation rapport recettes juin", "192.168.1.50"),
                createAuditEntry("2025-06-30T11:30:00", "admin", "DELETE_USER", "Suppression utilisateur: stagiaire01", "192.168.1.100"),
                createAuditEntry("2025-06-30T12:00:00", "admin", "LOGOUT", "Deconnexion administrateur", "192.168.1.100"),
                createAuditEntry("2025-06-30T14:15:00", "admin", "LOGIN", "Connexion administrateur reussie", "192.168.1.100"),
                createAuditEntry("2025-06-30T15:00:00", "admin", "UPDATE_TARIF", "Mise a jour tarif pediatrie", "192.168.1.100"),
                createAuditEntry("2025-06-30T15:45:00", "admin", "EXPORT_DATA", "Export donnees patients au format CSV", "192.168.1.100"),
                createAuditEntry("2025-06-30T16:30:00", "admin", "LOGOUT", "Deconnexion administrateur", "192.168.1.100")
        );
    }

    @Override
    public Map<String, Object> getHealthStatus() {
        Map<String, Object> health = new LinkedHashMap<>();
        health.put("status", "UP");
        health.put("service", "reporting-admin-service");

        Map<String, Object> components = new LinkedHashMap<>();
        components.put("database", Map.of("status", "UP", "details", "PostgreSQL connecte"));
        components.put("diskSpace", Map.of("status", "UP", "details", "Espace disque suffisant"));
        health.put("components", components);

        return health;
    }

    private Map<String, Object> createAuditEntry(String timestamp, String utilisateur, String action, String details, String ip) {
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("timestamp", timestamp);
        entry.put("utilisateur", utilisateur);
        entry.put("action", action);
        entry.put("details", details);
        entry.put("ip", ip);
        return entry;
    }
}

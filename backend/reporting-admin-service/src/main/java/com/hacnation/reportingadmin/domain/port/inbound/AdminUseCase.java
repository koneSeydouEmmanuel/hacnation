package com.hacnation.reportingadmin.domain.port.inbound;

import com.hacnation.reportingadmin.domain.model.ActeMedical;
import com.hacnation.reportingadmin.domain.model.ParametrageSysteme;
import com.hacnation.reportingadmin.domain.model.ServiceMedical;
import com.hacnation.reportingadmin.domain.model.Tarif;
import com.hacnation.reportingadmin.domain.model.Utilisateur;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface AdminUseCase {

    List<Utilisateur> getAllUtilisateurs();
    Utilisateur getUtilisateurById(String id);
    Utilisateur createUtilisateur(Utilisateur utilisateur);
    Utilisateur updateUtilisateur(String id, Utilisateur updated);
    void deleteUtilisateur(String id);

    List<ServiceMedical> getAllServices();
    ServiceMedical createService(ServiceMedical service);
    ServiceMedical updateService(String id, ServiceMedical updated);
    void deleteService(String id);

    List<ActeMedical> getAllActes();
    ActeMedical createActe(ActeMedical acte);
    ActeMedical updateActe(String id, ActeMedical updated);
    void deleteActe(String id);

    List<Tarif> getAllTarifs();
    Tarif createTarif(Tarif tarif);
    Tarif updateTarif(String id, Tarif updated);
    void deleteTarif(String id);

    List<ParametrageSysteme> getAllParametres();
    ParametrageSysteme getParametreByCle(String cle);
    ParametrageSysteme createParametre(ParametrageSysteme parametrage);
    ParametrageSysteme updateParametre(String id, ParametrageSysteme updated);

    List<Map<String, Object>> getAuditTrail();
    List<Map<String, Object>> getAuditTrailFiltre(String action, String userId, LocalDateTime dateDebut, LocalDateTime dateFin, String service);
    Map<String, Object> getHealthStatus();
}

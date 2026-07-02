package com.hacnation.reportingadmin.domain.port.outbound;

import com.hacnation.reportingadmin.domain.model.Utilisateur;
import java.util.List;
import java.util.Optional;

public interface UtilisateurRepositoryPort {

    Utilisateur save(Utilisateur utilisateur);
    Optional<Utilisateur> findById(String id);
    Optional<Utilisateur> findByUsername(String username);
    List<Utilisateur> findAll();
    List<Utilisateur> findByRolesContaining(String role);
    void deleteById(String id);
}

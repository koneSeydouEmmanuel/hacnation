package com.hacnation.reportingadmin.infrastructure.adapter.outbound;

import com.hacnation.reportingadmin.domain.model.Utilisateur;
import com.hacnation.reportingadmin.domain.port.outbound.UtilisateurRepositoryPort;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UtilisateurJpaRepository extends JpaRepository<Utilisateur, String> {

    Optional<Utilisateur> findByUsername(String username);

    List<Utilisateur> findByRolesContaining(String role);
}

@Repository
class UtilisateurJpaAdapter implements UtilisateurRepositoryPort {

    private final UtilisateurJpaRepository jpaRepository;

    UtilisateurJpaAdapter(UtilisateurJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur) {
        return jpaRepository.save(utilisateur);
    }

    @Override
    public Optional<Utilisateur> findById(String id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Utilisateur> findByUsername(String username) {
        return jpaRepository.findByUsername(username);
    }

    @Override
    public List<Utilisateur> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<Utilisateur> findByRolesContaining(String role) {
        return jpaRepository.findByRolesContaining(role);
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}

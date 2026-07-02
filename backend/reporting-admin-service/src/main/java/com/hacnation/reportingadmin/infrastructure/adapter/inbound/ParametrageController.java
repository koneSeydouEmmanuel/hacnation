package com.hacnation.reportingadmin.infrastructure.adapter.inbound;

import com.hacnation.reportingadmin.domain.model.ParametrageSysteme;
import com.hacnation.reportingadmin.domain.port.inbound.AdminUseCase;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/parametres")
public class ParametrageController {

    private final AdminUseCase adminUseCase;

    public ParametrageController(AdminUseCase adminUseCase) {
        this.adminUseCase = adminUseCase;
    }

    @GetMapping
    public ResponseEntity<List<ParametrageSysteme>> getAllParametres() {
        return ResponseEntity.ok(adminUseCase.getAllParametres());
    }

    @GetMapping("/{cle}")
    public ResponseEntity<ParametrageSysteme> getParametreByCle(@PathVariable String cle) {
        return ResponseEntity.ok(adminUseCase.getParametreByCle(cle));
    }

    @PostMapping
    public ResponseEntity<ParametrageSysteme> createParametre(@RequestBody ParametrageSysteme parametrage) {
        return ResponseEntity.status(201).body(adminUseCase.createParametre(parametrage));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParametrageSysteme> updateParametre(@PathVariable String id,
                                                               @RequestBody ParametrageSysteme parametrage) {
        return ResponseEntity.ok(adminUseCase.updateParametre(id, parametrage));
    }
}

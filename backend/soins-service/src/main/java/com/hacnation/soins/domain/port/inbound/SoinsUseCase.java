package com.hacnation.soins.domain.port.inbound;

import com.hacnation.soins.domain.model.SoinInfirmier;
import java.util.List;

public interface SoinsUseCase {

    SoinInfirmier creerPlanSoins(String hospitalisationId, String patientId,
                                 String typeSoin, String description,
                                 String frequence, String instructions);

    SoinInfirmier administrerSoin(String soinId, String infirmierId);

    List<SoinInfirmier> getSoinsPatient(String patientId);

    SoinInfirmier nonAdministre(String soinId, String motif);
}

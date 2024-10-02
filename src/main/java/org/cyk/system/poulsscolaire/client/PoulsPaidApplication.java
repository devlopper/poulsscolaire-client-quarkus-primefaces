package org.cyk.system.poulsscolaire.client;

import ci.gouv.dgbf.extension.primefaces.template.ContainerManager;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

/**
 * Cette classe repérsente le point d'entrée du processus.
 *
 * @author Christian
 *
 */
public class PoulsPaidApplication {

  @Inject
  ContainerManager containerManager;
  
  void onStart(@Observes StartupEvent startupEvent) {
    containerManager.setTheme("creative");
    containerManager.setLayout("vertical");
    
  }
}

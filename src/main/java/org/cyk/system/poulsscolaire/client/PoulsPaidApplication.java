package org.cyk.system.poulsscolaire.client;

import ci.gouv.dgbf.extension.core.Core;
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

  /**
   * Cette méthode permet d'écouter le démarrage.
   *
   * @param startupEvent {@link StartupEvent}
   */
  void onStart(@Observes StartupEvent startupEvent) {
    Core.runIfStringBlank(System.getProperty("authentifiable"),
        () -> System.setProperty("authentifiable", "false"));
    containerManager.setTheme("creative");
    containerManager.setLayout("vertical");

  }
}

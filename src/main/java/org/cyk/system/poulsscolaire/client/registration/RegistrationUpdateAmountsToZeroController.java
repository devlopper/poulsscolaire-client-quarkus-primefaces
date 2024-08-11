package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractActionController;
import ci.gouv.dgbf.extension.primefaces.ActionExecutor;
import ci.gouv.dgbf.extension.server.service.api.response.IdentifiableResponseDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationClient;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationService;

/**
 * Cette classe représente le contrôleur de {@link RegistrationService#updateAmountsToZero}.
 *
 * @author Christian
 *
 */
@Dependent
public class RegistrationUpdateAmountsToZeroController
    extends AbstractActionController<IdentifiableResponseDto> {

  @Inject
  RegistrationClient client;

  public RegistrationUpdateAmountsToZeroController() {
    name = "Mise à jour des montants à zéro";
  }

  /**
   * Cette méthode permet de supprimer.
   *
   * @return navigation
   */
  public String updateByIdentifier(String identifier) {
    return new ActionExecutor<String>(this, name, () -> {
      IdentifiableResponseDto response =
          client.updateAmountsToZero(identifier, userIdentifier, null);
      processResponse(response);
      return navigate(response);
    }).execute();
  }
}

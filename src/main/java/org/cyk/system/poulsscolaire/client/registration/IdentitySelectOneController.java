package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityClient;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityDto;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityService.IdentityGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link IdentityDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class IdentitySelectOneController extends AbstractSelectOneIdentifiableController<
    IdentityDto, IdentityGetManyResponseDto, IdentityClient> {

  @Inject
  @Getter
  IdentityClient client;

  protected IdentitySelectOneController() {
    super(IdentityDto.class, SelectItemLabelStrategy.AS_STRING);
  }
}

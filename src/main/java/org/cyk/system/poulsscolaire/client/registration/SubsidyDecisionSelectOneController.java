package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionClient;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionDto;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionService.SubsidyDecisionGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link SubsidyDecisionDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class SubsidyDecisionSelectOneController extends AbstractSelectOneIdentifiableController<
    SubsidyDecisionDto, SubsidyDecisionGetManyResponseDto, SubsidyDecisionClient> {

  @Inject
  @Getter
  SubsidyDecisionClient client;

  protected SubsidyDecisionSelectOneController() {
    super(SubsidyDecisionDto.class, SelectItemLabelStrategy.AS_STRING);
  }
}

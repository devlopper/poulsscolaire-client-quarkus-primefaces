package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineClient;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineDto;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineFilter;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineService.DeadlineGetManyResponseDto;

/**
 * Cette classe représente un sélecteur d'un élément de {@link DeadlineDto}.
 *
 * @author Christian
 *
 */
@Getter 
@Dependent
public class DeadlineSelectOneController extends AbstractSelectOneIdentifiableController<
    DeadlineDto, DeadlineGetManyResponseDto, DeadlineClient> {

  @Inject
  @Getter
  DeadlineClient client;

  @Inject
  SessionController sessionController;
  
  protected DeadlineSelectOneController() {
    super(DeadlineDto.class, SelectItemLabelStrategy.AS_STRING);
  }

  @Override
  protected void postConstruct() {
    super.postConstruct();
    Core.runIfStringNotBlank(sessionController.getSchoolIdentifier(), () -> {
      DeadlineFilter deadlineFilter = new DeadlineFilter();
      deadlineFilter.setSchoolIdentifier(sessionController.getSchoolIdentifier());
      filter = deadlineFilter.toDto();
    });
  }
}

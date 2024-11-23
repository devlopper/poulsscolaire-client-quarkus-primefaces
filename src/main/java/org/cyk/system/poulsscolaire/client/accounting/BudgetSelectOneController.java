package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.component.input.AbstractSelectOneIdentifiableController;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.SessionController;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetClient;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetDto;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetFilter;
import org.cyk.system.poulsscolaire.server.api.accounting.BudgetService.BudgetGetManyResponseDto;

/**
 * Cette classe représente le contrôleur de sélection de {@link BudgetDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class BudgetSelectOneController extends AbstractSelectOneIdentifiableController<
    BudgetDto, BudgetGetManyResponseDto, BudgetClient> {

  @Inject
  @Getter
  BudgetClient client;

  @Inject
  SessionController sessionController;
  
  protected BudgetSelectOneController() {
    super(BudgetDto.class);
  }
  
  @Override
  protected void postConstruct() {
    super.postConstruct();
    Core.runIfStringNotBlank(sessionController.getSchoolIdentifier(), () -> {
      BudgetFilter budgetFilter = new BudgetFilter();
      budgetFilter.setSchoolIdentifier(sessionController.getSchoolIdentifier());
      filter = budgetFilter.toDto();
    });
  }
}

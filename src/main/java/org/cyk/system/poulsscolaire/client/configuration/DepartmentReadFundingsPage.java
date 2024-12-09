package org.cyk.system.poulsscolaire.client.configuration;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.accounting.FundingController;
import org.cyk.system.poulsscolaire.server.api.configuration.DepartmentDto;

/**
 * Cette classe représente la page de lecture de des financements de {@link DepartmentDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class DepartmentReadFundingsPage extends AbstractDepartmentReadDatasPage {
  
  @Inject
  @Getter
  FundingController fundingController;

  @Override
  protected void postConstruct() {
    super.postConstruct();

    fundingController.getFilterController().getFilter()
        .setDepartmentIdentifier(department.getIdentifier());
    fundingController.initialize();
  }

  public static final String OUTCOME = "departmentReadFundingsPage";
}

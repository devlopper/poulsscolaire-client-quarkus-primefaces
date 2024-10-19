package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolConfigurationDto;

/**
 * Cette classe représente la page de liste de {@link SchoolConfigurationDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class SchoolConfigurationListPage extends AbstractPage {

  @Inject
  @Getter
  SchoolConfigurationController controller;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = "Liste " + SchoolConfigurationDto.PLURAL_NAME;

    controller.initialize();
    controller.getListController().getDataTable().getActionColumn()
        .computeWithForButtonsWithIconOnly(3);
  }

  public static final String OUTCOME = "schoolConfigurationListPage";
}

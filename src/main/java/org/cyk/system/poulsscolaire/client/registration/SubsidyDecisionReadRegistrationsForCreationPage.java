package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.registration.RegistrationDto;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionDto;

/**
 * Cette classe représente la page de lecture de {@link RegistrationDto} de
 * {@link SubsidyDecisionDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class SubsidyDecisionReadRegistrationsForCreationPage
    extends AbstractSubsidyDecisionReadDataPage {

  @Inject
  @Getter
  RegistrationController registrationController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    registrationController.projection.getNames().clear();
    registrationController.projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        RegistrationDto.JSON_STUDENT_AS_STRING, RegistrationDto.JSON_BRANCH_INSTANCE_AS_STRING);

    registrationController.getFilterController().getFilter()
        .setSchoolIdentifier(subsidyDecision.getSchoolingIdentifier());
    registrationController.getFilterController().getFilter()
        .setDoesNotBelongsToSubsidyDecisionIdentifier(subsidyDecision.getIdentifier());
    registrationController.initialize();
    registrationController.getListController().getShowCreateDialogButton().setRendered(false);
    registrationController.getListController().getShowUpdateDialogButton().setRendered(false);
    registrationController.getListController().getDeleteButton().setRendered(false);
    registrationController.getListController().getGotoReadPageButton().setRendered(false);
    configureDataTable(registrationController.getListController().getDataTable());
    registrationController.getListController().getDataTable().getActionColumn()
        .computeWithForButtonsWithIconOnly(2);
  }

  public static final String OUTCOME = "subsidyDecisionReadRegistrationsForCreationPage";
}

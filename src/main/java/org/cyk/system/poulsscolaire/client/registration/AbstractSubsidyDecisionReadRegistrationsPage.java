package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import jakarta.inject.Inject;
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
public abstract class AbstractSubsidyDecisionReadRegistrationsPage
    extends AbstractSubsidyDecisionReadDataPage {

  @Inject
  @Getter
  RegistrationController registrationController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    initializeRegistrationController();
  }

  void initializeRegistrationControllerProjection() {
    registrationController.projection.getNames().clear();
    registrationController.projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        RegistrationDto.JSON_STUDENT_AS_STRING, RegistrationDto.JSON_BRANCH_INSTANCE_AS_STRING);
  }

  void initializeRegistrationController() {
    initializeRegistrationControllerProjection();
    registrationController.getFilterController().getFilter()
        .setSchoolingIdentifier(subsidyDecision.getSchoolingIdentifier());
    registrationController.initialize();
    registrationController.getListController().getShowCreateDialogButton().setRendered(false);
    registrationController.getListController().getShowUpdateDialogButton().setRendered(false);
    registrationController.getListController().getDeleteButton().setRendered(false);
    registrationController.getListController().getGotoReadPageButton().setRendered(false);
    configureDataTable(registrationController.getListController().getDataTable());
    registrationController.getListController().getDataTable().getActionColumn()
        .computeWithForButtonsWithIconOnly(1);
  }
}

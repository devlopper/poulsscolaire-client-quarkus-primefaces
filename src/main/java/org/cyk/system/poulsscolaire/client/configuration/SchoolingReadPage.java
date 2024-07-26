package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.fee.FeeController;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingClient;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingDto;
import org.cyk.system.poulsscolaire.server.api.fee.AbstractAmountContainerDto;

/**
 * Cette classe représente la page de lecture de {@link SchoolingDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class SchoolingReadPage extends AbstractPage {

  @Inject
  SchoolingClient schoolingClient;

  @Getter
  SchoolingDto schooling;

  @Inject
  @Getter
  FeeController feeController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    String identifier = getRequestParameterIdentifier();
    schooling = schoolingClient.getByIdentifier(identifier,
        new ProjectionDto().addNames(SchoolingDto.JSON_BRANCH_AS_STRING,
            SchoolingDto.JSON_NOT_OPTIONAL_FEE_AMOUNT_VALUE_AS_STRING,
            SchoolingDto.JSON_NOT_OPTIONAL_FEE_AMOUNT_REGISTRATION_VALUE_PART_AS_STRING),
        userIdentifier, null);
    contentTitle = SchoolingDto.NAME + " - " + schooling.getBranchAsString();
    
    feeController.getFilterController().getFilter().setSchoolingIdentifier(identifier);
    if (feeController.getFilterController().getFilter().getAmountOptional() == null) {
      feeController.getFilterController().getFilter().setAmountOptional(false);
    }
    feeController.initialize();
    if (Boolean.TRUE.equals(feeController.getFilterController().getFilter().getAmountOptional())) {
      ((ProjectionDto) feeController.getListController().getReadController().getProjection())
          .getNames().remove(AbstractAmountContainerDto.JSON_AMOUNT_PAYMENT_ORDER_NUMBER_AS_STRING);
    }

    ((ProjectionDto) feeController.getListController().getReadController().getProjection())
        .getNames().remove(AbstractAmountContainerDto.JSON_AMOUNT_OPTIONAL_AS_STRING);
  }

  public static final String OUTCOME = "schoolingReadPage";
}

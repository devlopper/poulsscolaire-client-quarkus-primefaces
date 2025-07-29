package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.primefaces.component.DataTable;
import ci.gouv.dgbf.extension.primefaces.component.information.LabelValueGroupsInformationCard;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionClient;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionDto;

/**
 * Cette classe représente la base de page de lecture de données de {@link SubsidyDecisionDto}.
 *
 * @author Christian
 *
 */
public abstract class AbstractSubsidyDecisionReadDataPage extends AbstractPage {

  @Inject
  SubsidyDecisionClient subsidyDecisionClient;

  @Getter
  SubsidyDecisionDto subsidyDecision;

  @Inject
  @Getter
  SubsidyDecisionTabMenuController tabMenuController;

  @Getter
  LabelValueGroupsInformationCard labelValueGroupsInformationCard;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    contentTitle = SubsidyDecisionDto.NAME;

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, SubsidyDecisionDto.JSON_SCHOOLING_IDENTIFIER,
        SubsidyDecisionDto.JSON_SCHOOLING_AS_STRING, SubsidyDecisionDto.JSON_AMOUNT_AS_STRING);
    String identifier = getRequestParameterIdentifier();
    subsidyDecision =
        subsidyDecisionClient.getByIdentifier(identifier, projection, userIdentifier, null);

    tabMenuController.subsidyDecision = subsidyDecision;
    tabMenuController.initialize();

    labelValueGroupsInformationCard = new LabelValueGroupsInformationCard();
    labelValueGroupsInformationCard.getHeaderText().setValue("Informations");
    labelValueGroupsInformationCard.group().add("Numéro", subsidyDecision.getCode())
        .add("Branche", subsidyDecision.getSchoolingAsString())
        .add("Éffectif", subsidyDecision.getRegistrationCountAsString())
        .add("Montant", subsidyDecision.getAmountAsString())
        .add("Nombre de paiement", subsidyDecision.getPaymentCountAsString())
        .add("Montant payé", subsidyDecision.getPaidAmountAsString())
        .add("Reste à payer", subsidyDecision.getRemainingAmountToPayAsString());
  }

  void configureDataTable(DataTable dataTable) {
    dataTable.setHeaderRendered(false);
  }
}

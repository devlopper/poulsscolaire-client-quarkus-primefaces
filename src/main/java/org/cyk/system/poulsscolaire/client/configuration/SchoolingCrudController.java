package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingClient;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingDto;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingService;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolingService.SchoolingGenerateResponseDto;
import org.primefaces.PrimeFaces;

/**
 * Cette classe représente le contrôleur de CRUD de {@link SchoolingDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class SchoolingCrudController extends AbstractController {

  @Inject
  SchoolingClient client;

  @Inject
  @Getter
  ListController listController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = "Scolarités";

    listController.setEntityClass(SchoolingDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(SchoolingService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, SchoolingDto.JSON_SCHOOL_AS_STRING,
        SchoolingDto.JSON_BRANCH_AS_STRING, SchoolingDto.JSON_PERIOD_AS_STRING,
        SchoolingDto.JSON_NOT_OPTIONAL_FEE_AMOUNT_VALUE_AS_STRING,
        SchoolingDto.JSON_NOT_OPTIONAL_FEE_AMOUNT_REGISTRATION_VALUE_PART_AS_STRING);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getGotoReadPageButton().setRendered(true);
    listController.getGotoReadPageButton().setOutcome(SchoolingReadPage.OUTCOME);

    listController.getCreateController()
        .setFunction(entity -> client.create(((SchoolingDto) entity).getSchoolIdentifier(),
            ((SchoolingDto) entity).getBranchIdentifier(),
            ((SchoolingDto) entity).getPeriodIdentifier(), userIdentifier, null));

    listController.getUpdateController()
        .setFunction(entity -> client.update(((SchoolingDto) entity).getIdentifier(),
            ((SchoolingDto) entity).getSchoolIdentifier(),
            ((SchoolingDto) entity).getBranchIdentifier(),
            ((SchoolingDto) entity).getPeriodIdentifier(), userIdentifier, null));
  }

  public void generate() {
    SchoolingGenerateResponseDto response = client.generate(userIdentifier, null);
    PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(response.getMessage()));
  }
}

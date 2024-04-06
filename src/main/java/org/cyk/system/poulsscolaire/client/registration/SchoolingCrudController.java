package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.SchoolingClient;
import org.cyk.system.poulsscolaire.server.api.SchoolingDto;
import org.cyk.system.poulsscolaire.server.api.SchoolingService;

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
        AbstractIdentifiableCodableDto.JSON_CODE, SchoolingDto.JSON_SCHOOL_IDENTIFIER,
        SchoolingDto.JSON_BRANCH_IDENTIFIER, SchoolingDto.JSON_PERIOD_IDENTIFIER);
    listController.getReadController().setProjection(projection);

    listController.initialize();

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
}

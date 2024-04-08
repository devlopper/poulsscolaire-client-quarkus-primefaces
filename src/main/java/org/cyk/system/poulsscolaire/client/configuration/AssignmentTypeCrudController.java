package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.AssignmentTypeClient;
import org.cyk.system.poulsscolaire.server.api.configuration.AssignmentTypeDto;
import org.cyk.system.poulsscolaire.server.api.configuration.AssignmentTypeService;

/**
 * Cette classe représente le contrôleur de CRUD de {@link AssignmentTypeDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class AssignmentTypeCrudController extends AbstractController {

  @Inject
  AssignmentTypeClient client;

  @Inject
  @Getter
  ListController listController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = "Type d'affectation";

    listController.setEntityClass(AssignmentTypeDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(AssignmentTypeService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, AbstractIdentifiableCodableNamableDto.JSON_NAME);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController()
        .setFunction(entity -> client.create(((AssignmentTypeDto) entity).getCode(),
            ((AssignmentTypeDto) entity).getName(), userIdentifier, null));

    listController.getUpdateController()
        .setFunction(entity -> client.update(((AssignmentTypeDto) entity).getIdentifier(),
            ((AssignmentTypeDto) entity).getCode(), ((AssignmentTypeDto) entity).getName(),
            userIdentifier, null));
  }
}

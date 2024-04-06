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
import org.cyk.system.poulsscolaire.server.api.SeniorityClient;
import org.cyk.system.poulsscolaire.server.api.SeniorityDto;
import org.cyk.system.poulsscolaire.server.api.SeniorityService;

/**
 * Cette classe représente le contrôleur de CRUD de {@link SeniorityDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class SeniorityCrudController extends AbstractController {

  @Inject
  SeniorityClient client;

  @Inject
  @Getter
  ListController listController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = "Anciennetés";

    listController.setEntityClass(SeniorityDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(SeniorityService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, AbstractIdentifiableCodableNamableDto.JSON_NAME);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController()
        .setFunction(entity -> client.create(((SeniorityDto) entity).getCode(),
            ((SeniorityDto) entity).getName(), userIdentifier, null));

    listController.getUpdateController()
        .setFunction(entity -> client.update(((SeniorityDto) entity).getIdentifier(),
            ((SeniorityDto) entity).getCode(), ((SeniorityDto) entity).getName(), userIdentifier,
            null));
  }
}

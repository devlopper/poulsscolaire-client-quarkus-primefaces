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
import org.cyk.system.poulsscolaire.server.api.GenderClient;
import org.cyk.system.poulsscolaire.server.api.GenderDto;
import org.cyk.system.poulsscolaire.server.api.GenderService;

/**
 * Cette classe représente le contrôleur de CRUD de {@link GenderDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class GenderCrudController extends AbstractController {

  @Inject
  GenderClient client;

  @Inject
  @Getter
  ListController listController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = "Genre";

    listController.setEntityClass(GenderDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(GenderService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, AbstractIdentifiableCodableNamableDto.JSON_NAME);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController()
        .setFunction(entity -> client.create(((GenderDto) entity).getCode(),
            ((GenderDto) entity).getName(), userIdentifier, null));

    listController.getUpdateController()
        .setFunction(entity -> client.update(((GenderDto) entity).getIdentifier(),
            ((GenderDto) entity).getCode(), ((GenderDto) entity).getName(), userIdentifier,
            null));
  }
}

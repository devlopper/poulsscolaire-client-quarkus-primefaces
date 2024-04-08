package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineGroupClient;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineGroupDto;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineGroupService;

/**
 * Cette classe représente le contrôleur de CRUD de {@link DeadlineGroupDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class DeadlineGroupCrudController extends AbstractController {

  @Inject
  DeadlineGroupClient client;

  @Inject
  @Getter
  ListController listController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = "Groupe d'échéances";

    listController.setEntityClass(DeadlineGroupDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(DeadlineGroupService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, AbstractIdentifiableCodableNamableDto.JSON_NAME);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController()
        .setFunction(entity -> client.create(((DeadlineGroupDto) entity).getCode(),
            ((DeadlineGroupDto) entity).getName(), userIdentifier, null));

    listController.getUpdateController()
        .setFunction(entity -> client.update(((DeadlineGroupDto) entity).getIdentifier(),
            ((DeadlineGroupDto) entity).getCode(), ((DeadlineGroupDto) entity).getName(),
            userIdentifier, null));
  }
}

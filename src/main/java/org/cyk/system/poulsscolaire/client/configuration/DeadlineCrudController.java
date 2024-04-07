package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.ActionExecutor;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Inject;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.cyk.system.poulsscolaire.server.api.DeadlineClient;
import org.cyk.system.poulsscolaire.server.api.DeadlineDto;
import org.cyk.system.poulsscolaire.server.api.DeadlineGroupClient;
import org.cyk.system.poulsscolaire.server.api.DeadlineGroupService;
import org.cyk.system.poulsscolaire.server.api.DeadlineService;

/**
 * Cette classe représente le contrôleur de CRUD de {@link DeadlineDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class DeadlineCrudController extends AbstractController {

  @Inject
  DeadlineClient client;

  @Inject
  @Getter
  ListController listController;

  @Inject
  DeadlineGroupClient deadlineGroupClient;

  @Getter
  @Setter
  List<SelectItem> groups;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = "Groupe d'échéances";

    listController.setEntityClass(DeadlineDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(DeadlineService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, AbstractIdentifiableCodableNamableDto.JSON_NAME,
        DeadlineDto.JSON_GROUP_AS_STRING, DeadlineDto.JSON_DATE_AS_STRING);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController()
        .setFunction(entity -> client.create(((DeadlineDto) entity).getCode(),
            ((DeadlineDto) entity).getName(), ((DeadlineDto) entity).getGroupIdentifier(),
            ((DeadlineDto) entity).getDate(), userIdentifier, null));

    listController.getUpdateController()
        .setFunction(entity -> client.update(((DeadlineDto) entity).getIdentifier(),
            ((DeadlineDto) entity).getCode(), ((DeadlineDto) entity).getName(),
            ((DeadlineDto) entity).getGroupIdentifier(), ((DeadlineDto) entity).getDate(),
            userIdentifier, null));

    groups = new ActionExecutor<>(this, DeadlineGroupService.GET_MANY_IDENTIFIER,
        () -> deadlineGroupClient
            .getMany(
                new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
                    AbstractIdentifiableCodableNamableDto.JSON_NAME),
                null, null, userIdentifier, null)
            .getDatas().stream().map(dto -> new SelectItem(dto.getIdentifier(), dto.getName()))
            .toList()).execute();
  }
}

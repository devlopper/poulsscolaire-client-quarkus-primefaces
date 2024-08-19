package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.client.configuration.SchoolSelectOneController;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineClient;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineDto;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineGroupDto;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineService;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineService.DeadlineCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineService.DeadlineUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link DeadlineDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class DeadlineController extends AbstractController {

  @Inject
  DeadlineClient client;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  DeadlineFilterController filterController;

  @Inject
  @Getter
  DeadlineGroupSelectOneController groupSelectOneController;

  @Inject
  @Getter
  SchoolSelectOneController schoolSelectOneController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = DeadlineGroupDto.NAME;

    listController.setEntityClass(DeadlineDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(DeadlineService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();
    projection.addNamesIfStringBlank(filterController.getFilter().getSchoolIdentifier(),
        DeadlineDto.JSON_SCHOOL_AS_STRING);
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, AbstractIdentifiableCodableNamableDto.JSON_NAME,
        DeadlineDto.JSON_GROUP_AS_STRING, DeadlineDto.JSON_DATE_AS_STRING);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController().addEntityConsumer(entity -> ((DeadlineDto) entity)
        .setSchoolIdentifier(filterController.getFilter().getSchoolIdentifier()));
    listController.getCreateController().setFunction(entity -> {
      DeadlineCreateRequestDto request = new DeadlineCreateRequestDto();
      request.setGroupIdentifier(((DeadlineDto) entity).getGroupIdentifier());
      request.setSchoolIdentifier(((DeadlineDto) entity).getSchoolIdentifier());
      request.setName(((DeadlineDto) entity).getName());
      request.setDate(((DeadlineDto) entity).getDate());
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController().setFunction(entity -> {
      DeadlineUpdateRequestDto request = new DeadlineUpdateRequestDto();
      request.setIdentifier(((DeadlineDto) entity).getIdentifier());
      request.setGroupIdentifier(((DeadlineDto) entity).getGroupIdentifier());
      request.setSchoolIdentifier(((DeadlineDto) entity).getSchoolIdentifier());
      request.setName(((DeadlineDto) entity).getName());
      request.setDate(((DeadlineDto) entity).getDate());
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    schoolSelectOneController.getSelectOneMenu().addValueConsumer(
        identifier -> ((DeadlineDto) listController.getCreateControllerOrUpdateControllerEntity())
            .setSchoolIdentifier(identifier));
    schoolSelectOneController.getSelectOneMenu()
        .setRendered(Core.isStringBlank(filterController.getFilter().getSchoolIdentifier()));

    groupSelectOneController.getSelectOneMenu().addValueConsumer(
        identifier -> ((DeadlineDto) listController.getCreateControllerOrUpdateControllerEntity())
            .setGroupIdentifier(identifier));
  }
}

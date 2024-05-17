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
import org.cyk.system.poulsscolaire.server.api.fee.FeeCategoryClient;
import org.cyk.system.poulsscolaire.server.api.fee.FeeCategoryDto;
import org.cyk.system.poulsscolaire.server.api.fee.FeeCategoryService;

/**
 * Cette classe représente le contrôleur de CRUD de {@link FeeCategoryDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class FeeCategoryCrudController extends AbstractController {

  @Inject
  FeeCategoryClient client;

  @Inject
  @Getter
  ListController listController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = FeeCategoryDto.NAME;

    listController.setEntityClass(FeeCategoryDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(FeeCategoryService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, AbstractIdentifiableCodableNamableDto.JSON_NAME);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController()
        .setFunction(entity -> client.create(((FeeCategoryDto) entity).getCode(),
            ((FeeCategoryDto) entity).getName(), userIdentifier, null));

    listController.getUpdateController()
        .setFunction(entity -> client.update(((FeeCategoryDto) entity).getIdentifier(),
            ((FeeCategoryDto) entity).getCode(), ((FeeCategoryDto) entity).getName(),
            userIdentifier, null));
  }
}

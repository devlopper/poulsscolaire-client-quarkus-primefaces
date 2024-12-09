package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.configuration.DepartmentClient;
import org.cyk.system.poulsscolaire.server.api.configuration.DepartmentDto;
import org.cyk.system.poulsscolaire.server.api.configuration.DepartmentService;

/**
 * Cette classe représente le contrôleur de CRUD de {@link DepartmentDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class DepartmentController extends AbstractController {

  @Inject
  DepartmentClient client;

  @Inject
  @Getter
  ListController listController;
  
  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = DepartmentDto.PLURAL_NAME;

    listController.setEntityClass(DepartmentDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(DepartmentService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableNamableDto.JSON_NAME);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getGotoReadPageButton().setRendered(true);
    listController.getGotoReadPageButton().setOutcome(DepartmentReadBudgetsPage.OUTCOME);
  }
}

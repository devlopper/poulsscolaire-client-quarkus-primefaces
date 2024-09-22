package org.cyk.system.poulsscolaire.client.accounting;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingPlanClient;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingPlanDto;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingPlanRequestMapper;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingPlanService;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingPlanService.AccountingPlanCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.accounting.AccountingPlanService.AccountingPlanUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link AccountingPlanDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class AccountingPlanController extends AbstractController {

  @Inject
  AccountingPlanClient client;

  @Inject
  AccountingPlanRequestMapper requestMapper;

  @Inject
  @Getter
  ListController listController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = AccountingPlanDto.NAME;
  }

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    listController.setEntityClass(AccountingPlanDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(AccountingPlanService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, AbstractIdentifiableCodableNamableDto.JSON_NAME);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController().setFunction(entity -> {
      AccountingPlanCreateRequestDto request = requestMapper.mapCreate((AccountingPlanDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController().setFunction(entity -> {
      AccountingPlanUpdateRequestDto request = requestMapper.mapUpdate((AccountingPlanDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });
  }
}

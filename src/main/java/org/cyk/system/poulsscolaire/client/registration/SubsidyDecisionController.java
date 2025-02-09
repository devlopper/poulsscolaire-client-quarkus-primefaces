package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionClient;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionDto;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionRequestMapper;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionService;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionService.SubsidyDecisionCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.registration.SubsidyDecisionService.SubsidyDecisionUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link SubsidyDecisionDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class SubsidyDecisionController extends AbstractController {

  @Inject
  SubsidyDecisionClient client;

  @Inject
  SubsidyDecisionRequestMapper requestMapper;

  @Inject
  @Getter
  ListController listController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = SubsidyDecisionDto.NAME;
  }
  
  /**
   * Initialiser.
   */
  public void initialize() {
    listController.setEntityClass(SubsidyDecisionDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(SubsidyDecisionService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableCodableDto.JSON_CODE, AbstractIdentifiableCodableNamableDto.JSON_NAME);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController().setFunction(entity -> {
      SubsidyDecisionCreateRequestDto request =
          requestMapper.mapCreate((SubsidyDecisionDto) entity);
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController().setFunction(entity -> {
      SubsidyDecisionUpdateRequestDto request =
          requestMapper.mapUpdate((SubsidyDecisionDto) entity);
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });
  }
}

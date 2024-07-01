package org.cyk.system.poulsscolaire.client.configuration;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolClient;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolDto;
import org.cyk.system.poulsscolaire.server.api.configuration.SchoolService;
import org.cyk.system.poulsscolaire.server.api.fee.AmountStatisticsDto;

/**
 * Cette classe représente le contrôleur de CRUD de {@link SchoolDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class SchoolController extends AbstractController {

  @Inject
  SchoolClient client;

  @Inject
  @Getter
  ListController listController;

  @Getter
  @Setter
  AmountStatisticsDto amountValueStatistics;

  @Getter
  @Setter
  AmountStatisticsDto amountRegistrationPartStatistics;
  
  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = SchoolDto.PLURAL_NAME;

    listController.setEntityClass(SchoolDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(SchoolService.PATH);

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AbstractIdentifiableNamableDto.JSON_NAME, SchoolDto.JSON_TOTAL_AMOUNT_AS_STRING,
        SchoolDto.JSON_PAID_AMOUNT_AS_STRING, SchoolDto.JSON_PAYABLE_AMOUNT_AS_STRING);
    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getGotoReadPageButton().setRendered(true);
    listController.getGotoReadPageButton().setOutcome(SchoolReadPage.OUTCOME);
    
    amountValueStatistics = new AmountStatisticsDto();
    amountRegistrationPartStatistics = new AmountStatisticsDto();
  }
}

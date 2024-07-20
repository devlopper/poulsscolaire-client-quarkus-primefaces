package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.core.Core;
import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.crud.ListController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.Optional;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.AmountDeadlineClient;
import org.cyk.system.poulsscolaire.server.api.fee.AmountDeadlineDto;
import org.cyk.system.poulsscolaire.server.api.fee.AmountDeadlineService;
import org.cyk.system.poulsscolaire.server.api.fee.AmountDeadlineService.AmountDeadlineCreateRequestDto;
import org.cyk.system.poulsscolaire.server.api.fee.AmountDeadlineService.AmountDeadlineUpdateRequestDto;

/**
 * Cette classe représente le contrôleur de {@link AmountDeadlineDto}.
 *
 * @author Christian
 *
 */
@Dependent
public class AmountDeadlineController extends AbstractController {

  @Inject
  AmountDeadlineClient client;

  @Inject
  @Getter
  ListController listController;

  @Inject
  @Getter
  AmountSelectOneController amountSelectOneController;

  @Inject
  @Getter
  DeadlineSelectOneController deadlineSelectOneController;

  @Inject
  @Getter
  AmountDeadlineFilterController filterController;

  @Override
  protected void postConstruct() {
    super.postConstruct();
    name = AmountDeadlineDto.NAME;
  }

  /**
   * Cette méthode permet d'initialiser le contrôleur.
   */
  public void initialize() {
    listController.setEntityClass(AmountDeadlineDto.class);
    listController.setClient(client);
    listController.setNotificationChannel(AmountDeadlineService.PATH);
    listController.setFilterController(filterController);

    ProjectionDto projection = new ProjectionDto();

    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
        AmountDeadlineDto.JSON_PAYMENT_AS_STRING, AmountDeadlineDto.JSON_DEADLINE_AS_STRING);

    listController.getReadController().setProjection(projection);

    listController.initialize();

    listController.getCreateController().setFunction(entity -> {
      AmountDeadlineCreateRequestDto request = new AmountDeadlineCreateRequestDto();
      request.setAmountIdentifier(
          Core.getOrDefaultIfNull(filterController.getFilter().getAmountIdentifier(),
              ((AmountDeadlineDto) entity).getAmountIdentifier()));
      request.setDeadlineIdentifier(((AmountDeadlineDto) entity).getDeadlineIdentifier());
      request.setPayment(((AmountDeadlineDto) entity).getPayment());
      request.setAuditWho(userIdentifier);
      return client.create(request);
    });

    listController.getUpdateController().setFunction(entity -> {
      AmountDeadlineUpdateRequestDto request = new AmountDeadlineUpdateRequestDto();
      request.setIdentifier(((AmountDeadlineDto) entity).getIdentifier());
      request.setAmountIdentifier(((AmountDeadlineDto) entity).getAmountIdentifier());
      request.setDeadlineIdentifier(((AmountDeadlineDto) entity).getDeadlineIdentifier());
      request.setPayment(((AmountDeadlineDto) entity).getPayment());
      request.setAuditWho(userIdentifier);
      return client.update(request);
    });

    listController.getUpdateController()
        .setProjection(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
            AmountDeadlineDto.JSON_AMOUNT_IDENTIFIER, AmountDeadlineDto.JSON_DEADLINE_IDENTIFIER,
            AmountDeadlineDto.JSON_PAYMENT));

    listController.getUpdateController().addEntityConsumer(entity -> {
      amountSelectOneController.getSelectOneMenu()
          .useValue(((AmountDeadlineDto) entity).getAmountIdentifier());
      deadlineSelectOneController.getSelectOneMenu()
          .useValue(((AmountDeadlineDto) entity).getDeadlineIdentifier());
    });

    amountSelectOneController.initialize();
    amountSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> ((AmountDeadlineDto) listController
            .getCreateControllerOrUpdateControllerEntity()).setAmountIdentifier(identifier));
    amountSelectOneController.getSelectOneMenu()
        .setRendered(Core.isStringBlank(filterController.getFilter().getAmountIdentifier()));

    deadlineSelectOneController.initialize();
    deadlineSelectOneController.getSelectOneMenu()
        .addValueConsumer(identifier -> ((AmountDeadlineDto) listController
            .getCreateControllerOrUpdateControllerEntity()).setDeadlineIdentifier(identifier));
  }
}

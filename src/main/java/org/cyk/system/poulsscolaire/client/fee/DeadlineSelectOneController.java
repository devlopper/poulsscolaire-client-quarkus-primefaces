package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.ActionExecutor;
import ci.gouv.dgbf.extension.primefaces.component.input.SelectOneMenuString;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableNamableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Inject;
import java.util.List;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineClient;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineDto;
import org.cyk.system.poulsscolaire.server.api.fee.DeadlineService;

/**
 * Cette classe représente un sélecteur d'un élément de {@link DeadlineDto}.
 *
 * @author Christian
 *
 */
@Getter
@Dependent
public class DeadlineSelectOneController extends AbstractController {

  @Inject
  DeadlineClient client;

  SelectOneMenuString selectOneMenu;

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    selectOneMenu = new SelectOneMenuString();
    selectOneMenu.outputLabel().setValue(DeadlineDto.NAME);
    List<SelectItem> selectItems = new ActionExecutor<>(this, DeadlineService.GET_MANY_IDENTIFIER,
        () -> client
            .getMany(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
                AbstractIdentifiableCodableNamableDto.JSON_NAME, DeadlineDto.JSON_DATE_AS_STRING),
                null, null, userIdentifier, null)
            .getDatas().stream().map(dto -> new SelectItem(dto.getIdentifier(),
                String.format("%s(%s)", dto.getName(), dto.getDateAsString())))
            .toList()).execute();
    selectOneMenu.addNullChoice().addChoices(selectItems);
  }
}

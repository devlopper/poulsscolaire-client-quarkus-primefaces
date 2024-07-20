package org.cyk.system.poulsscolaire.client.fee;

import ci.gouv.dgbf.extension.primefaces.AbstractController;
import ci.gouv.dgbf.extension.primefaces.ActionExecutor;
import ci.gouv.dgbf.extension.primefaces.component.input.SelectOneMenuString;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import jakarta.enterprise.context.Dependent;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Inject;
import java.util.List;
import lombok.Getter;
import org.cyk.system.poulsscolaire.server.api.fee.AdjustedFeeClient;
import org.cyk.system.poulsscolaire.server.api.fee.AdjustedFeeDto;
import org.cyk.system.poulsscolaire.server.api.fee.AdjustedFeeService;

/**
 * Cette classe représente un sélecteur d'un élément de {@link AdjustedFeeDto}.
 *
 * @author Christian
 *
 */
@Getter
@Dependent
public class AdjustedFeeSelectOneController extends AbstractController {

  @Inject
  AdjustedFeeClient client;

  SelectOneMenuString selectOneMenu;

  /**
   * Cette méthode permet d'initialiser.
   */
  public void initialize() {
    selectOneMenu = new SelectOneMenuString();
    selectOneMenu.outputLabel().setValue(AdjustedFeeDto.NAME);
    List<SelectItem> selectItems =
        new ActionExecutor<>(this, AdjustedFeeService.GET_MANY_IDENTIFIER,
            () -> client
                .getMany(new ProjectionDto().addNames(AbstractIdentifiableDto.JSON_IDENTIFIER,
                    AdjustedFeeDto.JSON_FEE_AS_STRING), null, null, userIdentifier, null)
                .getDatas().stream()
                .map(dto -> new SelectItem(dto.getIdentifier(), dto.getFeeAsString())).toList())
                    .execute();
    selectOneMenu.addNullChoice().addChoices(selectItems);
  }
}

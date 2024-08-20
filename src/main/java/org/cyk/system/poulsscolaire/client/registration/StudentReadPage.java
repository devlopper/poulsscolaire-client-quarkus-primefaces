package org.cyk.system.poulsscolaire.client.registration;

import ci.gouv.dgbf.extension.primefaces.AbstractPage;
import ci.gouv.dgbf.extension.primefaces.ComponentManager;
import ci.gouv.dgbf.extension.primefaces.MessageManager;
import ci.gouv.dgbf.extension.primefaces.ScriptManager;
import ci.gouv.dgbf.extension.primefaces.component.Dialog;
import ci.gouv.dgbf.extension.primefaces.crud.CreateController;
import ci.gouv.dgbf.extension.primefaces.crud.DeleteController;
import ci.gouv.dgbf.extension.primefaces.crud.UpdateController;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableCodableDto;
import ci.gouv.dgbf.extension.server.service.api.entity.AbstractIdentifiableDto;
import ci.gouv.dgbf.extension.server.service.api.request.ProjectionDto;
import ci.gouv.dgbf.extension.server.service.api.response.IdentifiableResponseDto;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityClient;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityDto;
import org.cyk.system.poulsscolaire.server.api.registration.IdentityService.IdentityUpdateRequestDto;
import org.cyk.system.poulsscolaire.server.api.registration.StudentClient;
import org.cyk.system.poulsscolaire.server.api.registration.StudentDto;
import org.cyk.system.poulsscolaire.server.api.registration.StudentService.StudentCreateParentRequestDto;
import org.cyk.system.poulsscolaire.server.api.registration.StudentService.StudentCreateParentRequestDto.ParentalLink;

/**
 * Cette classe représente la page de lecture de {@link StudentDto}.
 *
 * @author Christian
 *
 */
@Named
@ViewScoped
public class StudentReadPage extends AbstractPage {

  @Inject
  StudentClient studentClient;

  @Getter
  StudentDto student;

  @Inject
  IdentityClient identityClient;

  ParentalLink parentalLink;

  @Getter
  @Setter
  Dialog saveParentDialog;

  @Inject
  @Getter
  CreateController createParentController;

  @Inject
  @Getter
  UpdateController updateParentController;

  @Inject
  @Getter
  DeleteController deleteParentController;

  @Getter
  Object saveParentController;

  @Inject
  ScriptManager scriptManager;

  @Inject
  ComponentManager componentManager;

  @Inject
  MessageManager messageManager;

  @Override
  protected void postConstruct() {
    super.postConstruct();

    ProjectionDto projection = new ProjectionDto();
    projection.addNames(AbstractIdentifiableDto.JSON_IDENTIFIER, StudentDto.JSON_SCHOOL_AS_STRING,
        AbstractIdentifiableCodableDto.JSON_CODE, StudentDto.JSON_REGISTRATION_NUMBER,
        StudentDto.JSON_FIRST_NAME, StudentDto.JSON_ARABIC_FIRST_NAME, StudentDto.JSON_LAST_NAMES,
        StudentDto.JSON_ARABIC_LAST_NAMES, StudentDto.JSON_GENDER_AS_STRING,
        StudentDto.JSON_BLOOD_GROUP_AS_STRING, StudentDto.JSON_BIRTH_DATE_AS_STRING,
        StudentDto.JSON_BIRTH_PLACE, StudentDto.JSON_BIRTH_CERTIFICATE_REFERENCE,
        StudentDto.JSON_NATIONALITY, StudentDto.JSON_ORIGIN_SCHOOL, StudentDto.JSON_RESIDENCE,
        StudentDto.JSON_EMAIL_ADDRESS, StudentDto.JSON_PHONE_NUMBER);

    projection.addNames(StudentDto.JSON_FATHER_IDENTIFIER, StudentDto.JSON_FATHER_FIRST_NAME,
        StudentDto.JSON_FATHER_LAST_NAMES, StudentDto.JSON_FATHER_PHONE_NUMBER,
        StudentDto.JSON_MOTHER_IDENTIFIER, StudentDto.JSON_MOTHER_FIRST_NAME,
        StudentDto.JSON_MOTHER_LAST_NAMES, StudentDto.JSON_MOTHER_PHONE_NUMBER,
        StudentDto.JSON_TUTOR_IDENTIFIER, StudentDto.JSON_TUTOR_FIRST_NAME,
        StudentDto.JSON_TUTOR_LAST_NAMES, StudentDto.JSON_TUTOR_PHONE_NUMBER);

    String identifier = getRequestParameterIdentifier();
    student = studentClient.getByIdentifier(identifier, projection, userIdentifier, null);
    contentTitle = StudentDto.NAME + " - " + student.getCode();

    saveParentDialog = new Dialog();
    saveParentDialog.setIdentifier("saveParentDialog");
    saveParentDialog.setWidgetVar("saveParentDialogWidgetVar");
    saveParentDialog.setContentFilePath("/private/student/component/saveParentForm.xhtml");
    saveParentDialog.setHeader("Informations parent");

    createParentController.setName("Création parent");
    createParentController.getNotificationPublishController().setEnabled(false);
    createParentController.addResponseConsumer(response -> {
      scriptManager.hide(saveParentDialog);
      componentManager.update(":form:fatherOutputPanel", ":form:motherOutputPanel",
          ":form:tutorOutputPanel");
      messageManager.addGrowlMessageFromResponse(response);
      componentManager.update(messageManager.getGrowlContainerIdentifier());
    });
    createParentController.setEntityClass(IdentityDto.class);
    createParentController.setFunction(entity -> {
      StudentCreateParentRequestDto request = new StudentCreateParentRequestDto();
      request.setIdentifier(student.getIdentifier());
      request.setFirstName(((IdentityDto) entity).getFirstName());
      request.setLastNames(((IdentityDto) entity).getLastNames());
      request.setPhoneNumber(((IdentityDto) entity).getPhoneNumber());
      request.setLink(parentalLink);
      request.setAuditWho(userIdentifier);
      final IdentifiableResponseDto response = studentClient.createParent(request);
      if (ParentalLink.FATHER.equals(parentalLink)) {
        student.setFatherIdentifier(response.getIdentifier());
        student.setFatherFirstName(request.getFirstName());
        student.setFatherLastNames(request.getLastNames());
        student.setFatherPhoneNumber(request.getPhoneNumber());
      } else if (ParentalLink.MOTHER.equals(parentalLink)) {
        student.setMotherIdentifier(response.getIdentifier());
        student.setMotherFirstName(request.getFirstName());
        student.setMotherLastNames(request.getLastNames());
        student.setMotherPhoneNumber(request.getPhoneNumber());
      } else if (ParentalLink.TUTOR.equals(parentalLink)) {
        student.setTutorIdentifier(response.getIdentifier());
        student.setTutorFirstName(request.getFirstName());
        student.setTutorLastNames(request.getLastNames());
        student.setTutorPhoneNumber(request.getPhoneNumber());
      }
      return response;
    });

    updateParentController.setName("Mise à jour parent");
    updateParentController.getNotificationPublishController().setEnabled(false);
    updateParentController.addResponseConsumer(response -> {
      scriptManager.hide(saveParentDialog);
      componentManager.update(":form:fatherOutputPanel", ":form:motherOutputPanel",
          ":form:tutorOutputPanel");
      messageManager.addGrowlMessageFromResponse(response);
      componentManager.update(messageManager.getGrowlContainerIdentifier());
    });
    updateParentController.setFunction(entity -> {
      IdentityUpdateRequestDto request = new IdentityUpdateRequestDto();
      request.setIdentifier(((IdentityDto) entity).getIdentifier());
      request.setFirstName(((IdentityDto) entity).getFirstName());
      request.setLastNames(((IdentityDto) entity).getLastNames());
      request.setPhoneNumber(((IdentityDto) entity).getPhoneNumber());
      request.setAuditWho(userIdentifier);
      final IdentifiableResponseDto response = identityClient.update(request);
      if (ParentalLink.FATHER.equals(parentalLink)) {
        student.setFatherFirstName(request.getFirstName());
        student.setFatherLastNames(request.getLastNames());
        student.setFatherPhoneNumber(request.getPhoneNumber());
      } else if (ParentalLink.MOTHER.equals(parentalLink)) {
        student.setMotherFirstName(request.getFirstName());
        student.setMotherLastNames(request.getLastNames());
        student.setMotherPhoneNumber(request.getPhoneNumber());
      } else if (ParentalLink.TUTOR.equals(parentalLink)) {
        student.setTutorFirstName(request.getFirstName());
        student.setTutorLastNames(request.getLastNames());
        student.setTutorPhoneNumber(request.getPhoneNumber());
      }
      return response;
    });

    deleteParentController.setName("Suppression parent");
    deleteParentController.setClient(identityClient);
    deleteParentController.getNotificationPublishController().setEnabled(false);
    deleteParentController.addResponseConsumer(response -> {
      messageManager.addGrowlMessageFromResponse(response);
      componentManager.update(messageManager.getGrowlContainerIdentifier());
      if (response.getIdentifier().equals(student.getFatherIdentifier())) {
        student.setFatherIdentifier(null);
      } else if (response.getIdentifier().equals(student.getMotherIdentifier())) {
        student.setMotherIdentifier(null);
      } else if (response.getIdentifier().equals(student.getTutorIdentifier())) {
        student.setTutorIdentifier(null);
      }
    });
  }

  /**
   * Cette méthode permet d'afficher le dialogue de création de parent.
   *
   * @param parentalLinkName {@link ParentalLink}
   */
  public void showCreateParentDialog(String parentalLinkName) {
    parentalLink = ParentalLink.valueOf(parentalLinkName);
    createParentController.instantiateEntity();
    IdentityDto identity = (IdentityDto) createParentController.getEntity();
    switch (parentalLink) {
      case FATHER:
        identity.setFirstName(student.getFatherFirstName());
        identity.setLastNames(student.getFatherLastNames());
        break;
      case MOTHER:
        identity.setFirstName(student.getMotherFirstName());
        identity.setLastNames(student.getMotherLastNames());
        break;
      case TUTOR:
        identity.setFirstName(student.getTutorFirstName());
        identity.setLastNames(student.getTutorLastNames());
        break;
      default:
        break;
    }
    componentManager.update(saveParentDialog);
    scriptManager.show(saveParentDialog);
    saveParentController = createParentController;
  }

  /**
   * Cette méthode permet d'afficher le dialogue de mise à jour de parent.
   *
   * @param parentalLinkName {@link ParentalLink}
   */
  public void showUpdateParentDialog(String parentalLinkName) {
    parentalLink = ParentalLink.valueOf(parentalLinkName);
    IdentityDto identity = null;
    switch (parentalLink) {
      case FATHER:
        identity = new IdentityDto();
        identity.setIdentifier(student.getFatherIdentifier());
        identity.setFirstName(student.getFatherFirstName());
        identity.setLastNames(student.getFatherLastNames());
        updateParentController.setEntity(identity);
        break;
      case MOTHER:
        identity = new IdentityDto();
        identity.setIdentifier(student.getMotherIdentifier());
        identity.setFirstName(student.getMotherFirstName());
        identity.setLastNames(student.getMotherLastNames());
        updateParentController.setEntity(identity);
        break;
      case TUTOR:
        identity = new IdentityDto();
        identity.setIdentifier(student.getTutorIdentifier());
        identity.setFirstName(student.getTutorFirstName());
        identity.setLastNames(student.getTutorLastNames());
        updateParentController.setEntity(identity);
        break;
      default:
        break;
    }
    componentManager.update(saveParentDialog);
    scriptManager.show(saveParentDialog);
    saveParentController = updateParentController;
  }

  public static final String OUTCOME = "studentReadPage";
}

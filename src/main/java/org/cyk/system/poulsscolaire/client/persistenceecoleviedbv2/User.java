package org.cyk.system.poulsscolaire.client.persistenceecoleviedbv2;

import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.PasswordType;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe représente un utilisateur.
 *
 * @author Christian
 *
 */
@Entity
@Table(schema = Constant.SCHEMA_NAME, name = "utilisateur")
@UserDefinition
public class User {
  
  @Id
  @Column(name = "utilisateurid")
  public int identifier;
  
  @Column(name = "utilisateu_login")
  @Username
  public String username;

  @Column(name = "utilisateur_mot_de_passe")
  @Password(PasswordType.CLEAR)
  public String password;
  
  @ManyToMany
  @JoinTable(
      name = "utilisateur_has_personnel",
      joinColumns = @JoinColumn(name = "utilisateur_utilisateurid"),
      inverseJoinColumns = @JoinColumn(name = "profil_profilid")
  )
  @Roles
  public List<Role> roles = new ArrayList<>();
}

package org.cyk.system.poulsscolaire.client.persistenceecoleviedbv2;

import io.quarkus.security.jpa.RolesValue;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;

/**
 * Cette classe représente un utilisateur.
 *
 * @author Christian
 *
 */
@Entity
@Table(schema = Constant.SCHEMA_NAME, name = "profil")
public class Role {
  
  @Id
  @Column(name = "profilid")
  public int identifier;
  
  @Column(name = "profilcode")
  @RolesValue
  public String code;

  @Column(name = "profil_libelle")
  public String name;
  
  @ManyToMany(mappedBy = "roles")
  public List<User> users;
}

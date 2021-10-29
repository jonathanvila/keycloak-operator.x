
package org.keycloak.operator.crds.v1alpha1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;

import java.util.List;

@Builder(toBuilder = true, builderClassName = "Builder")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class KeycloakSpec {

  @Singular(value = "addToExtensions", ignoreNullCollections = true)
  private List<String> extensions;

  private KeycloakSpecExternal external;

  private KeycloakSpecExternalAccess externalAccess;

  private KeycloakSpecExternalDatabase externalDatabase;

  private Number instances;

  private KeycloakSpecKeycloakDeploymentSpec keycloakDeploymentSpec;

  private KeycloakSpecMigration migration;

  private KeycloakSpecMultiAvailablityZones multiAvailablityZones;

  private KeycloakSpecPodDisruptionBudget podDisruptionBudget;

  private KeycloakSpecPostgresDeploymentSpec postgresDeploymentSpec;

  private String profile;

  private String storageClassName;

  private Boolean unmanaged;

}

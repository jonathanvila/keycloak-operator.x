
package org.keycloak.operator.crds.v1alpha1;

import io.fabric8.kubernetes.api.model.Affinity;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.Volume;
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
public class KeycloakSpecKeycloakDeploymentSpecExperimental {

  private Affinity affinity;

  @Singular(value = "addToArgs", ignoreNullCollections = true)
  private List<String> args;

  @Singular(value = "addToCommand", ignoreNullCollections = true)
  private List<String> command;

  @Singular(value = "addToEnv", ignoreNullCollections = true)
  //private List<KeycloakSpecKeycloakDeploymentSpecExperimentalEnv> env;
  private List<EnvVar> env;

  private String serviceAccountName;

  private KeycloakSpecKeycloakDeploymentSpecExperimentalVolumes volumes;

}

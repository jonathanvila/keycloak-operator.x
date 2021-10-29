
package org.keycloak.operator.crds.v1alpha1;

import io.fabric8.kubernetes.api.model.ResourceRequirements;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder(toBuilder = true, builderClassName = "Builder")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class KeycloakSpecKeycloakDeploymentSpec {

  private KeycloakSpecKeycloakDeploymentSpecExperimental experimental;

  private ResourceRequirements resources;

}

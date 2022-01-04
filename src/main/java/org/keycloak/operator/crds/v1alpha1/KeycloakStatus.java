
package org.keycloak.operator.crds.v1alpha1;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Singular;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Builder(toBuilder = true, builderClassName = "Builder")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class KeycloakStatus implements KubernetesResource {

  @NonNull

  private String credentialSecret;

  private String externalURL;

  @NonNull

  private String internalURL;

  @NonNull

  private String message;

  @NonNull

  private String phase;

  @NonNull

  private Boolean ready;

  @Singular(value = "putInSecondaryResources", ignoreNullCollections = true)
  private Map<String, List<String>> secondaryResources;

  @NonNull

  private String version;

}

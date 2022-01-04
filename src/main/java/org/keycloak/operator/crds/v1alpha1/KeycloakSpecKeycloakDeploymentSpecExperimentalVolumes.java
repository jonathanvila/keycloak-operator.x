
package org.keycloak.operator.crds.v1alpha1;

import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.api.model.ListMeta;
import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.client.CustomResourceList;
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
public class KeycloakSpecKeycloakDeploymentSpecExperimentalVolumes {

  private Number defaultMode;
  private List<Volume> items;

}

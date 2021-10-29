
package org.keycloak.operator.crds.v1alpha1;

import io.fabric8.kubernetes.api.model.Volume;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;
import org.keycloak.operator.crds.ListModel;

import java.util.List;

@Builder(toBuilder = true, builderClassName = "Builder")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class KeycloakSpecKeycloakDeploymentSpecExperimentalVolumes implements ListModel<Volume> {

  private Number defaultMode;

  @Singular(value = "addToItems", ignoreNullCollections = true)
  private List<Volume> items;

}

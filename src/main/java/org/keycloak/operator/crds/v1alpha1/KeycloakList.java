
package org.keycloak.operator.crds.v1alpha1;

import io.fabric8.kubernetes.api.model.ListMeta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Singular;
import lombok.ToString;
import org.keycloak.operator.crds.ListModel;

import java.util.List;

@Builder(toBuilder = true, builderClassName = "Builder")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class KeycloakList implements ListModel<Keycloak> {

  private String apiVersion;

  @NonNull

  @Singular(value = "addToItems", ignoreNullCollections = true)
  private List<Keycloak> items;

  private String kind;

  private ListMeta metadata;

}

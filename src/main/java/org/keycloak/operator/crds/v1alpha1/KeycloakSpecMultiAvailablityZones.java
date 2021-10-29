
package org.keycloak.operator.crds.v1alpha1;

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
public class KeycloakSpecMultiAvailablityZones {

  private Boolean enabled;

}


package org.keycloak.operator.crds.v1alpha1;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;
import org.keycloak.operator.Constants;

@Group(Constants.CRDS_GROUP)
@Version(Constants.CRDS_VERSION)
public class Keycloak extends CustomResource<KeycloakSpec, KeycloakStatus> implements Namespaced {
}

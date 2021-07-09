/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.operator.k8sobjects;

import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.apps.StatefulSetBuilder;
import org.keycloak.operator.ConfigManager;
import org.keycloak.operator.Constants;
import org.keycloak.operator.crds.Keycloak;

/**
 * @author Vaclav Muzikar <vmuzikar@redhat.com>
 */
public class KeycloakDeployment {
    public static StatefulSet getNew(Keycloak keycloak) {
        StatefulSet ss = new StatefulSetBuilder()
                .withNewMetadata()
                    .withName(Constants.APPLICATION)
                    .withNamespace(keycloak.getMetadata().getNamespace())
                .endMetadata()
                .build();

        return updateExisting(ss, keycloak);
    }

    public static StatefulSet updateExisting(StatefulSet ss, Keycloak keycloak) {
        return new StatefulSetBuilder(ss)
                .editOrNewMetadata()
                    .addToLabels(Constants.MANAGED_BY_LABEL, ConfigManager.getOperatorName())
                    .removeMatchingFromOwnerReferences(o -> true)
                    .addNewOwnerReference()
                        .withApiVersion(Constants.CRDS_VERSION)
                        .withKind(Keycloak.class.getName())
                        .withName(keycloak.getMetadata().getName())
                        .withUid(keycloak.getMetadata().getUid())
                    .endOwnerReference()
                .endMetadata()

                .withNewSpec()
                    .withReplicas(1)
                    .withNewSelector()
                        .addToMatchLabels(Constants.DEFAULT_LABELS)
                    .endSelector()
                    .withNewTemplate()

                        .withNewMetadata()
                            .withName(Constants.APPLICATION)
                            .withNamespace(keycloak.getMetadata().getNamespace())
                            .addToLabels(Constants.DEFAULT_LABELS)
                            .addToLabels(Constants.MANAGED_BY_LABEL, ConfigManager.getOperatorName())
                        .endMetadata()

                        .withNewSpec()

                            .addNewInitContainer()
                                .withName("init-container")
                                .withImage(ConfigManager.getKeycloakInitImage(keycloak))
                            .endInitContainer()

                            .addNewContainer()
                                .withName(Constants.APPLICATION)
                                .withImage(ConfigManager.getKeycloakImage(keycloak))
                                .addNewPort()
                                    .withContainerPort(8443)
                                    .withProtocol("TCP")
                                .endPort()
                            .endContainer()

                        .endSpec()

                    .endTemplate()
                .endSpec()
                .build();
    }
}

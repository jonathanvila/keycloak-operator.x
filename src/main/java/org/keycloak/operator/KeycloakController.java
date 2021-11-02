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

package org.keycloak.operator;

import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.Controller;
import io.javaoperatorsdk.operator.api.DeleteControl;
import io.javaoperatorsdk.operator.api.ResourceController;
import io.javaoperatorsdk.operator.api.UpdateControl;
import io.javaoperatorsdk.operator.processing.event.EventSourceManager;
import io.javaoperatorsdk.operator.processing.event.internal.CustomResourceEvent;
import lombok.extern.java.Log;
import org.keycloak.operator.crds.v1alpha1.Keycloak;
import org.keycloak.operator.crds.v1alpha1.KeycloakStatus;
import org.keycloak.operator.events.KeycloakDeploymentEvent;
import org.keycloak.operator.events.KeycloakDeploymentEventSource;
import org.keycloak.operator.k8sobjects.KeycloakDeployment;

import javax.inject.Inject;

import java.util.Objects;

@Controller(namespaces = Controller.WATCH_CURRENT_NAMESPACE)
@Log
public class KeycloakController implements ResourceController<Keycloak> {
    private final KeycloakDeploymentEventSource keycloakDeploymentEventSource;

    @Inject
    KubernetesClient client;

    public KeycloakController(KeycloakDeploymentEventSource keycloakDeploymentEventSource, KubernetesClient client) {
        this.keycloakDeploymentEventSource = keycloakDeploymentEventSource;
        this.client = client;
    }

    @Override
    public void init(EventSourceManager eventSourceManager) {
        keycloakDeploymentEventSource.registerWatch();
        eventSourceManager.registerEventSource("keycloak-deployment", keycloakDeploymentEventSource);
    }

    @Override
    public UpdateControl<Keycloak> createOrUpdateResource(Keycloak keycloak, Context<Keycloak> context) {
        log.info("--- Starting createOrUpdateResource");
        UpdateControl<Keycloak> ret = UpdateControl.noUpdate();

        if (context.getEvents().getLatestOfType(CustomResourceEvent.class).isPresent()) {
            log.info("CR change detected; updating KC deployment");
            reconcileKeycloakDeployment(keycloak);
        }

        if (context.getEvents().getLatestOfType(KeycloakDeploymentEvent.class).isPresent()) {
            log.info("KC deployment change detected; updating CR status");
            reconcileCRStatus(keycloak);
            ret = UpdateControl.updateStatusSubResource(keycloak);
        }

        log.info("--- createOrUpdateResource finished");
        return ret;
    }

    @Override
    public DeleteControl deleteResource(Keycloak resource, Context<Keycloak> context) {
        return DeleteControl.DEFAULT_DELETE;
    }

    private void reconcileKeycloakDeployment(Keycloak keycloak) {
        StatefulSet ss = getKeycloakDeployment(keycloak);

        if (ss == null) {
            ss = KeycloakDeployment.getNew(keycloak);
        }
        else {
            ss = KeycloakDeployment.updateExisting(ss, keycloak);
        }
        client.apps().statefulSets().createOrReplace(ss);
    }

    private void reconcileCRStatus(Keycloak keycloak) {
        StatefulSet ss = getKeycloakDeployment(keycloak);

        CRStatus crStatus;
        if (ss.getStatus().getReadyReplicas().equals(1)) {
            crStatus = CRStatus.RECONCILING;
        }
        else {
            crStatus = CRStatus.INITIALIZING;
        }

        KeycloakStatus status = Objects.requireNonNullElse(keycloak.getStatus(), new KeycloakStatus());
        status.setPhase(crStatus.getPhase());
        status.setReady(crStatus.isReady());
        keycloak.setStatus(status);
    }

    private StatefulSet getKeycloakDeployment(Keycloak keycloak) {
        return client
                    .apps()
                    .statefulSets()
                    .inNamespace(keycloak.getMetadata().getNamespace())
                    .withName(Constants.APPLICATION)
                    .get();
    }
}

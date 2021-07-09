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

package org.keycloak.operator.events;

import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import io.javaoperatorsdk.operator.processing.event.AbstractEventSource;
import org.jboss.logging.Logger;
import org.keycloak.operator.Constants;
import org.keycloak.operator.ConfigManager;

import javax.enterprise.context.Dependent;

/**
 * @author Vaclav Muzikar <vmuzikar@redhat.com>
 */
@Dependent
public class KeycloakDeploymentEventSource extends AbstractEventSource implements Watcher<StatefulSet> {
    private final KubernetesClient client;
    private final Logger logger;

    public KeycloakDeploymentEventSource(KubernetesClient client, Logger logger) {
        this.client = client;
        this.logger = logger;
    }

    public void registerWatch() {
        client
            .apps()
            .statefulSets()
            .inAnyNamespace() // TODO Is this safe given the controller should not set the label to any CR in unwatched namespaces?
            .withField("metadata.name", Constants.APPLICATION)
            .withLabel(Constants.MANAGED_BY_LABEL, ConfigManager.getOperatorName())
            .watch(this);
    }

    @Override
    public void eventReceived(Action action, StatefulSet resource) {
        logger.infof("Event received; Action: %s, Ready Replicas: %s", action.name(), resource.getStatus().getReadyReplicas());
        eventHandler.handleEvent(new KeycloakDeploymentEvent(action, resource, this));
    }

    @Override
    public void onClose(WatcherException e) {
        if (e == null) {
            return;
        }
        if (e.isHttpGone()) {
            logger.warn("Received error for watch, will try to reconnect.", e);
            registerWatch();
        } else {
            // Note that this should not happen normally, since fabric8 client handles reconnect.
            // In case it tries to reconnect this method is not called.
            logger.error("Unexpected error happened with watch. Will exit.", e);
            System.exit(1);
        }
    }
}

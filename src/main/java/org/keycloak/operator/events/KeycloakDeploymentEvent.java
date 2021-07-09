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
import io.fabric8.kubernetes.client.Watcher;
import io.javaoperatorsdk.operator.processing.event.AbstractEvent;

/**
 * @author Vaclav Muzikar <vmuzikar@redhat.com>
 */
public class KeycloakDeploymentEvent extends AbstractEvent {
    private final Watcher.Action action;
    private final StatefulSet resource;

    public KeycloakDeploymentEvent(Watcher.Action action, StatefulSet resource, KeycloakDeploymentEventSource eventSource) {
        super(resource.getMetadata().getOwnerReferences().get(0).getUid(), eventSource);
        this.action = action;
        this.resource = resource;
    }

    public Watcher.Action getAction() {
        return action;
    }

    public StatefulSet getResource() {
        return resource;
    }
}

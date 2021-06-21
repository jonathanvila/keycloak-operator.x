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

import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.Controller;
import io.javaoperatorsdk.operator.api.DeleteControl;
import io.javaoperatorsdk.operator.api.ResourceController;
import io.javaoperatorsdk.operator.api.UpdateControl;
import io.javaoperatorsdk.operator.processing.event.EventSourceManager;
import org.keycloak.operator.crds.Keycloak;

/**
 * @author Vaclav Muzikar <vmuzikar@redhat.com>
 */
@Controller
public class KeycloakController implements ResourceController<Keycloak> {
    @Override
    public void init(EventSourceManager eventSourceManager) {
    }

    @Override
    public UpdateControl<Keycloak> createOrUpdateResource(Keycloak keycloak, Context<Keycloak> context) {
        return null;
    }

    @Override
    public DeleteControl deleteResource(Keycloak resource, Context<Keycloak> context) {
        return DeleteControl.DEFAULT_DELETE;
    }
}

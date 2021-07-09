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

import org.keycloak.operator.crds.Keycloak;

/**
 * @author Vaclav Muzikar <vmuzikar@redhat.com>
 */
public final class ConfigManager {
    private static String operatorName = System.getProperty("OPERATOR_NAME", "keycloak-operator");

    private static String[] watchedNamespaces;
    static {
        String ns = System.getProperty("WATCH_NAMESPACE");
        if (ns != null) {
            watchedNamespaces = ns.split(",");
        }
    }

    public static String getOperatorName() {
        return operatorName;
    }

    // needs to be static for use in annotations
    public static String[] getWatchedNamespaces() {
        return watchedNamespaces;
    }

    public static boolean isProduct(Keycloak keycloak) {
        return false; // TODO
    }

    public static String getKeycloakImage(Keycloak keycloak) {
        return Constants.KEYCLOAK_IMAGE; // TODO RH-SSO support
    }

    public static String getKeycloakInitImage(Keycloak keycloak) {
        return Constants.KEYCLOAK_INIT_IMAGE; // TODO RH-SSO support
    }
}

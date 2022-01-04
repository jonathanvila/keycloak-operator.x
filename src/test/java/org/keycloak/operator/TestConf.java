package org.keycloak.operator;

import io.fabric8.kubernetes.client.NamespacedKubernetesClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesMixedDispatcher;
import io.fabric8.kubernetes.client.server.mock.KubernetesMockServer;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import io.fabric8.mockwebserver.Context;
import io.fabric8.mockwebserver.ServerRequest;
import io.fabric8.mockwebserver.ServerResponse;
import io.quarkus.arc.profile.IfBuildProfile;
import lombok.extern.java.Log;
import okhttp3.mockwebserver.MockWebServer;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

@Log
public class TestConf {
    @Produces
    @Singleton
    @IfBuildProfile("test")
    NamespacedKubernetesClient makeDefaultClient(KubernetesMockServer server) {
        NamespacedKubernetesClient client = server.createClient();
        log.info("Creating K8s Test Client instance :" + client);
        return client;
    }

    @Produces
    @Singleton
    @IfBuildProfile("test")
    KubernetesMockServer makeKubernetesServer() {
        KubernetesMockServer kubernetesMockServer = new KubernetesMockServer(new Context(), new MockWebServer(), new HashMap<>(), new KubernetesMixedDispatcher(new HashMap<>(), Collections.emptyList()), true);
        log.info("Creating K8sServer :" + kubernetesMockServer);
        return kubernetesMockServer;
    }
}

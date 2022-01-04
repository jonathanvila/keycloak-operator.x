package org.keycloak.operator;

import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
@QuarkusTestResource(K8sClusterTestResource.class)
public class OperatorE2ETest {
    DefaultKubernetesClient k8sclient;

    @Test
    public void maintest() {
        List<Node> nodes = k8sclient.nodes().list().getItems();
        Assertions.assertThat(nodes).hasSize(1);
    }
}

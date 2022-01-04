package org.keycloak.operator;

import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.javaoperatorsdk.operator.Operator;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.java.Log;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.operator.crds.v1alpha1.Keycloak;

import javax.inject.Inject;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@Log
public class OperatorTest {

    @Inject
    KubernetesClient client;

    @Inject
    Operator operator;

    @BeforeEach
    public void setUp() {
        client.namespaces().create(new NamespaceBuilder().withNewMetadata().withName("kc-test").endMetadata().build());
        client.apiextensions().v1().customResourceDefinitions().create(CustomResourceDefinitionContext.v1CRDFromCustomResourceType(Keycloak.class).build());

        operator.start();
    }

    @AfterEach
    public void cleanUp() {
        operator.close();
        client.namespaces().withName("kc-ns").delete();
    }

    /*
    Tests to cover :
    1. on the CR creation the operands are correctly created
    2. on a CR creation with wrong values the CR is not created
    3. on a CR modification the operands also reflect the modification
    4. on scaling up
     */
    @Test
    public void given_CRCreated_when_checkOperands_then_elementsArePresent() {
        // create CR
        Keycloak keycloak = new Keycloak();
        keycloak.setMetadata(new ObjectMeta());
        keycloak.getMetadata().setName("keycloak-test");
        client.resources(Keycloak.class).inNamespace("test").create(keycloak);

        // test
        assertThat(client.resources(Keycloak.class).inNamespace("test").list().getItems()).hasSize(1);
        Awaitility.await().atMost(Duration.ofMillis(100))
                        .untilAsserted( () -> assertThat(client.resources(StatefulSet.class).inNamespace("test").list().getItems()).hasSize(1));
    }
}

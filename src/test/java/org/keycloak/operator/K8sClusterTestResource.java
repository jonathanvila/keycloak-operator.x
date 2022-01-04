package org.keycloak.operator;

import com.dajudge.kindcontainer.KindContainer;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import lombok.extern.java.Log;
import org.testcontainers.containers.output.ToStringConsumer;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;
import java.util.Map;

@Log
public class K8sClusterTestResource implements QuarkusTestResourceLifecycleManager {
    private K3sContainer k3sContainer;
    private ToStringConsumer logger;
    public KindContainer kindContainer;
    public static boolean k3s = true;

    public Map<String, String> startK3s() {
        try {
            k3sContainer.start();
            Thread.sleep(10000);
            return null;
        } catch (Exception ex) {
            System.out.println(logger.toUtf8String());
            return null;
        }
    }

    public Map<String, String> startKind() {
        kindContainer.start();
        return null;
    }

    @Override
    public Map<String, String> start() {
        return (k3s) ? startK3s() : startKind();
    }

    @Override
    public void stop() {
        if (k3s) {
            kindContainer.stop();
        } else k3sContainer.stop();
    }

    @Override
    public void inject(TestInjector testInjector) {
        if (!k3s) {
            testInjector.injectIntoFields(kindContainer.client(), (f) -> f.getType().isAssignableFrom(DefaultKubernetesClient.class));
        } else {
            Config config = Config.fromKubeconfig(k3sContainer.getKubeConfigYaml());
            // workaround for undiagnosed issue; fabric8 seems to not identify
            // the client key algorithm correctly, so fails to work with K3s
            // ECDSA keys unless configured explicitly
            config.setClientKeyAlgo("EC");

            DefaultKubernetesClient k8sclient = new DefaultKubernetesClient(config);
            testInjector.injectIntoFields(k8sclient, (f) -> f.getType().isAssignableFrom(DefaultKubernetesClient.class));
        }
    }

    @Override
    public void init(Map<String, String> initArgs) {
        QuarkusTestResourceLifecycleManager.super.init(initArgs);
        if (k3s) {
            logger = new ToStringConsumer();
            k3sContainer = new K3sContainer(DockerImageName.parse("rancher/k3s")).withLogConsumer(new CustomLogConsumer(log).withPrefix("KIND"));
        } else {
            kindContainer = new KindContainer().withLogConsumer(new CustomLogConsumer(log).withPrefix("KIND"));
        }
    }
}

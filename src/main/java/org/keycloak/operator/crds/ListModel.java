package org.keycloak.operator.crds;

import java.io.Serializable;

import java.util.List;

/**
 * {@link Model} interface extension to be implemented by any class that represents a "listable"
 * Kubernetes resource.
 */
public interface ListModel<T> extends Serializable {

  List<T> getItems();
}
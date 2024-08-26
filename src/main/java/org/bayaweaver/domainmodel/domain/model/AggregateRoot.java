package org.bayaweaver.domainmodel.domain.model;

public interface AggregateRoot<T extends DefinedIdentifier> {

    T id();
}

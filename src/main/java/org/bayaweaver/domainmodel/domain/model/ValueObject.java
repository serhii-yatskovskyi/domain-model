package org.bayaweaver.domainmodel.domain.model;

public abstract class ValueObject {

    protected ValueObject() {}

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object o);
}

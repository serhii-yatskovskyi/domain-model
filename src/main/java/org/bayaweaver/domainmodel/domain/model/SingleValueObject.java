package org.bayaweaver.domainmodel.domain.model;

public abstract class SingleValueObject<T> extends NullableValueObject<T> {

    protected SingleValueObject(T value) {
        super(value);
        if (value == null) {
            throw new IllegalArgumentException("Initial value cannot be null");
        }
    }
}

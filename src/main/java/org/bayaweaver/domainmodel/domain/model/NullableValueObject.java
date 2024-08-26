package org.bayaweaver.domainmodel.domain.model;

import java.util.Objects;

public abstract class NullableValueObject<T> extends ValueObject {
    private final T value;

    protected NullableValueObject(T value) {
        this.value = value;
    }

    public T value() {
        return value;
    }

    @Override
    public int hashCode() {
        return value == null ? 0 : value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NullableValueObject<?> that = (NullableValueObject<?>) o;
        return Objects.equals(value, that.value);
    }
}

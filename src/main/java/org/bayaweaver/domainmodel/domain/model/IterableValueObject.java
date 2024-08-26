package org.bayaweaver.domainmodel.domain.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public abstract class IterableValueObject<E> extends ValueObject implements Iterable<E> {
    private final Iterable<E> values;
    private int count = -1;

    protected IterableValueObject(E[] values) {
        this(Arrays.asList(values));
    }

    protected IterableValueObject(Iterable<E> values) {
        if (values == null) {
            throw new IllegalArgumentException("Initial values cannot be null");
        }
        this.values = values;
        if (values instanceof Collection<?>) {
            this.count = ((Collection<?>) values).size();
        }
    }

    @Override
    public Iterator<E> iterator() {
        return values.iterator();
    }

    protected int count() {
        if (count == -1) {
            for (E ignored : this) {
                count++;
            }
        }
        return count;
    }

    protected boolean contains(E value) {
        for (E e : this) {
            if (e.equals(value)) {
                return true;
            }
        }
        return false;
    }

    Iterable<E> values() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IterableValueObject<?> that = (IterableValueObject<?>) o;
        return values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return count();
    }
}

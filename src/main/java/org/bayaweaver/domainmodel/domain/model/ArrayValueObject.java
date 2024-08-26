package org.bayaweaver.domainmodel.domain.model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ArrayValueObject<T> extends ValueObject implements Iterable<T> {
    private final T[] values;

    protected ArrayValueObject(T[] values) {
        if (values == null) {
            throw new IllegalArgumentException("Initial value cannot be null");
        }
        T[] arr = (T[]) new Object[values.length];
        System.arraycopy(values, 0, arr, 0, values.length);
        this.values = arr;
    }

    @Override
    public Iterator<T> iterator() {
        return Arrays.asList(values).iterator();
    }

    protected int count() {
        return values.length;
    }

    protected boolean contains(T value) {
        for (T e : this) {
            if (e.equals(value)) {
                return true;
            }
        }
        return false;
    }

    protected T value(int n) {
        try {
            return values[n];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ArrayValueObject<?> that = (ArrayValueObject<?>) o;
        return Arrays.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return values.length;
    }
}

package org.bayaweaver.domainmodel.domain.model;

import java.util.Arrays;
import java.util.List;

public abstract class ListValueObject<E> extends IterableValueObject<E> {

    protected ListValueObject(E[] values) {
        this(Arrays.asList(values));
    }

    protected ListValueObject(List<E> values) {
        super(values);
    }

    protected E get(int n) {
        Iterable<E> values = values();
        if(values instanceof List) {
            return ((List<E>) values).get(n);
        }
        int i = 0;
        for (E e : values()) {
            if (i == n) {
                return e;
            }
            i++;
        }
        throw new IndexOutOfBoundsException();
    }
}

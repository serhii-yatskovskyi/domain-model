package org.bayaweaver.domainmodel.util;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Iterables {

    public static <T> Stream<T> stream(Iterable<T> i) {
        if (i == null) {
            return null;
        }
        return StreamSupport.stream(i.spliterator(), false);
    }
}

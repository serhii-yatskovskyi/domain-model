package org.bayaweaver.domainmodel.infrastructure;

import org.bayaweaver.domainmodel.domain.model.Identifier;

import java.lang.reflect.Field;

public abstract class IdentifierSerializer {

    public static String asString(Identifier id) {
        return id.toString();
    }

    public static Long asNumber(Identifier id) {
        try {
            Field stringField = Identifier.class.getDeclaredField("numberValue");
            stringField.setAccessible(true);
            return (Long) stringField.get(id);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}

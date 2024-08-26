package org.bayaweaver.domainmodel.domain.model;

import java.util.Objects;

public abstract class Identifier extends ValueObject {
    private String stringValue;
    private final Long numberValue;

    public Identifier(String value) {
        this.stringValue = value;
        this.numberValue = null;
    }

    public Identifier(Long value) {
        this.stringValue = null;
        this.numberValue = value;
    }

    public Identifier(Identifier id) {
        if (id != null) {
            this.stringValue = id.stringValue;
            this.numberValue = id.numberValue;
        } else {
            this.stringValue = null;
            this.numberValue = null;
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
        Identifier that = (Identifier) o;
        return (numberValue != null)
                ? numberValue.equals(that.numberValue)
                : Objects.equals(stringValue, that.stringValue);
    }

    @Override
    public int hashCode() {
        return (numberValue != null) ? numberValue.hashCode() : stringValue.hashCode();
    }

    @Override
    public String toString() {
        if (stringValue == null) {
            stringValue = String.valueOf(numberValue);
        }
        return stringValue;
    }
}

package org.bayaweaver.domainmodel.domain.model;

public abstract class DefinedIdentifier extends Identifier {

    public DefinedIdentifier(String value) {
        super(value);
        if (value == null) {
            throw new IllegalArgumentException("An Identifier value cannot be null");
        }
    }

    public DefinedIdentifier(long value) {
        super(value);
    }

    public DefinedIdentifier(Identifier id) {
        super(id);
        if (id == null) {
            throw new IllegalArgumentException("An Identifier value cannot be null");
        }
    }
}

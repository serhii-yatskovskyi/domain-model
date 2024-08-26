package org.bayaweaver.domainmodel.domain.model;

import org.bayaweaver.domainmodel.util.JavaClassNameUtil;

public class AggregateRootNotFoundException extends DomainException {

    public <T extends DefinedIdentifier> AggregateRootNotFoundException(
            Class<? extends AggregateRoot<T>> aggrClass, T id) {

        super(JavaClassNameUtil.toHumanReadableName(aggrClass) + " '" + id + "' was not found.");
    }

    public <T extends DefinedIdentifier> AggregateRootNotFoundException(
            Class<? extends AggregateRoot<T>> aggrClass) {

        super(JavaClassNameUtil.toHumanReadableName(aggrClass) + " was not found.");
    }
}

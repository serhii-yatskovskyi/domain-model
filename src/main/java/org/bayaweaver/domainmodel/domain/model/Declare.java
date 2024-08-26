package org.bayaweaver.domainmodel.domain.model;

public abstract class Declare {

    private Declare() {}

    public static DomainRuleSpecification that(String specification) {
        return new DomainRuleSpecification(specification);
    }

    public static DomainRuleSpecification that(Enum<?> specificationCode, String specification) {
        return new DomainRuleSpecification(specificationCode + " " + specification);
    }
}

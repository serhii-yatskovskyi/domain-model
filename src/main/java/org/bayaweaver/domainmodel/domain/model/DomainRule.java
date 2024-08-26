package org.bayaweaver.domainmodel.domain.model;

public final class DomainRule {
    private final DomainRuleSpecification specification;
    private final DomainRuleCriteria criteria;

    DomainRule(DomainRuleSpecification specification, DomainRuleCriteria criteria) {
        this.specification = specification;
        this.criteria = criteria;
    }

    public void met() throws DomainRuleViolatedException {
        boolean fulfilled;
        try {
            fulfilled = criteria.fulfilled();
        } catch (Exception e) {
            fulfilled = false;
        }
        specification.test(fulfilled);
    }
}

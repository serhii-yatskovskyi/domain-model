package org.bayaweaver.domainmodel.domain.model;

public final class DomainRuleSpecification {
    private final String value;

    DomainRuleSpecification(String value) {
        this.value = value;
    }

    public void test(DomainRuleCriteria oneCriteria, DomainRuleCriteria... otherCriteria)
            throws DomainRuleViolatedException {

        boolean fulfilled;
        try {
            fulfilled = oneCriteria.fulfilled();
            if (fulfilled) {
                for (DomainRuleCriteria criteria : otherCriteria) {
                    if (!criteria.fulfilled()) {
                        fulfilled = false;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            fulfilled = false;
        }
        test(fulfilled);
    }

    public void test(boolean result) throws DomainRuleViolatedException {
        if (!result) {
            throw new DomainRuleViolatedException(value);
        }
    }

    public void violate() throws DomainRuleViolatedException {
        test(false);
    }

    public DomainRule criteria(DomainRuleCriteria criteria) {
        return new DomainRule(this, criteria);
    }
}

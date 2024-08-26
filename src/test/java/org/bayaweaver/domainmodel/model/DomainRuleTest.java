package org.bayaweaver.domainmodel.model;

import org.bayaweaver.domainmodel.domain.model.Declare;
import org.bayaweaver.domainmodel.domain.model.DomainRule;
import org.bayaweaver.domainmodel.domain.model.DomainRuleCriteria;
import org.bayaweaver.domainmodel.domain.model.DomainRuleSpecification;
import org.bayaweaver.domainmodel.domain.model.DomainRuleViolatedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DomainRuleTest {

    @Test
    public void test() {
        final int i = 0;
        DomainRuleSpecification spec = Declare.that("'i' must be equal to 0.");
        Assertions.assertDoesNotThrow(() -> spec.test(i == 0));
    }

    @Test
    public void met() {
        final int i = 0;
        DomainRuleSpecification spec = Declare.that("'i' must be equal to 0.");
        DomainRule rule = spec.criteria(() -> i == 0);
        Assertions.assertDoesNotThrow(rule::met);
    }

    @Test
    public void notMet() {
        final int i = 0;
        DomainRuleSpecification spec = Declare.that("'i' must be greater than 0.");
        DomainRule rule = spec.criteria(() -> i > 0);
        Assertions.assertThrows(DomainRuleViolatedException.class, rule::met);
    }

    @Test
    public void noSurroundingInTryCatch() {
        final Num i = new Num(0);
        DomainRuleSpecification spec = Declare.that("'i' must be equal to 0.");
        DomainRule rule = spec.criteria(() -> i.value() == 0);
        Assertions.assertDoesNotThrow(rule::met);
    }

    @Test
    public void throwsException() {
        final Num i = new Num(null);
        DomainRuleSpecification spec = Declare.that("'i' must throw exception.");
        DomainRule rule = spec.criteria(DomainRuleCriteria.throwsException(i::value, Exception.class));
        Assertions.assertDoesNotThrow(rule::met);
        DomainRule rule2 = spec.criteria(DomainRuleCriteria.throwsException(i::value, NullPointerException.class));
        Assertions.assertThrows(DomainRuleViolatedException.class, rule2::met);
    }

    @Test
    public void noException() {
        final Num i = new Num(null);
        DomainRuleSpecification spec = Declare.that("'i' must not throw exception.");
        DomainRule rule3 = spec.criteria(DomainRuleCriteria.noException(i::value, Exception.class));
        Assertions.assertThrows(DomainRuleViolatedException.class, rule3::met);
        DomainRule rule4 = spec.criteria(DomainRuleCriteria.noException(i::value, NullPointerException.class));
        Assertions.assertDoesNotThrow(rule4::met);
    }

    private static class Num {
        private final Integer value;

        private Num(Integer value) {
            this.value = value;
        }

        public int value() throws Exception {
            try {
                return value;
            } catch (NullPointerException e) {
                throw new Exception();
            }
        }
    }
}

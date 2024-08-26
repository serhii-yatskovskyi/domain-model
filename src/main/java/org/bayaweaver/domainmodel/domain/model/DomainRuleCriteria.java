package org.bayaweaver.domainmodel.domain.model;

public interface DomainRuleCriteria {

    boolean fulfilled() throws Exception;

    @SafeVarargs
    static DomainRuleCriteria throwsException(Executable executable, Class<? extends Exception>... expectedExceptions) {
        return () -> {
            try {
                executable.execute();
            } catch (Exception e) {
                if (expectedExceptions.length == 0) {
                    return true;
                }
                for (Class<? extends Exception> expectedException : expectedExceptions) {
                    if (e.getClass() == expectedException) {
                        return true;
                    }
                }
            }
            return false;
        };
    }

    @SafeVarargs
    static DomainRuleCriteria noException(
            Executable executable,
            Class<? extends Exception>... nonExpectedExceptions) {

        return () -> {
            try {
                executable.execute();
            } catch (Exception e) {
                if (nonExpectedExceptions.length == 0) {
                    return false;
                }
                for (Class<? extends Exception> nonExpectedException : nonExpectedExceptions) {
                    if (e.getClass() == nonExpectedException) {
                        return false;
                    }
                }
            }
            return true;
        };
    }

    @SafeVarargs
    static <T> DomainRuleCriteria noException(
            Iterable<T> elements,
            ExecutableOnElement<T> executable,
            Class<? extends Exception>... nonExpectedExceptions) {

        return () -> {
            if (nonExpectedExceptions.length == 0) {
                return false;
            }
            for (T element : elements) {
                DomainRuleCriteria criteria = noException(() -> {
                    executable.executeOn(element);
                });
                if (!criteria.fulfilled()) {
                    return false;
                }
            }
            return true;
        };
    }

    interface Executable {
        void execute() throws Exception;
    }

    interface ExecutableOnElement<T> {
        void executeOn(T element) throws Exception;
    }
}

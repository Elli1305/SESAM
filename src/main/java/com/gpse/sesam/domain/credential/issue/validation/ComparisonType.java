package com.gpse.sesam.domain.credential.issue.validation;

public enum ComparisonType {

    EQUAL {
        @Override
        public <T> boolean validate(Comparable<T> value1, T value2) {
            return value1.compareTo(value2) == 0;
        }
    },
    NOT_EQUAL {
        @Override
        public <T> boolean validate(Comparable<T> value1, T value2) {
            return value1.compareTo(value2) != 0;
        }
    },
    LESS_THAN {
        @Override
        public <T> boolean validate(Comparable<T> value1, T value2) {
            return value1.compareTo(value2) < 0;
        }
    },
    GREATER_THAN {
        @Override
        public <T> boolean validate(Comparable<T> value1, T value2) {
            return value1.compareTo(value2) > 0;
        }
    },
    LESS_EQUAL {
        @Override
        public <T> boolean validate(Comparable<T> value1, T value2) {
            return value1.compareTo(value2) <= 0;
        }
    },
    GREATER_EQUAL {
        @Override
        public <T> boolean validate(Comparable<T> value1, T value2) {
            return value1.compareTo(value2) >= 0;
        }
    };

    public abstract <T> boolean validate(Comparable<T> value1, T value2);

}

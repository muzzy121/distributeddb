package com.muzzy.domain.spsfl;

/**
 * Container made due to Serialization
 * Ordnung must sein
 * Nie wiedziałem ze ktos tu sprechen sie deutsch
 * Each class can have its "private static final long serialVersionUID" here
 */
public interface SerialVersionUIDContainer {
    long BLOCK_VERIFIED_SVUID = 1L;
    long TRANSACTION_SVUID = 2L;
    long TRANSACTION_INPUT_SVUID = 3L;
    long TRANSACTION_OUTPUT_SVUID = 4L;
    long BLOCK_UNVERIFIED_SVUID = 5L;
}

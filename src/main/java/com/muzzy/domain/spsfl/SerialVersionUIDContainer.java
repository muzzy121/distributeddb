package com.muzzy.domain.spsfl;

/**
 * Container made due to Serialization
 * Ordnung must sein
 * Each class can have its "private static final long serialVersionUID" here
 */
public interface SerialVersionUIDContainer {
    long BLOCK_SVUID = 1L;
    long TRANSACTION_SVUID = 2L;
    long TRANSACTION_INPUT_SVUID = 3L;
    long TRANSACTION_OUTPUT_SVUID = 4L;
}

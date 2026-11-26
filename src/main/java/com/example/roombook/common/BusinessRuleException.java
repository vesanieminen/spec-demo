package com.example.roombook.common;

/**
 * A request that is well-formed but violates a business rule or an alternative flow of a use case. The message is
 * the text shown to the actor.
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}

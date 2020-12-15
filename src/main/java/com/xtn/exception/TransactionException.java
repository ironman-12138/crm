package com.xtn.exception;

/**
 * 事务出现错误就抛此异常
 * 时间：2020年12月15日 18:41:58
 */
public class TransactionException extends RuntimeException {
    public TransactionException() {
        super();
    }

    public TransactionException(String message) {
        super(message);
    }
}

package com.xtn.exception;

/**
 * 自定义登录异常
 * 时间：2020年12月11日 18:53:24
 */
public class LoginException extends Exception {
    public LoginException() {
        super();
    }

    public LoginException(String message) {
        super(message);
    }
}

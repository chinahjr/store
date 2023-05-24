package com.cy.store.service.ex;

/**
 *用户名被占用的异常
 **/
public class UsernameDupicatedException extends ServiceException{
    public UsernameDupicatedException() {
        super();
    }

    public UsernameDupicatedException(String message) {
        super(message);
    }

    public UsernameDupicatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameDupicatedException(Throwable cause) {
        super(cause);
    }

    protected UsernameDupicatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

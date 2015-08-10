package com.rest.yun.exception;

public class ServerException extends RuntimeException {

    private static final long serialVersionUID = 475174380787444463L;
    
    private ErrorCode errorCode;
    
    public ServerException(ErrorCode errorCode, Object... replaceValues){
        this.errorCode = errorCode;
        this.errorCode.setValues(replaceValues);
    }
    
    public ServerException(ErrorCode errorCode, Throwable e){
        super(e);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}

package com.msw.et.exception;

import lombok.Data;

/**
 * 作者: mashuangwei
 * 日期: 2017/12/2
 */

@Data
public class MyException extends RuntimeException{
    private Integer code;
    private String message;

    public MyException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

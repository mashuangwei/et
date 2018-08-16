package com.msw.et.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Created by mashuangwei on 2017/7/16.
 */
@Data
public class Result<T> {
    private Integer code;
    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date date;
    private T data;

    public Result() {
    }

    @Override
    public String toString() {
        if (null == data) {
            return "Result{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    ", time='" + date.toString() + '\'' +
                    ", data=null" +
                    '}';
        }
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", time='" + date.toString()  + '\'' +
                ", data=" + data.toString() +
                '}';
    }
}

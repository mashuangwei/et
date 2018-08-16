package com.msw.et.util;

import com.msw.et.exception.Result;

import java.util.Date;

/**
 * Created by mashuangwei on 2018/7/16.
 */
public class ResultUtil {

    public static Result success(Integer code,Object object){
        Result result = new Result();
        result.setCode(code);
        result.setMessage("success");
        result.setData(object);
        result.setDate(new Date());
        return result;
    }

    public static Result success(Integer code){
        Result result = new Result();
        result.setCode(code);
        result.setMessage("success");
        result.setDate(new Date());
        return result;
    }

    public static Result error(Integer code,String message,Object object){
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        result.setData(object);
        result.setDate(new Date());
        return result;
    }

    public static Result error(Integer code,String message){
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        result.setDate(new Date());
        return result;
    }

}

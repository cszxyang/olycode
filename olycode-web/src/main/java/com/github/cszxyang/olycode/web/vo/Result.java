package com.github.cszxyang.olycode.web.vo;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @author cszxyang
 */
@Data
public class Result<T> {
    private int code;
    private T data;
    private String message;

    public static Result buildFailResponse(int code) {
        Result result = new Result();
        result.setCode(code);
        return result;
    }

    public static <D> Result<D> buildSuccessResponse(D data) {
        Result<D> result = new Result<>();
        result.setCode(HttpStatus.OK.value());
        result.setMessage(HttpStatus.OK.getReasonPhrase());
        result.setData(data);
        return result;
    }
}

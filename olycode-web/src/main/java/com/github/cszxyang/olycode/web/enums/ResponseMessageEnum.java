package com.github.cszxyang.olycode.web.enums;

import lombok.Getter;

/**
 * @author yzx
 */

public enum ResponseMessageEnum {
    /** 服务器繁忙，线程池拒绝新任务 */
    SERVER_BUSY(1501, "server busy"),
    /** 任务被中断 */
    PROGRAM_INTERRUPTED(1401, "program interrupted"),
    /** 任务执行超时 */
    TIME_LIMIT_EXCEEDED(1402, "Time Limit Exceeded"),
    ;

    @Getter
    private Integer code;
    @Getter
    private String message;

    ResponseMessageEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

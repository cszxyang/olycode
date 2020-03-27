package com.github.cszxyang.olycode.web.vo;

import lombok.Data;

/**
 * @author cszxyang
 * @since 2020-01-28
 */
@Data
public class ClientRequestBody {
    /**
     * 代码串
     */
    private String code;
    /**
     * 语言类型
     */
    private String lang;
}
package com.github.cszxyang.olycode.web.stat;

import lombok.Data;

import java.util.Date;

@Data
public class RequestRecord {
    private Long id;
    private String ip;
    private String requestMethod;
    private Date visitDate;
}

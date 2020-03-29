package com.github.cszxyang.olycode.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @since 2020-12-23
 * @author cszxyang
 */
@SpringBootApplication
@MapperScan("com.github.cszxyang.olycode.web.stat.mapper")
public class AppEntry {
    public static void main(String[] args) {
        SpringApplication.run(AppEntry.class);
    }
}

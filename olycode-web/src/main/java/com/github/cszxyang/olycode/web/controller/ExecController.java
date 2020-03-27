package com.github.cszxyang.olycode.web.controller;

import com.alibaba.fastjson.JSON;
import com.github.cszxyang.olycode.web.service.ClientApplicationRunner;
import com.github.cszxyang.olycode.web.vo.ClientRequestBody;
import com.github.cszxyang.olycode.web.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author cszxyang
 * @since 2020-01-28
 */
@Controller
public class ExecController {

    private Logger logger = LoggerFactory.getLogger(ExecController.class);

    @Autowired
    private ClientApplicationRunner clientApplicationRunner;

    @ResponseBody
    @RequestMapping(path = {"/run"}, method = RequestMethod.POST)
    public Result run(@RequestBody String body) {
        if (StringUtils.isEmpty(body)) {
            return Result.buildFailResponse(HttpStatus.BAD_REQUEST.value());
        }
        ClientRequestBody req = JSON.parseObject(body, ClientRequestBody.class);
        logger.info("compile request body: {}", req);

        String runResult = clientApplicationRunner.run(req.getCode());

        logger.info("compile result: {}", runResult);

        return Result.buildSuccessResponse(runResult);
    }

    @RequestMapping(path = {"/"}, method = RequestMethod.GET)
    public String entry() {
        return "ide";
    }

    @RequestMapping(path = {"/hello"}, method = RequestMethod.GET)
    public String hello() {
        return "hello";
    }
}


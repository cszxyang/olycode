package com.github.cszxyang.olycode.web.backdoor;

import com.github.cszxyang.olycode.web.stat.service.RequestRecordService;
import com.github.cszxyang.olycode.web.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author cszxyang
 * @since 2020-01-28
 */
@Controller("/backdoor")
public class StatController {

    private Logger logger = LoggerFactory.getLogger(StatController.class);

    @Autowired
    private RequestRecordService requestRecordService;

    @ResponseBody
    @RequestMapping(path = {"/totalUsersCount"}, method = RequestMethod.GET)
    public Result<Long> totalUsersCount() {
        return Result.buildSuccessResponse(requestRecordService.countTotalUsers());
    }

    @ResponseBody
    @RequestMapping(path = {"/totalVisitCount"}, method = RequestMethod.GET)
    public Result<Long> totalVisitCount() {
        return Result.buildSuccessResponse(requestRecordService.countTotalVisits());
    }
}


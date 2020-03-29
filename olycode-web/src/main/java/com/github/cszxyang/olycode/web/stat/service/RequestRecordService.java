package com.github.cszxyang.olycode.web.stat.service;

import com.github.cszxyang.olycode.web.stat.entity.RequestRecord;

public interface RequestRecordService {

    boolean insert(RequestRecord requestRecord);

    long countTotalUsers();

    long countTotalVisits();

}

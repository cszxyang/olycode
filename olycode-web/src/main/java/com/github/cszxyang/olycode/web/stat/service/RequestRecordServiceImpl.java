package com.github.cszxyang.olycode.web.stat.service;

import com.github.cszxyang.olycode.web.stat.entity.RequestRecord;
import com.github.cszxyang.olycode.web.stat.mapper.RequestRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RequestRecordServiceImpl implements RequestRecordService {

    @Autowired
    private RequestRecordMapper requestRecordMapper;

    @Override
    public boolean insert(RequestRecord requestRecord) {
        if (Objects.isNull(requestRecord)) {
            return false;
        }
        return requestRecordMapper.insertSelective(requestRecord) > 0;
    }

    @Override
    public long countTotalUsers() {
        Example example = Example.builder(RequestRecord.class).build();
        List<RequestRecord> requestRecords = requestRecordMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(requestRecords)) {
            return 0L;
        }
        Set<String> collect = requestRecords.stream().map(RequestRecord::getIp).collect(Collectors.toSet());
        return collect.size();
    }

    @Override
    public long countTotalVisits() {
        Example example = Example.builder(RequestRecord.class).build();
        return requestRecordMapper.selectCountByExample(example);
    }
}

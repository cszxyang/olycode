package com.github.cszxyang.olycode.web.service;

import com.github.cszxyang.olycode.web.enums.LanguageType;
import com.github.cszxyang.olycode.web.enums.ResponseMessageEnum;
import com.github.cszxyang.olycode.web.vo.ClientRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class ExecutionDispatcher {

    /**
     * 最长执行用时
     */
    private static final int MAX_EXECUTION_AWAIT_TIME = 10;

    @Autowired
    private AsynRunner asynRunner;

    public String doAsyncDispatcher(ClientRequestBody requestBody) {
        if (!validateBody(requestBody)) {
            throw new IllegalArgumentException("Expect argument not present");
        }
        Future<String> future;
        switch (LanguageType.langStrToEnum(requestBody.getLang())) {
            case JAVA:
                future = asynRunner.runJavaCode(requestBody);
                break;
            case LUA:
                future = asynRunner.runLuaCode(requestBody);
                break;
            case PYTHON:
                future = asynRunner.runPythonCode(requestBody);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + LanguageType.langStrToEnum(requestBody.getLang()));
        }
        // 获取运行结果，处理非客户端代码错误
        String runResult;
        try {
            runResult = future.get(MAX_EXECUTION_AWAIT_TIME, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            runResult = ResponseMessageEnum.PROGRAM_INTERRUPTED.getMessage();
        } catch (ExecutionException e) {
            runResult = e.getCause().getMessage();
        } catch (TimeoutException e) {
            runResult = ResponseMessageEnum.TIME_LIMIT_EXCEEDED.getMessage();
        } finally {
            future.cancel(true);
        }
        System.out.println("runResult: " + runResult);
        return runResult != null ? runResult : "";
    }

    private boolean validateBody(ClientRequestBody requestBody) {
        return !Objects.isNull(requestBody) && !Objects.isNull(requestBody.getCode())
                && !Objects.isNull(requestBody.getLang());
    }
}

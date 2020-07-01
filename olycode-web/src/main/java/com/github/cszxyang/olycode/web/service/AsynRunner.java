package com.github.cszxyang.olycode.web.service;

import com.github.cszxyang.olycode.java.compiler.CompileResult;
import com.github.cszxyang.olycode.java.compiler.StringSourceCompiler;
import com.github.cszxyang.olycode.java.exec.ClientEntryInvoker;
import com.github.cszxyang.olycode.lua.LuaExecutor;
import com.github.cszxyang.olycode.python.WrapperPythonInterpreter;
import com.github.cszxyang.olycode.web.vo.ClientRequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.tools.Diagnostic;
import javax.tools.FileObject;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Future;

@Component
public class AsynRunner {

    private Logger logger = LoggerFactory.getLogger(AsynRunner.class);

    private StringSourceCompiler compiler = new StringSourceCompiler();

    private WrapperPythonInterpreter pythonInterpreter = new WrapperPythonInterpreter();

    private LuaExecutor luaExecutor = new LuaExecutor();

    @Async("codeExecutor")
    public Future<String> runJavaCode(ClientRequestBody requestBody) {
        logger.info("Thread [{}] is running Java Code", Thread.currentThread().getName());

        // 编译源代码
        CompileResult compileResult = compiler.compile(requestBody.getCode());

        if (compileResult.isSuccess()) {
            return new AsyncResult<>(ClientEntryInvoker.invoke(compileResult.getBytes()));
        }

        // 获取编译错误信息
        List<Diagnostic<? extends FileObject>> diagnostics = compileResult.getDiagnostics();
        StringBuilder compileErrorRes = new StringBuilder();
        for (Diagnostic<? extends FileObject> diagnostic : diagnostics) {
            compileErrorRes.append("Compilation error at ");
            compileErrorRes.append(diagnostic.getLineNumber());
            compileErrorRes.append(": ");
            compileErrorRes.append(diagnostic.getMessage(Locale.ENGLISH));
            compileErrorRes.append(System.lineSeparator());
            compileErrorRes.append(System.lineSeparator());
        }
        return new AsyncResult<>(compileErrorRes.toString());
    }

    @Async("codeExecutor")
    public Future<String> runLuaCode(ClientRequestBody requestBody) {
        logger.info("Thread [{}] is running lua Code", Thread.currentThread().getName());
        return new AsyncResult<>(luaExecutor.execute(requestBody.getCode()));
    }

    @Async("codeExecutor")
    public Future<String> runPythonCode(ClientRequestBody requestBody) {
        logger.info("Thread [{}] is running python Code", Thread.currentThread().getName());
        return new AsyncResult<>(pythonInterpreter.execute(requestBody.getCode()));
    }
}
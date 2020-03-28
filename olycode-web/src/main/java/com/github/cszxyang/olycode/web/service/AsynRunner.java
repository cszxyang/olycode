package com.github.cszxyang.olycode.web.service;

import com.github.cszxyang.olycode.java.compiler.StringSourceCompiler;
import com.github.cszxyang.olycode.java.exec.ClientEntryInvoker;
import com.github.cszxyang.olycode.python.WrapperPythonInterpreter;
import com.github.cszxyang.olycode.web.vo.ClientRequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;

@Component
public class AsynRunner {

    private Logger logger = LoggerFactory.getLogger(AsynRunner.class);

    private StringSourceCompiler compiler = new StringSourceCompiler();

    private WrapperPythonInterpreter pythonInterpreter = new WrapperPythonInterpreter();

    @Async("javaCodeExecutor")
    public Future<String> runJavaCode(ClientRequestBody requestBody) {
        logger.info("Thread [{}] is running Java Code", Thread.currentThread().getName());

        // 编译结果收集器
        DiagnosticCollector<JavaFileObject> compileCollector = new DiagnosticCollector<>();

        // 编译源代码
        byte[] classBytes = compiler.compile(requestBody.getCode());

        // 编译不通过，获取并返回编译错误信息
        if (Objects.isNull(classBytes)) {
            // 获取编译错误信息
            List<Diagnostic<? extends JavaFileObject>> compileError = compileCollector.getDiagnostics();
            StringBuilder compileErrorRes = new StringBuilder();
            for (Diagnostic<? extends JavaFileObject> diagnostic : compileError) {
                compileErrorRes.append("Compilation error at ");
                compileErrorRes.append(diagnostic.getLineNumber());
                compileErrorRes.append(".");
                compileErrorRes.append(System.lineSeparator());
            }
            return new AsyncResult<>(compileErrorRes.toString());
        }
        String invoke = ClientEntryInvoker.invoke(classBytes);
        return new AsyncResult<>(invoke);
    }

    @Async("luaCodeExecutor")
    public Future<String> runLuaCode(ClientRequestBody requestBody) {
        logger.info("Thread [{}] is running lua Code", Thread.currentThread().getName());
        return new AsyncResult<>("开发中, 敬请期待...");
    }

    @Async("pythonCodeExecutor")
    public Future<String> runPythonCode(ClientRequestBody requestBody) {
        logger.info("Thread [{}] is running python Code", Thread.currentThread().getName());
        return new AsyncResult<>(pythonInterpreter.execute(requestBody.getCode()));
    }
}

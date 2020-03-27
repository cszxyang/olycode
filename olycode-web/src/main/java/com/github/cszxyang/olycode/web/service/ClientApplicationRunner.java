package com.github.cszxyang.olycode.web.service;

import com.github.cszxyang.olycode.java.compiler.StringSourceCompiler;
import com.github.cszxyang.olycode.java.exec.ClientEntryInvoker;
import com.github.cszxyang.olycode.web.enums.ResponseMessageEnum;
import org.springframework.stereotype.Service;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author yzx
 */
@Service
public class ClientApplicationRunner {
    /**
     * 最长执行用时
     */
    private static final int MAX_EXECUTION_AWAIT_TIME = 10;
    /**
     * 线程数 = CPU 核数 + 1
     */
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors() + 1;
    /**
     * 手动创建线程池
     */
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            THREAD_COUNT,
            16,
            0L,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(1024),
            new ThreadPoolExecutor.AbortPolicy()
    );

   /* public static void main(String[] args) {
        ClientApplicationRunner runner = new ClientApplicationRunner();
        String s = "public class Exmaple {\n" +
                "        public static void main(String[] args) {\n" +
                "            System.out.println(\"hello\");\n" +
                "        }\n" +
                "    }";
        System.out.println(runner.run(s));
    }*/

    private StringSourceCompiler compiler = new StringSourceCompiler();

    public String run(String srcCode) {
        // 编译结果收集器
        DiagnosticCollector<JavaFileObject> compileCollector = new DiagnosticCollector<>();

        // 编译源代码
        byte[] classBytes = compiler.compile(srcCode);

        // 编译不通过，获取并返回编译错误信息
        if (classBytes == null) {
            // 获取编译错误信息
            List<Diagnostic<? extends JavaFileObject>> compileError = compileCollector.getDiagnostics();
            StringBuilder compileErrorRes = new StringBuilder();
            for (Diagnostic diagnostic : compileError) {
                compileErrorRes.append("Compilation error at ");
                compileErrorRes.append(diagnostic.getLineNumber());
                compileErrorRes.append(".");
                compileErrorRes.append(System.lineSeparator());
            }
            return compileErrorRes.toString();
        }

        // 运行字节码的main方法
        Callable<String> runTask = () -> ClientEntryInvoker.invoke(classBytes);

        Future<String> res;
        try {
            res = threadPoolExecutor.submit(runTask);
        } catch (RejectedExecutionException e) {
            return ResponseMessageEnum.SERVER_BUSY.getMessage();
        }

        // 获取运行结果，处理非客户端代码错误
        String runResult;
        try {
            runResult = res.get(MAX_EXECUTION_AWAIT_TIME, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            runResult = ResponseMessageEnum.PROGRAM_INTERRUPTED.getMessage();
        } catch (ExecutionException e) {
            runResult = e.getCause().getMessage();
        } catch (TimeoutException e) {
            runResult = ResponseMessageEnum.TIME_LIMIT_EXCEEDED.getMessage();
        } finally {
            res.cancel(true);
        }
        System.out.println("runResult: " + runResult);
        return runResult != null ? runResult : "";
    }
}

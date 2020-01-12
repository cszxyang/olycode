package com.github.cszxyang.compiler;

import javax.tools.*;
import java.net.URI;
import java.util.Collections;
import java.util.Locale;

public class CustomJavaCompiler implements Compiler {
    @Override
    public CompileResult compile(URI uri, String code) {
        CompileResult compileResult = new CompileResult();
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
        StandardJavaFileManager standardJavaFileManager =
                javaCompiler.getStandardFileManager(null, Locale.ENGLISH, null);

        JavaFileObject testFile = new StringObject(uri, code);
        Iterable<? extends JavaFileObject> classes = Collections.singletonList(testFile);
        JavaCompiler.CompilationTask task =
                javaCompiler.getTask(null, standardJavaFileManager, collector, null, null, classes);

        compileResult.setSuccess(task.call());
        compileResult.setDiagnostic(collector.getDiagnostics().get(0));
        return compileResult;
    }
}

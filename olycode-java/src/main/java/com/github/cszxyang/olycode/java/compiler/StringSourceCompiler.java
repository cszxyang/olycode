package com.github.cszxyang.olycode.java.compiler;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yzx
 */
public class StringSourceCompiler extends JdkCompiler {

    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    private ModifiableDiagnosticCollector<FileObject> diagnosticCollector = new ModifiableDiagnosticCollector<>();

    private static Map<String, JavaFileObject> fileObjectMap = new ConcurrentHashMap<>();
    private JavaFileManager javaFileManager;
    /** 使用 Pattern 预编译功能 */
    private static final Pattern CLASS_PATTERN = Pattern.compile("class\\s+([$_a-zA-Z][$_a-zA-Z0-9]*)\\s*");

    private List<ExprCodeCheckProcessor> loopLimitEnhancers = Collections.singletonList(new ExprCodeCheckProcessor());

    public static void main(String[] args) {
        StringSourceCompiler compiler = new StringSourceCompiler();
        String code = "import java.util.Arrays;\n" +
                "import java.util.List;\n" +
                "import java.util.stream.IntStream;\n" +
                "\n" +
                "public class Example {\n" +
                "\n" +
                "    public static void main(String[] args) {\n" +
                "\n" +
                "        // endless loop handling\n" +
                "        for (int i = 0; i < 10; i++) {\n" +
                "            System.out.println(\"running...\");\n" +
                "        }\n" +
                "    }\n" +
                "}\n";
        System.out.println(compiler.compile(code));
    }

    private String getClassName(String source, Pattern pattern) {
        // 从源码字符串中匹配类名
        Matcher matcher = pattern.matcher(source);
        String className;
        if (matcher.find()) {
            className = matcher.group(1);
        } else {
            throw new IllegalArgumentException("No valid class");
        }
        return className;
    }

    public CompileResult compile(String source) {
        CompileResult compileResult = new CompileResult();
        String className = getClassName(source, CLASS_PATTERN);
        // 把源码字符串构造成JavaFileObject，供编译使用
        JavaFileObject sourceJavaFileObject = new StringSourceJavaFileObject(className, source);

        JavaCompiler.CompilationTask task = compiler.getTask(null, javaFileManager, diagnosticCollector,
                options, null, Collections.singletonList(sourceJavaFileObject));

        if (loopLimitEnhancers != null) {
            //task.setProcessors(loopLimitEnhancers);
        }

        Boolean compileSuccess = false;
        try {
            compileSuccess = task.call();
        } catch (Exception e) {
            e.printStackTrace();
        }

        compileResult.setSuccess(compileSuccess);
        if (!compileSuccess) {
            compileResult.setDiagnostics(diagnosticCollector.getDiagnostics());
            diagnosticCollector.clearDiagnostics();
            return compileResult;
        }
        JavaFileObject bytesJavaFileObject = fileObjectMap.get(className);
        compileResult.setBytes(((StringSourceJavaFileObject) bytesJavaFileObject).getCompiledBytes());
        return compileResult;
    }

    public StringSourceCompiler() {
        StandardJavaFileManager manager = compiler.getStandardFileManager(diagnosticCollector, Locale.ENGLISH, Charset.defaultCharset());
        javaFileManager = new JavaFileManagerImpl(manager);
    }

    public static class JavaFileManagerImpl extends ForwardingJavaFileManager<JavaFileManager> {
        protected JavaFileManagerImpl(JavaFileManager fileManager) {
            super(fileManager);
        }

        @Override
        public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) throws IOException {
            JavaFileObject javaFileObject = fileObjectMap.get(className);
            if (javaFileObject == null) {
                return super.getJavaFileForInput(location, className, kind);
            }
            return javaFileObject;
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            JavaFileObject javaFileObject = new StringSourceJavaFileObject(className, kind);
            fileObjectMap.put(className, javaFileObject);
            return javaFileObject;
        }
    }

    private static class StringSourceJavaFileObject extends SimpleJavaFileObject {
        private String source;
        private ByteArrayOutputStream outputStream;

        /**
         * 构造用来存储源代码的 JavaFileObject
         * 需要传入源码source，然后调用父类的构造方法创建kind = Kind.SOURCE的JavaFileObject对象
         */
        public StringSourceJavaFileObject(String name, String source) {
            super(URI.create("String:///" + name + Kind.SOURCE.extension), Kind.SOURCE);
            this.source = source;
        }

        /**
         * 构造用来存储字节码的JavaFileObject
         * 需要传入kind，即我们想要构建一个存储什么类型文件的JavaFileObject
         */
        public StringSourceJavaFileObject(String name, Kind kind) {
            super(URI.create("String:///" + name + Kind.SOURCE.extension), kind);
            this.source = null;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            if (source == null) {
                throw new IllegalArgumentException("source == null");
            }
            return source;
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            outputStream = new ByteArrayOutputStream();
            return outputStream;
        }

        public byte[] getCompiledBytes() {
            return outputStream.toByteArray();
        }
    }
}

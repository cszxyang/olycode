package com.github.cszxyang.compiler;

import javax.tools.Diagnostic;
import javax.tools.FileObject;
import java.net.URI;
import java.util.List;

public interface Compiler {
    /**
     * 编译，其他语言实现需要考虑继承 FileObject
     * @param code 代码字符串
     * @return 编译结果
     */
    CompileResult compile(URI uri, String code);
}

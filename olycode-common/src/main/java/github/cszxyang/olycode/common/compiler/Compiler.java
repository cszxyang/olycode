package github.cszxyang.olycode.common.compiler;

import java.net.URI;

/**
 * @since 2020/01/12
 * @author cszxyang
 */
public interface Compiler {
    /**
     * 编译，其他语言实现需要考虑继承 FileObject
     * @param code 代码字符串
     * @return 编译结果
     */
    CompileResult compile(URI uri, String code);
}

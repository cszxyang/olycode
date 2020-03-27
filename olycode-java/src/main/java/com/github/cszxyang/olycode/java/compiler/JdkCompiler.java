package com.github.cszxyang.olycode.java.compiler;

import java.util.Arrays;
import java.util.List;

/**
 * @author yzx
 */
public class JdkCompiler {

    protected volatile List<String> options;

    JdkCompiler() {
        options = Arrays.asList("-source", "1.8", "-target", "1.8", "-g");
    }
}

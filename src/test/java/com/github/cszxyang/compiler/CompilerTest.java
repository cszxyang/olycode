package com.github.cszxyang.compiler;

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class CompilerTest {

    @Test
    public void compileJavaCodeStr() throws URISyntaxException {
        final String codeStr = "package com.github.cszxyang.test.math;" +
                "class CalculatorTest {\n" +
                "  public void testMultiply() {\n" +
                "    Calculator c = new Calculator()\n" +
                "    System.out.println(c.multiply(2, 4));\n" +
                "  }\n" +
                "  public static void main(String[] args) {\n" +
                "    CalculatorTest ct = new CalculatorTest();\n" +
                "    ct.testMultiply();\n" +
                "  }\n" +
                "}\n";
        URI uri = new URI("com.github.cszxyang.CalculatorTest.");
        Compiler compiler = new CustomJavaCompiler();
        CompileResult result = compiler.compile(uri, codeStr);
        System.out.println(result.getCompileReport());;
    }
}

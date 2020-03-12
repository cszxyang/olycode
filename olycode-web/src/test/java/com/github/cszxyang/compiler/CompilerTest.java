package com.github.cszxyang.compiler;

import github.cszxyang.olycode.java.compiler.CompileResult;
import github.cszxyang.olycode.java.compiler.StringSourceCompiler;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.reflect.Field;
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
        /*URI uri = new URI("com.github.cszxyang.CalculatorTest.");
        Compiler compiler = new StringSourceCompiler();
        CompileResult result = compiler.compile(uri, codeStr);
        System.out.println(result.getCompileReport());;*/
    }

    @Test
    public void test() {
        Erasure<String> erasure = new Erasure<String>("hello");
        Class clz = erasure.getClass();
        System.out.println(clz.getName());
    }

    @Test
    public void test1() {
        Erasure<String> erasure = new Erasure<String>("hello");
        Class clz = erasure.getClass();
        Field[] fs = clz.getDeclaredFields();
        for ( Field f:fs) {
            System.out.println("field name：" + f.getName() + "\ttype:" + f.getType().getName());
        }
    }

    @Test
    public void te() {
        f(new Object());
    }

    void f(Object obj) {
        Class type = obj.getClass();
        Annotation a = type.getAnnotation(Documented.class); // unchecked warning
    }
}

class Base{}

class Sub extends Base {}

class Test2 <T, E extends T> {
    T value1;
    E value2;

    public T test1(T t){
        return value1;
    }
}

class Erasure <T> {
    T object;
    public Erasure(T object) {
        this.object = object;
    }
}
package com.github.cszxyang.olycode.java.exec;

import com.github.cszxyang.olycode.java.bytecode.ConstantPoolManipulator;
import com.github.cszxyang.olycode.common.proxy.ProxySystem;
import javafx.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * 启动客户端代码
 * 1. 代理 System
 * 2. 字节码操纵
 * 3. 类加载
 * @author yzx
 */
public class ClientEntryInvoker {

    public static String invoke(byte[] bytecode) {
        List<Pair<String, String>> pairs = Arrays.asList(
            new Pair<>("java/lang/System", "com/github/cszxyang/olycode/common/proxy/ProxySystem"),
            new Pair<>("java/util/Scanner", "com/github/cszxyang/olycode/common/proxy/ProxyScanner")
        );
        byte[] bytes = ConstantPoolManipulator.doManipulate(bytecode, pairs);

        CustomClassLoader classLoader = new CustomClassLoader();
        Class aClass = classLoader.loadBytes(bytes);

        try {
            Method main = aClass.getMethod("main", String[].class);
            main.invoke(null, (Object) new String[0]);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.getCause().printStackTrace(ProxySystem.err);
            ProxySystem.err.flush();
        }
        String execRes = ProxySystem.getBufferString();
        ProxySystem.closeBuffer();
        return execRes;
    }
}

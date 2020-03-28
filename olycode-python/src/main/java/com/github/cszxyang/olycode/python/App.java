package com.github.cszxyang.olycode.python;

import org.python.util.PythonInterpreter;

public class App {
    public static void main(String[] args) {
        PythonInterpreter interpreter = new PythonInterpreter();

        String code = "#!/usr/bin/python\n" +
                "jkj\n" +
                "print(\"Hello, World!\");";
        String et = "";
        try {
            interpreter.exec(code);   // 执行python脚本
        } catch (Exception e) {
            et = e.toString();
        }
        System.out.println("dhadha");
        System.out.println(et);
    }
}

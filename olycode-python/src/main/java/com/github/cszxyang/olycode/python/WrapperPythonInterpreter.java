package com.github.cszxyang.olycode.python;

import org.python.util.PythonInterpreter;

import java.io.*;

public class WrapperPythonInterpreter {

    private PythonInterpreter interpreter = new PythonInterpreter();

    public String execute(String code) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        interpreter.setOut(out);

        String res = "";
        try {
            interpreter.exec(code);
            res = out.toString();
        } catch (Exception e) {
            res = e.toString();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
}

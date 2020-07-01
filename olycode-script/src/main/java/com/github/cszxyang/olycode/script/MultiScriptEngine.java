package com.github.cszxyang.olycode.script;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MultiScriptEngine {

    public static void main(String[] args) {
        MultiScriptEngine engine = new MultiScriptEngine();
        String code = "function displayMessage() {\n" +
                "    console.log(\"Hello JS World!\");\n" +
                "};\n" +
                "displayMessage();";
        String res = engine.executeJavaScript(code);

        System.out.println("res: " + res);
    }

    private final ScriptEngine ecmaScript;

    public MultiScriptEngine() {
        // 构造一个脚本引擎管理器
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ecmaScript = scriptEngineManager.getEngineByName("nashorn");
    }

    public String executeJavaScript(String script) {
        String res;
        try {
            res = String.valueOf(ecmaScript.eval(loadConsole(script)));
        } catch (ScriptException e) {
            res = e.getMessage();
        }
        return res;
    }

    private String loadConsole(String script) {
        String consoleDefine = "console = { \n" +
                "    log: print,\n" +
                "    warn: print,\n" +
                "    error: print\n" +
                "};\n";
        return consoleDefine + script;
    }
}
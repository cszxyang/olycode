package com.github.cszxyang.compiler;

import lombok.Data;

import javax.tools.Diagnostic;
import javax.tools.FileObject;
import java.util.Locale;

@Data
public class CompileResult {

    private boolean isSuccess;

    private Diagnostic<? extends FileObject> diagnostic;

    public String getCompileReport() {
        return "line: " + diagnostic.getLineNumber() + "; "
                + diagnostic.getMessage(Locale.ENGLISH) + ';'
                + diagnostic.getSource();
    }
}
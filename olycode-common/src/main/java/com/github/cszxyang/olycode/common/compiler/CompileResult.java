package com.github.cszxyang.olycode.common.compiler;

import lombok.Data;

import javax.tools.Diagnostic;
import javax.tools.FileObject;
import java.util.Locale;

/**
 * @since 2020/01/12
 * @author cszxyang
 */
@Data
public class CompileResult {

    private boolean isSuccess;

    private byte[] bytes;

    private Diagnostic<? extends FileObject> diagnostic;

    public String getCompileReport() {
        return "line: " + diagnostic.getLineNumber() + "; "
                + diagnostic.getMessage(Locale.ENGLISH) + ';'
                + diagnostic.getSource();
    }
}
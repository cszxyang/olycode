package com.github.cszxyang.olycode.java.compiler;

import lombok.Data;

import javax.tools.Diagnostic;
import javax.tools.FileObject;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @since 2020/01/12
 * @author cszxyang
 */
public class CompileResult {

    private boolean isSuccess;

    private byte[] bytes;

    private List<Diagnostic<? extends FileObject>> diagnostics;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public List<Diagnostic<? extends FileObject>> getDiagnostics() {
        return diagnostics;
    }

    public void setDiagnostics(List<Diagnostic<? extends FileObject>> diagnostics) {
        this.diagnostics = diagnostics;
    }

    @Override
    public String toString() {
        return "CompileResult{" +
                "isSuccess=" + isSuccess +
                ", bytes=" + Arrays.toString(bytes) +
                ", diagnostics=" + diagnostics +
                '}';
    }
}
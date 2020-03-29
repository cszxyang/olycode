package com.github.cszxyang.olycode.java.compiler;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ModifiableDiagnosticCollector<S> implements DiagnosticListener<S> {

    private List<Diagnostic<? extends S>> diagnostics =
            Collections.synchronizedList(new LinkedList<>());

    public void report(Diagnostic<? extends S> diagnostic) {
        diagnostics.add(diagnostic);
    }

    /**
     * Gets a list view of diagnostics collected by this object.
     *
     * @return a list view of diagnostics
     */
    public List<Diagnostic<? extends S>> getDiagnostics() {
        return new ArrayList<>(diagnostics);
    }

    public void clearDiagnostics() {
        diagnostics.clear();
    }

}

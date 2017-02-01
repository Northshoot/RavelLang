package org.stanford.ravel.compiler;

import org.stanford.ravel.compiler.SourceLocation;

/**
 * Created by gcampagn on 1/20/17.
 */
public class CompileError {
    public enum Severity {
        INFO,
        ERROR,
        WARNING,
        FATAL,
    }

    private final SourceLocation location;
    private final Severity severity;
    private final String message;

    public CompileError(SourceLocation loc, Severity severity, String message) {
        this.location = loc;
        this.severity = severity;
        this.message = message;
    }

    public Severity getSeverity() {
        return severity;
    }

    @Override
    public String toString() {
        return (location != null ? location.toString() + ": " : "") + severity.toString().toLowerCase() + ": " + message;
    }
}

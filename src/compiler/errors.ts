/**
 * Ravel v2 Compiler Errors
 */

import type { SourceLocation } from "./tokens.js";

export enum Severity {
  INFO = "info",
  WARNING = "warning",
  ERROR = "error",
  FATAL = "fatal",
}

export class RavelError extends Error {
  constructor(
    public severity: Severity,
    public msg: string,
    public loc?: SourceLocation,
  ) {
    const prefix = loc
      ? `${loc.file ?? "<stdin>"}:${loc.line}:${loc.column}`
      : "<unknown>";
    super(`${prefix}: ${severity}: ${msg}`);
    this.name = "RavelError";
  }
}

export class LexerError extends RavelError {
  constructor(msg: string, loc?: SourceLocation) {
    super(Severity.ERROR, msg, loc);
    this.name = "LexerError";
  }
}

export class ParseError extends RavelError {
  constructor(msg: string, loc?: SourceLocation) {
    super(Severity.ERROR, msg, loc);
    this.name = "ParseError";
  }
}

export class AnalysisError extends RavelError {
  constructor(msg: string, loc?: SourceLocation) {
    super(Severity.ERROR, msg, loc);
    this.name = "AnalysisError";
  }
}

export class CompilerDiagnostics {
  private diagnostics: RavelError[] = [];

  add(error: RavelError): void {
    this.diagnostics.push(error);
  }

  info(msg: string, loc?: SourceLocation): void {
    this.diagnostics.push(new RavelError(Severity.INFO, msg, loc));
  }

  warn(msg: string, loc?: SourceLocation): void {
    this.diagnostics.push(new RavelError(Severity.WARNING, msg, loc));
  }

  error(msg: string, loc?: SourceLocation): void {
    this.diagnostics.push(new RavelError(Severity.ERROR, msg, loc));
  }

  fatal(msg: string, loc?: SourceLocation): never {
    const err = new RavelError(Severity.FATAL, msg, loc);
    this.diagnostics.push(err);
    throw err;
  }

  hasErrors(): boolean {
    return this.diagnostics.some(
      (d) => d.severity === Severity.ERROR || d.severity === Severity.FATAL,
    );
  }

  getAll(): readonly RavelError[] {
    return this.diagnostics;
  }

  format(): string {
    return this.diagnostics.map((d) => d.message).join("\n");
  }
}

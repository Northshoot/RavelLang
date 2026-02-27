/**
 * Ravel v2 — Language & Compiler for LLM-Agent-Orchestrated Systems
 *
 * Public API: compile Ravel source into an AgentExecutionPlan.
 *
 *   Original Ravel (2015)     Modern Ravel (2025)
 *   ────────────────────     ──────────────────────
 *   .rv → Java Compiler      .rv → TypeScript Compiler
 *     → C / Java source        → AgentExecutionPlan (JSON)
 *       → deploy to device        → LLM agents build the system
 *
 * Usage:
 *   import { compile } from "@ravel-lang/compiler";
 *   const plan = compile(sourceCode, "my-app.rv");
 *   // plan is an AgentExecutionPlan ready for agent dispatch
 */

// ── Compiler pipeline ──
export { Lexer } from "./compiler/lexer.js";
export { Parser } from "./compiler/parser.js";
export { Analyzer } from "./compiler/analyzer.js";
export type { AnalyzedProgram, Scope, Symbol, FeatureOwnershipMap } from "./compiler/analyzer.js";
export { IRBuilder } from "./compiler/ir.js";
export type { SystemIR, FeatureLayerIR, FeatureIR, SharedSymbolIR } from "./compiler/ir.js";

// ── AST types ──
export type * from "./compiler/ast.js";

// ── Tokens ──
export { TokenType } from "./compiler/tokens.js";
export type { Token, SourceLocation } from "./compiler/tokens.js";

// ── Errors ──
export {
  RavelError,
  LexerError,
  ParseError,
  AnalysisError,
  CompilerDiagnostics,
} from "./compiler/errors.js";

// ── Agent system ──
export { Planner } from "./agents/planner.js";
export {
  Orchestrator,
  serializePlan,
  deserializePlan,
  summarizePlan,
} from "./agents/orchestrator.js";
export type {
  AgentExecutionPlan,
  AgentTask,
  AgentType,
  ExecutionPhase,
  FeatureScope,
  SharedSymbolWarning,
} from "./agents/types.js";
export type {
  AgentRuntime,
  ExecutionResult,
} from "./agents/orchestrator.js";

// ── Convenience: full compile pipeline ──

import { Lexer } from "./compiler/lexer.js";
import { Parser } from "./compiler/parser.js";
import { Analyzer } from "./compiler/analyzer.js";
import { IRBuilder } from "./compiler/ir.js";
import { Planner } from "./agents/planner.js";
import type { AgentExecutionPlan } from "./agents/types.js";
import type { SystemIR } from "./compiler/ir.js";
import type { AnalyzedProgram } from "./compiler/analyzer.js";
import type { Program } from "./compiler/ast.js";

export interface CompileResult {
  /** The parsed AST */
  ast: Program;
  /** The analyzed and validated program */
  analyzed: AnalyzedProgram;
  /** The system intermediate representation */
  ir: SystemIR;
  /** The agent execution plan — the "compiled output" */
  plan: AgentExecutionPlan;
}

/**
 * Compile Ravel source code into an AgentExecutionPlan.
 *
 * This is the main entry point. It runs the full pipeline:
 *   source → lex → parse → analyze → IR → plan
 */
export function compile(source: string, filename?: string): CompileResult {
  // 1. Lex
  const lexer = new Lexer(source, filename);
  const tokens = lexer.tokenize();

  // 2. Parse
  const parser = new Parser(tokens);
  const ast = parser.parse();

  // 3. Analyze
  const analyzer = new Analyzer();
  const analyzed = analyzer.analyze(ast);

  if (analyzed.diagnostics.hasErrors()) {
    throw new Error(
      `Compilation failed:\n${analyzed.diagnostics.format()}`,
    );
  }

  // 4. Lower to IR
  const irBuilder = new IRBuilder();
  const ir = irBuilder.build(analyzed);

  // 5. Plan agent execution
  const planner = new Planner();
  const plan = planner.plan(ir);

  return { ast, analyzed, ir, plan };
}

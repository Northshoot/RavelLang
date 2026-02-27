/**
 * Ravel v2 Abstract Syntax Tree
 *
 * AST node types for the modern Ravel language. Each node carries a source
 * location for error reporting and maps directly to the grammar productions.
 *
 * Lineage from original Ravel (Riliskis, Hong, Levis 2015):
 *   model, controller, view    → preserved and enhanced
 *   interface                  → renamed to `service`
 *   space                      → renamed to `runtime`
 *   (new)                      → agent, system, behavior, flow
 */

import type { SourceLocation } from "./tokens.js";

// ═══════════════════════════════════════════════════════════════════
// Base
// ═══════════════════════════════════════════════════════════════════

export interface ASTNode {
  kind: string;
  loc: SourceLocation;
}

// ═══════════════════════════════════════════════════════════════════
// Program (root)
// ═══════════════════════════════════════════════════════════════════

export interface Program extends ASTNode {
  kind: "Program";
  declarations: Declaration[];
}

export type Declaration =
  | ImportDecl
  | SystemDecl
  | ModelDecl
  | ControllerDecl
  | ViewDecl
  | ServiceDecl
  | RuntimeDecl
  | AgentDecl
  | FlowDecl
  | FeatureDecl;

// ═══════════════════════════════════════════════════════════════════
// Import
// ═══════════════════════════════════════════════════════════════════

export interface ImportDecl extends ASTNode {
  kind: "ImportDecl";
  module: string[];
  names: string[] | "*";
  alias?: string;
}

// ═══════════════════════════════════════════════════════════════════
// System (new in v2 — top-level composition)
// ═══════════════════════════════════════════════════════════════════

export interface SystemDecl extends ASTNode {
  kind: "SystemDecl";
  name: string;
  properties: PropertyAssignment[];
}

// ═══════════════════════════════════════════════════════════════════
// Model (evolved from original Ravel model)
// ═══════════════════════════════════════════════════════════════════

export type ModelQualifier = "local" | "streaming" | "replicated" | "ephemeral";

export interface ModelDecl extends ASTNode {
  kind: "ModelDecl";
  name: string;
  qualifier?: ModelQualifier;
  schema: FieldDecl[];
  properties: PropertyAssignment[];
}

export interface FieldDecl extends ASTNode {
  kind: "FieldDecl";
  name: string;
  typeExpr: TypeExpr;
  annotations: Annotation[];
  defaultValue?: Expression;
}

export interface Annotation extends ASTNode {
  kind: "Annotation";
  name: string;
  args: AnnotationArg[];
}

export interface AnnotationArg {
  name?: string;
  value: Expression;
}

// ═══════════════════════════════════════════════════════════════════
// Controller (evolved from original Ravel controller)
// ═══════════════════════════════════════════════════════════════════

export interface ControllerDecl extends ASTNode {
  kind: "ControllerDecl";
  name: string;
  params: Parameter[];
  variables: VariableDecl[];
  events: EventHandler[];
  methods: MethodDecl[];
}

export interface Parameter extends ASTNode {
  kind: "Parameter";
  name: string;
  typeExpr: TypeExpr;
  defaultValue?: Expression;
}

export interface VariableDecl extends ASTNode {
  kind: "VariableDecl";
  name: string;
  typeExpr?: TypeExpr;
  value?: Expression;
}

export interface EventHandler extends ASTNode {
  kind: "EventHandler";
  target: string[];     // e.g., ["tickets", "purchase_requested"]
  params: Parameter[];
  body: Statement[];
}

export interface MethodDecl extends ASTNode {
  kind: "MethodDecl";
  name: string;
  params: Parameter[];
  returnType?: TypeExpr;
  body: Statement[];
}

// ═══════════════════════════════════════════════════════════════════
// View (evolved from original Ravel view — now declarative + behavior)
// ═══════════════════════════════════════════════════════════════════

export interface ViewDecl extends ASTNode {
  kind: "ViewDecl";
  name: string;
  displays?: TypeExpr;
  platforms: string[];
  behaviors: string[];
  style: PropertyAssignment[];
  components: ComponentDecl[];
  properties: PropertyAssignment[];
}

export interface ComponentDecl extends ASTNode {
  kind: "ComponentDecl";
  name: string;
  type: string;
  args: NamedArg[];
}

export interface NamedArg {
  name?: string;
  value: Expression;
}

// ═══════════════════════════════════════════════════════════════════
// Service (replaces original Ravel 'interface')
// ═══════════════════════════════════════════════════════════════════

export interface ServiceDecl extends ASTNode {
  kind: "ServiceDecl";
  name: string;
  provider?: string;
  methods: ServiceMethodDecl[];
  events: ServiceEventDecl[];
  config: PropertyAssignment[];
  properties: PropertyAssignment[];
}

export interface ServiceMethodDecl extends ASTNode {
  kind: "ServiceMethodDecl";
  name: string;
  params: Parameter[];
  returnType?: TypeExpr;
}

export interface ServiceEventDecl extends ASTNode {
  kind: "ServiceEventDecl";
  name: string;
  params: Parameter[];
}

// ═══════════════════════════════════════════════════════════════════
// Runtime (replaces original Ravel 'space')
// ═══════════════════════════════════════════════════════════════════

export interface RuntimeDecl extends ASTNode {
  kind: "RuntimeDecl";
  name: string;
  platform?: Expression;
  language?: Expression;
  orchestration?: string;
  views: string[];
  controllers: string[];
  services: string[];
  models: string[];
  agents: string[];
  scaling: PropertyAssignment[];
  config: PropertyAssignment[];
  env: PropertyAssignment[];
  properties: PropertyAssignment[];
}

// ═══════════════════════════════════════════════════════════════════
// Agent (new in v2 — LLM-powered component)
// ═══════════════════════════════════════════════════════════════════

export interface AgentDecl extends ASTNode {
  kind: "AgentDecl";
  name: string;
  model?: Expression;
  behaviors: string[];
  inputs: TypeExpr[];
  output?: TypeExpr;
  outputAnnotations: Annotation[];
  tools: Expression[];
  properties: PropertyAssignment[];
}

// ═══════════════════════════════════════════════════════════════════
// Flow (evolved from original Ravel flow — now typed pipelines)
// ═══════════════════════════════════════════════════════════════════

export interface FlowDecl extends ASTNode {
  kind: "FlowDecl";
  name: string;
  rules: FlowRule[];
}

export interface FlowRule extends ASTNode {
  kind: "FlowRule";
  source: string;
  target: string;
  properties: PropertyAssignment[];
}

// ═══════════════════════════════════════════════════════════════════
// Feature (new in v2 — ownership tracking and blast-radius analysis)
// ═══════════════════════════════════════════════════════════════════

export interface FeatureDecl extends ASTNode {
  kind: "FeatureDecl";
  name: string;
  description?: string;
  /** Symbols this feature primarily owns (models, controllers, views, etc.) */
  owns: string[];
  /** Symbols this feature reads/depends on but does not own */
  uses: string[];
  properties: PropertyAssignment[];
}

// ═══════════════════════════════════════════════════════════════════
// Type Expressions
// ═══════════════════════════════════════════════════════════════════

export type TypeExpr =
  | PrimitiveTypeExpr
  | NamedTypeExpr
  | ArrayTypeExpr
  | OptionalTypeExpr
  | MapTypeExpr
  | EnumTypeExpr
  | ForeignKeyTypeExpr
  | TupleTypeExpr;

export interface PrimitiveTypeExpr extends ASTNode {
  kind: "PrimitiveTypeExpr";
  name: string;
}

export interface NamedTypeExpr extends ASTNode {
  kind: "NamedTypeExpr";
  name: string;
}

export interface ArrayTypeExpr extends ASTNode {
  kind: "ArrayTypeExpr";
  element: TypeExpr;
  size?: number;
}

export interface OptionalTypeExpr extends ASTNode {
  kind: "OptionalTypeExpr";
  inner: TypeExpr;
}

export interface MapTypeExpr extends ASTNode {
  kind: "MapTypeExpr";
  key: TypeExpr;
  value: TypeExpr;
}

export interface EnumTypeExpr extends ASTNode {
  kind: "EnumTypeExpr";
  members: string[];
}

export interface ForeignKeyTypeExpr extends ASTNode {
  kind: "ForeignKeyTypeExpr";
  type: TypeExpr;
  refModel: string;
  refField: string;
}

export interface TupleTypeExpr extends ASTNode {
  kind: "TupleTypeExpr";
  elements: TypeExpr[];
}

// ═══════════════════════════════════════════════════════════════════
// Expressions
// ═══════════════════════════════════════════════════════════════════

export type Expression =
  | IntLiteral
  | FloatLiteral
  | StringLiteral
  | BoolLiteral
  | NoneLiteral
  | DurationLiteral
  | SizeLiteral
  | RateLiteral
  | PercentLiteral
  | Identifier
  | BinaryExpr
  | UnaryExpr
  | MemberExpr
  | CallExpr
  | IndexExpr
  | ListExpr
  | MapExpr
  | TernaryExpr
  | CastExpr
  | ErrorExpr
  | LenExpr;

export interface IntLiteral extends ASTNode {
  kind: "IntLiteral";
  value: number;
}

export interface FloatLiteral extends ASTNode {
  kind: "FloatLiteral";
  value: number;
}

export interface StringLiteral extends ASTNode {
  kind: "StringLiteral";
  value: string;
}

export interface BoolLiteral extends ASTNode {
  kind: "BoolLiteral";
  value: boolean;
}

export interface NoneLiteral extends ASTNode {
  kind: "NoneLiteral";
}

export interface DurationLiteral extends ASTNode {
  kind: "DurationLiteral";
  value: number;
  unit: "s" | "m" | "h" | "d";
}

export interface SizeLiteral extends ASTNode {
  kind: "SizeLiteral";
  value: number;
  unit: "kb" | "mb" | "gb" | "tb";
}

export interface RateLiteral extends ASTNode {
  kind: "RateLiteral";
  value: number;
  per: "s" | "min" | "h" | "d";
}

export interface PercentLiteral extends ASTNode {
  kind: "PercentLiteral";
  value: number;
}

export interface Identifier extends ASTNode {
  kind: "Identifier";
  name: string;
}

export type BinaryOp =
  | "+" | "-" | "*" | "/" | "%"
  | "==" | "!=" | "<" | ">" | "<=" | ">="
  | "and" | "or"
  | "&" | "|" | "^" | "<<" | ">>";

export interface BinaryExpr extends ASTNode {
  kind: "BinaryExpr";
  op: BinaryOp;
  left: Expression;
  right: Expression;
}

export type UnaryOp = "-" | "not" | "~";

export interface UnaryExpr extends ASTNode {
  kind: "UnaryExpr";
  op: UnaryOp;
  operand: Expression;
}

export interface MemberExpr extends ASTNode {
  kind: "MemberExpr";
  object: Expression;
  property: string;
}

export interface CallExpr extends ASTNode {
  kind: "CallExpr";
  callee: Expression;
  args: NamedArg[];
}

export interface IndexExpr extends ASTNode {
  kind: "IndexExpr";
  object: Expression;
  index: Expression;
}

export interface ListExpr extends ASTNode {
  kind: "ListExpr";
  elements: Expression[];
}

export interface MapExpr extends ASTNode {
  kind: "MapExpr";
  entries: MapEntry[];
}

export interface MapEntry {
  key: string;
  value: Expression;
}

export interface TernaryExpr extends ASTNode {
  kind: "TernaryExpr";
  condition: Expression;
  consequent: Expression;
  alternate: Expression;
}

export interface CastExpr extends ASTNode {
  kind: "CastExpr";
  typeExpr: TypeExpr;
  expr: Expression;
}

export interface ErrorExpr extends ASTNode {
  kind: "ErrorExpr";
  message: Expression;
}

export interface LenExpr extends ASTNode {
  kind: "LenExpr";
  expr: Expression;
}

// ═══════════════════════════════════════════════════════════════════
// Statements
// ═══════════════════════════════════════════════════════════════════

export type Statement =
  | AssignmentStmt
  | ExpressionStmt
  | IfStmt
  | ForStmt
  | WhileStmt
  | ReturnStmt
  | DeleteStmt
  | PassStmt
  | BreakStmt
  | ContinueStmt
  | VariableDecl
  | EmitStmt
  | AwaitStmt
  | LogStmt;

export interface AssignmentStmt extends ASTNode {
  kind: "AssignmentStmt";
  target: Expression;
  op: "=" | "+=" | "-=";
  value: Expression;
}

export interface ExpressionStmt extends ASTNode {
  kind: "ExpressionStmt";
  expr: Expression;
}

export interface IfStmt extends ASTNode {
  kind: "IfStmt";
  condition: Expression;
  body: Statement[];
  elifs: { condition: Expression; body: Statement[] }[];
  elseBody?: Statement[];
}

export interface ForStmt extends ASTNode {
  kind: "ForStmt";
  variable: string;
  iterable: Expression;
  body: Statement[];
}

export interface WhileStmt extends ASTNode {
  kind: "WhileStmt";
  condition: Expression;
  body: Statement[];
}

export interface ReturnStmt extends ASTNode {
  kind: "ReturnStmt";
  value?: Expression;
}

export interface DeleteStmt extends ASTNode {
  kind: "DeleteStmt";
  target: Expression;
}

export interface PassStmt extends ASTNode {
  kind: "PassStmt";
}

export interface BreakStmt extends ASTNode {
  kind: "BreakStmt";
}

export interface ContinueStmt extends ASTNode {
  kind: "ContinueStmt";
}

export interface EmitStmt extends ASTNode {
  kind: "EmitStmt";
  event: Expression;
}

export interface AwaitStmt extends ASTNode {
  kind: "AwaitStmt";
  expr: Expression;
}

export interface LogStmt extends ASTNode {
  kind: "LogStmt";
  expr: Expression;
}

// ═══════════════════════════════════════════════════════════════════
// Shared
// ═══════════════════════════════════════════════════════════════════

export interface PropertyAssignment extends ASTNode {
  kind: "PropertyAssignment";
  name: string;
  value: Expression;
}

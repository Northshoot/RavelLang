/**
 * Ravel v2 Intermediate Representation
 *
 * The IR is the bridge between the analyzed AST and the agent execution plan.
 *
 * Original Ravel compiled to TypedIR → platform source code (C/Java).
 * Modern Ravel compiles to SystemIR → Agent Execution Plan → LLM agents
 * build the entire project.
 *
 * The IR captures the essential architecture of the system:
 *   - What data structures exist (models → database schemas + API types)
 *   - What logic runs where (controllers → service handlers + UI logic)
 *   - What the user sees (views → UI components)
 *   - How pieces connect (flows → APIs, protocols, events)
 *   - What AI capabilities are needed (agents → LLM integrations)
 *   - Where everything runs (runtimes → containers, devices, clouds)
 */

import type { AnalyzedProgram, FlowEdge, FeatureOwnershipMap } from "./analyzer.js";
import type * as AST from "./ast.js";

// ═══════════════════════════════════════════════════════════════════
// System IR — The complete architectural description
// ═══════════════════════════════════════════════════════════════════

export interface SystemIR {
  name: string;
  description?: string;
  version?: string;

  /** Data layer: schemas, storage, sync rules */
  dataLayer: DataLayerIR;

  /** Logic layer: controllers, event handlers, business rules */
  logicLayer: LogicLayerIR;

  /** Presentation layer: views, UI components, behavior specs */
  presentationLayer: PresentationLayerIR;

  /** Integration layer: external services, APIs */
  integrationLayer: IntegrationLayerIR;

  /** Intelligence layer: AI agents, LLM integrations */
  intelligenceLayer: IntelligenceLayerIR;

  /** Infrastructure layer: runtimes, deployment targets, scaling */
  infrastructureLayer: InfrastructureLayerIR;

  /** Communication layer: data flows, protocols, events */
  communicationLayer: CommunicationLayerIR;

  /** Feature ownership layer: blast-radius tracking, cross-feature deps */
  featureLayer: FeatureLayerIR;
}

// ── Data Layer ──

export interface DataLayerIR {
  models: ModelIR[];
}

export interface ModelIR {
  name: string;
  qualifier: string;
  fields: FieldIR[];
  storage?: string;
  sync?: string;
  cache?: string;
  indexes: string[][];
  retention?: string;
  encryption?: string;
}

export interface FieldIR {
  name: string;
  type: string;
  nullable: boolean;
  isPrimary: boolean;
  isUnique: boolean;
  isIndexed: boolean;
  defaultValue?: string;
  foreignKey?: { model: string; field: string };
  enumValues?: string[];
}

// ── Logic Layer ──

export interface LogicLayerIR {
  controllers: ControllerIR[];
}

export interface ControllerIR {
  name: string;
  /** Models this controller operates on */
  modelBindings: { paramName: string; modelName: string }[];
  /** Service bindings */
  serviceBindings: { paramName: string; serviceName: string }[];
  events: EventHandlerIR[];
  methods: MethodIR[];
  variables: { name: string; type: string; defaultValue?: string }[];
}

export interface EventHandlerIR {
  name: string;
  trigger: string;           // e.g., "tickets.purchase_requested"
  params: { name: string; type: string }[];
  /** Serialized representation of the handler body for agent consumption */
  bodySource: string;
  /** Analyzed operations: what models are read/written */
  operations: OperationIR[];
}

export interface MethodIR {
  name: string;
  params: { name: string; type: string }[];
  returnType?: string;
  bodySource: string;
}

export interface OperationIR {
  kind: "create" | "read" | "update" | "delete" | "query";
  model: string;
  fields?: string[];
}

// ── Presentation Layer ──

export interface PresentationLayerIR {
  views: ViewIR[];
}

export interface ViewIR {
  name: string;
  /** The data type this view displays */
  displays?: string;
  /** Target platforms */
  platforms: string[];
  /** Natural language behavior specifications — the agent prompt */
  behaviors: string[];
  /** Style properties */
  style: Record<string, string>;
  /** UI component tree */
  components: ComponentIR[];
}

export interface ComponentIR {
  name: string;
  type: string;
  props: Record<string, string>;
}

// ── Integration Layer ──

export interface IntegrationLayerIR {
  services: ServiceIR[];
}

export interface ServiceIR {
  name: string;
  provider?: string;
  methods: ServiceMethodIR[];
  events: ServiceEventIR[];
  config: Record<string, string>;
}

export interface ServiceMethodIR {
  name: string;
  params: { name: string; type: string }[];
  returnType?: string;
}

export interface ServiceEventIR {
  name: string;
  params: { name: string; type: string }[];
}

// ── Intelligence Layer ──

export interface IntelligenceLayerIR {
  agents: AgentIR[];
}

export interface AgentIR {
  name: string;
  llmModel?: string;
  behaviors: string[];
  inputs: string[];
  output?: string;
  tools: string[];
  temperature?: number;
  guardrails: string[];
}

// ── Infrastructure Layer ──

export interface InfrastructureLayerIR {
  runtimes: RuntimeIR[];
}

export interface RuntimeIR {
  name: string;
  platform: string;
  language?: string;
  orchestration?: string;
  /** Names of views, controllers, services, models, agents assigned here */
  assignments: {
    views: string[];
    controllers: string[];
    services: string[];
    models: string[];
    agents: string[];
  };
  scaling?: {
    minReplicas?: number;
    maxReplicas?: number;
    cpuThreshold?: number;
  };
  config: Record<string, string>;
  env: Record<string, string>;
}

// ── Communication Layer ──

export interface CommunicationLayerIR {
  flows: FlowIR[];
}

export interface FlowIR {
  name: string;
  edges: FlowEdgeIR[];
}

export interface FlowEdgeIR {
  source: string;
  target: string;
  protocol?: string;
  auth?: string;
  rateLimit?: string;
  events?: string[];
  sharedModels: string[];
}

// ── Feature Ownership Layer ──

export interface FeatureLayerIR {
  features: FeatureIR[];
  /** symbol → owning feature (single owner) */
  ownership: Record<string, string>;
  /** Symbols used by 2+ features — agents must handle with care */
  sharedSymbols: SharedSymbolIR[];
  /** feature → features that would be affected by changes */
  blastRadius: Record<string, string[]>;
  /** Symbols not assigned to any feature */
  unownedSymbols: string[];
}

export interface FeatureIR {
  name: string;
  description?: string;
  /** Symbols this feature primarily owns */
  owns: string[];
  /** Symbols this feature reads from other features (cross-feature deps) */
  uses: string[];
  /** Other features that could break if this feature's code changes */
  affectedFeatures: string[];
}

export interface SharedSymbolIR {
  symbol: string;
  owner: string;
  /** Features that depend on this symbol (besides the owner) */
  usedBy: string[];
  /** The kind of declaration (model, controller, view, etc.) */
  kind: string;
}

// ═══════════════════════════════════════════════════════════════════
// IR Builder — Lowers AnalyzedProgram → SystemIR
// ═══════════════════════════════════════════════════════════════════

export class IRBuilder {
  build(program: AnalyzedProgram): SystemIR {
    const systemName =
      program.system?.name ?? "RavelApp";
    const description = this.findStringProp(
      program.system?.properties,
      "description",
    );
    const version = this.findStringProp(
      program.system?.properties,
      "version",
    );

    return {
      name: systemName,
      description,
      version,
      dataLayer: this.buildDataLayer(program),
      logicLayer: this.buildLogicLayer(program),
      presentationLayer: this.buildPresentationLayer(program),
      integrationLayer: this.buildIntegrationLayer(program),
      intelligenceLayer: this.buildIntelligenceLayer(program),
      infrastructureLayer: this.buildInfrastructureLayer(program),
      communicationLayer: this.buildCommunicationLayer(program),
      featureLayer: this.buildFeatureLayer(program),
    };
  }

  // ── Data Layer ──

  private buildDataLayer(program: AnalyzedProgram): DataLayerIR {
    const models: ModelIR[] = [];

    for (const [, model] of program.models) {
      const fields: FieldIR[] = model.schema.map((f) =>
        this.buildFieldIR(f),
      );

      models.push({
        name: model.name,
        qualifier: model.qualifier ?? "local",
        fields,
        storage: this.findStringProp(model.properties, "storage"),
        sync: this.findStringProp(model.properties, "sync"),
        cache: this.findStringProp(model.properties, "cache"),
        indexes: [],
        retention: this.findStringProp(model.properties, "retention"),
        encryption: this.findStringProp(model.properties, "encryption"),
      });
    }

    return { models };
  }

  private buildFieldIR(field: AST.FieldDecl): FieldIR {
    const annotations = new Set(field.annotations.map((a) => a.name));

    return {
      name: field.name,
      type: this.typeExprToString(field.typeExpr),
      nullable: field.typeExpr.kind === "OptionalTypeExpr",
      isPrimary: annotations.has("primary"),
      isUnique: annotations.has("unique"),
      isIndexed: annotations.has("index"),
      defaultValue: field.defaultValue
        ? this.exprToString(field.defaultValue)
        : undefined,
      foreignKey:
        field.typeExpr.kind === "ForeignKeyTypeExpr"
          ? {
              model: field.typeExpr.refModel,
              field: field.typeExpr.refField,
            }
          : undefined,
      enumValues:
        field.typeExpr.kind === "EnumTypeExpr"
          ? field.typeExpr.members
          : undefined,
    };
  }

  // ── Logic Layer ──

  private buildLogicLayer(program: AnalyzedProgram): LogicLayerIR {
    const controllers: ControllerIR[] = [];

    for (const [, ctrl] of program.controllers) {
      const modelBindings: { paramName: string; modelName: string }[] = [];
      const serviceBindings: { paramName: string; serviceName: string }[] = [];

      for (const param of ctrl.params) {
        const typeName = this.typeExprToString(param.typeExpr);
        if (program.models.has(typeName)) {
          modelBindings.push({ paramName: param.name, modelName: typeName });
        } else if (program.services.has(typeName)) {
          serviceBindings.push({ paramName: param.name, serviceName: typeName });
        }
      }

      const events: EventHandlerIR[] = ctrl.events.map((e) => ({
        name: e.target.join("."),
        trigger: e.target.join("."),
        params: e.params.map((p) => ({
          name: p.name,
          type: this.typeExprToString(p.typeExpr),
        })),
        bodySource: this.statementsToSource(e.body),
        operations: this.analyzeOperations(e.body, program),
      }));

      const methods: MethodIR[] = ctrl.methods.map((m) => ({
        name: m.name,
        params: m.params.map((p) => ({
          name: p.name,
          type: this.typeExprToString(p.typeExpr),
        })),
        returnType: m.returnType
          ? this.typeExprToString(m.returnType)
          : undefined,
        bodySource: this.statementsToSource(m.body),
      }));

      const variables = ctrl.variables.map((v) => ({
        name: v.name,
        type: v.typeExpr ? this.typeExprToString(v.typeExpr) : "any",
        defaultValue: v.value ? this.exprToString(v.value) : undefined,
      }));

      controllers.push({
        name: ctrl.name,
        modelBindings,
        serviceBindings,
        events,
        methods,
        variables,
      });
    }

    return { controllers };
  }

  // ── Presentation Layer ──

  private buildPresentationLayer(
    program: AnalyzedProgram,
  ): PresentationLayerIR {
    const views: ViewIR[] = [];

    for (const [, view] of program.views) {
      views.push({
        name: view.name,
        displays: view.displays
          ? this.typeExprToString(view.displays)
          : undefined,
        platforms: view.platforms,
        behaviors: view.behaviors,
        style: this.propsToRecord(view.style),
        components: view.components.map((c) => ({
          name: c.name,
          type: c.type,
          props: Object.fromEntries(
            c.args
              .filter((a) => a.name)
              .map((a) => [a.name!, this.exprToString(a.value)]),
          ),
        })),
      });
    }

    return { views };
  }

  // ── Integration Layer ──

  private buildIntegrationLayer(
    program: AnalyzedProgram,
  ): IntegrationLayerIR {
    const services: ServiceIR[] = [];

    for (const [, svc] of program.services) {
      services.push({
        name: svc.name,
        provider: svc.provider,
        methods: svc.methods.map((m) => ({
          name: m.name,
          params: m.params.map((p) => ({
            name: p.name,
            type: this.typeExprToString(p.typeExpr),
          })),
          returnType: m.returnType
            ? this.typeExprToString(m.returnType)
            : undefined,
        })),
        events: svc.events.map((e) => ({
          name: e.name,
          params: e.params.map((p) => ({
            name: p.name,
            type: this.typeExprToString(p.typeExpr),
          })),
        })),
        config: this.propsToRecord(svc.config),
      });
    }

    return { services };
  }

  // ── Intelligence Layer ──

  private buildIntelligenceLayer(
    program: AnalyzedProgram,
  ): IntelligenceLayerIR {
    const agents: AgentIR[] = [];

    for (const [, agent] of program.agents) {
      agents.push({
        name: agent.name,
        llmModel: agent.model ? this.exprToString(agent.model) : undefined,
        behaviors: agent.behaviors,
        inputs: agent.inputs.map((t) => this.typeExprToString(t)),
        output: agent.output
          ? this.typeExprToString(agent.output)
          : undefined,
        tools: agent.tools.map((t) => this.exprToString(t)),
        temperature: this.findNumberProp(agent.properties, "temperature"),
        guardrails: [],
      });
    }

    return { agents };
  }

  // ── Infrastructure Layer ──

  private buildInfrastructureLayer(
    program: AnalyzedProgram,
  ): InfrastructureLayerIR {
    const runtimes: RuntimeIR[] = [];

    for (const [, rt] of program.runtimes) {
      runtimes.push({
        name: rt.name,
        platform: rt.platform ? this.exprToString(rt.platform) : "container",
        language: rt.language ? this.exprToString(rt.language) : undefined,
        orchestration: rt.orchestration,
        assignments: {
          views: rt.views,
          controllers: rt.controllers,
          services: rt.services,
          models: rt.models,
          agents: rt.agents,
        },
        scaling: this.extractScaling(rt.scaling),
        config: this.propsToRecord(rt.config),
        env: this.propsToRecord(rt.env),
      });
    }

    return { runtimes };
  }

  // ── Communication Layer ──

  private buildCommunicationLayer(
    program: AnalyzedProgram,
  ): CommunicationLayerIR {
    const flows: FlowIR[] = [];

    for (const [, flow] of program.flows) {
      flows.push({
        name: flow.name,
        edges: flow.rules.map((r) => ({
          source: r.source,
          target: r.target,
          protocol: this.findStringProp(r.properties, "protocol"),
          auth: this.findStringProp(r.properties, "auth"),
          rateLimit: this.findStringProp(r.properties, "rate_limit"),
          events: this.findListProp(r.properties, "events"),
          sharedModels: this.findSharedModels(
            r.source,
            r.target,
            program.flowGraph,
          ),
        })),
      });
    }

    return { flows };
  }

  // ── Feature Ownership Layer ──

  private buildFeatureLayer(program: AnalyzedProgram): FeatureLayerIR {
    const fo = program.featureOwnership;

    const features: FeatureIR[] = [];
    for (const [name, feature] of program.features) {
      features.push({
        name,
        description: feature.description,
        owns: feature.owns,
        uses: feature.uses,
        affectedFeatures: [...(fo.blastRadius.get(name) ?? [])],
      });
    }

    // Build ownership record
    const ownership: Record<string, string> = {};
    for (const [sym, owner] of fo.symbolOwner) {
      ownership[sym] = owner;
    }

    // Build shared symbols list
    const sharedSymbols: SharedSymbolIR[] = [];
    for (const sym of fo.sharedSymbols) {
      const owner = fo.symbolOwner.get(sym) ?? "unowned";
      const users = fo.symbolUsers.get(sym) ?? new Set();
      const usedBy = [...users].filter((u) => u !== owner);

      // Determine symbol kind
      let kind = "unknown";
      if (program.models.has(sym)) kind = "model";
      else if (program.controllers.has(sym)) kind = "controller";
      else if (program.views.has(sym)) kind = "view";
      else if (program.services.has(sym)) kind = "service";
      else if (program.agents.has(sym)) kind = "agent";

      sharedSymbols.push({ symbol: sym, owner, usedBy, kind });
    }

    // Build blast radius record
    const blastRadius: Record<string, string[]> = {};
    for (const [feature, affected] of fo.blastRadius) {
      blastRadius[feature] = [...affected];
    }

    return {
      features,
      ownership,
      sharedSymbols,
      blastRadius,
      unownedSymbols: [...fo.unownedSymbols],
    };
  }

  // ─────────────────────── Utilities ─────────────────────────────

  private typeExprToString(type: AST.TypeExpr): string {
    switch (type.kind) {
      case "PrimitiveTypeExpr":
        return type.name;
      case "NamedTypeExpr":
        return type.name;
      case "ArrayTypeExpr":
        return type.size
          ? `${this.typeExprToString(type.element)}[${type.size}]`
          : `${this.typeExprToString(type.element)}[]`;
      case "OptionalTypeExpr":
        return `${this.typeExprToString(type.inner)}?`;
      case "MapTypeExpr":
        return `map<${this.typeExprToString(type.key)}, ${this.typeExprToString(type.value)}>`;
      case "EnumTypeExpr":
        return `enum(${type.members.map((m) => `"${m}"`).join(", ")})`;
      case "ForeignKeyTypeExpr":
        return `${this.typeExprToString(type.type)} -> ${type.refModel}.${type.refField}`;
      case "TupleTypeExpr":
        return `(${type.elements.map((e) => this.typeExprToString(e)).join(", ")})`;
    }
  }

  private exprToString(expr: AST.Expression): string {
    switch (expr.kind) {
      case "IntLiteral":
        return String(expr.value);
      case "FloatLiteral":
        return String(expr.value);
      case "StringLiteral":
        return expr.value;
      case "BoolLiteral":
        return expr.value ? "True" : "False";
      case "NoneLiteral":
        return "None";
      case "Identifier":
        return expr.name;
      case "DurationLiteral":
        return `${expr.value}${expr.unit}`;
      case "SizeLiteral":
        return `${expr.value}${expr.unit}`;
      case "RateLiteral":
        return `${expr.value}/${expr.per}`;
      case "PercentLiteral":
        return `${expr.value}%`;
      case "MemberExpr":
        return `${this.exprToString(expr.object)}.${expr.property}`;
      case "CallExpr":
        return `${this.exprToString(expr.callee)}(${expr.args.map((a) => (a.name ? `${a.name}=${this.exprToString(a.value)}` : this.exprToString(a.value))).join(", ")})`;
      case "ListExpr":
        return `[${expr.elements.map((e) => this.exprToString(e)).join(", ")}]`;
      default:
        return "<expr>";
    }
  }

  private statementsToSource(stmts: AST.Statement[]): string {
    return stmts.map((s) => this.stmtToSource(s, 0)).join("\n");
  }

  private stmtToSource(stmt: AST.Statement, indent: number): string {
    const pad = "    ".repeat(indent);
    switch (stmt.kind) {
      case "AssignmentStmt":
        return `${pad}${this.exprToString(stmt.target)} ${stmt.op} ${this.exprToString(stmt.value)}`;
      case "ExpressionStmt":
        return `${pad}${this.exprToString(stmt.expr)}`;
      case "ReturnStmt":
        return stmt.value
          ? `${pad}return ${this.exprToString(stmt.value)}`
          : `${pad}return`;
      case "IfStmt": {
        let s = `${pad}if ${this.exprToString(stmt.condition)}:\n`;
        s += stmt.body.map((b) => this.stmtToSource(b, indent + 1)).join("\n");
        for (const elif of stmt.elifs) {
          s += `\n${pad}elif ${this.exprToString(elif.condition)}:\n`;
          s += elif.body
            .map((b) => this.stmtToSource(b, indent + 1))
            .join("\n");
        }
        if (stmt.elseBody) {
          s += `\n${pad}else:\n`;
          s += stmt.elseBody
            .map((b) => this.stmtToSource(b, indent + 1))
            .join("\n");
        }
        return s;
      }
      case "ForStmt":
        return `${pad}for ${stmt.variable} in ${this.exprToString(stmt.iterable)}:\n${stmt.body.map((b) => this.stmtToSource(b, indent + 1)).join("\n")}`;
      case "WhileStmt":
        return `${pad}while ${this.exprToString(stmt.condition)}:\n${stmt.body.map((b) => this.stmtToSource(b, indent + 1)).join("\n")}`;
      case "VariableDecl":
        return `${pad}${stmt.name}${stmt.typeExpr ? `: ${this.typeExprToString(stmt.typeExpr)}` : ""}${stmt.value ? ` = ${this.exprToString(stmt.value)}` : ""}`;
      case "EmitStmt":
        return `${pad}emit ${this.exprToString(stmt.event)}`;
      case "LogStmt":
        return `${pad}log ${this.exprToString(stmt.expr)}`;
      case "PassStmt":
        return `${pad}pass`;
      case "BreakStmt":
        return `${pad}break`;
      case "ContinueStmt":
        return `${pad}continue`;
      default:
        return `${pad}<stmt>`;
    }
  }

  private analyzeOperations(
    stmts: AST.Statement[],
    program: AnalyzedProgram,
  ): OperationIR[] {
    const ops: OperationIR[] = [];
    for (const stmt of stmts) {
      this.collectOperations(stmt, program, ops);
    }
    return ops;
  }

  private collectOperations(
    stmt: AST.Statement,
    program: AnalyzedProgram,
    ops: OperationIR[],
  ): void {
    if (stmt.kind === "ExpressionStmt" || stmt.kind === "AssignmentStmt") {
      const expr =
        stmt.kind === "ExpressionStmt" ? stmt.expr : stmt.value;
      this.findCallOperations(expr, program, ops);
    }
    if (stmt.kind === "IfStmt") {
      stmt.body.forEach((s) => this.collectOperations(s, program, ops));
      stmt.elifs.forEach((e) =>
        e.body.forEach((s) => this.collectOperations(s, program, ops)),
      );
      stmt.elseBody?.forEach((s) =>
        this.collectOperations(s, program, ops),
      );
    }
    if (stmt.kind === "ForStmt" || stmt.kind === "WhileStmt") {
      stmt.body.forEach((s) => this.collectOperations(s, program, ops));
    }
  }

  private findCallOperations(
    expr: AST.Expression,
    program: AnalyzedProgram,
    ops: OperationIR[],
  ): void {
    if (expr.kind === "CallExpr" && expr.callee.kind === "MemberExpr") {
      const method = expr.callee.property;
      const obj = expr.callee.object;
      if (obj.kind === "Identifier" || obj.kind === "MemberExpr") {
        const modelName =
          obj.kind === "Identifier" ? obj.name : this.exprToString(obj);
        if (method === "create") ops.push({ kind: "create", model: modelName });
        if (method === "get" || method === "first" || method === "last" || method === "all")
          ops.push({ kind: "read", model: modelName });
        if (method === "save") ops.push({ kind: "update", model: modelName });
        if (method === "delete") ops.push({ kind: "delete", model: modelName });
      }
    }
  }

  private propsToRecord(props?: AST.PropertyAssignment[]): Record<string, string> {
    if (!props) return {};
    const result: Record<string, string> = {};
    for (const p of props) {
      result[p.name] = this.exprToString(p.value);
    }
    return result;
  }

  private findStringProp(
    props: AST.PropertyAssignment[] | undefined,
    name: string,
  ): string | undefined {
    if (!props) return undefined;
    const prop = props.find((p) => p.name === name);
    if (!prop) return undefined;
    return this.exprToString(prop.value);
  }

  private findNumberProp(
    props: AST.PropertyAssignment[] | undefined,
    name: string,
  ): number | undefined {
    if (!props) return undefined;
    const prop = props.find((p) => p.name === name);
    if (!prop) return undefined;
    if (prop.value.kind === "IntLiteral") return prop.value.value;
    if (prop.value.kind === "FloatLiteral") return prop.value.value;
    return undefined;
  }

  private findListProp(
    props: AST.PropertyAssignment[] | undefined,
    name: string,
  ): string[] | undefined {
    if (!props) return undefined;
    const prop = props.find((p) => p.name === name);
    if (!prop) return undefined;
    if (prop.value.kind === "ListExpr") {
      return prop.value.elements.map((e) => this.exprToString(e));
    }
    return undefined;
  }

  private extractScaling(
    props: AST.PropertyAssignment[],
  ): RuntimeIR["scaling"] | undefined {
    if (props.length === 0) return undefined;
    return {
      minReplicas: this.findNumberProp(props, "min_replicas"),
      maxReplicas: this.findNumberProp(props, "max_replicas"),
      cpuThreshold: this.findNumberProp(props, "cpu_threshold"),
    };
  }

  private findSharedModels(
    source: string,
    target: string,
    flowGraph: FlowEdge[],
  ): string[] {
    const edge = flowGraph.find(
      (e) => e.source === source && e.target === target,
    );
    return edge?.models ?? [];
  }
}

/**
 * Ravel v2 Semantic Analyzer
 *
 * Walks the AST to perform:
 *   1. Symbol resolution — build a symbol table of all declarations
 *   2. Type checking — validate type consistency
 *   3. Reference validation — ensure all referenced names exist
 *   4. Flow analysis — validate data flow between runtimes
 *   5. Security analysis — determine encryption/auth requirements
 *
 * Produces a validated, enriched AST (RavelProgram) ready for IR lowering.
 * Follows the same multi-pass approach as the original Ravel compiler
 * (DefPhase → ValidateScope → FlowAnalysis → SecurityAnalysis).
 */

import * as AST from "./ast.js";
import { CompilerDiagnostics, AnalysisError } from "./errors.js";

// ═══════════════════════════════════════════════════════════════════
// Symbol Table
// ═══════════════════════════════════════════════════════════════════

export type SymbolKind =
  | "system"
  | "model"
  | "controller"
  | "view"
  | "service"
  | "runtime"
  | "agent"
  | "flow"
  | "feature"
  | "field"
  | "parameter"
  | "variable"
  | "method"
  | "event";

export interface Symbol {
  name: string;
  kind: SymbolKind;
  node: AST.ASTNode;
  scope: Scope;
}

export class Scope {
  private symbols = new Map<string, Symbol>();
  public children: Scope[] = [];

  constructor(
    public name: string,
    public parent?: Scope,
  ) {
    parent?.children.push(this);
  }

  define(sym: Symbol): void {
    if (this.symbols.has(sym.name)) {
      throw new AnalysisError(
        `Duplicate symbol '${sym.name}' in scope '${this.name}'`,
        sym.node.loc,
      );
    }
    this.symbols.set(sym.name, sym);
  }

  resolve(name: string): Symbol | undefined {
    return this.symbols.get(name) ?? this.parent?.resolve(name);
  }

  resolveLocal(name: string): Symbol | undefined {
    return this.symbols.get(name);
  }

  allSymbols(): Symbol[] {
    return Array.from(this.symbols.values());
  }
}

// ═══════════════════════════════════════════════════════════════════
// Analyzed Program
// ═══════════════════════════════════════════════════════════════════

export interface AnalyzedProgram {
  ast: AST.Program;
  globalScope: Scope;
  models: Map<string, AST.ModelDecl>;
  controllers: Map<string, AST.ControllerDecl>;
  views: Map<string, AST.ViewDecl>;
  services: Map<string, AST.ServiceDecl>;
  runtimes: Map<string, AST.RuntimeDecl>;
  agents: Map<string, AST.AgentDecl>;
  flows: Map<string, AST.FlowDecl>;
  features: Map<string, AST.FeatureDecl>;
  system?: AST.SystemDecl;
  /** Which runtimes write to which models */
  modelWriters: Map<string, Set<string>>;
  /** Which runtimes read from which models */
  modelReaders: Map<string, Set<string>>;
  /** Data flow graph: runtime → runtime edges with metadata */
  flowGraph: FlowEdge[];
  /** Feature ownership analysis results */
  featureOwnership: FeatureOwnershipMap;
  diagnostics: CompilerDiagnostics;
}

/**
 * Feature Ownership Analysis
 *
 * Tracks which feature owns each symbol, which symbols are shared
 * across features, and the blast-radius graph for safe code generation.
 */
export interface FeatureOwnershipMap {
  /** symbol name → owning feature name (primary owner) */
  symbolOwner: Map<string, string>;
  /** symbol name → set of features that use (depend on) this symbol */
  symbolUsers: Map<string, Set<string>>;
  /** Symbols owned by multiple features (conflict — should be a warning) */
  multiOwned: Map<string, string[]>;
  /** Symbols used by 2+ features (shared boundary — require careful handling) */
  sharedSymbols: Set<string>;
  /** feature name → set of other features that could be affected by changes */
  blastRadius: Map<string, Set<string>>;
  /** feature name → symbols it owns */
  featureSymbols: Map<string, Set<string>>;
  /** feature name → symbols it uses but doesn't own */
  featureDependencies: Map<string, Set<string>>;
  /** Symbols with no feature assignment (unowned) */
  unownedSymbols: Set<string>;
}

export interface FlowEdge {
  source: string;
  target: string;
  models: string[];
  protocol?: string;
  auth?: string;
}

// ═══════════════════════════════════════════════════════════════════
// Analyzer
// ═══════════════════════════════════════════════════════════════════

export class Analyzer {
  private globalScope: Scope;
  private diagnostics: CompilerDiagnostics;

  private models = new Map<string, AST.ModelDecl>();
  private controllers = new Map<string, AST.ControllerDecl>();
  private views = new Map<string, AST.ViewDecl>();
  private services = new Map<string, AST.ServiceDecl>();
  private runtimes = new Map<string, AST.RuntimeDecl>();
  private agents = new Map<string, AST.AgentDecl>();
  private flows = new Map<string, AST.FlowDecl>();
  private features = new Map<string, AST.FeatureDecl>();
  private system?: AST.SystemDecl;

  constructor() {
    this.globalScope = new Scope("global");
    this.diagnostics = new CompilerDiagnostics();
  }

  analyze(program: AST.Program): AnalyzedProgram {
    // ── Pass 1: Define all top-level symbols ──
    this.definePass(program);

    // ── Pass 2: Validate references ──
    this.validatePass();

    // ── Pass 3: Flow analysis ──
    const { modelWriters, modelReaders, flowGraph } = this.analyzeFlows();

    // ── Pass 4: Feature ownership analysis ──
    const featureOwnership = this.analyzeFeatureOwnership();

    return {
      ast: program,
      globalScope: this.globalScope,
      models: this.models,
      controllers: this.controllers,
      views: this.views,
      services: this.services,
      runtimes: this.runtimes,
      agents: this.agents,
      flows: this.flows,
      features: this.features,
      system: this.system,
      modelWriters,
      modelReaders,
      flowGraph,
      featureOwnership,
      diagnostics: this.diagnostics,
    };
  }

  // ── Pass 1: Define ──

  private definePass(program: AST.Program): void {
    for (const decl of program.declarations) {
      switch (decl.kind) {
        case "SystemDecl":
          if (this.system) {
            this.diagnostics.warn(
              "Multiple system declarations; using the last one",
              decl.loc,
            );
          }
          this.system = decl;
          this.globalScope.define({
            name: decl.name,
            kind: "system",
            node: decl,
            scope: this.globalScope,
          });
          break;

        case "ModelDecl":
          this.models.set(decl.name, decl);
          this.defineModel(decl);
          break;

        case "ControllerDecl":
          this.controllers.set(decl.name, decl);
          this.globalScope.define({
            name: decl.name,
            kind: "controller",
            node: decl,
            scope: this.globalScope,
          });
          break;

        case "ViewDecl":
          this.views.set(decl.name, decl);
          this.globalScope.define({
            name: decl.name,
            kind: "view",
            node: decl,
            scope: this.globalScope,
          });
          break;

        case "ServiceDecl":
          this.services.set(decl.name, decl);
          this.globalScope.define({
            name: decl.name,
            kind: "service",
            node: decl,
            scope: this.globalScope,
          });
          break;

        case "RuntimeDecl":
          this.runtimes.set(decl.name, decl);
          this.globalScope.define({
            name: decl.name,
            kind: "runtime",
            node: decl,
            scope: this.globalScope,
          });
          break;

        case "AgentDecl":
          this.agents.set(decl.name, decl);
          this.globalScope.define({
            name: decl.name,
            kind: "agent",
            node: decl,
            scope: this.globalScope,
          });
          break;

        case "FlowDecl":
          this.flows.set(decl.name, decl);
          this.globalScope.define({
            name: decl.name,
            kind: "flow",
            node: decl,
            scope: this.globalScope,
          });
          break;

        case "FeatureDecl":
          this.features.set(decl.name, decl);
          this.globalScope.define({
            name: decl.name,
            kind: "feature",
            node: decl,
            scope: this.globalScope,
          });
          break;

        case "ImportDecl":
          // Imports are resolved at a higher level
          break;
      }
    }
  }

  private defineModel(decl: AST.ModelDecl): void {
    const modelScope = new Scope(decl.name, this.globalScope);
    this.globalScope.define({
      name: decl.name,
      kind: "model",
      node: decl,
      scope: modelScope,
    });

    for (const field of decl.schema) {
      modelScope.define({
        name: field.name,
        kind: "field",
        node: field,
        scope: modelScope,
      });
    }
  }

  // ── Pass 2: Validate ──

  private validatePass(): void {
    // Validate runtime references
    for (const [name, rt] of this.runtimes) {
      for (const ctrlName of rt.controllers) {
        if (!this.controllers.has(ctrlName)) {
          this.diagnostics.error(
            `Runtime '${name}' references unknown controller '${ctrlName}'`,
            rt.loc,
          );
        }
      }
      for (const viewName of rt.views) {
        if (!this.views.has(viewName)) {
          this.diagnostics.error(
            `Runtime '${name}' references unknown view '${viewName}'`,
            rt.loc,
          );
        }
      }
      for (const svcName of rt.services) {
        if (!this.services.has(svcName)) {
          this.diagnostics.error(
            `Runtime '${name}' references unknown service '${svcName}'`,
            rt.loc,
          );
        }
      }
      for (const modelName of rt.models) {
        if (!this.models.has(modelName)) {
          this.diagnostics.error(
            `Runtime '${name}' references unknown model '${modelName}'`,
            rt.loc,
          );
        }
      }
      for (const agentName of rt.agents) {
        if (!this.agents.has(agentName)) {
          this.diagnostics.error(
            `Runtime '${name}' references unknown agent '${agentName}'`,
            rt.loc,
          );
        }
      }
    }

    // Validate controller parameter types reference known models
    for (const [name, ctrl] of this.controllers) {
      for (const param of ctrl.params) {
        const typeName = this.resolveTypeName(param.typeExpr);
        if (typeName && !this.models.has(typeName) && !this.services.has(typeName)) {
          this.diagnostics.warn(
            `Controller '${name}' parameter '${param.name}' references type '${typeName}' which is not a known model or service`,
            param.loc,
          );
        }
      }
    }

    // Validate feature references
    for (const [name, feature] of this.features) {
      const allSymbols = new Set([
        ...this.models.keys(),
        ...this.controllers.keys(),
        ...this.views.keys(),
        ...this.services.keys(),
        ...this.agents.keys(),
      ]);
      for (const owned of feature.owns) {
        if (!allSymbols.has(owned)) {
          this.diagnostics.error(
            `Feature '${name}' owns unknown symbol '${owned}'`,
            feature.loc,
          );
        }
      }
      for (const used of feature.uses) {
        if (!allSymbols.has(used)) {
          this.diagnostics.error(
            `Feature '${name}' uses unknown symbol '${used}'`,
            feature.loc,
          );
        }
      }
    }

    // Validate flow endpoints reference known runtimes
    for (const [name, flow] of this.flows) {
      for (const rule of flow.rules) {
        if (!this.runtimes.has(rule.source)) {
          this.diagnostics.error(
            `Flow '${name}' references unknown runtime '${rule.source}'`,
            rule.loc,
          );
        }
        if (!this.runtimes.has(rule.target)) {
          this.diagnostics.error(
            `Flow '${name}' references unknown runtime '${rule.target}'`,
            rule.loc,
          );
        }
      }
    }
  }

  private resolveTypeName(type: AST.TypeExpr): string | undefined {
    switch (type.kind) {
      case "NamedTypeExpr":
        return type.name;
      case "ArrayTypeExpr":
        return this.resolveTypeName(type.element);
      case "OptionalTypeExpr":
        return this.resolveTypeName(type.inner);
      default:
        return undefined;
    }
  }

  // ── Pass 3: Flow Analysis ──

  private analyzeFlows(): {
    modelWriters: Map<string, Set<string>>;
    modelReaders: Map<string, Set<string>>;
    flowGraph: FlowEdge[];
  } {
    const modelWriters = new Map<string, Set<string>>();
    const modelReaders = new Map<string, Set<string>>();
    const flowGraph: FlowEdge[] = [];

    // Determine which runtimes interact with which models via controllers
    for (const [rtName, rt] of this.runtimes) {
      for (const ctrlName of rt.controllers) {
        const ctrl = this.controllers.get(ctrlName);
        if (!ctrl) continue;

        for (const param of ctrl.params) {
          const modelName = this.resolveTypeName(param.typeExpr);
          if (modelName && this.models.has(modelName)) {
            // Assume controllers both read and write their models
            if (!modelWriters.has(modelName)) modelWriters.set(modelName, new Set());
            if (!modelReaders.has(modelName)) modelReaders.set(modelName, new Set());
            modelWriters.get(modelName)!.add(rtName);
            modelReaders.get(modelName)!.add(rtName);
          }
        }
      }

      for (const modelName of rt.models) {
        if (!modelReaders.has(modelName)) modelReaders.set(modelName, new Set());
        modelReaders.get(modelName)!.add(rtName);
      }
    }

    // Build flow graph from explicit flow declarations
    for (const [, flow] of this.flows) {
      for (const rule of flow.rules) {
        const protocol = this.findPropValue(rule.properties, "protocol");
        const auth = this.findPropValue(rule.properties, "auth");

        // Find shared models between source and target
        const sharedModels: string[] = [];
        for (const [modelName, writers] of modelWriters) {
          const readers = modelReaders.get(modelName);
          if (writers.has(rule.source) && readers?.has(rule.target)) {
            sharedModels.push(modelName);
          }
        }

        flowGraph.push({
          source: rule.source,
          target: rule.target,
          models: sharedModels,
          protocol,
          auth,
        });
      }
    }

    return { modelWriters, modelReaders, flowGraph };
  }

  // ── Pass 4: Feature Ownership Analysis ──

  private analyzeFeatureOwnership(): FeatureOwnershipMap {
    const symbolOwner = new Map<string, string>();
    const symbolUsers = new Map<string, Set<string>>();
    const multiOwned = new Map<string, string[]>();
    const sharedSymbols = new Set<string>();
    const blastRadius = new Map<string, Set<string>>();
    const featureSymbols = new Map<string, Set<string>>();
    const featureDependencies = new Map<string, Set<string>>();

    // Initialize per-feature tracking
    for (const [featureName] of this.features) {
      featureSymbols.set(featureName, new Set());
      featureDependencies.set(featureName, new Set());
      blastRadius.set(featureName, new Set());
    }

    // Build ownership map from `owns` declarations
    for (const [featureName, feature] of this.features) {
      for (const sym of feature.owns) {
        featureSymbols.get(featureName)!.add(sym);

        if (symbolOwner.has(sym)) {
          // Multi-ownership conflict
          if (!multiOwned.has(sym)) {
            multiOwned.set(sym, [symbolOwner.get(sym)!]);
          }
          multiOwned.get(sym)!.push(featureName);
          this.diagnostics.warn(
            `Symbol '${sym}' is owned by multiple features: ${multiOwned.get(sym)!.join(", ")}. Consider splitting shared logic.`,
            feature.loc,
          );
        } else {
          symbolOwner.set(sym, featureName);
        }
      }
    }

    // Build usage map from `uses` declarations
    for (const [featureName, feature] of this.features) {
      for (const sym of feature.uses) {
        featureDependencies.get(featureName)!.add(sym);

        if (!symbolUsers.has(sym)) {
          symbolUsers.set(sym, new Set());
        }
        symbolUsers.get(sym)!.add(featureName);
      }
    }

    // Also add implicit ownership from `owns` to symbolUsers
    // (the owning feature also "uses" the symbol)
    for (const [featureName, feature] of this.features) {
      for (const sym of feature.owns) {
        if (!symbolUsers.has(sym)) {
          symbolUsers.set(sym, new Set());
        }
        symbolUsers.get(sym)!.add(featureName);
      }
    }

    // Infer additional cross-feature dependencies from controllers
    // (if a controller references models owned by different features)
    for (const [, ctrl] of this.controllers) {
      const ctrlFeature = symbolOwner.get(ctrl.name);
      if (!ctrlFeature) continue;

      for (const param of ctrl.params) {
        const modelName = this.resolveTypeName(param.typeExpr);
        if (modelName && symbolOwner.has(modelName)) {
          const modelFeature = symbolOwner.get(modelName)!;
          if (modelFeature !== ctrlFeature) {
            // This controller implicitly depends on another feature's model
            featureDependencies.get(ctrlFeature)!.add(modelName);
            if (!symbolUsers.has(modelName)) {
              symbolUsers.set(modelName, new Set());
            }
            symbolUsers.get(modelName)!.add(ctrlFeature);
          }
        }
      }
    }

    // Identify shared symbols (used by 2+ features)
    for (const [sym, users] of symbolUsers) {
      if (users.size >= 2) {
        sharedSymbols.add(sym);
      }
    }

    // Compute blast radius: if feature X changes a symbol, which
    // other features could be affected?
    for (const [featureName, feature] of this.features) {
      const affected = blastRadius.get(featureName)!;
      // Every symbol this feature owns could affect features that use it
      for (const sym of feature.owns) {
        const users = symbolUsers.get(sym);
        if (users) {
          for (const user of users) {
            if (user !== featureName) {
              affected.add(user);
            }
          }
        }
      }
    }

    // Find unowned symbols
    const allDeclarationNames = new Set<string>([
      ...this.models.keys(),
      ...this.controllers.keys(),
      ...this.views.keys(),
      ...this.services.keys(),
      ...this.agents.keys(),
    ]);
    const unownedSymbols = new Set<string>();
    if (this.features.size > 0) {
      for (const name of allDeclarationNames) {
        if (!symbolOwner.has(name)) {
          unownedSymbols.add(name);
        }
      }
      if (unownedSymbols.size > 0) {
        this.diagnostics.warn(
          `Unowned symbols (not assigned to any feature): ${[...unownedSymbols].join(", ")}. Consider assigning them to a feature for blast-radius tracking.`,
          this.system?.loc ?? { line: 1, column: 1 },
        );
      }
    }

    return {
      symbolOwner,
      symbolUsers,
      multiOwned,
      sharedSymbols,
      blastRadius,
      featureSymbols,
      featureDependencies,
      unownedSymbols,
    };
  }

  private findPropValue(
    props: AST.PropertyAssignment[],
    name: string,
  ): string | undefined {
    const prop = props.find((p) => p.name === name);
    if (!prop) return undefined;
    if (prop.value.kind === "Identifier") return prop.value.name;
    if (prop.value.kind === "StringLiteral") return prop.value.value;
    return undefined;
  }
}

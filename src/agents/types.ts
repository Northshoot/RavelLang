/**
 * Ravel v2 Agent Type System
 *
 * Defines the types for the LLM agent orchestration system.
 * Agents are the "compilation backend" of modern Ravel — instead of
 * template-based code generation (StringTemplate 4 in original Ravel),
 * we generate Agent Execution Plans that LLM agents carry out.
 *
 * Each agent specializes in one aspect of system construction:
 *
 *   Agent                Original Ravel Equivalent
 *   ─────                ─────────────────────────
 *   DatabaseAgent     →  ModelCompiler + model.stg templates
 *   BackendAgent      →  ControllerCompiler + controller.stg
 *   iOSAgent          →  N/A (new)
 *   AndroidAgent      →  Android platform + JLang code gen
 *   WebAgent          →  N/A (new)
 *   InfraAgent        →  PlatformBuilder + platform templates
 *   APIAgent          →  InterfaceCompiler + interface.stg
 *   SecurityAgent     →  SecurityAnalysis + SecurityTransformation
 *   TestAgent         →  N/A (new)
 *   OrchestrationAgent→  RavelCompiler (the compiler itself)
 */

import type { SystemIR } from "../compiler/ir.js";

// ═══════════════════════════════════════════════════════════════════
// Agent Execution Plan
// ═══════════════════════════════════════════════════════════════════

/**
 * The Agent Execution Plan is the "compiled output" of Ravel.
 * It describes all the tasks that agents need to perform to build
 * the complete system.
 */
export interface AgentExecutionPlan {
  /** System metadata */
  system: {
    name: string;
    description?: string;
    version?: string;
  };

  /** Ordered phases of execution */
  phases: ExecutionPhase[];

  /** The full SystemIR for context */
  ir: SystemIR;

  /** Generated at compilation time */
  generatedAt: string;
  compilerVersion: string;
}

export interface ExecutionPhase {
  name: string;
  description: string;
  /** Tasks can run in parallel within a phase */
  tasks: AgentTask[];
  /** Phases execute sequentially; dependencies are implicit in ordering */
  dependsOn?: string[];
}

// ═══════════════════════════════════════════════════════════════════
// Agent Tasks
// ═══════════════════════════════════════════════════════════════════

export interface AgentTask {
  id: string;
  agent: AgentType;
  description: string;
  /** The structured prompt/context for the agent */
  context: AgentContext;
  /** Expected outputs */
  outputs: ExpectedOutput[];
  /** Priority within the phase (lower = higher priority) */
  priority: number;
  /** Feature ownership annotations — which features this task touches */
  featureScope?: FeatureScope;
}

/**
 * Feature scope annotations for a task.
 * Tells the agent exactly which features it's working on,
 * what's shared, and what it must not break.
 */
export interface FeatureScope {
  /** Features this task primarily serves */
  primaryFeatures: string[];
  /** Symbols that are shared across features — handle with care */
  sharedSymbols: SharedSymbolWarning[];
  /** Other features that could break if this task's output changes shared code */
  blastRadius: string[];
  /** Per-symbol ownership: which feature owns each symbol this task touches */
  symbolOwnership: Record<string, string>;
}

export interface SharedSymbolWarning {
  symbol: string;
  owner: string;
  usedBy: string[];
  warning: string;
}

export type AgentType =
  | "database"
  | "backend"
  | "ios"
  | "android"
  | "web"
  | "infrastructure"
  | "api"
  | "security"
  | "testing"
  | "documentation";

export interface AgentContext {
  /** The specific IR slice this agent needs */
  relevantIR: Record<string, unknown>;
  /** Natural language instructions */
  instructions: string[];
  /** Constraints the agent must satisfy */
  constraints: string[];
  /** Reference material (other generated artifacts the agent depends on) */
  dependencies: string[];
  /** Technology-specific requirements */
  techStack: TechStackRequirement[];
}

export interface TechStackRequirement {
  category: string;
  technology: string;
  version?: string;
  reason: string;
}

export interface ExpectedOutput {
  type: OutputType;
  path: string;
  description: string;
}

export type OutputType =
  | "source_code"
  | "config"
  | "schema"
  | "migration"
  | "dockerfile"
  | "kubernetes_manifest"
  | "api_spec"
  | "test"
  | "documentation";

// ═══════════════════════════════════════════════════════════════════
// Agent Capabilities Registry
// ═══════════════════════════════════════════════════════════════════

export interface AgentCapability {
  type: AgentType;
  name: string;
  description: string;
  /** What IR layers this agent consumes */
  inputLayers: string[];
  /** What kinds of files this agent produces */
  outputTypes: OutputType[];
  /** Supported platforms/technologies */
  supportedPlatforms: string[];
}

export const AGENT_CAPABILITIES: AgentCapability[] = [
  {
    type: "database",
    name: "Database Architect Agent",
    description:
      "Generates database schemas, migrations, ORM models, and data access layers",
    inputLayers: ["dataLayer"],
    outputTypes: ["schema", "migration", "source_code"],
    supportedPlatforms: ["postgres", "mysql", "mongodb", "redis", "sqlite"],
  },
  {
    type: "backend",
    name: "Backend Engineer Agent",
    description:
      "Generates server-side code: API handlers, business logic, middleware, auth",
    inputLayers: ["logicLayer", "dataLayer", "integrationLayer"],
    outputTypes: ["source_code", "config"],
    supportedPlatforms: [
      "node",
      "typescript",
      "python",
      "go",
      "rust",
      "java",
    ],
  },
  {
    type: "ios",
    name: "iOS Engineer Agent",
    description:
      "Generates Swift/SwiftUI code for iOS applications",
    inputLayers: ["presentationLayer", "logicLayer", "communicationLayer"],
    outputTypes: ["source_code", "config"],
    supportedPlatforms: ["ios", "swiftui", "uikit"],
  },
  {
    type: "android",
    name: "Android Engineer Agent",
    description:
      "Generates Kotlin/Jetpack Compose code for Android applications",
    inputLayers: ["presentationLayer", "logicLayer", "communicationLayer"],
    outputTypes: ["source_code", "config"],
    supportedPlatforms: ["android", "compose", "kotlin"],
  },
  {
    type: "web",
    name: "Web Frontend Agent",
    description:
      "Generates web frontend code: React/Vue/Svelte components, styles, routing",
    inputLayers: ["presentationLayer", "logicLayer", "communicationLayer"],
    outputTypes: ["source_code", "config"],
    supportedPlatforms: ["react", "vue", "svelte", "nextjs"],
  },
  {
    type: "infrastructure",
    name: "Infrastructure Agent",
    description:
      "Generates Docker, Kubernetes, Terraform, CI/CD configs",
    inputLayers: ["infrastructureLayer", "communicationLayer"],
    outputTypes: [
      "dockerfile",
      "kubernetes_manifest",
      "config",
    ],
    supportedPlatforms: [
      "docker",
      "kubernetes",
      "terraform",
      "aws",
      "gcp",
      "azure",
    ],
  },
  {
    type: "api",
    name: "API Contract Agent",
    description:
      "Generates OpenAPI/GraphQL specs and client SDKs from the communication layer",
    inputLayers: [
      "communicationLayer",
      "dataLayer",
      "integrationLayer",
    ],
    outputTypes: ["api_spec", "source_code"],
    supportedPlatforms: ["openapi", "graphql", "grpc", "rest"],
  },
  {
    type: "security",
    name: "Security Agent",
    description:
      "Analyzes the system for vulnerabilities and generates security configs",
    inputLayers: [
      "communicationLayer",
      "dataLayer",
      "logicLayer",
      "infrastructureLayer",
    ],
    outputTypes: ["config", "source_code"],
    supportedPlatforms: ["oauth", "jwt", "tls", "encryption"],
  },
  {
    type: "testing",
    name: "Testing Agent",
    description:
      "Generates test suites: unit, integration, e2e for all layers",
    inputLayers: [
      "dataLayer",
      "logicLayer",
      "presentationLayer",
      "communicationLayer",
    ],
    outputTypes: ["test"],
    supportedPlatforms: ["jest", "pytest", "xctest", "espresso", "playwright"],
  },
  {
    type: "documentation",
    name: "Documentation Agent",
    description:
      "Generates API docs, architecture diagrams, runbooks, and READMEs",
    inputLayers: [
      "dataLayer",
      "logicLayer",
      "communicationLayer",
      "infrastructureLayer",
    ],
    outputTypes: ["documentation"],
    supportedPlatforms: ["markdown", "openapi", "mermaid"],
  },
];

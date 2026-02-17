/**
 * Ravel v2 Agent Planner
 *
 * Transforms a SystemIR into an AgentExecutionPlan.
 *
 * This is the modern equivalent of PlatformBuilder in the original Ravel
 * compiler. Where PlatformBuilder iterated over spaces and called CLang/JLang
 * to generate platform-specific code files, the Planner iterates over the
 * SystemIR layers and generates tasks for LLM agents.
 *
 * The planning follows a layered architecture:
 *   Phase 1: Foundation   — API contracts, database schemas
 *   Phase 2: Core         — Backend services, business logic
 *   Phase 3: Clients      — Mobile apps, web frontends
 *   Phase 4: Intelligence  — AI agent integrations
 *   Phase 5: Infrastructure— Docker, K8s, CI/CD
 *   Phase 6: Quality      — Security audit, tests, docs
 */

import type { SystemIR, RuntimeIR } from "../compiler/ir.js";
import type {
  AgentExecutionPlan,
  ExecutionPhase,
  AgentTask,
  AgentContext,
  AgentType,
  ExpectedOutput,
  OutputType,
  TechStackRequirement,
} from "./types.js";

export class Planner {
  private taskCounter = 0;

  plan(ir: SystemIR): AgentExecutionPlan {
    this.taskCounter = 0;

    const phases: ExecutionPhase[] = [
      this.planFoundationPhase(ir),
      this.planCorePhase(ir),
      this.planClientPhase(ir),
      this.planIntelligencePhase(ir),
      this.planInfrastructurePhase(ir),
      this.planQualityPhase(ir),
    ].filter((p) => p.tasks.length > 0);

    return {
      system: {
        name: ir.name,
        description: ir.description,
        version: ir.version,
      },
      phases,
      ir,
      generatedAt: new Date().toISOString(),
      compilerVersion: "2.0.0",
    };
  }

  // ── Phase 1: Foundation ──

  private planFoundationPhase(ir: SystemIR): ExecutionPhase {
    const tasks: AgentTask[] = [];

    // API contract generation
    if (ir.communicationLayer.flows.length > 0 || ir.dataLayer.models.length > 0) {
      tasks.push(
        this.createTask("api", "Generate API contracts and type definitions", {
          relevantIR: {
            models: ir.dataLayer.models,
            flows: ir.communicationLayer.flows,
            services: ir.integrationLayer.services,
          },
          instructions: [
            "Generate OpenAPI 3.1 specification for all API endpoints",
            "Create TypeScript type definitions shared across all clients and server",
            "Define request/response schemas based on the model definitions",
            "Include authentication/authorization requirements from flow specs",
            `System name: ${ir.name}`,
          ],
          constraints: [
            "All endpoints must be RESTful unless explicitly specified as GraphQL/gRPC",
            "Include proper error response schemas",
            "Version the API (v1 prefix)",
          ],
          dependencies: [],
          techStack: [
            { category: "spec", technology: "openapi", version: "3.1", reason: "API contract" },
          ],
        }, [
          { type: "api_spec", path: "api/openapi.yaml", description: "OpenAPI specification" },
          { type: "source_code", path: "shared/types/", description: "Shared type definitions" },
        ]),
      );
    }

    // Database schema generation
    if (ir.dataLayer.models.length > 0) {
      tasks.push(
        this.createTask("database", "Generate database schemas and migrations", {
          relevantIR: {
            models: ir.dataLayer.models,
          },
          instructions: [
            "Generate database schema for all models",
            "Create initial migration files",
            "Generate ORM model definitions",
            "Include indexes, constraints, and foreign keys",
            ...ir.dataLayer.models.map(
              (m) =>
                `Model '${m.name}' (${m.qualifier}): ${m.fields.length} fields, storage: ${m.storage ?? "default"}`,
            ),
          ],
          constraints: [
            "Use UUID for primary keys unless specified otherwise",
            "Include created_at and updated_at timestamps automatically",
            "Foreign keys must have proper ON DELETE behavior",
          ],
          dependencies: [],
          techStack: this.inferDatabaseTechStack(ir),
        }, [
          { type: "schema", path: "database/schema.sql", description: "Database schema" },
          { type: "migration", path: "database/migrations/", description: "Migration files" },
          { type: "source_code", path: "backend/models/", description: "ORM models" },
        ]),
      );
    }

    return {
      name: "foundation",
      description: "API contracts, database schemas, and shared types",
      tasks,
    };
  }

  // ── Phase 2: Core ──

  private planCorePhase(ir: SystemIR): ExecutionPhase {
    const tasks: AgentTask[] = [];
    const backendRuntimes = ir.infrastructureLayer.runtimes.filter(
      (r) => this.isBackendRuntime(r),
    );

    for (const runtime of backendRuntimes) {
      const assignedControllers = ir.logicLayer.controllers.filter(
        (c) => runtime.assignments.controllers.includes(c.name),
      );
      const assignedServices = ir.integrationLayer.services.filter(
        (s) => runtime.assignments.services.includes(s.name),
      );

      if (assignedControllers.length === 0 && assignedServices.length === 0) continue;

      tasks.push(
        this.createTask("backend", `Generate backend service for runtime '${runtime.name}'`, {
          relevantIR: {
            runtime,
            controllers: assignedControllers,
            services: assignedServices,
            models: ir.dataLayer.models,
          },
          instructions: [
            `Generate the backend service for runtime '${runtime.name}'`,
            `Language: ${runtime.language ?? "typescript"}`,
            `Platform: ${runtime.platform}`,
            ...assignedControllers.flatMap((c) => [
              `Controller '${c.name}' with ${c.events.length} event handlers:`,
              ...c.events.map(
                (e) => `  - ${e.trigger}: ${e.bodySource.split("\n")[0]}...`,
              ),
            ]),
            ...assignedServices.map(
              (s) =>
                `Service '${s.name}' (provider: ${s.provider ?? "custom"}) with ${s.methods.length} methods`,
            ),
          ],
          constraints: [
            "Implement proper error handling and validation",
            "Use dependency injection for service integrations",
            "Include health check endpoint",
            "Implement rate limiting as specified in flow definitions",
          ],
          dependencies: [
            "api/openapi.yaml",
            "shared/types/",
            "database/schema.sql",
          ],
          techStack: this.inferBackendTechStack(runtime),
        }, [
          { type: "source_code", path: `backend/src/`, description: "Backend source code" },
          { type: "config", path: `backend/package.json`, description: "Backend config" },
        ]),
      );
    }

    return {
      name: "core",
      description: "Backend services, business logic, and service integrations",
      tasks,
      dependsOn: ["foundation"],
    };
  }

  // ── Phase 3: Clients ──

  private planClientPhase(ir: SystemIR): ExecutionPhase {
    const tasks: AgentTask[] = [];

    // Group views by platform
    const viewsByPlatform = new Map<string, typeof ir.presentationLayer.views>();
    for (const view of ir.presentationLayer.views) {
      for (const platform of view.platforms) {
        if (!viewsByPlatform.has(platform)) viewsByPlatform.set(platform, []);
        viewsByPlatform.get(platform)!.push(view);
      }
    }

    // Also check runtimes for platform assignments
    for (const runtime of ir.infrastructureLayer.runtimes) {
      if (this.isIOSRuntime(runtime)) {
        const views = ir.presentationLayer.views.filter(
          (v) => runtime.assignments.views.includes(v.name),
        );
        tasks.push(this.planIOSTask(ir, runtime, views));
      }
      if (this.isAndroidRuntime(runtime)) {
        const views = ir.presentationLayer.views.filter(
          (v) => runtime.assignments.views.includes(v.name),
        );
        tasks.push(this.planAndroidTask(ir, runtime, views));
      }
      if (this.isWebRuntime(runtime)) {
        const views = ir.presentationLayer.views.filter(
          (v) => runtime.assignments.views.includes(v.name),
        );
        tasks.push(this.planWebTask(ir, runtime, views));
      }
    }

    // Also generate from platform-specified views
    if (viewsByPlatform.has("ios") && !tasks.some((t) => t.agent === "ios")) {
      tasks.push(this.planIOSTaskFromViews(ir, viewsByPlatform.get("ios")!));
    }
    if (viewsByPlatform.has("android") && !tasks.some((t) => t.agent === "android")) {
      tasks.push(this.planAndroidTaskFromViews(ir, viewsByPlatform.get("android")!));
    }
    if (viewsByPlatform.has("web") && !tasks.some((t) => t.agent === "web")) {
      tasks.push(this.planWebTaskFromViews(ir, viewsByPlatform.get("web")!));
    }

    return {
      name: "clients",
      description: "Mobile apps, web frontends, and client-side logic",
      tasks,
      dependsOn: ["foundation", "core"],
    };
  }

  private planIOSTask(ir: SystemIR, runtime: RuntimeIR, views: typeof ir.presentationLayer.views): AgentTask {
    return this.createTask("ios", `Generate iOS app for runtime '${runtime.name}'`, {
      relevantIR: { runtime, views, models: ir.dataLayer.models },
      instructions: [
        `Generate a SwiftUI iOS application for runtime '${runtime.name}'`,
        ...views.flatMap((v) => [
          `View '${v.name}' displaying ${v.displays ?? "data"}:`,
          ...v.behaviors.map((b) => `  Behavior: "${b}"`),
        ]),
      ],
      constraints: [
        `Minimum iOS version: ${runtime.config["min_ios"] ?? "16"}`,
        "Use SwiftUI with MVVM architecture",
        "Implement offline-first with local caching",
        "Support dark mode",
      ],
      dependencies: ["api/openapi.yaml", "shared/types/"],
      techStack: [
        { category: "language", technology: "swift", version: "5.9", reason: "iOS development" },
        { category: "framework", technology: "swiftui", reason: "Declarative UI" },
      ],
    }, [
      { type: "source_code", path: "ios/", description: "iOS app source" },
      { type: "config", path: "ios/Package.swift", description: "Swift package" },
    ]);
  }

  private planAndroidTask(ir: SystemIR, runtime: RuntimeIR, views: typeof ir.presentationLayer.views): AgentTask {
    return this.createTask("android", `Generate Android app for runtime '${runtime.name}'`, {
      relevantIR: { runtime, views, models: ir.dataLayer.models },
      instructions: [
        `Generate a Kotlin/Jetpack Compose Android application for runtime '${runtime.name}'`,
        ...views.flatMap((v) => [
          `View '${v.name}' displaying ${v.displays ?? "data"}:`,
          ...v.behaviors.map((b) => `  Behavior: "${b}"`),
        ]),
      ],
      constraints: [
        `Minimum Android SDK: ${runtime.config["min_android"] ?? "26"}`,
        "Use Jetpack Compose with MVVM architecture",
        "Implement offline-first with Room database",
        "Support Material Design 3",
      ],
      dependencies: ["api/openapi.yaml", "shared/types/"],
      techStack: [
        { category: "language", technology: "kotlin", version: "1.9", reason: "Android development" },
        { category: "framework", technology: "compose", reason: "Declarative UI" },
      ],
    }, [
      { type: "source_code", path: "android/", description: "Android app source" },
      { type: "config", path: "android/build.gradle.kts", description: "Gradle build" },
    ]);
  }

  private planWebTask(ir: SystemIR, runtime: RuntimeIR, views: typeof ir.presentationLayer.views): AgentTask {
    return this.createTask("web", `Generate web frontend for runtime '${runtime.name}'`, {
      relevantIR: { runtime, views, models: ir.dataLayer.models },
      instructions: [
        `Generate a web frontend for runtime '${runtime.name}'`,
        ...views.flatMap((v) => [
          `View '${v.name}' displaying ${v.displays ?? "data"}:`,
          ...v.behaviors.map((b) => `  Behavior: "${b}"`),
        ]),
      ],
      constraints: [
        "Use React with TypeScript",
        "Implement responsive design",
        "Support server-side rendering where appropriate",
      ],
      dependencies: ["api/openapi.yaml", "shared/types/"],
      techStack: [
        { category: "framework", technology: "react", version: "19", reason: "Web UI" },
        { category: "language", technology: "typescript", reason: "Type safety" },
      ],
    }, [
      { type: "source_code", path: "web/", description: "Web app source" },
      { type: "config", path: "web/package.json", description: "Web config" },
    ]);
  }

  private planIOSTaskFromViews(ir: SystemIR, views: typeof ir.presentationLayer.views): AgentTask {
    return this.createTask("ios", "Generate iOS app from view declarations", {
      relevantIR: { views, models: ir.dataLayer.models },
      instructions: [
        "Generate a SwiftUI iOS application from the view declarations",
        ...views.flatMap((v) => [
          `View '${v.name}':`,
          ...v.behaviors.map((b) => `  "${b}"`),
        ]),
      ],
      constraints: ["Use SwiftUI with MVVM", "Support iOS 16+"],
      dependencies: ["api/openapi.yaml"],
      techStack: [
        { category: "language", technology: "swift", reason: "iOS" },
      ],
    }, [
      { type: "source_code", path: "ios/", description: "iOS source" },
    ]);
  }

  private planAndroidTaskFromViews(ir: SystemIR, views: typeof ir.presentationLayer.views): AgentTask {
    return this.createTask("android", "Generate Android app from view declarations", {
      relevantIR: { views, models: ir.dataLayer.models },
      instructions: [
        "Generate a Kotlin/Compose Android application from the view declarations",
        ...views.flatMap((v) => [
          `View '${v.name}':`,
          ...v.behaviors.map((b) => `  "${b}"`),
        ]),
      ],
      constraints: ["Use Jetpack Compose", "Support API 26+"],
      dependencies: ["api/openapi.yaml"],
      techStack: [
        { category: "language", technology: "kotlin", reason: "Android" },
      ],
    }, [
      { type: "source_code", path: "android/", description: "Android source" },
    ]);
  }

  private planWebTaskFromViews(ir: SystemIR, views: typeof ir.presentationLayer.views): AgentTask {
    return this.createTask("web", "Generate web frontend from view declarations", {
      relevantIR: { views, models: ir.dataLayer.models },
      instructions: [
        "Generate a React/TypeScript web application from the view declarations",
        ...views.flatMap((v) => [
          `View '${v.name}':`,
          ...v.behaviors.map((b) => `  "${b}"`),
        ]),
      ],
      constraints: ["Use React 19 + TypeScript", "Responsive design"],
      dependencies: ["api/openapi.yaml"],
      techStack: [
        { category: "framework", technology: "react", reason: "Web" },
      ],
    }, [
      { type: "source_code", path: "web/", description: "Web source" },
    ]);
  }

  // ── Phase 4: Intelligence ──

  private planIntelligencePhase(ir: SystemIR): ExecutionPhase {
    const tasks: AgentTask[] = [];

    for (const agent of ir.intelligenceLayer.agents) {
      tasks.push(
        this.createTask("backend", `Integrate AI agent '${agent.name}'`, {
          relevantIR: { agent, models: ir.dataLayer.models },
          instructions: [
            `Implement the AI agent '${agent.name}'`,
            `LLM model: ${agent.llmModel ?? "configurable"}`,
            ...agent.behaviors.map((b) => `Behavior: "${b}"`),
            `Inputs: ${agent.inputs.join(", ") || "none specified"}`,
            `Output: ${agent.output ?? "none specified"}`,
          ],
          constraints: [
            "Implement proper prompt engineering",
            "Include rate limiting and cost controls",
            "Add fallback behavior for LLM failures",
            ...agent.guardrails.map((g) => `Guardrail: ${g}`),
          ],
          dependencies: ["backend/src/"],
          techStack: [
            {
              category: "ai",
              technology: agent.llmModel ?? "openai",
              reason: "LLM integration",
            },
          ],
        }, [
          {
            type: "source_code",
            path: `backend/src/agents/${agent.name.toLowerCase()}/`,
            description: `AI agent ${agent.name}`,
          },
        ]),
      );
    }

    return {
      name: "intelligence",
      description: "AI agent integrations and LLM-powered features",
      tasks,
      dependsOn: ["core"],
    };
  }

  // ── Phase 5: Infrastructure ──

  private planInfrastructurePhase(ir: SystemIR): ExecutionPhase {
    const tasks: AgentTask[] = [];

    const containerRuntimes = ir.infrastructureLayer.runtimes.filter(
      (r) => r.platform === "container" || r.orchestration,
    );

    if (containerRuntimes.length > 0) {
      tasks.push(
        this.createTask("infrastructure", "Generate Docker and Kubernetes configs", {
          relevantIR: {
            runtimes: ir.infrastructureLayer.runtimes,
            flows: ir.communicationLayer.flows,
          },
          instructions: [
            "Generate Dockerfiles for all container-based runtimes",
            "Generate docker-compose.yml for local development",
            ...containerRuntimes.map(
              (r) =>
                `Runtime '${r.name}': platform=${r.platform}, orchestration=${r.orchestration ?? "none"}`,
            ),
            ...containerRuntimes
              .filter((r) => r.scaling)
              .map(
                (r) =>
                  `Scaling for '${r.name}': min=${r.scaling?.minReplicas}, max=${r.scaling?.maxReplicas}`,
              ),
          ],
          constraints: [
            "Use multi-stage Docker builds for minimal image size",
            "Include health checks in all containers",
            "Use non-root users in containers",
          ],
          dependencies: ["backend/", "web/"],
          techStack: [
            { category: "container", technology: "docker", reason: "Containerization" },
            ...(containerRuntimes.some((r) => r.orchestration === "kubernetes")
              ? [
                  {
                    category: "orchestration",
                    technology: "kubernetes",
                    reason: "Container orchestration",
                  },
                ]
              : []),
          ],
        }, [
          { type: "dockerfile", path: "infra/docker/", description: "Dockerfiles" },
          { type: "config", path: "docker-compose.yml", description: "Docker Compose" },
          ...(containerRuntimes.some((r) => r.orchestration === "kubernetes")
            ? [
                {
                  type: "kubernetes_manifest" as OutputType,
                  path: "infra/k8s/",
                  description: "Kubernetes manifests",
                },
              ]
            : []),
        ]),
      );
    }

    return {
      name: "infrastructure",
      description: "Docker, Kubernetes, CI/CD, and deployment configs",
      tasks,
      dependsOn: ["core", "clients"],
    };
  }

  // ── Phase 6: Quality ──

  private planQualityPhase(ir: SystemIR): ExecutionPhase {
    const tasks: AgentTask[] = [];

    // Security audit
    tasks.push(
      this.createTask("security", "Security audit and hardening", {
        relevantIR: {
          flows: ir.communicationLayer.flows,
          models: ir.dataLayer.models,
          runtimes: ir.infrastructureLayer.runtimes,
        },
        instructions: [
          "Audit all API endpoints for authentication/authorization",
          "Review database schemas for sensitive data encryption",
          "Check infrastructure configs for security best practices",
          "Generate security middleware and CORS configs",
        ],
        constraints: [
          "Follow OWASP Top 10 guidelines",
          "Implement principle of least privilege",
          "Encrypt sensitive data at rest and in transit",
        ],
        dependencies: ["backend/", "api/openapi.yaml", "infra/"],
        techStack: [],
      }, [
        { type: "config", path: "security/", description: "Security configs" },
      ]),
    );

    // Test generation
    tasks.push(
      this.createTask("testing", "Generate test suites", {
        relevantIR: {
          controllers: ir.logicLayer.controllers,
          models: ir.dataLayer.models,
          views: ir.presentationLayer.views,
        },
        instructions: [
          "Generate unit tests for all controllers and business logic",
          "Generate integration tests for API endpoints",
          "Generate e2e tests for critical user flows",
          ...ir.logicLayer.controllers.flatMap((c) =>
            c.events.map((e) => `Test event handler: ${e.trigger}`),
          ),
        ],
        constraints: [
          "Achieve minimum 80% code coverage",
          "Include edge cases and error scenarios",
          "Mock external service integrations",
        ],
        dependencies: ["backend/", "web/", "ios/", "android/"],
        techStack: [],
      }, [
        { type: "test", path: "tests/", description: "Test suites" },
      ]),
    );

    return {
      name: "quality",
      description: "Security audit, testing, and documentation",
      tasks,
      dependsOn: ["core", "clients", "infrastructure"],
    };
  }

  // ─────────────────────── Helpers ───────────────────────────────

  private createTask(
    agent: AgentType,
    description: string,
    context: AgentContext,
    outputs: ExpectedOutput[],
  ): AgentTask {
    return {
      id: `task_${++this.taskCounter}`,
      agent,
      description,
      context,
      outputs,
      priority: this.taskCounter,
    };
  }

  private isBackendRuntime(r: RuntimeIR): boolean {
    const p = r.platform.toLowerCase();
    return (
      p === "container" ||
      p === "managed" ||
      p === "serverless" ||
      p === "server" ||
      p === "vm" ||
      r.assignments.controllers.length > 0
    );
  }

  private isIOSRuntime(r: RuntimeIR): boolean {
    const p = r.platform.toLowerCase();
    return p === "ios" || p.includes("ios") ||
      (Array.isArray(p) && (p as unknown as string[]).includes("ios"));
  }

  private isAndroidRuntime(r: RuntimeIR): boolean {
    const p = r.platform.toLowerCase();
    return p === "android" || p.includes("android");
  }

  private isWebRuntime(r: RuntimeIR): boolean {
    const p = r.platform.toLowerCase();
    return p === "web" || p === "browser" || p.includes("web");
  }

  private inferDatabaseTechStack(ir: SystemIR): TechStackRequirement[] {
    const stacks: TechStackRequirement[] = [];
    const storages = new Set(
      ir.dataLayer.models
        .map((m) => m.storage?.toLowerCase())
        .filter(Boolean),
    );

    if (storages.has("postgres") || storages.size === 0) {
      stacks.push({
        category: "database",
        technology: "postgresql",
        version: "16",
        reason: "Primary data store",
      });
    }
    if (storages.has("redis") || ir.dataLayer.models.some((m) => m.cache)) {
      stacks.push({
        category: "cache",
        technology: "redis",
        version: "7",
        reason: "Caching layer",
      });
    }
    if (storages.has("mongodb")) {
      stacks.push({
        category: "database",
        technology: "mongodb",
        version: "7",
        reason: "Document store",
      });
    }

    return stacks;
  }

  private inferBackendTechStack(runtime: RuntimeIR): TechStackRequirement[] {
    const lang = (runtime.language ?? "typescript").toLowerCase();
    const stacks: TechStackRequirement[] = [];

    switch (lang) {
      case "typescript":
      case "node":
        stacks.push(
          { category: "language", technology: "typescript", version: "5", reason: "Backend language" },
          { category: "runtime", technology: "node", version: "20", reason: "Runtime" },
        );
        break;
      case "python":
        stacks.push(
          { category: "language", technology: "python", version: "3.12", reason: "Backend language" },
          { category: "framework", technology: "fastapi", reason: "Web framework" },
        );
        break;
      case "go":
        stacks.push(
          { category: "language", technology: "go", version: "1.22", reason: "Backend language" },
        );
        break;
    }

    return stacks;
  }
}

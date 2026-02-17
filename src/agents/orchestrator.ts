/**
 * Ravel v2 Agent Orchestrator
 *
 * Executes an AgentExecutionPlan by dispatching tasks to LLM agents.
 *
 * Architecture:
 *   Ravel Source (.rv)
 *     → Lexer → Parser → AST
 *       → Analyzer → AnalyzedProgram
 *         → IRBuilder → SystemIR
 *           → Planner → AgentExecutionPlan
 *             → Orchestrator → dispatches to LLM agents
 *               → Generated Project (iOS + Android + Backend + Infra + ...)
 *
 * The Orchestrator is agent-runtime agnostic. It defines an AgentRuntime
 * interface that can be implemented for different LLM providers:
 *   - OpenAI (GPT-4, GPT-4o)
 *   - Anthropic (Claude)
 *   - Local models (Ollama, vLLM)
 *   - Mock runtime (for testing / dry-run)
 *
 * In "plan-only" mode (the default for `ravel compile`), the orchestrator
 * simply outputs the execution plan as JSON — the user can then feed it
 * to any agent runtime. This makes Ravel a pure compiler that doesn't
 * require an API key to run.
 */

import type {
  AgentExecutionPlan,
  ExecutionPhase,
  AgentTask,
  AgentType,
} from "./types.js";

// ═══════════════════════════════════════════════════════════════════
// Agent Runtime Interface
// ═══════════════════════════════════════════════════════════════════

export interface AgentRuntime {
  /** Execute a single agent task and return generated artifacts */
  execute(task: AgentTask, context: ExecutionContext): Promise<TaskResult>;

  /** Check if this runtime supports a given agent type */
  supports(agentType: AgentType): boolean;
}

export interface ExecutionContext {
  /** Output directory for generated files */
  outputDir: string;
  /** Previously generated artifacts (from earlier phases/tasks) */
  artifacts: Map<string, GeneratedArtifact>;
  /** Environment variables / secrets */
  env: Record<string, string>;
  /** Verbose logging */
  verbose: boolean;
}

export interface TaskResult {
  taskId: string;
  success: boolean;
  artifacts: GeneratedArtifact[];
  logs: string[];
  errors: string[];
}

export interface GeneratedArtifact {
  path: string;
  content: string;
  type: string;
}

// ═══════════════════════════════════════════════════════════════════
// Orchestrator
// ═══════════════════════════════════════════════════════════════════

export interface OrchestratorOptions {
  outputDir: string;
  runtime?: AgentRuntime;
  verbose?: boolean;
  dryRun?: boolean;
  env?: Record<string, string>;
  /** Callback for progress reporting */
  onProgress?: (event: ProgressEvent) => void;
}

export interface ProgressEvent {
  type: "phase_start" | "phase_end" | "task_start" | "task_end" | "error";
  phase?: string;
  task?: string;
  message: string;
}

export class Orchestrator {
  private options: OrchestratorOptions;

  constructor(options: OrchestratorOptions) {
    this.options = options;
  }

  /**
   * Execute the full plan.
   *
   * In dry-run mode (default), just validates and returns the plan.
   * With a runtime, actually dispatches tasks to LLM agents.
   */
  async execute(plan: AgentExecutionPlan): Promise<ExecutionResult> {
    const context: ExecutionContext = {
      outputDir: this.options.outputDir,
      artifacts: new Map(),
      env: this.options.env ?? {},
      verbose: this.options.verbose ?? false,
    };

    const results: PhaseResult[] = [];

    for (const phase of plan.phases) {
      this.emit({
        type: "phase_start",
        phase: phase.name,
        message: `Starting phase: ${phase.name} — ${phase.description}`,
      });

      const phaseResult = await this.executePhase(phase, context);
      results.push(phaseResult);

      // Collect artifacts from this phase for later phases
      for (const taskResult of phaseResult.taskResults) {
        for (const artifact of taskResult.artifacts) {
          context.artifacts.set(artifact.path, artifact);
        }
      }

      this.emit({
        type: "phase_end",
        phase: phase.name,
        message: `Completed phase: ${phase.name} (${phaseResult.taskResults.length} tasks)`,
      });

      if (phaseResult.taskResults.some((r) => !r.success)) {
        this.emit({
          type: "error",
          phase: phase.name,
          message: `Phase '${phase.name}' had failures`,
        });
      }
    }

    return {
      plan,
      phases: results,
      totalTasks: plan.phases.reduce((sum, p) => sum + p.tasks.length, 0),
      successfulTasks: results.reduce(
        (sum, p) => sum + p.taskResults.filter((r) => r.success).length,
        0,
      ),
      artifacts: Array.from(context.artifacts.values()),
    };
  }

  private async executePhase(
    phase: ExecutionPhase,
    context: ExecutionContext,
  ): Promise<PhaseResult> {
    const taskResults: TaskResult[] = [];
    const runtime = this.options.runtime;

    if (!runtime || this.options.dryRun) {
      // Dry-run: simulate execution
      for (const task of phase.tasks) {
        this.emit({
          type: "task_start",
          task: task.id,
          message: `[dry-run] ${task.agent}: ${task.description}`,
        });

        taskResults.push({
          taskId: task.id,
          success: true,
          artifacts: task.outputs.map((o) => ({
            path: o.path,
            content: `/* Generated by Ravel v2 — ${task.agent} agent */\n/* ${o.description} */\n`,
            type: o.type,
          })),
          logs: [`[dry-run] Would execute: ${task.description}`],
          errors: [],
        });

        this.emit({
          type: "task_end",
          task: task.id,
          message: `[dry-run] Completed: ${task.id}`,
        });
      }
    } else {
      // Real execution: dispatch tasks in parallel within a phase
      const promises = phase.tasks.map(async (task) => {
        this.emit({
          type: "task_start",
          task: task.id,
          message: `Executing: ${task.agent}: ${task.description}`,
        });

        try {
          if (!runtime.supports(task.agent)) {
            return {
              taskId: task.id,
              success: false,
              artifacts: [],
              logs: [],
              errors: [
                `Runtime does not support agent type '${task.agent}'`,
              ],
            } satisfies TaskResult;
          }

          const result = await runtime.execute(task, context);

          this.emit({
            type: "task_end",
            task: task.id,
            message: `Completed: ${task.id} (${result.artifacts.length} artifacts)`,
          });

          return result;
        } catch (err) {
          const msg =
            err instanceof Error ? err.message : String(err);
          this.emit({
            type: "error",
            task: task.id,
            message: `Failed: ${task.id}: ${msg}`,
          });

          return {
            taskId: task.id,
            success: false,
            artifacts: [],
            logs: [],
            errors: [msg],
          } satisfies TaskResult;
        }
      });

      taskResults.push(...(await Promise.all(promises)));
    }

    return { phase: phase.name, taskResults };
  }

  private emit(event: ProgressEvent): void {
    this.options.onProgress?.(event);
  }
}

// ═══════════════════════════════════════════════════════════════════
// Result types
// ═══════════════════════════════════════════════════════════════════

export interface PhaseResult {
  phase: string;
  taskResults: TaskResult[];
}

export interface ExecutionResult {
  plan: AgentExecutionPlan;
  phases: PhaseResult[];
  totalTasks: number;
  successfulTasks: number;
  artifacts: GeneratedArtifact[];
}

// ═══════════════════════════════════════════════════════════════════
// Plan Serialization — the "compiled output" of Ravel
// ═══════════════════════════════════════════════════════════════════

/**
 * Serialize an execution plan to a portable JSON format.
 * This is the primary output of `ravel compile` — a complete description
 * of every task needed to build the system, ready to be consumed by
 * any agent runtime.
 */
export function serializePlan(plan: AgentExecutionPlan): string {
  return JSON.stringify(plan, null, 2);
}

/**
 * Deserialize a previously compiled plan.
 */
export function deserializePlan(json: string): AgentExecutionPlan {
  return JSON.parse(json) as AgentExecutionPlan;
}

/**
 * Generate a human-readable summary of the execution plan.
 */
export function summarizePlan(plan: AgentExecutionPlan): string {
  const lines: string[] = [];

  lines.push(`Ravel Execution Plan: ${plan.system.name}`);
  if (plan.system.description) {
    lines.push(`  ${plan.system.description}`);
  }
  lines.push(`  Version: ${plan.system.version ?? "0.1.0"}`);
  lines.push(`  Generated: ${plan.generatedAt}`);
  lines.push(`  Compiler: Ravel v${plan.compilerVersion}`);
  lines.push("");

  let totalTasks = 0;
  for (const phase of plan.phases) {
    lines.push(`Phase: ${phase.name}`);
    lines.push(`  ${phase.description}`);
    if (phase.dependsOn?.length) {
      lines.push(`  Depends on: ${phase.dependsOn.join(", ")}`);
    }
    lines.push(`  Tasks (${phase.tasks.length}):`);

    for (const task of phase.tasks) {
      lines.push(`    [${task.agent}] ${task.description}`);
      for (const out of task.outputs) {
        lines.push(`      -> ${out.path} (${out.type})`);
      }
      totalTasks++;
    }
    lines.push("");
  }

  lines.push(`Total: ${plan.phases.length} phases, ${totalTasks} tasks`);

  return lines.join("\n");
}

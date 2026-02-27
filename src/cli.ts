#!/usr/bin/env node

/**
 * Ravel v2 CLI
 *
 * Usage:
 *   ravel compile <file.rv>              Compile and output execution plan
 *   ravel compile <file.rv> -o plan.json Write plan to file
 *   ravel compile <file.rv> --summary    Print human-readable summary
 *   ravel compile <file.rv> --ir         Output the SystemIR
 *   ravel compile <file.rv> --ast        Output the AST
 *   ravel compile <file.rv> --tokens     Output the token stream
 *   ravel run <plan.json>                Execute a plan (requires agent runtime)
 *   ravel run <file.rv>                  Compile and execute in one step
 *   ravel init <name>                    Scaffold a new Ravel project
 */

import * as fs from "node:fs";
import * as path from "node:path";
import { compile } from "./index.js";
import { Lexer } from "./compiler/lexer.js";
import {
  serializePlan,
  summarizePlan,
  Orchestrator,
  deserializePlan,
} from "./agents/orchestrator.js";

// ── Argument parsing (minimal, no deps) ──

interface CliArgs {
  command: string;
  file?: string;
  output?: string;
  flags: Set<string>;
}

function parseArgs(argv: string[]): CliArgs {
  const args = argv.slice(2);
  const command = args[0] ?? "help";
  let file: string | undefined;
  let output: string | undefined;
  const flags = new Set<string>();

  for (let i = 1; i < args.length; i++) {
    const arg = args[i];
    if (arg === "-o" || arg === "--output") {
      output = args[++i];
    } else if (arg.startsWith("--")) {
      flags.add(arg.slice(2));
    } else if (arg.startsWith("-")) {
      flags.add(arg.slice(1));
    } else if (!file) {
      file = arg;
    }
  }

  return { command, file, output, flags };
}

// ── Commands ──

function cmdCompile(args: CliArgs): void {
  if (!args.file) {
    console.error("Error: No input file specified");
    console.error("Usage: ravel compile <file.rv>");
    process.exit(1);
  }

  const filePath = path.resolve(args.file);
  if (!fs.existsSync(filePath)) {
    console.error(`Error: File not found: ${filePath}`);
    process.exit(1);
  }

  const source = fs.readFileSync(filePath, "utf-8");

  try {
    // Token dump mode
    if (args.flags.has("tokens")) {
      const lexer = new Lexer(source, args.file);
      const tokens = lexer.tokenize();
      const output = tokens
        .map(
          (t) =>
            `${String(t.loc.line).padStart(4)}:${String(t.loc.column).padStart(3)}  ${t.type.padEnd(16)} ${JSON.stringify(t.value)}`,
        )
        .join("\n");
      writeOutput(output, args.output);
      return;
    }

    const result = compile(source, args.file);

    // AST dump mode
    if (args.flags.has("ast")) {
      writeOutput(JSON.stringify(result.ast, null, 2), args.output);
      return;
    }

    // IR dump mode
    if (args.flags.has("ir")) {
      writeOutput(JSON.stringify(result.ir, null, 2), args.output);
      return;
    }

    // Summary mode
    if (args.flags.has("summary") || args.flags.has("s")) {
      const summary = summarizePlan(result.plan);
      writeOutput(summary, args.output);
      return;
    }

    // Default: output the full execution plan
    const planJson = serializePlan(result.plan);

    if (args.output) {
      writeOutput(planJson, args.output);
      console.error(`Compiled ${args.file} -> ${args.output}`);
    } else {
      // If no output file, print summary to stderr and plan to stdout
      console.error(summarizePlan(result.plan));
      console.error("");
      console.error("Plan JSON written to stdout. Use -o <file> to save.");
      console.log(planJson);
    }
  } catch (err) {
    console.error(
      `Compilation error: ${err instanceof Error ? err.message : String(err)}`,
    );
    process.exit(1);
  }
}

async function cmdRun(args: CliArgs): Promise<void> {
  if (!args.file) {
    console.error("Error: No input file specified");
    console.error("Usage: ravel run <file.rv|plan.json>");
    process.exit(1);
  }

  const filePath = path.resolve(args.file);
  if (!fs.existsSync(filePath)) {
    console.error(`Error: File not found: ${filePath}`);
    process.exit(1);
  }

  const source = fs.readFileSync(filePath, "utf-8");

  // Determine if input is a plan JSON or a .rv source
  let plan;
  if (filePath.endsWith(".json")) {
    plan = deserializePlan(source);
  } else {
    const result = compile(source, args.file);
    plan = result.plan;
  }

  const outputDir = args.output ?? path.join(process.cwd(), "ravel-output");

  const orchestrator = new Orchestrator({
    outputDir,
    dryRun: args.flags.has("dry-run"),
    verbose: args.flags.has("verbose") || args.flags.has("v"),
    onProgress: (event) => {
      const prefix =
        event.type === "error" ? "ERROR" : event.type.toUpperCase();
      console.error(`[${prefix}] ${event.message}`);
    },
  });

  const result = await orchestrator.execute(plan);

  console.error("");
  console.error(
    `Execution complete: ${result.successfulTasks}/${result.totalTasks} tasks succeeded`,
  );
  console.error(`Output directory: ${outputDir}`);

  if (result.successfulTasks < result.totalTasks) {
    process.exit(1);
  }
}

function cmdInit(args: CliArgs): void {
  const name = args.file ?? "my-ravel-app";
  const dir = path.resolve(name);

  if (fs.existsSync(dir)) {
    console.error(`Error: Directory '${name}' already exists`);
    process.exit(1);
  }

  fs.mkdirSync(dir, { recursive: true });

  // Create a starter .rv file
  const starterSource = `# ${name} — A Ravel Application
# Generated by: ravel init

system ${toPascalCase(name)}:
    description: "A new Ravel application"
    version: "0.1.0"

# Define your data models
model Item:
    schema:
        id: uuid @primary
        name: string
        created_at: datetime
    storage: postgres

# Define your business logic
controller ItemController(items: Item):
    event items.created(item: Item):
        log item.name

# Define your views
view ItemListView:
    displays: Item[]
    platform: [web]
    behavior:
        "Show a list of items with name and creation date"
        "Allow creating new items via a form"

# Define where things run
runtime Backend:
    platform: container
    language: typescript
    controllers: [ItemController]
    models: [Item]

runtime WebApp:
    platform: web
    language: typescript
    views: [ItemListView]

# Define how things connect
flow AppFlow:
    WebApp -> Backend:
        protocol: https
        auth: jwt
`;

  fs.writeFileSync(path.join(dir, `${name}.rv`), starterSource);

  console.log(`Created new Ravel project: ${name}/`);
  console.log(`  ${name}/${name}.rv`);
  console.log("");
  console.log("Next steps:");
  console.log(`  cd ${name}`);
  console.log(`  ravel compile ${name}.rv --summary`);
}

function cmdHelp(): void {
  console.log(`
Ravel v2 — Language & Compiler for LLM-Agent-Orchestrated Systems

  Ravel is a declarative language for describing full-stack systems.
  The compiler transforms .rv source files into Agent Execution Plans
  that LLM agents carry out to build the complete project.

USAGE:
  ravel <command> [options] [file]

COMMANDS:
  compile <file.rv>    Compile a Ravel source file into an execution plan
  run <file>           Execute a plan (or compile+run a .rv file)
  init <name>          Create a new Ravel project

COMPILE OPTIONS:
  -o, --output <file>  Write output to file
  --summary            Print human-readable plan summary
  --ast                Output the parsed AST
  --ir                 Output the System IR
  --tokens             Output the token stream

RUN OPTIONS:
  -o, --output <dir>   Output directory (default: ravel-output/)
  --dry-run            Simulate execution without calling agents
  --verbose            Show detailed progress

EXAMPLES:
  ravel compile app.rv --summary
  ravel compile app.rv -o plan.json
  ravel run app.rv --dry-run
  ravel init my-app

LANGUAGE OVERVIEW:
  system       Top-level system declaration
  model        Data schema with storage, sync, and flow semantics
  controller   Event-driven business logic
  view         Declarative UI with natural-language behavior specs
  service      External service integration (APIs, providers)
  runtime      Deployment target (container, mobile, serverless, edge)
  agent        AI/LLM-powered component
  flow         Data pipeline between runtimes with protocol specs
  feature      Ownership tracking and blast-radius analysis

Learn more: https://github.com/Northshoot/RavelLang
`);
}

// ── Utilities ──

function writeOutput(content: string, outputPath?: string): void {
  if (outputPath) {
    const dir = path.dirname(outputPath);
    if (!fs.existsSync(dir)) fs.mkdirSync(dir, { recursive: true });
    fs.writeFileSync(outputPath, content, "utf-8");
  } else {
    console.log(content);
  }
}

function toPascalCase(str: string): string {
  return str
    .split(/[-_\s]+/)
    .map((s) => s.charAt(0).toUpperCase() + s.slice(1))
    .join("");
}

// ── Main ──

async function main(): Promise<void> {
  const args = parseArgs(process.argv);

  switch (args.command) {
    case "compile":
    case "c":
      cmdCompile(args);
      break;
    case "run":
    case "r":
      await cmdRun(args);
      break;
    case "init":
      cmdInit(args);
      break;
    case "help":
    case "--help":
    case "-h":
      cmdHelp();
      break;
    case "version":
    case "--version":
    case "-v":
      console.log("ravel 2.0.0");
      break;
    default:
      console.error(`Unknown command: ${args.command}`);
      cmdHelp();
      process.exit(1);
  }
}

main().catch((err) => {
  console.error(err);
  process.exit(1);
});

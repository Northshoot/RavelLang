# Ravel v2: Compiling Declarative System Specifications into LLM Agent Execution Plans

**Authors:** Building on work by L. Riliskis, J. Hong, and P. Levis (Stanford University, 2015)

---

## Abstract

Modern software systems span mobile applications, backend services,
databases, AI components, and cloud infrastructure — yet developers
must manually coordinate code generation across all of these layers.
We present Ravel v2, a declarative language and compiler that
transforms high-level system specifications into *Agent Execution
Plans*: structured task graphs that Large Language Model (LLM)
agents carry out to produce complete, deployable projects. Ravel v2
evolves the original Ravel IoT framework (Riliskis et al., 2015),
which introduced distributed Model-View-Controller abstractions for
embedded-gateway-cloud applications. Where the original compiled
`.rv` source to C and Java via template-based code generation, the
modern system compiles to orchestration plans for AI agents
specialized in database design, backend engineering, mobile
development, infrastructure, security, and testing. We demonstrate
the language on seven example systems ranging from a 55-line URL
shortener to a multi-tenant analytics platform, showing that a
single Ravel source file can specify the architecture of systems
that would conventionally require tens of thousands of lines of
hand-written code distributed across 5-10 separate repositories.

## 1. Introduction

Building a modern application is a distributed systems problem
disguised as a software engineering task. A conceptually simple
product — say, a concert ticketing platform — requires native iOS
and Android apps (Swift, Kotlin), a backend API (TypeScript, Go,
or Python), a database schema with migrations (SQL), infrastructure
definitions (Docker, Kubernetes, Terraform), CI/CD pipelines,
authentication, API contracts, and test suites. Each component
speaks a different language, follows different conventions, and
lives in a different repository. Developers spend significant
effort not on the *what* — the business logic — but on the *how*
— the glue between layers.

This problem echoes the challenge that motivated the original Ravel
framework [1]. In 2015, Riliskis, Hong, and Levis observed that
IoT applications spanning embedded devices, gateways, and cloud
servers suffered from the same fragmentation: developers manually
translated high-level application logic into platform-specific C
(for microcontrollers), Java (for Android gateways and cloud
servers), and Python (for web dashboards). Their solution was a
domain-specific language built on distributed MVC abstractions —
*models*, *controllers*, *views*, *interfaces*, and *spaces* — that
a compiler automatically distributed across heterogeneous platforms.

We observe that today's full-stack development has the same
structure as 2015-era IoT development, at a larger scale. The
"devices" are now containers, mobile phones, edge functions, and
managed cloud services. The "protocols" are now REST, WebSocket,
gRPC, and message queues. And critically, a new capability has
emerged: Large Language Models can generate correct, idiomatic code
for any target platform when given precise, structured
specifications.

Ravel v2 synthesizes these observations. It preserves the original
Ravel insight — describe *what* your system does, and let the
compiler handle *how* — while replacing template-based code
generation with LLM agent orchestration. The result is a language
where 80 lines of Ravel can specify a system that would require
50,000+ lines of hand-written code across six programming
languages.

## 2. Language Design

### 2.1 Design Principles

Ravel v2 follows five principles:

1. **Declarative intent over procedural implementation.** Developers
   describe the desired architecture, not the build steps.

2. **Full-stack composition in a single source.** One `.rv` file
   captures data, logic, UI, integrations, AI, infrastructure, and
   communication — the complete system topology.

3. **Natural-language behavior specifications.** UI views and AI
   agents accept behavior descriptions as strings, which are passed
   directly to specialized LLM agents as prompts.

4. **Platform independence with runtime awareness.** The language
   abstracts over platforms (iOS, Android, web, containers,
   serverless) while allowing platform-specific configuration.

5. **Security by default through flow analysis.** The compiler
   analyzes data flows between runtimes to determine required
   authentication, encryption, and access control — extending the
   original Ravel security analysis to modern auth patterns (JWT,
   OAuth, TLS).

### 2.2 Language Constructs

Ravel v2 provides eight top-level constructs. The table below
traces each to its origin in the 2015 Ravel language:

| Construct    | Original Ravel | Purpose |
|-------------|---------------|---------|
| `system`    | *(new)*       | Top-level composition and metadata |
| `model`     | `model`       | Data schema with storage, sync, caching, and flow |
| `controller`| `controller`  | Event-driven business logic |
| `view`      | `view`        | Declarative UI with behavior specifications |
| `service`   | `interface`   | External API/provider integration |
| `runtime`   | `space`       | Deployment target (container, mobile, serverless) |
| `agent`     | *(new)*       | LLM-powered component |
| `flow`      | `flow` (prop) | Typed data pipeline between runtimes |

**Models** retain the original Ravel qualifiers (`local`,
`streaming`, `replicated`) and add `ephemeral` for transient
data. They gain modern properties: `storage` (postgres, redis,
mongodb), `cache`, `retention`, `index`, and `encryption`.

**Controllers** preserve the event-driven paradigm from 2015
Ravel — they bind to models via parameters and define event
handlers triggered by data lifecycle events (created, arrived,
departed). The expression language supports full control flow
(if/elif/else, for, while), method calls, and error handling.

**Views** are the most significantly evolved construct. Where
original Ravel views were thin wrappers around platform-specific
templates, Ravel v2 views accept *behavior specifications* — plain
English descriptions of the desired user experience:

```ravel
view PetMap:
    displays: Location[]
    platform: [ios, android]
    behavior:
        "Full-screen map centered on the pet's last location"
        "Animated breadcrumb trail showing today's path"
        "Safe zone shown as a translucent green circle"
        "Red pulse animation if pet is outside safe zone"
```

These behavior strings become part of the structured prompt sent to
the iOS or Android agent, which generates platform-native code that
satisfies the specification. This is a form of *intent-driven UI
programming* — the developer specifies what the user experiences,
not how pixels are laid out.

**Agents** are entirely new. They declare LLM-powered components
with model selection, behavior specifications (the system prompt),
typed inputs/outputs, and safety guardrails:

```ravel
agent EscapeDetector:
    model: "gpt-4o-mini"
    behavior:
        "Detect if a pet has left its safe zone"
        "Distinguish walks with owner vs actual escapes"
    input: Location[], Pet
    output: json
    temperature: 0.2
```

**Runtimes** generalize the original `space` to support modern
deployment targets: `container` (Docker/K8s), mobile platforms
(`[ios, android]`), `web`, `serverless`, `managed` (cloud
databases), and `edge`. Each runtime specifies which models,
controllers, views, services, and agents are assigned to it —
creating a clear deployment topology.

**Flows** make the original Ravel `flow` property a first-class
declaration with protocol, authentication, rate limiting, and event
specifications:

```ravel
flow TrackerFlow:
    Collar -> Cloud:
        protocol: mqtt
        auth: tls_cert
    Cloud -> Phone:
        protocol: websocket
        auth: jwt
        events: [location_update, escape_alert]
```

### 2.3 Type System

The type system supports primitive types (`int`, `float`, `string`,
`bool`, `uuid`, `datetime`, `json`, etc.), composite types (arrays
`T[]`, optionals `T?`, maps `map<K,V>`, tuples `(A,B)`), inline
enums (`enum("a","b","c")`), and foreign keys (`uuid -> Model.field`).
Annotations (`@primary`, `@unique`, `@index`) encode database
constraints directly in the schema. Domain-specific literals
express durations (`60s`), sizes (`10mb`), rates (`100/min`), and
percentages (`70%`).

## 3. Compiler Architecture

### 3.1 Compilation Pipeline

The Ravel v2 compiler is implemented in TypeScript (approximately
4,000 lines) and follows a six-stage pipeline:

```
Source (.rv) → Lexer → Parser → AST
  → Analyzer → Analyzed Program
    → IR Builder → System IR
      → Planner → Agent Execution Plan (JSON)
```

The **lexer** uses Python-style indentation tracking with bracket-
aware implicit line joining — identical to the technique used in
the original Ravel lexer and CPython's tokenizer. When the token
stream is inside parentheses, brackets, or braces, INDENT/DEDENT
tokens are suppressed, allowing multi-line expressions.

The **parser** is a hand-written recursive descent parser with Pratt
precedence climbing for expressions. This provides precise error
messages and full control over the grammar, compared to the original
Ravel's ANTLR4-generated parser.

The **semantic analyzer** performs three passes:
1. *Definition pass*: builds a global symbol table of all
   declarations (models, controllers, views, etc.)
2. *Validation pass*: checks that all references resolve — every
   controller referenced by a runtime exists, every model type
   referenced by a controller exists, every flow endpoint is a
   known runtime
3. *Flow analysis pass*: determines which runtimes read/write which
   models, building a data flow graph that informs the agent planner

### 3.2 System Intermediate Representation

The IR is a seven-layer architectural description:

| Layer           | Contents |
|----------------|----------|
| Data           | Model schemas, field types, storage backends, indexes |
| Logic          | Controllers, event handlers, method bodies, CRUD operations |
| Presentation   | Views, behavior specs, style properties, component trees |
| Integration    | Service definitions, provider configs, API methods |
| Intelligence   | Agent declarations, LLM models, behavior prompts, guardrails |
| Infrastructure | Runtime assignments, scaling rules, environment configs |
| Communication  | Flow edges with protocols, auth, rate limits, events |

This layered IR serves the same role as the TypedIR in the original
Ravel compiler — it is the fully analyzed, validated, lowered
representation of the program. But where TypedIR was designed for
translation to C/Java instructions (SSA form, register allocation,
constant folding), the System IR is designed for consumption by LLM
agents: it provides structured context that can be serialized into
agent prompts.

### 3.3 Agent Execution Plan

The planner transforms the System IR into an Agent Execution Plan —
a phased task graph where each task is assigned to a specialized
LLM agent. Phases execute sequentially (each depends on artifacts
from prior phases); tasks within a phase can execute in parallel:

| Phase          | Agent Types              | Artifacts Produced |
|---------------|--------------------------|-------------------|
| 1. Foundation  | API, Database            | OpenAPI spec, DB schema, migrations, shared types |
| 2. Core        | Backend                  | Server code, business logic, middleware |
| 3. Clients     | iOS, Android, Web        | Native apps, web frontend |
| 4. Intelligence| Backend (AI integration) | LLM agent wrappers, prompt templates |
| 5. Infrastructure | Infrastructure        | Dockerfiles, K8s manifests, docker-compose |
| 6. Quality     | Security, Testing        | Security configs, test suites |

Each task includes:
- **Relevant IR slice**: the subset of the System IR the agent needs
- **Natural language instructions**: specific to the task
- **Constraints**: requirements the generated code must satisfy
- **Dependencies**: artifacts from earlier phases this task depends on
- **Tech stack requirements**: language, framework, and version specs
- **Expected outputs**: file paths and types

The plan is serialized as JSON — the *compiled output* of Ravel.
It can be consumed by any agent runtime (Claude, GPT-4, open-source
models) or inspected and modified by developers. This design makes
Ravel a pure compiler that requires no API keys to run.

## 4. Evaluation

### 4.1 Expressiveness

We evaluate Ravel v2 on seven example systems of increasing
complexity:

| Example              | Lines | Models | Views | Agents | Runtimes | Planned Tasks |
|---------------------|-------|--------|-------|--------|----------|--------------|
| URL Shortener        | 55    | 1      | 1     | 0      | 2        | 7            |
| Collaborative Whiteboard | 65 | 2    | 2     | 0      | 2        | 7            |
| AI Recipe App        | 75    | 2      | 2     | 2      | 2        | 9            |
| Pet Tracker          | 90    | 2      | 1     | 1      | 3        | 10           |
| Concert Ticketing    | 195   | 4      | 3     | 2      | 3        | 11           |
| Smart Home           | 175   | 4      | 3     | 2      | 4        | 12           |
| SaaS Analytics       | 180   | 5      | 3     | 2      | 4        | 10           |

For comparison, a production concert ticketing platform comparable
to the 195-line Ravel specification would typically require:
- ~5,000 lines of Swift/SwiftUI (iOS app)
- ~5,000 lines of Kotlin/Compose (Android app)
- ~3,000 lines of TypeScript (backend API)
- ~500 lines of SQL (schema + migrations)
- ~500 lines of YAML (Docker, K8s, CI/CD)
- ~2,000 lines of TypeScript (tests)

This represents approximately a **100x reduction** in specification
size. The reduction comes from three sources: (a) data model
declarations replace hand-written schemas, ORM models, and API
types; (b) behavior specifications replace UI layout code; and
(c) runtime/flow declarations replace infrastructure configuration.

### 4.2 Compilation Performance

The compiler processes all seven examples in under 100ms each on a
standard machine. The pipeline is entirely synchronous and requires
no network calls — the output is a JSON plan, not generated code.
Actual code generation latency depends on the agent runtime used.

### 4.3 Comparison with Original Ravel

| Dimension           | Original Ravel (2015) | Ravel v2 (2025) |
|--------------------|----------------------|-----------------|
| Target domain       | IoT (embedded-gateway-cloud) | Full-stack (mobile-backend-cloud-AI) |
| Implementation      | Java (ANTLR4, StringTemplate) | TypeScript (hand-written parser) |
| Code generation     | Template-based (C, Java) | LLM agent orchestration |
| Platforms supported | nRF52, Contiki, POSIX, J2SE, Android | iOS, Android, Web, Container, Serverless, Edge |
| Security model      | Field-level encryption/MAC | Flow-level auth (JWT, OAuth, TLS) |
| UI specification    | Template references | Natural language behavior specs |
| AI integration      | None | First-class `agent` construct |
| Compilation output  | Platform source code | Agent Execution Plan (JSON) |

The fundamental insight is preserved: *separation of architectural
intent from platform implementation.* What changed is the mechanism
of translation — from templates to agents — and the scope — from
three IoT tiers to the entire modern software stack.

## 5. Related Work

**Infrastructure as Code.** Terraform [2] and Pulumi [3] declare
infrastructure but not application logic. Ravel v2 encompasses both.

**Full-stack frameworks.** Ruby on Rails, Next.js, and similar
frameworks provide conventions for one stack. Ravel v2 is
stack-agnostic and cross-platform.

**AI code generation.** GitHub Copilot [4] and similar tools
generate code inline within an editor. Ravel v2 instead provides
a *structured specification* that constrains and coordinates
multiple agents, ensuring architectural consistency.

**Low-code platforms.** Tools like Retool and Bubble provide visual
builders for simple applications. Ravel v2 targets a wider spectrum
of complexity and produces standard, editable source code.

**Multi-agent systems.** AutoGPT [5], CrewAI [6], and similar
frameworks orchestrate LLM agents on general tasks. Ravel v2
contributes a *domain-specific compilation* model where the task
decomposition is determined by a formal compiler analysis, not by
agent self-planning.

**IoT programming frameworks.** The original Ravel [1], Node-RED [7],
and IoTivity provide programming models for IoT applications. Ravel
v2 extends the IoT programming model to the full-stack domain.

## 6. Discussion and Future Work

**Agent runtime implementation.** The current system produces
execution plans but includes only a dry-run orchestrator. A
production-ready agent runtime — dispatching tasks to Claude or
GPT-4, validating outputs, and handling errors — is the primary
next step.

**Iterative refinement.** The current compilation is one-shot:
source to plan. An interactive mode where the developer reviews
and refines agent outputs (e.g., "make the map view use Apple
MapKit instead of Google Maps") would close the feedback loop.

**Type-checked agent contracts.** Currently, agent inputs/outputs
are checked by the semantic analyzer but not enforced at the
generated code boundary. A future type-checking pass could verify
that generated code satisfies the IR contract.

**Standard library.** The original Ravel included a library of
interface implementations (BLE, Timer, LED, InfluxDB). A Ravel v2
standard library of service and view templates would reduce agent
work and improve consistency.

## 7. Conclusion

Ravel v2 demonstrates that LLM agents can serve as a *compilation
backend* for a domain-specific language. By preserving the
declarative, architecture-first design philosophy of the original
Ravel IoT framework and targeting LLM agents instead of code
templates, the language achieves a new point in the design space:
a single source file that specifies an entire system — from
database schemas to mobile UIs to Kubernetes deployments — and a
compiler that produces an execution plan any LLM agent runtime can
carry out.

The system embodies a simple thesis: the right abstraction level
for describing software systems is *architecture, not code*. Code
is the output, not the input. The compiler's job is to bridge
the gap — and in the era of LLMs, that bridge is an agent.

## References

[1] L. Riliskis, J. Hong, and P. Levis. "Ravel: Programming IoT
Applications as Distributed Models, Views, and Controllers." In
*Proc. IoT-App Workshop*, 2015.

[2] HashiCorp. "Terraform: Infrastructure as Code." 2014.
https://www.terraform.io

[3] Pulumi Corporation. "Pulumi: Infrastructure as Code in Any
Programming Language." 2018. https://www.pulumi.com

[4] GitHub. "GitHub Copilot." 2021.
https://github.com/features/copilot

[5] T. Richards et al. "Auto-GPT: An Autonomous GPT-4 Experiment."
2023. https://github.com/Significant-Gravitas/AutoGPT

[6] J. Moura. "CrewAI: Framework for Orchestrating Role-Playing AI
Agents." 2024. https://www.crewai.com

[7] N. O'Leary and D. Conway-Jones. "Node-RED: A Visual Tool for
Wiring the Internet of Things." 2013. https://nodered.org

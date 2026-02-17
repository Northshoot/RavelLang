# Ravel v2

**A language and compiler for orchestrating full-stack systems through LLM agents.**

Ravel is a declarative language where you describe *what* your system should do — the data, logic, views, services, AI agents, and infrastructure — and the compiler produces an **Agent Execution Plan** that LLM agents carry out to build the entire project.

```
.rv source
  → Lexer → Parser → AST
    → Analyzer → Validated Program
      → IR Builder → System IR
        → Planner → Agent Execution Plan (JSON)
          → Orchestrator → LLM Agents build the system
            ├── iOS (Swift/SwiftUI)
            ├── Android (Kotlin/Compose)
            ├── Web (React/TypeScript)
            ├── Backend (TypeScript/Python/Go)
            ├── Database (Postgres/Redis/Mongo)
            ├── Infrastructure (Docker/K8s/Terraform)
            └── Tests + Security + Documentation
```

## Heritage

Ravel descends from the [Stanford Ravel IoT framework](https://iot.stanford.edu/pubs/ravel-riliskis-iotapp15.pdf) (Riliskis, Hong, Levis — 2015), which introduced distributed MVC for IoT applications across embedded devices, gateways, and cloud servers.

| Original Ravel (2015)  | Modern Ravel (2025)          |
|------------------------|------------------------------|
| `model` (data + flow)  | `model` (data + storage + sync + cache) |
| `controller` (events)  | `controller` (events + AI logic)        |
| `view` (UI)            | `view` (declarative UI + behavior specs)|
| `interface` (hw/sw)    | `service` (cloud APIs + providers)      |
| `space` (platform)     | `runtime` (container, mobile, serverless, edge) |
| *N/A*                  | `agent` (LLM-powered component)         |
| `flow` (data pipes)    | `flow` (typed pipelines + protocols)    |
| *N/A*                  | `system` (top-level composition)        |

The original compiled `.rv` → C/Java source code via StringTemplate.
Modern Ravel compiles `.rv` → Agent Execution Plans via LLM orchestration.

## Quick Start

```bash
# Install dependencies
npm install

# Build the compiler
npm run build

# Compile an example
node dist/cli.js compile examples/concert-app/concert-app.rv --summary

# Dry-run the execution plan
node dist/cli.js run examples/concert-app/concert-app.rv --dry-run

# Output full JSON plan
node dist/cli.js compile examples/concert-app/concert-app.rv -o plan.json

# Scaffold a new project
node dist/cli.js init my-app
```

## Language Overview

### System

Top-level declaration that names and describes the application:

```ravel
system ConcertApp:
    description: "Live concert discovery and ticketing platform"
    version: "1.0.0"
```

### Model

Data schemas with storage semantics. Evolved from original Ravel models with modern storage backends:

```ravel
replicated model Concert:
    schema:
        id: uuid @primary
        name: string
        venue: string
        date: datetime
        capacity: int
        genre: enum("rock", "pop", "jazz", "electronic")
        status: enum("upcoming", "sold_out", "cancelled")
    storage: postgres
    cache: redis(ttl: 60s)
    index: [venue, date]
```

Qualifiers: `local`, `streaming`, `replicated`, `ephemeral`

### Controller

Event-driven business logic. Same model as original Ravel — controllers bind to models and react to events:

```ravel
controller TicketController(tickets: Ticket, concerts: Concert):
    max_per_user: int = 6

    event tickets.purchase_requested(user_id: uuid, concert_id: uuid, seats: string[]):
        concert = concerts.get(concert_id)
        if concert.tickets_sold + len(seats) > concert.capacity:
            return error("Not enough seats available")
        for seat in seats:
            ticket = tickets.create(
                concert_id: concert_id,
                user_id: user_id,
                seat: seat,
                status: "reserved",
                price_cents: concert.price_cents
            )
            ticket.save()
```

### View

Declarative UI with natural-language behavior specifications. The compiler sends these behavior specs as prompts to the appropriate agent (iOS, Android, or Web):

```ravel
view ConcertDiscovery:
    displays: Concert[]
    platform: [ios, android]
    behavior:
        "Show a curated feed of upcoming concerts"
        "Each concert card shows: poster, artist name, venue, date, price"
        "Support pull-to-refresh and infinite scroll"
        "Show a genre filter bar at the top"
    style:
        theme: material
        dark_mode: auto
```

### Service

External integrations. Replaces original Ravel's `interface` concept, modernized for cloud APIs:

```ravel
service PaymentService:
    provider: stripe
    def charge(amount: int, currency: string, token: string) -> json
    def refund(payment_id: string) -> json
```

### Runtime

Deployment targets. Replaces original Ravel's `space` concept, supporting modern platforms:

```ravel
runtime Backend:
    platform: container
    orchestration: kubernetes
    language: typescript
    controllers: [TicketController]
    services: [PaymentService]
    scaling:
        min_replicas: 3
        max_replicas: 20
        cpu_threshold: 70

runtime MobileApp:
    platform: [ios, android]
    views: [ConcertDiscovery, TicketWallet]
    config:
        min_ios: 16
        min_android: 26
```

### Agent

LLM-powered components — the new primitive that makes modern Ravel uniquely powerful:

```ravel
agent ConcertRecommender:
    model: "claude-sonnet"
    behavior:
        "Recommend concerts based on the user's listening history"
        "Consider genre preferences, location, and price sensitivity"
        "Diversify recommendations beyond just popular concerts"
    input: User, Concert[]
    output: Concert[] @ranked
    temperature: 0.7
```

### Flow

Data pipelines between runtimes. Evolved from original Ravel's flow declarations with protocol and auth specs:

```ravel
flow AppFlow:
    MobileApp -> Backend:
        protocol: https
        auth: jwt
        rate_limit: 100/min
    Backend -> DataStore:
        protocol: postgres
    Backend -> MobileApp:
        protocol: websocket
        events: [ticket_confirmed, seat_taken]
```

## Type System

**Primitives:** `int`, `int32`, `int64`, `float`, `double`, `decimal`, `string`, `bool`, `byte`, `bytes`, `uuid`, `datetime`, `timestamp`, `json`, `void`

**Composite:** `Type[]` (array), `Type[N]` (fixed array), `Type?` (optional), `map<K,V>`, `enum("a","b")`, `(A, B)` (tuple)

**Foreign keys:** `uuid -> Concert.id`

**Annotations:** `@primary`, `@unique`, `@index`, `@ranked`

## Compilation Output

The compiler produces an **Agent Execution Plan** — a JSON document that describes every task needed to build the complete system. Phases execute sequentially; tasks within a phase can run in parallel:

```
Phase 1: Foundation  — API contracts, database schemas, shared types
Phase 2: Core        — Backend services, business logic
Phase 3: Clients     — iOS, Android, Web frontends
Phase 4: Intelligence — AI agent integrations
Phase 5: Infrastructure — Docker, K8s, CI/CD
Phase 6: Quality     — Security audit, tests, docs
```

Each task includes structured instructions for a specialized LLM agent (database, backend, ios, android, web, infrastructure, security, testing).

## CLI Reference

```
ravel compile <file.rv>              # Compile and output execution plan
ravel compile <file.rv> --summary    # Print human-readable summary
ravel compile <file.rv> --ir         # Output the System IR
ravel compile <file.rv> --ast        # Output the parsed AST
ravel compile <file.rv> --tokens     # Output the token stream
ravel compile <file.rv> -o plan.json # Save plan to file
ravel run <file.rv> --dry-run        # Simulate execution
ravel run <plan.json>                # Execute a pre-compiled plan
ravel init <name>                    # Create a new project
```

## Architecture

```
src/
  compiler/
    tokens.ts      Token definitions
    lexer.ts       Tokenizer with Python-style indentation tracking
    ast.ts         AST node types
    parser.ts      Recursive descent parser
    analyzer.ts    Semantic analysis (symbol resolution, validation, flow analysis)
    ir.ts          System IR builder (the architectural description)
    errors.ts      Compiler error types
  agents/
    types.ts       Agent type system and execution plan schema
    planner.ts     IR → Agent Execution Plan transformation
    orchestrator.ts  Plan execution and agent dispatch
  cli.ts           CLI tool
  index.ts         Public API

grammar/
  Ravel.g4         Formal ANTLR4 grammar specification

examples/
  concert-app/     Full-stack concert ticketing platform
  smart-home/      IoT smart home with AI assistant
  saas-platform/   Multi-tenant analytics SaaS

legacy/            Original Ravel Java compiler (preserved)
```

## License

MIT + Apache 2.0

/**
 * Ravel v2 Recursive Descent Parser
 *
 * Parses a token stream into an AST. Hand-written for precise error messages
 * and full control over precedence / associativity. Follows the Ravel.g4
 * grammar spec but uses Pratt parsing for expressions.
 */

import {
  type Token,
  TokenType,
  type SourceLocation,
  PRIMITIVE_TYPE_TOKENS,
  MODEL_QUALIFIER_TOKENS,
} from "./tokens.js";
import * as AST from "./ast.js";
import { ParseError } from "./errors.js";

export class Parser {
  private tokens: Token[];
  private pos = 0;

  constructor(tokens: Token[]) {
    // Filter out redundant newlines but keep structural ones
    this.tokens = tokens;
  }

  parse(): AST.Program {
    const declarations: AST.Declaration[] = [];
    this.skipNewlines();

    while (!this.isAtEnd()) {
      this.skipNewlines();
      if (this.isAtEnd()) break;
      declarations.push(this.parseDeclaration());
      this.skipNewlines();
    }

    return { kind: "Program", declarations, loc: { line: 1, column: 1 } };
  }

  // ─────────────────────── Declarations ───────────────────────────

  private parseDeclaration(): AST.Declaration {
    if (this.check(TokenType.FROM) || this.check(TokenType.IMPORT)) {
      return this.parseImport();
    }
    if (this.check(TokenType.SYSTEM)) return this.parseSystem();
    if (this.check(TokenType.MODEL) || this.isModelQualifier()) return this.parseModel();
    if (this.check(TokenType.CONTROLLER)) return this.parseController();
    if (this.check(TokenType.VIEW)) return this.parseView();
    if (this.check(TokenType.SERVICE)) return this.parseService();
    if (this.check(TokenType.RUNTIME)) return this.parseRuntime();
    if (this.check(TokenType.AGENT)) return this.parseAgent();
    if (this.check(TokenType.FLOW)) return this.parseFlow();

    throw this.error(
      `Expected declaration (system, model, controller, view, service, runtime, agent, flow), got '${this.peek().value}'`,
    );
  }

  // ── Import ──

  private parseImport(): AST.ImportDecl {
    const loc = this.loc();

    if (this.match(TokenType.FROM)) {
      const module = this.parseModulePath();
      this.expect(TokenType.IMPORT);
      let names: string[] | "*";
      if (this.match(TokenType.STAR)) {
        names = "*";
      } else {
        names = [this.expectIdent()];
        while (this.match(TokenType.COMMA)) {
          names.push(this.expectIdent());
        }
      }
      return { kind: "ImportDecl", module, names, loc };
    }

    this.expect(TokenType.IMPORT);
    const module = this.parseModulePath();
    let alias: string | undefined;
    if (this.match(TokenType.AS)) {
      alias = this.expectIdent();
    }
    return { kind: "ImportDecl", module, names: "*", alias, loc };
  }

  private parseModulePath(): string[] {
    const parts = [this.expectIdent()];
    while (this.match(TokenType.DOT)) {
      parts.push(this.expectIdent());
    }
    return parts;
  }

  // ── System ──

  private parseSystem(): AST.SystemDecl {
    const loc = this.loc();
    this.expect(TokenType.SYSTEM);
    const name = this.expectIdent();
    this.expect(TokenType.COLON);
    this.expectIndent();
    const properties = this.parsePropertyBlock();
    this.expect(TokenType.DEDENT);
    return { kind: "SystemDecl", name, properties, loc };
  }

  // ── Model ──

  private parseModel(): AST.ModelDecl {
    const loc = this.loc();
    let qualifier: AST.ModelQualifier | undefined;

    if (this.isModelQualifier()) {
      qualifier = this.advance().value as AST.ModelQualifier;
    }

    this.expect(TokenType.MODEL);
    const name = this.expectIdent();
    const schema: AST.FieldDecl[] = [];
    const properties: AST.PropertyAssignment[] = [];

    if (this.match(TokenType.COLON)) {
      this.expectIndent();

      while (!this.check(TokenType.DEDENT) && !this.isAtEnd()) {
        this.skipNewlines();
        if (this.check(TokenType.DEDENT)) break;

        if (this.check(TokenType.SCHEMA)) {
          this.advance();
          this.expect(TokenType.COLON);
          this.expectIndent();
          while (!this.check(TokenType.DEDENT) && !this.isAtEnd()) {
            this.skipNewlines();
            if (this.check(TokenType.DEDENT)) break;
            schema.push(this.parseFieldDecl());
            this.skipNewlines();
          }
          this.expect(TokenType.DEDENT);
        } else if (this.check(TokenType.FLOW) || (this.check(TokenType.IDENT) && this.peek().value === "flow")) {
          // flow: A -> B -> C  or  flow: A, B, C
          this.advance();
          this.expect(TokenType.COLON);
          const flowParts = [this.expectIdent()];
          if (this.check(TokenType.ARROW)) {
            while (this.match(TokenType.ARROW)) {
              flowParts.push(this.expectIdent());
            }
          } else {
            while (this.match(TokenType.COMMA)) {
              flowParts.push(this.expectIdent());
            }
          }
          const flowStr: AST.Expression = {
            kind: "StringLiteral",
            value: flowParts.join(" -> "),
            loc: this.loc(),
          };
          properties.push({
            kind: "PropertyAssignment",
            name: "flow",
            value: flowStr,
            loc: this.loc(),
          });
          this.skipNewlines();
        } else {
          // model-level property
          const propName = this.expectIdent();
          this.expect(TokenType.COLON);
          const value = this.parseExpression();
          properties.push({
            kind: "PropertyAssignment",
            name: propName,
            value,
            loc: this.loc(),
          });
          this.skipNewlines();
        }
      }

      this.expect(TokenType.DEDENT);
    }

    return { kind: "ModelDecl", name, qualifier, schema, properties, loc };
  }

  private parseFieldDecl(): AST.FieldDecl {
    const loc = this.loc();
    const name = this.expectIdent();
    this.expect(TokenType.COLON);
    const typeExpr = this.parseTypeExpr();
    const annotations = this.parseAnnotations();
    let defaultValue: AST.Expression | undefined;
    if (this.match(TokenType.EQ)) {
      defaultValue = this.parseExpression();
    }
    return { kind: "FieldDecl", name, typeExpr, annotations, defaultValue, loc };
  }

  private parseAnnotations(): AST.Annotation[] {
    const annotations: AST.Annotation[] = [];
    while (this.check(TokenType.AT)) {
      annotations.push(this.parseAnnotation());
    }
    return annotations;
  }

  private parseAnnotation(): AST.Annotation {
    const loc = this.loc();
    this.expect(TokenType.AT);
    const name = this.expectIdent();
    const args: AST.AnnotationArg[] = [];
    if (this.match(TokenType.LPAREN)) {
      if (!this.check(TokenType.RPAREN)) {
        do {
          if (
            this.check(TokenType.IDENT) &&
            (this.peekNext()?.type === TokenType.EQ ||
             this.peekNext()?.type === TokenType.COLON)
          ) {
            const argName = this.expectIdent();
            this.advance(); // skip = or :
            const value = this.parseExpression();
            args.push({ name: argName, value });
          } else {
            args.push({ value: this.parseExpression() });
          }
        } while (this.match(TokenType.COMMA));
      }
      this.expect(TokenType.RPAREN);
    }
    return { kind: "Annotation", name, args, loc };
  }

  // ── Controller ──

  private parseController(): AST.ControllerDecl {
    const loc = this.loc();
    this.expect(TokenType.CONTROLLER);
    const name = this.expectIdent();
    this.expect(TokenType.LPAREN);
    const params = this.parseParamList();
    this.expect(TokenType.RPAREN);
    this.expect(TokenType.COLON);
    this.expectIndent();

    const variables: AST.VariableDecl[] = [];
    const events: AST.EventHandler[] = [];
    const methods: AST.MethodDecl[] = [];

    while (!this.check(TokenType.DEDENT) && !this.isAtEnd()) {
      this.skipNewlines();
      if (this.check(TokenType.DEDENT)) break;

      if (this.check(TokenType.EVENT)) {
        events.push(this.parseEventHandler());
      } else if (this.check(TokenType.DEF)) {
        methods.push(this.parseMethodDecl());
      } else {
        variables.push(this.parseVariableDecl());
      }
      this.skipNewlines();
    }

    this.expect(TokenType.DEDENT);
    return { kind: "ControllerDecl", name, params, variables, events, methods, loc };
  }

  private parseEventHandler(): AST.EventHandler {
    const loc = this.loc();
    this.expect(TokenType.EVENT);

    // Parse qualified name: model.event_name
    const target = [this.expectIdent()];
    while (this.match(TokenType.DOT)) {
      target.push(this.expectIdent());
    }

    this.expect(TokenType.LPAREN);
    const params = this.parseParamList();
    this.expect(TokenType.RPAREN);
    this.expect(TokenType.COLON);
    this.expectIndent();
    const body = this.parseBlock();
    this.expect(TokenType.DEDENT);

    return { kind: "EventHandler", target, params, body, loc };
  }

  private parseMethodDecl(): AST.MethodDecl {
    const loc = this.loc();
    this.expect(TokenType.DEF);
    const name = this.expectIdent();
    this.expect(TokenType.LPAREN);
    const params = this.parseParamList();
    this.expect(TokenType.RPAREN);

    let returnType: AST.TypeExpr | undefined;
    if (this.match(TokenType.ARROW)) {
      returnType = this.parseTypeExpr();
    }

    this.expect(TokenType.COLON);
    this.expectIndent();
    const body = this.parseBlock();
    this.expect(TokenType.DEDENT);

    return { kind: "MethodDecl", name, params, returnType, body, loc };
  }

  // ── View ──

  private parseView(): AST.ViewDecl {
    const loc = this.loc();
    this.expect(TokenType.VIEW);
    const name = this.expectIdent();
    this.expect(TokenType.COLON);
    this.expectIndent();

    let displays: AST.TypeExpr | undefined;
    const platforms: string[] = [];
    const behaviors: string[] = [];
    const style: AST.PropertyAssignment[] = [];
    const components: AST.ComponentDecl[] = [];
    const properties: AST.PropertyAssignment[] = [];

    while (!this.check(TokenType.DEDENT) && !this.isAtEnd()) {
      this.skipNewlines();
      if (this.check(TokenType.DEDENT)) break;

      const secName = this.peek().value;
      if (secName === "displays") {
        this.advance();
        this.expect(TokenType.COLON);
        displays = this.parseTypeExpr();
      } else if (secName === "platform") {
        this.advance();
        this.expect(TokenType.COLON);
        const list = this.parseListValue();
        platforms.push(...list);
      } else if (this.check(TokenType.BEHAVIOR)) {
        this.advance();
        this.expect(TokenType.COLON);
        this.expectIndent();
        while (!this.check(TokenType.DEDENT) && !this.isAtEnd()) {
          this.skipNewlines();
          if (this.check(TokenType.DEDENT)) break;
          if (this.check(TokenType.STRING)) {
            behaviors.push(this.advance().value);
          } else {
            break;
          }
          this.skipNewlines();
        }
        this.expect(TokenType.DEDENT);
      } else if (this.check(TokenType.STYLE)) {
        this.advance();
        this.expect(TokenType.COLON);
        this.expectIndent();
        style.push(...this.parsePropertyBlock());
        this.expect(TokenType.DEDENT);
      } else if (this.check(TokenType.COMPONENTS)) {
        this.advance();
        this.expect(TokenType.COLON);
        this.expectIndent();
        while (!this.check(TokenType.DEDENT) && !this.isAtEnd()) {
          this.skipNewlines();
          if (this.check(TokenType.DEDENT)) break;
          components.push(this.parseComponentDecl());
          this.skipNewlines();
        }
        this.expect(TokenType.DEDENT);
      } else {
        const propName = this.expectIdent();
        this.expect(TokenType.COLON);
        const value = this.parseExpression();
        properties.push({
          kind: "PropertyAssignment",
          name: propName,
          value,
          loc: this.loc(),
        });
      }
      this.skipNewlines();
    }

    this.expect(TokenType.DEDENT);
    return {
      kind: "ViewDecl",
      name,
      displays,
      platforms,
      behaviors,
      style,
      components,
      properties,
      loc,
    };
  }

  private parseComponentDecl(): AST.ComponentDecl {
    const loc = this.loc();
    const name = this.expectIdent();
    this.expect(TokenType.COLON);
    const type = this.expectIdent();
    const args: AST.NamedArg[] = [];
    if (this.match(TokenType.LPAREN)) {
      if (!this.check(TokenType.RPAREN)) {
        do {
          if (
            this.check(TokenType.IDENT) &&
            (this.peekNext()?.type === TokenType.EQ ||
             this.peekNext()?.type === TokenType.COLON)
          ) {
            const argName = this.expectIdent();
            this.advance(); // skip = or :
            const value = this.parseExpression();
            args.push({ name: argName, value });
          } else {
            args.push({ value: this.parseExpression() });
          }
        } while (this.match(TokenType.COMMA));
      }
      this.expect(TokenType.RPAREN);
    }
    return { kind: "ComponentDecl", name, type, args, loc };
  }

  // ── Service ──

  private parseService(): AST.ServiceDecl {
    const loc = this.loc();
    this.expect(TokenType.SERVICE);
    const name = this.expectIdent();
    this.expect(TokenType.COLON);
    this.expectIndent();

    let provider: string | undefined;
    const methods: AST.ServiceMethodDecl[] = [];
    const events: AST.ServiceEventDecl[] = [];
    const config: AST.PropertyAssignment[] = [];
    const properties: AST.PropertyAssignment[] = [];

    while (!this.check(TokenType.DEDENT) && !this.isAtEnd()) {
      this.skipNewlines();
      if (this.check(TokenType.DEDENT)) break;

      if (this.peek().value === "provider") {
        this.advance();
        this.expect(TokenType.COLON);
        provider = this.expectIdent();
      } else if (this.check(TokenType.DEF)) {
        methods.push(this.parseServiceMethod());
      } else if (this.check(TokenType.EVENT)) {
        events.push(this.parseServiceEvent());
      } else if (this.check(TokenType.CONFIG)) {
        this.advance();
        this.expect(TokenType.COLON);
        this.expectIndent();
        config.push(...this.parsePropertyBlock());
        this.expect(TokenType.DEDENT);
      } else {
        const propName = this.expectIdent();
        this.expect(TokenType.COLON);
        const value = this.parseExpression();
        properties.push({
          kind: "PropertyAssignment",
          name: propName,
          value,
          loc: this.loc(),
        });
      }
      this.skipNewlines();
    }

    this.expect(TokenType.DEDENT);
    return {
      kind: "ServiceDecl",
      name,
      provider,
      methods,
      events,
      config,
      properties,
      loc,
    };
  }

  private parseServiceMethod(): AST.ServiceMethodDecl {
    const loc = this.loc();
    this.expect(TokenType.DEF);
    const name = this.expectIdent();
    this.expect(TokenType.LPAREN);
    const params = this.parseParamList();
    this.expect(TokenType.RPAREN);
    let returnType: AST.TypeExpr | undefined;
    if (this.match(TokenType.ARROW)) {
      returnType = this.parseTypeExpr();
    }
    return { kind: "ServiceMethodDecl", name, params, returnType, loc };
  }

  private parseServiceEvent(): AST.ServiceEventDecl {
    const loc = this.loc();
    this.expect(TokenType.EVENT);
    const name = this.expectIdent();
    this.expect(TokenType.LPAREN);
    const params = this.parseParamList();
    this.expect(TokenType.RPAREN);
    return { kind: "ServiceEventDecl", name, params, loc };
  }

  // ── Runtime ──

  private parseRuntime(): AST.RuntimeDecl {
    const loc = this.loc();
    this.expect(TokenType.RUNTIME);
    const name = this.expectIdent();
    this.expect(TokenType.COLON);
    this.expectIndent();

    let platform: AST.Expression | undefined;
    let language: AST.Expression | undefined;
    let orchestration: string | undefined;
    const views: string[] = [];
    const controllers: string[] = [];
    const services: string[] = [];
    const models: string[] = [];
    const agents: string[] = [];
    const scaling: AST.PropertyAssignment[] = [];
    const config: AST.PropertyAssignment[] = [];
    const env: AST.PropertyAssignment[] = [];
    const properties: AST.PropertyAssignment[] = [];

    while (!this.check(TokenType.DEDENT) && !this.isAtEnd()) {
      this.skipNewlines();
      if (this.check(TokenType.DEDENT)) break;

      const secName = this.peek().value;
      if (secName === "platform") {
        this.advance();
        this.expect(TokenType.COLON);
        platform = this.parseExpression();
      } else if (secName === "language") {
        this.advance();
        this.expect(TokenType.COLON);
        language = this.parseExpression();
      } else if (secName === "orchestration") {
        this.advance();
        this.expect(TokenType.COLON);
        orchestration = this.expectIdent();
      } else if (secName === "views") {
        this.advance();
        this.expect(TokenType.COLON);
        views.push(...this.parseListValue());
      } else if (secName === "controllers") {
        this.advance();
        this.expect(TokenType.COLON);
        controllers.push(...this.parseListValue());
      } else if (secName === "services") {
        this.advance();
        this.expect(TokenType.COLON);
        services.push(...this.parseListValue());
      } else if (secName === "models") {
        this.advance();
        this.expect(TokenType.COLON);
        models.push(...this.parseListValue());
      } else if (secName === "agents") {
        this.advance();
        this.expect(TokenType.COLON);
        agents.push(...this.parseListValue());
      } else if (this.check(TokenType.SCALING)) {
        this.advance();
        this.expect(TokenType.COLON);
        this.expectIndent();
        scaling.push(...this.parsePropertyBlock());
        this.expect(TokenType.DEDENT);
      } else if (this.check(TokenType.CONFIG)) {
        this.advance();
        this.expect(TokenType.COLON);
        this.expectIndent();
        config.push(...this.parsePropertyBlock());
        this.expect(TokenType.DEDENT);
      } else if (this.check(TokenType.ENV)) {
        this.advance();
        this.expect(TokenType.COLON);
        this.expectIndent();
        env.push(...this.parsePropertyBlock());
        this.expect(TokenType.DEDENT);
      } else {
        const propName = this.expectIdent();
        this.expect(TokenType.COLON);
        const value = this.parseExpression();
        properties.push({
          kind: "PropertyAssignment",
          name: propName,
          value,
          loc: this.loc(),
        });
      }
      this.skipNewlines();
    }

    this.expect(TokenType.DEDENT);
    return {
      kind: "RuntimeDecl",
      name,
      platform,
      language,
      orchestration,
      views,
      controllers,
      services,
      models,
      agents,
      scaling,
      config,
      env,
      properties,
      loc,
    };
  }

  // ── Agent ──

  private parseAgent(): AST.AgentDecl {
    const loc = this.loc();
    this.expect(TokenType.AGENT);
    const name = this.expectIdent();
    this.expect(TokenType.COLON);
    this.expectIndent();

    let model: AST.Expression | undefined;
    const behaviors: string[] = [];
    const inputs: AST.TypeExpr[] = [];
    let output: AST.TypeExpr | undefined;
    const outputAnnotations: AST.Annotation[] = [];
    const tools: AST.Expression[] = [];
    const properties: AST.PropertyAssignment[] = [];

    while (!this.check(TokenType.DEDENT) && !this.isAtEnd()) {
      this.skipNewlines();
      if (this.check(TokenType.DEDENT)) break;

      const secName = this.peek().value;
      if (secName === "model") {
        this.advance();
        this.expect(TokenType.COLON);
        model = this.parseExpression();
      } else if (this.check(TokenType.BEHAVIOR)) {
        this.advance();
        this.expect(TokenType.COLON);
        this.expectIndent();
        while (!this.check(TokenType.DEDENT) && !this.isAtEnd()) {
          this.skipNewlines();
          if (this.check(TokenType.DEDENT)) break;
          if (this.check(TokenType.STRING)) {
            behaviors.push(this.advance().value);
          } else {
            break;
          }
          this.skipNewlines();
        }
        this.expect(TokenType.DEDENT);
      } else if (secName === "input") {
        this.advance();
        this.expect(TokenType.COLON);
        inputs.push(this.parseTypeExpr());
        while (this.match(TokenType.COMMA)) {
          inputs.push(this.parseTypeExpr());
        }
      } else if (secName === "output") {
        this.advance();
        this.expect(TokenType.COLON);
        output = this.parseTypeExpr();
        outputAnnotations.push(...this.parseAnnotations());
      } else if (secName === "tools") {
        this.advance();
        this.expect(TokenType.COLON);
        if (this.check(TokenType.LBRACK)) {
          const list = this.parseListExpr();
          tools.push(...list.elements);
        } else {
          tools.push(this.parseExpression());
        }
      } else {
        const propName = this.expectIdent();
        this.expect(TokenType.COLON);
        const value = this.parseExpression();
        properties.push({
          kind: "PropertyAssignment",
          name: propName,
          value,
          loc: this.loc(),
        });
      }
      this.skipNewlines();
    }

    this.expect(TokenType.DEDENT);
    return {
      kind: "AgentDecl",
      name,
      model,
      behaviors,
      inputs,
      output,
      outputAnnotations,
      tools,
      properties,
      loc,
    };
  }

  // ── Flow ──

  private parseFlow(): AST.FlowDecl {
    const loc = this.loc();
    this.expect(TokenType.FLOW);
    const name = this.expectIdent();
    this.expect(TokenType.COLON);
    this.expectIndent();

    const rules: AST.FlowRule[] = [];
    while (!this.check(TokenType.DEDENT) && !this.isAtEnd()) {
      this.skipNewlines();
      if (this.check(TokenType.DEDENT)) break;

      const source = this.expectIdent();
      this.expect(TokenType.ARROW);
      const target = this.expectIdent();

      const ruleProps: AST.PropertyAssignment[] = [];
      if (this.match(TokenType.COLON)) {
        this.expectIndent();
        ruleProps.push(...this.parsePropertyBlock());
        this.expect(TokenType.DEDENT);
      }

      rules.push({
        kind: "FlowRule",
        source,
        target,
        properties: ruleProps,
        loc: this.loc(),
      });
      this.skipNewlines();
    }

    this.expect(TokenType.DEDENT);
    return { kind: "FlowDecl", name, rules, loc };
  }

  // ─────────────────────── Type Expressions ──────────────────────

  private parseTypeExpr(): AST.TypeExpr {
    let type = this.parseBaseType();

    // Postfix: [], [N], ?, -> Model.field
    while (true) {
      if (this.match(TokenType.LBRACK)) {
        if (this.check(TokenType.INT_LIT)) {
          const size = parseInt(this.advance().value, 10);
          this.expect(TokenType.RBRACK);
          type = { kind: "ArrayTypeExpr", element: type, size, loc: type.loc };
        } else {
          this.expect(TokenType.RBRACK);
          type = { kind: "ArrayTypeExpr", element: type, loc: type.loc };
        }
      } else if (this.match(TokenType.QUESTION)) {
        type = { kind: "OptionalTypeExpr", inner: type, loc: type.loc };
      } else if (this.match(TokenType.ARROW)) {
        const refModel = this.expectIdent();
        this.expect(TokenType.DOT);
        const refField = this.expectIdent();
        type = {
          kind: "ForeignKeyTypeExpr",
          type,
          refModel,
          refField,
          loc: type.loc,
        };
      } else {
        break;
      }
    }

    return type;
  }

  private parseBaseType(): AST.TypeExpr {
    const loc = this.loc();

    // map<K, V>
    if (this.check(TokenType.MAP)) {
      this.advance();
      this.expect(TokenType.LT);
      const key = this.parseTypeExpr();
      this.expect(TokenType.COMMA);
      const value = this.parseTypeExpr();
      this.expect(TokenType.GT);
      return { kind: "MapTypeExpr", key, value, loc };
    }

    // enum("a", "b", "c")
    if (this.check(TokenType.ENUM)) {
      this.advance();
      this.expect(TokenType.LPAREN);
      const members: string[] = [];
      if (!this.check(TokenType.RPAREN)) {
        members.push(this.expectString());
        while (this.match(TokenType.COMMA)) {
          members.push(this.expectString());
        }
      }
      this.expect(TokenType.RPAREN);
      return { kind: "EnumTypeExpr", members, loc };
    }

    // (T1, T2) tuple
    if (this.check(TokenType.LPAREN)) {
      this.advance();
      const elements: AST.TypeExpr[] = [this.parseTypeExpr()];
      while (this.match(TokenType.COMMA)) {
        elements.push(this.parseTypeExpr());
      }
      this.expect(TokenType.RPAREN);
      if (elements.length === 1) return elements[0];
      return { kind: "TupleTypeExpr", elements, loc };
    }

    // Primitive types
    if (PRIMITIVE_TYPE_TOKENS.has(this.peek().type)) {
      const name = this.advance().value;
      return { kind: "PrimitiveTypeExpr", name, loc };
    }

    // Named type
    if (this.check(TokenType.IDENT)) {
      const name = this.advance().value;
      return { kind: "NamedTypeExpr", name, loc };
    }

    throw this.error(`Expected type expression, got '${this.peek().value}'`);
  }

  // ─────────────────────── Statements ────────────────────────────

  private parseBlock(): AST.Statement[] {
    const stmts: AST.Statement[] = [];
    while (!this.check(TokenType.DEDENT) && !this.isAtEnd()) {
      this.skipNewlines();
      if (this.check(TokenType.DEDENT)) break;
      stmts.push(this.parseStatement());
      this.skipNewlines();
    }
    return stmts;
  }

  private parseStatement(): AST.Statement {
    if (this.check(TokenType.IF)) return this.parseIfStmt();
    if (this.check(TokenType.FOR)) return this.parseForStmt();
    if (this.check(TokenType.WHILE)) return this.parseWhileStmt();
    if (this.check(TokenType.RETURN)) return this.parseReturnStmt();
    if (this.check(TokenType.DEL)) return this.parseDeleteStmt();
    if (this.check(TokenType.PASS)) return this.parsePassStmt();
    if (this.check(TokenType.BREAK)) return this.parseBreakStmt();
    if (this.check(TokenType.CONTINUE)) return this.parseContinueStmt();
    if (this.check(TokenType.EMIT)) return this.parseEmitStmt();
    if (this.check(TokenType.AWAIT)) return this.parseAwaitStmt();
    if (this.check(TokenType.LOG)) return this.parseLogStmt();

    // Variable declaration with type: `x : int = 5`
    if (
      this.check(TokenType.IDENT) &&
      this.peekNext()?.type === TokenType.COLON &&
      !this.isAtBlockSectionBoundary()
    ) {
      return this.parseVariableDecl();
    }

    // Expression or assignment
    const expr = this.parseExpression();
    if (
      this.check(TokenType.EQ) ||
      this.check(TokenType.PLUSEQ) ||
      this.check(TokenType.MINUSEQ)
    ) {
      const op = this.advance().value as "=" | "+=" | "-=";
      const value = this.parseExpression();
      return {
        kind: "AssignmentStmt",
        target: expr,
        op,
        value,
        loc: expr.loc,
      };
    }

    return { kind: "ExpressionStmt", expr, loc: expr.loc };
  }

  private parseVariableDecl(): AST.VariableDecl {
    const loc = this.loc();
    const name = this.expectIdent();
    let typeExpr: AST.TypeExpr | undefined;
    let value: AST.Expression | undefined;

    if (this.match(TokenType.COLON)) {
      typeExpr = this.parseTypeExpr();
    }
    if (this.match(TokenType.EQ)) {
      value = this.parseExpression();
    }

    return { kind: "VariableDecl", name, typeExpr, value, loc };
  }

  private parseIfStmt(): AST.IfStmt {
    const loc = this.loc();
    this.expect(TokenType.IF);
    const condition = this.parseExpression();
    this.expect(TokenType.COLON);
    this.expectIndent();
    const body = this.parseBlock();
    this.expect(TokenType.DEDENT);

    const elifs: { condition: AST.Expression; body: AST.Statement[] }[] = [];
    while (this.check(TokenType.ELIF)) {
      this.advance();
      const elifCond = this.parseExpression();
      this.expect(TokenType.COLON);
      this.expectIndent();
      const elifBody = this.parseBlock();
      this.expect(TokenType.DEDENT);
      elifs.push({ condition: elifCond, body: elifBody });
    }

    let elseBody: AST.Statement[] | undefined;
    if (this.check(TokenType.ELSE)) {
      this.advance();
      this.expect(TokenType.COLON);
      this.expectIndent();
      elseBody = this.parseBlock();
      this.expect(TokenType.DEDENT);
    }

    return { kind: "IfStmt", condition, body, elifs, elseBody, loc };
  }

  private parseForStmt(): AST.ForStmt {
    const loc = this.loc();
    this.expect(TokenType.FOR);
    const variable = this.expectIdent();
    this.expect(TokenType.IN);
    const iterable = this.parseExpression();
    this.expect(TokenType.COLON);
    this.expectIndent();
    const body = this.parseBlock();
    this.expect(TokenType.DEDENT);
    return { kind: "ForStmt", variable, iterable, body, loc };
  }

  private parseWhileStmt(): AST.WhileStmt {
    const loc = this.loc();
    this.expect(TokenType.WHILE);
    const condition = this.parseExpression();
    this.expect(TokenType.COLON);
    this.expectIndent();
    const body = this.parseBlock();
    this.expect(TokenType.DEDENT);
    return { kind: "WhileStmt", condition, body, loc };
  }

  private parseReturnStmt(): AST.ReturnStmt {
    const loc = this.loc();
    this.expect(TokenType.RETURN);
    let value: AST.Expression | undefined;
    if (
      !this.check(TokenType.NEWLINE) &&
      !this.check(TokenType.DEDENT) &&
      !this.isAtEnd()
    ) {
      value = this.parseExpression();
    }
    return { kind: "ReturnStmt", value, loc };
  }

  private parseDeleteStmt(): AST.DeleteStmt {
    const loc = this.loc();
    this.expect(TokenType.DEL);
    const target = this.parseExpression();
    return { kind: "DeleteStmt", target, loc };
  }

  private parsePassStmt(): AST.PassStmt {
    const loc = this.loc();
    this.expect(TokenType.PASS);
    return { kind: "PassStmt", loc };
  }

  private parseBreakStmt(): AST.BreakStmt {
    const loc = this.loc();
    this.expect(TokenType.BREAK);
    return { kind: "BreakStmt", loc };
  }

  private parseContinueStmt(): AST.ContinueStmt {
    const loc = this.loc();
    this.expect(TokenType.CONTINUE);
    return { kind: "ContinueStmt", loc };
  }

  private parseEmitStmt(): AST.EmitStmt {
    const loc = this.loc();
    this.expect(TokenType.EMIT);
    const event = this.parseExpression();
    return { kind: "EmitStmt", event, loc };
  }

  private parseAwaitStmt(): AST.AwaitStmt {
    const loc = this.loc();
    this.expect(TokenType.AWAIT);
    const expr = this.parseExpression();
    return { kind: "AwaitStmt", expr, loc };
  }

  private parseLogStmt(): AST.LogStmt {
    const loc = this.loc();
    this.expect(TokenType.LOG);
    const expr = this.parseExpression();
    return { kind: "LogStmt", expr, loc };
  }

  // ─────────────────── Expressions (Pratt parsing) ──────────────

  private parseExpression(): AST.Expression {
    return this.parseOr();
  }

  private parseOr(): AST.Expression {
    let left = this.parseAnd();
    while (this.check(TokenType.OR) || this.check(TokenType.PIPEPIPE)) {
      this.advance();
      const right = this.parseAnd();
      left = { kind: "BinaryExpr", op: "or", left, right, loc: left.loc };
    }
    return left;
  }

  private parseAnd(): AST.Expression {
    let left = this.parseBitwiseOr();
    while (this.check(TokenType.AND) || this.check(TokenType.AMPAMP)) {
      this.advance();
      const right = this.parseBitwiseOr();
      left = { kind: "BinaryExpr", op: "and", left, right, loc: left.loc };
    }
    return left;
  }

  private parseBitwiseOr(): AST.Expression {
    let left = this.parseBitwiseXor();
    while (this.check(TokenType.PIPE)) {
      this.advance();
      const right = this.parseBitwiseXor();
      left = { kind: "BinaryExpr", op: "|", left, right, loc: left.loc };
    }
    return left;
  }

  private parseBitwiseXor(): AST.Expression {
    let left = this.parseBitwiseAnd();
    while (this.check(TokenType.CARET)) {
      this.advance();
      const right = this.parseBitwiseAnd();
      left = { kind: "BinaryExpr", op: "^", left, right, loc: left.loc };
    }
    return left;
  }

  private parseBitwiseAnd(): AST.Expression {
    let left = this.parseComparison();
    while (this.check(TokenType.AMP)) {
      this.advance();
      const right = this.parseComparison();
      left = { kind: "BinaryExpr", op: "&", left, right, loc: left.loc };
    }
    return left;
  }

  private parseComparison(): AST.Expression {
    let left = this.parseShift();
    const compOps = new Set([
      TokenType.EQEQ,
      TokenType.NEQ,
      TokenType.LT,
      TokenType.GT,
      TokenType.LTE,
      TokenType.GTE,
    ]);
    while (compOps.has(this.peek().type)) {
      const opToken = this.advance();
      const op = opToken.value as AST.BinaryOp;
      const right = this.parseShift();
      left = { kind: "BinaryExpr", op, left, right, loc: left.loc };
    }
    return left;
  }

  private parseShift(): AST.Expression {
    let left = this.parseAddSub();
    while (this.check(TokenType.LSHIFT) || this.check(TokenType.RSHIFT)) {
      const op = this.advance().value as AST.BinaryOp;
      const right = this.parseAddSub();
      left = { kind: "BinaryExpr", op, left, right, loc: left.loc };
    }
    return left;
  }

  private parseAddSub(): AST.Expression {
    let left = this.parseMulDiv();
    while (this.check(TokenType.PLUS) || this.check(TokenType.MINUS)) {
      const op = this.advance().value as AST.BinaryOp;
      const right = this.parseMulDiv();
      left = { kind: "BinaryExpr", op, left, right, loc: left.loc };
    }
    return left;
  }

  private parseMulDiv(): AST.Expression {
    let left = this.parseUnary();
    while (
      this.check(TokenType.STAR) ||
      this.check(TokenType.SLASH) ||
      this.check(TokenType.PERCENT)
    ) {
      const op = this.advance().value as AST.BinaryOp;
      const right = this.parseUnary();
      left = { kind: "BinaryExpr", op, left, right, loc: left.loc };
    }
    return left;
  }

  private parseUnary(): AST.Expression {
    if (this.check(TokenType.MINUS)) {
      const loc = this.loc();
      this.advance();
      const operand = this.parseUnary();
      return { kind: "UnaryExpr", op: "-", operand, loc };
    }
    if (this.check(TokenType.NOT)) {
      const loc = this.loc();
      this.advance();
      const operand = this.parseUnary();
      return { kind: "UnaryExpr", op: "not", operand, loc };
    }
    if (this.check(TokenType.TILDE)) {
      const loc = this.loc();
      this.advance();
      const operand = this.parseUnary();
      return { kind: "UnaryExpr", op: "~", operand, loc };
    }
    return this.parsePostfix();
  }

  private parsePostfix(): AST.Expression {
    let expr = this.parsePrimary();

    while (true) {
      if (this.check(TokenType.DOT)) {
        this.advance();
        const property = this.expectIdent();
        expr = { kind: "MemberExpr", object: expr, property, loc: expr.loc };
      } else if (this.check(TokenType.LPAREN)) {
        this.advance();
        const args: AST.NamedArg[] = [];
        if (!this.check(TokenType.RPAREN)) {
          do {
            if (
              this.check(TokenType.IDENT) &&
              (this.peekNext()?.type === TokenType.EQ ||
               this.peekNext()?.type === TokenType.COLON)
            ) {
              const name = this.expectIdent();
              this.advance(); // skip = or :
              const value = this.parseExpression();
              args.push({ name, value });
            } else {
              args.push({ value: this.parseExpression() });
            }
          } while (this.match(TokenType.COMMA));
        }
        this.expect(TokenType.RPAREN);
        expr = { kind: "CallExpr", callee: expr, args, loc: expr.loc };
      } else if (this.check(TokenType.LBRACK)) {
        this.advance();
        const index = this.parseExpression();
        this.expect(TokenType.RBRACK);
        expr = { kind: "IndexExpr", object: expr, index, loc: expr.loc };
      } else {
        break;
      }
    }

    return expr;
  }

  private parsePrimary(): AST.Expression {
    const loc = this.loc();

    // Integer literal
    if (this.check(TokenType.INT_LIT)) {
      const value = parseInt(this.advance().value, 10);
      return { kind: "IntLiteral", value, loc };
    }

    // Float literal
    if (this.check(TokenType.FLOAT_LIT)) {
      const value = parseFloat(this.advance().value);
      return { kind: "FloatLiteral", value, loc };
    }

    // String literal
    if (this.check(TokenType.STRING)) {
      const value = this.advance().value;
      return { kind: "StringLiteral", value, loc };
    }

    // Boolean literals
    if (this.check(TokenType.TRUE)) {
      this.advance();
      return { kind: "BoolLiteral", value: true, loc };
    }
    if (this.check(TokenType.FALSE)) {
      this.advance();
      return { kind: "BoolLiteral", value: false, loc };
    }

    // None
    if (this.check(TokenType.NONE)) {
      this.advance();
      return { kind: "NoneLiteral", loc };
    }

    // Duration literal
    if (this.check(TokenType.DURATION_LIT)) {
      const raw = this.advance().value;
      const unit = raw[raw.length - 1] as "s" | "m" | "h" | "d";
      const value = parseInt(raw.slice(0, -1), 10);
      return { kind: "DurationLiteral", value, unit, loc };
    }

    // Size literal
    if (this.check(TokenType.SIZE_LIT)) {
      const raw = this.advance().value;
      const match = raw.match(/^(\d+)(kb|mb|gb|tb)$/);
      if (!match) throw this.error(`Invalid size literal: ${raw}`);
      return {
        kind: "SizeLiteral",
        value: parseInt(match[1], 10),
        unit: match[2] as "kb" | "mb" | "gb" | "tb",
        loc,
      };
    }

    // Rate literal
    if (this.check(TokenType.RATE_LIT)) {
      const raw = this.advance().value;
      const match = raw.match(/^(\d+)\/(s|min|h|d)$/);
      if (!match) throw this.error(`Invalid rate literal: ${raw}`);
      return {
        kind: "RateLiteral",
        value: parseInt(match[1], 10),
        per: match[2] as "s" | "min" | "h" | "d",
        loc,
      };
    }

    // Percent literal
    if (this.check(TokenType.PERCENT_LIT)) {
      const raw = this.advance().value;
      const value = parseInt(raw.slice(0, -1), 10);
      return { kind: "PercentLiteral", value, loc };
    }

    // error(...)
    if (this.check(TokenType.ERROR)) {
      this.advance();
      this.expect(TokenType.LPAREN);
      const message = this.parseExpression();
      this.expect(TokenType.RPAREN);
      return { kind: "ErrorExpr", message, loc };
    }

    // len(...)
    if (this.peek().value === "len" && this.peekNext()?.type === TokenType.LPAREN) {
      this.advance();
      this.expect(TokenType.LPAREN);
      const expr = this.parseExpression();
      this.expect(TokenType.RPAREN);
      return { kind: "LenExpr", expr, loc };
    }

    // List expression
    if (this.check(TokenType.LBRACK)) {
      return this.parseListExpr();
    }

    // Map expression
    if (this.check(TokenType.LBRACE)) {
      return this.parseMapExpr();
    }

    // Parenthesized expression
    if (this.check(TokenType.LPAREN)) {
      this.advance();
      const expr = this.parseExpression();
      this.expect(TokenType.RPAREN);
      return expr;
    }

    // Identifier (or keyword used as identifier value in expressions)
    if (this.check(TokenType.IDENT)) {
      const name = this.advance().value;
      return { kind: "Identifier", name, loc };
    }

    // Allow certain keywords to be used as identifier values in expressions
    // e.g., `sync: replicated`, `platform: container`, `orchestration: kubernetes`
    const EXPR_KEYWORDS = new Set([
      TokenType.LOCAL, TokenType.STREAMING, TokenType.REPLICATED,
      TokenType.EPHEMERAL, TokenType.AGENT, TokenType.SERVICE,
      TokenType.RUNTIME, TokenType.SYSTEM, TokenType.VIEW,
      TokenType.CONTROLLER, TokenType.MODEL, TokenType.FLOW,
      TokenType.SCHEMA, TokenType.STYLE, TokenType.CONFIG,
      TokenType.ENV, TokenType.MAP, TokenType.ENUM,
      // Type names used as identifiers in property values
      TokenType.INT, TokenType.INT32, TokenType.INT64,
      TokenType.FLOAT, TokenType.DOUBLE, TokenType.DECIMAL,
      TokenType.STRING_TYPE, TokenType.STR,
      TokenType.BOOL, TokenType.BYTE, TokenType.BYTES,
      TokenType.UUID, TokenType.DATETIME, TokenType.TIMESTAMP,
      TokenType.JSON_TYPE, TokenType.VOID,
      // Other keywords used as values
      TokenType.EVENT, TokenType.DEF,
      TokenType.BEHAVIOR, TokenType.SCALING, TokenType.COMPONENTS,
    ]);
    if (EXPR_KEYWORDS.has(this.peek().type)) {
      const name = this.advance().value;
      return { kind: "Identifier", name, loc };
    }

    throw this.error(`Expected expression, got '${this.peek().value}' (${this.peek().type})`);
  }

  private parseListExpr(): AST.ListExpr {
    const loc = this.loc();
    this.expect(TokenType.LBRACK);
    const elements: AST.Expression[] = [];
    if (!this.check(TokenType.RBRACK)) {
      elements.push(this.parseExpression());
      while (this.match(TokenType.COMMA)) {
        if (this.check(TokenType.RBRACK)) break; // trailing comma
        elements.push(this.parseExpression());
      }
    }
    this.expect(TokenType.RBRACK);
    return { kind: "ListExpr", elements, loc };
  }

  private parseMapExpr(): AST.MapExpr {
    const loc = this.loc();
    this.expect(TokenType.LBRACE);
    const entries: AST.MapEntry[] = [];
    if (!this.check(TokenType.RBRACE)) {
      entries.push(this.parseMapEntry());
      while (this.match(TokenType.COMMA)) {
        if (this.check(TokenType.RBRACE)) break;
        entries.push(this.parseMapEntry());
      }
    }
    this.expect(TokenType.RBRACE);
    return { kind: "MapExpr", entries, loc };
  }

  private parseMapEntry(): AST.MapEntry {
    let key: string;
    if (this.check(TokenType.STRING)) {
      key = this.advance().value;
    } else {
      key = this.expectIdent();
    }
    this.expect(TokenType.COLON);
    const value = this.parseExpression();
    return { key, value };
  }

  // ─────────────────────── Helpers ───────────────────────────────

  private parseParamList(): AST.Parameter[] {
    const params: AST.Parameter[] = [];
    if (this.check(TokenType.RPAREN)) return params;

    params.push(this.parseParam());
    while (this.match(TokenType.COMMA)) {
      params.push(this.parseParam());
    }
    return params;
  }

  private parseParam(): AST.Parameter {
    const loc = this.loc();
    const name = this.expectIdent();
    this.expect(TokenType.COLON);
    const typeExpr = this.parseTypeExpr();
    let defaultValue: AST.Expression | undefined;
    if (this.match(TokenType.EQ)) {
      defaultValue = this.parseExpression();
    }
    return { kind: "Parameter", name, typeExpr, defaultValue, loc };
  }

  private parsePropertyBlock(): AST.PropertyAssignment[] {
    const props: AST.PropertyAssignment[] = [];
    while (!this.check(TokenType.DEDENT) && !this.isAtEnd()) {
      this.skipNewlines();
      if (this.check(TokenType.DEDENT)) break;

      const loc = this.loc();
      const name = this.expectIdent();
      this.expect(TokenType.COLON);
      const value = this.parseExpression();
      props.push({ kind: "PropertyAssignment", name, value, loc });
      this.skipNewlines();
    }
    return props;
  }

  /** Parse a list value like `[A, B, C]` or just `A` returning string[] */
  private parseListValue(): string[] {
    if (this.check(TokenType.LBRACK)) {
      this.advance();
      const items: string[] = [];
      if (!this.check(TokenType.RBRACK)) {
        items.push(this.expectIdent());
        while (this.match(TokenType.COMMA)) {
          if (this.check(TokenType.RBRACK)) break;
          items.push(this.expectIdent());
        }
      }
      this.expect(TokenType.RBRACK);
      return items;
    }
    return [this.expectIdent()];
  }

  /** Check if current token starts a block-level section boundary */
  private isAtBlockSectionBoundary(): boolean {
    // Look ahead: if after ident : we see INDENT, it's a section not a var decl
    if (this.pos + 2 < this.tokens.length) {
      const afterColon = this.tokens[this.pos + 2];
      return afterColon?.type === TokenType.INDENT;
    }
    return false;
  }

  // ─────────────────────── Token navigation ─────────────────────

  private peek(): Token {
    return this.tokens[this.pos] ?? { type: TokenType.EOF, value: "", loc: { line: 0, column: 0 } };
  }

  private peekNext(): Token | undefined {
    return this.tokens[this.pos + 1];
  }

  private advance(): Token {
    const token = this.peek();
    this.pos++;
    return token;
  }

  private check(type: TokenType): boolean {
    return this.peek().type === type;
  }

  private match(type: TokenType): boolean {
    if (this.check(type)) {
      this.advance();
      return true;
    }
    return false;
  }

  private expect(type: TokenType): Token {
    if (this.check(type)) return this.advance();
    throw this.error(
      `Expected ${type}, got '${this.peek().value}' (${this.peek().type})`,
    );
  }

  private expectIdent(): string {
    // Accept IDENT or keywords that can be used as identifiers in certain contexts
    if (this.check(TokenType.IDENT)) {
      return this.advance().value;
    }
    // Allow keywords used as property/field names
    const IDENT_KEYWORDS = new Set([
      TokenType.FLOW, TokenType.MODEL, TokenType.SCHEMA,
      TokenType.BEHAVIOR, TokenType.STYLE, TokenType.SCALING,
      TokenType.CONFIG, TokenType.ENV, TokenType.COMPONENTS,
      TokenType.LOCAL, TokenType.STREAMING, TokenType.REPLICATED,
      TokenType.EPHEMERAL, TokenType.AGENT, TokenType.SERVICE,
      TokenType.RUNTIME, TokenType.SYSTEM, TokenType.VIEW,
      TokenType.CONTROLLER, TokenType.EVENT, TokenType.ERROR,
      TokenType.MAP, TokenType.ENUM, TokenType.INT, TokenType.FLOAT,
      TokenType.DOUBLE, TokenType.BOOL, TokenType.STRING_TYPE,
      TokenType.BYTE, TokenType.BYTES, TokenType.UUID,
      TokenType.DATETIME, TokenType.TIMESTAMP, TokenType.VOID,
      TokenType.EMIT, TokenType.LOG,
    ]);
    if (IDENT_KEYWORDS.has(this.peek().type)) {
      return this.advance().value;
    }
    throw this.error(`Expected identifier, got '${this.peek().value}' (${this.peek().type})`);
  }

  private expectString(): string {
    const tok = this.expect(TokenType.STRING);
    return tok.value;
  }

  private isModelQualifier(): boolean {
    return MODEL_QUALIFIER_TOKENS.has(this.peek().type);
  }

  private isAtEnd(): boolean {
    return this.peek().type === TokenType.EOF;
  }

  private skipNewlines(): void {
    while (this.check(TokenType.NEWLINE)) {
      this.advance();
    }
  }

  /** Expect an INDENT, skipping any preceding newlines */
  private expectIndent(): Token {
    this.skipNewlines();
    return this.expect(TokenType.INDENT);
  }

  private loc(): SourceLocation {
    return { ...this.peek().loc };
  }

  private error(msg: string): ParseError {
    return new ParseError(msg, this.loc());
  }
}

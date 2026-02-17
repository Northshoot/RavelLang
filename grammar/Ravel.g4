/**
 * Ravel v2 — A Language for Orchestrating Full-Stack Systems via LLM Agents
 *
 * Evolution of the Stanford Ravel IoT framework (Riliskis, Hong, Levis 2015).
 * Original Ravel introduced distributed MVC for IoT with models, controllers,
 * views, interfaces, and spaces. This grammar modernizes those abstractions for
 * the era of AI agents and full-stack systems:
 *
 *   Original Ravel         Modern Ravel
 *   ─────────────         ─────────────
 *   model (data+flow)  →  model   (data + storage + sync + cache)
 *   controller (events)→  controller (events + AI-augmented logic)
 *   view (UI)          →  view    (declarative UI + behavior specs)
 *   interface (hw/sw)  →  service (cloud/API integration)
 *   space (platform)   →  runtime (container, mobile, serverless, edge)
 *   —                  →  agent   (LLM-powered component)
 *   flow (data pipes)  →  flow    (typed data pipelines + protocols)
 *   —                  →  system  (top-level composition)
 *
 * Compilation target: not platform source code, but an Agent Execution Plan
 * that LLM agents carry out to produce the entire project.
 *
 * Grammar: ANTLR4, Python-style indentation (INDENT/DEDENT tokens injected
 * by the lexer, same technique as original Ravel and CPython).
 */

grammar Ravel;

// ─────────────────────────── Top-Level ───────────────────────────

program
    : NEWLINE* (declaration NEWLINE*)* EOF
    ;

declaration
    : importStmt
    | systemDecl
    | modelDecl
    | controllerDecl
    | viewDecl
    | serviceDecl
    | runtimeDecl
    | agentDecl
    | flowDecl
    ;

// ─────────────────────────── Imports ────────────────────────────

importStmt
    : 'from' modulePath 'import' importList
    | 'import' modulePath ('as' IDENT)?
    ;

modulePath
    : IDENT ('.' IDENT)*
    ;

importList
    : IDENT (',' IDENT)*
    | '*'
    ;

// ─────────────────────── System Declaration ─────────────────────

systemDecl
    : 'system' IDENT ':' INDENT systemBody DEDENT
    ;

systemBody
    : (systemProp NEWLINE)*
    ;

systemProp
    : 'description' ':' STRING
    | 'version' ':' STRING
    | 'author' ':' STRING
    | 'license' ':' STRING
    | IDENT ':' expr
    ;

// ────────────────────── Model Declaration ───────────────────────

modelDecl
    : modelQualifier? 'model' IDENT (':' INDENT modelBody DEDENT)?
    ;

modelQualifier
    : 'local'
    | 'streaming'
    | 'replicated'
    | 'ephemeral'
    ;

modelBody
    : (modelSection NEWLINE*)*
    ;

modelSection
    : schemaSection
    | 'storage' ':' expr
    | 'sync' ':' IDENT
    | 'cache' ':' expr
    | 'flow' ':' flowExpr
    | 'index' ':' indexExpr
    | 'retention' ':' expr
    | 'encryption' ':' expr
    | IDENT ':' expr
    ;

schemaSection
    : 'schema' ':' INDENT (fieldDecl NEWLINE)* DEDENT
    ;

fieldDecl
    : IDENT ':' typeExpr annotation* ('=' expr)?
    ;

annotation
    : '@' IDENT ('(' annotationArgs ')')?
    ;

annotationArgs
    : annotationArg (',' annotationArg)*
    ;

annotationArg
    : IDENT '=' expr
    | expr
    ;

indexExpr
    : '[' IDENT (',' IDENT)* ']'
    | IDENT
    ;

flowExpr
    : flowNode ('->' flowNode)+    // streaming: A -> B -> C
    | flowNode (',' flowNode)+     // replicated: A, B, C
    | flowNode                     // local (single space)
    ;

flowNode
    : IDENT
    ;

// ─────────────────── Controller Declaration ─────────────────────

controllerDecl
    : 'controller' IDENT '(' paramList ')' ':' INDENT controllerBody DEDENT
    ;

controllerBody
    : (controllerMember NEWLINE*)*
    ;

controllerMember
    : variableDecl
    | eventHandler
    | methodDecl
    ;

variableDecl
    : IDENT ':' typeExpr ('=' expr)?
    | IDENT '=' expr
    ;

eventHandler
    : 'event' qualifiedName '(' paramList? ')' ':' INDENT block DEDENT
    ;

methodDecl
    : 'def' IDENT '(' paramList? ')' (':' typeExpr)? ':' INDENT block DEDENT
    ;

qualifiedName
    : IDENT ('.' IDENT)*
    ;

// ───────────────────── View Declaration ─────────────────────────

viewDecl
    : 'view' IDENT ':' INDENT viewBody DEDENT
    ;

viewBody
    : (viewSection NEWLINE*)*
    ;

viewSection
    : 'displays' ':' typeExpr
    | 'platform' ':' listExpr
    | behaviorSection
    | styleSection
    | componentsSection
    | 'layout' ':' IDENT
    | IDENT ':' expr
    ;

behaviorSection
    : 'behavior' ':' INDENT (STRING NEWLINE)* DEDENT
    ;

styleSection
    : 'style' ':' INDENT (styleProp NEWLINE)* DEDENT
    ;

styleProp
    : IDENT ':' expr
    ;

componentsSection
    : 'components' ':' INDENT (componentDecl NEWLINE)* DEDENT
    ;

componentDecl
    : IDENT ':' IDENT '(' namedArgList? ')'
    | IDENT ':' IDENT
    ;

// ──────────────────── Service Declaration ───────────────────────

serviceDecl
    : 'service' IDENT ':' INDENT serviceBody DEDENT
    ;

serviceBody
    : (serviceMember NEWLINE*)*
    ;

serviceMember
    : 'provider' ':' IDENT
    | 'base_url' ':' STRING
    | 'auth' ':' IDENT
    | serviceMethodDecl
    | serviceEventDecl
    | configSection
    ;

serviceMethodDecl
    : 'def' IDENT '(' paramList? ')' ('->' typeExpr)?
    ;

serviceEventDecl
    : 'event' IDENT '(' paramList? ')'
    ;

configSection
    : 'config' ':' INDENT (configProp NEWLINE)* DEDENT
    ;

configProp
    : IDENT ':' expr
    ;

// ──────────────────── Runtime Declaration ───────────────────────

runtimeDecl
    : 'runtime' IDENT ':' INDENT runtimeBody DEDENT
    ;

runtimeBody
    : (runtimeSection NEWLINE*)*
    ;

runtimeSection
    : 'platform' ':' expr
    | 'language' ':' expr
    | 'orchestration' ':' IDENT
    | 'views' ':' listExpr
    | 'controllers' ':' listExpr
    | 'services' ':' listExpr
    | 'models' ':' listExpr
    | 'agents' ':' listExpr
    | scalingSection
    | configSection
    | envSection
    | IDENT ':' expr
    ;

scalingSection
    : 'scaling' ':' INDENT (scalingProp NEWLINE)* DEDENT
    ;

scalingProp
    : IDENT ':' expr
    ;

envSection
    : 'env' ':' INDENT (envProp NEWLINE)* DEDENT
    ;

envProp
    : IDENT ':' expr
    ;

// ───────────────────── Agent Declaration ────────────────────────

agentDecl
    : 'agent' IDENT ':' INDENT agentBody DEDENT
    ;

agentBody
    : (agentSection NEWLINE*)*
    ;

agentSection
    : 'model' ':' expr
    | behaviorSection
    | 'input' ':' typeList
    | 'output' ':' typeExpr annotation*
    | 'tools' ':' listExpr
    | 'temperature' ':' expr
    | 'context' ':' expr
    | 'guardrails' ':' listExpr
    | IDENT ':' expr
    ;

// ────────────────────── Flow Declaration ────────────────────────

flowDecl
    : 'flow' IDENT ':' INDENT flowBody DEDENT
    ;

flowBody
    : (flowRule NEWLINE*)*
    ;

flowRule
    : IDENT '->' IDENT ':' INDENT (flowProp NEWLINE)* DEDENT
    | IDENT '->' IDENT
    ;

flowProp
    : 'protocol' ':' IDENT
    | 'auth' ':' IDENT
    | 'rate_limit' ':' expr
    | 'retry' ':' expr
    | 'timeout' ':' expr
    | 'events' ':' listExpr
    | 'transform' ':' expr
    | IDENT ':' expr
    ;

// ───────────────────── Type Expressions ─────────────────────────

typeExpr
    : primitiveType
    | IDENT
    | typeExpr '[]'                           // array
    | typeExpr '[' INT_LIT ']'                // fixed array
    | typeExpr '?'                            // optional
    | 'map' '<' typeExpr ',' typeExpr '>'     // map
    | 'enum' '(' enumMembers ')'              // inline enum
    | typeExpr '->' IDENT '.' IDENT           // foreign key
    | '(' typeExpr (',' typeExpr)* ')'        // tuple
    ;

primitiveType
    : 'int' | 'int32' | 'int64'
    | 'float' | 'double' | 'decimal'
    | 'string' | 'str'
    | 'bool'
    | 'byte' | 'bytes'
    | 'uuid'
    | 'datetime' | 'timestamp'
    | 'json'
    | 'void'
    ;

enumMembers
    : STRING (',' STRING)*
    ;

typeList
    : typeExpr (',' typeExpr)*
    ;

// ──────────────────── Parameters & Arguments ────────────────────

paramList
    : param (',' param)*
    ;

param
    : IDENT ':' typeExpr ('=' expr)?
    ;

namedArgList
    : namedArg (',' namedArg)*
    ;

namedArg
    : IDENT '=' expr
    | expr
    ;

argList
    : expr (',' expr)*
    ;

// ────────────────────── Statements (Block) ──────────────────────

block
    : (statement NEWLINE)*
    ;

statement
    : assignment
    | exprStmt
    | ifStmt
    | forStmt
    | whileStmt
    | returnStmt
    | deleteStmt
    | passStmt
    | breakStmt
    | continueStmt
    | variableDecl
    | 'emit' expr
    | 'await' expr
    | 'log' expr
    ;

assignment
    : assignTarget '=' expr
    | assignTarget '+=' expr
    | assignTarget '-=' expr
    ;

assignTarget
    : IDENT
    | expr '.' IDENT
    | expr '[' expr ']'
    ;

exprStmt
    : expr
    ;

ifStmt
    : 'if' expr ':' INDENT block DEDENT
      ('elif' expr ':' INDENT block DEDENT)*
      ('else' ':' INDENT block DEDENT)?
    ;

forStmt
    : 'for' IDENT 'in' expr ':' INDENT block DEDENT
    ;

whileStmt
    : 'while' expr ':' INDENT block DEDENT
    ;

returnStmt
    : 'return' expr?
    ;

deleteStmt
    : 'del' expr
    ;

passStmt
    : 'pass'
    ;

breakStmt
    : 'break'
    ;

continueStmt
    : 'continue'
    ;

// ─────────────────────── Expressions ────────────────────────────

expr
    : primary                                          # primaryExpr
    | expr '.' IDENT                                   # memberAccess
    | expr '(' argList? ')'                            # callExpr
    | expr '[' expr ']'                                # indexExpr
    | ('not' | '-' | '~') expr                         # unaryExpr
    | expr ('*' | '/' | '%') expr                      # mulExpr
    | expr ('+' | '-') expr                            # addExpr
    | expr ('<<' | '>>') expr                          # shiftExpr
    | expr ('&' | '|' | '^') expr                      # bitwiseExpr
    | expr ('<' | '>' | '<=' | '>=' | '==' | '!=') expr # compareExpr
    | expr ('and' | '&&') expr                         # andExpr
    | expr ('or' | '||') expr                          # orExpr
    | expr 'if' expr 'else' expr                       # ternaryExpr
    | '(' typeExpr ')' expr                            # castExpr
    ;

primary
    : INT_LIT
    | FLOAT_LIT
    | STRING
    | 'True' | 'False'
    | 'None'
    | IDENT
    | listExpr
    | mapExpr
    | '(' expr ')'
    | 'error' '(' expr ')'
    | 'len' '(' expr ')'
    ;

listExpr
    : '[' (expr (',' expr)*)? ']'
    ;

mapExpr
    : '{' (mapEntry (',' mapEntry)*)? '}'
    ;

mapEntry
    : (IDENT | STRING) ':' expr
    ;

// ─────────────────────── Lexer Rules ────────────────────────────

// Keywords (additions to original Ravel marked with *)
SYSTEM      : 'system';      // *
MODEL       : 'model';
CONTROLLER  : 'controller';
VIEW        : 'view';
SERVICE     : 'service';     // * (replaces 'interface')
RUNTIME     : 'runtime';     // * (replaces 'space')
AGENT       : 'agent';       // *
FLOW        : 'flow';
SCHEMA      : 'schema';
BEHAVIOR    : 'behavior';    // *
STYLE       : 'style';       // *
SCALING     : 'scaling';     // *
CONFIG      : 'config';      // *
ENV         : 'env';         // *

LOCAL       : 'local';
STREAMING   : 'streaming';
REPLICATED  : 'replicated';
EPHEMERAL   : 'ephemeral';  // *

DEF         : 'def';
EVENT       : 'event';
IMPORT      : 'import';
FROM        : 'from';
AS          : 'as';

IF          : 'if';
ELIF        : 'elif';
ELSE        : 'else';
FOR         : 'for';
WHILE       : 'while';
IN          : 'in';
RETURN      : 'return';
BREAK       : 'break';
CONTINUE    : 'continue';
PASS        : 'pass';
DEL         : 'del';
EMIT        : 'emit';       // *
AWAIT       : 'await';      // *
LOG         : 'log';        // *

AND         : 'and';
OR          : 'or';
NOT         : 'not';
TRUE        : 'True';
FALSE       : 'False';
NONE        : 'None';

// Literals
INT_LIT     : [0-9]+;
FLOAT_LIT   : [0-9]+ '.' [0-9]+ ([eE] [+-]? [0-9]+)?;
STRING      : '"' (~["\\\r\n] | '\\' .)* '"'
            | '\'' (~['\\\r\n] | '\\' .)* '\''
            ;

// Duration / size literals  (e.g., 60s, 5m, 1h, 10mb, 100/min)
DURATION_LIT : [0-9]+ [smhd];
SIZE_LIT     : [0-9]+ ('kb' | 'mb' | 'gb' | 'tb');
RATE_LIT     : [0-9]+ '/' ('s' | 'min' | 'h' | 'd');
PERCENT_LIT  : [0-9]+ '%';

IDENT       : [a-zA-Z_] [a-zA-Z0-9_]*;

// Operators & punctuation
ARROW       : '->';
COLON       : ':';
COMMA       : ',';
DOT         : '.';
LPAREN      : '(';
RPAREN      : ')';
LBRACK      : '[';
RBRACK      : ']';
LBRACE      : '{';
RBRACE      : '}';
LT          : '<';
GT          : '>';
EQ          : '=';
EQEQ        : '==';
NEQ         : '!=';
LTE         : '<=';
GTE         : '>=';
PLUS        : '+';
MINUS       : '-';
STAR        : '*';
SLASH       : '/';
PERCENT     : '%';
AMP         : '&';
PIPE        : '|';
CARET       : '^';
TILDE       : '~';
LSHIFT      : '<<';
RSHIFT      : '>>';
AT          : '@';
PLUSEQ      : '+=';
MINUSEQ     : '-=';
AMPAMP      : '&&';
PIPEPIPE    : '||';

// Whitespace & indentation (handled by custom lexer logic, as in original Ravel)
NEWLINE     : '\r'? '\n' ' '*;
WS          : [ \t]+ -> skip;
COMMENT     : '#' ~[\r\n]* -> skip;
BLOCK_COMMENT : '/*' .*? '*/' -> skip;

// INDENT and DEDENT are synthetic tokens emitted by the lexer's
// indentation tracking logic (identical to original Ravel / CPython).
// They are NOT produced by lexer rules; the lexer post-processor injects
// them by comparing leading whitespace on each NEWLINE.

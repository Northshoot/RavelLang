/**
 * Ravel v2 Token Definitions
 *
 * Tokens for the modern Ravel language. The lexer produces these tokens,
 * which the parser consumes to build the AST.
 */

export enum TokenType {
  // ── Structural ──
  INDENT = "INDENT",
  DEDENT = "DEDENT",
  NEWLINE = "NEWLINE",
  EOF = "EOF",

  // ── Literals ──
  INT_LIT = "INT_LIT",
  FLOAT_LIT = "FLOAT_LIT",
  STRING = "STRING",
  DURATION_LIT = "DURATION_LIT",
  SIZE_LIT = "SIZE_LIT",
  RATE_LIT = "RATE_LIT",
  PERCENT_LIT = "PERCENT_LIT",

  // ── Identifiers ──
  IDENT = "IDENT",

  // ── Keywords: Top-level declarations ──
  SYSTEM = "SYSTEM",
  MODEL = "MODEL",
  CONTROLLER = "CONTROLLER",
  VIEW = "VIEW",
  SERVICE = "SERVICE",
  RUNTIME = "RUNTIME",
  AGENT = "AGENT",
  FLOW = "FLOW",
  FEATURE = "FEATURE",

  // ── Keywords: Model qualifiers ──
  LOCAL = "LOCAL",
  STREAMING = "STREAMING",
  REPLICATED = "REPLICATED",
  EPHEMERAL = "EPHEMERAL",

  // ── Keywords: Sections ──
  SCHEMA = "SCHEMA",
  BEHAVIOR = "BEHAVIOR",
  STYLE = "STYLE",
  SCALING = "SCALING",
  CONFIG = "CONFIG",
  ENV = "ENV",
  COMPONENTS = "COMPONENTS",

  // ── Keywords: Definitions ──
  DEF = "DEF",
  EVENT = "EVENT",
  IMPORT = "IMPORT",
  FROM = "FROM",
  AS = "AS",

  // ── Keywords: Control flow ──
  IF = "IF",
  ELIF = "ELIF",
  ELSE = "ELSE",
  FOR = "FOR",
  WHILE = "WHILE",
  IN = "IN",
  RETURN = "RETURN",
  BREAK = "BREAK",
  CONTINUE = "CONTINUE",
  PASS = "PASS",
  DEL = "DEL",
  EMIT = "EMIT",
  AWAIT = "AWAIT",
  LOG = "LOG",

  // ── Keywords: Logical ──
  AND = "AND",
  OR = "OR",
  NOT = "NOT",

  // ── Keywords: Literals ──
  TRUE = "TRUE",
  FALSE = "FALSE",
  NONE = "NONE",

  // ── Keywords: Type primitives ──
  INT = "INT",
  INT32 = "INT32",
  INT64 = "INT64",
  FLOAT = "FLOAT",
  DOUBLE = "DOUBLE",
  DECIMAL = "DECIMAL",
  STRING_TYPE = "STRING_TYPE",
  STR = "STR",
  BOOL = "BOOL",
  BYTE = "BYTE",
  BYTES = "BYTES",
  UUID = "UUID",
  DATETIME = "DATETIME",
  TIMESTAMP = "TIMESTAMP",
  JSON_TYPE = "JSON_TYPE",
  VOID = "VOID",
  MAP = "MAP",
  ENUM = "ENUM",
  ERROR = "ERROR",

  // ── Operators ──
  ARROW = "ARROW",
  PLUS = "PLUS",
  MINUS = "MINUS",
  STAR = "STAR",
  SLASH = "SLASH",
  PERCENT = "PERCENT",
  AMP = "AMP",
  PIPE = "PIPE",
  CARET = "CARET",
  TILDE = "TILDE",
  LSHIFT = "LSHIFT",
  RSHIFT = "RSHIFT",
  AMPAMP = "AMPAMP",
  PIPEPIPE = "PIPEPIPE",

  // ── Comparison ──
  EQEQ = "EQEQ",
  NEQ = "NEQ",
  LT = "LT",
  GT = "GT",
  LTE = "LTE",
  GTE = "GTE",

  // ── Assignment ──
  EQ = "EQ",
  PLUSEQ = "PLUSEQ",
  MINUSEQ = "MINUSEQ",

  // ── Punctuation ──
  COLON = "COLON",
  COMMA = "COMMA",
  DOT = "DOT",
  AT = "AT",
  LPAREN = "LPAREN",
  RPAREN = "RPAREN",
  LBRACK = "LBRACK",
  RBRACK = "RBRACK",
  LBRACE = "LBRACE",
  RBRACE = "RBRACE",
  QUESTION = "QUESTION",
}

export interface SourceLocation {
  line: number;
  column: number;
  file?: string;
}

export interface Token {
  type: TokenType;
  value: string;
  loc: SourceLocation;
}

/** Map from keyword string to TokenType */
export const KEYWORDS: ReadonlyMap<string, TokenType> = new Map([
  // Top-level
  ["system", TokenType.SYSTEM],
  ["model", TokenType.MODEL],
  ["controller", TokenType.CONTROLLER],
  ["view", TokenType.VIEW],
  ["service", TokenType.SERVICE],
  ["runtime", TokenType.RUNTIME],
  ["agent", TokenType.AGENT],
  ["flow", TokenType.FLOW],
  ["feature", TokenType.FEATURE],

  // Model qualifiers
  ["local", TokenType.LOCAL],
  ["streaming", TokenType.STREAMING],
  ["replicated", TokenType.REPLICATED],
  ["ephemeral", TokenType.EPHEMERAL],

  // Sections
  ["schema", TokenType.SCHEMA],
  ["behavior", TokenType.BEHAVIOR],
  ["style", TokenType.STYLE],
  ["scaling", TokenType.SCALING],
  ["config", TokenType.CONFIG],
  ["env", TokenType.ENV],
  ["components", TokenType.COMPONENTS],

  // Definitions
  ["def", TokenType.DEF],
  ["event", TokenType.EVENT],
  ["import", TokenType.IMPORT],
  ["from", TokenType.FROM],
  ["as", TokenType.AS],

  // Control flow
  ["if", TokenType.IF],
  ["elif", TokenType.ELIF],
  ["else", TokenType.ELSE],
  ["for", TokenType.FOR],
  ["while", TokenType.WHILE],
  ["in", TokenType.IN],
  ["return", TokenType.RETURN],
  ["break", TokenType.BREAK],
  ["continue", TokenType.CONTINUE],
  ["pass", TokenType.PASS],
  ["del", TokenType.DEL],
  ["emit", TokenType.EMIT],
  ["await", TokenType.AWAIT],
  ["log", TokenType.LOG],

  // Logical
  ["and", TokenType.AND],
  ["or", TokenType.OR],
  ["not", TokenType.NOT],

  // Literal keywords
  ["True", TokenType.TRUE],
  ["False", TokenType.FALSE],
  ["None", TokenType.NONE],

  // Primitive types
  ["int", TokenType.INT],
  ["int32", TokenType.INT32],
  ["int64", TokenType.INT64],
  ["float", TokenType.FLOAT],
  ["double", TokenType.DOUBLE],
  ["decimal", TokenType.DECIMAL],
  ["string", TokenType.STRING_TYPE],
  ["str", TokenType.STR],
  ["bool", TokenType.BOOL],
  ["byte", TokenType.BYTE],
  ["bytes", TokenType.BYTES],
  ["uuid", TokenType.UUID],
  ["datetime", TokenType.DATETIME],
  ["timestamp", TokenType.TIMESTAMP],
  ["json", TokenType.JSON_TYPE],
  ["void", TokenType.VOID],
  ["map", TokenType.MAP],
  ["enum", TokenType.ENUM],
  ["error", TokenType.ERROR],
]);

/** Tokens that represent primitive types */
export const PRIMITIVE_TYPE_TOKENS = new Set<TokenType>([
  TokenType.INT,
  TokenType.INT32,
  TokenType.INT64,
  TokenType.FLOAT,
  TokenType.DOUBLE,
  TokenType.DECIMAL,
  TokenType.STRING_TYPE,
  TokenType.STR,
  TokenType.BOOL,
  TokenType.BYTE,
  TokenType.BYTES,
  TokenType.UUID,
  TokenType.DATETIME,
  TokenType.TIMESTAMP,
  TokenType.JSON_TYPE,
  TokenType.VOID,
]);

/** Tokens that represent model qualifiers */
export const MODEL_QUALIFIER_TOKENS = new Set<TokenType>([
  TokenType.LOCAL,
  TokenType.STREAMING,
  TokenType.REPLICATED,
  TokenType.EPHEMERAL,
]);

/**
 * Ravel v2 Lexer
 *
 * Tokenizer with Python-style indentation tracking. Emits synthetic INDENT
 * and DEDENT tokens by comparing leading whitespace on each newline, identical
 * to the technique used in the original Ravel lexer (RavelLexer.java) and
 * CPython's tokenizer.
 *
 * The lexer operates in two phases:
 *   1. Raw tokenization: scan characters into raw tokens
 *   2. Indentation injection: post-process to insert INDENT/DEDENT/NEWLINE
 */

import {
  type Token,
  TokenType,
  type SourceLocation,
  KEYWORDS,
} from "./tokens.js";
import { LexerError } from "./errors.js";

export class Lexer {
  private source: string;
  private file: string;
  private pos = 0;
  private line = 1;
  private col = 1;
  private tokens: Token[] = [];
  private indentStack: number[] = [0];
  /** Bracket nesting depth — suppress INDENT/DEDENT/NEWLINE when > 0 (Python rule) */
  private bracketDepth = 0;

  constructor(source: string, file = "<stdin>") {
    this.source = source;
    this.file = file;
  }

  tokenize(): Token[] {
    this.tokens = [];
    this.indentStack = [0];
    this.bracketDepth = 0;
    this.pos = 0;
    this.line = 1;
    this.col = 1;

    // Process line by line for indentation tracking
    const lines = this.source.split("\n");

    for (let i = 0; i < lines.length; i++) {
      this.line = i + 1;
      const rawLine = lines[i];

      // Skip completely empty lines and comment-only lines
      const trimmed = rawLine.trim();
      if (trimmed === "" || trimmed.startsWith("#") || trimmed.startsWith("//")) {
        // Only emit newline if not inside brackets
        if (this.tokens.length > 0 && this.bracketDepth === 0) {
          this.emitNewline();
        }
        continue;
      }

      // Calculate indentation
      const indent = this.measureIndent(rawLine);

      // Only handle INDENT/DEDENT when NOT inside brackets (Python rule)
      if (this.bracketDepth === 0) {
        // Emit INDENT / DEDENT tokens
        const currentIndent = this.indentStack[this.indentStack.length - 1];
        if (indent > currentIndent) {
          this.indentStack.push(indent);
          this.tokens.push(this.makeToken(TokenType.INDENT, "", 1, 1));
        } else if (indent < currentIndent) {
          while (
            this.indentStack.length > 1 &&
            this.indentStack[this.indentStack.length - 1] > indent
          ) {
            this.indentStack.pop();
            this.tokens.push(this.makeToken(TokenType.DEDENT, "", this.line, 1));
          }
          if (this.indentStack[this.indentStack.length - 1] !== indent) {
            throw new LexerError(
              `Inconsistent indentation at line ${this.line}`,
              { line: this.line, column: 1, file: this.file },
            );
          }
        }
      }

      // Tokenize the content of this line (skip leading whitespace)
      this.col = indent + 1;
      this.pos = 0;
      this.tokenizeLine(rawLine.slice(indent));

      // Emit newline after each logical line (only outside brackets)
      if (this.bracketDepth === 0) {
        this.emitNewline();
      }
    }

    // Close remaining indentation levels
    while (this.indentStack.length > 1) {
      this.indentStack.pop();
      this.tokens.push(
        this.makeToken(TokenType.DEDENT, "", this.line, 1),
      );
    }

    this.tokens.push(
      this.makeToken(TokenType.EOF, "", this.line, this.col),
    );

    return this.tokens;
  }

  private measureIndent(line: string): number {
    let indent = 0;
    for (const ch of line) {
      if (ch === " ") indent++;
      else if (ch === "\t") indent += 4; // treat tab as 4 spaces
      else break;
    }
    return indent;
  }

  private emitNewline(): void {
    // Avoid duplicate newlines
    if (
      this.tokens.length > 0 &&
      this.tokens[this.tokens.length - 1].type !== TokenType.NEWLINE &&
      this.tokens[this.tokens.length - 1].type !== TokenType.INDENT
    ) {
      this.tokens.push(
        this.makeToken(TokenType.NEWLINE, "\n", this.line, this.col),
      );
    }
  }

  private tokenizeLine(line: string): void {
    this.pos = 0;
    while (this.pos < line.length) {
      const ch = line[this.pos];

      // Skip whitespace within a line
      if (ch === " " || ch === "\t") {
        this.pos++;
        this.col++;
        continue;
      }

      // Skip inline comments
      if (ch === "#") break;
      if (ch === "/" && this.pos + 1 < line.length && line[this.pos + 1] === "/") break;

      // String literals
      if (ch === '"' || ch === "'") {
        this.readString(line, ch);
        continue;
      }

      // Numeric literals (int, float, duration, size, rate, percent)
      if (this.isDigit(ch)) {
        this.readNumber(line);
        continue;
      }

      // Identifiers and keywords
      if (this.isIdentStart(ch)) {
        this.readIdentifier(line);
        continue;
      }

      // Multi-character operators
      if (this.pos + 1 < line.length) {
        const two = line.slice(this.pos, this.pos + 2);
        const twoCharOp = TWO_CHAR_OPS.get(two);
        if (twoCharOp) {
          this.tokens.push(
            this.makeToken(twoCharOp, two, this.line, this.col),
          );
          this.pos += 2;
          this.col += 2;
          continue;
        }
      }

      // Single-character operators
      const oneCharOp = ONE_CHAR_OPS.get(ch);
      if (oneCharOp) {
        // Track bracket depth for implicit line joining (Python rule)
        if (ch === "(" || ch === "[" || ch === "{") this.bracketDepth++;
        if (ch === ")" || ch === "]" || ch === "}") this.bracketDepth = Math.max(0, this.bracketDepth - 1);
        this.tokens.push(
          this.makeToken(oneCharOp, ch, this.line, this.col),
        );
        this.pos++;
        this.col++;
        continue;
      }

      throw new LexerError(`Unexpected character '${ch}'`, {
        line: this.line,
        column: this.col,
        file: this.file,
      });
    }
  }

  private readString(line: string, quote: string): void {
    const startCol = this.col;
    this.pos++; // skip opening quote
    this.col++;
    let value = "";

    while (this.pos < line.length && line[this.pos] !== quote) {
      if (line[this.pos] === "\\") {
        this.pos++;
        this.col++;
        if (this.pos < line.length) {
          const escaped = line[this.pos];
          switch (escaped) {
            case "n":
              value += "\n";
              break;
            case "t":
              value += "\t";
              break;
            case "r":
              value += "\r";
              break;
            case "\\":
              value += "\\";
              break;
            case "'":
              value += "'";
              break;
            case '"':
              value += '"';
              break;
            default:
              value += "\\" + escaped;
          }
        }
      } else {
        value += line[this.pos];
      }
      this.pos++;
      this.col++;
    }

    if (this.pos >= line.length) {
      throw new LexerError("Unterminated string literal", {
        line: this.line,
        column: startCol,
        file: this.file,
      });
    }

    this.pos++; // skip closing quote
    this.col++;
    this.tokens.push(
      this.makeToken(TokenType.STRING, value, this.line, startCol),
    );
  }

  private readNumber(line: string): void {
    const startCol = this.col;
    const startPos = this.pos;
    let isFloat = false;

    while (this.pos < line.length && this.isDigit(line[this.pos])) {
      this.pos++;
      this.col++;
    }

    // Check for float
    if (
      this.pos < line.length &&
      line[this.pos] === "." &&
      this.pos + 1 < line.length &&
      this.isDigit(line[this.pos + 1])
    ) {
      isFloat = true;
      this.pos++;
      this.col++;
      while (this.pos < line.length && this.isDigit(line[this.pos])) {
        this.pos++;
        this.col++;
      }
    }

    const numStr = line.slice(startPos, this.pos);

    // Check for suffix (duration, size, rate, percent)
    if (this.pos < line.length) {
      const rest = line.slice(this.pos);

      // Percent
      if (rest.startsWith("%")) {
        this.pos++;
        this.col++;
        this.tokens.push(
          this.makeToken(TokenType.PERCENT_LIT, numStr + "%", this.line, startCol),
        );
        return;
      }

      // Rate: 100/min, 10/s, etc.
      if (rest.startsWith("/")) {
        const rateMatch = rest.match(/^\/(s|min|h|d)\b/);
        if (rateMatch) {
          const suffix = rateMatch[0];
          this.pos += suffix.length;
          this.col += suffix.length;
          this.tokens.push(
            this.makeToken(
              TokenType.RATE_LIT,
              numStr + suffix,
              this.line,
              startCol,
            ),
          );
          return;
        }
      }

      // Size: 10mb, 1gb, etc.
      const sizeMatch = rest.match(/^(kb|mb|gb|tb)\b/);
      if (sizeMatch) {
        const suffix = sizeMatch[0];
        this.pos += suffix.length;
        this.col += suffix.length;
        this.tokens.push(
          this.makeToken(
            TokenType.SIZE_LIT,
            numStr + suffix,
            this.line,
            startCol,
          ),
        );
        return;
      }

      // Duration: 60s, 5m, 1h, 2d
      const durMatch = rest.match(/^([smhd])\b/);
      if (durMatch && !this.isIdentStart(rest[1] || "")) {
        const suffix = durMatch[0];
        this.pos += suffix.length;
        this.col += suffix.length;
        this.tokens.push(
          this.makeToken(
            TokenType.DURATION_LIT,
            numStr + suffix,
            this.line,
            startCol,
          ),
        );
        return;
      }
    }

    // Plain number
    this.tokens.push(
      this.makeToken(
        isFloat ? TokenType.FLOAT_LIT : TokenType.INT_LIT,
        numStr,
        this.line,
        startCol,
      ),
    );
  }

  private readIdentifier(line: string): void {
    const startCol = this.col;
    const startPos = this.pos;

    while (this.pos < line.length && this.isIdentPart(line[this.pos])) {
      this.pos++;
      this.col++;
    }

    const word = line.slice(startPos, this.pos);
    const keyword = KEYWORDS.get(word);
    if (keyword) {
      this.tokens.push(
        this.makeToken(keyword, word, this.line, startCol),
      );
    } else {
      this.tokens.push(
        this.makeToken(TokenType.IDENT, word, this.line, startCol),
      );
    }
  }

  private isDigit(ch: string): boolean {
    return ch >= "0" && ch <= "9";
  }

  private isIdentStart(ch: string): boolean {
    return (
      (ch >= "a" && ch <= "z") ||
      (ch >= "A" && ch <= "Z") ||
      ch === "_"
    );
  }

  private isIdentPart(ch: string): boolean {
    return this.isIdentStart(ch) || this.isDigit(ch);
  }

  private makeToken(
    type: TokenType,
    value: string,
    line: number,
    column: number,
  ): Token {
    return { type, value, loc: { line, column, file: this.file } };
  }
}

// ── Operator lookup tables ──

const TWO_CHAR_OPS = new Map<string, TokenType>([
  ["->", TokenType.ARROW],
  ["==", TokenType.EQEQ],
  ["!=", TokenType.NEQ],
  ["<=", TokenType.LTE],
  [">=", TokenType.GTE],
  ["<<", TokenType.LSHIFT],
  [">>", TokenType.RSHIFT],
  ["+=", TokenType.PLUSEQ],
  ["-=", TokenType.MINUSEQ],
  ["&&", TokenType.AMPAMP],
  ["||", TokenType.PIPEPIPE],
]);

const ONE_CHAR_OPS = new Map<string, TokenType>([
  [":", TokenType.COLON],
  [",", TokenType.COMMA],
  [".", TokenType.DOT],
  ["@", TokenType.AT],
  ["(", TokenType.LPAREN],
  [")", TokenType.RPAREN],
  ["[", TokenType.LBRACK],
  ["]", TokenType.RBRACK],
  ["{", TokenType.LBRACE],
  ["}", TokenType.RBRACE],
  ["+", TokenType.PLUS],
  ["-", TokenType.MINUS],
  ["*", TokenType.STAR],
  ["/", TokenType.SLASH],
  ["%", TokenType.PERCENT],
  ["&", TokenType.AMP],
  ["|", TokenType.PIPE],
  ["^", TokenType.CARET],
  ["~", TokenType.TILDE],
  ["<", TokenType.LT],
  [">", TokenType.GT],
  ["=", TokenType.EQ],
  ["?", TokenType.QUESTION],
]);

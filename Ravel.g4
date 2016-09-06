grammar Ravel;

@header{
import ai.harmony.ravel.compiler.scope.*;
}
tokens { INDENT, DEDENT }

@lexer::members {

  // A queue where extra tokens are pushed on (see the NEWLINE lexer rule).
  private java.util.LinkedList<Token> tokens = new java.util.LinkedList<>();

  // The stack that keeps track of the indentation level.
  private java.util.Stack<Integer> indents = new java.util.Stack<>();

  // The amount of opened braces, brackets and parenthesis.
  private int opened = 0;

  // The most recently produced token.
  private Token lastToken = null;

  @Override
  public void emit(Token t) {
    super.setToken(t);
    tokens.offer(t);
  }

  @Override
  public Token nextToken() {

    // Check if the end-of-file is ahead and there are still some DEDENTS expected.
    if (_input.LA(1) == EOF && !this.indents.isEmpty()) {

      // Remove any trailing EOF tokens from our buffer.
      for (int i = tokens.size() - 1; i >= 0; i--) {
        if (tokens.get(i).getType() == EOF) {
          tokens.remove(i);
        }
      }

      // First emit an extra line break that serves as the end of the statement.
      this.emit(commonToken(RavelParser.NEWLINE, "\n"));

      // Now emit as much DEDENT tokens as needed.
      while (!indents.isEmpty()) {
        this.emit(createDedent());
        indents.pop();
      }

      // Put the EOF back on the token stream.
      this.emit(commonToken(RavelParser.EOF, "<EOF>"));
    }

    Token next = super.nextToken();

    if (next.getChannel() == Token.DEFAULT_CHANNEL) {
      // Keep track of the last token on the default channel.
      this.lastToken = next;
    }

    return tokens.isEmpty() ? next : tokens.poll();
  }

  private Token createDedent() {
    CommonToken dedent = commonToken(RavelParser.DEDENT, "");
    dedent.setLine(this.lastToken.getLine());
    return dedent;
  }

  private CommonToken commonToken(int type, String text) {
    int stop = this.getCharIndex() - 1;
    int start = text.isEmpty() ? stop : stop - text.length() + 1;
    return new CommonToken(this._tokenFactorySourcePair, type, DEFAULT_TOKEN_CHANNEL, start, stop);
  }

  // Calculates the indentation of the provided spaces, taking the
  // following rules into account:
  //
  // "Tabs are replaced (from left to right) by one to eight spaces
  //  such that the total number of characters up to and including
  //  the replacement is a multiple of eight [...]"
  //
  //  -- https://docs.python.org/3.1/reference/lexical_analysis.html#indentation
  static int getIndentationCount(String spaces) {

    int count = 0;

    for (char ch : spaces.toCharArray()) {
      switch (ch) {
        case '\t':
          count += 8 - (count % 8);
          break;
        default:
          // A normal space char.
          count++;
      }
    }

    return count;
  }

  boolean atStartOfInput() {
    return super.getCharPositionInLine() == 0 && super.getLine() == 1;
  }
}

// most of the lexer rules are copy pasted from antrl4 pthon 3 implementation
// thank you Ter! :)



/*
 * parser rules
 */
 //only file inputs are accepted

//the file can be newlines or components definitions
//TODO: add imports
file_input returns [Scope scope]
    : ( NEWLINE | comp_def )* EOF
    ;
/**
 *
 * Ravel application consists of componets
 */
//we have models, views, controllers, spaces, transform, flow?
comp_def
    : model_comp
    | controller_comp
    | space_comp
    ;

/**
 *
 * Space parser rules
 */
space_comp returns [Scope scope]
    : SPACE Identifier ':' space_body #SpaceScope
    ;
space_body
    : NEWLINE INDENT space_block+ DEDENT
    ;
space_block
    : platform_scope
    | models_scope
    | controllers_scope
    | sink_scope
    | source_scope
    | NEWLINE
    ;

platform_scope returns [Scope scope]
    : 'platform:' space_assigments #PlatformScope
    ;

space_assigments
    : NEWLINE INDENT space_assigment+ DEDENT
    ;

space_assigment
    : ref_assig
    | NEWLINE
    ;

models_scope returns [Scope scope]
    : 'models:' instantiations #ModelInstanciation
    ;
instantiations
    : NEWLINE INDENT instance+ DEDENT
    ;

instance
    : Identifier '=' instance_name '(' elementValuePairs? ')' NEWLINE?
    ;
instance_name
    : Identifier
    ;
controllers_scope returns [Scope scope]
    : 'controllers:' instantiations #ControllerInstanciation
    ;
sink_scope returns [Scope scope]
    : 'sinks:' space_assigments #SinkLinks
    ;

references
    : NEWLINE INDENT space_assigment+ DEDENT
    ;
source_scope returns [Scope scope]
    : 'sources:' space_assigments #SourceLinks
    ;
/**
 *
 * Model parser rules
 */
model_comp returns [Scope scope]
    :  modelType MODEL Identifier  component_parameters ':' model_body # ModelScope
    ;


modelType
    : 'local'
    | 'streaming'
    | 'replicated'
    ;

model_body
    : NEWLINE INDENT model_block+ DEDENT
    ;

model_block
    : properties_block
    | schema_block
    | NEWLINE
    ;
properties_block returns [Scope scope]
    : 'properties:' properties #PropertiesScope
    ;
properties
    : NEWLINE INDENT property+ DEDENT
    ;

property
    : Identifier '=' propValue NEWLINE #VarAssigment
    ;

propValue
    : propArray
    | prop
    ;

propArray
    : '[' prop (',' prop)* ']'
    ;
// we are just very specific what it is allowed in the property definirion
prop
    : StringLiteral
    | boolean_r
    | IntegerLiteral
    | FloatingPointLiteral
    | Identifier
    ;
schema_block returns [Scope scope]
    :'schema:' fields #SchemaScope
    ;

fields
    : NEWLINE INDENT field+ DEDENT
    ;

field
    : Identifier '=' field_type '(' elementValuePairs? ')'  NEWLINE? #FieldDeclaration
    ;

/**
 *
 * Field types, currently only in parser. But maybe it should be like a Ravel library
 * that you can write your own field in Ravel and then get it translated
 * TODO: dynamic field loading
 *
 */
field_type
    : T_BYTE_FIELD
    | T_STRING_FIELD
    | T_BOOLEAN_FIELD
    | T_INTEGER_FIELD
    | T_NUMBER_FIELD
    | T_DATE_FIELD
    | T_DATE_TIME_FIELD
    | T_TIME_STAMP_FIELD
    | T_CONTEXT_FIELD
    | T_MODEL_FIELD
    ;

/**
 * Controller stuff
 */

controller_comp returns [Scope scope]
    : CONTROLLER Identifier component_parameters ':' controller_scope # ControllerScope
    ;

controller_scope
    : NEWLINE INDENT controller_body+ DEDENT
    ;

controller_body
    : eventdef // can only be events
    | ref_assig // reference
    | property // simple var assigment
    | variableDeclarator // or variable assigment
    | funct_expr
    | NEWLINE
    ;

/**
 *All here for handling normal language statements and expressions
 *
 */

eventdef returns [Scope scope]
    : EVENT qualified_name  function_args ':' block_stmt #EventScope
    ;
block_stmt
    : NEWLINE INDENT blockStatement+ DEDENT
    ;
blockStatement
    : controller_body
    | statement
    | if_stmt
    | del_stmt
    ;
del_stmt
    : DELETE qualified_name '(' elementValuePairs? ')' #DeleteStmt
    ;
variableDeclarators
    :   variableDeclarator (',' variableDeclarator)*
    ;
variableDeclarator
    :   Identifier ('=' variableInitializer)?
    ;
variableInitializer
    :   arrayInitializer
    |   expression
    ;
arrayInitializer
    :   '{' (variableInitializer (',' variableInitializer)* (',')? )? '}'
    ;

statement
    :   ASSERT expression
    |   'for'  forControl ':' block_stmt
    |   'return' expression?
    |   'break' Identifier?
    |   'continue' Identifier?
    |   statementExpression
    |   NEWLINE
    ;
if_stmt
    : IF comp_expr ':' block_stmt ( ELIF comp_expr ':' block_stmt )* ( ELSE ':' block_stmt )? #IfStatement
    ;
comp_expr
    : or_test ( IF or_test ELSE comp_expr )?
    ;
or_test
    : and_test (OR and_test)*
    ;
and_test
    : not_test (AND not_test)*
    ;
not_test
    : NOT not_test
    | comparison
    ;
comparison
    : expr (comp_op expr)*
    ;
expr
    : atom //this is prep for future to implement advance comparisons
    ;
atom
    : Identifier
    | IntegerLiteral
    | FloatingPointLiteral
    | boolean_r
    | qualified_name
    ;

statementExpression
    :  expression
    ;

//for_stmt
// : FOR exprlist IN testlist ':' suite ( ELSE ':' suite )?
// ;
forControl
    :   forInit? ';' expression? ';' forUpdate?
    ;

forInit
    :   variableDeclarators
    |   expressionList
    ;

forUpdate
    :   expressionList
    ;
function_args
    : '(' functionArgsList? ')'
    ;
functionArgsList
    :functionArg (',' functionArg)?
    ;

functionArg
    : Identifier Identifier
    ;
component_parameters
    : '(' params? ')'
    ;
params
    : param (',' param)?
    ;
param
    : qualified_name
    ;
elementValuePairs
    :   elementValuePair (',' elementValuePair)*
    ;
elementValuePair
    :   Identifier '=' elementValue
    ;

elementValue
    : expression
    | elementValueArrayInitializer
    ;
elementValueArrayInitializer
    :   '{' (elementValue (',' elementValue)*)? (',')? '}'
    ;


expressionList
    :   expression (',' expression)*
    ;
increament_expr
    : Identifier '++'
    ;
decrement_exp
    : Identifier '--'
    ;
expression
    :   primary
    |   expression '[' expression ']'
    |   expression '(' expressionList? ')'
    |   increament_expr
    |   decrement_exp
    |   ('+'|'-') expression
    |   ('~'|'!') expression
    |   expression ('*'|'/'|'%') expression
    |   expression ('+'|'-') expression
    |   expression comp_op expression
    |   expression '&' expression
    |   expression '^' expression
    |   expression '|' expression
    |   expression '&&' expression
    |   expression '||' expression
    |   <assoc=right> expression
        (   '='
        |   '+='
        |   '-='
        |   '*='
        |   '/='
        |   '&='
        |   '|='
        |   '^='
        |   '>>='
        |   '>>>='
        |   '<<='
        |   '%='
        )
        expression
    | NEWLINE
    ;

primary
    : '(' expression ')'
    | 'self'
    | literal
    | qualified_name
    ;
//reference is a dottend name accesesing scopes
ref_assig
    : key '=' value  #ReferenceAssigment
    ;
key: qualified_name;
value: qualified_name ;



funct_expr
    : func_no_return
    | func_with_return
    ;

func_no_return
    : function_name component_parameters  #FunctionRet
    ;

function_name
    : qualified_name
    ;
func_with_return
    : ident '=' func_no_return  #FunctionWithReturn
    ;

ident
    : Identifier
    | qualified_name
    ;
qualified_name
    :   Identifier ('.' Identifier)*
    ;
comp_op
    : '<'
    | '>'
    | '=='
    | '>='
    | '<='
    | '!='
    | IN
    | NOT IN
    | IS
    | IS NOT
    ;

/*
 * lexer rules
 */
 // Keywords
SELF                : 'self' ;
MODEL               : 'model' ;
SPACE               : 'space' ;
CONTROLLER          : 'controller' ;
VIEW                : 'view';
FLOW                : 'flow' ;
//model types
LOCAL               : 'local' ;
STREAMING           : 'streaming' ;
REPLICATED          : 'replicated';
PROPERTIES          : 'properties' ;
SCHEMA              : 'schema' ;

//space
//MODELS              : 'models';
//CONTROLLERS         : 'controllers';
//PLATFORM            : 'platform' ;
//SINKS               : 'sinks' ;
//SOURCES             : 'sources' ;

//controller
EVENT               : 'event' ;
COMMAND             :  'command' ;

//fields
T_BYTE_FIELD        : 'ByteField' ;
T_STRING_FIELD      : 'StringField' ;
T_BOOLEAN_FIELD     : 'BooleanField' ;
T_INTEGER_FIELD     : 'IntegerField';
T_NUMBER_FIELD      : 'NumberField' ;
T_DATE_FIELD        : 'DateField' ;
T_DATE_TIME_FIELD   : 'DateTimeField' ;
T_TIME_STAMP_FIELD  : 'TimeStampField' ;
T_CONTEXT_FIELD     : 'ContextField' ;
T_MODEL_FIELD       : 'ModelField' ;

//expression operators
ASSERT              : 'assert' ;
RETURN              : 'return' ;
TRUE                : 'True' ;
FALSE               : 'False' ;
IF                  : 'if' ;
ELIF                : 'else if' ;
ELSE                : 'else';
FOR                 : 'for' ;
WHILE               : 'while' ;
AND                 : 'and' ;
NOT                 : 'not';
OR                  : 'or' ;
IN                  : 'in' ;
IS                  : 'is' ;
DELETE              : 'delete' ;
PASS                : 'pass';
CONTINUE            : 'continue';
BREAK               : 'break' ;
NONE                : 'none' ;

NEWLINE
 : ( {atStartOfInput()}?   SPACES
   | ( '\r'? '\n' | '\r' ) SPACES?
   )
   {
     String newLine = getText().replaceAll("[^\r\n]+", "");
     String spaces = getText().replaceAll("[\r\n]+", "");
     int next = _input.LA(1);

     if (opened > 0 || next == '\r' || next == '\n' || next == '#') {
       // If we're inside a list or on a blank line, ignore all indents,
       // dedents and line breaks.
       skip();
     }
     else {
       emit(commonToken(NEWLINE, newLine));

       int indent = getIndentationCount(spaces);
       int previous = indents.isEmpty() ? 0 : indents.peek();

       if (indent == previous) {
         // skip indents of the same size as the present indent-size
         skip();
       }
       else if (indent > previous) {
         indents.push(indent);
         emit(commonToken(RavelParser.INDENT, spaces));
       }
       else {
         // Possibly emit more than 1 DEDENT token.
         while(!indents.isEmpty() && indents.peek() > indent) {
           this.emit(createDedent());
           indents.pop();
         }
       }
     }
   }
 ;
literal
    :   IntegerLiteral
    |   FloatingPointLiteral
    |   CharacterLiteral
    | boolean_r
    |   StringLiteral
    |   'null'
    ;
// §3.10.1 Integer Literals

IntegerLiteral
    :   DecimalIntegerLiteral
    |   HexIntegerLiteral
    |   OctalIntegerLiteral
    |   BinaryIntegerLiteral
    ;

fragment
DecimalIntegerLiteral
    :   DecimalNumeral IntegerTypeSuffix?
    ;

fragment
HexIntegerLiteral
    :   HexNumeral IntegerTypeSuffix?
    ;

fragment
OctalIntegerLiteral
    :   OctalNumeral IntegerTypeSuffix?
    ;

fragment
BinaryIntegerLiteral
    :   BinaryNumeral IntegerTypeSuffix?
    ;

fragment
IntegerTypeSuffix
    :   [lL]
    ;

fragment
DecimalNumeral
    :   '0'
    |   NonZeroDigit (Digits? | Underscores Digits)
    ;

fragment
Digits
    :   Digit (DigitOrUnderscore* Digit)?
    ;

fragment
Digit
    :   '0'
    |   NonZeroDigit
    ;

fragment
NonZeroDigit
    :   [1-9]
    ;

fragment
DigitOrUnderscore
    :   Digit
    |   '_'
    ;

fragment
Underscores
    :   '_'+
    ;

fragment
HexNumeral
    :   '0' [xX] HexDigits
    ;

fragment
HexDigits
    :   HexDigit (HexDigitOrUnderscore* HexDigit)?
    ;

fragment
HexDigit
    :   [0-9a-fA-F]
    ;

fragment
HexDigitOrUnderscore
    :   HexDigit
    |   '_'
    ;

fragment
OctalNumeral
    :   '0' Underscores? OctalDigits
    ;

fragment
OctalDigits
    :   OctalDigit (OctalDigitOrUnderscore* OctalDigit)?
    ;

fragment
OctalDigit
    :   [0-7]
    ;

fragment
OctalDigitOrUnderscore
    :   OctalDigit
    |   '_'
    ;

fragment
BinaryNumeral
    :   '0' [bB] BinaryDigits
    ;

fragment
BinaryDigits
    :   BinaryDigit (BinaryDigitOrUnderscore* BinaryDigit)?
    ;

fragment
BinaryDigit
    :   [01]
    ;

fragment
BinaryDigitOrUnderscore
    :   BinaryDigit
    |   '_'
    ;

// §3.10.2 Floating-Point Literals

FloatingPointLiteral
    :   DecimalFloatingPointLiteral
    |   HexadecimalFloatingPointLiteral
    ;

fragment
DecimalFloatingPointLiteral
    :   Digits '.' Digits? ExponentPart? FloatTypeSuffix?
    |   '.' Digits ExponentPart? FloatTypeSuffix?
    |   Digits ExponentPart FloatTypeSuffix?
    |   Digits FloatTypeSuffix
    ;

fragment
ExponentPart
    :   ExponentIndicator SignedInteger
    ;

fragment
ExponentIndicator
    :   [eE]
    ;

fragment
SignedInteger
    :   Sign? Digits
    ;

fragment
Sign
    :   [+-]
    ;

fragment
FloatTypeSuffix
    :   [fFdD]
    ;

fragment
HexadecimalFloatingPointLiteral
    :   HexSignificand BinaryExponent FloatTypeSuffix?
    ;

fragment
HexSignificand
    :   HexNumeral '.'?
    |   '0' [xX] HexDigits? '.' HexDigits
    ;

fragment
BinaryExponent
    :   BinaryExponentIndicator SignedInteger
    ;

fragment
BinaryExponentIndicator
    :   [pP]
    ;

// §3.10.3 Boolean Literals

boolean_r
    : TRUE
    | FALSE
    ;

// §3.10.4 Character Literals

CharacterLiteral
    :   '\'' SingleCharacter '\''
    ;

fragment
SingleCharacter
    :   ~['\\]
    ;

// §3.10.5 String Literals

StringLiteral
    :   '"' StringCharacters? '"'
    ;

fragment
StringCharacters
    :   StringCharacter+
    ;

fragment
StringCharacter
    :   ~["\\]
    ;



fragment
OctalEscape
    :   '\\' OctalDigit
    |   '\\' OctalDigit OctalDigit
    |   '\\' ZeroToThree OctalDigit OctalDigit
    ;

fragment
UnicodeEscape
    :   '\\' 'u' HexDigit HexDigit HexDigit HexDigit
    ;

fragment
ZeroToThree
    :   [0-3]
    ;

// §3.10.7 The Null Literal

NullLiteral
    :   'null'
    ;

// §3.11 Separators

LPAREN          : '(';
RPAREN          : ')';
LBRACE          : '{';
RBRACE          : '}';
LBRACK          : '[';
RBRACK          : ']';
SEMI            : ';';
COMMA           : ',';
DOT             : '.';

// §3.12 Operators

ASSIGN          : '=';
GT              : '>';
LT              : '<';
BANG            : '!';
TILDE           : '~';
QUESTION        : '?';
COLON           : ':';
EQUAL           : '==';
LE              : '<=';
GE              : '>=';
NOTEQUAL        : '!=';
AND_S           : '&&';
OR_S             : '||';
INC             : '++';
DEC             : '--';
ADD             : '+';
SUB             : '-';
MUL             : '*';
DIV             : '/';
BITAND          : '&';
BITOR           : '|';
CARET           : '^';
MOD             : '%';

ADD_ASSIGN      : '+=';
SUB_ASSIGN      : '-=';
MUL_ASSIGN      : '*=';
DIV_ASSIGN      : '/=';
AND_ASSIGN      : '&=';
OR_ASSIGN       : '|=';
XOR_ASSIGN      : '^=';
MOD_ASSIGN      : '%=';
LSHIFT_ASSIGN   : '<<=';
RSHIFT_ASSIGN   : '>>=';
URSHIFT_ASSIGN  : '>>>=';

// §3.8 Identifiers (must appear after all keywords in the grammar)

Identifier
    :   JavaLetter JavaLetterOrDigit*
    ;

fragment
JavaLetter
    :   [a-zA-Z$_] // these are the "java letters" below 0x7F
    |   // covers all characters above 0x7F which are not a surrogate
        ~[\u0000-\u007F\uD800-\uDBFF]
        {Character.isJavaIdentifierStart(_input.LA(-1))}?
    |   // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
        [\uD800-\uDBFF] [\uDC00-\uDFFF]
        {Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
    ;

fragment
JavaLetterOrDigit
    :   [a-zA-Z0-9$_] // these are the "java letters or digits" below 0x7F
    |   // covers all characters above 0x7F which are not a surrogate
        ~[\u0000-\u007F\uD800-\uDBFF]
        {Character.isJavaIdentifierPart(_input.LA(-1))}?
    |   // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
        [\uD800-\uDBFF] [\uDC00-\uDFFF]
        {Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
    ;


SKIP_
    : ( SPACES | COMMENT | LINE_JOINING ) -> skip
    ;


/*
 * fragments
 */

fragment SPACES
 : [ \t]+
 ;

fragment COMMENT
 : '#' ~[\r\n]*
 ;

fragment LINE_JOINING
 : '\\' SPACES? ( '\r'? '\n' | '\r' )
 ;



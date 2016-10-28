grammar Ravel;

@header{
import ai.harmony.ravel.compiler.scope.*;
import ai.harmony.ravel.compiler.symbol.*;
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
/** space currently has 5 blocks */
//TODO: implment views and transforms
space_block
    : instanciation_block
    | reference_block
    | NEWLINE
    ;
//blocks that perform instantiation
instanciation_block
    : models_scope
    | controllers_scope
    ;
models_scope returns [Scope scope]
    : 'models:' instantiations #ModelInstanciation
    ;
controllers_scope returns [Scope scope]
    : 'controllers:' instantiations #ControllerInstanciation
    ;
instantiations
    : NEWLINE INDENT instance_def+ DEDENT
    ;
instance_def returns [Symbol symbol]
    : Identifier '=' instance_name '(' param_assig_list? ')' NEWLINE? #Instance
    ;
instance_name
    : Identifier
    ;
param_assig_list
    : param_assig (',' param_assig)? #ParameterAssignments
    ;
param_assig
    : Identifier '=' param_val
    ;
//We do not allow expressions to be assigned during instantiation
//TODO: error making sense
param_val : literal | SELF;

//blocks that only wire
reference_block
    : platform_scope
    | sink_scope
    | source_scope
    ;
platform_scope returns [Scope scope]
    : 'platform:' space_assignments #PlatformScope
    ;
sink_scope returns [Scope scope]
    : 'sinks:' space_assignments #SinkLinks
    ;

source_scope returns [Scope scope]
    : 'sources:' space_assignments #SourceLinks
    ;
space_assignments returns [Symbol symbol]
    : NEWLINE INDENT ref_assig+ DEDENT
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
    : Identifier '=' propValue NEWLINE #VarAssignment
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
    | BooleanLiteral
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
    : Identifier '=' field_type '(' field_options? ')'  NEWLINE? #FieldDeclaration
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

field_options
    : field_option (',' field_option)?
    ;
//for now field options are build and checked in field symbol rather than in lexer
field_option
    : Identifier '=' literal
    ;
/**
 * Controller rules
 */
controller_comp returns [Scope scope]
    : CONTROLLER Identifier component_parameters ':' controller_scope # ControllerScope
    ;

controller_scope
    : NEWLINE INDENT controller_body+ DEDENT
    ;
/**
 * controller body and event body are similar, excep that
 * no tests or function calls are allowed in the controller body
 * all the assigments are evaluated staticaly inside the controller body
 * while in event expresions are evaluated on call
 */
controller_body
    : eventdef // can only be events
    | ref_assig // reference: wiring tier componets to local variables
    | variable_declaration //variable assignment
    | NEWLINE
    ;
eventdef returns [Scope scope]
    : EVENT qualified_name  '(' functionArgsList? ')' ':' block_stmt #EventScope
    ;
functionArgsList
    :functionArg (',' functionArg)?
    ;
functionArg
    : arg_type arg_name
    ;
arg_type: Identifier ;
arg_name: Identifier ;

block_stmt
    : NEWLINE INDENT blockStatement+ DEDENT
    ;
/** event block can be all this */
blockStatement
    : ref_assig // reference
    | variable_declaration // or variable assignment
    | funct_expr // model command or query
    | comp_stmt // conditional or loops
    | PASS
    | NEWLINE
    ;
comp_stmt
    : while_stmt
    | if_stmt
    | del_stmt
    | for_stmt
    | repeat_stmt
    ;
repeat_stmt
    : REPEAT expr TIMES ':' block_stmt
    ;
del_stmt
    : DELETE qualified_name '(' elementValuePairs? ')' #DeleteStmt
    ;
variable_declarations
    :   variable_declaration (',' variable_declaration)*
    ;
variable_declaration
    :   Identifier '=' variableInitializer
    ;
variableInitializer
    :   arrayInitializer

    ;
arrayInitializer
    :   '[' (variableInitializer (',' variableInitializer)* (',')? )? ']'
    ;
/// while_stmt: 'while' test ':' suite ['else' ':' suite]
while_stmt
 : WHILE comp_expr ':' block_stmt
 ;

/// for_stmt: 'for' exprlist 'in' testlist ':' suite ['else' ':' suite]
for_stmt
 : FOR forControl ':' block_stmt
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
    | BooleanLiteral
    | qualified_name
    ;
//for_stmt
// : FOR exprlist IN testlist ':' suite ( ELSE ':' suite )?
// ;
forControl
    :   exprlist IN testlist
    ;

exprlist
    :   variable_declarations
    |   expressionList
    ;

testlist
    :   expressionList
    ;

whileControl
    : comp_expr  #WhileStmt
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
    //: //expression
    : elementValueArrayInitializer
    ;
elementValueArrayInitializer
    :   '{' (elementValue (',' elementValue)*)? (',')? '}'
    ;
expressionList
    :   //expression (',' expression)*
    ;
//reference is a dottend name accesesing scopes
ref_assig_list
    :ref_assig (',' ref_assig)? #ReferenceAssignmentsList
    ;

ref_assig
    : key '=' value  #ReferenceAssignment
    ;
key: qualified_name;
value: qualified_name | SELF ;

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
//controller
EVENT               : 'event' ;
COMMAND             : 'command' ;

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
REPEAT              : 'repeat' ;
TIMES               : 'times' ;

//comparators

comp_op
    : LESS_THAN
    | GREATER_THAN
    | EQUALS
    | GT_EQ
    | LT_EQ
    | NOT_EQ_1
    | NOT_EQ_2
    | IN
    | NOT IN
    | IS
    | IS NOT
    ;

LESS_THAN       : '<';
GREATER_THAN    : '>';
EQUALS          : '==';
GT_EQ           : '>=';
LT_EQ           : '<=';
NOT_EQ_1        : '<>';
NOT_EQ_2        : '!=';

//operands
STAR            : '*';
POWER           : '**';
OR_OP           : '|';
XOR             : '^';
AND_OP          : '&';
LEFT_SHIFT      : '<<';
RIGHT_SHIFT     : '>>';
ADD             : '+';
MINUS           : '-';
DIV             : '/';
MOD             : '%';
NOT_OP          : '~';

//operand assigments
ARROW : '->';
ADD_ASSIGN : '+=';
SUB_ASSIGN : '-=';
MULT_ASSIGN : '*=';
DIV_ASSIGN : '/=';
MOD_ASSIGN : '%=';
AND_ASSIGN : '&=';
OR_ASSIGN : '|=';
XOR_ASSIGN : '^=';
LEFT_SHIFT_ASSIGN : '<<=';
RIGHT_SHIFT_ASSIGN : '>>=';
POWER_ASSIGN : '**=';


literal
    :   IntegerLiteral
    |   FloatingPointLiteral
    |   CharacterLiteral
    |   BooleanLiteral
    |   StringLiteral
    |   NullLiteral
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

BooleanLiteral
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
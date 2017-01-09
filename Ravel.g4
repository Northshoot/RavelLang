grammar Ravel;

@header{
import org.stanford.ravel.compiler.scope.*;
import org.stanford.ravel.compiler.symbol.*;
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
    : 'platform:' space_assignments #PlatformScope
    ;

space_assignments returns [Symbol symbol]
    : NEWLINE INDENT space_assigment+ DEDENT
    ;

space_assigment
    : ref_assig
    | NEWLINE
    ;

models_scope returns [Scope scope]
    : 'models:' instantiations #ModelInstantiation
    ;
instantiations
    : NEWLINE INDENT instance_def+ DEDENT
    ;

instance_def returns [Symbol symbol]
    : Identifier '=' instance_name '(' param_assig_list? ')' NEWLINE? #Instance
    ;

param_assig_list
    : param_assig (',' param_assig)? #ParameterAssignments
    ;

param_assig
    : Identifier '=' param_val
    ;

param_val : literal | SELF;

instance_name
    : Identifier
    ;
controllers_scope returns [Scope scope]
    : 'controllers:' instantiations #ControllerInstantiation
    ;
sink_scope returns [Scope scope]
    : 'sinks:' space_assignments #SinkLinks
    ;

source_scope returns [Scope scope]
    : 'sources:' space_assignments #SourceLinks
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
    | literal
    ;

propArray
    : '[' literal (',' literal)* ']'
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
    | blockStatement
    ;

/**
 *All here for handling normal language statements and expressions
 *
 */

eventdef returns [Scope scope]
    : EVENT qualified_name  function_args ':' block_stmt #EventScope
    ;

block_stmt
    : NEWLINE INDENT blockStatement+ DEDENT #Block
    ;

blockStatement
    : ref_assig // reference
    | variable // or variable assignment
    | funct_expr
    | comp_stmt
    | NEWLINE
    ;
comp_stmt
    : while_stmt
    | if_stmt
    | del_stmt
    | for_stmt
    | NEWLINE
    ;
del_stmt
    : DELETE funct_expr #DeleteStmt
    ;

variable
    :   Identifier '=' variableInitializer
    ;

variableInitializer
    :   arrayInitializer
    |   expression
    ;
arrayInitializer
    :   '[' (variableInitializer (',' variableInitializer)* (',')? )? ']'
    ;
/// while_stmt: 'while' test ':' suite ['else' ':' suite]
while_stmt
 : WHILE comp_expr ':' block_stmt #WhileStatement
 ;

/// for_stmt: 'for' exprlist 'in' testlist ':' suite ['else' ':' suite]
for_stmt
     : FOR forControl ':' block_stmt #ForStatement
     ;


if_stmt
    : IF comp_expr ':' block_stmt ( ELIF comp_expr ':' block_stmt )? ( ELSE ':' block_stmt )? #IfStatement
    ;

comp_expr
    : comparison (AND comparison)? (OR comparison)? #CompExpr
    ;

comparison
    : expr (comp_op expr)* #CompRule
    ;
expr
    : atom //this is prep for future to implement advance comparisons
    ;
atom
    : Identifier
    | number
    | boolean_rule
    | qualified_name
    ;
//for_stmt
// : FOR exprlist IN testlist ':' suite ( ELSE ':' suite )?
// ;
forControl
    :   exprlist IN testlist
    ;

exprlist
    :   variable
    |   expressionList
    ;

testlist
    :   expressionList
    ;
function_args
    : '(' functionArgsList? ')'
    ;
functionArgsList
    :functionArg (',' functionArg)?
    ;
whileControl
    : comp_expr  #WhileStmt
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
    | NEWLINE
    ;

primary
    : '(' expression ')'
    | 'self'
    | literal
    | qualified_name
    ;
//reference is a dottend name accesesing scopes
ref_assig_list
    :ref_assig (',' ref_assig)? #ReferenceAssignmentsList
    ;

ref_assig
    : reference_name '=' reference_value  #ReferenceAssignment
    ;
reference_name: qualified_name;
reference_value: qualified_name | SELF ;

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

NEWLINE
 : ( {atStartOfInput()}?   SPACES
   | ( '\r'? '\n' | '\r' ) SPACES?
   )
   {
     String newLine = getText().replaceAll("[^\r\n]+", "");
     String spaces = getText().replaceAll("[\r\n]+", "");
     int next = _input.LA(1);

     if (opened > 0 || next == '\r' || next == '\n' || next == '/') {
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
    : number
    | boolean_rule
    | string
    | Identifier
    | NullLiteral
    ;
string
    : '"' Identifier? '"'
    ;
number
     : integer
     | float_point
     ;
/// integer        ::=  decimalinteger | octinteger | hexinteger | bininteger
integer
     : DECIMAL_INTEGER
     ;
DECIMAL_INTEGER
     : NON_ZERO_DIGIT DIGIT*
     | '0'+
     ;
fragment NON_ZERO_DIGIT
     : [1-9]
     ;
/// digit          ::=  "0"..."9"
fragment DIGIT
     : [0-9]
     ;
/// floatnumber   ::=  pointfloat | exponentfloat
float_point
     : FLOAT_NUMBER
     ;

FLOAT_NUMBER
    : POINT_FLOAT
    ;

fragment POINT_FLOAT
     : INT_PART? FRACTION
     | INT_PART '.'
     ;
fragment INT_PART
     : DIGIT+
     ;
/// fraction      ::=  "." digit+
fragment FRACTION
     : '.' DIGIT+
     ;

boolean_rule
    : TRUE
    | FALSE
    ;

NullLiteral
    :   'null'
    ;

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

Identifier
    :   ID_START ID_CONTINUE*
    ;

fragment ID_START
     : '_'
     | [A-Z]
     | [a-z]
     ;

fragment ID_CONTINUE
     : ID_START
     | [0-9]
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
 : '//' ~[\r\n]*
 ;

fragment LINE_JOINING
 : '\\' SPACES? ( '\r'? '\n' | '\r' )
 ;


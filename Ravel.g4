grammar Ravel;

@header{
import ai.harmony.ravel.compiler.scope.*;
import ai.harmony.ravel.compiler.symbol.*;
import ai.harmony.ravel.compiler.types.Type;
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



/*
 * parser rules
 */
 //only file inputs are accepted

//the file can be newlines or components definitions
//TODO: add imports
file_input returns [Scope scope]
    : ( NEWLINE | import_stmt | comp_def )* EOF
    ;

import_stmt
 : import_name #importName
 | import_from #importFrom
 ;

/// import_name: 'import' dotted_as_names
import_name
 : IMPORT dotted_as_names
 ;

/// # note below: the ('.' | '...') is necessary because '...' is tokenized as ELLIPSIS
/// import_from: ('from' (('.' | '...')* dotted_name | ('.' | '...')+)
///               'import' ('*' | '(' import_as_names ')' | import_as_names))
import_from
 : FROM ( ( '.' | '...' )* dotted_name
        | ('.' | '...')+
        )
   IMPORT ( '*'
          | '(' import_as_names ')'
          | import_as_names
          )
 ;

/// import_as_name: NAME ['as' NAME]
import_as_name
 : Identifier ( AS Identifier )?
 ;

/// dotted_as_name: dotted_name ['as' NAME]
dotted_as_name
 : dotted_name ( AS Identifier )?
 ;

/// import_as_names: import_as_name (',' import_as_name)* [',']
import_as_names
 : import_as_name ( ',' import_as_name )* ','?
 ;
/// dotted_as_names: dotted_as_name (',' dotted_as_name)*
dotted_as_names
 : dotted_as_name ( ',' dotted_as_name )*
 ;

/// dotted_name: NAME ('.' NAME)*
dotted_name
 : Identifier ( '.' Identifier )*
 ;
/**
 *
 * Ravel application consists of componets
 */
//we have models, views, controllers, spaces, interfaces
comp_def
    : model_comp
    | controller_comp
    | iface_comp
    | view_comp
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
    | interface_scope
    | views_scope
    | NEWLINE
    ;

platform_scope returns [Scope scope]
    : 'platform:' space_assignments #PlatformScope
    ;

space_assignments returns [Symbol symbol]
    : NEWLINE INDENT space_assigment+ DEDENT
    ;

space_assigment
    : ref_assign
    | NEWLINE
    ;

// a simplified version of an assignment, to use in constant expression contexts
// (eg. in model and space declarations)
ref_assign
    : qualified_name '=' simple_expression
    ;

simple_expression
    : literal
    | qualified_name
    ;

models_scope returns [Scope scope]
    : 'models:' instantiations #ModelInstantiation
    ;
instantiations
    : NEWLINE INDENT instance_line+ DEDENT
    ;

instance_line
    : instance_def
    | NEWLINE
    ;

instance_def returns [InstanceSymbol symbol]
    : Identifier '=' instance_name '(' param_assig_list? ')' NEWLINE? #Instance
    ;

param_assig_list
    : param_assig (',' param_assig)* #ParameterAssignments
    ;

param_assig
    : Identifier '=' param_val
    ;

param_val : simple_expression;

instance_name
    : Identifier
    ;
controllers_scope returns [Scope scope]
    : 'controllers:' instantiations #ControllerInstantiation
    ;
interface_scope returns [Scope scope]
    : 'interfaces:' instantiations #InterfaceInstantiation
    ;
views_scope returns [Scope scope]
    : 'views:' instantiations #ViewInstantiation
    ;

/**
 *
 * Interface parser rules
 */
iface_comp returns [Scope scope]
    : INTERFACE Identifier function_args ':' iface_body #InterfaceScope
    ;

iface_body
    : NEWLINE INDENT config_scope? uses_scope? impl_scope iface_members* DEDENT
    ;

impl_scope returns [Scope scope]
    : 'implementation:' space_assignments* #ImplementationScope
    ;

config_scope returns [Scope scope]
    : 'configuration:' space_assignments* #ConfigurationScope
    ;

uses_scope returns [Scope scope]
    : 'use:' space_assignments* #UsesScope
    ;

iface_members
    : iface_def
    | iface_event
    | NEWLINE
    ;

iface_def returns [MethodDeclarationSymbol symbol]
    : DEF Identifier '(' typed_identifier_list? ')' (':' type)? #InterfaceDef
    ;

iface_event returns [MethodDeclarationSymbol symbol]
    : EVENT Identifier '(' typed_identifier_list? ')' #InterfaceEvent
    ;

/**
 *
 * View parser rules
 */
view_comp returns [Scope scope]
    : VIEW Identifier '(' ')' ':' view_body #ViewScope
    ;

view_body
    : NEWLINE INDENT uses_scope? iface_members* DEDENT
    ;


/**
 *
 * Model parser rules
 */
model_comp returns [Scope scope]
    :  modelType MODEL Identifier  function_args ':' model_body # ModelScope
    ;


modelType
    : LOCAL
    | STREAMING
    | REPLICATED
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
    : NEWLINE INDENT property_line+ DEDENT
    ;

property_line
    : ref_assign
    | flow_assign
    | NEWLINE
    ;
// TODO: add flow lists, star, and flow as parameter
flow_assign
    : FLOW '=' Identifier ('->' Identifier)+ #DirectedFlow
    | FLOW '=' Identifier (',' Identifier)+ #UndirectedFlow
    ;

schema_block returns [Scope scope]
    :'schema:' fields #SchemaScope
    ;

fields
    : NEWLINE INDENT field_line+ DEDENT
    ;

field_line
    : field
    | NEWLINE
    ;

field
    : Identifier ':' type #FieldDeclaration
    ;

/**
 * Controller stuff
 */

controller_comp returns [Scope scope]
    : CONTROLLER Identifier function_args ':' controller_scope # ControllerScope
    ;

controller_scope
    : NEWLINE INDENT controller_entry* DEDENT
    ;

controller_entry
    : eventdef #EventDefinition
    | Identifier (':' type)? '=' simple_expression #ControllerVariableDefinition
    | Identifier ':' type '=' '[' (literal (',' literal)+)? ']' #ControllerArrayConstant
    | NEWLINE #ControllerNewline
    ;

/**
 *All here for handling normal language statements and expressions
 *
 */

eventdef returns [Scope scope]
    : EVENT Identifier '.' Identifier  function_args ':' block_stmt #EventScope
    ;

block_stmt returns [Scope scope]
    : NEWLINE INDENT statement+ DEDENT #Block
    ;

statement
    : var_decl
    | assignment
    | expression // expression statement (eg function call)
    | del_stmt
    | while_stmt
    | if_stmt
    | for_stmt
    | break_stmt
    | continue_stmt
    | return_stmt
    | PASS
    | NEWLINE
    ;

del_stmt
    : DELETE lvalue_expression #DeleteStmt
    ;

break_stmt
    : BREAK
    ;

continue_stmt
    : CONTINUE
    ;

return_stmt
    : RETURN expression?
    ;

lvalue
    : lvalue_expression (',' lvalue_expression)*
    ;

assign_op
    : '=' | '+=' | '-=' | '*=' | '/=' | '//=' | '^=' | '<<=' | '>>=' ;

ident_decl
    : Identifier (':' type)?
    ;

identifier_list
    : ident_decl (',' ident_decl )*
    ;

typed_ident_decl
    : Identifier ':' type #TypedIdentDecl
    ;

typed_identifier_list
    : typed_ident_decl (',' typed_ident_decl )*
    ;

var_decl
    : identifier_list ('=' expressionList)?
    ;

type
    : Identifier ('.' Identifier)* array_marker*;

array_marker: '[' DECIMAL_INTEGER? ']' ;

assignment
    : lvalue assign_op expressionList
    ;

// an expression that evaluates to an lvalue:
// could be an identifier, an expression followed by member access,
// or an expression followed by array access
lvalue_expression
    : Identifier
    | primary '.' Identifier
    | primary '[' expression ']'
    ;

expressionList
    :   expression (',' expression)*
    ;

atom
    : '(' expression ')'
    | Identifier
    | literal
    | array_literal
    ;

array_literal
    : '[' (expressionList (',')?)? ']'
    ;

method_call
    : '.' Identifier '(' expressionList? ')' ;

primary
    : cast_op? atom access_op*
    ;

cast_op returns [Type computedType]
    : '(' type ')'
    ;

access_op
    : array_access
    | method_call
    | member_access
    ;

member_access
    : '.' Identifier ;

array_access
    : '[' expression ']' ;

power_exp
    : primary ('**' unary_exp)? ;

unary_op : '-' | '+' | '~' ;
unary_exp
    : power_exp
    | unary_op unary_exp
    ;

mult_op : '*' | '/' | '//' | '%' ;
mult_exp
    : unary_exp
    | mult_exp mult_op unary_exp
    ;

add_op : '+' | '-' ;
add_exp
    : mult_exp
    | add_exp add_op mult_exp
    ;

shift_op : '<<' | '>>' ;
shift_exp
    : add_exp
    | shift_exp shift_op add_exp
    ;

bin_and_exp
    : shift_exp
    | bin_and_exp '&' shift_exp
    ;
bin_xor_exp
    : bin_and_exp
    | bin_xor_exp '^' bin_and_exp
    ;
bin_or_exp
    : bin_xor_exp
    | bin_or_exp '|' bin_xor_exp
    ;

comp_op
    : GT
    | LT
    | EQUAL
    | LE
    | GE
    | NOTEQUAL
    //| IN
    //| NOT IN
    //| IS
    //| IS NOT
    ;
comp_exp
    : bin_or_exp (comp_op bin_or_exp)*
    ;

not_exp
    : comp_exp
    | NOT not_exp
    ;

and_exp
    : not_exp
    | and_exp AND not_exp
    ;

or_exp
    : and_exp
    | or_exp OR and_exp
    ;

expression
    : or_exp
    ;


/// while_stmt: 'while' test ':' suite ['else' ':' suite]
while_stmt
    : WHILE expression ':' block_stmt #WhileStatement
    ;

/// for_stmt: 'for' exprlist 'in' testlist ':' suite ['else' ':' suite]
for_stmt returns [Scope scope]
    : FOR forControl ':' block_stmt #ForStatement
    | FOR ident_decl '=' expression 'to' expression ('step' expression)? ':' block_stmt #CLikeForStatement
    ;

forControl
    : ident_decl IN expression
    ;

if_stmt returns [Scope scope]
    : IF expression ':' block_stmt ( ELIF expression ':' block_stmt )* ( ELSE ':' block_stmt )? #IfStatement
    ;

qualified_name
    : Identifier ('.' Identifier)*
    ;

function_args
    : '(' typed_identifier_list? ')'
    ;

/*
 * lexer rules
 */
 // Keywords
MODEL               : 'model' ;
SPACE               : 'space' ;
CONTROLLER          : 'controller' ;
VIEW                : 'view';
FLOW                : 'flow' ;
LOCAL               : 'local' ;
STREAMING           : 'streaming' ;
REPLICATED          : 'replicated' ;

// interface
INTERFACE           : 'interface' ;
DEF                 : 'def' ;

//controller
EVENT               : 'event' ;
COMMAND             : 'command' ;

//import python style
FROM : 'from';
IMPORT : 'import';
AS : 'as';

//expression operators
ASSERT              : 'assert' ;
RETURN              : 'return' ;
TRUE                : 'True' ;
FALSE               : 'False' ;
IF                  : 'if' ;
ELIF                : 'elif' ;
ELSE                : 'else';
FOR                 : 'for' ;
WHILE               : 'while' ;
AND                 : 'and' ;
NOT                 : 'not';
OR                  : 'or' ;
IN                  : 'in' ;
IS                  : 'is' ;
DELETE              : 'del' ;
PASS                : 'pass';
CONTINUE            : 'continue';
BREAK               : 'break' ;
NONE                : 'None' ;

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
    | STRING_LITERAL
    | NONE
    ;

/// string     ::=  "'" stringitem* "'" | '"' stringitem* '"'
STRING_LITERAL
    : '\'' ( STRING_ESCAPE_SEQ | ~[\\\r\n'] )* '\''
    | '"' ( STRING_ESCAPE_SEQ | ~[\\\r\n"] )* '"'
    ;

/// stringescapeseq ::=  "\" <any source character>
fragment STRING_ESCAPE_SEQ
 : '\\' .
 ;

number
     : integer
     | float_point
     ;

/// integer        ::=  decimalinteger | octinteger | hexinteger | bininteger
integer
     : DECIMAL_INTEGER | OCT_INTEGER | HEX_INTEGER | BIN_INTEGER
     ;
/// decimalinteger ::=  nonzerodigit digit* | "0"+
DECIMAL_INTEGER
 : NON_ZERO_DIGIT DIGIT*
 | '0'+
 ;

/// octinteger     ::=  "0" ("o" | "O") octdigit+
OCT_INTEGER
 : '0' [oO] OCT_DIGIT+
 ;

/// hexinteger     ::=  "0" ("x" | "X") hexdigit+
HEX_INTEGER
 : '0' [xX] HEX_DIGIT+
 ;

/// bininteger     ::=  "0" ("b" | "B") bindigit+
BIN_INTEGER
 : '0' [bB] BIN_DIGIT+
 ;


/// nonzerodigit   ::=  "1"..."9"
fragment NON_ZERO_DIGIT
 : [1-9]
 ;

/// digit          ::=  "0"..."9"
fragment DIGIT
 : [0-9]
 ;

/// octdigit       ::=  "0"..."7"
fragment OCT_DIGIT
 : [0-7]
 ;

/// hexdigit       ::=  digit | "a"..."f" | "A"..."F"
fragment HEX_DIGIT
 : [0-9a-fA-F]
 ;

/// bindigit       ::=  "0" | "1"
fragment BIN_DIGIT
 : [01]
 ;

/// floatnumber   ::=  pointfloat | exponentfloat
float_point
     : FLOAT_NUMBER
     ;

FLOAT_NUMBER
    : POINT_FLOAT | EXPONENT_FLOAT
    ;

/// pointfloat    ::=  [intpart] fraction | intpart "."
fragment POINT_FLOAT
 : INT_PART? FRACTION
 | INT_PART '.'
 ;

/// exponentfloat ::=  (intpart | pointfloat) exponent
fragment EXPONENT_FLOAT
 : ( INT_PART | POINT_FLOAT ) EXPONENT
 ;

/// intpart       ::=  digit+
fragment INT_PART
 : DIGIT+
 ;

/// fraction      ::=  "." digit+
fragment FRACTION
 : '.' DIGIT+
 ;

/// exponent      ::=  ("e" | "E") ["+" | "-"] digit+
fragment EXPONENT
 : [eE] [+-]? DIGIT+
 ;

boolean_rule
    : TRUE
    | FALSE
    ;

NullLiteral
    :   'null'
    ;

// §3.12 Operators

DOT : '.';
ELLIPSIS : '...';
STAR : '*';
OPEN_PAREN : '(' {opened++;};
CLOSE_PAREN : ')' {opened--;};
COMMA : ',';
COLON : ':';
SEMI_COLON : ';';
POWER : '**';
ASSIGN : '=';
OPEN_BRACK : '[' {opened++;};
CLOSE_BRACK : ']' {opened--;};
OR_OP : '|';
XOR : '^';
AND_OP : '&';
LEFT_SHIFT : '<<';
RIGHT_SHIFT : '>>';
ADD : '+';
MINUS : '-';
DIV : '/';
MOD : '%';
IDIV : '//';
NOT_OP : '~';
OPEN_BRACE : '{' {opened++;};
CLOSE_BRACE : '}' {opened--;};
LT : '<';
GT : '>';
EQUAL : '==';
GE : '>=';
LE : '<=';
NOTEQUAL : '<>' | '!=';
AT : '@';
ARROW : '->';
ADD_ASSIGN : '+=';
SUB_ASSIGN : '-=';
MULT_ASSIGN : '*=';
AT_ASSIGN : '@=';
DIV_ASSIGN : '/=';
MOD_ASSIGN : '%=';
AND_ASSIGN : '&=';
OR_ASSIGN : '|=';
XOR_ASSIGN : '^=';
LEFT_SHIFT_ASSIGN : '<<=';
RIGHT_SHIFT_ASSIGN : '>>=';
POWER_ASSIGN : '**=';
IDIV_ASSIGN : '//=';

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

UNKNOWN_CHAR
 : .
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
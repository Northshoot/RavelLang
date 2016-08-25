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

parameters
    : '(' typedargslist? ')'
    ;
//variable assigments
typedargslist
    : identifier ( '=' tdefvar )? ( ',' identifier ( '=' tdefvar )? )*
    | dotted_name
    ;
//variable
identifier
    : NAME
    ;
tdefvar
    : integer
    | number
    | string
    | bool
    | identifier
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
    : SPACE identifier ':' space_body #SpaceScope
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
    ;

platform_scope returns [Scope scope]
    : 'platform:' properties #PlatformScope
    ;
models_scope returns [Scope scope]
    : 'models:' instantiations #ModelInstanciation
    ;
instantiations
    : NEWLINE INDENT instance+ DEDENT
    ;
instance
    : identifier '=' instance_name parameters
    ;
instance_name
    : NAME
    ;
controllers_scope returns [Scope scope]
    : 'controllers:' instantiations #ControllerInstanciation
    ;
sink_scope returns [Scope scope]
    : 'sinks:' references #SinkLinks
    ;

references returns [Scope scope]
    : NEWLINE INDENT ref_assig+ DEDENT
    ;
source_scope returns [Scope scope]
    : 'sources:' references #SourceLinks
    ;
/**
 *
 * Model parser rules
 */
model_comp returns [Scope scope]
    :  modelType MODEL identifier  parameters ':' model_body # ModelScope
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
    ;
properties_block returns [Scope scope]
    : 'properties:' properties #PropertiesScope
    ;
properties
    : NEWLINE INDENT tdefvar_assig+ DEDENT
    ;

tdefvar_assig
    : identifier '=' tdefvar NEWLINE? #VarAssigment
    ;
schema_block returns [Scope scope]
    :'schema:' fields #SchemaScope
    ;

fields
    : NEWLINE INDENT field+ DEDENT
    ;

field
    : identifier '=' field_type parameters NEWLINE? #FieldDeclaration
    ;

/**
 *
 * Field types, currently only in parser. But maybe it should be like a Ravel library
 * that you can write your own field in Ravel and then get it translated
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
    ;

model_field_identifier
    : dotted_name
    ;
controller_comp returns [Scope scope]
    : CONTROLLER identifier parameters ':' controller_scope # ControllerScope
    ;

controller_scope
    : NEWLINE INDENT controller_body+ DEDENT
    ;

controller_body
    : eventdef // can only be events
    | ref_assig // reference
    | tdefvar_assig // or variable assigment
    | NEWLINE
    ;

//reference is a dottend name accesesing scopes
ref_assig
    : identifier '=' dotted_name #ReferenceAssigment
    ;
eventdef returns [Scope scope]
    : EVENT identifier  function_args ':' suite #EventScope
    ;
function_args
    : '(' function_param? ')'
    ;
function_param
    : param_prair (',' param_prair)*
    ;
param_prair
    : param_type param_name
    ;

param_type : NAME;
param_name : NAME;

suite
    :
    | NEWLINE INDENT stmt+ DEDENT
    ;
stmt
    : small_stmt
    | compound_stmt
    ;

small_stmt
    : tdefvar_assig
    | model_field_assig
    | del_stmt
    | function_call
    | flow_stmt
    | assert_stmt
    ;

model_field_assig
    : model_field_identifier '=' model_assig_expr NEWLINE
    ;

model_assig_expr
    : SELF
    | tdefvar
    | funct_expr
    | local_query
    ;
compound_stmt
    : if_stmt
    | while_stmt
    | for_stmt
    ;
assert_stmt
    : ASSERT test ( ',' test )?
    ;
del_stmt
    : DELETE query_expr
    ;

function_call
    : query_expr
    | funct_expr
    ;
query_expr
    : local_query
    | db_query
    ;
local_query
    : identifier '.' query_operation #QueryOperations
    ;
query_operation
    : 'first()'
    | 'last()'
    ;
db_query
    : 'query' '.' query_operation
    ;
funct_expr
    : func_no_return
    | func_with_return
    ;

func_no_return
    : identifier ('.' identifier)? parameters NEWLINE
    ;

func_with_return
    : identifier '=' func_no_return
    ;
dotted_name
    : NAME ( '.' NAME )*
    ;

pass_stmt
    : PASS
    ;
flow_stmt
    : break_stmt
    | continue_stmt
    | return_stmt
    ;


break_stmt
    : BREAK
    ;

continue_stmt
    : CONTINUE
    ;

return_stmt
    : RETURN identifier
    ;

if_stmt
    : IF test ':' suite ( ELIF test ':' suite )* ( ELSE ':' suite )?
    ;

while_stmt
    : WHILE test ':' suite ( ELSE ':' suite )?
    ;

    /// for_stmt: 'for' exprlist 'in' testlist ':' suite ['else' ':' suite]
for_stmt
    : FOR identifier IN identifier ':' suite
    ;

test
    : or_test ( IF or_test ELSE test )?
    ;

or_test
    : and_test ( OR and_test )*
    ;

/// and_test: not_test ('and' not_test)*
and_test
    : not_test ( AND not_test )*
    ;

/// not_test: 'not' not_test | comparison
not_test
    : NOT not_test
    | comparison
    ;

/// comparison: star_expr (comp_op star_expr)*
comparison
    : expr ( comp_op expr )*
    ;


/// # <> isn't actually a valid comparison operator in Python. It's here for the
/// # sake of a __future__ import described in PEP 401
/// comp_op: '<'|'>'|'=='|'>='|'<='|'<>'|'!='|'in'|'not' 'in'|'is'|'is' 'not'
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
/// expr: xor_expr ('|' xor_expr)*
expr
 : xor_expr ( '|' xor_expr )*
 ;

/// xor_expr: and_expr ('^' and_expr)*
xor_expr
 : and_expr ( '^' and_expr )*
 ;

/// and_expr: shift_expr ('&' shift_expr)*
and_expr
 : shift_expr ( '&' shift_expr )*
 ;

/// shift_expr: arith_expr (('<<'|'>>') arith_expr)*
shift_expr
 : arith_expr ( '<<' arith_expr
              | '>>' arith_expr
              )*
 ;

/// arith_expr: term (('+'|'-') term)*
arith_expr
 : term ( '+' term
        | '-' term
        )*
 ;

/// term: factor (('*'|'/'|'%'|'//') factor)*
term
 : factor ( '*' factor
          | '/' factor
          | '%' factor
          | '//' factor
          | '@' factor // PEP 465
          )*
 ;

/// factor: ('+'|'-'|'~') factor | power
factor
 : '+' factor
 | '-' factor
 | '~' factor
 | power
 ;

/// power: atom trailer* ['**' factor]
power
 : atom trailer* ( '**' factor )?
 ;

/// atom: ('(' [yield_expr|testlist_comp] ')' |
///        '[' [testlist_comp] ']' |
///        '{' [dictorsetmaker] '}' |
///        NAME | NUMBER | STRING+ | '...' | 'None' | 'True' | 'False')
atom
 : '(' ( testlist_comp )? ')'
 | '[' testlist_comp? ']'
 | NAME
 | number
 | string+
 | '...'
 | NONE
 | TRUE
 | FALSE
 ;

/// testlist_comp: test ( comp_for | (',' test)* [','] )
testlist_comp
 : test ( comp_for
        | ( ',' test )* ','?
        )
 ;

/// trailer: '(' [arglist] ')' | '[' subscriptlist ']' | '.' NAME
trailer
 : '(' arglist? ')'
 | '[' subscriptlist ']'
 | '.' NAME
 ;

arglist
 : ( argument ',' )* ( argument ','?
                     | '*' test ( ',' argument )* ( ',' '**' test )?
                     | '**' test
                     )
 ;

/// # The reason that keywords are test nodes instead of NAME is that using NAME
/// # results in an ambiguity. ast.c makes sure it's a NAME.
/// argument: test [comp_for] | test '=' test  # Really [keyword '='] test
argument
 : test comp_for?
 | test '=' test
 ;
/// subscriptlist: subscript (',' subscript)* [',']
subscriptlist
 : subscript ( ',' subscript )* ','?
 ;

/// subscript: test | [test] ':' [test] [sliceop]
subscript
 : test
 | test? ':' test? sliceop?
 ;

/// sliceop: ':' [test]
sliceop
 : ':' test?
 ;

/// exprlist: star_expr (',' star_expr)* [',']
exprlist
 : expr ( ',' expr )* ','?
 ;

/// testlist: test (',' test)* [',']
testlist
 : test ( ',' test )* ','?
 ;

/// comp_iter: comp_for | comp_if
comp_iter
 : comp_for
 | comp_if
 ;

/// comp_for: 'for' exprlist 'in' or_test [comp_iter]
comp_for
 : FOR exprlist IN or_test comp_iter?
 ;

/// comp_if: 'if' test_nocond [comp_iter]
comp_if
 : IF or_test comp_iter?
 ;
string
    : STRING_LITERAL
    | BYTES_LITERAL
    ;

number
    : integer
    | FLOAT_NUMBER
    | IMAG_NUMBER
    ;

/// integer        ::=  decimalinteger | octinteger | hexinteger | bininteger
integer
    : DECIMAL_INTEGER
    | OCT_INTEGER
    | HEX_INTEGER
    | BIN_INTEGER
    ;
bool
    : TRUE
    | FALSE
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
T_BOOLEAN_FIELD     : 'Boolean' ;
T_INTEGER_FIELD     : 'IntegerField';
T_NUMBER_FIELD      : 'NumberField' ;
T_DATE_FIELD        : 'DateField' ;
T_DATE_TIME_FIELD   : 'DateTime' ;
T_TIME_STAMP_FIELD  : 'TimestampField' ;
T_CONTEXT_FIELD     : 'ContextField' ;

//expression operators
ASSERT              : 'assert' ;
RETURN              : 'return' ;
TRUE                : 'true' ;
FALSE               : 'false' ;
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
NAME
 : ID_START ID_CONTINUE*
 ;


/// stringliteral   ::=  [stringprefix](shortstring | longstring)
/// stringprefix    ::=  "r" | "R"
STRING_LITERAL
 : [uU]? [rR]? ( SHORT_STRING | LONG_STRING )
 ;

/// bytesliteral   ::=  bytesprefix(shortbytes | longbytes)
/// bytesprefix    ::=  "b" | "B" | "br" | "Br" | "bR" | "BR"
BYTES_LITERAL
 : [bB] [rR]? ( SHORT_BYTES | LONG_BYTES )
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

/// floatnumber   ::=  pointfloat | exponentfloat
FLOAT_NUMBER
 : POINT_FLOAT
 | EXPONENT_FLOAT
 ;

/// imagnumber ::=  (floatnumber | intpart) ("j" | "J")
IMAG_NUMBER
 : ( FLOAT_NUMBER | INT_PART ) [jJ]
 ;

DOT             : '.';
STAR            : '*';
OPEN_PAREN      : '(' {opened++;};
CLOSE_PAREN     : ')' {opened--;};
COMMA           : ',';
COLON           : ':';
SEMI_COLON      : ';';
ASSIGN          : '=';
OPEN_BRACK      : '[' {opened++;};
CLOSE_BRACK     : ']' {opened--;};
OR_OP           : '|';
XOR             : '^';
AND_OP          : '&';
LEFT_SHIFT      : '<<';
RIGHT_SHIFT     : '>>';
ADD             : '+';
MINUS           : '-';
DIV             : '/';
MOD             : '%';
IDIV            : '//';
NOT_OP          : '~';
OPEN_BRACE      : '{' {opened++;};
CLOSE_BRACE     : '}' {opened--;};
LESS_THAN       : '<';
GREATER_THAN    : '>';
EQUALS          : '==';
GT_EQ           : '>=';
LT_EQ           : '<=';
NOT_EQ          : '!=';
AT              : '@';
ARROW           : '->';
ADD_ASSIGN      : '+=';
SUB_ASSIGN      : '-=';
MULT_ASSIGN     : '*=';
AT_ASSIGN       : '@=';
DIV_ASSIGN      : '/=';
MOD_ASSIGN      : '%=';
AND_ASSIGN      : '&=';
OR_ASSIGN       : '|=';
XOR_ASSIGN      : '^=';
LEFT_SHIFT_ASSIGN : '<<=';
RIGHT_SHIFT_ASSIGN : '>>=';
POWER_ASSIGN    : '**=';
IDIV_ASSIGN     : '//=';

SKIP_
    : ( SPACES | COMMENT | LINE_JOINING ) -> skip
    ;


/*
 * fragments
 */
fragment SHORT_STRING
 : '\'' ( STRING_ESCAPE_SEQ | ~[\\\r\n'] )* '\''
 | '"' ( STRING_ESCAPE_SEQ | ~[\\\r\n"] )* '"'
 ;

/// longstring      ::=  "'''" longstringitem* "'''" | '"""' longstringitem* '"""'
fragment LONG_STRING
 : '\'\'\'' LONG_STRING_ITEM*? '\'\'\''
 | '"""' LONG_STRING_ITEM*? '"""'
 ;

/// longstringitem  ::=  longstringchar | stringescapeseq
fragment LONG_STRING_ITEM
 : LONG_STRING_CHAR
 | STRING_ESCAPE_SEQ
 ;

/// longstringchar  ::=  <any source character except "\">
fragment LONG_STRING_CHAR
 : ~'\\'
 ;

/// stringescapeseq ::=  "\" <any source character>
fragment STRING_ESCAPE_SEQ
 : '\\' .
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

/// shortbytes     ::=  "'" shortbytesitem* "'" | '"' shortbytesitem* '"'
/// shortbytesitem ::=  shortbyteschar | bytesescapeseq
fragment SHORT_BYTES
 : '\'' ( SHORT_BYTES_CHAR_NO_SINGLE_QUOTE | BYTES_ESCAPE_SEQ )* '\''
 | '"' ( SHORT_BYTES_CHAR_NO_DOUBLE_QUOTE | BYTES_ESCAPE_SEQ )* '"'
 ;

/// longbytes      ::=  "'''" longbytesitem* "'''" | '"""' longbytesitem* '"""'
fragment LONG_BYTES
 : '\'\'\'' LONG_BYTES_ITEM*? '\'\'\''
 | '"""' LONG_BYTES_ITEM*? '"""'
 ;

/// longbytesitem  ::=  longbyteschar | bytesescapeseq
fragment LONG_BYTES_ITEM
 : LONG_BYTES_CHAR
 | BYTES_ESCAPE_SEQ
 ;

/// shortbyteschar ::=  <any ASCII character except "\" or newline or the quote>
fragment SHORT_BYTES_CHAR_NO_SINGLE_QUOTE
 : [\u0000-\u0009]
 | [\u000B-\u000C]
 | [\u000E-\u0026]
 | [\u0028-\u005B]
 | [\u005D-\u007F]
 ;

fragment SHORT_BYTES_CHAR_NO_DOUBLE_QUOTE
 : [\u0000-\u0009]
 | [\u000B-\u000C]
 | [\u000E-\u0021]
 | [\u0023-\u005B]
 | [\u005D-\u007F]
 ;

/// longbyteschar  ::=  <any ASCII character except "\">
fragment LONG_BYTES_CHAR
 : [\u0000-\u005B]
 | [\u005D-\u007F]
 ;

/// bytesescapeseq ::=  "\" <any ASCII character>
fragment BYTES_ESCAPE_SEQ
 : '\\' [\u0000-\u007F]
 ;

fragment SPACES
 : [ \t]+
 ;

fragment COMMENT
 : '#' ~[\r\n]*
 ;

fragment LINE_JOINING
 : '\\' SPACES? ( '\r'? '\n' | '\r' )
 ;


//https://github.com/antlr/grammars-v4/blob/master/python3/Python3.g4
/// id_start     ::=  <all characters in general categories Lu, Ll, Lt, Lm, Lo, Nl, the underscore, and characters with the Other_ID_Start property>
fragment ID_START
 : '_'
 | [A-Z]
 | [a-z]
 ;

/// id_continue  ::=  <all characters in id_start, plus characters in the categories Mn, Mc, Nd, Pc and others with the Other_ID_Continue property>
fragment ID_CONTINUE
 : ID_START
 | [0-9]
 ;
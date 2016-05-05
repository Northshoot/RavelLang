/** pseudo grammar for Ravel language */

// Ravel can only start from file
// start symbol
//          file_input is a module read from a input file

grammar RAVELPSEUDO;

file_input
  : NEWLINE
  | import_stmt
  | ravel
  ;

import_stmt
    : IMPORT NAME   #Import
    ;


// Ravel allows only declaring objects
// each object has a type
// OBJECT name (OBJECT_TYPE):
ravel
    : PRIMITIVE  NAME? '(' TYPE '):' suite # Primitive
    | config_stmt+                         # Config//one or more config statements
    | funcdef
    ;

// config_stmt is mandatory statements and consist of pairs
config_stmt
    : ('configuration' | 'properties' | 'schema') ':'
    | pair+
    ;

// pair is a STRING : value
pair:   STRING ':' value ;

funcdef
    : 'def' NAME parameters  ':' suite ;

parameters
    : ;
// object body is composed of statements
suite
    : simple_stmt
    | NEWLINE INDENT stmt+ DEDENT
    ;

stmt
    : simple_stmt
    | compound_stmt
    ;

simple_stmt
    : small_stmt (';' small_stmt)* NEWLINE
    ;

// the rest body of the object can contain statements
small_stmt
    : ( expr_stmt | flow_stmt | global_stmt )
    ;

expr_stmt
    : ;

// flow statements allowing controllr of execution
flow_stmt:
    break_stmt | continue_stmt | return_stmt
    ;
break_stmt:  BREAK ;
continue_stmt: CONTINUE;
return_stmt: RETURN ;

// declaration of global primite
global_stmt: GLOBAL NAME (',' NAME)* ;

compound_stmt
    : if_stmt
    | for_stmt
    | funcdef
    ;


if_stmt
    : IF TEST ':' suite (ELIF TEST ':' suite)*
    ;

for_stmt
    : FOR expr_list IN test_list ':' suite ;

expr_list
    : ;

test_list
    : ;


/*
 * lexer rules
 */

PRIMITIVE   : (MODEL|TRANSFORM|CONTROLLER|VIEW|SPACE|SETTINGS) ;
MODEL       :   [Mm][o][d][e][l] ; // Allow variation Model model
TRANSFORM   :   [Tt][r][a][n][s][f][o][r][m] ;
CONTROLLER  :   [Cc][o][n][t][r][o][l][l][e][r] ;
VIEW        :   [Vv][i][e][w] ;
SPACE       :   [Ss][p][a][c][e] ;
SETTINGS    :   [Ss][e][t][t][i][n][g][s] ;



DEF         : 'def';
RETURN      : 'return';
// used for imports
FROM        : 'from';
IMPORT      : 'import';
AS          : 'as';
// define variables
GLOBAL : 'global';
// flow statemenet
IF : 'if';
ELIF : 'elif';
ELSE : 'else';
FOR : 'for';
// boolean test
OR : 'or';
AND : 'and';
NOT : 'not';
IS : 'is';
NONE : 'None';
TRUE : 'True';
FALSE : 'False';

value
    :   STRING
    |   NUMBER
    |   FIELD
    |   'true'  // keywords
    |   'false'
    |   'null'
    ;
// teporarely def, annything goes
STRING :  '"' (ESC | ~["\\])* '"' ;
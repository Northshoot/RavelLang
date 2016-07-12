/** pseudo grammar for Ravel language */

// Ravel can only start from file
// start symbol
//          file_input is a module read from a input file

grammar RAVELPSEUDO;

//input is only allowed by file
// file consists of import statements
// or object declations
file_input
  : ( import_stmt | object ) + // recursively multiple imports and or  objects
  ;

// import takes name and ends with new line
import_stmt
    : IMPORT NAME NEWLINE
    ;

// Ravel objects are called primitives
// lang allows only declaring objects and functions inside the object
// each primitive has one to the primitive specific type
// OBJECT name (OBJECT_TYPE):
object
    : primitive NAME'(' primitive_decl  '):' NEWLINE
    | body+
    ;

//we have different types and declarations for each primitve
primitive_decl
    : ( model_decl | controller_decl | transform_decl | view_decl | space_decl)
    | funcdef+
    ;

//this could be enforced in application rather than compiler
model_decl
    : model_type
    | property_stmt
    | schema_stmt
    ;

model_type
    : ('Local' | 'Streaming' | 'Realtime' )
    ;
//
controller_decl
    : controller_type
    | config_stmt
    ;

controller_type
    : ('Timer' | 'Event')
    ;

//etc with the declations
transform_decl
    : ('Math' | 'Filter')
    ;

view_decl
    : ('Embedded' | 'Gateway' | 'Cloud')
    ;

space_decl
    : (property_stmt | config_stmt | 'models' | 'controllers' | 'transforms' | 'views' | 'sinks' | 'sources')
    ;



// object body consist of
// configuration statement (s) and function declaration (s)
body
    : NEWLINE
    | object_stmt+
    | funcdef+
    ;
// object_stmt is mandatory statements and consist of
// STRING = value pairs
//
object_stmt
//the choise here is to stmt in grammar or say that
//arbitary ID will do and check that in run time
// e.g. parser time error vs compile time error
    : (property_stmt | config_stmt | schema_stmt) ':'
    | pair (NEWLINE pair)+
    ;

property_stmt
    : 'property'
    ;

config_stmt
    : 'configuration'
    ;

schema_stmt
    :
    ;
// pair is a STRING amd  value
// depending on type of config_stmt
// the ID and value have to match their keywords and values
pair:   KEYWORD ':' value ;

// depending on the type of config_stmt

value
    : ID
    ;
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

primitive   : (MODEL|TRANSFORM|CONTROLLER|VIEW|SPACE|SETTINGS) ;
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
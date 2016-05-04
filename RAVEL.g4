/*
 * Ravel Language is a subset of Python 3, adding new types and primites
 * the intendation and writing style is talen strigh forward form Python
 * Antrl implementation is inspired by:
 * Project      : python3-parser; an ANTLR4 grammar for Python 3
 *                https://github.com/bkiers/python3-parser
 * https://github.com/antlr/grammars-v4/tree/master/python3
 * Developed by : Bart Kiers, bart@big-o.nl
 */
grammar RAVEL;
/*
 * parser rules
 */
/// file_input: (NEWLINE | stmt)* ENDMARKER
file_input
  : ( NEWLINE | stmt )* EOF
  ;

ravel: PROMITIVES  id? '(' prim_type '):' ;
;

/*
 * lexer rules
 */

PROMITIVE   : (MODEL|TRANSFORM|CONTROLLER|VIEW|SPACE|SETTINGS) ;
MODEL       :   [Mm][o][d][e][l] ; // Allow variation Model model
TRANSFORM   :   [Tt][r][a][n][s][f][o][r][m] ;
CONTROLLER  :   [Cc][o][n][t][r][o][l][l][e][r] ;
VIEW        :   [Vv][i][e][w] ;
SPACE       :   [Ss][p][a][c][e] ;
SETTINGS    :   [Ss][e][t][t][i][n][g][s] ;
DEF         : 'def';
RETURN      : 'return';
RAISE       : 'raise';
FROM        : 'from';
IMPORT      : 'import';
AS          : 'as';
GLOBAL      : 'global';
ASSERT      : 'assert';
IF          : 'if';
ELIF        : 'elif';
ELSE        : 'else';
WHILE       : 'while';
FOR         : 'for';
IN          : 'in';
OR          : 'or';
AND         : 'and';
NOT         : 'not';
IS          : 'is';
NONE        : 'None';
TRUE        : 'True';
FALSE       : 'False';
PASS        : 'pass';
CONTINUE    : 'continue';
BREAK       : 'break';

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
         emit(commonToken(Python3Parser.INDENT, spaces));
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

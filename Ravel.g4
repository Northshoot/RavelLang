grammar Ravel;

tokens { INDENT, DEDENT }
@header {
import java.text.SimpleDateFormat;
import java.util.Calendar;
}
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

file_input
    : ( NEWLINE | comp_def )* EOF
    ;

comp_def
    : model_comp
    | controller_comp
    | space_comp
    ;

model_comp
    :  modelType MODEL NAME  ':' suite # ModelDeclaration
    ;


modelType
    : LOCAL
    | STREAMING
    | REPLICATED
    ;

suite
    : NEWLINE INDENT block_def+ DEDENT
    ;

block_def
    : decl
    | event
    | NEWLINE
    ;

 decl
    : declType ':' NEWLINE block_suite #BlockSuite
    ;

declType
    : PROPERTIES
    | SCHEMA
    | CONTROLLERS
    | CONFIGURATION
    | MODELS
    ;


block_suite
    :  INDENT assigment+ DEDENT
    | NEWLINE
    ;

assigment
    : NAME '=' ( NAME | INT | field) NEWLINE #AssignmentDec
    ;

field
    : NAME '(' args* ')' #FieldDeclaration
    ;

args
    : arg (',' arg)*
    ;
arg
    : NAME '=' ( NAME | INT )
    ;

event
    : EVENT comp '.' trigger '():' stmt #EventDecl
    ;

comp : NAME;
trigger: NAME ;

//TODO: placeholder
stmt
    : expr_stmt
    | flow_stmt
    | NEWLINE
    ;
expr_stmt
    : NEWLINE INDENT assigment+ DEDENT
    ;

//TODO: placeholder
flow_stmt
 //: break_stmt
 //| continue_stmt
 : return_stmt
// | raise_stmt
// | yield_stmt
 ;

//TODO: placeholder for now
return_stmt
 : RETURN NAME
 ;

space_comp
    : SPACE NAME ':' suite # SpaceDeclaration
    ;

controller_comp
    : CONTROLLER NAME ':' suite # ControllerDeclaration
    ;

INT :   [0-9]+ ;


//NL: ('\r'? '\n' ' '*);
//WS      : [' ' \t]+ -> skip ;
/*
 * lexer rules
 */

MODEL : 'model' ;
SPACE : 'space' ;
CONTROLLER: 'controller' ;
VIEW: 'view';
FLOW: 'flow' ;
LOCAL    : 'local' ;
STREAMING: 'streaming' ;
REPLICATED: 'replicated';
PROPERTIES: 'properties' ;
SCHEMA: 'schema' ;
CONFIGURATION: 'configuration' ;
MODELS: 'models';
CONTROLLERS: 'controllers';
SINKS: 'sinks' ;
SOURCES: 'sources' ;
EVENT: 'event' ;
COMMAND:  'command' ;
RETURN : 'return' ;

T_INTEGER : 'integer' ;
T_NUMBER : 'number' ;
T_INTEGER_FIED : 'IntegerField';
T_NUMBER_FIED : 'IntegerField' ;
T_DATE_FIED : 'IntegerField' ;
T_DATE_TIME_FIED : 'IntegerField' ;
T_TIME_STAMP_FIELD: 'TimestampField' ;


NAME
 : ID_START ID_CONTINUE*
 ;


fragment SPACES
 : [ \t]+
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

SKIP_
 : ( SPACES | COMMENT ) -> skip
 ;

fragment COMMENT
 : '//' ~[\r\n]*
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
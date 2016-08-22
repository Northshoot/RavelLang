grammar Ravel;

tokens { INDENT, DEDENT }

@lexer::members {
   //from Ter
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
    :  modelType MODEL NAME  ':' model_suite # ModelDeclaration
    ;

modelType
    : LOCAL
    | STREAMING
    | REPLICATED
    ;

model_suite
    : NEWLINE INDENT model_block_def+ DEDENT
    ;

/** Just model particularities */
model_block_def
    : property_decl
    | schema_decl
    | NEWLINE
    ;

property_decl
    : PROPERTIES ':' NEWLINE property_block #ModelPropertyBlock
    ;

property_block
    : INDENT model_property+ DEDENT
    | NEWLINE
    ;

 model_property
    : model_property_opt '=' ( INT | TRUE | FALSE | property_expression) #ModelProperty
    ;

property_expression
    : NAME '(' NAME ')'
    ;

schema_decl
    : SCHEMA ':' NEWLINE schema_block #ModelSchemaBlock
    ;

schema_block
    : INDENT field+ DEDENT
    | NEWLINE
    ;

field
    : NAME '=' field_type '(' args* ')' #FieldDeclaration
    ;

field_type
    : T_BYTE_FIELD
    | T_STRING_FIELD
    | T_BOOLEAN_FIELD
    | T_INTEGER_FIELD
    | T_NUMBER_FIELD
    | T_DATE_FIELD
    | T_DATE_TIME_FIELD
    | T_TIME_STAMP_FIELD
    ;

/** Just controller particularities */
controller_comp
    : CONTROLLER NAME ':' cntr_suite # ControllerDeclaration
    ;

cntr_suite
    : NEWLINE INDENT cntr_block_def+ DEDENT
    ;

cntr_block_def
    : config_decl
    | event
    | NEWLINE
    ;

config_decl
    : CONFIGURATION ':' NEWLINE config_block #CntrConfigBlock
    ;

config_block
    : INDENT controller_config+ DEDENT
    | NEWLINE
    ;
controller_config
    : reference
    | var_assig
    | NEWLINE
    ;
reference
    : NAME '=' dotted_name #RefDecl
    ;
 /// dotted_name: NAME ('.' NAME)*
dotted_name
    : NAME ( '.' NAME )*
    ;
var_assig
    : primitive_type NAME '=' ( INT | TRUE | FALSE ) #VarAssig
    ;

event
    : EVENT refName '.' trigEvent '(' args'):' expr_stmt #EventDecl
    ;

refName : NAME;
trigEvent: NAME ;

args
    : arg (',' arg)*
    ;
arg
    : NAME '=' ( NAME | INT )
    ;

expr_stmt
    : NEWLINE INDENT stmt+ DEDENT
    ;

stmt
    : var_assig
    | expr_stmt
    | flow_stmt
    | del_stmt
    | string_comps_stmt
    | NEWLINE
    ;

//TODO: placeholder
flow_stmt
 //: break_stmt
 //| continue_stmt
 : return_stmt
// | raise_stmt
// | yield_stmt
 ;
del_stmt
    : DELETE recordRef
    ;
recordRef
    : recName '.' position '(' args* ')'
    ;
recName : NAME;
position
    : FIRST
    | LAST
    | GET
    ;
//TODO: placeholder for now
return_stmt
    : RETURN NAME
    ;
string_comps_stmt
    : (NAME | dotted_name) '=' string_stmt
    ;

string_stmt
    : INT
    | '"' NAME '"'
    | '+'
    ;

space_comp
    : SPACE NAME ':' space_suite # SpaceDeclaration
    ;
space_suite
    : NEWLINE INDENT space_block_def+ DEDENT
    ;

space_block_def
    : space_property_block
    | space_platform_block
    | space_models_block
    | space_controllers_block
    | space_sources_block
    | space_sinks_block
    | NEWLINE
    ;
space_property_block
    : PROPERTIES ':' NEWLINE space_properties #SpacePropertiesBlock
    ;
space_properties
    : INDENT space_property+ DEDENT
    | NEWLINE
    ;
space_property
    : spaceProp_lang '=' lang_opt #SpaceProperty
    ;
spaceProp_lang: LANGUAGE;

space_platform_block
    : PLATFORM ':' NEWLINE space_platform_dec #SpacePlatformBlock
    ;
space_platform_dec
     : INDENT space_platform+ DEDENT
     | NEWLINE
     ;
space_platform
     : templates_dir
     | api_ref
     | event_dec
     | NEWLINE
     ;
templates_dir
    : TEMPLATES '=' dir #PlatformTemplates
    ;
dir: NAME;

api_ref
    : PLATFORM '=' base '.' api_version #PlatformAPI
    ;

base: NAME;
api_version: 'api.' INT ;
event_dec
    : NAME '=' PLATFORM '.' event_ref #PlatformEvent
    ;
event_ref: NAME ;

space_models_block
    : MODELS ':' NEWLINE space_inst_block #SpaceModelsBlock
    ;
space_inst_block
    : INDENT instanciation+ DEDENT
    | NEWLINE
    ;
instanciation
    : refName '=' compName '(' args* ')' #InstansDecl
    ;
compName: 'NAME' ;

space_controllers_block
    : CONTROLLERS ':' NEWLINE space_inst_block #SpaceControllerBlock
    ;

space_sources_block
    : SOURCES ':' NEWLINE space_sources #SpaceSourceBlock
    ;
space_sources
    : INDENT instanciation+ DEDENT
    | NEWLINE
    ;
space_sinks_block
    : SINKS ':' NEWLINE space_sinks #SapceSinkBlock
    ;
space_sinks
    : INDENT instanciation+ DEDENT
    | NEWLINE
    ;



INT :   [0-9]+ ;


//NL: ('\r'? '\n' ' '*);
//WS      : [' ' \t]+ -> skip ;
/*
 * lexer rules
 */
// components
MODEL : 'model' ;
SPACE : 'space' ;
CONTROLLER: 'controller' ;
VIEW: 'view';
FLOW: 'flow' ;
//model types
LOCAL    : 'local' ;
STREAMING: 'streaming' ;
REPLICATED: 'replicated';
//blocks
PROPERTIES: 'properties' ;
//property statements
DURABLE: 'durable' ;
RELIABLE: 'reliable' ;
ENCRYPTON: 'encryption';
//configuration
CONFIGURATION: 'configuration' ;
//schema
SCHEMA: 'schema' ;

//space
PLATFORM: 'platform' ;
MODELS: 'models';
CONTROLLERS: 'controllers';
SINKS: 'sinks' ;
SOURCES: 'sources' ;
TEMPLATES:'templates';
LANGUAGE: 'language';
CLANG : 'clang';
JLANG: 'java';
PLANG: 'python';
//events commands
EVENT: 'event' ;
COMMAND:  'command' ;
RETURN : 'return' ;
DELETE: 'delete';
TRUE : 'true' ;
FALSE : 'false' ;

//local queries
LAST: 'last' ;
FIRST: 'first';
GET : 'get' ;

lang_opt
    : CLANG
    | JLANG
    | PLANG
    ;

model_property_opt
    : DURABLE
    | RELIABLE
    | ENCRYPTON
    ;

primitive_type
    : T_INTEGER
    | T_NUMBER
    | T_BOOL
    ;

T_INTEGER : 'integer' ;
T_NUMBER : 'number' ;
T_BOOL: 'boolean' ;



T_BYTE_FIELD: 'ByteField' ;
T_STRING_FIELD: 'StringField' ;
T_BOOLEAN_FIELD: 'Boolean' ;
T_INTEGER_FIELD : 'IntegerField';
T_NUMBER_FIELD : 'IntegerField' ;
T_DATE_FIELD : 'IntegerField' ;
T_DATE_TIME_FIELD : 'IntegerField' ;
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
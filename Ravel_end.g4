grammar Ravel;

tokens { INDENT, DEDENT }

@lexer::header {
  import com.yuvalshavit.antlr4.DenterHelper;
}

@lexer::members {
  private final DenterHelper denter = DenterHelper.builder()
    .nl(NL)
    .indent(RavelParser.INDENT)
    .dedent(RavelParser.DEDENT)
    .pullToken(RavelLexer.super::nextToken);

  @Override
  public Token nextToken() {
    return denter.nextToken();
  }
}

file_input
    : (modelDecl | spaceDecl)+ ;


modelDecl
    : modelType 'model' name  ':' NL* modelBody NL* end # ModelDeclaration
    ;

name: ID ;

modelType
    : 'local'
    | 'streaming'
    | 'replicated'
    ;

modelBody
    : flowDecl? NL* propDecl NL* schemaDecl NL*
    ;

//just accepting input for now
flowDecl
    : 'flow:' NL ID ( ID | '[' | ']' | '=' )* end #ModelFlow
    ;

propDecl
    : 'properties:' NL property+ end  # ModelProperties
    ;

property
    : ID '=' (ID | INT | exprs)  NL*  #PropertyDecl
    ;

exprs
    : expr (',' expr)*
    ;

expr
    : ID '(' formalParameters? ')' NL*
    ;
formalParameters
    :   formalParameter (',' formalParameter)*
    ;

formalParameter
    :    ID (',' ID)*
    ;

schemaBody
    : ( field NL*)+
    ;

schemaDecl
    : 'schema:' NL* schemaBody+ NL  end   # ModelSchema
    ;
 /*
 field declarations
 */
field_name : ID # FieldName ;
field_type : ID # FieldType ;

field
    : field_name '='  field_type '(' field_opt? ')' NL*
    ;

field_opt
    : pair (',' pair)*  # FieldOpt
    ;

spaceDecl
    : 'space' name  ':' NL* spaceBody NL* end # SpaceDeclaration
    ;

spaceBody
    : propDecl? NL* spaceConf? NL* spaceModel? NL* spaceCntr? NL* spaceSrc? NL* spaceSnk? NL*
    ;


spaceConf
    :'configuration:' NL property+ end # SpaceConfig
    ;

spaceModel
    : 'models:' NL property+ end     # SpaceModels
    ;

spaceCntr
    : 'controllers:' NL  property+ end     # SpaceControler
    ;

spaceSrc
    : 'sources:' NL  property+ end     # SpaceSource
    ;

spaceSnk
    : 'sinks:' NL  property+ end     # SpaceSink
    ;

pair : ID '=' (ID | INT)+ ;
end : 'END' NL+;


ID  :   LETTER (LETTER | CONNECTOR | [0-9])* ;

fragment
CONNECTOR : ('_' | '.' | '/') ;
fragment
LETTER : [a-zA-Z] ; //needs to be cleaned out

INT :   [0-9]+ ;


NL: ('\r'? '\n' ' '*);
WS      : [' ' \t]+ -> skip ;
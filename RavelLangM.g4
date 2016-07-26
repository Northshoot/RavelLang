grammar RavelLangM;

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
    : propDecl NL* schemaDecl NL*
    ;

propDecl
    : 'properties:' NL property+ end  # ModelProperties
    ;

property
    : ID ' = ' (ID | INT)  NL* #PropertyDecl
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
    : field_name ' = '  field_type '(' field_opt? ')' NL*
    ;

field_opt
    : pair (',' pair)*  # FieldOpt
    ;



spaceDecl
    : 'space' name  ':' NL* spaceBody NL* end # SpaceDeclaration
    ;

spaceBody
    : propDecl NL* spaceConf NL* spaceModel NL*
    ;


spaceConf
    :'configuration:' NL property+ end # SpaceConfig
    ;

spaceModel
    : 'models:' NL property+ end     # SpaceModels
    ;


/* Just temporarely hack, for now we omit options */
pair:   ID ' = ' (ID | INT);
end : 'END' ;


ID  :   LETTER (LETTER | [0-9])* ;

fragment
LETTER : [a-zA-Z_/'] ; //needs to be cleaned out

INT :   [0-9]+ ;


NL      : '\r'? '\n' ;
WS      : [ \t]+ -> skip ;


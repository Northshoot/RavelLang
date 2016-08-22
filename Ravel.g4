parser grammar Ravel;
options { tokenVocab=RavelLexer; }

file_input
    : ( NEWLINE | comp_def )* EOF
    ;

comp_def
    : model_comp
    | controller_comp
    | space_comp
    ;

model_comp
    :  modelType MODEL NAME LEFT_BRACKET RIGHTBRAKET BLOCKSTART model_suite # ModelDeclaration
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
    : PROPERTIES BLOCKSTART NEWLINE property_block #ModelPropertyBlock
    ;

property_block
    : INDENT model_property+ DEDENT
    ;

model_property
    : model_property_opt EQUAL ( INT | TRUE | FALSE | property_expression) #ModelProperty
    ;

model_property_opt
    : DURABLE
    | RELIABLE
    | ENCRYPTON
    ;
property_expression
    : NAME LEFT_BRACKET NAME RIGHT_BRACKET
    ;

schema_decl
    : SCHEMA BLOCKSTART NEWLINE schema_block #ModelSchemaBlock
    ;

schema_block
    : INDENT field+ DEDENT
    | NEWLINE
    ;

field
    : NAME EQUAL field_type LEFT_BRACKET args* RIGHT_BRACKET #FieldDeclaration
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
    : CONTROLLER NAME BLOCKSTART cntr_suite # ControllerDeclaration
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
    : CONFIGURATION BLOCKSTART NEWLINE config_block #CntrConfigBlock
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
    : NAME EQUAL dotted_name #RefDecl
    ;
 /// dotted_name: NAME (DOT NAME)*
dotted_name
    : NAME ( DOT NAME )*
    ;
var_assig
    : primitive_type NAME EQUAL ( INT | TRUE | FALSE ) #VarAssig
    ;

event
    : EVENT refName DOT trigEvent LEFT_BRACKET args RIGHT_BRACKET BLOCKSTART expr_stmt #EventDecl
    ;

refName : NAME;
trigEvent: NAME ;

args
    : arg (COMMA arg)*
    ;
arg
    : NAME EQUAL ( NAME | INT )
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
    : recName DOT position LEFT_BRACKET args* RIGHT_BRACKET
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
    : (NAME | dotted_name) EQUAL string_stmt
    ;

string_stmt
    : INT
    | DOUBLE_APPOS NAME DOUBLE_APPOS
    | PLUS
    ;

space_comp
    : SPACE NAME BLOCKSTART space_suite # SpaceDeclaration
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
    : PROPERTIES BLOCKSTART NEWLINE space_properties #SpacePropertiesBlock
    ;
space_properties
    : INDENT space_property+ DEDENT
    | NEWLINE
    ;
space_property
    : spaceProp_lang EQUAL lang_opt #SpaceProperty
    ;
spaceProp_lang: LANGUAGE;

space_platform_block
    : PLATFORM BLOCKSTART NEWLINE space_platform_dec #SpacePlatformBlock
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
    : TEMPLATES EQUAL dir #PlatformTemplates
    ;
dir: NAME;

api_ref
    : PLATFORM EQUAL base DOT api_version #PlatformAPI
    ;

base: NAME;
api_version: 'api.' INT ;
event_dec
    : NAME EQUAL PLATFORM DOT event_ref #PlatformEvent
    ;
event_ref: NAME ;

space_models_block
    : MODELS BLOCKSTART NEWLINE space_inst_block #SpaceModelsBlock
    ;
space_inst_block
    : INDENT instanciation+ DEDENT
    | NEWLINE
    ;
instanciation
    : refName EQUAL compName LEFT_BRACKET args* RIGHT_BRACKET #InstansDecl
    ;
compName: NAME ;

space_controllers_block
    : CONTROLLERS BLOCKSTART NEWLINE space_inst_block #SpaceControllerBlock
    ;

space_sources_block
    : SOURCES BLOCKSTART NEWLINE space_sources #SpaceSourceBlock
    ;
space_sources
    : INDENT instanciation+ DEDENT
    | NEWLINE
    ;
space_sinks_block
    : SINKS BLOCKSTART NEWLINE space_sinks #SapceSinkBlock
    ;
space_sinks
    : INDENT instanciation+ DEDENT
    | NEWLINE
    ;

lang_opt
    : CLANG
    | JLANG
    | PLANG
    ;


primitive_type
    : T_INTEGER
    | T_NUMBER
    | T_BOOL
    ;

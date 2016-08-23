// Generated from /Users/lauril/workspace/01-ravel/RavelLang/RavelParser.g4 by ANTLR 4.5.3
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link RavelParser}.
 */
public interface RavelParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link RavelParser#file_input}.
	 * @param ctx the parse tree
	 */
	void enterFile_input(RavelParser.File_inputContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#file_input}.
	 * @param ctx the parse tree
	 */
	void exitFile_input(RavelParser.File_inputContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#comp_def}.
	 * @param ctx the parse tree
	 */
	void enterComp_def(RavelParser.Comp_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#comp_def}.
	 * @param ctx the parse tree
	 */
	void exitComp_def(RavelParser.Comp_defContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ModelDeclaration}
	 * labeled alternative in {@link RavelParser#model_comp}.
	 * @param ctx the parse tree
	 */
	void enterModelDeclaration(RavelParser.ModelDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ModelDeclaration}
	 * labeled alternative in {@link RavelParser#model_comp}.
	 * @param ctx the parse tree
	 */
	void exitModelDeclaration(RavelParser.ModelDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#modelType}.
	 * @param ctx the parse tree
	 */
	void enterModelType(RavelParser.ModelTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#modelType}.
	 * @param ctx the parse tree
	 */
	void exitModelType(RavelParser.ModelTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#model_suite}.
	 * @param ctx the parse tree
	 */
	void enterModel_suite(RavelParser.Model_suiteContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#model_suite}.
	 * @param ctx the parse tree
	 */
	void exitModel_suite(RavelParser.Model_suiteContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#model_block_def}.
	 * @param ctx the parse tree
	 */
	void enterModel_block_def(RavelParser.Model_block_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#model_block_def}.
	 * @param ctx the parse tree
	 */
	void exitModel_block_def(RavelParser.Model_block_defContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ModelPropertyBlock}
	 * labeled alternative in {@link RavelParser#property_decl}.
	 * @param ctx the parse tree
	 */
	void enterModelPropertyBlock(RavelParser.ModelPropertyBlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ModelPropertyBlock}
	 * labeled alternative in {@link RavelParser#property_decl}.
	 * @param ctx the parse tree
	 */
	void exitModelPropertyBlock(RavelParser.ModelPropertyBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#property_block}.
	 * @param ctx the parse tree
	 */
	void enterProperty_block(RavelParser.Property_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#property_block}.
	 * @param ctx the parse tree
	 */
	void exitProperty_block(RavelParser.Property_blockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ModelProperty}
	 * labeled alternative in {@link RavelParser#model_property}.
	 * @param ctx the parse tree
	 */
	void enterModelProperty(RavelParser.ModelPropertyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ModelProperty}
	 * labeled alternative in {@link RavelParser#model_property}.
	 * @param ctx the parse tree
	 */
	void exitModelProperty(RavelParser.ModelPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#model_property_opt}.
	 * @param ctx the parse tree
	 */
	void enterModel_property_opt(RavelParser.Model_property_optContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#model_property_opt}.
	 * @param ctx the parse tree
	 */
	void exitModel_property_opt(RavelParser.Model_property_optContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#property_expression}.
	 * @param ctx the parse tree
	 */
	void enterProperty_expression(RavelParser.Property_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#property_expression}.
	 * @param ctx the parse tree
	 */
	void exitProperty_expression(RavelParser.Property_expressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ModelSchemaBlock}
	 * labeled alternative in {@link RavelParser#schema_decl}.
	 * @param ctx the parse tree
	 */
	void enterModelSchemaBlock(RavelParser.ModelSchemaBlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ModelSchemaBlock}
	 * labeled alternative in {@link RavelParser#schema_decl}.
	 * @param ctx the parse tree
	 */
	void exitModelSchemaBlock(RavelParser.ModelSchemaBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#schema_block}.
	 * @param ctx the parse tree
	 */
	void enterSchema_block(RavelParser.Schema_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#schema_block}.
	 * @param ctx the parse tree
	 */
	void exitSchema_block(RavelParser.Schema_blockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FieldDeclaration}
	 * labeled alternative in {@link RavelParser#field}.
	 * @param ctx the parse tree
	 */
	void enterFieldDeclaration(RavelParser.FieldDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FieldDeclaration}
	 * labeled alternative in {@link RavelParser#field}.
	 * @param ctx the parse tree
	 */
	void exitFieldDeclaration(RavelParser.FieldDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#field_type}.
	 * @param ctx the parse tree
	 */
	void enterField_type(RavelParser.Field_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#field_type}.
	 * @param ctx the parse tree
	 */
	void exitField_type(RavelParser.Field_typeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ControllerDeclaration}
	 * labeled alternative in {@link RavelParser#controller_comp}.
	 * @param ctx the parse tree
	 */
	void enterControllerDeclaration(RavelParser.ControllerDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ControllerDeclaration}
	 * labeled alternative in {@link RavelParser#controller_comp}.
	 * @param ctx the parse tree
	 */
	void exitControllerDeclaration(RavelParser.ControllerDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#cntr_suite}.
	 * @param ctx the parse tree
	 */
	void enterCntr_suite(RavelParser.Cntr_suiteContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#cntr_suite}.
	 * @param ctx the parse tree
	 */
	void exitCntr_suite(RavelParser.Cntr_suiteContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#cntr_block_def}.
	 * @param ctx the parse tree
	 */
	void enterCntr_block_def(RavelParser.Cntr_block_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#cntr_block_def}.
	 * @param ctx the parse tree
	 */
	void exitCntr_block_def(RavelParser.Cntr_block_defContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CntrConfigBlock}
	 * labeled alternative in {@link RavelParser#config_decl}.
	 * @param ctx the parse tree
	 */
	void enterCntrConfigBlock(RavelParser.CntrConfigBlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CntrConfigBlock}
	 * labeled alternative in {@link RavelParser#config_decl}.
	 * @param ctx the parse tree
	 */
	void exitCntrConfigBlock(RavelParser.CntrConfigBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#config_block}.
	 * @param ctx the parse tree
	 */
	void enterConfig_block(RavelParser.Config_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#config_block}.
	 * @param ctx the parse tree
	 */
	void exitConfig_block(RavelParser.Config_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#controller_config}.
	 * @param ctx the parse tree
	 */
	void enterController_config(RavelParser.Controller_configContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#controller_config}.
	 * @param ctx the parse tree
	 */
	void exitController_config(RavelParser.Controller_configContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RefDecl}
	 * labeled alternative in {@link RavelParser#reference}.
	 * @param ctx the parse tree
	 */
	void enterRefDecl(RavelParser.RefDeclContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RefDecl}
	 * labeled alternative in {@link RavelParser#reference}.
	 * @param ctx the parse tree
	 */
	void exitRefDecl(RavelParser.RefDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#dotted_name}.
	 * @param ctx the parse tree
	 */
	void enterDotted_name(RavelParser.Dotted_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#dotted_name}.
	 * @param ctx the parse tree
	 */
	void exitDotted_name(RavelParser.Dotted_nameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VarAssig}
	 * labeled alternative in {@link RavelParser#var_assig}.
	 * @param ctx the parse tree
	 */
	void enterVarAssig(RavelParser.VarAssigContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VarAssig}
	 * labeled alternative in {@link RavelParser#var_assig}.
	 * @param ctx the parse tree
	 */
	void exitVarAssig(RavelParser.VarAssigContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EventDecl}
	 * labeled alternative in {@link RavelParser#event}.
	 * @param ctx the parse tree
	 */
	void enterEventDecl(RavelParser.EventDeclContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EventDecl}
	 * labeled alternative in {@link RavelParser#event}.
	 * @param ctx the parse tree
	 */
	void exitEventDecl(RavelParser.EventDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#refName}.
	 * @param ctx the parse tree
	 */
	void enterRefName(RavelParser.RefNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#refName}.
	 * @param ctx the parse tree
	 */
	void exitRefName(RavelParser.RefNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#trigEvent}.
	 * @param ctx the parse tree
	 */
	void enterTrigEvent(RavelParser.TrigEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#trigEvent}.
	 * @param ctx the parse tree
	 */
	void exitTrigEvent(RavelParser.TrigEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#args}.
	 * @param ctx the parse tree
	 */
	void enterArgs(RavelParser.ArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#args}.
	 * @param ctx the parse tree
	 */
	void exitArgs(RavelParser.ArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterArg(RavelParser.ArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitArg(RavelParser.ArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#expr_stmt}.
	 * @param ctx the parse tree
	 */
	void enterExpr_stmt(RavelParser.Expr_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#expr_stmt}.
	 * @param ctx the parse tree
	 */
	void exitExpr_stmt(RavelParser.Expr_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt(RavelParser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt(RavelParser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#flow_stmt}.
	 * @param ctx the parse tree
	 */
	void enterFlow_stmt(RavelParser.Flow_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#flow_stmt}.
	 * @param ctx the parse tree
	 */
	void exitFlow_stmt(RavelParser.Flow_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#del_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDel_stmt(RavelParser.Del_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#del_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDel_stmt(RavelParser.Del_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#recordRef}.
	 * @param ctx the parse tree
	 */
	void enterRecordRef(RavelParser.RecordRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#recordRef}.
	 * @param ctx the parse tree
	 */
	void exitRecordRef(RavelParser.RecordRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#recName}.
	 * @param ctx the parse tree
	 */
	void enterRecName(RavelParser.RecNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#recName}.
	 * @param ctx the parse tree
	 */
	void exitRecName(RavelParser.RecNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#position}.
	 * @param ctx the parse tree
	 */
	void enterPosition(RavelParser.PositionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#position}.
	 * @param ctx the parse tree
	 */
	void exitPosition(RavelParser.PositionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#return_stmt}.
	 * @param ctx the parse tree
	 */
	void enterReturn_stmt(RavelParser.Return_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#return_stmt}.
	 * @param ctx the parse tree
	 */
	void exitReturn_stmt(RavelParser.Return_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#string_comps_stmt}.
	 * @param ctx the parse tree
	 */
	void enterString_comps_stmt(RavelParser.String_comps_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#string_comps_stmt}.
	 * @param ctx the parse tree
	 */
	void exitString_comps_stmt(RavelParser.String_comps_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#string_stmt}.
	 * @param ctx the parse tree
	 */
	void enterString_stmt(RavelParser.String_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#string_stmt}.
	 * @param ctx the parse tree
	 */
	void exitString_stmt(RavelParser.String_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SpaceDeclaration}
	 * labeled alternative in {@link RavelParser#space_comp}.
	 * @param ctx the parse tree
	 */
	void enterSpaceDeclaration(RavelParser.SpaceDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SpaceDeclaration}
	 * labeled alternative in {@link RavelParser#space_comp}.
	 * @param ctx the parse tree
	 */
	void exitSpaceDeclaration(RavelParser.SpaceDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#space_suite}.
	 * @param ctx the parse tree
	 */
	void enterSpace_suite(RavelParser.Space_suiteContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#space_suite}.
	 * @param ctx the parse tree
	 */
	void exitSpace_suite(RavelParser.Space_suiteContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#space_block_def}.
	 * @param ctx the parse tree
	 */
	void enterSpace_block_def(RavelParser.Space_block_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#space_block_def}.
	 * @param ctx the parse tree
	 */
	void exitSpace_block_def(RavelParser.Space_block_defContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SpacePropertiesBlock}
	 * labeled alternative in {@link RavelParser#space_property_block}.
	 * @param ctx the parse tree
	 */
	void enterSpacePropertiesBlock(RavelParser.SpacePropertiesBlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SpacePropertiesBlock}
	 * labeled alternative in {@link RavelParser#space_property_block}.
	 * @param ctx the parse tree
	 */
	void exitSpacePropertiesBlock(RavelParser.SpacePropertiesBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#space_properties}.
	 * @param ctx the parse tree
	 */
	void enterSpace_properties(RavelParser.Space_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#space_properties}.
	 * @param ctx the parse tree
	 */
	void exitSpace_properties(RavelParser.Space_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SpaceProperty}
	 * labeled alternative in {@link RavelParser#space_property}.
	 * @param ctx the parse tree
	 */
	void enterSpaceProperty(RavelParser.SpacePropertyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SpaceProperty}
	 * labeled alternative in {@link RavelParser#space_property}.
	 * @param ctx the parse tree
	 */
	void exitSpaceProperty(RavelParser.SpacePropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#spaceProp_lang}.
	 * @param ctx the parse tree
	 */
	void enterSpaceProp_lang(RavelParser.SpaceProp_langContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#spaceProp_lang}.
	 * @param ctx the parse tree
	 */
	void exitSpaceProp_lang(RavelParser.SpaceProp_langContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SpacePlatformBlock}
	 * labeled alternative in {@link RavelParser#space_platform_block}.
	 * @param ctx the parse tree
	 */
	void enterSpacePlatformBlock(RavelParser.SpacePlatformBlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SpacePlatformBlock}
	 * labeled alternative in {@link RavelParser#space_platform_block}.
	 * @param ctx the parse tree
	 */
	void exitSpacePlatformBlock(RavelParser.SpacePlatformBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#space_platform_dec}.
	 * @param ctx the parse tree
	 */
	void enterSpace_platform_dec(RavelParser.Space_platform_decContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#space_platform_dec}.
	 * @param ctx the parse tree
	 */
	void exitSpace_platform_dec(RavelParser.Space_platform_decContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#space_platform}.
	 * @param ctx the parse tree
	 */
	void enterSpace_platform(RavelParser.Space_platformContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#space_platform}.
	 * @param ctx the parse tree
	 */
	void exitSpace_platform(RavelParser.Space_platformContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PlatformTemplates}
	 * labeled alternative in {@link RavelParser#templates_dir}.
	 * @param ctx the parse tree
	 */
	void enterPlatformTemplates(RavelParser.PlatformTemplatesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PlatformTemplates}
	 * labeled alternative in {@link RavelParser#templates_dir}.
	 * @param ctx the parse tree
	 */
	void exitPlatformTemplates(RavelParser.PlatformTemplatesContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#dir}.
	 * @param ctx the parse tree
	 */
	void enterDir(RavelParser.DirContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#dir}.
	 * @param ctx the parse tree
	 */
	void exitDir(RavelParser.DirContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PlatformAPI}
	 * labeled alternative in {@link RavelParser#api_ref}.
	 * @param ctx the parse tree
	 */
	void enterPlatformAPI(RavelParser.PlatformAPIContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PlatformAPI}
	 * labeled alternative in {@link RavelParser#api_ref}.
	 * @param ctx the parse tree
	 */
	void exitPlatformAPI(RavelParser.PlatformAPIContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#base}.
	 * @param ctx the parse tree
	 */
	void enterBase(RavelParser.BaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#base}.
	 * @param ctx the parse tree
	 */
	void exitBase(RavelParser.BaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#api_version}.
	 * @param ctx the parse tree
	 */
	void enterApi_version(RavelParser.Api_versionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#api_version}.
	 * @param ctx the parse tree
	 */
	void exitApi_version(RavelParser.Api_versionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PlatformEvent}
	 * labeled alternative in {@link RavelParser#event_dec}.
	 * @param ctx the parse tree
	 */
	void enterPlatformEvent(RavelParser.PlatformEventContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PlatformEvent}
	 * labeled alternative in {@link RavelParser#event_dec}.
	 * @param ctx the parse tree
	 */
	void exitPlatformEvent(RavelParser.PlatformEventContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#event_ref}.
	 * @param ctx the parse tree
	 */
	void enterEvent_ref(RavelParser.Event_refContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#event_ref}.
	 * @param ctx the parse tree
	 */
	void exitEvent_ref(RavelParser.Event_refContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SpaceModelsBlock}
	 * labeled alternative in {@link RavelParser#space_models_block}.
	 * @param ctx the parse tree
	 */
	void enterSpaceModelsBlock(RavelParser.SpaceModelsBlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SpaceModelsBlock}
	 * labeled alternative in {@link RavelParser#space_models_block}.
	 * @param ctx the parse tree
	 */
	void exitSpaceModelsBlock(RavelParser.SpaceModelsBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#space_inst_block}.
	 * @param ctx the parse tree
	 */
	void enterSpace_inst_block(RavelParser.Space_inst_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#space_inst_block}.
	 * @param ctx the parse tree
	 */
	void exitSpace_inst_block(RavelParser.Space_inst_blockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InstansDecl}
	 * labeled alternative in {@link RavelParser#instanciation}.
	 * @param ctx the parse tree
	 */
	void enterInstansDecl(RavelParser.InstansDeclContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InstansDecl}
	 * labeled alternative in {@link RavelParser#instanciation}.
	 * @param ctx the parse tree
	 */
	void exitInstansDecl(RavelParser.InstansDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#compName}.
	 * @param ctx the parse tree
	 */
	void enterCompName(RavelParser.CompNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#compName}.
	 * @param ctx the parse tree
	 */
	void exitCompName(RavelParser.CompNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SpaceControllerBlock}
	 * labeled alternative in {@link RavelParser#space_controllers_block}.
	 * @param ctx the parse tree
	 */
	void enterSpaceControllerBlock(RavelParser.SpaceControllerBlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SpaceControllerBlock}
	 * labeled alternative in {@link RavelParser#space_controllers_block}.
	 * @param ctx the parse tree
	 */
	void exitSpaceControllerBlock(RavelParser.SpaceControllerBlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SpaceSourceBlock}
	 * labeled alternative in {@link RavelParser#space_sources_block}.
	 * @param ctx the parse tree
	 */
	void enterSpaceSourceBlock(RavelParser.SpaceSourceBlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SpaceSourceBlock}
	 * labeled alternative in {@link RavelParser#space_sources_block}.
	 * @param ctx the parse tree
	 */
	void exitSpaceSourceBlock(RavelParser.SpaceSourceBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#space_sources}.
	 * @param ctx the parse tree
	 */
	void enterSpace_sources(RavelParser.Space_sourcesContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#space_sources}.
	 * @param ctx the parse tree
	 */
	void exitSpace_sources(RavelParser.Space_sourcesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SapceSinkBlock}
	 * labeled alternative in {@link RavelParser#space_sinks_block}.
	 * @param ctx the parse tree
	 */
	void enterSapceSinkBlock(RavelParser.SapceSinkBlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SapceSinkBlock}
	 * labeled alternative in {@link RavelParser#space_sinks_block}.
	 * @param ctx the parse tree
	 */
	void exitSapceSinkBlock(RavelParser.SapceSinkBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#space_sinks}.
	 * @param ctx the parse tree
	 */
	void enterSpace_sinks(RavelParser.Space_sinksContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#space_sinks}.
	 * @param ctx the parse tree
	 */
	void exitSpace_sinks(RavelParser.Space_sinksContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#lang_opt}.
	 * @param ctx the parse tree
	 */
	void enterLang_opt(RavelParser.Lang_optContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#lang_opt}.
	 * @param ctx the parse tree
	 */
	void exitLang_opt(RavelParser.Lang_optContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#primitive_type}.
	 * @param ctx the parse tree
	 */
	void enterPrimitive_type(RavelParser.Primitive_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#primitive_type}.
	 * @param ctx the parse tree
	 */
	void exitPrimitive_type(RavelParser.Primitive_typeContext ctx);
}
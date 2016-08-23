// Generated from /Users/lauril/workspace/01-ravel/RavelLang/RavelParser.g4 by ANTLR 4.5.3
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link RavelParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface RavelParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link RavelParser#file_input}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile_input(RavelParser.File_inputContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#comp_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComp_def(RavelParser.Comp_defContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ModelDeclaration}
	 * labeled alternative in {@link RavelParser#model_comp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelDeclaration(RavelParser.ModelDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#modelType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelType(RavelParser.ModelTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#model_suite}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_suite(RavelParser.Model_suiteContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#model_block_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_block_def(RavelParser.Model_block_defContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ModelPropertyBlock}
	 * labeled alternative in {@link RavelParser#property_decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelPropertyBlock(RavelParser.ModelPropertyBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#property_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty_block(RavelParser.Property_blockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ModelProperty}
	 * labeled alternative in {@link RavelParser#model_property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelProperty(RavelParser.ModelPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#model_property_opt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_property_opt(RavelParser.Model_property_optContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#property_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty_expression(RavelParser.Property_expressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ModelSchemaBlock}
	 * labeled alternative in {@link RavelParser#schema_decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelSchemaBlock(RavelParser.ModelSchemaBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#schema_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchema_block(RavelParser.Schema_blockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FieldDeclaration}
	 * labeled alternative in {@link RavelParser#field}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldDeclaration(RavelParser.FieldDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#field_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitField_type(RavelParser.Field_typeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ControllerDeclaration}
	 * labeled alternative in {@link RavelParser#controller_comp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControllerDeclaration(RavelParser.ControllerDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#cntr_suite}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCntr_suite(RavelParser.Cntr_suiteContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#cntr_block_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCntr_block_def(RavelParser.Cntr_block_defContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CntrConfigBlock}
	 * labeled alternative in {@link RavelParser#config_decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCntrConfigBlock(RavelParser.CntrConfigBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#config_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConfig_block(RavelParser.Config_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#controller_config}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitController_config(RavelParser.Controller_configContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RefDecl}
	 * labeled alternative in {@link RavelParser#reference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefDecl(RavelParser.RefDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#dotted_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotted_name(RavelParser.Dotted_nameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VarAssig}
	 * labeled alternative in {@link RavelParser#var_assig}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarAssig(RavelParser.VarAssigContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EventDecl}
	 * labeled alternative in {@link RavelParser#event}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEventDecl(RavelParser.EventDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#refName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefName(RavelParser.RefNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#trigEvent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrigEvent(RavelParser.TrigEventContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#args}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgs(RavelParser.ArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg(RavelParser.ArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#expr_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_stmt(RavelParser.Expr_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt(RavelParser.StmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#flow_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlow_stmt(RavelParser.Flow_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#del_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDel_stmt(RavelParser.Del_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#recordRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecordRef(RavelParser.RecordRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#recName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecName(RavelParser.RecNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#position}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPosition(RavelParser.PositionContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#return_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_stmt(RavelParser.Return_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#string_comps_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_comps_stmt(RavelParser.String_comps_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#string_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_stmt(RavelParser.String_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SpaceDeclaration}
	 * labeled alternative in {@link RavelParser#space_comp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceDeclaration(RavelParser.SpaceDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#space_suite}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpace_suite(RavelParser.Space_suiteContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#space_block_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpace_block_def(RavelParser.Space_block_defContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SpacePropertiesBlock}
	 * labeled alternative in {@link RavelParser#space_property_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpacePropertiesBlock(RavelParser.SpacePropertiesBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#space_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpace_properties(RavelParser.Space_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SpaceProperty}
	 * labeled alternative in {@link RavelParser#space_property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceProperty(RavelParser.SpacePropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#spaceProp_lang}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceProp_lang(RavelParser.SpaceProp_langContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SpacePlatformBlock}
	 * labeled alternative in {@link RavelParser#space_platform_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpacePlatformBlock(RavelParser.SpacePlatformBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#space_platform_dec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpace_platform_dec(RavelParser.Space_platform_decContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#space_platform}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpace_platform(RavelParser.Space_platformContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PlatformTemplates}
	 * labeled alternative in {@link RavelParser#templates_dir}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlatformTemplates(RavelParser.PlatformTemplatesContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#dir}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDir(RavelParser.DirContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PlatformAPI}
	 * labeled alternative in {@link RavelParser#api_ref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlatformAPI(RavelParser.PlatformAPIContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#base}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBase(RavelParser.BaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#api_version}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitApi_version(RavelParser.Api_versionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PlatformEvent}
	 * labeled alternative in {@link RavelParser#event_dec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlatformEvent(RavelParser.PlatformEventContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#event_ref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent_ref(RavelParser.Event_refContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SpaceModelsBlock}
	 * labeled alternative in {@link RavelParser#space_models_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceModelsBlock(RavelParser.SpaceModelsBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#space_inst_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpace_inst_block(RavelParser.Space_inst_blockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InstansDecl}
	 * labeled alternative in {@link RavelParser#instanciation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstansDecl(RavelParser.InstansDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#compName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompName(RavelParser.CompNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SpaceControllerBlock}
	 * labeled alternative in {@link RavelParser#space_controllers_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceControllerBlock(RavelParser.SpaceControllerBlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SpaceSourceBlock}
	 * labeled alternative in {@link RavelParser#space_sources_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceSourceBlock(RavelParser.SpaceSourceBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#space_sources}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpace_sources(RavelParser.Space_sourcesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SapceSinkBlock}
	 * labeled alternative in {@link RavelParser#space_sinks_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSapceSinkBlock(RavelParser.SapceSinkBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#space_sinks}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpace_sinks(RavelParser.Space_sinksContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#lang_opt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLang_opt(RavelParser.Lang_optContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#primitive_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitive_type(RavelParser.Primitive_typeContext ctx);
}
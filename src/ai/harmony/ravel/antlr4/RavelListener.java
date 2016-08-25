// Generated from /Users/lauril/workspace/01-ravel/RavelLang/Ravel.g4 by ANTLR 4.5.3
package ai.harmony.ravel.antlr4;

import ai.harmony.ravel.compiler.scope.*;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link RavelParser}.
 */
public interface RavelListener extends ParseTreeListener {
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
	 * Enter a parse tree produced by {@link RavelParser#parameters}.
	 * @param ctx the parse tree
	 */
	void enterParameters(RavelParser.ParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#parameters}.
	 * @param ctx the parse tree
	 */
	void exitParameters(RavelParser.ParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#typedargslist}.
	 * @param ctx the parse tree
	 */
	void enterTypedargslist(RavelParser.TypedargslistContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#typedargslist}.
	 * @param ctx the parse tree
	 */
	void exitTypedargslist(RavelParser.TypedargslistContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(RavelParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(RavelParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#tdefvar}.
	 * @param ctx the parse tree
	 */
	void enterTdefvar(RavelParser.TdefvarContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#tdefvar}.
	 * @param ctx the parse tree
	 */
	void exitTdefvar(RavelParser.TdefvarContext ctx);
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
	 * Enter a parse tree produced by the {@code SpaceScope}
	 * labeled alternative in {@link RavelParser#space_comp}.
	 * @param ctx the parse tree
	 */
	void enterSpaceScope(RavelParser.SpaceScopeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SpaceScope}
	 * labeled alternative in {@link RavelParser#space_comp}.
	 * @param ctx the parse tree
	 */
	void exitSpaceScope(RavelParser.SpaceScopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#space_body}.
	 * @param ctx the parse tree
	 */
	void enterSpace_body(RavelParser.Space_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#space_body}.
	 * @param ctx the parse tree
	 */
	void exitSpace_body(RavelParser.Space_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#space_block}.
	 * @param ctx the parse tree
	 */
	void enterSpace_block(RavelParser.Space_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#space_block}.
	 * @param ctx the parse tree
	 */
	void exitSpace_block(RavelParser.Space_blockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PlatformScope}
	 * labeled alternative in {@link RavelParser#platform_scope}.
	 * @param ctx the parse tree
	 */
	void enterPlatformScope(RavelParser.PlatformScopeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PlatformScope}
	 * labeled alternative in {@link RavelParser#platform_scope}.
	 * @param ctx the parse tree
	 */
	void exitPlatformScope(RavelParser.PlatformScopeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ModelInstanciation}
	 * labeled alternative in {@link RavelParser#models_scope}.
	 * @param ctx the parse tree
	 */
	void enterModelInstanciation(RavelParser.ModelInstanciationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ModelInstanciation}
	 * labeled alternative in {@link RavelParser#models_scope}.
	 * @param ctx the parse tree
	 */
	void exitModelInstanciation(RavelParser.ModelInstanciationContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#instantiations}.
	 * @param ctx the parse tree
	 */
	void enterInstantiations(RavelParser.InstantiationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#instantiations}.
	 * @param ctx the parse tree
	 */
	void exitInstantiations(RavelParser.InstantiationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#instance}.
	 * @param ctx the parse tree
	 */
	void enterInstance(RavelParser.InstanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#instance}.
	 * @param ctx the parse tree
	 */
	void exitInstance(RavelParser.InstanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#instance_name}.
	 * @param ctx the parse tree
	 */
	void enterInstance_name(RavelParser.Instance_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#instance_name}.
	 * @param ctx the parse tree
	 */
	void exitInstance_name(RavelParser.Instance_nameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ControllerInstanciation}
	 * labeled alternative in {@link RavelParser#controllers_scope}.
	 * @param ctx the parse tree
	 */
	void enterControllerInstanciation(RavelParser.ControllerInstanciationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ControllerInstanciation}
	 * labeled alternative in {@link RavelParser#controllers_scope}.
	 * @param ctx the parse tree
	 */
	void exitControllerInstanciation(RavelParser.ControllerInstanciationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SinkLinks}
	 * labeled alternative in {@link RavelParser#sink_scope}.
	 * @param ctx the parse tree
	 */
	void enterSinkLinks(RavelParser.SinkLinksContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SinkLinks}
	 * labeled alternative in {@link RavelParser#sink_scope}.
	 * @param ctx the parse tree
	 */
	void exitSinkLinks(RavelParser.SinkLinksContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#references}.
	 * @param ctx the parse tree
	 */
	void enterReferences(RavelParser.ReferencesContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#references}.
	 * @param ctx the parse tree
	 */
	void exitReferences(RavelParser.ReferencesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SourceLinks}
	 * labeled alternative in {@link RavelParser#source_scope}.
	 * @param ctx the parse tree
	 */
	void enterSourceLinks(RavelParser.SourceLinksContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SourceLinks}
	 * labeled alternative in {@link RavelParser#source_scope}.
	 * @param ctx the parse tree
	 */
	void exitSourceLinks(RavelParser.SourceLinksContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ModelScope}
	 * labeled alternative in {@link RavelParser#model_comp}.
	 * @param ctx the parse tree
	 */
	void enterModelScope(RavelParser.ModelScopeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ModelScope}
	 * labeled alternative in {@link RavelParser#model_comp}.
	 * @param ctx the parse tree
	 */
	void exitModelScope(RavelParser.ModelScopeContext ctx);
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
	 * Enter a parse tree produced by {@link RavelParser#model_body}.
	 * @param ctx the parse tree
	 */
	void enterModel_body(RavelParser.Model_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#model_body}.
	 * @param ctx the parse tree
	 */
	void exitModel_body(RavelParser.Model_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#model_block}.
	 * @param ctx the parse tree
	 */
	void enterModel_block(RavelParser.Model_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#model_block}.
	 * @param ctx the parse tree
	 */
	void exitModel_block(RavelParser.Model_blockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PropertiesScope}
	 * labeled alternative in {@link RavelParser#properties_block}.
	 * @param ctx the parse tree
	 */
	void enterPropertiesScope(RavelParser.PropertiesScopeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PropertiesScope}
	 * labeled alternative in {@link RavelParser#properties_block}.
	 * @param ctx the parse tree
	 */
	void exitPropertiesScope(RavelParser.PropertiesScopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#properties}.
	 * @param ctx the parse tree
	 */
	void enterProperties(RavelParser.PropertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#properties}.
	 * @param ctx the parse tree
	 */
	void exitProperties(RavelParser.PropertiesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VarAssigment}
	 * labeled alternative in {@link RavelParser#tdefvar_assig}.
	 * @param ctx the parse tree
	 */
	void enterVarAssigment(RavelParser.VarAssigmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VarAssigment}
	 * labeled alternative in {@link RavelParser#tdefvar_assig}.
	 * @param ctx the parse tree
	 */
	void exitVarAssigment(RavelParser.VarAssigmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SchemaScope}
	 * labeled alternative in {@link RavelParser#schema_block}.
	 * @param ctx the parse tree
	 */
	void enterSchemaScope(RavelParser.SchemaScopeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SchemaScope}
	 * labeled alternative in {@link RavelParser#schema_block}.
	 * @param ctx the parse tree
	 */
	void exitSchemaScope(RavelParser.SchemaScopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#fields}.
	 * @param ctx the parse tree
	 */
	void enterFields(RavelParser.FieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#fields}.
	 * @param ctx the parse tree
	 */
	void exitFields(RavelParser.FieldsContext ctx);
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
	 * Enter a parse tree produced by {@link RavelParser#model_field_identifier}.
	 * @param ctx the parse tree
	 */
	void enterModel_field_identifier(RavelParser.Model_field_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#model_field_identifier}.
	 * @param ctx the parse tree
	 */
	void exitModel_field_identifier(RavelParser.Model_field_identifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ControllerScope}
	 * labeled alternative in {@link RavelParser#controller_comp}.
	 * @param ctx the parse tree
	 */
	void enterControllerScope(RavelParser.ControllerScopeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ControllerScope}
	 * labeled alternative in {@link RavelParser#controller_comp}.
	 * @param ctx the parse tree
	 */
	void exitControllerScope(RavelParser.ControllerScopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#controller_scope}.
	 * @param ctx the parse tree
	 */
	void enterController_scope(RavelParser.Controller_scopeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#controller_scope}.
	 * @param ctx the parse tree
	 */
	void exitController_scope(RavelParser.Controller_scopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#controller_body}.
	 * @param ctx the parse tree
	 */
	void enterController_body(RavelParser.Controller_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#controller_body}.
	 * @param ctx the parse tree
	 */
	void exitController_body(RavelParser.Controller_bodyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ReferenceAssigment}
	 * labeled alternative in {@link RavelParser#ref_assig}.
	 * @param ctx the parse tree
	 */
	void enterReferenceAssigment(RavelParser.ReferenceAssigmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ReferenceAssigment}
	 * labeled alternative in {@link RavelParser#ref_assig}.
	 * @param ctx the parse tree
	 */
	void exitReferenceAssigment(RavelParser.ReferenceAssigmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EventScope}
	 * labeled alternative in {@link RavelParser#eventdef}.
	 * @param ctx the parse tree
	 */
	void enterEventScope(RavelParser.EventScopeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EventScope}
	 * labeled alternative in {@link RavelParser#eventdef}.
	 * @param ctx the parse tree
	 */
	void exitEventScope(RavelParser.EventScopeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#function_args}.
	 * @param ctx the parse tree
	 */
	void enterFunction_args(RavelParser.Function_argsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#function_args}.
	 * @param ctx the parse tree
	 */
	void exitFunction_args(RavelParser.Function_argsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#function_param}.
	 * @param ctx the parse tree
	 */
	void enterFunction_param(RavelParser.Function_paramContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#function_param}.
	 * @param ctx the parse tree
	 */
	void exitFunction_param(RavelParser.Function_paramContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#param_prair}.
	 * @param ctx the parse tree
	 */
	void enterParam_prair(RavelParser.Param_prairContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#param_prair}.
	 * @param ctx the parse tree
	 */
	void exitParam_prair(RavelParser.Param_prairContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#param_type}.
	 * @param ctx the parse tree
	 */
	void enterParam_type(RavelParser.Param_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#param_type}.
	 * @param ctx the parse tree
	 */
	void exitParam_type(RavelParser.Param_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#param_name}.
	 * @param ctx the parse tree
	 */
	void enterParam_name(RavelParser.Param_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#param_name}.
	 * @param ctx the parse tree
	 */
	void exitParam_name(RavelParser.Param_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#suite}.
	 * @param ctx the parse tree
	 */
	void enterSuite(RavelParser.SuiteContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#suite}.
	 * @param ctx the parse tree
	 */
	void exitSuite(RavelParser.SuiteContext ctx);
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
	 * Enter a parse tree produced by {@link RavelParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSmall_stmt(RavelParser.Small_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#small_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSmall_stmt(RavelParser.Small_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#model_field_assig}.
	 * @param ctx the parse tree
	 */
	void enterModel_field_assig(RavelParser.Model_field_assigContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#model_field_assig}.
	 * @param ctx the parse tree
	 */
	void exitModel_field_assig(RavelParser.Model_field_assigContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#model_assig_expr}.
	 * @param ctx the parse tree
	 */
	void enterModel_assig_expr(RavelParser.Model_assig_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#model_assig_expr}.
	 * @param ctx the parse tree
	 */
	void exitModel_assig_expr(RavelParser.Model_assig_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCompound_stmt(RavelParser.Compound_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#compound_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCompound_stmt(RavelParser.Compound_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#assert_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAssert_stmt(RavelParser.Assert_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#assert_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAssert_stmt(RavelParser.Assert_stmtContext ctx);
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
	 * Enter a parse tree produced by {@link RavelParser#function_call}.
	 * @param ctx the parse tree
	 */
	void enterFunction_call(RavelParser.Function_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#function_call}.
	 * @param ctx the parse tree
	 */
	void exitFunction_call(RavelParser.Function_callContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#query_expr}.
	 * @param ctx the parse tree
	 */
	void enterQuery_expr(RavelParser.Query_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#query_expr}.
	 * @param ctx the parse tree
	 */
	void exitQuery_expr(RavelParser.Query_exprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code QueryOperations}
	 * labeled alternative in {@link RavelParser#local_query}.
	 * @param ctx the parse tree
	 */
	void enterQueryOperations(RavelParser.QueryOperationsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code QueryOperations}
	 * labeled alternative in {@link RavelParser#local_query}.
	 * @param ctx the parse tree
	 */
	void exitQueryOperations(RavelParser.QueryOperationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#query_operation}.
	 * @param ctx the parse tree
	 */
	void enterQuery_operation(RavelParser.Query_operationContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#query_operation}.
	 * @param ctx the parse tree
	 */
	void exitQuery_operation(RavelParser.Query_operationContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#db_query}.
	 * @param ctx the parse tree
	 */
	void enterDb_query(RavelParser.Db_queryContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#db_query}.
	 * @param ctx the parse tree
	 */
	void exitDb_query(RavelParser.Db_queryContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#funct_expr}.
	 * @param ctx the parse tree
	 */
	void enterFunct_expr(RavelParser.Funct_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#funct_expr}.
	 * @param ctx the parse tree
	 */
	void exitFunct_expr(RavelParser.Funct_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#func_no_return}.
	 * @param ctx the parse tree
	 */
	void enterFunc_no_return(RavelParser.Func_no_returnContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#func_no_return}.
	 * @param ctx the parse tree
	 */
	void exitFunc_no_return(RavelParser.Func_no_returnContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#func_with_return}.
	 * @param ctx the parse tree
	 */
	void enterFunc_with_return(RavelParser.Func_with_returnContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#func_with_return}.
	 * @param ctx the parse tree
	 */
	void exitFunc_with_return(RavelParser.Func_with_returnContext ctx);
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
	 * Enter a parse tree produced by {@link RavelParser#pass_stmt}.
	 * @param ctx the parse tree
	 */
	void enterPass_stmt(RavelParser.Pass_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#pass_stmt}.
	 * @param ctx the parse tree
	 */
	void exitPass_stmt(RavelParser.Pass_stmtContext ctx);
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
	 * Enter a parse tree produced by {@link RavelParser#break_stmt}.
	 * @param ctx the parse tree
	 */
	void enterBreak_stmt(RavelParser.Break_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#break_stmt}.
	 * @param ctx the parse tree
	 */
	void exitBreak_stmt(RavelParser.Break_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#continue_stmt}.
	 * @param ctx the parse tree
	 */
	void enterContinue_stmt(RavelParser.Continue_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#continue_stmt}.
	 * @param ctx the parse tree
	 */
	void exitContinue_stmt(RavelParser.Continue_stmtContext ctx);
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
	 * Enter a parse tree produced by {@link RavelParser#if_stmt}.
	 * @param ctx the parse tree
	 */
	void enterIf_stmt(RavelParser.If_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#if_stmt}.
	 * @param ctx the parse tree
	 */
	void exitIf_stmt(RavelParser.If_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#while_stmt}.
	 * @param ctx the parse tree
	 */
	void enterWhile_stmt(RavelParser.While_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#while_stmt}.
	 * @param ctx the parse tree
	 */
	void exitWhile_stmt(RavelParser.While_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#for_stmt}.
	 * @param ctx the parse tree
	 */
	void enterFor_stmt(RavelParser.For_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#for_stmt}.
	 * @param ctx the parse tree
	 */
	void exitFor_stmt(RavelParser.For_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#test}.
	 * @param ctx the parse tree
	 */
	void enterTest(RavelParser.TestContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#test}.
	 * @param ctx the parse tree
	 */
	void exitTest(RavelParser.TestContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#or_test}.
	 * @param ctx the parse tree
	 */
	void enterOr_test(RavelParser.Or_testContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#or_test}.
	 * @param ctx the parse tree
	 */
	void exitOr_test(RavelParser.Or_testContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#and_test}.
	 * @param ctx the parse tree
	 */
	void enterAnd_test(RavelParser.And_testContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#and_test}.
	 * @param ctx the parse tree
	 */
	void exitAnd_test(RavelParser.And_testContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#not_test}.
	 * @param ctx the parse tree
	 */
	void enterNot_test(RavelParser.Not_testContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#not_test}.
	 * @param ctx the parse tree
	 */
	void exitNot_test(RavelParser.Not_testContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#comparison}.
	 * @param ctx the parse tree
	 */
	void enterComparison(RavelParser.ComparisonContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#comparison}.
	 * @param ctx the parse tree
	 */
	void exitComparison(RavelParser.ComparisonContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#comp_op}.
	 * @param ctx the parse tree
	 */
	void enterComp_op(RavelParser.Comp_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#comp_op}.
	 * @param ctx the parse tree
	 */
	void exitComp_op(RavelParser.Comp_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(RavelParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(RavelParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#xor_expr}.
	 * @param ctx the parse tree
	 */
	void enterXor_expr(RavelParser.Xor_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#xor_expr}.
	 * @param ctx the parse tree
	 */
	void exitXor_expr(RavelParser.Xor_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#and_expr}.
	 * @param ctx the parse tree
	 */
	void enterAnd_expr(RavelParser.And_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#and_expr}.
	 * @param ctx the parse tree
	 */
	void exitAnd_expr(RavelParser.And_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#shift_expr}.
	 * @param ctx the parse tree
	 */
	void enterShift_expr(RavelParser.Shift_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#shift_expr}.
	 * @param ctx the parse tree
	 */
	void exitShift_expr(RavelParser.Shift_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void enterArith_expr(RavelParser.Arith_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void exitArith_expr(RavelParser.Arith_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(RavelParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(RavelParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(RavelParser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(RavelParser.FactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#power}.
	 * @param ctx the parse tree
	 */
	void enterPower(RavelParser.PowerContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#power}.
	 * @param ctx the parse tree
	 */
	void exitPower(RavelParser.PowerContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(RavelParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(RavelParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#testlist_comp}.
	 * @param ctx the parse tree
	 */
	void enterTestlist_comp(RavelParser.Testlist_compContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#testlist_comp}.
	 * @param ctx the parse tree
	 */
	void exitTestlist_comp(RavelParser.Testlist_compContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#trailer}.
	 * @param ctx the parse tree
	 */
	void enterTrailer(RavelParser.TrailerContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#trailer}.
	 * @param ctx the parse tree
	 */
	void exitTrailer(RavelParser.TrailerContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#arglist}.
	 * @param ctx the parse tree
	 */
	void enterArglist(RavelParser.ArglistContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#arglist}.
	 * @param ctx the parse tree
	 */
	void exitArglist(RavelParser.ArglistContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(RavelParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(RavelParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#subscriptlist}.
	 * @param ctx the parse tree
	 */
	void enterSubscriptlist(RavelParser.SubscriptlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#subscriptlist}.
	 * @param ctx the parse tree
	 */
	void exitSubscriptlist(RavelParser.SubscriptlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#subscript}.
	 * @param ctx the parse tree
	 */
	void enterSubscript(RavelParser.SubscriptContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#subscript}.
	 * @param ctx the parse tree
	 */
	void exitSubscript(RavelParser.SubscriptContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#sliceop}.
	 * @param ctx the parse tree
	 */
	void enterSliceop(RavelParser.SliceopContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#sliceop}.
	 * @param ctx the parse tree
	 */
	void exitSliceop(RavelParser.SliceopContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#exprlist}.
	 * @param ctx the parse tree
	 */
	void enterExprlist(RavelParser.ExprlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#exprlist}.
	 * @param ctx the parse tree
	 */
	void exitExprlist(RavelParser.ExprlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#testlist}.
	 * @param ctx the parse tree
	 */
	void enterTestlist(RavelParser.TestlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#testlist}.
	 * @param ctx the parse tree
	 */
	void exitTestlist(RavelParser.TestlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#comp_iter}.
	 * @param ctx the parse tree
	 */
	void enterComp_iter(RavelParser.Comp_iterContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#comp_iter}.
	 * @param ctx the parse tree
	 */
	void exitComp_iter(RavelParser.Comp_iterContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#comp_for}.
	 * @param ctx the parse tree
	 */
	void enterComp_for(RavelParser.Comp_forContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#comp_for}.
	 * @param ctx the parse tree
	 */
	void exitComp_for(RavelParser.Comp_forContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#comp_if}.
	 * @param ctx the parse tree
	 */
	void enterComp_if(RavelParser.Comp_ifContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#comp_if}.
	 * @param ctx the parse tree
	 */
	void exitComp_if(RavelParser.Comp_ifContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#string}.
	 * @param ctx the parse tree
	 */
	void enterString(RavelParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#string}.
	 * @param ctx the parse tree
	 */
	void exitString(RavelParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(RavelParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(RavelParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#integer}.
	 * @param ctx the parse tree
	 */
	void enterInteger(RavelParser.IntegerContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#integer}.
	 * @param ctx the parse tree
	 */
	void exitInteger(RavelParser.IntegerContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#bool}.
	 * @param ctx the parse tree
	 */
	void enterBool(RavelParser.BoolContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#bool}.
	 * @param ctx the parse tree
	 */
	void exitBool(RavelParser.BoolContext ctx);
}
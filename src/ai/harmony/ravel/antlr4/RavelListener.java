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
	 * Enter a parse tree produced by {@link RavelParser#platforms}.
	 * @param ctx the parse tree
	 */
	void enterPlatforms(RavelParser.PlatformsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#platforms}.
	 * @param ctx the parse tree
	 */
	void exitPlatforms(RavelParser.PlatformsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PlatformAssigment}
	 * labeled alternative in {@link RavelParser#platform}.
	 * @param ctx the parse tree
	 */
	void enterPlatformAssigment(RavelParser.PlatformAssigmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PlatformAssigment}
	 * labeled alternative in {@link RavelParser#platform}.
	 * @param ctx the parse tree
	 */
	void exitPlatformAssigment(RavelParser.PlatformAssigmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#assigment}.
	 * @param ctx the parse tree
	 */
	void enterAssigment(RavelParser.AssigmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#assigment}.
	 * @param ctx the parse tree
	 */
	void exitAssigment(RavelParser.AssigmentContext ctx);
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
	 * labeled alternative in {@link RavelParser#property}.
	 * @param ctx the parse tree
	 */
	void enterVarAssigment(RavelParser.VarAssigmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VarAssigment}
	 * labeled alternative in {@link RavelParser#property}.
	 * @param ctx the parse tree
	 */
	void exitVarAssigment(RavelParser.VarAssigmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#prop}.
	 * @param ctx the parse tree
	 */
	void enterProp(RavelParser.PropContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#prop}.
	 * @param ctx the parse tree
	 */
	void exitProp(RavelParser.PropContext ctx);
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
	 * Enter a parse tree produced by {@link RavelParser#block_stmt}.
	 * @param ctx the parse tree
	 */
	void enterBlock_stmt(RavelParser.Block_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#block_stmt}.
	 * @param ctx the parse tree
	 */
	void exitBlock_stmt(RavelParser.Block_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(RavelParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(RavelParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#variableDeclarators}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarators(RavelParser.VariableDeclaratorsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#variableDeclarators}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarators(RavelParser.VariableDeclaratorsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarator(RavelParser.VariableDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarator(RavelParser.VariableDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#variableInitializer}.
	 * @param ctx the parse tree
	 */
	void enterVariableInitializer(RavelParser.VariableInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#variableInitializer}.
	 * @param ctx the parse tree
	 */
	void exitVariableInitializer(RavelParser.VariableInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#arrayInitializer}.
	 * @param ctx the parse tree
	 */
	void enterArrayInitializer(RavelParser.ArrayInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#arrayInitializer}.
	 * @param ctx the parse tree
	 */
	void exitArrayInitializer(RavelParser.ArrayInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(RavelParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(RavelParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfStatement}
	 * labeled alternative in {@link RavelParser#if_stmt}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(RavelParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfStatement}
	 * labeled alternative in {@link RavelParser#if_stmt}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(RavelParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#comp_expr}.
	 * @param ctx the parse tree
	 */
	void enterComp_expr(RavelParser.Comp_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#comp_expr}.
	 * @param ctx the parse tree
	 */
	void exitComp_expr(RavelParser.Comp_exprContext ctx);
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
	 * Enter a parse tree produced by {@link RavelParser#statementExpression}.
	 * @param ctx the parse tree
	 */
	void enterStatementExpression(RavelParser.StatementExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#statementExpression}.
	 * @param ctx the parse tree
	 */
	void exitStatementExpression(RavelParser.StatementExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#forControl}.
	 * @param ctx the parse tree
	 */
	void enterForControl(RavelParser.ForControlContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#forControl}.
	 * @param ctx the parse tree
	 */
	void exitForControl(RavelParser.ForControlContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#forInit}.
	 * @param ctx the parse tree
	 */
	void enterForInit(RavelParser.ForInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#forInit}.
	 * @param ctx the parse tree
	 */
	void exitForInit(RavelParser.ForInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#forUpdate}.
	 * @param ctx the parse tree
	 */
	void enterForUpdate(RavelParser.ForUpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#forUpdate}.
	 * @param ctx the parse tree
	 */
	void exitForUpdate(RavelParser.ForUpdateContext ctx);
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
	 * Enter a parse tree produced by {@link RavelParser#functionArgsList}.
	 * @param ctx the parse tree
	 */
	void enterFunctionArgsList(RavelParser.FunctionArgsListContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#functionArgsList}.
	 * @param ctx the parse tree
	 */
	void exitFunctionArgsList(RavelParser.FunctionArgsListContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#functionArg}.
	 * @param ctx the parse tree
	 */
	void enterFunctionArg(RavelParser.FunctionArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#functionArg}.
	 * @param ctx the parse tree
	 */
	void exitFunctionArg(RavelParser.FunctionArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#component_parameters}.
	 * @param ctx the parse tree
	 */
	void enterComponent_parameters(RavelParser.Component_parametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#component_parameters}.
	 * @param ctx the parse tree
	 */
	void exitComponent_parameters(RavelParser.Component_parametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#params}.
	 * @param ctx the parse tree
	 */
	void enterParams(RavelParser.ParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#params}.
	 * @param ctx the parse tree
	 */
	void exitParams(RavelParser.ParamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#elementValuePairs}.
	 * @param ctx the parse tree
	 */
	void enterElementValuePairs(RavelParser.ElementValuePairsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#elementValuePairs}.
	 * @param ctx the parse tree
	 */
	void exitElementValuePairs(RavelParser.ElementValuePairsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#elementValuePair}.
	 * @param ctx the parse tree
	 */
	void enterElementValuePair(RavelParser.ElementValuePairContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#elementValuePair}.
	 * @param ctx the parse tree
	 */
	void exitElementValuePair(RavelParser.ElementValuePairContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#elementValue}.
	 * @param ctx the parse tree
	 */
	void enterElementValue(RavelParser.ElementValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#elementValue}.
	 * @param ctx the parse tree
	 */
	void exitElementValue(RavelParser.ElementValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#elementValueArrayInitializer}.
	 * @param ctx the parse tree
	 */
	void enterElementValueArrayInitializer(RavelParser.ElementValueArrayInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#elementValueArrayInitializer}.
	 * @param ctx the parse tree
	 */
	void exitElementValueArrayInitializer(RavelParser.ElementValueArrayInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(RavelParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(RavelParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#increament_expr}.
	 * @param ctx the parse tree
	 */
	void enterIncreament_expr(RavelParser.Increament_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#increament_expr}.
	 * @param ctx the parse tree
	 */
	void exitIncreament_expr(RavelParser.Increament_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#decrement_exp}.
	 * @param ctx the parse tree
	 */
	void enterDecrement_exp(RavelParser.Decrement_expContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#decrement_exp}.
	 * @param ctx the parse tree
	 */
	void exitDecrement_exp(RavelParser.Decrement_expContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(RavelParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(RavelParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(RavelParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(RavelParser.PrimaryContext ctx);
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
	 * Enter a parse tree produced by {@link RavelParser#qualified_name}.
	 * @param ctx the parse tree
	 */
	void enterQualified_name(RavelParser.Qualified_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#qualified_name}.
	 * @param ctx the parse tree
	 */
	void exitQualified_name(RavelParser.Qualified_nameContext ctx);
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
	 * Enter a parse tree produced by {@link RavelParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(RavelParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(RavelParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#boolean_r}.
	 * @param ctx the parse tree
	 */
	void enterBoolean_r(RavelParser.Boolean_rContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#boolean_r}.
	 * @param ctx the parse tree
	 */
	void exitBoolean_r(RavelParser.Boolean_rContext ctx);
}
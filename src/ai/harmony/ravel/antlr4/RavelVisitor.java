// Generated from /Users/lauril/workspace/01-ravel/RavelLang/Ravel.g4 by ANTLR 4.5.3
package ai.harmony.ravel.antlr4;

import ai.harmony.ravel.compiler.scope.*;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link RavelParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface RavelVisitor<T> extends ParseTreeVisitor<T> {
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
	 * Visit a parse tree produced by the {@code SpaceScope}
	 * labeled alternative in {@link RavelParser#space_comp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceScope(RavelParser.SpaceScopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#space_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpace_body(RavelParser.Space_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#space_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpace_block(RavelParser.Space_blockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PlatformScope}
	 * labeled alternative in {@link RavelParser#platform_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlatformScope(RavelParser.PlatformScopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#platforms}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlatforms(RavelParser.PlatformsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PlatformAssigment}
	 * labeled alternative in {@link RavelParser#platform}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlatformAssigment(RavelParser.PlatformAssigmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#assigment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssigment(RavelParser.AssigmentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ModelInstanciation}
	 * labeled alternative in {@link RavelParser#models_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelInstanciation(RavelParser.ModelInstanciationContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#instantiations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstantiations(RavelParser.InstantiationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#instance}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstance(RavelParser.InstanceContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#instance_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstance_name(RavelParser.Instance_nameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ControllerInstanciation}
	 * labeled alternative in {@link RavelParser#controllers_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControllerInstanciation(RavelParser.ControllerInstanciationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SinkLinks}
	 * labeled alternative in {@link RavelParser#sink_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSinkLinks(RavelParser.SinkLinksContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#references}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferences(RavelParser.ReferencesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SourceLinks}
	 * labeled alternative in {@link RavelParser#source_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceLinks(RavelParser.SourceLinksContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ModelScope}
	 * labeled alternative in {@link RavelParser#model_comp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelScope(RavelParser.ModelScopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#modelType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelType(RavelParser.ModelTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#model_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_body(RavelParser.Model_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#model_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_block(RavelParser.Model_blockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PropertiesScope}
	 * labeled alternative in {@link RavelParser#properties_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertiesScope(RavelParser.PropertiesScopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperties(RavelParser.PropertiesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VarAssigment}
	 * labeled alternative in {@link RavelParser#property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarAssigment(RavelParser.VarAssigmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#prop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProp(RavelParser.PropContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SchemaScope}
	 * labeled alternative in {@link RavelParser#schema_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaScope(RavelParser.SchemaScopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#fields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFields(RavelParser.FieldsContext ctx);
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
	 * Visit a parse tree produced by {@link RavelParser#model_field_identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_field_identifier(RavelParser.Model_field_identifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ControllerScope}
	 * labeled alternative in {@link RavelParser#controller_comp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControllerScope(RavelParser.ControllerScopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#controller_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitController_scope(RavelParser.Controller_scopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#controller_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitController_body(RavelParser.Controller_bodyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EventScope}
	 * labeled alternative in {@link RavelParser#eventdef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEventScope(RavelParser.EventScopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#block_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock_stmt(RavelParser.Block_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#blockStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatement(RavelParser.BlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#variableDeclarators}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclarators(RavelParser.VariableDeclaratorsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#variableDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclarator(RavelParser.VariableDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#variableInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableInitializer(RavelParser.VariableInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#arrayInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayInitializer(RavelParser.ArrayInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(RavelParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfStatement}
	 * labeled alternative in {@link RavelParser#if_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(RavelParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#comp_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComp_expr(RavelParser.Comp_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#or_test}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr_test(RavelParser.Or_testContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#and_test}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd_test(RavelParser.And_testContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#not_test}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNot_test(RavelParser.Not_testContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#comparison}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparison(RavelParser.ComparisonContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(RavelParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(RavelParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#statementExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementExpression(RavelParser.StatementExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#forControl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForControl(RavelParser.ForControlContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#forInit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForInit(RavelParser.ForInitContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#forUpdate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForUpdate(RavelParser.ForUpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#function_args}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_args(RavelParser.Function_argsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#functionArgsList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionArgsList(RavelParser.FunctionArgsListContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#functionArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionArg(RavelParser.FunctionArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#component_parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComponent_parameters(RavelParser.Component_parametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParams(RavelParser.ParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#elementValuePairs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementValuePairs(RavelParser.ElementValuePairsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#elementValuePair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementValuePair(RavelParser.ElementValuePairContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#elementValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementValue(RavelParser.ElementValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#elementValueArrayInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementValueArrayInitializer(RavelParser.ElementValueArrayInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(RavelParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#increament_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncreament_expr(RavelParser.Increament_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#decrement_exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecrement_exp(RavelParser.Decrement_expContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(RavelParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary(RavelParser.PrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ReferenceAssigment}
	 * labeled alternative in {@link RavelParser#ref_assig}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceAssigment(RavelParser.ReferenceAssigmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#funct_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunct_expr(RavelParser.Funct_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#func_no_return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_no_return(RavelParser.Func_no_returnContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#func_with_return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_with_return(RavelParser.Func_with_returnContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#qualified_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualified_name(RavelParser.Qualified_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#comp_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComp_op(RavelParser.Comp_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(RavelParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#boolean_r}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolean_r(RavelParser.Boolean_rContext ctx);
}
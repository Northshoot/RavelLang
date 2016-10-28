// Generated from /Users/lauril/workspace/01-ravel/RavelLang/Ravel.g4 by ANTLR 4.5.3
package ai.harmony.antlr4;

import ai.harmony.ravel.compiler.scope.*;
import ai.harmony.ravel.compiler.symbol.*;

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
	 * Visit a parse tree produced by {@link RavelParser#instanciation_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstanciation_block(RavelParser.Instanciation_blockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ModelInstanciation}
	 * labeled alternative in {@link RavelParser#models_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelInstanciation(RavelParser.ModelInstanciationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ControllerInstanciation}
	 * labeled alternative in {@link RavelParser#controllers_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControllerInstanciation(RavelParser.ControllerInstanciationContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#instantiations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstantiations(RavelParser.InstantiationsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Instance}
	 * labeled alternative in {@link RavelParser#instance_def}.
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
	 * Visit a parse tree produced by the {@code ParameterAssignments}
	 * labeled alternative in {@link RavelParser#param_assig_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterAssignments(RavelParser.ParameterAssignmentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#param_assig}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_assig(RavelParser.Param_assigContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#param_val}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_val(RavelParser.Param_valContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#reference_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReference_block(RavelParser.Reference_blockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PlatformScope}
	 * labeled alternative in {@link RavelParser#platform_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlatformScope(RavelParser.PlatformScopeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SinkLinks}
	 * labeled alternative in {@link RavelParser#sink_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSinkLinks(RavelParser.SinkLinksContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SourceLinks}
	 * labeled alternative in {@link RavelParser#source_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceLinks(RavelParser.SourceLinksContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#space_assignments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpace_assignments(RavelParser.Space_assignmentsContext ctx);
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
	 * Visit a parse tree produced by the {@code VarAssignment}
	 * labeled alternative in {@link RavelParser#property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarAssignment(RavelParser.VarAssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#propValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropValue(RavelParser.PropValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#propArray}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropArray(RavelParser.PropArrayContext ctx);
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
	 * Visit a parse tree produced by {@link RavelParser#field_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitField_options(RavelParser.Field_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#field_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitField_option(RavelParser.Field_optionContext ctx);
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
	 * Visit a parse tree produced by {@link RavelParser#arg_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg_type(RavelParser.Arg_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#arg_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg_name(RavelParser.Arg_nameContext ctx);
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
	 * Visit a parse tree produced by {@link RavelParser#comp_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComp_stmt(RavelParser.Comp_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#repeat_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeat_stmt(RavelParser.Repeat_stmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DeleteStmt}
	 * labeled alternative in {@link RavelParser#del_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteStmt(RavelParser.DeleteStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#variable_declarations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable_declarations(RavelParser.Variable_declarationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#variable_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable_declaration(RavelParser.Variable_declarationContext ctx);
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
	 * Visit a parse tree produced by {@link RavelParser#while_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_stmt(RavelParser.While_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#for_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_stmt(RavelParser.For_stmtContext ctx);
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
	 * Visit a parse tree produced by {@link RavelParser#forControl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForControl(RavelParser.ForControlContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#exprlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprlist(RavelParser.ExprlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#testlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTestlist(RavelParser.TestlistContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WhileStmt}
	 * labeled alternative in {@link RavelParser#whileControl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStmt(RavelParser.WhileStmtContext ctx);
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
	 * Visit a parse tree produced by {@link RavelParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(RavelParser.ParamContext ctx);
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
	 * Visit a parse tree produced by the {@code ReferenceAssignmentsList}
	 * labeled alternative in {@link RavelParser#ref_assig_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceAssignmentsList(RavelParser.ReferenceAssignmentsListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ReferenceAssignment}
	 * labeled alternative in {@link RavelParser#ref_assig}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceAssignment(RavelParser.ReferenceAssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#key}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey(RavelParser.KeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(RavelParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#funct_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunct_expr(RavelParser.Funct_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FunctionRet}
	 * labeled alternative in {@link RavelParser#func_no_return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionRet(RavelParser.FunctionRetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#function_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_name(RavelParser.Function_nameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FunctionWithReturn}
	 * labeled alternative in {@link RavelParser#func_with_return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionWithReturn(RavelParser.FunctionWithReturnContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#ident}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdent(RavelParser.IdentContext ctx);
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
}
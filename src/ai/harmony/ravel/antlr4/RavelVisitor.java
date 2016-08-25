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
	 * Visit a parse tree produced by {@link RavelParser#parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameters(RavelParser.ParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#typedargslist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedargslist(RavelParser.TypedargslistContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(RavelParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#tdefvar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTdefvar(RavelParser.TdefvarContext ctx);
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
	 * labeled alternative in {@link RavelParser#tdefvar_assig}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarAssigment(RavelParser.VarAssigmentContext ctx);
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
	 * Visit a parse tree produced by the {@code ReferenceAssigment}
	 * labeled alternative in {@link RavelParser#ref_assig}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceAssigment(RavelParser.ReferenceAssigmentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EventScope}
	 * labeled alternative in {@link RavelParser#eventdef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEventScope(RavelParser.EventScopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#function_args}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_args(RavelParser.Function_argsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#function_param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_param(RavelParser.Function_paramContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#param_prair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_prair(RavelParser.Param_prairContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#param_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_type(RavelParser.Param_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#param_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_name(RavelParser.Param_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#suite}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuite(RavelParser.SuiteContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt(RavelParser.StmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#small_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSmall_stmt(RavelParser.Small_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#model_field_assig}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_field_assig(RavelParser.Model_field_assigContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#model_assig_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel_assig_expr(RavelParser.Model_assig_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#compound_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompound_stmt(RavelParser.Compound_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#assert_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssert_stmt(RavelParser.Assert_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#del_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDel_stmt(RavelParser.Del_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#function_call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_call(RavelParser.Function_callContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#query_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery_expr(RavelParser.Query_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code QueryOperations}
	 * labeled alternative in {@link RavelParser#local_query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryOperations(RavelParser.QueryOperationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#query_operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery_operation(RavelParser.Query_operationContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#db_query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDb_query(RavelParser.Db_queryContext ctx);
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
	 * Visit a parse tree produced by {@link RavelParser#dotted_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotted_name(RavelParser.Dotted_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#pass_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPass_stmt(RavelParser.Pass_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#flow_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFlow_stmt(RavelParser.Flow_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#break_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreak_stmt(RavelParser.Break_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#continue_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinue_stmt(RavelParser.Continue_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#return_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_stmt(RavelParser.Return_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#if_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_stmt(RavelParser.If_stmtContext ctx);
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
	 * Visit a parse tree produced by {@link RavelParser#test}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTest(RavelParser.TestContext ctx);
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
	 * Visit a parse tree produced by {@link RavelParser#comp_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComp_op(RavelParser.Comp_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(RavelParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#xor_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXor_expr(RavelParser.Xor_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#and_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd_expr(RavelParser.And_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#shift_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShift_expr(RavelParser.Shift_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#arith_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArith_expr(RavelParser.Arith_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(RavelParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(RavelParser.FactorContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#power}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPower(RavelParser.PowerContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(RavelParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#testlist_comp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTestlist_comp(RavelParser.Testlist_compContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#trailer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrailer(RavelParser.TrailerContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#arglist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArglist(RavelParser.ArglistContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(RavelParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#subscriptlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubscriptlist(RavelParser.SubscriptlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#subscript}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubscript(RavelParser.SubscriptContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#sliceop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSliceop(RavelParser.SliceopContext ctx);
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
	 * Visit a parse tree produced by {@link RavelParser#comp_iter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComp_iter(RavelParser.Comp_iterContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#comp_for}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComp_for(RavelParser.Comp_forContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#comp_if}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComp_if(RavelParser.Comp_ifContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(RavelParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(RavelParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#integer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInteger(RavelParser.IntegerContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#bool}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool(RavelParser.BoolContext ctx);
}
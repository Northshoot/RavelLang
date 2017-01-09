// Generated from /Users/lauril/workspace/01-ravel/RavelLang/Ravel.g4 by ANTLR 4.5.3
package org.stanford.antlr4;

import org.stanford.ravel.compiler.scope.*;
import org.stanford.ravel.compiler.symbol.*;

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
	 * Enter a parse tree produced by {@link RavelParser#space_assignments}.
	 * @param ctx the parse tree
	 */
	void enterSpace_assignments(RavelParser.Space_assignmentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#space_assignments}.
	 * @param ctx the parse tree
	 */
	void exitSpace_assignments(RavelParser.Space_assignmentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#space_assigment}.
	 * @param ctx the parse tree
	 */
	void enterSpace_assigment(RavelParser.Space_assigmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#space_assigment}.
	 * @param ctx the parse tree
	 */
	void exitSpace_assigment(RavelParser.Space_assigmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ModelInstantiation}
	 * labeled alternative in {@link RavelParser#models_scope}.
	 * @param ctx the parse tree
	 */
	void enterModelInstantiation(RavelParser.ModelInstantiationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ModelInstantiation}
	 * labeled alternative in {@link RavelParser#models_scope}.
	 * @param ctx the parse tree
	 */
	void exitModelInstantiation(RavelParser.ModelInstantiationContext ctx);
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
	 * Enter a parse tree produced by the {@code Instance}
	 * labeled alternative in {@link RavelParser#instance_def}.
	 * @param ctx the parse tree
	 */
	void enterInstance(RavelParser.InstanceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Instance}
	 * labeled alternative in {@link RavelParser#instance_def}.
	 * @param ctx the parse tree
	 */
	void exitInstance(RavelParser.InstanceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParameterAssignments}
	 * labeled alternative in {@link RavelParser#param_assig_list}.
	 * @param ctx the parse tree
	 */
	void enterParameterAssignments(RavelParser.ParameterAssignmentsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParameterAssignments}
	 * labeled alternative in {@link RavelParser#param_assig_list}.
	 * @param ctx the parse tree
	 */
	void exitParameterAssignments(RavelParser.ParameterAssignmentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#param_assig}.
	 * @param ctx the parse tree
	 */
	void enterParam_assig(RavelParser.Param_assigContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#param_assig}.
	 * @param ctx the parse tree
	 */
	void exitParam_assig(RavelParser.Param_assigContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#param_val}.
	 * @param ctx the parse tree
	 */
	void enterParam_val(RavelParser.Param_valContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#param_val}.
	 * @param ctx the parse tree
	 */
	void exitParam_val(RavelParser.Param_valContext ctx);
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
	 * Enter a parse tree produced by the {@code ControllerInstantiation}
	 * labeled alternative in {@link RavelParser#controllers_scope}.
	 * @param ctx the parse tree
	 */
	void enterControllerInstantiation(RavelParser.ControllerInstantiationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ControllerInstantiation}
	 * labeled alternative in {@link RavelParser#controllers_scope}.
	 * @param ctx the parse tree
	 */
	void exitControllerInstantiation(RavelParser.ControllerInstantiationContext ctx);
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
	 * Enter a parse tree produced by the {@code VarAssignment}
	 * labeled alternative in {@link RavelParser#property}.
	 * @param ctx the parse tree
	 */
	void enterVarAssignment(RavelParser.VarAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VarAssignment}
	 * labeled alternative in {@link RavelParser#property}.
	 * @param ctx the parse tree
	 */
	void exitVarAssignment(RavelParser.VarAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#propValue}.
	 * @param ctx the parse tree
	 */
	void enterPropValue(RavelParser.PropValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#propValue}.
	 * @param ctx the parse tree
	 */
	void exitPropValue(RavelParser.PropValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#propArray}.
	 * @param ctx the parse tree
	 */
	void enterPropArray(RavelParser.PropArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#propArray}.
	 * @param ctx the parse tree
	 */
	void exitPropArray(RavelParser.PropArrayContext ctx);
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
	 * Enter a parse tree produced by the {@code Block}
	 * labeled alternative in {@link RavelParser#block_stmt}.
	 * @param ctx the parse tree
	 */
	void enterBlock(RavelParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Block}
	 * labeled alternative in {@link RavelParser#block_stmt}.
	 * @param ctx the parse tree
	 */
	void exitBlock(RavelParser.BlockContext ctx);
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
	 * Enter a parse tree produced by {@link RavelParser#comp_stmt}.
	 * @param ctx the parse tree
	 */
	void enterComp_stmt(RavelParser.Comp_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#comp_stmt}.
	 * @param ctx the parse tree
	 */
	void exitComp_stmt(RavelParser.Comp_stmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DeleteStmt}
	 * labeled alternative in {@link RavelParser#del_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDeleteStmt(RavelParser.DeleteStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DeleteStmt}
	 * labeled alternative in {@link RavelParser#del_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDeleteStmt(RavelParser.DeleteStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(RavelParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(RavelParser.VariableContext ctx);
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
	 * Enter a parse tree produced by the {@code WhileStatement}
	 * labeled alternative in {@link RavelParser#while_stmt}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(RavelParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WhileStatement}
	 * labeled alternative in {@link RavelParser#while_stmt}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(RavelParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ForStatement}
	 * labeled alternative in {@link RavelParser#for_stmt}.
	 * @param ctx the parse tree
	 */
	void enterForStatement(RavelParser.ForStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ForStatement}
	 * labeled alternative in {@link RavelParser#for_stmt}.
	 * @param ctx the parse tree
	 */
	void exitForStatement(RavelParser.ForStatementContext ctx);
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
	 * Enter a parse tree produced by the {@code CompExpr}
	 * labeled alternative in {@link RavelParser#comp_expr}.
	 * @param ctx the parse tree
	 */
	void enterCompExpr(RavelParser.CompExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CompExpr}
	 * labeled alternative in {@link RavelParser#comp_expr}.
	 * @param ctx the parse tree
	 */
	void exitCompExpr(RavelParser.CompExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CompRule}
	 * labeled alternative in {@link RavelParser#comparison}.
	 * @param ctx the parse tree
	 */
	void enterCompRule(RavelParser.CompRuleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CompRule}
	 * labeled alternative in {@link RavelParser#comparison}.
	 * @param ctx the parse tree
	 */
	void exitCompRule(RavelParser.CompRuleContext ctx);
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
	 * Enter a parse tree produced by the {@code WhileStmt}
	 * labeled alternative in {@link RavelParser#whileControl}.
	 * @param ctx the parse tree
	 */
	void enterWhileStmt(RavelParser.WhileStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WhileStmt}
	 * labeled alternative in {@link RavelParser#whileControl}.
	 * @param ctx the parse tree
	 */
	void exitWhileStmt(RavelParser.WhileStmtContext ctx);
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
	 * Enter a parse tree produced by {@link RavelParser#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(RavelParser.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(RavelParser.ParamContext ctx);
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
	 * Enter a parse tree produced by the {@code ReferenceAssignmentsList}
	 * labeled alternative in {@link RavelParser#ref_assig_list}.
	 * @param ctx the parse tree
	 */
	void enterReferenceAssignmentsList(RavelParser.ReferenceAssignmentsListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ReferenceAssignmentsList}
	 * labeled alternative in {@link RavelParser#ref_assig_list}.
	 * @param ctx the parse tree
	 */
	void exitReferenceAssignmentsList(RavelParser.ReferenceAssignmentsListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ReferenceAssignment}
	 * labeled alternative in {@link RavelParser#ref_assig}.
	 * @param ctx the parse tree
	 */
	void enterReferenceAssignment(RavelParser.ReferenceAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ReferenceAssignment}
	 * labeled alternative in {@link RavelParser#ref_assig}.
	 * @param ctx the parse tree
	 */
	void exitReferenceAssignment(RavelParser.ReferenceAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#reference_name}.
	 * @param ctx the parse tree
	 */
	void enterReference_name(RavelParser.Reference_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#reference_name}.
	 * @param ctx the parse tree
	 */
	void exitReference_name(RavelParser.Reference_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#reference_value}.
	 * @param ctx the parse tree
	 */
	void enterReference_value(RavelParser.Reference_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#reference_value}.
	 * @param ctx the parse tree
	 */
	void exitReference_value(RavelParser.Reference_valueContext ctx);
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
	 * Enter a parse tree produced by the {@code FunctionRet}
	 * labeled alternative in {@link RavelParser#func_no_return}.
	 * @param ctx the parse tree
	 */
	void enterFunctionRet(RavelParser.FunctionRetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FunctionRet}
	 * labeled alternative in {@link RavelParser#func_no_return}.
	 * @param ctx the parse tree
	 */
	void exitFunctionRet(RavelParser.FunctionRetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#function_name}.
	 * @param ctx the parse tree
	 */
	void enterFunction_name(RavelParser.Function_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#function_name}.
	 * @param ctx the parse tree
	 */
	void exitFunction_name(RavelParser.Function_nameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FunctionWithReturn}
	 * labeled alternative in {@link RavelParser#func_with_return}.
	 * @param ctx the parse tree
	 */
	void enterFunctionWithReturn(RavelParser.FunctionWithReturnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FunctionWithReturn}
	 * labeled alternative in {@link RavelParser#func_with_return}.
	 * @param ctx the parse tree
	 */
	void exitFunctionWithReturn(RavelParser.FunctionWithReturnContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#ident}.
	 * @param ctx the parse tree
	 */
	void enterIdent(RavelParser.IdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#ident}.
	 * @param ctx the parse tree
	 */
	void exitIdent(RavelParser.IdentContext ctx);
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
	 * Enter a parse tree produced by {@link RavelParser#float_point}.
	 * @param ctx the parse tree
	 */
	void enterFloat_point(RavelParser.Float_pointContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#float_point}.
	 * @param ctx the parse tree
	 */
	void exitFloat_point(RavelParser.Float_pointContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#boolean_rule}.
	 * @param ctx the parse tree
	 */
	void enterBoolean_rule(RavelParser.Boolean_ruleContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#boolean_rule}.
	 * @param ctx the parse tree
	 */
	void exitBoolean_rule(RavelParser.Boolean_ruleContext ctx);
}
// Generated from /home/gcampagn/secureiot/ravellang/Ravel.g4 by ANTLR 4.6
package org.stanford.antlr4;

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
	 * Visit a parse tree produced by {@link RavelParser#space_assignments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpace_assignments(RavelParser.Space_assignmentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#space_assigment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpace_assigment(RavelParser.Space_assigmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#ref_assign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRef_assign(RavelParser.Ref_assignContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#simple_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_expression(RavelParser.Simple_expressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ModelInstantiation}
	 * labeled alternative in {@link RavelParser#models_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelInstantiation(RavelParser.ModelInstantiationContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#instantiations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstantiations(RavelParser.InstantiationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#instance_line}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstance_line(RavelParser.Instance_lineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Instance}
	 * labeled alternative in {@link RavelParser#instance_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstance(RavelParser.InstanceContext ctx);
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
	 * Visit a parse tree produced by {@link RavelParser#instance_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstance_name(RavelParser.Instance_nameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ControllerInstantiation}
	 * labeled alternative in {@link RavelParser#controllers_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControllerInstantiation(RavelParser.ControllerInstantiationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InterfaceInstantiation}
	 * labeled alternative in {@link RavelParser#interface_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceInstantiation(RavelParser.InterfaceInstantiationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InterfaceScope}
	 * labeled alternative in {@link RavelParser#iface_comp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceScope(RavelParser.InterfaceScopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#iface_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIface_body(RavelParser.Iface_bodyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ImplementationScope}
	 * labeled alternative in {@link RavelParser#impl_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplementationScope(RavelParser.ImplementationScopeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConfigurationScope}
	 * labeled alternative in {@link RavelParser#config_scope}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConfigurationScope(RavelParser.ConfigurationScopeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#iface_members}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIface_members(RavelParser.Iface_membersContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InterfaceDef}
	 * labeled alternative in {@link RavelParser#iface_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceDef(RavelParser.InterfaceDefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InterfaceEvent}
	 * labeled alternative in {@link RavelParser#iface_event}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceEvent(RavelParser.InterfaceEventContext ctx);
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
	 * Visit a parse tree produced by {@link RavelParser#property_line}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProperty_line(RavelParser.Property_lineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DirectedFlow}
	 * labeled alternative in {@link RavelParser#flow_assign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirectedFlow(RavelParser.DirectedFlowContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UndirectedFlow}
	 * labeled alternative in {@link RavelParser#flow_assign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUndirectedFlow(RavelParser.UndirectedFlowContext ctx);
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
	 * Visit a parse tree produced by {@link RavelParser#field_line}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitField_line(RavelParser.Field_lineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FieldDeclaration}
	 * labeled alternative in {@link RavelParser#field}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldDeclaration(RavelParser.FieldDeclarationContext ctx);
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
	 * Visit a parse tree produced by {@link RavelParser#controller_entry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitController_entry(RavelParser.Controller_entryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EventScope}
	 * labeled alternative in {@link RavelParser#eventdef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEventScope(RavelParser.EventScopeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Block}
	 * labeled alternative in {@link RavelParser#block_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(RavelParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(RavelParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DeleteStmt}
	 * labeled alternative in {@link RavelParser#del_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteStmt(RavelParser.DeleteStmtContext ctx);
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
	 * Visit a parse tree produced by {@link RavelParser#lvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLvalue(RavelParser.LvalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#assign_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign_op(RavelParser.Assign_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#ident_decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdent_decl(RavelParser.Ident_declContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#identifier_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier_list(RavelParser.Identifier_listContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypedIdentDecl}
	 * labeled alternative in {@link RavelParser#typed_ident_decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedIdentDecl(RavelParser.TypedIdentDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#typed_identifier_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTyped_identifier_list(RavelParser.Typed_identifier_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#var_decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar_decl(RavelParser.Var_declContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(RavelParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#array_marker}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_marker(RavelParser.Array_markerContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(RavelParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#lvalue_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLvalue_expression(RavelParser.Lvalue_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(RavelParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(RavelParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#array_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_literal(RavelParser.Array_literalContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#method_call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethod_call(RavelParser.Method_callContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary(RavelParser.PrimaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#access_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccess_op(RavelParser.Access_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#member_access}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMember_access(RavelParser.Member_accessContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#array_access}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_access(RavelParser.Array_accessContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#power_exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPower_exp(RavelParser.Power_expContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#unary_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary_op(RavelParser.Unary_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#unary_exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary_exp(RavelParser.Unary_expContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#mult_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMult_op(RavelParser.Mult_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#mult_exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMult_exp(RavelParser.Mult_expContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#add_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_op(RavelParser.Add_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#add_exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_exp(RavelParser.Add_expContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#shift_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShift_op(RavelParser.Shift_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#shift_exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShift_exp(RavelParser.Shift_expContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#bin_and_exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBin_and_exp(RavelParser.Bin_and_expContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#bin_xor_exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBin_xor_exp(RavelParser.Bin_xor_expContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#bin_or_exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBin_or_exp(RavelParser.Bin_or_expContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#comp_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComp_op(RavelParser.Comp_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#comp_exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComp_exp(RavelParser.Comp_expContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#not_exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNot_exp(RavelParser.Not_expContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#and_exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd_exp(RavelParser.And_expContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#or_exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr_exp(RavelParser.Or_expContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(RavelParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WhileStatement}
	 * labeled alternative in {@link RavelParser#while_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(RavelParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ForStatement}
	 * labeled alternative in {@link RavelParser#for_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatement(RavelParser.ForStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#forControl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForControl(RavelParser.ForControlContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfStatement}
	 * labeled alternative in {@link RavelParser#if_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(RavelParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#qualified_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualified_name(RavelParser.Qualified_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#function_args}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_args(RavelParser.Function_argsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(RavelParser.LiteralContext ctx);
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
	 * Visit a parse tree produced by {@link RavelParser#float_point}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloat_point(RavelParser.Float_pointContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#boolean_rule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolean_rule(RavelParser.Boolean_ruleContext ctx);
}
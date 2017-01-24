// Generated from /home/gcampagn/secureiot/ravellang/Ravel.g4 by ANTLR 4.6
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
	 * Enter a parse tree produced by {@link RavelParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void enterLvalue(RavelParser.LvalueContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void exitLvalue(RavelParser.LvalueContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#assign_op}.
	 * @param ctx the parse tree
	 */
	void enterAssign_op(RavelParser.Assign_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#assign_op}.
	 * @param ctx the parse tree
	 */
	void exitAssign_op(RavelParser.Assign_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#ident_decl}.
	 * @param ctx the parse tree
	 */
	void enterIdent_decl(RavelParser.Ident_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#ident_decl}.
	 * @param ctx the parse tree
	 */
	void exitIdent_decl(RavelParser.Ident_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#identifier_list}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier_list(RavelParser.Identifier_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#identifier_list}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier_list(RavelParser.Identifier_listContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypedIdentDecl}
	 * labeled alternative in {@link RavelParser#typed_ident_decl}.
	 * @param ctx the parse tree
	 */
	void enterTypedIdentDecl(RavelParser.TypedIdentDeclContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypedIdentDecl}
	 * labeled alternative in {@link RavelParser#typed_ident_decl}.
	 * @param ctx the parse tree
	 */
	void exitTypedIdentDecl(RavelParser.TypedIdentDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#typed_identifier_list}.
	 * @param ctx the parse tree
	 */
	void enterTyped_identifier_list(RavelParser.Typed_identifier_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#typed_identifier_list}.
	 * @param ctx the parse tree
	 */
	void exitTyped_identifier_list(RavelParser.Typed_identifier_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#var_decl}.
	 * @param ctx the parse tree
	 */
	void enterVar_decl(RavelParser.Var_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#var_decl}.
	 * @param ctx the parse tree
	 */
	void exitVar_decl(RavelParser.Var_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(RavelParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(RavelParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(RavelParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(RavelParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#ref_assign}.
	 * @param ctx the parse tree
	 */
	void enterRef_assign(RavelParser.Ref_assignContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#ref_assign}.
	 * @param ctx the parse tree
	 */
	void exitRef_assign(RavelParser.Ref_assignContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#lvalue_expression}.
	 * @param ctx the parse tree
	 */
	void enterLvalue_expression(RavelParser.Lvalue_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#lvalue_expression}.
	 * @param ctx the parse tree
	 */
	void exitLvalue_expression(RavelParser.Lvalue_expressionContext ctx);
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
	 * Enter a parse tree produced by {@link RavelParser#array_literal}.
	 * @param ctx the parse tree
	 */
	void enterArray_literal(RavelParser.Array_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#array_literal}.
	 * @param ctx the parse tree
	 */
	void exitArray_literal(RavelParser.Array_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#method_call}.
	 * @param ctx the parse tree
	 */
	void enterMethod_call(RavelParser.Method_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#method_call}.
	 * @param ctx the parse tree
	 */
	void exitMethod_call(RavelParser.Method_callContext ctx);
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
	 * Enter a parse tree produced by {@link RavelParser#access_op}.
	 * @param ctx the parse tree
	 */
	void enterAccess_op(RavelParser.Access_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#access_op}.
	 * @param ctx the parse tree
	 */
	void exitAccess_op(RavelParser.Access_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#member_access}.
	 * @param ctx the parse tree
	 */
	void enterMember_access(RavelParser.Member_accessContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#member_access}.
	 * @param ctx the parse tree
	 */
	void exitMember_access(RavelParser.Member_accessContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#array_access}.
	 * @param ctx the parse tree
	 */
	void enterArray_access(RavelParser.Array_accessContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#array_access}.
	 * @param ctx the parse tree
	 */
	void exitArray_access(RavelParser.Array_accessContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#power_exp}.
	 * @param ctx the parse tree
	 */
	void enterPower_exp(RavelParser.Power_expContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#power_exp}.
	 * @param ctx the parse tree
	 */
	void exitPower_exp(RavelParser.Power_expContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#unary_op}.
	 * @param ctx the parse tree
	 */
	void enterUnary_op(RavelParser.Unary_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#unary_op}.
	 * @param ctx the parse tree
	 */
	void exitUnary_op(RavelParser.Unary_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#unary_exp}.
	 * @param ctx the parse tree
	 */
	void enterUnary_exp(RavelParser.Unary_expContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#unary_exp}.
	 * @param ctx the parse tree
	 */
	void exitUnary_exp(RavelParser.Unary_expContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#mult_op}.
	 * @param ctx the parse tree
	 */
	void enterMult_op(RavelParser.Mult_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#mult_op}.
	 * @param ctx the parse tree
	 */
	void exitMult_op(RavelParser.Mult_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#mult_exp}.
	 * @param ctx the parse tree
	 */
	void enterMult_exp(RavelParser.Mult_expContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#mult_exp}.
	 * @param ctx the parse tree
	 */
	void exitMult_exp(RavelParser.Mult_expContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#add_op}.
	 * @param ctx the parse tree
	 */
	void enterAdd_op(RavelParser.Add_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#add_op}.
	 * @param ctx the parse tree
	 */
	void exitAdd_op(RavelParser.Add_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#add_exp}.
	 * @param ctx the parse tree
	 */
	void enterAdd_exp(RavelParser.Add_expContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#add_exp}.
	 * @param ctx the parse tree
	 */
	void exitAdd_exp(RavelParser.Add_expContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#shift_op}.
	 * @param ctx the parse tree
	 */
	void enterShift_op(RavelParser.Shift_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#shift_op}.
	 * @param ctx the parse tree
	 */
	void exitShift_op(RavelParser.Shift_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#shift_exp}.
	 * @param ctx the parse tree
	 */
	void enterShift_exp(RavelParser.Shift_expContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#shift_exp}.
	 * @param ctx the parse tree
	 */
	void exitShift_exp(RavelParser.Shift_expContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#bin_and_exp}.
	 * @param ctx the parse tree
	 */
	void enterBin_and_exp(RavelParser.Bin_and_expContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#bin_and_exp}.
	 * @param ctx the parse tree
	 */
	void exitBin_and_exp(RavelParser.Bin_and_expContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#bin_xor_exp}.
	 * @param ctx the parse tree
	 */
	void enterBin_xor_exp(RavelParser.Bin_xor_expContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#bin_xor_exp}.
	 * @param ctx the parse tree
	 */
	void exitBin_xor_exp(RavelParser.Bin_xor_expContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#bin_or_exp}.
	 * @param ctx the parse tree
	 */
	void enterBin_or_exp(RavelParser.Bin_or_expContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#bin_or_exp}.
	 * @param ctx the parse tree
	 */
	void exitBin_or_exp(RavelParser.Bin_or_expContext ctx);
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
	 * Enter a parse tree produced by {@link RavelParser#comp_exp}.
	 * @param ctx the parse tree
	 */
	void enterComp_exp(RavelParser.Comp_expContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#comp_exp}.
	 * @param ctx the parse tree
	 */
	void exitComp_exp(RavelParser.Comp_expContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#not_exp}.
	 * @param ctx the parse tree
	 */
	void enterNot_exp(RavelParser.Not_expContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#not_exp}.
	 * @param ctx the parse tree
	 */
	void exitNot_exp(RavelParser.Not_expContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#and_exp}.
	 * @param ctx the parse tree
	 */
	void enterAnd_exp(RavelParser.And_expContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#and_exp}.
	 * @param ctx the parse tree
	 */
	void exitAnd_exp(RavelParser.And_expContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#or_exp}.
	 * @param ctx the parse tree
	 */
	void enterOr_exp(RavelParser.Or_expContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#or_exp}.
	 * @param ctx the parse tree
	 */
	void exitOr_exp(RavelParser.Or_expContext ctx);
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
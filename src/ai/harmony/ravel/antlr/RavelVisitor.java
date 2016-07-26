// Generated from /Users/lauril/workspace/01-ravel/IoTCompiler/Ravel.g4 by ANTLR 4.5.3
package ai.harmony.ravel.antlr;
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
	 * Visit a parse tree produced by the {@code Model}
	 * labeled alternative in {@link RavelParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModel(RavelParser.ModelContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Controller}
	 * labeled alternative in {@link RavelParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitController(RavelParser.ControllerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Views}
	 * labeled alternative in {@link RavelParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViews(RavelParser.ViewsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Transform}
	 * labeled alternative in {@link RavelParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransform(RavelParser.TransformContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Space}
	 * labeled alternative in {@link RavelParser#primitive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpace(RavelParser.SpaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#modelDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelDecl(RavelParser.ModelDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#modelType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelType(RavelParser.ModelTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ModelProp}
	 * labeled alternative in {@link RavelParser#modelBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelProp(RavelParser.ModelPropContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ModelSchema}
	 * labeled alternative in {@link RavelParser#modelBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelSchema(RavelParser.ModelSchemaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PropertyPair}
	 * labeled alternative in {@link RavelParser#propBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyPair(RavelParser.PropertyPairContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SchemaPair}
	 * labeled alternative in {@link RavelParser#schemaBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaPair(RavelParser.SchemaPairContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#controllerDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControllerDecl(RavelParser.ControllerDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#controllerType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControllerType(RavelParser.ControllerTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#controllerBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControllerBody(RavelParser.ControllerBodyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConfigPair}
	 * labeled alternative in {@link RavelParser#configBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConfigPair(RavelParser.ConfigPairContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EventStatement}
	 * labeled alternative in {@link RavelParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEventStatement(RavelParser.EventStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CommandStatement}
	 * labeled alternative in {@link RavelParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommandStatement(RavelParser.CommandStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Event}
	 * labeled alternative in {@link RavelParser#event_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent(RavelParser.EventContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Command}
	 * labeled alternative in {@link RavelParser#commnad}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommand(RavelParser.CommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#func_decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_decl(RavelParser.Func_declContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#arg_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg_list(RavelParser.Arg_listContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Arg}
	 * labeled alternative in {@link RavelParser#args}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg(RavelParser.ArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#config_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConfig_type(RavelParser.Config_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#transformDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransformDecl(RavelParser.TransformDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#transformType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransformType(RavelParser.TransformTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TransConfig}
	 * labeled alternative in {@link RavelParser#transformBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransConfig(RavelParser.TransConfigContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TransCmd}
	 * labeled alternative in {@link RavelParser#transformBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransCmd(RavelParser.TransCmdContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#viewDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewDecl(RavelParser.ViewDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ViewConfig}
	 * labeled alternative in {@link RavelParser#viewBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewConfig(RavelParser.ViewConfigContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ViewCmd}
	 * labeled alternative in {@link RavelParser#viewBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitViewCmd(RavelParser.ViewCmdContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#spaceDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceDecl(RavelParser.SpaceDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#spaceType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceType(RavelParser.SpaceTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SpaceProp}
	 * labeled alternative in {@link RavelParser#spaceBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceProp(RavelParser.SpacePropContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SpaceConfig}
	 * labeled alternative in {@link RavelParser#spaceBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceConfig(RavelParser.SpaceConfigContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SpaceModels}
	 * labeled alternative in {@link RavelParser#spaceBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceModels(RavelParser.SpaceModelsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SpaceCntr}
	 * labeled alternative in {@link RavelParser#spaceBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceCntr(RavelParser.SpaceCntrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SpaceSinks}
	 * labeled alternative in {@link RavelParser#spaceBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceSinks(RavelParser.SpaceSinksContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SpaceSrc}
	 * labeled alternative in {@link RavelParser#spaceBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceSrc(RavelParser.SpaceSrcContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#pair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPair(RavelParser.PairContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(RavelParser.ArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(RavelParser.ValueContext ctx);
}
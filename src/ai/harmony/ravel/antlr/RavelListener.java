// Generated from /Users/lauril/workspace/01-ravel/IoTCompiler/Ravel.g4 by ANTLR 4.5.3
package ai.harmony.ravel.antlr;
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
	 * Enter a parse tree produced by the {@code Model}
	 * labeled alternative in {@link RavelParser#primitive}.
	 * @param ctx the parse tree
	 */
	void enterModel(RavelParser.ModelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Model}
	 * labeled alternative in {@link RavelParser#primitive}.
	 * @param ctx the parse tree
	 */
	void exitModel(RavelParser.ModelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Controller}
	 * labeled alternative in {@link RavelParser#primitive}.
	 * @param ctx the parse tree
	 */
	void enterController(RavelParser.ControllerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Controller}
	 * labeled alternative in {@link RavelParser#primitive}.
	 * @param ctx the parse tree
	 */
	void exitController(RavelParser.ControllerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Views}
	 * labeled alternative in {@link RavelParser#primitive}.
	 * @param ctx the parse tree
	 */
	void enterViews(RavelParser.ViewsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Views}
	 * labeled alternative in {@link RavelParser#primitive}.
	 * @param ctx the parse tree
	 */
	void exitViews(RavelParser.ViewsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Transform}
	 * labeled alternative in {@link RavelParser#primitive}.
	 * @param ctx the parse tree
	 */
	void enterTransform(RavelParser.TransformContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Transform}
	 * labeled alternative in {@link RavelParser#primitive}.
	 * @param ctx the parse tree
	 */
	void exitTransform(RavelParser.TransformContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Space}
	 * labeled alternative in {@link RavelParser#primitive}.
	 * @param ctx the parse tree
	 */
	void enterSpace(RavelParser.SpaceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Space}
	 * labeled alternative in {@link RavelParser#primitive}.
	 * @param ctx the parse tree
	 */
	void exitSpace(RavelParser.SpaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#modelDecl}.
	 * @param ctx the parse tree
	 */
	void enterModelDecl(RavelParser.ModelDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#modelDecl}.
	 * @param ctx the parse tree
	 */
	void exitModelDecl(RavelParser.ModelDeclContext ctx);
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
	 * Enter a parse tree produced by the {@code ModelProp}
	 * labeled alternative in {@link RavelParser#modelBody}.
	 * @param ctx the parse tree
	 */
	void enterModelProp(RavelParser.ModelPropContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ModelProp}
	 * labeled alternative in {@link RavelParser#modelBody}.
	 * @param ctx the parse tree
	 */
	void exitModelProp(RavelParser.ModelPropContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ModelSchema}
	 * labeled alternative in {@link RavelParser#modelBody}.
	 * @param ctx the parse tree
	 */
	void enterModelSchema(RavelParser.ModelSchemaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ModelSchema}
	 * labeled alternative in {@link RavelParser#modelBody}.
	 * @param ctx the parse tree
	 */
	void exitModelSchema(RavelParser.ModelSchemaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PropertyPair}
	 * labeled alternative in {@link RavelParser#propBody}.
	 * @param ctx the parse tree
	 */
	void enterPropertyPair(RavelParser.PropertyPairContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PropertyPair}
	 * labeled alternative in {@link RavelParser#propBody}.
	 * @param ctx the parse tree
	 */
	void exitPropertyPair(RavelParser.PropertyPairContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SchemaPair}
	 * labeled alternative in {@link RavelParser#schemaBody}.
	 * @param ctx the parse tree
	 */
	void enterSchemaPair(RavelParser.SchemaPairContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SchemaPair}
	 * labeled alternative in {@link RavelParser#schemaBody}.
	 * @param ctx the parse tree
	 */
	void exitSchemaPair(RavelParser.SchemaPairContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#controllerDecl}.
	 * @param ctx the parse tree
	 */
	void enterControllerDecl(RavelParser.ControllerDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#controllerDecl}.
	 * @param ctx the parse tree
	 */
	void exitControllerDecl(RavelParser.ControllerDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#controllerType}.
	 * @param ctx the parse tree
	 */
	void enterControllerType(RavelParser.ControllerTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#controllerType}.
	 * @param ctx the parse tree
	 */
	void exitControllerType(RavelParser.ControllerTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#controllerBody}.
	 * @param ctx the parse tree
	 */
	void enterControllerBody(RavelParser.ControllerBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#controllerBody}.
	 * @param ctx the parse tree
	 */
	void exitControllerBody(RavelParser.ControllerBodyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConfigPair}
	 * labeled alternative in {@link RavelParser#configBody}.
	 * @param ctx the parse tree
	 */
	void enterConfigPair(RavelParser.ConfigPairContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConfigPair}
	 * labeled alternative in {@link RavelParser#configBody}.
	 * @param ctx the parse tree
	 */
	void exitConfigPair(RavelParser.ConfigPairContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EventStatement}
	 * labeled alternative in {@link RavelParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterEventStatement(RavelParser.EventStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EventStatement}
	 * labeled alternative in {@link RavelParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitEventStatement(RavelParser.EventStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CommandStatement}
	 * labeled alternative in {@link RavelParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCommandStatement(RavelParser.CommandStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CommandStatement}
	 * labeled alternative in {@link RavelParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCommandStatement(RavelParser.CommandStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Event}
	 * labeled alternative in {@link RavelParser#event_statement}.
	 * @param ctx the parse tree
	 */
	void enterEvent(RavelParser.EventContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Event}
	 * labeled alternative in {@link RavelParser#event_statement}.
	 * @param ctx the parse tree
	 */
	void exitEvent(RavelParser.EventContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Command}
	 * labeled alternative in {@link RavelParser#commnad}.
	 * @param ctx the parse tree
	 */
	void enterCommand(RavelParser.CommandContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Command}
	 * labeled alternative in {@link RavelParser#commnad}.
	 * @param ctx the parse tree
	 */
	void exitCommand(RavelParser.CommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#func_decl}.
	 * @param ctx the parse tree
	 */
	void enterFunc_decl(RavelParser.Func_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#func_decl}.
	 * @param ctx the parse tree
	 */
	void exitFunc_decl(RavelParser.Func_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#arg_list}.
	 * @param ctx the parse tree
	 */
	void enterArg_list(RavelParser.Arg_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#arg_list}.
	 * @param ctx the parse tree
	 */
	void exitArg_list(RavelParser.Arg_listContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Arg}
	 * labeled alternative in {@link RavelParser#args}.
	 * @param ctx the parse tree
	 */
	void enterArg(RavelParser.ArgContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Arg}
	 * labeled alternative in {@link RavelParser#args}.
	 * @param ctx the parse tree
	 */
	void exitArg(RavelParser.ArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#config_type}.
	 * @param ctx the parse tree
	 */
	void enterConfig_type(RavelParser.Config_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#config_type}.
	 * @param ctx the parse tree
	 */
	void exitConfig_type(RavelParser.Config_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#transformDecl}.
	 * @param ctx the parse tree
	 */
	void enterTransformDecl(RavelParser.TransformDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#transformDecl}.
	 * @param ctx the parse tree
	 */
	void exitTransformDecl(RavelParser.TransformDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#transformType}.
	 * @param ctx the parse tree
	 */
	void enterTransformType(RavelParser.TransformTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#transformType}.
	 * @param ctx the parse tree
	 */
	void exitTransformType(RavelParser.TransformTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TransConfig}
	 * labeled alternative in {@link RavelParser#transformBody}.
	 * @param ctx the parse tree
	 */
	void enterTransConfig(RavelParser.TransConfigContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TransConfig}
	 * labeled alternative in {@link RavelParser#transformBody}.
	 * @param ctx the parse tree
	 */
	void exitTransConfig(RavelParser.TransConfigContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TransCmd}
	 * labeled alternative in {@link RavelParser#transformBody}.
	 * @param ctx the parse tree
	 */
	void enterTransCmd(RavelParser.TransCmdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TransCmd}
	 * labeled alternative in {@link RavelParser#transformBody}.
	 * @param ctx the parse tree
	 */
	void exitTransCmd(RavelParser.TransCmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#viewDecl}.
	 * @param ctx the parse tree
	 */
	void enterViewDecl(RavelParser.ViewDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#viewDecl}.
	 * @param ctx the parse tree
	 */
	void exitViewDecl(RavelParser.ViewDeclContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ViewConfig}
	 * labeled alternative in {@link RavelParser#viewBody}.
	 * @param ctx the parse tree
	 */
	void enterViewConfig(RavelParser.ViewConfigContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ViewConfig}
	 * labeled alternative in {@link RavelParser#viewBody}.
	 * @param ctx the parse tree
	 */
	void exitViewConfig(RavelParser.ViewConfigContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ViewCmd}
	 * labeled alternative in {@link RavelParser#viewBody}.
	 * @param ctx the parse tree
	 */
	void enterViewCmd(RavelParser.ViewCmdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ViewCmd}
	 * labeled alternative in {@link RavelParser#viewBody}.
	 * @param ctx the parse tree
	 */
	void exitViewCmd(RavelParser.ViewCmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#spaceDecl}.
	 * @param ctx the parse tree
	 */
	void enterSpaceDecl(RavelParser.SpaceDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#spaceDecl}.
	 * @param ctx the parse tree
	 */
	void exitSpaceDecl(RavelParser.SpaceDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#spaceType}.
	 * @param ctx the parse tree
	 */
	void enterSpaceType(RavelParser.SpaceTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#spaceType}.
	 * @param ctx the parse tree
	 */
	void exitSpaceType(RavelParser.SpaceTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SpaceProp}
	 * labeled alternative in {@link RavelParser#spaceBody}.
	 * @param ctx the parse tree
	 */
	void enterSpaceProp(RavelParser.SpacePropContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SpaceProp}
	 * labeled alternative in {@link RavelParser#spaceBody}.
	 * @param ctx the parse tree
	 */
	void exitSpaceProp(RavelParser.SpacePropContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SpaceConfig}
	 * labeled alternative in {@link RavelParser#spaceBody}.
	 * @param ctx the parse tree
	 */
	void enterSpaceConfig(RavelParser.SpaceConfigContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SpaceConfig}
	 * labeled alternative in {@link RavelParser#spaceBody}.
	 * @param ctx the parse tree
	 */
	void exitSpaceConfig(RavelParser.SpaceConfigContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SpaceModels}
	 * labeled alternative in {@link RavelParser#spaceBody}.
	 * @param ctx the parse tree
	 */
	void enterSpaceModels(RavelParser.SpaceModelsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SpaceModels}
	 * labeled alternative in {@link RavelParser#spaceBody}.
	 * @param ctx the parse tree
	 */
	void exitSpaceModels(RavelParser.SpaceModelsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SpaceCntr}
	 * labeled alternative in {@link RavelParser#spaceBody}.
	 * @param ctx the parse tree
	 */
	void enterSpaceCntr(RavelParser.SpaceCntrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SpaceCntr}
	 * labeled alternative in {@link RavelParser#spaceBody}.
	 * @param ctx the parse tree
	 */
	void exitSpaceCntr(RavelParser.SpaceCntrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SpaceSinks}
	 * labeled alternative in {@link RavelParser#spaceBody}.
	 * @param ctx the parse tree
	 */
	void enterSpaceSinks(RavelParser.SpaceSinksContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SpaceSinks}
	 * labeled alternative in {@link RavelParser#spaceBody}.
	 * @param ctx the parse tree
	 */
	void exitSpaceSinks(RavelParser.SpaceSinksContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SpaceSrc}
	 * labeled alternative in {@link RavelParser#spaceBody}.
	 * @param ctx the parse tree
	 */
	void enterSpaceSrc(RavelParser.SpaceSrcContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SpaceSrc}
	 * labeled alternative in {@link RavelParser#spaceBody}.
	 * @param ctx the parse tree
	 */
	void exitSpaceSrc(RavelParser.SpaceSrcContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#pair}.
	 * @param ctx the parse tree
	 */
	void enterPair(RavelParser.PairContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#pair}.
	 * @param ctx the parse tree
	 */
	void exitPair(RavelParser.PairContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#array}.
	 * @param ctx the parse tree
	 */
	void enterArray(RavelParser.ArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#array}.
	 * @param ctx the parse tree
	 */
	void exitArray(RavelParser.ArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link RavelParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(RavelParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link RavelParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(RavelParser.ValueContext ctx);
}
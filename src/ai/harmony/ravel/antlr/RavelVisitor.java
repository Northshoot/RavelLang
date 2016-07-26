// Generated from /Users/lauril/workspace/01-ravel/RavelLang/Ravel.g4 by ANTLR 4.5.3
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
	 * Visit a parse tree produced by the {@code ModelDeclaration}
	 * labeled alternative in {@link RavelParser#modelDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelDeclaration(RavelParser.ModelDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(RavelParser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#modelType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelType(RavelParser.ModelTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#modelBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelBody(RavelParser.ModelBodyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ModelProperties}
	 * labeled alternative in {@link RavelParser#propDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelProperties(RavelParser.ModelPropertiesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PropertyDecl}
	 * labeled alternative in {@link RavelParser#property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyDecl(RavelParser.PropertyDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#schemaBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaBody(RavelParser.SchemaBodyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ModelSchema}
	 * labeled alternative in {@link RavelParser#schemaDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelSchema(RavelParser.ModelSchemaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FieldName}
	 * labeled alternative in {@link RavelParser#field_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldName(RavelParser.FieldNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FieldType}
	 * labeled alternative in {@link RavelParser#field_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldType(RavelParser.FieldTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#field}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitField(RavelParser.FieldContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FieldOpt}
	 * labeled alternative in {@link RavelParser#field_opt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldOpt(RavelParser.FieldOptContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SpaceDeclaration}
	 * labeled alternative in {@link RavelParser#spaceDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceDeclaration(RavelParser.SpaceDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#spaceBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceBody(RavelParser.SpaceBodyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SpaceConfig}
	 * labeled alternative in {@link RavelParser#spaceConf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceConfig(RavelParser.SpaceConfigContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SpaceModels}
	 * labeled alternative in {@link RavelParser#spaceModel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceModels(RavelParser.SpaceModelsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#pair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPair(RavelParser.PairContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelParser#end}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnd(RavelParser.EndContext ctx);
}
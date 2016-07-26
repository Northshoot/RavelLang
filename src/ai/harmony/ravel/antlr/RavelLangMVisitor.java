// Generated from /Users/lauril/workspace/01-ravel/RavelLang/RavelLangM.g4 by ANTLR 4.5.3
package ai.harmony.ravel.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link RavelLangMParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface RavelLangMVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link RavelLangMParser#file_input}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile_input(RavelLangMParser.File_inputContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ModelDeclaration}
	 * labeled alternative in {@link RavelLangMParser#modelDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelDeclaration(RavelLangMParser.ModelDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelLangMParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(RavelLangMParser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelLangMParser#modelType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelType(RavelLangMParser.ModelTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelLangMParser#modelBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelBody(RavelLangMParser.ModelBodyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ModelProperties}
	 * labeled alternative in {@link RavelLangMParser#propDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelProperties(RavelLangMParser.ModelPropertiesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PropertyDecl}
	 * labeled alternative in {@link RavelLangMParser#property}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyDecl(RavelLangMParser.PropertyDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelLangMParser#schemaBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchemaBody(RavelLangMParser.SchemaBodyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ModelSchema}
	 * labeled alternative in {@link RavelLangMParser#schemaDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModelSchema(RavelLangMParser.ModelSchemaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FieldName}
	 * labeled alternative in {@link RavelLangMParser#field_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldName(RavelLangMParser.FieldNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FieldType}
	 * labeled alternative in {@link RavelLangMParser#field_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldType(RavelLangMParser.FieldTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelLangMParser#field}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitField(RavelLangMParser.FieldContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FieldOpt}
	 * labeled alternative in {@link RavelLangMParser#field_opt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldOpt(RavelLangMParser.FieldOptContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SpaceDeclaration}
	 * labeled alternative in {@link RavelLangMParser#spaceDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceDeclaration(RavelLangMParser.SpaceDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelLangMParser#spaceBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceBody(RavelLangMParser.SpaceBodyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SpaceConfig}
	 * labeled alternative in {@link RavelLangMParser#spaceConf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceConfig(RavelLangMParser.SpaceConfigContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SpaceModels}
	 * labeled alternative in {@link RavelLangMParser#spaceModel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpaceModels(RavelLangMParser.SpaceModelsContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelLangMParser#pair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPair(RavelLangMParser.PairContext ctx);
	/**
	 * Visit a parse tree produced by {@link RavelLangMParser#end}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnd(RavelLangMParser.EndContext ctx);
}
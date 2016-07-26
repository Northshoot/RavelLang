// Generated from /Users/lauril/workspace/01-ravel/RavelLang/RavelLangM.g4 by ANTLR 4.5.3
package ai.harmony.ravel.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class RavelLangMParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, ID=16, INT=17, 
		NL=18, WS=19;
	public static final int
		RULE_file_input = 0, RULE_modelDecl = 1, RULE_name = 2, RULE_modelType = 3, 
		RULE_modelBody = 4, RULE_propDecl = 5, RULE_property = 6, RULE_schemaBody = 7, 
		RULE_schemaDecl = 8, RULE_field_name = 9, RULE_field_type = 10, RULE_field = 11, 
		RULE_field_opt = 12, RULE_spaceDecl = 13, RULE_spaceBody = 14, RULE_spaceConf = 15, 
		RULE_spaceModel = 16, RULE_pair = 17, RULE_end = 18;
	public static final String[] ruleNames = {
		"file_input", "modelDecl", "name", "modelType", "modelBody", "propDecl", 
		"property", "schemaBody", "schemaDecl", "field_name", "field_type", "field", 
		"field_opt", "spaceDecl", "spaceBody", "spaceConf", "spaceModel", "pair", 
		"end"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'model'", "':'", "'local'", "'streaming'", "'replicated'", "'properties:'", 
		"' = '", "'schema:'", "'('", "')'", "','", "'space'", "'configuration:'", 
		"'models:'", "'END'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, "ID", "INT", "NL", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Ravel.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public RavelLangMParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class File_inputContext extends ParserRuleContext {
		public List<ModelDeclContext> modelDecl() {
			return getRuleContexts(ModelDeclContext.class);
		}
		public ModelDeclContext modelDecl(int i) {
			return getRuleContext(ModelDeclContext.class,i);
		}
		public List<SpaceDeclContext> spaceDecl() {
			return getRuleContexts(SpaceDeclContext.class);
		}
		public SpaceDeclContext spaceDecl(int i) {
			return getRuleContext(SpaceDeclContext.class,i);
		}
		public File_inputContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_file_input; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RavelLangMVisitor ) return ((RavelLangMVisitor<? extends T>)visitor).visitFile_input(this);
			else return visitor.visitChildren(this);
		}
	}

	public final File_inputContext file_input() throws RecognitionException {
		File_inputContext _localctx = new File_inputContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_file_input);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(40);
				switch (_input.LA(1)) {
				case T__2:
				case T__3:
				case T__4:
					{
					setState(38);
					modelDecl();
					}
					break;
				case T__11:
					{
					setState(39);
					spaceDecl();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(42); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__11))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ModelDeclContext extends ParserRuleContext {
		public ModelDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modelDecl; }
	 
		public ModelDeclContext() { }
		public void copyFrom(ModelDeclContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ModelDeclarationContext extends ModelDeclContext {
		public ModelTypeContext modelType() {
			return getRuleContext(ModelTypeContext.class,0);
		}
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public ModelBodyContext modelBody() {
			return getRuleContext(ModelBodyContext.class,0);
		}
		public EndContext end() {
			return getRuleContext(EndContext.class,0);
		}
		public List<TerminalNode> NL() { return getTokens(RavelLangMParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(RavelLangMParser.NL, i);
		}
		public ModelDeclarationContext(ModelDeclContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RavelLangMVisitor ) return ((RavelLangMVisitor<? extends T>)visitor).visitModelDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModelDeclContext modelDecl() throws RecognitionException {
		ModelDeclContext _localctx = new ModelDeclContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_modelDecl);
		int _la;
		try {
			_localctx = new ModelDeclarationContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(44);
			modelType();
			setState(45);
			match(T__0);
			setState(46);
			name();
			setState(47);
			match(T__1);
			setState(51);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(48);
				match(NL);
				}
				}
				setState(53);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(54);
			modelBody();
			setState(58);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(55);
				match(NL);
				}
				}
				setState(60);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(61);
			end();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NameContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(RavelLangMParser.ID, 0); }
		public NameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_name; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RavelLangMVisitor ) return ((RavelLangMVisitor<? extends T>)visitor).visitName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NameContext name() throws RecognitionException {
		NameContext _localctx = new NameContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ModelTypeContext extends ParserRuleContext {
		public ModelTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modelType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RavelLangMVisitor ) return ((RavelLangMVisitor<? extends T>)visitor).visitModelType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModelTypeContext modelType() throws RecognitionException {
		ModelTypeContext _localctx = new ModelTypeContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_modelType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ModelBodyContext extends ParserRuleContext {
		public PropDeclContext propDecl() {
			return getRuleContext(PropDeclContext.class,0);
		}
		public SchemaDeclContext schemaDecl() {
			return getRuleContext(SchemaDeclContext.class,0);
		}
		public List<TerminalNode> NL() { return getTokens(RavelLangMParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(RavelLangMParser.NL, i);
		}
		public ModelBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modelBody; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RavelLangMVisitor ) return ((RavelLangMVisitor<? extends T>)visitor).visitModelBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModelBodyContext modelBody() throws RecognitionException {
		ModelBodyContext _localctx = new ModelBodyContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_modelBody);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(67);
			propDecl();
			setState(71);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(68);
				match(NL);
				}
				}
				setState(73);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(74);
			schemaDecl();
			setState(78);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(75);
					match(NL);
					}
					} 
				}
				setState(80);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropDeclContext extends ParserRuleContext {
		public PropDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propDecl; }
	 
		public PropDeclContext() { }
		public void copyFrom(PropDeclContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ModelPropertiesContext extends PropDeclContext {
		public TerminalNode NL() { return getToken(RavelLangMParser.NL, 0); }
		public EndContext end() {
			return getRuleContext(EndContext.class,0);
		}
		public List<PropertyContext> property() {
			return getRuleContexts(PropertyContext.class);
		}
		public PropertyContext property(int i) {
			return getRuleContext(PropertyContext.class,i);
		}
		public ModelPropertiesContext(PropDeclContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RavelLangMVisitor ) return ((RavelLangMVisitor<? extends T>)visitor).visitModelProperties(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropDeclContext propDecl() throws RecognitionException {
		PropDeclContext _localctx = new PropDeclContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_propDecl);
		int _la;
		try {
			_localctx = new ModelPropertiesContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			match(T__5);
			setState(82);
			match(NL);
			setState(84); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(83);
				property();
				}
				}
				setState(86); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==ID );
			setState(88);
			end();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertyContext extends ParserRuleContext {
		public PropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_property; }
	 
		public PropertyContext() { }
		public void copyFrom(PropertyContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class PropertyDeclContext extends PropertyContext {
		public List<TerminalNode> ID() { return getTokens(RavelLangMParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(RavelLangMParser.ID, i);
		}
		public TerminalNode INT() { return getToken(RavelLangMParser.INT, 0); }
		public List<TerminalNode> NL() { return getTokens(RavelLangMParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(RavelLangMParser.NL, i);
		}
		public PropertyDeclContext(PropertyContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RavelLangMVisitor ) return ((RavelLangMVisitor<? extends T>)visitor).visitPropertyDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyContext property() throws RecognitionException {
		PropertyContext _localctx = new PropertyContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_property);
		int _la;
		try {
			_localctx = new PropertyDeclContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			match(ID);
			setState(91);
			match(T__6);
			setState(92);
			_la = _input.LA(1);
			if ( !(_la==ID || _la==INT) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			setState(96);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(93);
				match(NL);
				}
				}
				setState(98);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SchemaBodyContext extends ParserRuleContext {
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public List<TerminalNode> NL() { return getTokens(RavelLangMParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(RavelLangMParser.NL, i);
		}
		public SchemaBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaBody; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RavelLangMVisitor ) return ((RavelLangMVisitor<? extends T>)visitor).visitSchemaBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaBodyContext schemaBody() throws RecognitionException {
		SchemaBodyContext _localctx = new SchemaBodyContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_schemaBody);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(106); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(99);
					field();
					setState(103);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(100);
							match(NL);
							}
							} 
						}
						setState(105);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(108); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SchemaDeclContext extends ParserRuleContext {
		public SchemaDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_schemaDecl; }
	 
		public SchemaDeclContext() { }
		public void copyFrom(SchemaDeclContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ModelSchemaContext extends SchemaDeclContext {
		public List<TerminalNode> NL() { return getTokens(RavelLangMParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(RavelLangMParser.NL, i);
		}
		public EndContext end() {
			return getRuleContext(EndContext.class,0);
		}
		public List<SchemaBodyContext> schemaBody() {
			return getRuleContexts(SchemaBodyContext.class);
		}
		public SchemaBodyContext schemaBody(int i) {
			return getRuleContext(SchemaBodyContext.class,i);
		}
		public ModelSchemaContext(SchemaDeclContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RavelLangMVisitor ) return ((RavelLangMVisitor<? extends T>)visitor).visitModelSchema(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemaDeclContext schemaDecl() throws RecognitionException {
		SchemaDeclContext _localctx = new SchemaDeclContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_schemaDecl);
		int _la;
		try {
			_localctx = new ModelSchemaContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			match(T__7);
			setState(114);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(111);
				match(NL);
				}
				}
				setState(116);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(118); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(117);
				schemaBody();
				}
				}
				setState(120); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==ID );
			setState(122);
			match(NL);
			setState(123);
			end();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Field_nameContext extends ParserRuleContext {
		public Field_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_field_name; }
	 
		public Field_nameContext() { }
		public void copyFrom(Field_nameContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class FieldNameContext extends Field_nameContext {
		public TerminalNode ID() { return getToken(RavelLangMParser.ID, 0); }
		public FieldNameContext(Field_nameContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RavelLangMVisitor ) return ((RavelLangMVisitor<? extends T>)visitor).visitFieldName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Field_nameContext field_name() throws RecognitionException {
		Field_nameContext _localctx = new Field_nameContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_field_name);
		try {
			_localctx = new FieldNameContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(125);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Field_typeContext extends ParserRuleContext {
		public Field_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_field_type; }
	 
		public Field_typeContext() { }
		public void copyFrom(Field_typeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class FieldTypeContext extends Field_typeContext {
		public TerminalNode ID() { return getToken(RavelLangMParser.ID, 0); }
		public FieldTypeContext(Field_typeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RavelLangMVisitor ) return ((RavelLangMVisitor<? extends T>)visitor).visitFieldType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Field_typeContext field_type() throws RecognitionException {
		Field_typeContext _localctx = new Field_typeContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_field_type);
		try {
			_localctx = new FieldTypeContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(127);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldContext extends ParserRuleContext {
		public Field_nameContext field_name() {
			return getRuleContext(Field_nameContext.class,0);
		}
		public Field_typeContext field_type() {
			return getRuleContext(Field_typeContext.class,0);
		}
		public Field_optContext field_opt() {
			return getRuleContext(Field_optContext.class,0);
		}
		public List<TerminalNode> NL() { return getTokens(RavelLangMParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(RavelLangMParser.NL, i);
		}
		public FieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_field; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RavelLangMVisitor ) return ((RavelLangMVisitor<? extends T>)visitor).visitField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldContext field() throws RecognitionException {
		FieldContext _localctx = new FieldContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_field);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
			field_name();
			setState(130);
			match(T__6);
			setState(131);
			field_type();
			setState(132);
			match(T__8);
			setState(134);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(133);
				field_opt();
				}
			}

			setState(136);
			match(T__9);
			setState(140);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(137);
					match(NL);
					}
					} 
				}
				setState(142);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Field_optContext extends ParserRuleContext {
		public Field_optContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_field_opt; }
	 
		public Field_optContext() { }
		public void copyFrom(Field_optContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class FieldOptContext extends Field_optContext {
		public List<PairContext> pair() {
			return getRuleContexts(PairContext.class);
		}
		public PairContext pair(int i) {
			return getRuleContext(PairContext.class,i);
		}
		public FieldOptContext(Field_optContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RavelLangMVisitor ) return ((RavelLangMVisitor<? extends T>)visitor).visitFieldOpt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Field_optContext field_opt() throws RecognitionException {
		Field_optContext _localctx = new Field_optContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_field_opt);
		int _la;
		try {
			_localctx = new FieldOptContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(143);
			pair();
			setState(148);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__10) {
				{
				{
				setState(144);
				match(T__10);
				setState(145);
				pair();
				}
				}
				setState(150);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SpaceDeclContext extends ParserRuleContext {
		public SpaceDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_spaceDecl; }
	 
		public SpaceDeclContext() { }
		public void copyFrom(SpaceDeclContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SpaceDeclarationContext extends SpaceDeclContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public SpaceBodyContext spaceBody() {
			return getRuleContext(SpaceBodyContext.class,0);
		}
		public EndContext end() {
			return getRuleContext(EndContext.class,0);
		}
		public List<TerminalNode> NL() { return getTokens(RavelLangMParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(RavelLangMParser.NL, i);
		}
		public SpaceDeclarationContext(SpaceDeclContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RavelLangMVisitor ) return ((RavelLangMVisitor<? extends T>)visitor).visitSpaceDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SpaceDeclContext spaceDecl() throws RecognitionException {
		SpaceDeclContext _localctx = new SpaceDeclContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_spaceDecl);
		int _la;
		try {
			_localctx = new SpaceDeclarationContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(151);
			match(T__11);
			setState(152);
			name();
			setState(153);
			match(T__1);
			setState(157);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(154);
				match(NL);
				}
				}
				setState(159);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(160);
			spaceBody();
			setState(164);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(161);
				match(NL);
				}
				}
				setState(166);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(167);
			end();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SpaceBodyContext extends ParserRuleContext {
		public PropDeclContext propDecl() {
			return getRuleContext(PropDeclContext.class,0);
		}
		public SpaceConfContext spaceConf() {
			return getRuleContext(SpaceConfContext.class,0);
		}
		public SpaceModelContext spaceModel() {
			return getRuleContext(SpaceModelContext.class,0);
		}
		public List<TerminalNode> NL() { return getTokens(RavelLangMParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(RavelLangMParser.NL, i);
		}
		public SpaceBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_spaceBody; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RavelLangMVisitor ) return ((RavelLangMVisitor<? extends T>)visitor).visitSpaceBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SpaceBodyContext spaceBody() throws RecognitionException {
		SpaceBodyContext _localctx = new SpaceBodyContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_spaceBody);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(169);
			propDecl();
			setState(173);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(170);
				match(NL);
				}
				}
				setState(175);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(176);
			spaceConf();
			setState(180);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(177);
				match(NL);
				}
				}
				setState(182);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(183);
			spaceModel();
			setState(187);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(184);
					match(NL);
					}
					} 
				}
				setState(189);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SpaceConfContext extends ParserRuleContext {
		public SpaceConfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_spaceConf; }
	 
		public SpaceConfContext() { }
		public void copyFrom(SpaceConfContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SpaceConfigContext extends SpaceConfContext {
		public TerminalNode NL() { return getToken(RavelLangMParser.NL, 0); }
		public EndContext end() {
			return getRuleContext(EndContext.class,0);
		}
		public List<PropertyContext> property() {
			return getRuleContexts(PropertyContext.class);
		}
		public PropertyContext property(int i) {
			return getRuleContext(PropertyContext.class,i);
		}
		public SpaceConfigContext(SpaceConfContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RavelLangMVisitor ) return ((RavelLangMVisitor<? extends T>)visitor).visitSpaceConfig(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SpaceConfContext spaceConf() throws RecognitionException {
		SpaceConfContext _localctx = new SpaceConfContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_spaceConf);
		int _la;
		try {
			_localctx = new SpaceConfigContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(190);
			match(T__12);
			setState(191);
			match(NL);
			setState(193); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(192);
				property();
				}
				}
				setState(195); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==ID );
			setState(197);
			end();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SpaceModelContext extends ParserRuleContext {
		public SpaceModelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_spaceModel; }
	 
		public SpaceModelContext() { }
		public void copyFrom(SpaceModelContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SpaceModelsContext extends SpaceModelContext {
		public TerminalNode NL() { return getToken(RavelLangMParser.NL, 0); }
		public EndContext end() {
			return getRuleContext(EndContext.class,0);
		}
		public List<PropertyContext> property() {
			return getRuleContexts(PropertyContext.class);
		}
		public PropertyContext property(int i) {
			return getRuleContext(PropertyContext.class,i);
		}
		public SpaceModelsContext(SpaceModelContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RavelLangMVisitor ) return ((RavelLangMVisitor<? extends T>)visitor).visitSpaceModels(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SpaceModelContext spaceModel() throws RecognitionException {
		SpaceModelContext _localctx = new SpaceModelContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_spaceModel);
		int _la;
		try {
			_localctx = new SpaceModelsContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(199);
			match(T__13);
			setState(200);
			match(NL);
			setState(202); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(201);
				property();
				}
				}
				setState(204); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==ID );
			setState(206);
			end();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PairContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(RavelLangMParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(RavelLangMParser.ID, i);
		}
		public TerminalNode INT() { return getToken(RavelLangMParser.INT, 0); }
		public PairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pair; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RavelLangMVisitor ) return ((RavelLangMVisitor<? extends T>)visitor).visitPair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PairContext pair() throws RecognitionException {
		PairContext _localctx = new PairContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_pair);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(208);
			match(ID);
			setState(209);
			match(T__6);
			setState(210);
			_la = _input.LA(1);
			if ( !(_la==ID || _la==INT) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EndContext extends ParserRuleContext {
		public EndContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_end; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RavelLangMVisitor ) return ((RavelLangMVisitor<? extends T>)visitor).visitEnd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EndContext end() throws RecognitionException {
		EndContext _localctx = new EndContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_end);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(212);
			match(T__14);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\25\u00d9\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\3\2\3\2\6\2+\n\2\r\2\16\2,\3\3\3\3\3\3\3\3\3\3\7"+
		"\3\64\n\3\f\3\16\3\67\13\3\3\3\3\3\7\3;\n\3\f\3\16\3>\13\3\3\3\3\3\3\4"+
		"\3\4\3\5\3\5\3\6\3\6\7\6H\n\6\f\6\16\6K\13\6\3\6\3\6\7\6O\n\6\f\6\16\6"+
		"R\13\6\3\7\3\7\3\7\6\7W\n\7\r\7\16\7X\3\7\3\7\3\b\3\b\3\b\3\b\7\ba\n\b"+
		"\f\b\16\bd\13\b\3\t\3\t\7\th\n\t\f\t\16\tk\13\t\6\tm\n\t\r\t\16\tn\3\n"+
		"\3\n\7\ns\n\n\f\n\16\nv\13\n\3\n\6\ny\n\n\r\n\16\nz\3\n\3\n\3\n\3\13\3"+
		"\13\3\f\3\f\3\r\3\r\3\r\3\r\3\r\5\r\u0089\n\r\3\r\3\r\7\r\u008d\n\r\f"+
		"\r\16\r\u0090\13\r\3\16\3\16\3\16\7\16\u0095\n\16\f\16\16\16\u0098\13"+
		"\16\3\17\3\17\3\17\3\17\7\17\u009e\n\17\f\17\16\17\u00a1\13\17\3\17\3"+
		"\17\7\17\u00a5\n\17\f\17\16\17\u00a8\13\17\3\17\3\17\3\20\3\20\7\20\u00ae"+
		"\n\20\f\20\16\20\u00b1\13\20\3\20\3\20\7\20\u00b5\n\20\f\20\16\20\u00b8"+
		"\13\20\3\20\3\20\7\20\u00bc\n\20\f\20\16\20\u00bf\13\20\3\21\3\21\3\21"+
		"\6\21\u00c4\n\21\r\21\16\21\u00c5\3\21\3\21\3\22\3\22\3\22\6\22\u00cd"+
		"\n\22\r\22\16\22\u00ce\3\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\24\2"+
		"\2\25\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&\2\4\3\2\5\7\3\2\22\23"+
		"\u00db\2*\3\2\2\2\4.\3\2\2\2\6A\3\2\2\2\bC\3\2\2\2\nE\3\2\2\2\fS\3\2\2"+
		"\2\16\\\3\2\2\2\20l\3\2\2\2\22p\3\2\2\2\24\177\3\2\2\2\26\u0081\3\2\2"+
		"\2\30\u0083\3\2\2\2\32\u0091\3\2\2\2\34\u0099\3\2\2\2\36\u00ab\3\2\2\2"+
		" \u00c0\3\2\2\2\"\u00c9\3\2\2\2$\u00d2\3\2\2\2&\u00d6\3\2\2\2(+\5\4\3"+
		"\2)+\5\34\17\2*(\3\2\2\2*)\3\2\2\2+,\3\2\2\2,*\3\2\2\2,-\3\2\2\2-\3\3"+
		"\2\2\2./\5\b\5\2/\60\7\3\2\2\60\61\5\6\4\2\61\65\7\4\2\2\62\64\7\24\2"+
		"\2\63\62\3\2\2\2\64\67\3\2\2\2\65\63\3\2\2\2\65\66\3\2\2\2\668\3\2\2\2"+
		"\67\65\3\2\2\28<\5\n\6\29;\7\24\2\2:9\3\2\2\2;>\3\2\2\2<:\3\2\2\2<=\3"+
		"\2\2\2=?\3\2\2\2><\3\2\2\2?@\5&\24\2@\5\3\2\2\2AB\7\22\2\2B\7\3\2\2\2"+
		"CD\t\2\2\2D\t\3\2\2\2EI\5\f\7\2FH\7\24\2\2GF\3\2\2\2HK\3\2\2\2IG\3\2\2"+
		"\2IJ\3\2\2\2JL\3\2\2\2KI\3\2\2\2LP\5\22\n\2MO\7\24\2\2NM\3\2\2\2OR\3\2"+
		"\2\2PN\3\2\2\2PQ\3\2\2\2Q\13\3\2\2\2RP\3\2\2\2ST\7\b\2\2TV\7\24\2\2UW"+
		"\5\16\b\2VU\3\2\2\2WX\3\2\2\2XV\3\2\2\2XY\3\2\2\2YZ\3\2\2\2Z[\5&\24\2"+
		"[\r\3\2\2\2\\]\7\22\2\2]^\7\t\2\2^b\t\3\2\2_a\7\24\2\2`_\3\2\2\2ad\3\2"+
		"\2\2b`\3\2\2\2bc\3\2\2\2c\17\3\2\2\2db\3\2\2\2ei\5\30\r\2fh\7\24\2\2g"+
		"f\3\2\2\2hk\3\2\2\2ig\3\2\2\2ij\3\2\2\2jm\3\2\2\2ki\3\2\2\2le\3\2\2\2"+
		"mn\3\2\2\2nl\3\2\2\2no\3\2\2\2o\21\3\2\2\2pt\7\n\2\2qs\7\24\2\2rq\3\2"+
		"\2\2sv\3\2\2\2tr\3\2\2\2tu\3\2\2\2ux\3\2\2\2vt\3\2\2\2wy\5\20\t\2xw\3"+
		"\2\2\2yz\3\2\2\2zx\3\2\2\2z{\3\2\2\2{|\3\2\2\2|}\7\24\2\2}~\5&\24\2~\23"+
		"\3\2\2\2\177\u0080\7\22\2\2\u0080\25\3\2\2\2\u0081\u0082\7\22\2\2\u0082"+
		"\27\3\2\2\2\u0083\u0084\5\24\13\2\u0084\u0085\7\t\2\2\u0085\u0086\5\26"+
		"\f\2\u0086\u0088\7\13\2\2\u0087\u0089\5\32\16\2\u0088\u0087\3\2\2\2\u0088"+
		"\u0089\3\2\2\2\u0089\u008a\3\2\2\2\u008a\u008e\7\f\2\2\u008b\u008d\7\24"+
		"\2\2\u008c\u008b\3\2\2\2\u008d\u0090\3\2\2\2\u008e\u008c\3\2\2\2\u008e"+
		"\u008f\3\2\2\2\u008f\31\3\2\2\2\u0090\u008e\3\2\2\2\u0091\u0096\5$\23"+
		"\2\u0092\u0093\7\r\2\2\u0093\u0095\5$\23\2\u0094\u0092\3\2\2\2\u0095\u0098"+
		"\3\2\2\2\u0096\u0094\3\2\2\2\u0096\u0097\3\2\2\2\u0097\33\3\2\2\2\u0098"+
		"\u0096\3\2\2\2\u0099\u009a\7\16\2\2\u009a\u009b\5\6\4\2\u009b\u009f\7"+
		"\4\2\2\u009c\u009e\7\24\2\2\u009d\u009c\3\2\2\2\u009e\u00a1\3\2\2\2\u009f"+
		"\u009d\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\u00a2\3\2\2\2\u00a1\u009f\3\2"+
		"\2\2\u00a2\u00a6\5\36\20\2\u00a3\u00a5\7\24\2\2\u00a4\u00a3\3\2\2\2\u00a5"+
		"\u00a8\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\u00a9\3\2"+
		"\2\2\u00a8\u00a6\3\2\2\2\u00a9\u00aa\5&\24\2\u00aa\35\3\2\2\2\u00ab\u00af"+
		"\5\f\7\2\u00ac\u00ae\7\24\2\2\u00ad\u00ac\3\2\2\2\u00ae\u00b1\3\2\2\2"+
		"\u00af\u00ad\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00b2\3\2\2\2\u00b1\u00af"+
		"\3\2\2\2\u00b2\u00b6\5 \21\2\u00b3\u00b5\7\24\2\2\u00b4\u00b3\3\2\2\2"+
		"\u00b5\u00b8\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00b9"+
		"\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b9\u00bd\5\"\22\2\u00ba\u00bc\7\24\2\2"+
		"\u00bb\u00ba\3\2\2\2\u00bc\u00bf\3\2\2\2\u00bd\u00bb\3\2\2\2\u00bd\u00be"+
		"\3\2\2\2\u00be\37\3\2\2\2\u00bf\u00bd\3\2\2\2\u00c0\u00c1\7\17\2\2\u00c1"+
		"\u00c3\7\24\2\2\u00c2\u00c4\5\16\b\2\u00c3\u00c2\3\2\2\2\u00c4\u00c5\3"+
		"\2\2\2\u00c5\u00c3\3\2\2\2\u00c5\u00c6\3\2\2\2\u00c6\u00c7\3\2\2\2\u00c7"+
		"\u00c8\5&\24\2\u00c8!\3\2\2\2\u00c9\u00ca\7\20\2\2\u00ca\u00cc\7\24\2"+
		"\2\u00cb\u00cd\5\16\b\2\u00cc\u00cb\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce"+
		"\u00cc\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00d0\3\2\2\2\u00d0\u00d1\5&"+
		"\24\2\u00d1#\3\2\2\2\u00d2\u00d3\7\22\2\2\u00d3\u00d4\7\t\2\2\u00d4\u00d5"+
		"\t\3\2\2\u00d5%\3\2\2\2\u00d6\u00d7\7\21\2\2\u00d7\'\3\2\2\2\30*,\65<"+
		"IPXbintz\u0088\u008e\u0096\u009f\u00a6\u00af\u00b6\u00bd\u00c5\u00ce";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
// Generated from /Users/lauril/workspace/01-ravel/RavelLang/Ravel.g4 by ANTLR 4.5.3
package ai.harmony.antlr4;

import ai.harmony.ravel.compiler.scope.*;
import ai.harmony.ravel.compiler.symbol.*;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class RavelLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, SELF=20, MODEL=21, SPACE=22, CONTROLLER=23, VIEW=24, 
		FLOW=25, EVENT=26, COMMAND=27, T_BYTE_FIELD=28, T_STRING_FIELD=29, T_BOOLEAN_FIELD=30, 
		T_INTEGER_FIELD=31, T_NUMBER_FIELD=32, T_DATE_FIELD=33, T_DATE_TIME_FIELD=34, 
		T_TIME_STAMP_FIELD=35, T_CONTEXT_FIELD=36, T_MODEL_FIELD=37, ASSERT=38, 
		RETURN=39, TRUE=40, FALSE=41, IF=42, ELIF=43, ELSE=44, FOR=45, WHILE=46, 
		AND=47, NOT=48, OR=49, IN=50, IS=51, DELETE=52, PASS=53, CONTINUE=54, 
		BREAK=55, NONE=56, NEWLINE=57, DECIMAL_INTEGER=58, NullLiteral=59, ASSIGN=60, 
		GT=61, LT=62, BANG=63, TILDE=64, QUESTION=65, COLON=66, EQUAL=67, LE=68, 
		GE=69, NOTEQUAL=70, AND_S=71, OR_S=72, INC=73, DEC=74, ADD=75, SUB=76, 
		MUL=77, DIV=78, BITAND=79, BITOR=80, CARET=81, MOD=82, ADD_ASSIGN=83, 
		SUB_ASSIGN=84, MUL_ASSIGN=85, DIV_ASSIGN=86, AND_ASSIGN=87, OR_ASSIGN=88, 
		XOR_ASSIGN=89, MOD_ASSIGN=90, LSHIFT_ASSIGN=91, RSHIFT_ASSIGN=92, URSHIFT_ASSIGN=93, 
		Identifier=94, SKIP_=95;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
		"T__17", "T__18", "SELF", "MODEL", "SPACE", "CONTROLLER", "VIEW", "FLOW", 
		"EVENT", "COMMAND", "T_BYTE_FIELD", "T_STRING_FIELD", "T_BOOLEAN_FIELD", 
		"T_INTEGER_FIELD", "T_NUMBER_FIELD", "T_DATE_FIELD", "T_DATE_TIME_FIELD", 
		"T_TIME_STAMP_FIELD", "T_CONTEXT_FIELD", "T_MODEL_FIELD", "ASSERT", "RETURN", 
		"TRUE", "FALSE", "IF", "ELIF", "ELSE", "FOR", "WHILE", "AND", "NOT", "OR", 
		"IN", "IS", "DELETE", "PASS", "CONTINUE", "BREAK", "NONE", "NEWLINE", 
		"DECIMAL_INTEGER", "NON_ZERO_DIGIT", "DIGIT", "POINT_FLOAT", "INT_PART", 
		"FRACTION", "NullLiteral", "ASSIGN", "GT", "LT", "BANG", "TILDE", "QUESTION", 
		"COLON", "EQUAL", "LE", "GE", "NOTEQUAL", "AND_S", "OR_S", "INC", "DEC", 
		"ADD", "SUB", "MUL", "DIV", "BITAND", "BITOR", "CARET", "MOD", "ADD_ASSIGN", 
		"SUB_ASSIGN", "MUL_ASSIGN", "DIV_ASSIGN", "AND_ASSIGN", "OR_ASSIGN", "XOR_ASSIGN", 
		"MOD_ASSIGN", "LSHIFT_ASSIGN", "RSHIFT_ASSIGN", "URSHIFT_ASSIGN", "Identifier", 
		"ID_START", "ID_CONTINUE", "SKIP_", "SPACES", "COMMENT", "LINE_JOINING"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'platform:'", "'models:'", "'('", "')'", "','", "'controllers:'", 
		"'sinks:'", "'sources:'", "'local'", "'streaming'", "'replicated'", "'properties:'", 
		"'['", "']'", "'schema:'", "'{'", "'}'", "'.'", "'\"'", "'self'", "'model'", 
		"'space'", "'controller'", "'view'", "'flow'", "'event'", "'command'", 
		"'ByteField'", "'StringField'", "'BooleanField'", "'IntegerField'", "'NumberField'", 
		"'DateField'", "'DateTimeField'", "'TimeStampField'", "'ContextField'", 
		"'ModelField'", "'assert'", "'return'", "'True'", "'False'", "'if'", "'else if'", 
		"'else'", "'for'", "'while'", "'and'", "'not'", "'or'", "'in'", "'is'", 
		"'delete'", "'pass'", "'continue'", "'break'", "'none'", null, null, "'null'", 
		"'='", "'>'", "'<'", "'!'", "'~'", "'?'", "':'", "'=='", "'<='", "'>='", 
		"'!='", "'&&'", "'||'", "'++'", "'--'", "'+'", "'-'", "'*'", "'/'", "'&'", 
		"'|'", "'^'", "'%'", "'+='", "'-='", "'*='", "'/='", "'&='", "'|='", "'^='", 
		"'%='", "'<<='", "'>>='", "'>>>='"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, "SELF", "MODEL", "SPACE", 
		"CONTROLLER", "VIEW", "FLOW", "EVENT", "COMMAND", "T_BYTE_FIELD", "T_STRING_FIELD", 
		"T_BOOLEAN_FIELD", "T_INTEGER_FIELD", "T_NUMBER_FIELD", "T_DATE_FIELD", 
		"T_DATE_TIME_FIELD", "T_TIME_STAMP_FIELD", "T_CONTEXT_FIELD", "T_MODEL_FIELD", 
		"ASSERT", "RETURN", "TRUE", "FALSE", "IF", "ELIF", "ELSE", "FOR", "WHILE", 
		"AND", "NOT", "OR", "IN", "IS", "DELETE", "PASS", "CONTINUE", "BREAK", 
		"NONE", "NEWLINE", "DECIMAL_INTEGER", "NullLiteral", "ASSIGN", "GT", "LT", 
		"BANG", "TILDE", "QUESTION", "COLON", "EQUAL", "LE", "GE", "NOTEQUAL", 
		"AND_S", "OR_S", "INC", "DEC", "ADD", "SUB", "MUL", "DIV", "BITAND", "BITOR", 
		"CARET", "MOD", "ADD_ASSIGN", "SUB_ASSIGN", "MUL_ASSIGN", "DIV_ASSIGN", 
		"AND_ASSIGN", "OR_ASSIGN", "XOR_ASSIGN", "MOD_ASSIGN", "LSHIFT_ASSIGN", 
		"RSHIFT_ASSIGN", "URSHIFT_ASSIGN", "Identifier", "SKIP_"
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



	  // A queue where extra tokens are pushed on (see the NEWLINE lexer rule).
	  private java.util.LinkedList<Token> tokens = new java.util.LinkedList<>();

	  // The stack that keeps track of the indentation level.
	  private java.util.Stack<Integer> indents = new java.util.Stack<>();

	  // The amount of opened braces, brackets and parenthesis.
	  private int opened = 0;

	  // The most recently produced token.
	  private Token lastToken = null;

	  @Override
	  public void emit(Token t) {
	    super.setToken(t);
	    tokens.offer(t);
	  }

	  @Override
	  public Token nextToken() {

	    // Check if the end-of-file is ahead and there are still some DEDENTS expected.
	    if (_input.LA(1) == EOF && !this.indents.isEmpty()) {

	      // Remove any trailing EOF tokens from our buffer.
	      for (int i = tokens.size() - 1; i >= 0; i--) {
	        if (tokens.get(i).getType() == EOF) {
	          tokens.remove(i);
	        }
	      }

	      // First emit an extra line break that serves as the end of the statement.
	      this.emit(commonToken(RavelParser.NEWLINE, "\n"));

	      // Now emit as much DEDENT tokens as needed.
	      while (!indents.isEmpty()) {
	        this.emit(createDedent());
	        indents.pop();
	      }

	      // Put the EOF back on the token stream.
	      this.emit(commonToken(RavelParser.EOF, "<EOF>"));
	    }

	    Token next = super.nextToken();

	    if (next.getChannel() == Token.DEFAULT_CHANNEL) {
	      // Keep track of the last token on the default channel.
	      this.lastToken = next;
	    }

	    return tokens.isEmpty() ? next : tokens.poll();
	  }

	  private Token createDedent() {
	    CommonToken dedent = commonToken(RavelParser.DEDENT, "");
	    dedent.setLine(this.lastToken.getLine());
	    return dedent;
	  }

	  private CommonToken commonToken(int type, String text) {
	    int stop = this.getCharIndex() - 1;
	    int start = text.isEmpty() ? stop : stop - text.length() + 1;
	    return new CommonToken(this._tokenFactorySourcePair, type, DEFAULT_TOKEN_CHANNEL, start, stop);
	  }

	  // Calculates the indentation of the provided spaces, taking the
	  // following rules into account:
	  //
	  // "Tabs are replaced (from left to right) by one to eight spaces
	  //  such that the total number of characters up to and including
	  //  the replacement is a multiple of eight [...]"
	  //
	  //  -- https://docs.python.org/3.1/reference/lexical_analysis.html#indentation
	  static int getIndentationCount(String spaces) {

	    int count = 0;

	    for (char ch : spaces.toCharArray()) {
	      switch (ch) {
	        case '\t':
	          count += 8 - (count % 8);
	          break;
	        default:
	          // A normal space char.
	          count++;
	      }
	    }

	    return count;
	  }

	  boolean atStartOfInput() {
	    return super.getCharPositionInLine() == 0 && super.getLine() == 1;
	  }


	public RavelLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Ravel.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 56:
			NEWLINE_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void NEWLINE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:

			     String newLine = getText().replaceAll("[^\r\n]+", "");
			     String spaces = getText().replaceAll("[\r\n]+", "");
			     int next = _input.LA(1);

			     if (opened > 0 || next == '\r' || next == '\n' || next == '/') {
			       // If we're inside a list or on a blank line, ignore all indents,
			       // dedents and line breaks.
			       skip();
			     }
			     else {
			       emit(commonToken(NEWLINE, newLine));

			       int indent = getIndentationCount(spaces);
			       int previous = indents.isEmpty() ? 0 : indents.peek();

			       if (indent == previous) {
			         // skip indents of the same size as the present indent-size
			         skip();
			       }
			       else if (indent > previous) {
			         indents.push(indent);
			         emit(commonToken(RavelParser.INDENT, spaces));
			       }
			       else {
			         // Possibly emit more than 1 DEDENT token.
			         while(!indents.isEmpty() && indents.peek() > indent) {
			           this.emit(createDedent());
			           indents.pop();
			         }
			       }
			     }
			   
			break;
		}
	}
	@Override
	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 56:
			return NEWLINE_sempred((RuleContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean NEWLINE_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return atStartOfInput();
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2a\u031c\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\4_\t_\4"+
		"`\t`\4a\ta\4b\tb\4c\tc\4d\td\4e\te\4f\tf\4g\tg\4h\th\4i\ti\4j\tj\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4"+
		"\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3"+
		"\20\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\25\3"+
		"\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3"+
		"\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3"+
		"\31\3\31\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\33\3\34\3"+
		"\34\3\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3"+
		"\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3"+
		"\36\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3"+
		" \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3"+
		"!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3#\3#\3#\3"+
		"#\3#\3#\3#\3#\3#\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3%\3%\3"+
		"%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3\'\3"+
		"\'\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3(\3(\3(\3)\3)\3)\3)\3)\3*\3*\3*\3"+
		"*\3*\3*\3+\3+\3+\3,\3,\3,\3,\3,\3,\3,\3,\3-\3-\3-\3-\3-\3.\3.\3.\3.\3"+
		"/\3/\3/\3/\3/\3/\3\60\3\60\3\60\3\60\3\61\3\61\3\61\3\61\3\62\3\62\3\62"+
		"\3\63\3\63\3\63\3\64\3\64\3\64\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\66"+
		"\3\66\3\66\3\66\3\66\3\67\3\67\3\67\3\67\3\67\3\67\3\67\3\67\3\67\38\3"+
		"8\38\38\38\38\39\39\39\39\39\3:\3:\3:\5:\u025c\n:\3:\3:\5:\u0260\n:\3"+
		":\5:\u0263\n:\5:\u0265\n:\3:\3:\3;\3;\7;\u026b\n;\f;\16;\u026e\13;\3;"+
		"\6;\u0271\n;\r;\16;\u0272\5;\u0275\n;\3<\3<\3=\3=\3>\5>\u027c\n>\3>\3"+
		">\3>\3>\5>\u0282\n>\3?\6?\u0285\n?\r?\16?\u0286\3@\3@\6@\u028b\n@\r@\16"+
		"@\u028c\3A\3A\3A\3A\3A\3B\3B\3C\3C\3D\3D\3E\3E\3F\3F\3G\3G\3H\3H\3I\3"+
		"I\3I\3J\3J\3J\3K\3K\3K\3L\3L\3L\3M\3M\3M\3N\3N\3N\3O\3O\3O\3P\3P\3P\3"+
		"Q\3Q\3R\3R\3S\3S\3T\3T\3U\3U\3V\3V\3W\3W\3X\3X\3Y\3Y\3Y\3Z\3Z\3Z\3[\3"+
		"[\3[\3\\\3\\\3\\\3]\3]\3]\3^\3^\3^\3_\3_\3_\3`\3`\3`\3a\3a\3a\3a\3b\3"+
		"b\3b\3b\3c\3c\3c\3c\3c\3d\3d\7d\u02f1\nd\fd\16d\u02f4\13d\3e\5e\u02f7"+
		"\ne\3f\3f\5f\u02fb\nf\3g\3g\3g\5g\u0300\ng\3g\3g\3h\6h\u0305\nh\rh\16"+
		"h\u0306\3i\3i\3i\3i\7i\u030d\ni\fi\16i\u0310\13i\3j\3j\5j\u0314\nj\3j"+
		"\5j\u0317\nj\3j\3j\5j\u031b\nj\2\2k\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n"+
		"\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30"+
		"/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.["+
		"/]\60_\61a\62c\63e\64g\65i\66k\67m8o9q:s;u<w\2y\2{\2}\2\177\2\u0081=\u0083"+
		">\u0085?\u0087@\u0089A\u008bB\u008dC\u008fD\u0091E\u0093F\u0095G\u0097"+
		"H\u0099I\u009bJ\u009dK\u009fL\u00a1M\u00a3N\u00a5O\u00a7P\u00a9Q\u00ab"+
		"R\u00adS\u00afT\u00b1U\u00b3V\u00b5W\u00b7X\u00b9Y\u00bbZ\u00bd[\u00bf"+
		"\\\u00c1]\u00c3^\u00c5_\u00c7`\u00c9\2\u00cb\2\u00cda\u00cf\2\u00d1\2"+
		"\u00d3\2\3\2\7\3\2\63;\3\2\62;\5\2C\\aac|\4\2\13\13\"\"\4\2\f\f\17\17"+
		"\u0325\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2"+
		"\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3"+
		"\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2"+
		"\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2"+
		"/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2"+
		"\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2"+
		"G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3"+
		"\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2"+
		"\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2"+
		"m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2\u0081\3\2\2\2"+
		"\2\u0083\3\2\2\2\2\u0085\3\2\2\2\2\u0087\3\2\2\2\2\u0089\3\2\2\2\2\u008b"+
		"\3\2\2\2\2\u008d\3\2\2\2\2\u008f\3\2\2\2\2\u0091\3\2\2\2\2\u0093\3\2\2"+
		"\2\2\u0095\3\2\2\2\2\u0097\3\2\2\2\2\u0099\3\2\2\2\2\u009b\3\2\2\2\2\u009d"+
		"\3\2\2\2\2\u009f\3\2\2\2\2\u00a1\3\2\2\2\2\u00a3\3\2\2\2\2\u00a5\3\2\2"+
		"\2\2\u00a7\3\2\2\2\2\u00a9\3\2\2\2\2\u00ab\3\2\2\2\2\u00ad\3\2\2\2\2\u00af"+
		"\3\2\2\2\2\u00b1\3\2\2\2\2\u00b3\3\2\2\2\2\u00b5\3\2\2\2\2\u00b7\3\2\2"+
		"\2\2\u00b9\3\2\2\2\2\u00bb\3\2\2\2\2\u00bd\3\2\2\2\2\u00bf\3\2\2\2\2\u00c1"+
		"\3\2\2\2\2\u00c3\3\2\2\2\2\u00c5\3\2\2\2\2\u00c7\3\2\2\2\2\u00cd\3\2\2"+
		"\2\3\u00d5\3\2\2\2\5\u00df\3\2\2\2\7\u00e7\3\2\2\2\t\u00e9\3\2\2\2\13"+
		"\u00eb\3\2\2\2\r\u00ed\3\2\2\2\17\u00fa\3\2\2\2\21\u0101\3\2\2\2\23\u010a"+
		"\3\2\2\2\25\u0110\3\2\2\2\27\u011a\3\2\2\2\31\u0125\3\2\2\2\33\u0131\3"+
		"\2\2\2\35\u0133\3\2\2\2\37\u0135\3\2\2\2!\u013d\3\2\2\2#\u013f\3\2\2\2"+
		"%\u0141\3\2\2\2\'\u0143\3\2\2\2)\u0145\3\2\2\2+\u014a\3\2\2\2-\u0150\3"+
		"\2\2\2/\u0156\3\2\2\2\61\u0161\3\2\2\2\63\u0166\3\2\2\2\65\u016b\3\2\2"+
		"\2\67\u0171\3\2\2\29\u0179\3\2\2\2;\u0183\3\2\2\2=\u018f\3\2\2\2?\u019c"+
		"\3\2\2\2A\u01a9\3\2\2\2C\u01b5\3\2\2\2E\u01bf\3\2\2\2G\u01cd\3\2\2\2I"+
		"\u01dc\3\2\2\2K\u01e9\3\2\2\2M\u01f4\3\2\2\2O\u01fb\3\2\2\2Q\u0202\3\2"+
		"\2\2S\u0207\3\2\2\2U\u020d\3\2\2\2W\u0210\3\2\2\2Y\u0218\3\2\2\2[\u021d"+
		"\3\2\2\2]\u0221\3\2\2\2_\u0227\3\2\2\2a\u022b\3\2\2\2c\u022f\3\2\2\2e"+
		"\u0232\3\2\2\2g\u0235\3\2\2\2i\u0238\3\2\2\2k\u023f\3\2\2\2m\u0244\3\2"+
		"\2\2o\u024d\3\2\2\2q\u0253\3\2\2\2s\u0264\3\2\2\2u\u0274\3\2\2\2w\u0276"+
		"\3\2\2\2y\u0278\3\2\2\2{\u0281\3\2\2\2}\u0284\3\2\2\2\177\u0288\3\2\2"+
		"\2\u0081\u028e\3\2\2\2\u0083\u0293\3\2\2\2\u0085\u0295\3\2\2\2\u0087\u0297"+
		"\3\2\2\2\u0089\u0299\3\2\2\2\u008b\u029b\3\2\2\2\u008d\u029d\3\2\2\2\u008f"+
		"\u029f\3\2\2\2\u0091\u02a1\3\2\2\2\u0093\u02a4\3\2\2\2\u0095\u02a7\3\2"+
		"\2\2\u0097\u02aa\3\2\2\2\u0099\u02ad\3\2\2\2\u009b\u02b0\3\2\2\2\u009d"+
		"\u02b3\3\2\2\2\u009f\u02b6\3\2\2\2\u00a1\u02b9\3\2\2\2\u00a3\u02bb\3\2"+
		"\2\2\u00a5\u02bd\3\2\2\2\u00a7\u02bf\3\2\2\2\u00a9\u02c1\3\2\2\2\u00ab"+
		"\u02c3\3\2\2\2\u00ad\u02c5\3\2\2\2\u00af\u02c7\3\2\2\2\u00b1\u02c9\3\2"+
		"\2\2\u00b3\u02cc\3\2\2\2\u00b5\u02cf\3\2\2\2\u00b7\u02d2\3\2\2\2\u00b9"+
		"\u02d5\3\2\2\2\u00bb\u02d8\3\2\2\2\u00bd\u02db\3\2\2\2\u00bf\u02de\3\2"+
		"\2\2\u00c1\u02e1\3\2\2\2\u00c3\u02e5\3\2\2\2\u00c5\u02e9\3\2\2\2\u00c7"+
		"\u02ee\3\2\2\2\u00c9\u02f6\3\2\2\2\u00cb\u02fa\3\2\2\2\u00cd\u02ff\3\2"+
		"\2\2\u00cf\u0304\3\2\2\2\u00d1\u0308\3\2\2\2\u00d3\u0311\3\2\2\2\u00d5"+
		"\u00d6\7r\2\2\u00d6\u00d7\7n\2\2\u00d7\u00d8\7c\2\2\u00d8\u00d9\7v\2\2"+
		"\u00d9\u00da\7h\2\2\u00da\u00db\7q\2\2\u00db\u00dc\7t\2\2\u00dc\u00dd"+
		"\7o\2\2\u00dd\u00de\7<\2\2\u00de\4\3\2\2\2\u00df\u00e0\7o\2\2\u00e0\u00e1"+
		"\7q\2\2\u00e1\u00e2\7f\2\2\u00e2\u00e3\7g\2\2\u00e3\u00e4\7n\2\2\u00e4"+
		"\u00e5\7u\2\2\u00e5\u00e6\7<\2\2\u00e6\6\3\2\2\2\u00e7\u00e8\7*\2\2\u00e8"+
		"\b\3\2\2\2\u00e9\u00ea\7+\2\2\u00ea\n\3\2\2\2\u00eb\u00ec\7.\2\2\u00ec"+
		"\f\3\2\2\2\u00ed\u00ee\7e\2\2\u00ee\u00ef\7q\2\2\u00ef\u00f0\7p\2\2\u00f0"+
		"\u00f1\7v\2\2\u00f1\u00f2\7t\2\2\u00f2\u00f3\7q\2\2\u00f3\u00f4\7n\2\2"+
		"\u00f4\u00f5\7n\2\2\u00f5\u00f6\7g\2\2\u00f6\u00f7\7t\2\2\u00f7\u00f8"+
		"\7u\2\2\u00f8\u00f9\7<\2\2\u00f9\16\3\2\2\2\u00fa\u00fb\7u\2\2\u00fb\u00fc"+
		"\7k\2\2\u00fc\u00fd\7p\2\2\u00fd\u00fe\7m\2\2\u00fe\u00ff\7u\2\2\u00ff"+
		"\u0100\7<\2\2\u0100\20\3\2\2\2\u0101\u0102\7u\2\2\u0102\u0103\7q\2\2\u0103"+
		"\u0104\7w\2\2\u0104\u0105\7t\2\2\u0105\u0106\7e\2\2\u0106\u0107\7g\2\2"+
		"\u0107\u0108\7u\2\2\u0108\u0109\7<\2\2\u0109\22\3\2\2\2\u010a\u010b\7"+
		"n\2\2\u010b\u010c\7q\2\2\u010c\u010d\7e\2\2\u010d\u010e\7c\2\2\u010e\u010f"+
		"\7n\2\2\u010f\24\3\2\2\2\u0110\u0111\7u\2\2\u0111\u0112\7v\2\2\u0112\u0113"+
		"\7t\2\2\u0113\u0114\7g\2\2\u0114\u0115\7c\2\2\u0115\u0116\7o\2\2\u0116"+
		"\u0117\7k\2\2\u0117\u0118\7p\2\2\u0118\u0119\7i\2\2\u0119\26\3\2\2\2\u011a"+
		"\u011b\7t\2\2\u011b\u011c\7g\2\2\u011c\u011d\7r\2\2\u011d\u011e\7n\2\2"+
		"\u011e\u011f\7k\2\2\u011f\u0120\7e\2\2\u0120\u0121\7c\2\2\u0121\u0122"+
		"\7v\2\2\u0122\u0123\7g\2\2\u0123\u0124\7f\2\2\u0124\30\3\2\2\2\u0125\u0126"+
		"\7r\2\2\u0126\u0127\7t\2\2\u0127\u0128\7q\2\2\u0128\u0129\7r\2\2\u0129"+
		"\u012a\7g\2\2\u012a\u012b\7t\2\2\u012b\u012c\7v\2\2\u012c\u012d\7k\2\2"+
		"\u012d\u012e\7g\2\2\u012e\u012f\7u\2\2\u012f\u0130\7<\2\2\u0130\32\3\2"+
		"\2\2\u0131\u0132\7]\2\2\u0132\34\3\2\2\2\u0133\u0134\7_\2\2\u0134\36\3"+
		"\2\2\2\u0135\u0136\7u\2\2\u0136\u0137\7e\2\2\u0137\u0138\7j\2\2\u0138"+
		"\u0139\7g\2\2\u0139\u013a\7o\2\2\u013a\u013b\7c\2\2\u013b\u013c\7<\2\2"+
		"\u013c \3\2\2\2\u013d\u013e\7}\2\2\u013e\"\3\2\2\2\u013f\u0140\7\177\2"+
		"\2\u0140$\3\2\2\2\u0141\u0142\7\60\2\2\u0142&\3\2\2\2\u0143\u0144\7$\2"+
		"\2\u0144(\3\2\2\2\u0145\u0146\7u\2\2\u0146\u0147\7g\2\2\u0147\u0148\7"+
		"n\2\2\u0148\u0149\7h\2\2\u0149*\3\2\2\2\u014a\u014b\7o\2\2\u014b\u014c"+
		"\7q\2\2\u014c\u014d\7f\2\2\u014d\u014e\7g\2\2\u014e\u014f\7n\2\2\u014f"+
		",\3\2\2\2\u0150\u0151\7u\2\2\u0151\u0152\7r\2\2\u0152\u0153\7c\2\2\u0153"+
		"\u0154\7e\2\2\u0154\u0155\7g\2\2\u0155.\3\2\2\2\u0156\u0157\7e\2\2\u0157"+
		"\u0158\7q\2\2\u0158\u0159\7p\2\2\u0159\u015a\7v\2\2\u015a\u015b\7t\2\2"+
		"\u015b\u015c\7q\2\2\u015c\u015d\7n\2\2\u015d\u015e\7n\2\2\u015e\u015f"+
		"\7g\2\2\u015f\u0160\7t\2\2\u0160\60\3\2\2\2\u0161\u0162\7x\2\2\u0162\u0163"+
		"\7k\2\2\u0163\u0164\7g\2\2\u0164\u0165\7y\2\2\u0165\62\3\2\2\2\u0166\u0167"+
		"\7h\2\2\u0167\u0168\7n\2\2\u0168\u0169\7q\2\2\u0169\u016a\7y\2\2\u016a"+
		"\64\3\2\2\2\u016b\u016c\7g\2\2\u016c\u016d\7x\2\2\u016d\u016e\7g\2\2\u016e"+
		"\u016f\7p\2\2\u016f\u0170\7v\2\2\u0170\66\3\2\2\2\u0171\u0172\7e\2\2\u0172"+
		"\u0173\7q\2\2\u0173\u0174\7o\2\2\u0174\u0175\7o\2\2\u0175\u0176\7c\2\2"+
		"\u0176\u0177\7p\2\2\u0177\u0178\7f\2\2\u01788\3\2\2\2\u0179\u017a\7D\2"+
		"\2\u017a\u017b\7{\2\2\u017b\u017c\7v\2\2\u017c\u017d\7g\2\2\u017d\u017e"+
		"\7H\2\2\u017e\u017f\7k\2\2\u017f\u0180\7g\2\2\u0180\u0181\7n\2\2\u0181"+
		"\u0182\7f\2\2\u0182:\3\2\2\2\u0183\u0184\7U\2\2\u0184\u0185\7v\2\2\u0185"+
		"\u0186\7t\2\2\u0186\u0187\7k\2\2\u0187\u0188\7p\2\2\u0188\u0189\7i\2\2"+
		"\u0189\u018a\7H\2\2\u018a\u018b\7k\2\2\u018b\u018c\7g\2\2\u018c\u018d"+
		"\7n\2\2\u018d\u018e\7f\2\2\u018e<\3\2\2\2\u018f\u0190\7D\2\2\u0190\u0191"+
		"\7q\2\2\u0191\u0192\7q\2\2\u0192\u0193\7n\2\2\u0193\u0194\7g\2\2\u0194"+
		"\u0195\7c\2\2\u0195\u0196\7p\2\2\u0196\u0197\7H\2\2\u0197\u0198\7k\2\2"+
		"\u0198\u0199\7g\2\2\u0199\u019a\7n\2\2\u019a\u019b\7f\2\2\u019b>\3\2\2"+
		"\2\u019c\u019d\7K\2\2\u019d\u019e\7p\2\2\u019e\u019f\7v\2\2\u019f\u01a0"+
		"\7g\2\2\u01a0\u01a1\7i\2\2\u01a1\u01a2\7g\2\2\u01a2\u01a3\7t\2\2\u01a3"+
		"\u01a4\7H\2\2\u01a4\u01a5\7k\2\2\u01a5\u01a6\7g\2\2\u01a6\u01a7\7n\2\2"+
		"\u01a7\u01a8\7f\2\2\u01a8@\3\2\2\2\u01a9\u01aa\7P\2\2\u01aa\u01ab\7w\2"+
		"\2\u01ab\u01ac\7o\2\2\u01ac\u01ad\7d\2\2\u01ad\u01ae\7g\2\2\u01ae\u01af"+
		"\7t\2\2\u01af\u01b0\7H\2\2\u01b0\u01b1\7k\2\2\u01b1\u01b2\7g\2\2\u01b2"+
		"\u01b3\7n\2\2\u01b3\u01b4\7f\2\2\u01b4B\3\2\2\2\u01b5\u01b6\7F\2\2\u01b6"+
		"\u01b7\7c\2\2\u01b7\u01b8\7v\2\2\u01b8\u01b9\7g\2\2\u01b9\u01ba\7H\2\2"+
		"\u01ba\u01bb\7k\2\2\u01bb\u01bc\7g\2\2\u01bc\u01bd\7n\2\2\u01bd\u01be"+
		"\7f\2\2\u01beD\3\2\2\2\u01bf\u01c0\7F\2\2\u01c0\u01c1\7c\2\2\u01c1\u01c2"+
		"\7v\2\2\u01c2\u01c3\7g\2\2\u01c3\u01c4\7V\2\2\u01c4\u01c5\7k\2\2\u01c5"+
		"\u01c6\7o\2\2\u01c6\u01c7\7g\2\2\u01c7\u01c8\7H\2\2\u01c8\u01c9\7k\2\2"+
		"\u01c9\u01ca\7g\2\2\u01ca\u01cb\7n\2\2\u01cb\u01cc\7f\2\2\u01ccF\3\2\2"+
		"\2\u01cd\u01ce\7V\2\2\u01ce\u01cf\7k\2\2\u01cf\u01d0\7o\2\2\u01d0\u01d1"+
		"\7g\2\2\u01d1\u01d2\7U\2\2\u01d2\u01d3\7v\2\2\u01d3\u01d4\7c\2\2\u01d4"+
		"\u01d5\7o\2\2\u01d5\u01d6\7r\2\2\u01d6\u01d7\7H\2\2\u01d7\u01d8\7k\2\2"+
		"\u01d8\u01d9\7g\2\2\u01d9\u01da\7n\2\2\u01da\u01db\7f\2\2\u01dbH\3\2\2"+
		"\2\u01dc\u01dd\7E\2\2\u01dd\u01de\7q\2\2\u01de\u01df\7p\2\2\u01df\u01e0"+
		"\7v\2\2\u01e0\u01e1\7g\2\2\u01e1\u01e2\7z\2\2\u01e2\u01e3\7v\2\2\u01e3"+
		"\u01e4\7H\2\2\u01e4\u01e5\7k\2\2\u01e5\u01e6\7g\2\2\u01e6\u01e7\7n\2\2"+
		"\u01e7\u01e8\7f\2\2\u01e8J\3\2\2\2\u01e9\u01ea\7O\2\2\u01ea\u01eb\7q\2"+
		"\2\u01eb\u01ec\7f\2\2\u01ec\u01ed\7g\2\2\u01ed\u01ee\7n\2\2\u01ee\u01ef"+
		"\7H\2\2\u01ef\u01f0\7k\2\2\u01f0\u01f1\7g\2\2\u01f1\u01f2\7n\2\2\u01f2"+
		"\u01f3\7f\2\2\u01f3L\3\2\2\2\u01f4\u01f5\7c\2\2\u01f5\u01f6\7u\2\2\u01f6"+
		"\u01f7\7u\2\2\u01f7\u01f8\7g\2\2\u01f8\u01f9\7t\2\2\u01f9\u01fa\7v\2\2"+
		"\u01faN\3\2\2\2\u01fb\u01fc\7t\2\2\u01fc\u01fd\7g\2\2\u01fd\u01fe\7v\2"+
		"\2\u01fe\u01ff\7w\2\2\u01ff\u0200\7t\2\2\u0200\u0201\7p\2\2\u0201P\3\2"+
		"\2\2\u0202\u0203\7V\2\2\u0203\u0204\7t\2\2\u0204\u0205\7w\2\2\u0205\u0206"+
		"\7g\2\2\u0206R\3\2\2\2\u0207\u0208\7H\2\2\u0208\u0209\7c\2\2\u0209\u020a"+
		"\7n\2\2\u020a\u020b\7u\2\2\u020b\u020c\7g\2\2\u020cT\3\2\2\2\u020d\u020e"+
		"\7k\2\2\u020e\u020f\7h\2\2\u020fV\3\2\2\2\u0210\u0211\7g\2\2\u0211\u0212"+
		"\7n\2\2\u0212\u0213\7u\2\2\u0213\u0214\7g\2\2\u0214\u0215\7\"\2\2\u0215"+
		"\u0216\7k\2\2\u0216\u0217\7h\2\2\u0217X\3\2\2\2\u0218\u0219\7g\2\2\u0219"+
		"\u021a\7n\2\2\u021a\u021b\7u\2\2\u021b\u021c\7g\2\2\u021cZ\3\2\2\2\u021d"+
		"\u021e\7h\2\2\u021e\u021f\7q\2\2\u021f\u0220\7t\2\2\u0220\\\3\2\2\2\u0221"+
		"\u0222\7y\2\2\u0222\u0223\7j\2\2\u0223\u0224\7k\2\2\u0224\u0225\7n\2\2"+
		"\u0225\u0226\7g\2\2\u0226^\3\2\2\2\u0227\u0228\7c\2\2\u0228\u0229\7p\2"+
		"\2\u0229\u022a\7f\2\2\u022a`\3\2\2\2\u022b\u022c\7p\2\2\u022c\u022d\7"+
		"q\2\2\u022d\u022e\7v\2\2\u022eb\3\2\2\2\u022f\u0230\7q\2\2\u0230\u0231"+
		"\7t\2\2\u0231d\3\2\2\2\u0232\u0233\7k\2\2\u0233\u0234\7p\2\2\u0234f\3"+
		"\2\2\2\u0235\u0236\7k\2\2\u0236\u0237\7u\2\2\u0237h\3\2\2\2\u0238\u0239"+
		"\7f\2\2\u0239\u023a\7g\2\2\u023a\u023b\7n\2\2\u023b\u023c\7g\2\2\u023c"+
		"\u023d\7v\2\2\u023d\u023e\7g\2\2\u023ej\3\2\2\2\u023f\u0240\7r\2\2\u0240"+
		"\u0241\7c\2\2\u0241\u0242\7u\2\2\u0242\u0243\7u\2\2\u0243l\3\2\2\2\u0244"+
		"\u0245\7e\2\2\u0245\u0246\7q\2\2\u0246\u0247\7p\2\2\u0247\u0248\7v\2\2"+
		"\u0248\u0249\7k\2\2\u0249\u024a\7p\2\2\u024a\u024b\7w\2\2\u024b\u024c"+
		"\7g\2\2\u024cn\3\2\2\2\u024d\u024e\7d\2\2\u024e\u024f\7t\2\2\u024f\u0250"+
		"\7g\2\2\u0250\u0251\7c\2\2\u0251\u0252\7m\2\2\u0252p\3\2\2\2\u0253\u0254"+
		"\7p\2\2\u0254\u0255\7q\2\2\u0255\u0256\7p\2\2\u0256\u0257\7g\2\2\u0257"+
		"r\3\2\2\2\u0258\u0259\6:\2\2\u0259\u0265\5\u00cfh\2\u025a\u025c\7\17\2"+
		"\2\u025b\u025a\3\2\2\2\u025b\u025c\3\2\2\2\u025c\u025d\3\2\2\2\u025d\u0260"+
		"\7\f\2\2\u025e\u0260\7\17\2\2\u025f\u025b\3\2\2\2\u025f\u025e\3\2\2\2"+
		"\u0260\u0262\3\2\2\2\u0261\u0263\5\u00cfh\2\u0262\u0261\3\2\2\2\u0262"+
		"\u0263\3\2\2\2\u0263\u0265\3\2\2\2\u0264\u0258\3\2\2\2\u0264\u025f\3\2"+
		"\2\2\u0265\u0266\3\2\2\2\u0266\u0267\b:\2\2\u0267t\3\2\2\2\u0268\u026c"+
		"\5w<\2\u0269\u026b\5y=\2\u026a\u0269\3\2\2\2\u026b\u026e\3\2\2\2\u026c"+
		"\u026a\3\2\2\2\u026c\u026d\3\2\2\2\u026d\u0275\3\2\2\2\u026e\u026c\3\2"+
		"\2\2\u026f\u0271\7\62\2\2\u0270\u026f\3\2\2\2\u0271\u0272\3\2\2\2\u0272"+
		"\u0270\3\2\2\2\u0272\u0273\3\2\2\2\u0273\u0275\3\2\2\2\u0274\u0268\3\2"+
		"\2\2\u0274\u0270\3\2\2\2\u0275v\3\2\2\2\u0276\u0277\t\2\2\2\u0277x\3\2"+
		"\2\2\u0278\u0279\t\3\2\2\u0279z\3\2\2\2\u027a\u027c\5}?\2\u027b\u027a"+
		"\3\2\2\2\u027b\u027c\3\2\2\2\u027c\u027d\3\2\2\2\u027d\u0282\5\177@\2"+
		"\u027e\u027f\5}?\2\u027f\u0280\7\60\2\2\u0280\u0282\3\2\2\2\u0281\u027b"+
		"\3\2\2\2\u0281\u027e\3\2\2\2\u0282|\3\2\2\2\u0283\u0285\5y=\2\u0284\u0283"+
		"\3\2\2\2\u0285\u0286\3\2\2\2\u0286\u0284\3\2\2\2\u0286\u0287\3\2\2\2\u0287"+
		"~\3\2\2\2\u0288\u028a\7\60\2\2\u0289\u028b\5y=\2\u028a\u0289\3\2\2\2\u028b"+
		"\u028c\3\2\2\2\u028c\u028a\3\2\2\2\u028c\u028d\3\2\2\2\u028d\u0080\3\2"+
		"\2\2\u028e\u028f\7p\2\2\u028f\u0290\7w\2\2\u0290\u0291\7n\2\2\u0291\u0292"+
		"\7n\2\2\u0292\u0082\3\2\2\2\u0293\u0294\7?\2\2\u0294\u0084\3\2\2\2\u0295"+
		"\u0296\7@\2\2\u0296\u0086\3\2\2\2\u0297\u0298\7>\2\2\u0298\u0088\3\2\2"+
		"\2\u0299\u029a\7#\2\2\u029a\u008a\3\2\2\2\u029b\u029c\7\u0080\2\2\u029c"+
		"\u008c\3\2\2\2\u029d\u029e\7A\2\2\u029e\u008e\3\2\2\2\u029f\u02a0\7<\2"+
		"\2\u02a0\u0090\3\2\2\2\u02a1\u02a2\7?\2\2\u02a2\u02a3\7?\2\2\u02a3\u0092"+
		"\3\2\2\2\u02a4\u02a5\7>\2\2\u02a5\u02a6\7?\2\2\u02a6\u0094\3\2\2\2\u02a7"+
		"\u02a8\7@\2\2\u02a8\u02a9\7?\2\2\u02a9\u0096\3\2\2\2\u02aa\u02ab\7#\2"+
		"\2\u02ab\u02ac\7?\2\2\u02ac\u0098\3\2\2\2\u02ad\u02ae\7(\2\2\u02ae\u02af"+
		"\7(\2\2\u02af\u009a\3\2\2\2\u02b0\u02b1\7~\2\2\u02b1\u02b2\7~\2\2\u02b2"+
		"\u009c\3\2\2\2\u02b3\u02b4\7-\2\2\u02b4\u02b5\7-\2\2\u02b5\u009e\3\2\2"+
		"\2\u02b6\u02b7\7/\2\2\u02b7\u02b8\7/\2\2\u02b8\u00a0\3\2\2\2\u02b9\u02ba"+
		"\7-\2\2\u02ba\u00a2\3\2\2\2\u02bb\u02bc\7/\2\2\u02bc\u00a4\3\2\2\2\u02bd"+
		"\u02be\7,\2\2\u02be\u00a6\3\2\2\2\u02bf\u02c0\7\61\2\2\u02c0\u00a8\3\2"+
		"\2\2\u02c1\u02c2\7(\2\2\u02c2\u00aa\3\2\2\2\u02c3\u02c4\7~\2\2\u02c4\u00ac"+
		"\3\2\2\2\u02c5\u02c6\7`\2\2\u02c6\u00ae\3\2\2\2\u02c7\u02c8\7\'\2\2\u02c8"+
		"\u00b0\3\2\2\2\u02c9\u02ca\7-\2\2\u02ca\u02cb\7?\2\2\u02cb\u00b2\3\2\2"+
		"\2\u02cc\u02cd\7/\2\2\u02cd\u02ce\7?\2\2\u02ce\u00b4\3\2\2\2\u02cf\u02d0"+
		"\7,\2\2\u02d0\u02d1\7?\2\2\u02d1\u00b6\3\2\2\2\u02d2\u02d3\7\61\2\2\u02d3"+
		"\u02d4\7?\2\2\u02d4\u00b8\3\2\2\2\u02d5\u02d6\7(\2\2\u02d6\u02d7\7?\2"+
		"\2\u02d7\u00ba\3\2\2\2\u02d8\u02d9\7~\2\2\u02d9\u02da\7?\2\2\u02da\u00bc"+
		"\3\2\2\2\u02db\u02dc\7`\2\2\u02dc\u02dd\7?\2\2\u02dd\u00be\3\2\2\2\u02de"+
		"\u02df\7\'\2\2\u02df\u02e0\7?\2\2\u02e0\u00c0\3\2\2\2\u02e1\u02e2\7>\2"+
		"\2\u02e2\u02e3\7>\2\2\u02e3\u02e4\7?\2\2\u02e4\u00c2\3\2\2\2\u02e5\u02e6"+
		"\7@\2\2\u02e6\u02e7\7@\2\2\u02e7\u02e8\7?\2\2\u02e8\u00c4\3\2\2\2\u02e9"+
		"\u02ea\7@\2\2\u02ea\u02eb\7@\2\2\u02eb\u02ec\7@\2\2\u02ec\u02ed\7?\2\2"+
		"\u02ed\u00c6\3\2\2\2\u02ee\u02f2\5\u00c9e\2\u02ef\u02f1\5\u00cbf\2\u02f0"+
		"\u02ef\3\2\2\2\u02f1\u02f4\3\2\2\2\u02f2\u02f0\3\2\2\2\u02f2\u02f3\3\2"+
		"\2\2\u02f3\u00c8\3\2\2\2\u02f4\u02f2\3\2\2\2\u02f5\u02f7\t\4\2\2\u02f6"+
		"\u02f5\3\2\2\2\u02f7\u00ca\3\2\2\2\u02f8\u02fb\5\u00c9e\2\u02f9\u02fb"+
		"\t\3\2\2\u02fa\u02f8\3\2\2\2\u02fa\u02f9\3\2\2\2\u02fb\u00cc\3\2\2\2\u02fc"+
		"\u0300\5\u00cfh\2\u02fd\u0300\5\u00d1i\2\u02fe\u0300\5\u00d3j\2\u02ff"+
		"\u02fc\3\2\2\2\u02ff\u02fd\3\2\2\2\u02ff\u02fe\3\2\2\2\u0300\u0301\3\2"+
		"\2\2\u0301\u0302\bg\3\2\u0302\u00ce\3\2\2\2\u0303\u0305\t\5\2\2\u0304"+
		"\u0303\3\2\2\2\u0305\u0306\3\2\2\2\u0306\u0304\3\2\2\2\u0306\u0307\3\2"+
		"\2\2\u0307\u00d0\3\2\2\2\u0308\u0309\7\61\2\2\u0309\u030a\7\61\2\2\u030a"+
		"\u030e\3\2\2\2\u030b\u030d\n\6\2\2\u030c\u030b\3\2\2\2\u030d\u0310\3\2"+
		"\2\2\u030e\u030c\3\2\2\2\u030e\u030f\3\2\2\2\u030f\u00d2\3\2\2\2\u0310"+
		"\u030e\3\2\2\2\u0311\u0313\7^\2\2\u0312\u0314\5\u00cfh\2\u0313\u0312\3"+
		"\2\2\2\u0313\u0314\3\2\2\2\u0314\u031a\3\2\2\2\u0315\u0317\7\17\2\2\u0316"+
		"\u0315\3\2\2\2\u0316\u0317\3\2\2\2\u0317\u0318\3\2\2\2\u0318\u031b\7\f"+
		"\2\2\u0319\u031b\7\17\2\2\u031a\u0316\3\2\2\2\u031a\u0319\3\2\2\2\u031b"+
		"\u00d4\3\2\2\2\27\2\u025b\u025f\u0262\u0264\u026c\u0272\u0274\u027b\u0281"+
		"\u0286\u028c\u02f2\u02f6\u02fa\u02ff\u0306\u030e\u0313\u0316\u031a\4\3"+
		":\2\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
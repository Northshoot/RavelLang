// Generated from /Users/lauril/workspace/01-ravel/RavelLang/RavelLexer.g4 by ANTLR 4.5.3
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
		INDENT=1, DEDENT=2, INT=3, MODEL=4, SPACE=5, CONTROLLER=6, VIEW=7, FLOW=8, 
		LOCAL=9, STREAMING=10, REPLICATED=11, PROPERTIES=12, DURABLE=13, RELIABLE=14, 
		ENCRYPTON=15, CONFIGURATION=16, SCHEMA=17, PLATFORM=18, MODELS=19, CONTROLLERS=20, 
		SINKS=21, SOURCES=22, TEMPLATES=23, LANGUAGE=24, CLANG=25, JLANG=26, PLANG=27, 
		EVENT=28, COMMAND=29, RETURN=30, DELETE=31, TRUE=32, FALSE=33, LAST=34, 
		FIRST=35, GET=36, T_INTEGER=37, T_NUMBER=38, T_BOOL=39, T_BYTE_FIELD=40, 
		T_STRING_FIELD=41, T_BOOLEAN_FIELD=42, T_INTEGER_FIELD=43, T_NUMBER_FIELD=44, 
		T_DATE_FIELD=45, T_DATE_TIME_FIELD=46, T_TIME_STAMP_FIELD=47, API_V=48, 
		BLOCKSTART=49, EQUAL=50, PLUS=51, MINUS=52, DOT=53, COMMA=54, LEFT_BRACKET=55, 
		RIGHT_BRACKET=56, DOUBLE_APPOS=57, NAME=58, NEWLINE=59, SKIP_=60;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"INT", "MODEL", "SPACE", "CONTROLLER", "VIEW", "FLOW", "LOCAL", "STREAMING", 
		"REPLICATED", "PROPERTIES", "DURABLE", "RELIABLE", "ENCRYPTON", "CONFIGURATION", 
		"SCHEMA", "PLATFORM", "MODELS", "CONTROLLERS", "SINKS", "SOURCES", "TEMPLATES", 
		"LANGUAGE", "CLANG", "JLANG", "PLANG", "EVENT", "COMMAND", "RETURN", "DELETE", 
		"TRUE", "FALSE", "LAST", "FIRST", "GET", "T_INTEGER", "T_NUMBER", "T_BOOL", 
		"T_BYTE_FIELD", "T_STRING_FIELD", "T_BOOLEAN_FIELD", "T_INTEGER_FIELD", 
		"T_NUMBER_FIELD", "T_DATE_FIELD", "T_DATE_TIME_FIELD", "T_TIME_STAMP_FIELD", 
		"API_V", "BLOCKSTART", "EQUAL", "PLUS", "MINUS", "DOT", "COMMA", "LEFT_BRACKET", 
		"RIGHT_BRACKET", "DOUBLE_APPOS", "NAME", "SPACES", "NEWLINE", "SKIP_", 
		"COMMENT", "ID_START", "ID_CONTINUE"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, null, null, "'model'", "'space'", "'controller'", "'view'", 
		"'flow'", "'local'", "'streaming'", "'replicated'", "'properties'", "'durable'", 
		"'reliable'", "'encryption'", "'configuration'", "'schema'", "'platform'", 
		"'models'", "'controllers'", "'sinks'", "'sources'", "'templates'", "'language'", 
		"'clang'", "'java'", "'python'", "'event'", "'command'", "'return'", "'delete'", 
		"'true'", "'false'", "'last'", "'first'", "'get'", "'integer'", "'number'", 
		"'boolean'", "'ByteField'", "'StringField'", "'Boolean'", null, null, 
		null, null, "'TimestampField'", "'api.'", "':'", "'='", "'+'", "'-'", 
		"'.'", "','", "'('", "')'", "'\"'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "INDENT", "DEDENT", "INT", "MODEL", "SPACE", "CONTROLLER", "VIEW", 
		"FLOW", "LOCAL", "STREAMING", "REPLICATED", "PROPERTIES", "DURABLE", "RELIABLE", 
		"ENCRYPTON", "CONFIGURATION", "SCHEMA", "PLATFORM", "MODELS", "CONTROLLERS", 
		"SINKS", "SOURCES", "TEMPLATES", "LANGUAGE", "CLANG", "JLANG", "PLANG", 
		"EVENT", "COMMAND", "RETURN", "DELETE", "TRUE", "FALSE", "LAST", "FIRST", 
		"GET", "T_INTEGER", "T_NUMBER", "T_BOOL", "T_BYTE_FIELD", "T_STRING_FIELD", 
		"T_BOOLEAN_FIELD", "T_INTEGER_FIELD", "T_NUMBER_FIELD", "T_DATE_FIELD", 
		"T_DATE_TIME_FIELD", "T_TIME_STAMP_FIELD", "API_V", "BLOCKSTART", "EQUAL", 
		"PLUS", "MINUS", "DOT", "COMMA", "LEFT_BRACKET", "RIGHT_BRACKET", "DOUBLE_APPOS", 
		"NAME", "NEWLINE", "SKIP_"
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


	   //from Ter
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
	public String getGrammarFileName() { return "RavelLexer.g4"; }

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
		case 57:
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

			     if (opened > 0 || next == '\r' || next == '\n' || next == '#') {
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
		case 57:
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2>\u0242\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\3\2\6\2\u0081\n\2\r\2\16\2\u0082\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27"+
		"\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\31"+
		"\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33"+
		"\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35"+
		"\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\37\3\37\3\37"+
		"\3\37\3\37\3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3"+
		"#\3#\3#\3#\3$\3$\3$\3$\3$\3$\3$\3$\3%\3%\3%\3%\3%\3%\3%\3&\3&\3&\3&\3"+
		"&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3(\3(\3"+
		"(\3(\3(\3(\3(\3(\3)\3)\3)\3)\3)\3)\3)\3)\3*\3*\3*\3*\3*\3*\3*\3*\3*\3"+
		"*\3*\3*\3*\3+\3+\3+\3+\3+\3+\3+\3+\3+\3+\3+\3+\3+\3,\3,\3,\3,\3,\3,\3"+
		",\3,\3,\3,\3,\3,\3,\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3.\3.\3.\3"+
		".\3.\3.\3.\3.\3.\3.\3.\3.\3.\3.\3.\3/\3/\3/\3/\3/\3\60\3\60\3\61\3\61"+
		"\3\62\3\62\3\63\3\63\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3\67\38\38\39"+
		"\39\79\u0213\n9\f9\169\u0216\139\3:\6:\u0219\n:\r:\16:\u021a\3;\3;\3;"+
		"\5;\u0220\n;\3;\3;\5;\u0224\n;\3;\5;\u0227\n;\5;\u0229\n;\3;\3;\3<\3<"+
		"\5<\u022f\n<\3<\3<\3=\3=\3=\3=\7=\u0237\n=\f=\16=\u023a\13=\3>\5>\u023d"+
		"\n>\3?\3?\5?\u0241\n?\2\2@\3\5\5\6\7\7\t\b\13\t\r\n\17\13\21\f\23\r\25"+
		"\16\27\17\31\20\33\21\35\22\37\23!\24#\25%\26\'\27)\30+\31-\32/\33\61"+
		"\34\63\35\65\36\67\379 ;!=\"?#A$C%E&G\'I(K)M*O+Q,S-U.W/Y\60[\61]\62_\63"+
		"a\64c\65e\66g\67i8k9m:o;q<s\2u=w>y\2{\2}\2\3\2\6\3\2\62;\4\2\13\13\"\""+
		"\4\2\f\f\17\17\5\2C\\aac|\u0247\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2"+
		"\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2"+
		"\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2"+
		"\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2"+
		"\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2"+
		"\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2"+
		"\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O"+
		"\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2"+
		"\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2"+
		"\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2u\3\2\2\2\2w"+
		"\3\2\2\2\3\u0080\3\2\2\2\5\u0084\3\2\2\2\7\u008a\3\2\2\2\t\u0090\3\2\2"+
		"\2\13\u009b\3\2\2\2\r\u00a0\3\2\2\2\17\u00a5\3\2\2\2\21\u00ab\3\2\2\2"+
		"\23\u00b5\3\2\2\2\25\u00c0\3\2\2\2\27\u00cb\3\2\2\2\31\u00d3\3\2\2\2\33"+
		"\u00dc\3\2\2\2\35\u00e7\3\2\2\2\37\u00f5\3\2\2\2!\u00fc\3\2\2\2#\u0105"+
		"\3\2\2\2%\u010c\3\2\2\2\'\u0118\3\2\2\2)\u011e\3\2\2\2+\u0126\3\2\2\2"+
		"-\u0130\3\2\2\2/\u0139\3\2\2\2\61\u013f\3\2\2\2\63\u0144\3\2\2\2\65\u014b"+
		"\3\2\2\2\67\u0151\3\2\2\29\u0159\3\2\2\2;\u0160\3\2\2\2=\u0167\3\2\2\2"+
		"?\u016c\3\2\2\2A\u0172\3\2\2\2C\u0177\3\2\2\2E\u017d\3\2\2\2G\u0181\3"+
		"\2\2\2I\u0189\3\2\2\2K\u0190\3\2\2\2M\u0198\3\2\2\2O\u01a2\3\2\2\2Q\u01ae"+
		"\3\2\2\2S\u01b6\3\2\2\2U\u01c3\3\2\2\2W\u01d0\3\2\2\2Y\u01dd\3\2\2\2["+
		"\u01ea\3\2\2\2]\u01f9\3\2\2\2_\u01fe\3\2\2\2a\u0200\3\2\2\2c\u0202\3\2"+
		"\2\2e\u0204\3\2\2\2g\u0206\3\2\2\2i\u0208\3\2\2\2k\u020a\3\2\2\2m\u020c"+
		"\3\2\2\2o\u020e\3\2\2\2q\u0210\3\2\2\2s\u0218\3\2\2\2u\u0228\3\2\2\2w"+
		"\u022e\3\2\2\2y\u0232\3\2\2\2{\u023c\3\2\2\2}\u0240\3\2\2\2\177\u0081"+
		"\t\2\2\2\u0080\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082\u0080\3\2\2\2\u0082"+
		"\u0083\3\2\2\2\u0083\4\3\2\2\2\u0084\u0085\7o\2\2\u0085\u0086\7q\2\2\u0086"+
		"\u0087\7f\2\2\u0087\u0088\7g\2\2\u0088\u0089\7n\2\2\u0089\6\3\2\2\2\u008a"+
		"\u008b\7u\2\2\u008b\u008c\7r\2\2\u008c\u008d\7c\2\2\u008d\u008e\7e\2\2"+
		"\u008e\u008f\7g\2\2\u008f\b\3\2\2\2\u0090\u0091\7e\2\2\u0091\u0092\7q"+
		"\2\2\u0092\u0093\7p\2\2\u0093\u0094\7v\2\2\u0094\u0095\7t\2\2\u0095\u0096"+
		"\7q\2\2\u0096\u0097\7n\2\2\u0097\u0098\7n\2\2\u0098\u0099\7g\2\2\u0099"+
		"\u009a\7t\2\2\u009a\n\3\2\2\2\u009b\u009c\7x\2\2\u009c\u009d\7k\2\2\u009d"+
		"\u009e\7g\2\2\u009e\u009f\7y\2\2\u009f\f\3\2\2\2\u00a0\u00a1\7h\2\2\u00a1"+
		"\u00a2\7n\2\2\u00a2\u00a3\7q\2\2\u00a3\u00a4\7y\2\2\u00a4\16\3\2\2\2\u00a5"+
		"\u00a6\7n\2\2\u00a6\u00a7\7q\2\2\u00a7\u00a8\7e\2\2\u00a8\u00a9\7c\2\2"+
		"\u00a9\u00aa\7n\2\2\u00aa\20\3\2\2\2\u00ab\u00ac\7u\2\2\u00ac\u00ad\7"+
		"v\2\2\u00ad\u00ae\7t\2\2\u00ae\u00af\7g\2\2\u00af\u00b0\7c\2\2\u00b0\u00b1"+
		"\7o\2\2\u00b1\u00b2\7k\2\2\u00b2\u00b3\7p\2\2\u00b3\u00b4\7i\2\2\u00b4"+
		"\22\3\2\2\2\u00b5\u00b6\7t\2\2\u00b6\u00b7\7g\2\2\u00b7\u00b8\7r\2\2\u00b8"+
		"\u00b9\7n\2\2\u00b9\u00ba\7k\2\2\u00ba\u00bb\7e\2\2\u00bb\u00bc\7c\2\2"+
		"\u00bc\u00bd\7v\2\2\u00bd\u00be\7g\2\2\u00be\u00bf\7f\2\2\u00bf\24\3\2"+
		"\2\2\u00c0\u00c1\7r\2\2\u00c1\u00c2\7t\2\2\u00c2\u00c3\7q\2\2\u00c3\u00c4"+
		"\7r\2\2\u00c4\u00c5\7g\2\2\u00c5\u00c6\7t\2\2\u00c6\u00c7\7v\2\2\u00c7"+
		"\u00c8\7k\2\2\u00c8\u00c9\7g\2\2\u00c9\u00ca\7u\2\2\u00ca\26\3\2\2\2\u00cb"+
		"\u00cc\7f\2\2\u00cc\u00cd\7w\2\2\u00cd\u00ce\7t\2\2\u00ce\u00cf\7c\2\2"+
		"\u00cf\u00d0\7d\2\2\u00d0\u00d1\7n\2\2\u00d1\u00d2\7g\2\2\u00d2\30\3\2"+
		"\2\2\u00d3\u00d4\7t\2\2\u00d4\u00d5\7g\2\2\u00d5\u00d6\7n\2\2\u00d6\u00d7"+
		"\7k\2\2\u00d7\u00d8\7c\2\2\u00d8\u00d9\7d\2\2\u00d9\u00da\7n\2\2\u00da"+
		"\u00db\7g\2\2\u00db\32\3\2\2\2\u00dc\u00dd\7g\2\2\u00dd\u00de\7p\2\2\u00de"+
		"\u00df\7e\2\2\u00df\u00e0\7t\2\2\u00e0\u00e1\7{\2\2\u00e1\u00e2\7r\2\2"+
		"\u00e2\u00e3\7v\2\2\u00e3\u00e4\7k\2\2\u00e4\u00e5\7q\2\2\u00e5\u00e6"+
		"\7p\2\2\u00e6\34\3\2\2\2\u00e7\u00e8\7e\2\2\u00e8\u00e9\7q\2\2\u00e9\u00ea"+
		"\7p\2\2\u00ea\u00eb\7h\2\2\u00eb\u00ec\7k\2\2\u00ec\u00ed\7i\2\2\u00ed"+
		"\u00ee\7w\2\2\u00ee\u00ef\7t\2\2\u00ef\u00f0\7c\2\2\u00f0\u00f1\7v\2\2"+
		"\u00f1\u00f2\7k\2\2\u00f2\u00f3\7q\2\2\u00f3\u00f4\7p\2\2\u00f4\36\3\2"+
		"\2\2\u00f5\u00f6\7u\2\2\u00f6\u00f7\7e\2\2\u00f7\u00f8\7j\2\2\u00f8\u00f9"+
		"\7g\2\2\u00f9\u00fa\7o\2\2\u00fa\u00fb\7c\2\2\u00fb \3\2\2\2\u00fc\u00fd"+
		"\7r\2\2\u00fd\u00fe\7n\2\2\u00fe\u00ff\7c\2\2\u00ff\u0100\7v\2\2\u0100"+
		"\u0101\7h\2\2\u0101\u0102\7q\2\2\u0102\u0103\7t\2\2\u0103\u0104\7o\2\2"+
		"\u0104\"\3\2\2\2\u0105\u0106\7o\2\2\u0106\u0107\7q\2\2\u0107\u0108\7f"+
		"\2\2\u0108\u0109\7g\2\2\u0109\u010a\7n\2\2\u010a\u010b\7u\2\2\u010b$\3"+
		"\2\2\2\u010c\u010d\7e\2\2\u010d\u010e\7q\2\2\u010e\u010f\7p\2\2\u010f"+
		"\u0110\7v\2\2\u0110\u0111\7t\2\2\u0111\u0112\7q\2\2\u0112\u0113\7n\2\2"+
		"\u0113\u0114\7n\2\2\u0114\u0115\7g\2\2\u0115\u0116\7t\2\2\u0116\u0117"+
		"\7u\2\2\u0117&\3\2\2\2\u0118\u0119\7u\2\2\u0119\u011a\7k\2\2\u011a\u011b"+
		"\7p\2\2\u011b\u011c\7m\2\2\u011c\u011d\7u\2\2\u011d(\3\2\2\2\u011e\u011f"+
		"\7u\2\2\u011f\u0120\7q\2\2\u0120\u0121\7w\2\2\u0121\u0122\7t\2\2\u0122"+
		"\u0123\7e\2\2\u0123\u0124\7g\2\2\u0124\u0125\7u\2\2\u0125*\3\2\2\2\u0126"+
		"\u0127\7v\2\2\u0127\u0128\7g\2\2\u0128\u0129\7o\2\2\u0129\u012a\7r\2\2"+
		"\u012a\u012b\7n\2\2\u012b\u012c\7c\2\2\u012c\u012d\7v\2\2\u012d\u012e"+
		"\7g\2\2\u012e\u012f\7u\2\2\u012f,\3\2\2\2\u0130\u0131\7n\2\2\u0131\u0132"+
		"\7c\2\2\u0132\u0133\7p\2\2\u0133\u0134\7i\2\2\u0134\u0135\7w\2\2\u0135"+
		"\u0136\7c\2\2\u0136\u0137\7i\2\2\u0137\u0138\7g\2\2\u0138.\3\2\2\2\u0139"+
		"\u013a\7e\2\2\u013a\u013b\7n\2\2\u013b\u013c\7c\2\2\u013c\u013d\7p\2\2"+
		"\u013d\u013e\7i\2\2\u013e\60\3\2\2\2\u013f\u0140\7l\2\2\u0140\u0141\7"+
		"c\2\2\u0141\u0142\7x\2\2\u0142\u0143\7c\2\2\u0143\62\3\2\2\2\u0144\u0145"+
		"\7r\2\2\u0145\u0146\7{\2\2\u0146\u0147\7v\2\2\u0147\u0148\7j\2\2\u0148"+
		"\u0149\7q\2\2\u0149\u014a\7p\2\2\u014a\64\3\2\2\2\u014b\u014c\7g\2\2\u014c"+
		"\u014d\7x\2\2\u014d\u014e\7g\2\2\u014e\u014f\7p\2\2\u014f\u0150\7v\2\2"+
		"\u0150\66\3\2\2\2\u0151\u0152\7e\2\2\u0152\u0153\7q\2\2\u0153\u0154\7"+
		"o\2\2\u0154\u0155\7o\2\2\u0155\u0156\7c\2\2\u0156\u0157\7p\2\2\u0157\u0158"+
		"\7f\2\2\u01588\3\2\2\2\u0159\u015a\7t\2\2\u015a\u015b\7g\2\2\u015b\u015c"+
		"\7v\2\2\u015c\u015d\7w\2\2\u015d\u015e\7t\2\2\u015e\u015f\7p\2\2\u015f"+
		":\3\2\2\2\u0160\u0161\7f\2\2\u0161\u0162\7g\2\2\u0162\u0163\7n\2\2\u0163"+
		"\u0164\7g\2\2\u0164\u0165\7v\2\2\u0165\u0166\7g\2\2\u0166<\3\2\2\2\u0167"+
		"\u0168\7v\2\2\u0168\u0169\7t\2\2\u0169\u016a\7w\2\2\u016a\u016b\7g\2\2"+
		"\u016b>\3\2\2\2\u016c\u016d\7h\2\2\u016d\u016e\7c\2\2\u016e\u016f\7n\2"+
		"\2\u016f\u0170\7u\2\2\u0170\u0171\7g\2\2\u0171@\3\2\2\2\u0172\u0173\7"+
		"n\2\2\u0173\u0174\7c\2\2\u0174\u0175\7u\2\2\u0175\u0176\7v\2\2\u0176B"+
		"\3\2\2\2\u0177\u0178\7h\2\2\u0178\u0179\7k\2\2\u0179\u017a\7t\2\2\u017a"+
		"\u017b\7u\2\2\u017b\u017c\7v\2\2\u017cD\3\2\2\2\u017d\u017e\7i\2\2\u017e"+
		"\u017f\7g\2\2\u017f\u0180\7v\2\2\u0180F\3\2\2\2\u0181\u0182\7k\2\2\u0182"+
		"\u0183\7p\2\2\u0183\u0184\7v\2\2\u0184\u0185\7g\2\2\u0185\u0186\7i\2\2"+
		"\u0186\u0187\7g\2\2\u0187\u0188\7t\2\2\u0188H\3\2\2\2\u0189\u018a\7p\2"+
		"\2\u018a\u018b\7w\2\2\u018b\u018c\7o\2\2\u018c\u018d\7d\2\2\u018d\u018e"+
		"\7g\2\2\u018e\u018f\7t\2\2\u018fJ\3\2\2\2\u0190\u0191\7d\2\2\u0191\u0192"+
		"\7q\2\2\u0192\u0193\7q\2\2\u0193\u0194\7n\2\2\u0194\u0195\7g\2\2\u0195"+
		"\u0196\7c\2\2\u0196\u0197\7p\2\2\u0197L\3\2\2\2\u0198\u0199\7D\2\2\u0199"+
		"\u019a\7{\2\2\u019a\u019b\7v\2\2\u019b\u019c\7g\2\2\u019c\u019d\7H\2\2"+
		"\u019d\u019e\7k\2\2\u019e\u019f\7g\2\2\u019f\u01a0\7n\2\2\u01a0\u01a1"+
		"\7f\2\2\u01a1N\3\2\2\2\u01a2\u01a3\7U\2\2\u01a3\u01a4\7v\2\2\u01a4\u01a5"+
		"\7t\2\2\u01a5\u01a6\7k\2\2\u01a6\u01a7\7p\2\2\u01a7\u01a8\7i\2\2\u01a8"+
		"\u01a9\7H\2\2\u01a9\u01aa\7k\2\2\u01aa\u01ab\7g\2\2\u01ab\u01ac\7n\2\2"+
		"\u01ac\u01ad\7f\2\2\u01adP\3\2\2\2\u01ae\u01af\7D\2\2\u01af\u01b0\7q\2"+
		"\2\u01b0\u01b1\7q\2\2\u01b1\u01b2\7n\2\2\u01b2\u01b3\7g\2\2\u01b3\u01b4"+
		"\7c\2\2\u01b4\u01b5\7p\2\2\u01b5R\3\2\2\2\u01b6\u01b7\7K\2\2\u01b7\u01b8"+
		"\7p\2\2\u01b8\u01b9\7v\2\2\u01b9\u01ba\7g\2\2\u01ba\u01bb\7i\2\2\u01bb"+
		"\u01bc\7g\2\2\u01bc\u01bd\7t\2\2\u01bd\u01be\7H\2\2\u01be\u01bf\7k\2\2"+
		"\u01bf\u01c0\7g\2\2\u01c0\u01c1\7n\2\2\u01c1\u01c2\7f\2\2\u01c2T\3\2\2"+
		"\2\u01c3\u01c4\7K\2\2\u01c4\u01c5\7p\2\2\u01c5\u01c6\7v\2\2\u01c6\u01c7"+
		"\7g\2\2\u01c7\u01c8\7i\2\2\u01c8\u01c9\7g\2\2\u01c9\u01ca\7t\2\2\u01ca"+
		"\u01cb\7H\2\2\u01cb\u01cc\7k\2\2\u01cc\u01cd\7g\2\2\u01cd\u01ce\7n\2\2"+
		"\u01ce\u01cf\7f\2\2\u01cfV\3\2\2\2\u01d0\u01d1\7K\2\2\u01d1\u01d2\7p\2"+
		"\2\u01d2\u01d3\7v\2\2\u01d3\u01d4\7g\2\2\u01d4\u01d5\7i\2\2\u01d5\u01d6"+
		"\7g\2\2\u01d6\u01d7\7t\2\2\u01d7\u01d8\7H\2\2\u01d8\u01d9\7k\2\2\u01d9"+
		"\u01da\7g\2\2\u01da\u01db\7n\2\2\u01db\u01dc\7f\2\2\u01dcX\3\2\2\2\u01dd"+
		"\u01de\7K\2\2\u01de\u01df\7p\2\2\u01df\u01e0\7v\2\2\u01e0\u01e1\7g\2\2"+
		"\u01e1\u01e2\7i\2\2\u01e2\u01e3\7g\2\2\u01e3\u01e4\7t\2\2\u01e4\u01e5"+
		"\7H\2\2\u01e5\u01e6\7k\2\2\u01e6\u01e7\7g\2\2\u01e7\u01e8\7n\2\2\u01e8"+
		"\u01e9\7f\2\2\u01e9Z\3\2\2\2\u01ea\u01eb\7V\2\2\u01eb\u01ec\7k\2\2\u01ec"+
		"\u01ed\7o\2\2\u01ed\u01ee\7g\2\2\u01ee\u01ef\7u\2\2\u01ef\u01f0\7v\2\2"+
		"\u01f0\u01f1\7c\2\2\u01f1\u01f2\7o\2\2\u01f2\u01f3\7r\2\2\u01f3\u01f4"+
		"\7H\2\2\u01f4\u01f5\7k\2\2\u01f5\u01f6\7g\2\2\u01f6\u01f7\7n\2\2\u01f7"+
		"\u01f8\7f\2\2\u01f8\\\3\2\2\2\u01f9\u01fa\7c\2\2\u01fa\u01fb\7r\2\2\u01fb"+
		"\u01fc\7k\2\2\u01fc\u01fd\7\60\2\2\u01fd^\3\2\2\2\u01fe\u01ff\7<\2\2\u01ff"+
		"`\3\2\2\2\u0200\u0201\7?\2\2\u0201b\3\2\2\2\u0202\u0203\7-\2\2\u0203d"+
		"\3\2\2\2\u0204\u0205\7/\2\2\u0205f\3\2\2\2\u0206\u0207\7\60\2\2\u0207"+
		"h\3\2\2\2\u0208\u0209\7.\2\2\u0209j\3\2\2\2\u020a\u020b\7*\2\2\u020bl"+
		"\3\2\2\2\u020c\u020d\7+\2\2\u020dn\3\2\2\2\u020e\u020f\7$\2\2\u020fp\3"+
		"\2\2\2\u0210\u0214\5{>\2\u0211\u0213\5}?\2\u0212\u0211\3\2\2\2\u0213\u0216"+
		"\3\2\2\2\u0214\u0212\3\2\2\2\u0214\u0215\3\2\2\2\u0215r\3\2\2\2\u0216"+
		"\u0214\3\2\2\2\u0217\u0219\t\3\2\2\u0218\u0217\3\2\2\2\u0219\u021a\3\2"+
		"\2\2\u021a\u0218\3\2\2\2\u021a\u021b\3\2\2\2\u021bt\3\2\2\2\u021c\u021d"+
		"\6;\2\2\u021d\u0229\5s:\2\u021e\u0220\7\17\2\2\u021f\u021e\3\2\2\2\u021f"+
		"\u0220\3\2\2\2\u0220\u0221\3\2\2\2\u0221\u0224\7\f\2\2\u0222\u0224\7\17"+
		"\2\2\u0223\u021f\3\2\2\2\u0223\u0222\3\2\2\2\u0224\u0226\3\2\2\2\u0225"+
		"\u0227\5s:\2\u0226\u0225\3\2\2\2\u0226\u0227\3\2\2\2\u0227\u0229\3\2\2"+
		"\2\u0228\u021c\3\2\2\2\u0228\u0223\3\2\2\2\u0229\u022a\3\2\2\2\u022a\u022b"+
		"\b;\2\2\u022bv\3\2\2\2\u022c\u022f\5s:\2\u022d\u022f\5y=\2\u022e\u022c"+
		"\3\2\2\2\u022e\u022d\3\2\2\2\u022f\u0230\3\2\2\2\u0230\u0231\b<\3\2\u0231"+
		"x\3\2\2\2\u0232\u0233\7\61\2\2\u0233\u0234\7\61\2\2\u0234\u0238\3\2\2"+
		"\2\u0235\u0237\n\4\2\2\u0236\u0235\3\2\2\2\u0237\u023a\3\2\2\2\u0238\u0236"+
		"\3\2\2\2\u0238\u0239\3\2\2\2\u0239z\3\2\2\2\u023a\u0238\3\2\2\2\u023b"+
		"\u023d\t\5\2\2\u023c\u023b\3\2\2\2\u023d|\3\2\2\2\u023e\u0241\5{>\2\u023f"+
		"\u0241\t\2\2\2\u0240\u023e\3\2\2\2\u0240\u023f\3\2\2\2\u0241~\3\2\2\2"+
		"\16\2\u0082\u0214\u021a\u021f\u0223\u0226\u0228\u022e\u0238\u023c\u0240"+
		"\4\3;\2\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
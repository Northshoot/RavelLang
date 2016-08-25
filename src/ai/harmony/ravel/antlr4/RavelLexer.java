// Generated from /Users/lauril/workspace/01-ravel/RavelLang/Ravel.g4 by ANTLR 4.5.3
package ai.harmony.ravel.antlr4;
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
		T__9=10, T__10=11, T__11=12, SELF=13, MODEL=14, SPACE=15, CONTROLLER=16, 
		VIEW=17, FLOW=18, LOCAL=19, STREAMING=20, REPLICATED=21, PROPERTIES=22, 
		SCHEMA=23, EVENT=24, COMMAND=25, T_BYTE_FIELD=26, T_STRING_FIELD=27, T_BOOLEAN_FIELD=28, 
		T_INTEGER_FIELD=29, T_NUMBER_FIELD=30, T_DATE_FIELD=31, T_DATE_TIME_FIELD=32, 
		T_TIME_STAMP_FIELD=33, T_CONTEXT_FIELD=34, ASSERT=35, RETURN=36, TRUE=37, 
		FALSE=38, IF=39, ELIF=40, ELSE=41, FOR=42, WHILE=43, AND=44, NOT=45, OR=46, 
		IN=47, IS=48, DELETE=49, PASS=50, CONTINUE=51, BREAK=52, NONE=53, NEWLINE=54, 
		NAME=55, STRING_LITERAL=56, BYTES_LITERAL=57, DECIMAL_INTEGER=58, OCT_INTEGER=59, 
		HEX_INTEGER=60, BIN_INTEGER=61, FLOAT_NUMBER=62, IMAG_NUMBER=63, DOT=64, 
		STAR=65, OPEN_PAREN=66, CLOSE_PAREN=67, COMMA=68, COLON=69, SEMI_COLON=70, 
		ASSIGN=71, OPEN_BRACK=72, CLOSE_BRACK=73, OR_OP=74, XOR=75, AND_OP=76, 
		LEFT_SHIFT=77, RIGHT_SHIFT=78, ADD=79, MINUS=80, DIV=81, MOD=82, IDIV=83, 
		NOT_OP=84, OPEN_BRACE=85, CLOSE_BRACE=86, LESS_THAN=87, GREATER_THAN=88, 
		EQUALS=89, GT_EQ=90, LT_EQ=91, NOT_EQ=92, AT=93, ARROW=94, ADD_ASSIGN=95, 
		SUB_ASSIGN=96, MULT_ASSIGN=97, AT_ASSIGN=98, DIV_ASSIGN=99, MOD_ASSIGN=100, 
		AND_ASSIGN=101, OR_ASSIGN=102, XOR_ASSIGN=103, LEFT_SHIFT_ASSIGN=104, 
		RIGHT_SHIFT_ASSIGN=105, POWER_ASSIGN=106, IDIV_ASSIGN=107, SKIP_=108;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "SELF", "MODEL", "SPACE", "CONTROLLER", "VIEW", 
		"FLOW", "LOCAL", "STREAMING", "REPLICATED", "PROPERTIES", "SCHEMA", "EVENT", 
		"COMMAND", "T_BYTE_FIELD", "T_STRING_FIELD", "T_BOOLEAN_FIELD", "T_INTEGER_FIELD", 
		"T_NUMBER_FIELD", "T_DATE_FIELD", "T_DATE_TIME_FIELD", "T_TIME_STAMP_FIELD", 
		"T_CONTEXT_FIELD", "ASSERT", "RETURN", "TRUE", "FALSE", "IF", "ELIF", 
		"ELSE", "FOR", "WHILE", "AND", "NOT", "OR", "IN", "IS", "DELETE", "PASS", 
		"CONTINUE", "BREAK", "NONE", "NEWLINE", "NAME", "STRING_LITERAL", "BYTES_LITERAL", 
		"DECIMAL_INTEGER", "OCT_INTEGER", "HEX_INTEGER", "BIN_INTEGER", "FLOAT_NUMBER", 
		"IMAG_NUMBER", "DOT", "STAR", "OPEN_PAREN", "CLOSE_PAREN", "COMMA", "COLON", 
		"SEMI_COLON", "ASSIGN", "OPEN_BRACK", "CLOSE_BRACK", "OR_OP", "XOR", "AND_OP", 
		"LEFT_SHIFT", "RIGHT_SHIFT", "ADD", "MINUS", "DIV", "MOD", "IDIV", "NOT_OP", 
		"OPEN_BRACE", "CLOSE_BRACE", "LESS_THAN", "GREATER_THAN", "EQUALS", "GT_EQ", 
		"LT_EQ", "NOT_EQ", "AT", "ARROW", "ADD_ASSIGN", "SUB_ASSIGN", "MULT_ASSIGN", 
		"AT_ASSIGN", "DIV_ASSIGN", "MOD_ASSIGN", "AND_ASSIGN", "OR_ASSIGN", "XOR_ASSIGN", 
		"LEFT_SHIFT_ASSIGN", "RIGHT_SHIFT_ASSIGN", "POWER_ASSIGN", "IDIV_ASSIGN", 
		"SKIP_", "SHORT_STRING", "LONG_STRING", "LONG_STRING_ITEM", "LONG_STRING_CHAR", 
		"STRING_ESCAPE_SEQ", "NON_ZERO_DIGIT", "DIGIT", "OCT_DIGIT", "HEX_DIGIT", 
		"BIN_DIGIT", "POINT_FLOAT", "EXPONENT_FLOAT", "INT_PART", "FRACTION", 
		"EXPONENT", "SHORT_BYTES", "LONG_BYTES", "LONG_BYTES_ITEM", "SHORT_BYTES_CHAR_NO_SINGLE_QUOTE", 
		"SHORT_BYTES_CHAR_NO_DOUBLE_QUOTE", "LONG_BYTES_CHAR", "BYTES_ESCAPE_SEQ", 
		"SPACES", "COMMENT", "LINE_JOINING", "ID_START", "ID_CONTINUE"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'platform:'", "'models:'", "'controllers:'", "'sinks:'", "'sources:'", 
		"'properties:'", "'schema:'", "'first()'", "'last()'", "'query'", "'**'", 
		"'...'", "'self'", "'model'", "'space'", "'controller'", "'view'", "'flow'", 
		"'local'", "'streaming'", "'replicated'", "'properties'", "'schema'", 
		"'event'", "'command'", "'ByteField'", "'StringField'", "'Boolean'", "'IntegerField'", 
		"'NumberField'", "'DateField'", "'DateTime'", "'TimestampField'", "'ContextField'", 
		"'assert'", "'return'", "'true'", "'false'", "'if'", "'elif'", "'else'", 
		"'for'", "'while'", "'and'", "'not'", "'or'", "'in'", "'is'", "'delete'", 
		"'pass'", "'continue'", "'break'", "'none'", null, null, null, null, null, 
		null, null, null, null, null, "'.'", "'*'", "'('", "')'", "','", "':'", 
		"';'", "'='", "'['", "']'", "'|'", "'^'", "'&'", "'<<'", "'>>'", "'+'", 
		"'-'", "'/'", "'%'", "'//'", "'~'", "'{'", "'}'", "'<'", "'>'", "'=='", 
		"'>='", "'<='", "'!='", "'@'", "'->'", "'+='", "'-='", "'*='", "'@='", 
		"'/='", "'%='", "'&='", "'|='", "'^='", "'<<='", "'>>='", "'**='", "'//='"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, "SELF", "MODEL", "SPACE", "CONTROLLER", "VIEW", "FLOW", "LOCAL", 
		"STREAMING", "REPLICATED", "PROPERTIES", "SCHEMA", "EVENT", "COMMAND", 
		"T_BYTE_FIELD", "T_STRING_FIELD", "T_BOOLEAN_FIELD", "T_INTEGER_FIELD", 
		"T_NUMBER_FIELD", "T_DATE_FIELD", "T_DATE_TIME_FIELD", "T_TIME_STAMP_FIELD", 
		"T_CONTEXT_FIELD", "ASSERT", "RETURN", "TRUE", "FALSE", "IF", "ELIF", 
		"ELSE", "FOR", "WHILE", "AND", "NOT", "OR", "IN", "IS", "DELETE", "PASS", 
		"CONTINUE", "BREAK", "NONE", "NEWLINE", "NAME", "STRING_LITERAL", "BYTES_LITERAL", 
		"DECIMAL_INTEGER", "OCT_INTEGER", "HEX_INTEGER", "BIN_INTEGER", "FLOAT_NUMBER", 
		"IMAG_NUMBER", "DOT", "STAR", "OPEN_PAREN", "CLOSE_PAREN", "COMMA", "COLON", 
		"SEMI_COLON", "ASSIGN", "OPEN_BRACK", "CLOSE_BRACK", "OR_OP", "XOR", "AND_OP", 
		"LEFT_SHIFT", "RIGHT_SHIFT", "ADD", "MINUS", "DIV", "MOD", "IDIV", "NOT_OP", 
		"OPEN_BRACE", "CLOSE_BRACE", "LESS_THAN", "GREATER_THAN", "EQUALS", "GT_EQ", 
		"LT_EQ", "NOT_EQ", "AT", "ARROW", "ADD_ASSIGN", "SUB_ASSIGN", "MULT_ASSIGN", 
		"AT_ASSIGN", "DIV_ASSIGN", "MOD_ASSIGN", "AND_ASSIGN", "OR_ASSIGN", "XOR_ASSIGN", 
		"LEFT_SHIFT_ASSIGN", "RIGHT_SHIFT_ASSIGN", "POWER_ASSIGN", "IDIV_ASSIGN", 
		"SKIP_"
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
		case 53:
			NEWLINE_action((RuleContext)_localctx, actionIndex);
			break;
		case 65:
			OPEN_PAREN_action((RuleContext)_localctx, actionIndex);
			break;
		case 66:
			CLOSE_PAREN_action((RuleContext)_localctx, actionIndex);
			break;
		case 71:
			OPEN_BRACK_action((RuleContext)_localctx, actionIndex);
			break;
		case 72:
			CLOSE_BRACK_action((RuleContext)_localctx, actionIndex);
			break;
		case 84:
			OPEN_BRACE_action((RuleContext)_localctx, actionIndex);
			break;
		case 85:
			CLOSE_BRACE_action((RuleContext)_localctx, actionIndex);
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
	private void OPEN_PAREN_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:
			opened++;
			break;
		}
	}
	private void CLOSE_PAREN_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 2:
			opened--;
			break;
		}
	}
	private void OPEN_BRACK_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 3:
			opened++;
			break;
		}
	}
	private void CLOSE_BRACK_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 4:
			opened--;
			break;
		}
	}
	private void OPEN_BRACE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 5:
			opened++;
			break;
		}
	}
	private void CLOSE_BRACE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 6:
			opened--;
			break;
		}
	}
	@Override
	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 53:
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2n\u0430\b\1\4\2\t"+
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
		"`\t`\4a\ta\4b\tb\4c\tc\4d\td\4e\te\4f\tf\4g\tg\4h\th\4i\ti\4j\tj\4k\t"+
		"k\4l\tl\4m\tm\4n\tn\4o\to\4p\tp\4q\tq\4r\tr\4s\ts\4t\tt\4u\tu\4v\tv\4"+
		"w\tw\4x\tx\4y\ty\4z\tz\4{\t{\4|\t|\4}\t}\4~\t~\4\177\t\177\4\u0080\t\u0080"+
		"\4\u0081\t\u0081\4\u0082\t\u0082\4\u0083\t\u0083\4\u0084\t\u0084\4\u0085"+
		"\t\u0085\4\u0086\t\u0086\4\u0087\t\u0087\4\u0088\t\u0088\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3"+
		"\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3"+
		"\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3"+
		"\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3"+
		"\23\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3"+
		"\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3"+
		"\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3"+
		"\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3"+
		"\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3"+
		"\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3"+
		"\35\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3"+
		"\36\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3"+
		"\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\3!\3!\3"+
		"!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3"+
		"#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3$\3$\3$\3$\3$\3$\3$\3%\3%\3%\3%\3%\3"+
		"%\3%\3&\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3)\3)\3)\3)\3)\3"+
		"*\3*\3*\3*\3*\3+\3+\3+\3+\3,\3,\3,\3,\3,\3,\3-\3-\3-\3-\3.\3.\3.\3.\3"+
		"/\3/\3/\3\60\3\60\3\60\3\61\3\61\3\61\3\62\3\62\3\62\3\62\3\62\3\62\3"+
		"\62\3\63\3\63\3\63\3\63\3\63\3\64\3\64\3\64\3\64\3\64\3\64\3\64\3\64\3"+
		"\64\3\65\3\65\3\65\3\65\3\65\3\65\3\66\3\66\3\66\3\66\3\66\3\67\3\67\3"+
		"\67\5\67\u029c\n\67\3\67\3\67\5\67\u02a0\n\67\3\67\5\67\u02a3\n\67\5\67"+
		"\u02a5\n\67\3\67\3\67\38\38\78\u02ab\n8\f8\168\u02ae\138\39\59\u02b1\n"+
		"9\39\59\u02b4\n9\39\39\59\u02b8\n9\3:\3:\5:\u02bc\n:\3:\3:\5:\u02c0\n"+
		":\3;\3;\7;\u02c4\n;\f;\16;\u02c7\13;\3;\6;\u02ca\n;\r;\16;\u02cb\5;\u02ce"+
		"\n;\3<\3<\3<\6<\u02d3\n<\r<\16<\u02d4\3=\3=\3=\6=\u02da\n=\r=\16=\u02db"+
		"\3>\3>\3>\6>\u02e1\n>\r>\16>\u02e2\3?\3?\5?\u02e7\n?\3@\3@\5@\u02eb\n"+
		"@\3@\3@\3A\3A\3B\3B\3C\3C\3C\3D\3D\3D\3E\3E\3F\3F\3G\3G\3H\3H\3I\3I\3"+
		"I\3J\3J\3J\3K\3K\3L\3L\3M\3M\3N\3N\3N\3O\3O\3O\3P\3P\3Q\3Q\3R\3R\3S\3"+
		"S\3T\3T\3T\3U\3U\3V\3V\3V\3W\3W\3W\3X\3X\3Y\3Y\3Z\3Z\3Z\3[\3[\3[\3\\\3"+
		"\\\3\\\3]\3]\3]\3^\3^\3_\3_\3_\3`\3`\3`\3a\3a\3a\3b\3b\3b\3c\3c\3c\3d"+
		"\3d\3d\3e\3e\3e\3f\3f\3f\3g\3g\3g\3h\3h\3h\3i\3i\3i\3i\3j\3j\3j\3j\3k"+
		"\3k\3k\3k\3l\3l\3l\3l\3m\3m\3m\5m\u0369\nm\3m\3m\3n\3n\3n\7n\u0370\nn"+
		"\fn\16n\u0373\13n\3n\3n\3n\3n\7n\u0379\nn\fn\16n\u037c\13n\3n\5n\u037f"+
		"\nn\3o\3o\3o\3o\3o\7o\u0386\no\fo\16o\u0389\13o\3o\3o\3o\3o\3o\3o\3o\3"+
		"o\7o\u0393\no\fo\16o\u0396\13o\3o\3o\3o\5o\u039b\no\3p\3p\5p\u039f\np"+
		"\3q\3q\3r\3r\3r\3s\3s\3t\3t\3u\3u\3v\3v\3w\3w\3x\5x\u03b1\nx\3x\3x\3x"+
		"\3x\5x\u03b7\nx\3y\3y\5y\u03bb\ny\3y\3y\3z\6z\u03c0\nz\rz\16z\u03c1\3"+
		"{\3{\6{\u03c6\n{\r{\16{\u03c7\3|\3|\5|\u03cc\n|\3|\6|\u03cf\n|\r|\16|"+
		"\u03d0\3}\3}\3}\7}\u03d6\n}\f}\16}\u03d9\13}\3}\3}\3}\3}\7}\u03df\n}\f"+
		"}\16}\u03e2\13}\3}\5}\u03e5\n}\3~\3~\3~\3~\3~\7~\u03ec\n~\f~\16~\u03ef"+
		"\13~\3~\3~\3~\3~\3~\3~\3~\3~\7~\u03f9\n~\f~\16~\u03fc\13~\3~\3~\3~\5~"+
		"\u0401\n~\3\177\3\177\5\177\u0405\n\177\3\u0080\5\u0080\u0408\n\u0080"+
		"\3\u0081\5\u0081\u040b\n\u0081\3\u0082\5\u0082\u040e\n\u0082\3\u0083\3"+
		"\u0083\3\u0083\3\u0084\6\u0084\u0414\n\u0084\r\u0084\16\u0084\u0415\3"+
		"\u0085\3\u0085\7\u0085\u041a\n\u0085\f\u0085\16\u0085\u041d\13\u0085\3"+
		"\u0086\3\u0086\5\u0086\u0421\n\u0086\3\u0086\5\u0086\u0424\n\u0086\3\u0086"+
		"\3\u0086\5\u0086\u0428\n\u0086\3\u0087\5\u0087\u042b\n\u0087\3\u0088\3"+
		"\u0088\5\u0088\u042f\n\u0088\6\u0387\u0394\u03ed\u03fa\2\u0089\3\3\5\4"+
		"\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22"+
		"#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C"+
		"#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g\65i\66k\67m8o9q:s;u<w"+
		"=y>{?}@\177A\u0081B\u0083C\u0085D\u0087E\u0089F\u008bG\u008dH\u008fI\u0091"+
		"J\u0093K\u0095L\u0097M\u0099N\u009bO\u009dP\u009fQ\u00a1R\u00a3S\u00a5"+
		"T\u00a7U\u00a9V\u00abW\u00adX\u00afY\u00b1Z\u00b3[\u00b5\\\u00b7]\u00b9"+
		"^\u00bb_\u00bd`\u00bfa\u00c1b\u00c3c\u00c5d\u00c7e\u00c9f\u00cbg\u00cd"+
		"h\u00cfi\u00d1j\u00d3k\u00d5l\u00d7m\u00d9n\u00db\2\u00dd\2\u00df\2\u00e1"+
		"\2\u00e3\2\u00e5\2\u00e7\2\u00e9\2\u00eb\2\u00ed\2\u00ef\2\u00f1\2\u00f3"+
		"\2\u00f5\2\u00f7\2\u00f9\2\u00fb\2\u00fd\2\u00ff\2\u0101\2\u0103\2\u0105"+
		"\2\u0107\2\u0109\2\u010b\2\u010d\2\u010f\2\3\2\31\4\2WWww\4\2TTtt\4\2"+
		"DDdd\4\2QQqq\4\2ZZzz\4\2LLll\6\2\f\f\17\17))^^\6\2\f\f\17\17$$^^\3\2^"+
		"^\3\2\63;\3\2\62;\3\2\629\5\2\62;CHch\3\2\62\63\4\2GGgg\4\2--//\7\2\2"+
		"\13\r\16\20(*]_\u0081\7\2\2\13\r\16\20#%]_\u0081\4\2\2]_\u0081\3\2\2\u0081"+
		"\4\2\13\13\"\"\4\2\f\f\17\17\5\2C\\aac|\u0447\2\3\3\2\2\2\2\5\3\2\2\2"+
		"\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3"+
		"\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2"+
		"\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2"+
		"\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2"+
		"\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2"+
		"\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2"+
		"\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y"+
		"\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2"+
		"\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2"+
		"\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177"+
		"\3\2\2\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2\2\u0085\3\2\2\2\2\u0087\3\2\2"+
		"\2\2\u0089\3\2\2\2\2\u008b\3\2\2\2\2\u008d\3\2\2\2\2\u008f\3\2\2\2\2\u0091"+
		"\3\2\2\2\2\u0093\3\2\2\2\2\u0095\3\2\2\2\2\u0097\3\2\2\2\2\u0099\3\2\2"+
		"\2\2\u009b\3\2\2\2\2\u009d\3\2\2\2\2\u009f\3\2\2\2\2\u00a1\3\2\2\2\2\u00a3"+
		"\3\2\2\2\2\u00a5\3\2\2\2\2\u00a7\3\2\2\2\2\u00a9\3\2\2\2\2\u00ab\3\2\2"+
		"\2\2\u00ad\3\2\2\2\2\u00af\3\2\2\2\2\u00b1\3\2\2\2\2\u00b3\3\2\2\2\2\u00b5"+
		"\3\2\2\2\2\u00b7\3\2\2\2\2\u00b9\3\2\2\2\2\u00bb\3\2\2\2\2\u00bd\3\2\2"+
		"\2\2\u00bf\3\2\2\2\2\u00c1\3\2\2\2\2\u00c3\3\2\2\2\2\u00c5\3\2\2\2\2\u00c7"+
		"\3\2\2\2\2\u00c9\3\2\2\2\2\u00cb\3\2\2\2\2\u00cd\3\2\2\2\2\u00cf\3\2\2"+
		"\2\2\u00d1\3\2\2\2\2\u00d3\3\2\2\2\2\u00d5\3\2\2\2\2\u00d7\3\2\2\2\2\u00d9"+
		"\3\2\2\2\3\u0111\3\2\2\2\5\u011b\3\2\2\2\7\u0123\3\2\2\2\t\u0130\3\2\2"+
		"\2\13\u0137\3\2\2\2\r\u0140\3\2\2\2\17\u014c\3\2\2\2\21\u0154\3\2\2\2"+
		"\23\u015c\3\2\2\2\25\u0163\3\2\2\2\27\u0169\3\2\2\2\31\u016c\3\2\2\2\33"+
		"\u0170\3\2\2\2\35\u0175\3\2\2\2\37\u017b\3\2\2\2!\u0181\3\2\2\2#\u018c"+
		"\3\2\2\2%\u0191\3\2\2\2\'\u0196\3\2\2\2)\u019c\3\2\2\2+\u01a6\3\2\2\2"+
		"-\u01b1\3\2\2\2/\u01bc\3\2\2\2\61\u01c3\3\2\2\2\63\u01c9\3\2\2\2\65\u01d1"+
		"\3\2\2\2\67\u01db\3\2\2\29\u01e7\3\2\2\2;\u01ef\3\2\2\2=\u01fc\3\2\2\2"+
		"?\u0208\3\2\2\2A\u0212\3\2\2\2C\u021b\3\2\2\2E\u022a\3\2\2\2G\u0237\3"+
		"\2\2\2I\u023e\3\2\2\2K\u0245\3\2\2\2M\u024a\3\2\2\2O\u0250\3\2\2\2Q\u0253"+
		"\3\2\2\2S\u0258\3\2\2\2U\u025d\3\2\2\2W\u0261\3\2\2\2Y\u0267\3\2\2\2["+
		"\u026b\3\2\2\2]\u026f\3\2\2\2_\u0272\3\2\2\2a\u0275\3\2\2\2c\u0278\3\2"+
		"\2\2e\u027f\3\2\2\2g\u0284\3\2\2\2i\u028d\3\2\2\2k\u0293\3\2\2\2m\u02a4"+
		"\3\2\2\2o\u02a8\3\2\2\2q\u02b0\3\2\2\2s\u02b9\3\2\2\2u\u02cd\3\2\2\2w"+
		"\u02cf\3\2\2\2y\u02d6\3\2\2\2{\u02dd\3\2\2\2}\u02e6\3\2\2\2\177\u02ea"+
		"\3\2\2\2\u0081\u02ee\3\2\2\2\u0083\u02f0\3\2\2\2\u0085\u02f2\3\2\2\2\u0087"+
		"\u02f5\3\2\2\2\u0089\u02f8\3\2\2\2\u008b\u02fa\3\2\2\2\u008d\u02fc\3\2"+
		"\2\2\u008f\u02fe\3\2\2\2\u0091\u0300\3\2\2\2\u0093\u0303\3\2\2\2\u0095"+
		"\u0306\3\2\2\2\u0097\u0308\3\2\2\2\u0099\u030a\3\2\2\2\u009b\u030c\3\2"+
		"\2\2\u009d\u030f\3\2\2\2\u009f\u0312\3\2\2\2\u00a1\u0314\3\2\2\2\u00a3"+
		"\u0316\3\2\2\2\u00a5\u0318\3\2\2\2\u00a7\u031a\3\2\2\2\u00a9\u031d\3\2"+
		"\2\2\u00ab\u031f\3\2\2\2\u00ad\u0322\3\2\2\2\u00af\u0325\3\2\2\2\u00b1"+
		"\u0327\3\2\2\2\u00b3\u0329\3\2\2\2\u00b5\u032c\3\2\2\2\u00b7\u032f\3\2"+
		"\2\2\u00b9\u0332\3\2\2\2\u00bb\u0335\3\2\2\2\u00bd\u0337\3\2\2\2\u00bf"+
		"\u033a\3\2\2\2\u00c1\u033d\3\2\2\2\u00c3\u0340\3\2\2\2\u00c5\u0343\3\2"+
		"\2\2\u00c7\u0346\3\2\2\2\u00c9\u0349\3\2\2\2\u00cb\u034c\3\2\2\2\u00cd"+
		"\u034f\3\2\2\2\u00cf\u0352\3\2\2\2\u00d1\u0355\3\2\2\2\u00d3\u0359\3\2"+
		"\2\2\u00d5\u035d\3\2\2\2\u00d7\u0361\3\2\2\2\u00d9\u0368\3\2\2\2\u00db"+
		"\u037e\3\2\2\2\u00dd\u039a\3\2\2\2\u00df\u039e\3\2\2\2\u00e1\u03a0\3\2"+
		"\2\2\u00e3\u03a2\3\2\2\2\u00e5\u03a5\3\2\2\2\u00e7\u03a7\3\2\2\2\u00e9"+
		"\u03a9\3\2\2\2\u00eb\u03ab\3\2\2\2\u00ed\u03ad\3\2\2\2\u00ef\u03b6\3\2"+
		"\2\2\u00f1\u03ba\3\2\2\2\u00f3\u03bf\3\2\2\2\u00f5\u03c3\3\2\2\2\u00f7"+
		"\u03c9\3\2\2\2\u00f9\u03e4\3\2\2\2\u00fb\u0400\3\2\2\2\u00fd\u0404\3\2"+
		"\2\2\u00ff\u0407\3\2\2\2\u0101\u040a\3\2\2\2\u0103\u040d\3\2\2\2\u0105"+
		"\u040f\3\2\2\2\u0107\u0413\3\2\2\2\u0109\u0417\3\2\2\2\u010b\u041e\3\2"+
		"\2\2\u010d\u042a\3\2\2\2\u010f\u042e\3\2\2\2\u0111\u0112\7r\2\2\u0112"+
		"\u0113\7n\2\2\u0113\u0114\7c\2\2\u0114\u0115\7v\2\2\u0115\u0116\7h\2\2"+
		"\u0116\u0117\7q\2\2\u0117\u0118\7t\2\2\u0118\u0119\7o\2\2\u0119\u011a"+
		"\7<\2\2\u011a\4\3\2\2\2\u011b\u011c\7o\2\2\u011c\u011d\7q\2\2\u011d\u011e"+
		"\7f\2\2\u011e\u011f\7g\2\2\u011f\u0120\7n\2\2\u0120\u0121\7u\2\2\u0121"+
		"\u0122\7<\2\2\u0122\6\3\2\2\2\u0123\u0124\7e\2\2\u0124\u0125\7q\2\2\u0125"+
		"\u0126\7p\2\2\u0126\u0127\7v\2\2\u0127\u0128\7t\2\2\u0128\u0129\7q\2\2"+
		"\u0129\u012a\7n\2\2\u012a\u012b\7n\2\2\u012b\u012c\7g\2\2\u012c\u012d"+
		"\7t\2\2\u012d\u012e\7u\2\2\u012e\u012f\7<\2\2\u012f\b\3\2\2\2\u0130\u0131"+
		"\7u\2\2\u0131\u0132\7k\2\2\u0132\u0133\7p\2\2\u0133\u0134\7m\2\2\u0134"+
		"\u0135\7u\2\2\u0135\u0136\7<\2\2\u0136\n\3\2\2\2\u0137\u0138\7u\2\2\u0138"+
		"\u0139\7q\2\2\u0139\u013a\7w\2\2\u013a\u013b\7t\2\2\u013b\u013c\7e\2\2"+
		"\u013c\u013d\7g\2\2\u013d\u013e\7u\2\2\u013e\u013f\7<\2\2\u013f\f\3\2"+
		"\2\2\u0140\u0141\7r\2\2\u0141\u0142\7t\2\2\u0142\u0143\7q\2\2\u0143\u0144"+
		"\7r\2\2\u0144\u0145\7g\2\2\u0145\u0146\7t\2\2\u0146\u0147\7v\2\2\u0147"+
		"\u0148\7k\2\2\u0148\u0149\7g\2\2\u0149\u014a\7u\2\2\u014a\u014b\7<\2\2"+
		"\u014b\16\3\2\2\2\u014c\u014d\7u\2\2\u014d\u014e\7e\2\2\u014e\u014f\7"+
		"j\2\2\u014f\u0150\7g\2\2\u0150\u0151\7o\2\2\u0151\u0152\7c\2\2\u0152\u0153"+
		"\7<\2\2\u0153\20\3\2\2\2\u0154\u0155\7h\2\2\u0155\u0156\7k\2\2\u0156\u0157"+
		"\7t\2\2\u0157\u0158\7u\2\2\u0158\u0159\7v\2\2\u0159\u015a\7*\2\2\u015a"+
		"\u015b\7+\2\2\u015b\22\3\2\2\2\u015c\u015d\7n\2\2\u015d\u015e\7c\2\2\u015e"+
		"\u015f\7u\2\2\u015f\u0160\7v\2\2\u0160\u0161\7*\2\2\u0161\u0162\7+\2\2"+
		"\u0162\24\3\2\2\2\u0163\u0164\7s\2\2\u0164\u0165\7w\2\2\u0165\u0166\7"+
		"g\2\2\u0166\u0167\7t\2\2\u0167\u0168\7{\2\2\u0168\26\3\2\2\2\u0169\u016a"+
		"\7,\2\2\u016a\u016b\7,\2\2\u016b\30\3\2\2\2\u016c\u016d\7\60\2\2\u016d"+
		"\u016e\7\60\2\2\u016e\u016f\7\60\2\2\u016f\32\3\2\2\2\u0170\u0171\7u\2"+
		"\2\u0171\u0172\7g\2\2\u0172\u0173\7n\2\2\u0173\u0174\7h\2\2\u0174\34\3"+
		"\2\2\2\u0175\u0176\7o\2\2\u0176\u0177\7q\2\2\u0177\u0178\7f\2\2\u0178"+
		"\u0179\7g\2\2\u0179\u017a\7n\2\2\u017a\36\3\2\2\2\u017b\u017c\7u\2\2\u017c"+
		"\u017d\7r\2\2\u017d\u017e\7c\2\2\u017e\u017f\7e\2\2\u017f\u0180\7g\2\2"+
		"\u0180 \3\2\2\2\u0181\u0182\7e\2\2\u0182\u0183\7q\2\2\u0183\u0184\7p\2"+
		"\2\u0184\u0185\7v\2\2\u0185\u0186\7t\2\2\u0186\u0187\7q\2\2\u0187\u0188"+
		"\7n\2\2\u0188\u0189\7n\2\2\u0189\u018a\7g\2\2\u018a\u018b\7t\2\2\u018b"+
		"\"\3\2\2\2\u018c\u018d\7x\2\2\u018d\u018e\7k\2\2\u018e\u018f\7g\2\2\u018f"+
		"\u0190\7y\2\2\u0190$\3\2\2\2\u0191\u0192\7h\2\2\u0192\u0193\7n\2\2\u0193"+
		"\u0194\7q\2\2\u0194\u0195\7y\2\2\u0195&\3\2\2\2\u0196\u0197\7n\2\2\u0197"+
		"\u0198\7q\2\2\u0198\u0199\7e\2\2\u0199\u019a\7c\2\2\u019a\u019b\7n\2\2"+
		"\u019b(\3\2\2\2\u019c\u019d\7u\2\2\u019d\u019e\7v\2\2\u019e\u019f\7t\2"+
		"\2\u019f\u01a0\7g\2\2\u01a0\u01a1\7c\2\2\u01a1\u01a2\7o\2\2\u01a2\u01a3"+
		"\7k\2\2\u01a3\u01a4\7p\2\2\u01a4\u01a5\7i\2\2\u01a5*\3\2\2\2\u01a6\u01a7"+
		"\7t\2\2\u01a7\u01a8\7g\2\2\u01a8\u01a9\7r\2\2\u01a9\u01aa\7n\2\2\u01aa"+
		"\u01ab\7k\2\2\u01ab\u01ac\7e\2\2\u01ac\u01ad\7c\2\2\u01ad\u01ae\7v\2\2"+
		"\u01ae\u01af\7g\2\2\u01af\u01b0\7f\2\2\u01b0,\3\2\2\2\u01b1\u01b2\7r\2"+
		"\2\u01b2\u01b3\7t\2\2\u01b3\u01b4\7q\2\2\u01b4\u01b5\7r\2\2\u01b5\u01b6"+
		"\7g\2\2\u01b6\u01b7\7t\2\2\u01b7\u01b8\7v\2\2\u01b8\u01b9\7k\2\2\u01b9"+
		"\u01ba\7g\2\2\u01ba\u01bb\7u\2\2\u01bb.\3\2\2\2\u01bc\u01bd\7u\2\2\u01bd"+
		"\u01be\7e\2\2\u01be\u01bf\7j\2\2\u01bf\u01c0\7g\2\2\u01c0\u01c1\7o\2\2"+
		"\u01c1\u01c2\7c\2\2\u01c2\60\3\2\2\2\u01c3\u01c4\7g\2\2\u01c4\u01c5\7"+
		"x\2\2\u01c5\u01c6\7g\2\2\u01c6\u01c7\7p\2\2\u01c7\u01c8\7v\2\2\u01c8\62"+
		"\3\2\2\2\u01c9\u01ca\7e\2\2\u01ca\u01cb\7q\2\2\u01cb\u01cc\7o\2\2\u01cc"+
		"\u01cd\7o\2\2\u01cd\u01ce\7c\2\2\u01ce\u01cf\7p\2\2\u01cf\u01d0\7f\2\2"+
		"\u01d0\64\3\2\2\2\u01d1\u01d2\7D\2\2\u01d2\u01d3\7{\2\2\u01d3\u01d4\7"+
		"v\2\2\u01d4\u01d5\7g\2\2\u01d5\u01d6\7H\2\2\u01d6\u01d7\7k\2\2\u01d7\u01d8"+
		"\7g\2\2\u01d8\u01d9\7n\2\2\u01d9\u01da\7f\2\2\u01da\66\3\2\2\2\u01db\u01dc"+
		"\7U\2\2\u01dc\u01dd\7v\2\2\u01dd\u01de\7t\2\2\u01de\u01df\7k\2\2\u01df"+
		"\u01e0\7p\2\2\u01e0\u01e1\7i\2\2\u01e1\u01e2\7H\2\2\u01e2\u01e3\7k\2\2"+
		"\u01e3\u01e4\7g\2\2\u01e4\u01e5\7n\2\2\u01e5\u01e6\7f\2\2\u01e68\3\2\2"+
		"\2\u01e7\u01e8\7D\2\2\u01e8\u01e9\7q\2\2\u01e9\u01ea\7q\2\2\u01ea\u01eb"+
		"\7n\2\2\u01eb\u01ec\7g\2\2\u01ec\u01ed\7c\2\2\u01ed\u01ee\7p\2\2\u01ee"+
		":\3\2\2\2\u01ef\u01f0\7K\2\2\u01f0\u01f1\7p\2\2\u01f1\u01f2\7v\2\2\u01f2"+
		"\u01f3\7g\2\2\u01f3\u01f4\7i\2\2\u01f4\u01f5\7g\2\2\u01f5\u01f6\7t\2\2"+
		"\u01f6\u01f7\7H\2\2\u01f7\u01f8\7k\2\2\u01f8\u01f9\7g\2\2\u01f9\u01fa"+
		"\7n\2\2\u01fa\u01fb\7f\2\2\u01fb<\3\2\2\2\u01fc\u01fd\7P\2\2\u01fd\u01fe"+
		"\7w\2\2\u01fe\u01ff\7o\2\2\u01ff\u0200\7d\2\2\u0200\u0201\7g\2\2\u0201"+
		"\u0202\7t\2\2\u0202\u0203\7H\2\2\u0203\u0204\7k\2\2\u0204\u0205\7g\2\2"+
		"\u0205\u0206\7n\2\2\u0206\u0207\7f\2\2\u0207>\3\2\2\2\u0208\u0209\7F\2"+
		"\2\u0209\u020a\7c\2\2\u020a\u020b\7v\2\2\u020b\u020c\7g\2\2\u020c\u020d"+
		"\7H\2\2\u020d\u020e\7k\2\2\u020e\u020f\7g\2\2\u020f\u0210\7n\2\2\u0210"+
		"\u0211\7f\2\2\u0211@\3\2\2\2\u0212\u0213\7F\2\2\u0213\u0214\7c\2\2\u0214"+
		"\u0215\7v\2\2\u0215\u0216\7g\2\2\u0216\u0217\7V\2\2\u0217\u0218\7k\2\2"+
		"\u0218\u0219\7o\2\2\u0219\u021a\7g\2\2\u021aB\3\2\2\2\u021b\u021c\7V\2"+
		"\2\u021c\u021d\7k\2\2\u021d\u021e\7o\2\2\u021e\u021f\7g\2\2\u021f\u0220"+
		"\7u\2\2\u0220\u0221\7v\2\2\u0221\u0222\7c\2\2\u0222\u0223\7o\2\2\u0223"+
		"\u0224\7r\2\2\u0224\u0225\7H\2\2\u0225\u0226\7k\2\2\u0226\u0227\7g\2\2"+
		"\u0227\u0228\7n\2\2\u0228\u0229\7f\2\2\u0229D\3\2\2\2\u022a\u022b\7E\2"+
		"\2\u022b\u022c\7q\2\2\u022c\u022d\7p\2\2\u022d\u022e\7v\2\2\u022e\u022f"+
		"\7g\2\2\u022f\u0230\7z\2\2\u0230\u0231\7v\2\2\u0231\u0232\7H\2\2\u0232"+
		"\u0233\7k\2\2\u0233\u0234\7g\2\2\u0234\u0235\7n\2\2\u0235\u0236\7f\2\2"+
		"\u0236F\3\2\2\2\u0237\u0238\7c\2\2\u0238\u0239\7u\2\2\u0239\u023a\7u\2"+
		"\2\u023a\u023b\7g\2\2\u023b\u023c\7t\2\2\u023c\u023d\7v\2\2\u023dH\3\2"+
		"\2\2\u023e\u023f\7t\2\2\u023f\u0240\7g\2\2\u0240\u0241\7v\2\2\u0241\u0242"+
		"\7w\2\2\u0242\u0243\7t\2\2\u0243\u0244\7p\2\2\u0244J\3\2\2\2\u0245\u0246"+
		"\7v\2\2\u0246\u0247\7t\2\2\u0247\u0248\7w\2\2\u0248\u0249\7g\2\2\u0249"+
		"L\3\2\2\2\u024a\u024b\7h\2\2\u024b\u024c\7c\2\2\u024c\u024d\7n\2\2\u024d"+
		"\u024e\7u\2\2\u024e\u024f\7g\2\2\u024fN\3\2\2\2\u0250\u0251\7k\2\2\u0251"+
		"\u0252\7h\2\2\u0252P\3\2\2\2\u0253\u0254\7g\2\2\u0254\u0255\7n\2\2\u0255"+
		"\u0256\7k\2\2\u0256\u0257\7h\2\2\u0257R\3\2\2\2\u0258\u0259\7g\2\2\u0259"+
		"\u025a\7n\2\2\u025a\u025b\7u\2\2\u025b\u025c\7g\2\2\u025cT\3\2\2\2\u025d"+
		"\u025e\7h\2\2\u025e\u025f\7q\2\2\u025f\u0260\7t\2\2\u0260V\3\2\2\2\u0261"+
		"\u0262\7y\2\2\u0262\u0263\7j\2\2\u0263\u0264\7k\2\2\u0264\u0265\7n\2\2"+
		"\u0265\u0266\7g\2\2\u0266X\3\2\2\2\u0267\u0268\7c\2\2\u0268\u0269\7p\2"+
		"\2\u0269\u026a\7f\2\2\u026aZ\3\2\2\2\u026b\u026c\7p\2\2\u026c\u026d\7"+
		"q\2\2\u026d\u026e\7v\2\2\u026e\\\3\2\2\2\u026f\u0270\7q\2\2\u0270\u0271"+
		"\7t\2\2\u0271^\3\2\2\2\u0272\u0273\7k\2\2\u0273\u0274\7p\2\2\u0274`\3"+
		"\2\2\2\u0275\u0276\7k\2\2\u0276\u0277\7u\2\2\u0277b\3\2\2\2\u0278\u0279"+
		"\7f\2\2\u0279\u027a\7g\2\2\u027a\u027b\7n\2\2\u027b\u027c\7g\2\2\u027c"+
		"\u027d\7v\2\2\u027d\u027e\7g\2\2\u027ed\3\2\2\2\u027f\u0280\7r\2\2\u0280"+
		"\u0281\7c\2\2\u0281\u0282\7u\2\2\u0282\u0283\7u\2\2\u0283f\3\2\2\2\u0284"+
		"\u0285\7e\2\2\u0285\u0286\7q\2\2\u0286\u0287\7p\2\2\u0287\u0288\7v\2\2"+
		"\u0288\u0289\7k\2\2\u0289\u028a\7p\2\2\u028a\u028b\7w\2\2\u028b\u028c"+
		"\7g\2\2\u028ch\3\2\2\2\u028d\u028e\7d\2\2\u028e\u028f\7t\2\2\u028f\u0290"+
		"\7g\2\2\u0290\u0291\7c\2\2\u0291\u0292\7m\2\2\u0292j\3\2\2\2\u0293\u0294"+
		"\7p\2\2\u0294\u0295\7q\2\2\u0295\u0296\7p\2\2\u0296\u0297\7g\2\2\u0297"+
		"l\3\2\2\2\u0298\u0299\6\67\2\2\u0299\u02a5\5\u0107\u0084\2\u029a\u029c"+
		"\7\17\2\2\u029b\u029a\3\2\2\2\u029b\u029c\3\2\2\2\u029c\u029d\3\2\2\2"+
		"\u029d\u02a0\7\f\2\2\u029e\u02a0\7\17\2\2\u029f\u029b\3\2\2\2\u029f\u029e"+
		"\3\2\2\2\u02a0\u02a2\3\2\2\2\u02a1\u02a3\5\u0107\u0084\2\u02a2\u02a1\3"+
		"\2\2\2\u02a2\u02a3\3\2\2\2\u02a3\u02a5\3\2\2\2\u02a4\u0298\3\2\2\2\u02a4"+
		"\u029f\3\2\2\2\u02a5\u02a6\3\2\2\2\u02a6\u02a7\b\67\2\2\u02a7n\3\2\2\2"+
		"\u02a8\u02ac\5\u010d\u0087\2\u02a9\u02ab\5\u010f\u0088\2\u02aa\u02a9\3"+
		"\2\2\2\u02ab\u02ae\3\2\2\2\u02ac\u02aa\3\2\2\2\u02ac\u02ad\3\2\2\2\u02ad"+
		"p\3\2\2\2\u02ae\u02ac\3\2\2\2\u02af\u02b1\t\2\2\2\u02b0\u02af\3\2\2\2"+
		"\u02b0\u02b1\3\2\2\2\u02b1\u02b3\3\2\2\2\u02b2\u02b4\t\3\2\2\u02b3\u02b2"+
		"\3\2\2\2\u02b3\u02b4\3\2\2\2\u02b4\u02b7\3\2\2\2\u02b5\u02b8\5\u00dbn"+
		"\2\u02b6\u02b8\5\u00ddo\2\u02b7\u02b5\3\2\2\2\u02b7\u02b6\3\2\2\2\u02b8"+
		"r\3\2\2\2\u02b9\u02bb\t\4\2\2\u02ba\u02bc\t\3\2\2\u02bb\u02ba\3\2\2\2"+
		"\u02bb\u02bc\3\2\2\2\u02bc\u02bf\3\2\2\2\u02bd\u02c0\5\u00f9}\2\u02be"+
		"\u02c0\5\u00fb~\2\u02bf\u02bd\3\2\2\2\u02bf\u02be\3\2\2\2\u02c0t\3\2\2"+
		"\2\u02c1\u02c5\5\u00e5s\2\u02c2\u02c4\5\u00e7t\2\u02c3\u02c2\3\2\2\2\u02c4"+
		"\u02c7\3\2\2\2\u02c5\u02c3\3\2\2\2\u02c5\u02c6\3\2\2\2\u02c6\u02ce\3\2"+
		"\2\2\u02c7\u02c5\3\2\2\2\u02c8\u02ca\7\62\2\2\u02c9\u02c8\3\2\2\2\u02ca"+
		"\u02cb\3\2\2\2\u02cb\u02c9\3\2\2\2\u02cb\u02cc\3\2\2\2\u02cc\u02ce\3\2"+
		"\2\2\u02cd\u02c1\3\2\2\2\u02cd\u02c9\3\2\2\2\u02cev\3\2\2\2\u02cf\u02d0"+
		"\7\62\2\2\u02d0\u02d2\t\5\2\2\u02d1\u02d3\5\u00e9u\2\u02d2\u02d1\3\2\2"+
		"\2\u02d3\u02d4\3\2\2\2\u02d4\u02d2\3\2\2\2\u02d4\u02d5\3\2\2\2\u02d5x"+
		"\3\2\2\2\u02d6\u02d7\7\62\2\2\u02d7\u02d9\t\6\2\2\u02d8\u02da\5\u00eb"+
		"v\2\u02d9\u02d8\3\2\2\2\u02da\u02db\3\2\2\2\u02db\u02d9\3\2\2\2\u02db"+
		"\u02dc\3\2\2\2\u02dcz\3\2\2\2\u02dd\u02de\7\62\2\2\u02de\u02e0\t\4\2\2"+
		"\u02df\u02e1\5\u00edw\2\u02e0\u02df\3\2\2\2\u02e1\u02e2\3\2\2\2\u02e2"+
		"\u02e0\3\2\2\2\u02e2\u02e3\3\2\2\2\u02e3|\3\2\2\2\u02e4\u02e7\5\u00ef"+
		"x\2\u02e5\u02e7\5\u00f1y\2\u02e6\u02e4\3\2\2\2\u02e6\u02e5\3\2\2\2\u02e7"+
		"~\3\2\2\2\u02e8\u02eb\5}?\2\u02e9\u02eb\5\u00f3z\2\u02ea\u02e8\3\2\2\2"+
		"\u02ea\u02e9\3\2\2\2\u02eb\u02ec\3\2\2\2\u02ec\u02ed\t\7\2\2\u02ed\u0080"+
		"\3\2\2\2\u02ee\u02ef\7\60\2\2\u02ef\u0082\3\2\2\2\u02f0\u02f1\7,\2\2\u02f1"+
		"\u0084\3\2\2\2\u02f2\u02f3\7*\2\2\u02f3\u02f4\bC\3\2\u02f4\u0086\3\2\2"+
		"\2\u02f5\u02f6\7+\2\2\u02f6\u02f7\bD\4\2\u02f7\u0088\3\2\2\2\u02f8\u02f9"+
		"\7.\2\2\u02f9\u008a\3\2\2\2\u02fa\u02fb\7<\2\2\u02fb\u008c\3\2\2\2\u02fc"+
		"\u02fd\7=\2\2\u02fd\u008e\3\2\2\2\u02fe\u02ff\7?\2\2\u02ff\u0090\3\2\2"+
		"\2\u0300\u0301\7]\2\2\u0301\u0302\bI\5\2\u0302\u0092\3\2\2\2\u0303\u0304"+
		"\7_\2\2\u0304\u0305\bJ\6\2\u0305\u0094\3\2\2\2\u0306\u0307\7~\2\2\u0307"+
		"\u0096\3\2\2\2\u0308\u0309\7`\2\2\u0309\u0098\3\2\2\2\u030a\u030b\7(\2"+
		"\2\u030b\u009a\3\2\2\2\u030c\u030d\7>\2\2\u030d\u030e\7>\2\2\u030e\u009c"+
		"\3\2\2\2\u030f\u0310\7@\2\2\u0310\u0311\7@\2\2\u0311\u009e\3\2\2\2\u0312"+
		"\u0313\7-\2\2\u0313\u00a0\3\2\2\2\u0314\u0315\7/\2\2\u0315\u00a2\3\2\2"+
		"\2\u0316\u0317\7\61\2\2\u0317\u00a4\3\2\2\2\u0318\u0319\7\'\2\2\u0319"+
		"\u00a6\3\2\2\2\u031a\u031b\7\61\2\2\u031b\u031c\7\61\2\2\u031c\u00a8\3"+
		"\2\2\2\u031d\u031e\7\u0080\2\2\u031e\u00aa\3\2\2\2\u031f\u0320\7}\2\2"+
		"\u0320\u0321\bV\7\2\u0321\u00ac\3\2\2\2\u0322\u0323\7\177\2\2\u0323\u0324"+
		"\bW\b\2\u0324\u00ae\3\2\2\2\u0325\u0326\7>\2\2\u0326\u00b0\3\2\2\2\u0327"+
		"\u0328\7@\2\2\u0328\u00b2\3\2\2\2\u0329\u032a\7?\2\2\u032a\u032b\7?\2"+
		"\2\u032b\u00b4\3\2\2\2\u032c\u032d\7@\2\2\u032d\u032e\7?\2\2\u032e\u00b6"+
		"\3\2\2\2\u032f\u0330\7>\2\2\u0330\u0331\7?\2\2\u0331\u00b8\3\2\2\2\u0332"+
		"\u0333\7#\2\2\u0333\u0334\7?\2\2\u0334\u00ba\3\2\2\2\u0335\u0336\7B\2"+
		"\2\u0336\u00bc\3\2\2\2\u0337\u0338\7/\2\2\u0338\u0339\7@\2\2\u0339\u00be"+
		"\3\2\2\2\u033a\u033b\7-\2\2\u033b\u033c\7?\2\2\u033c\u00c0\3\2\2\2\u033d"+
		"\u033e\7/\2\2\u033e\u033f\7?\2\2\u033f\u00c2\3\2\2\2\u0340\u0341\7,\2"+
		"\2\u0341\u0342\7?\2\2\u0342\u00c4\3\2\2\2\u0343\u0344\7B\2\2\u0344\u0345"+
		"\7?\2\2\u0345\u00c6\3\2\2\2\u0346\u0347\7\61\2\2\u0347\u0348\7?\2\2\u0348"+
		"\u00c8\3\2\2\2\u0349\u034a\7\'\2\2\u034a\u034b\7?\2\2\u034b\u00ca\3\2"+
		"\2\2\u034c\u034d\7(\2\2\u034d\u034e\7?\2\2\u034e\u00cc\3\2\2\2\u034f\u0350"+
		"\7~\2\2\u0350\u0351\7?\2\2\u0351\u00ce\3\2\2\2\u0352\u0353\7`\2\2\u0353"+
		"\u0354\7?\2\2\u0354\u00d0\3\2\2\2\u0355\u0356\7>\2\2\u0356\u0357\7>\2"+
		"\2\u0357\u0358\7?\2\2\u0358\u00d2\3\2\2\2\u0359\u035a\7@\2\2\u035a\u035b"+
		"\7@\2\2\u035b\u035c\7?\2\2\u035c\u00d4\3\2\2\2\u035d\u035e\7,\2\2\u035e"+
		"\u035f\7,\2\2\u035f\u0360\7?\2\2\u0360\u00d6\3\2\2\2\u0361\u0362\7\61"+
		"\2\2\u0362\u0363\7\61\2\2\u0363\u0364\7?\2\2\u0364\u00d8\3\2\2\2\u0365"+
		"\u0369\5\u0107\u0084\2\u0366\u0369\5\u0109\u0085\2\u0367\u0369\5\u010b"+
		"\u0086\2\u0368\u0365\3\2\2\2\u0368\u0366\3\2\2\2\u0368\u0367\3\2\2\2\u0369"+
		"\u036a\3\2\2\2\u036a\u036b\bm\t\2\u036b\u00da\3\2\2\2\u036c\u0371\7)\2"+
		"\2\u036d\u0370\5\u00e3r\2\u036e\u0370\n\b\2\2\u036f\u036d\3\2\2\2\u036f"+
		"\u036e\3\2\2\2\u0370\u0373\3\2\2\2\u0371\u036f\3\2\2\2\u0371\u0372\3\2"+
		"\2\2\u0372\u0374\3\2\2\2\u0373\u0371\3\2\2\2\u0374\u037f\7)\2\2\u0375"+
		"\u037a\7$\2\2\u0376\u0379\5\u00e3r\2\u0377\u0379\n\t\2\2\u0378\u0376\3"+
		"\2\2\2\u0378\u0377\3\2\2\2\u0379\u037c\3\2\2\2\u037a\u0378\3\2\2\2\u037a"+
		"\u037b\3\2\2\2\u037b\u037d\3\2\2\2\u037c\u037a\3\2\2\2\u037d\u037f\7$"+
		"\2\2\u037e\u036c\3\2\2\2\u037e\u0375\3\2\2\2\u037f\u00dc\3\2\2\2\u0380"+
		"\u0381\7)\2\2\u0381\u0382\7)\2\2\u0382\u0383\7)\2\2\u0383\u0387\3\2\2"+
		"\2\u0384\u0386\5\u00dfp\2\u0385\u0384\3\2\2\2\u0386\u0389\3\2\2\2\u0387"+
		"\u0388\3\2\2\2\u0387\u0385\3\2\2\2\u0388\u038a\3\2\2\2\u0389\u0387\3\2"+
		"\2\2\u038a\u038b\7)\2\2\u038b\u038c\7)\2\2\u038c\u039b\7)\2\2\u038d\u038e"+
		"\7$\2\2\u038e\u038f\7$\2\2\u038f\u0390\7$\2\2\u0390\u0394\3\2\2\2\u0391"+
		"\u0393\5\u00dfp\2\u0392\u0391\3\2\2\2\u0393\u0396\3\2\2\2\u0394\u0395"+
		"\3\2\2\2\u0394\u0392\3\2\2\2\u0395\u0397\3\2\2\2\u0396\u0394\3\2\2\2\u0397"+
		"\u0398\7$\2\2\u0398\u0399\7$\2\2\u0399\u039b\7$\2\2\u039a\u0380\3\2\2"+
		"\2\u039a\u038d\3\2\2\2\u039b\u00de\3\2\2\2\u039c\u039f\5\u00e1q\2\u039d"+
		"\u039f\5\u00e3r\2\u039e\u039c\3\2\2\2\u039e\u039d\3\2\2\2\u039f\u00e0"+
		"\3\2\2\2\u03a0\u03a1\n\n\2\2\u03a1\u00e2\3\2\2\2\u03a2\u03a3\7^\2\2\u03a3"+
		"\u03a4\13\2\2\2\u03a4\u00e4\3\2\2\2\u03a5\u03a6\t\13\2\2\u03a6\u00e6\3"+
		"\2\2\2\u03a7\u03a8\t\f\2\2\u03a8\u00e8\3\2\2\2\u03a9\u03aa\t\r\2\2\u03aa"+
		"\u00ea\3\2\2\2\u03ab\u03ac\t\16\2\2\u03ac\u00ec\3\2\2\2\u03ad\u03ae\t"+
		"\17\2\2\u03ae\u00ee\3\2\2\2\u03af\u03b1\5\u00f3z\2\u03b0\u03af\3\2\2\2"+
		"\u03b0\u03b1\3\2\2\2\u03b1\u03b2\3\2\2\2\u03b2\u03b7\5\u00f5{\2\u03b3"+
		"\u03b4\5\u00f3z\2\u03b4\u03b5\7\60\2\2\u03b5\u03b7\3\2\2\2\u03b6\u03b0"+
		"\3\2\2\2\u03b6\u03b3\3\2\2\2\u03b7\u00f0\3\2\2\2\u03b8\u03bb\5\u00f3z"+
		"\2\u03b9\u03bb\5\u00efx\2\u03ba\u03b8\3\2\2\2\u03ba\u03b9\3\2\2\2\u03bb"+
		"\u03bc\3\2\2\2\u03bc\u03bd\5\u00f7|\2\u03bd\u00f2\3\2\2\2\u03be\u03c0"+
		"\5\u00e7t\2\u03bf\u03be\3\2\2\2\u03c0\u03c1\3\2\2\2\u03c1\u03bf\3\2\2"+
		"\2\u03c1\u03c2\3\2\2\2\u03c2\u00f4\3\2\2\2\u03c3\u03c5\7\60\2\2\u03c4"+
		"\u03c6\5\u00e7t\2\u03c5\u03c4\3\2\2\2\u03c6\u03c7\3\2\2\2\u03c7\u03c5"+
		"\3\2\2\2\u03c7\u03c8\3\2\2\2\u03c8\u00f6\3\2\2\2\u03c9\u03cb\t\20\2\2"+
		"\u03ca\u03cc\t\21\2\2\u03cb\u03ca\3\2\2\2\u03cb\u03cc\3\2\2\2\u03cc\u03ce"+
		"\3\2\2\2\u03cd\u03cf\5\u00e7t\2\u03ce\u03cd\3\2\2\2\u03cf\u03d0\3\2\2"+
		"\2\u03d0\u03ce\3\2\2\2\u03d0\u03d1\3\2\2\2\u03d1\u00f8\3\2\2\2\u03d2\u03d7"+
		"\7)\2\2\u03d3\u03d6\5\u00ff\u0080\2\u03d4\u03d6\5\u0105\u0083\2\u03d5"+
		"\u03d3\3\2\2\2\u03d5\u03d4\3\2\2\2\u03d6\u03d9\3\2\2\2\u03d7\u03d5\3\2"+
		"\2\2\u03d7\u03d8\3\2\2\2\u03d8\u03da\3\2\2\2\u03d9\u03d7\3\2\2\2\u03da"+
		"\u03e5\7)\2\2\u03db\u03e0\7$\2\2\u03dc\u03df\5\u0101\u0081\2\u03dd\u03df"+
		"\5\u0105\u0083\2\u03de\u03dc\3\2\2\2\u03de\u03dd\3\2\2\2\u03df\u03e2\3"+
		"\2\2\2\u03e0\u03de\3\2\2\2\u03e0\u03e1\3\2\2\2\u03e1\u03e3\3\2\2\2\u03e2"+
		"\u03e0\3\2\2\2\u03e3\u03e5\7$\2\2\u03e4\u03d2\3\2\2\2\u03e4\u03db\3\2"+
		"\2\2\u03e5\u00fa\3\2\2\2\u03e6\u03e7\7)\2\2\u03e7\u03e8\7)\2\2\u03e8\u03e9"+
		"\7)\2\2\u03e9\u03ed\3\2\2\2\u03ea\u03ec\5\u00fd\177\2\u03eb\u03ea\3\2"+
		"\2\2\u03ec\u03ef\3\2\2\2\u03ed\u03ee\3\2\2\2\u03ed\u03eb\3\2\2\2\u03ee"+
		"\u03f0\3\2\2\2\u03ef\u03ed\3\2\2\2\u03f0\u03f1\7)\2\2\u03f1\u03f2\7)\2"+
		"\2\u03f2\u0401\7)\2\2\u03f3\u03f4\7$\2\2\u03f4\u03f5\7$\2\2\u03f5\u03f6"+
		"\7$\2\2\u03f6\u03fa\3\2\2\2\u03f7\u03f9\5\u00fd\177\2\u03f8\u03f7\3\2"+
		"\2\2\u03f9\u03fc\3\2\2\2\u03fa\u03fb\3\2\2\2\u03fa\u03f8\3\2\2\2\u03fb"+
		"\u03fd\3\2\2\2\u03fc\u03fa\3\2\2\2\u03fd\u03fe\7$\2\2\u03fe\u03ff\7$\2"+
		"\2\u03ff\u0401\7$\2\2\u0400\u03e6\3\2\2\2\u0400\u03f3\3\2\2\2\u0401\u00fc"+
		"\3\2\2\2\u0402\u0405\5\u0103\u0082\2\u0403\u0405\5\u0105\u0083\2\u0404"+
		"\u0402\3\2\2\2\u0404\u0403\3\2\2\2\u0405\u00fe\3\2\2\2\u0406\u0408\t\22"+
		"\2\2\u0407\u0406\3\2\2\2\u0408\u0100\3\2\2\2\u0409\u040b\t\23\2\2\u040a"+
		"\u0409\3\2\2\2\u040b\u0102\3\2\2\2\u040c\u040e\t\24\2\2\u040d\u040c\3"+
		"\2\2\2\u040e\u0104\3\2\2\2\u040f\u0410\7^\2\2\u0410\u0411\t\25\2\2\u0411"+
		"\u0106\3\2\2\2\u0412\u0414\t\26\2\2\u0413\u0412\3\2\2\2\u0414\u0415\3"+
		"\2\2\2\u0415\u0413\3\2\2\2\u0415\u0416\3\2\2\2\u0416\u0108\3\2\2\2\u0417"+
		"\u041b\7%\2\2\u0418\u041a\n\27\2\2\u0419\u0418\3\2\2\2\u041a\u041d\3\2"+
		"\2\2\u041b\u0419\3\2\2\2\u041b\u041c\3\2\2\2\u041c\u010a\3\2\2\2\u041d"+
		"\u041b\3\2\2\2\u041e\u0420\7^\2\2\u041f\u0421\5\u0107\u0084\2\u0420\u041f"+
		"\3\2\2\2\u0420\u0421\3\2\2\2\u0421\u0427\3\2\2\2\u0422\u0424\7\17\2\2"+
		"\u0423\u0422\3\2\2\2\u0423\u0424\3\2\2\2\u0424\u0425\3\2\2\2\u0425\u0428"+
		"\7\f\2\2\u0426\u0428\7\17\2\2\u0427\u0423\3\2\2\2\u0427\u0426\3\2\2\2"+
		"\u0428\u010c\3\2\2\2\u0429\u042b\t\30\2\2\u042a\u0429\3\2\2\2\u042b\u010e"+
		"\3\2\2\2\u042c\u042f\5\u010d\u0087\2\u042d\u042f\t\f\2\2\u042e\u042c\3"+
		"\2\2\2\u042e\u042d\3\2\2\2\u042f\u0110\3\2\2\29\2\u029b\u029f\u02a2\u02a4"+
		"\u02ac\u02b0\u02b3\u02b7\u02bb\u02bf\u02c5\u02cb\u02cd\u02d4\u02db\u02e2"+
		"\u02e6\u02ea\u0368\u036f\u0371\u0378\u037a\u037e\u0387\u0394\u039a\u039e"+
		"\u03b0\u03b6\u03ba\u03c1\u03c7\u03cb\u03d0\u03d5\u03d7\u03de\u03e0\u03e4"+
		"\u03ed\u03fa\u0400\u0404\u0407\u040a\u040d\u0415\u041b\u0420\u0423\u0427"+
		"\u042a\u042e\n\3\67\2\3C\3\3D\4\3I\5\3J\6\3V\7\3W\b\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
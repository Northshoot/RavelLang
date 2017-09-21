// Generated from /Users/larry/workspace/01-Ravel/ravellang/Ravel.g4 by ANTLR 4.7
package ai.harmony.antlr4;

import ai.harmony.ravel.compiler.scope.*;
import ai.harmony.ravel.compiler.symbol.*;
import ai.harmony.ravel.compiler.types.Type;

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
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, MODEL=13, SPACE=14, CONTROLLER=15, VIEW=16, 
		FLOW=17, LOCAL=18, STREAMING=19, REPLICATED=20, INTERFACE=21, DEF=22, 
		EVENT=23, COMMAND=24, FROM=25, IMPORT=26, AS=27, ASSERT=28, RETURN=29, 
		TRUE=30, FALSE=31, IF=32, ELIF=33, ELSE=34, FOR=35, WHILE=36, AND=37, 
		NOT=38, OR=39, IN=40, IS=41, DELETE=42, PASS=43, CONTINUE=44, BREAK=45, 
		NONE=46, NEWLINE=47, STRING_LITERAL=48, DECIMAL_INTEGER=49, OCT_INTEGER=50, 
		HEX_INTEGER=51, BIN_INTEGER=52, FLOAT_NUMBER=53, NullLiteral=54, DOT=55, 
		ELLIPSIS=56, STAR=57, OPEN_PAREN=58, CLOSE_PAREN=59, COMMA=60, COLON=61, 
		SEMI_COLON=62, POWER=63, ASSIGN=64, OPEN_BRACK=65, CLOSE_BRACK=66, OR_OP=67, 
		XOR=68, AND_OP=69, LEFT_SHIFT=70, RIGHT_SHIFT=71, ADD=72, MINUS=73, DIV=74, 
		MOD=75, IDIV=76, NOT_OP=77, OPEN_BRACE=78, CLOSE_BRACE=79, LT=80, GT=81, 
		EQUAL=82, GE=83, LE=84, NOTEQUAL=85, AT=86, ARROW=87, ADD_ASSIGN=88, SUB_ASSIGN=89, 
		MULT_ASSIGN=90, AT_ASSIGN=91, DIV_ASSIGN=92, MOD_ASSIGN=93, AND_ASSIGN=94, 
		OR_ASSIGN=95, XOR_ASSIGN=96, LEFT_SHIFT_ASSIGN=97, RIGHT_SHIFT_ASSIGN=98, 
		POWER_ASSIGN=99, IDIV_ASSIGN=100, Identifier=101, SKIP_=102, UNKNOWN_CHAR=103;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "MODEL", "SPACE", "CONTROLLER", "VIEW", "FLOW", 
		"LOCAL", "STREAMING", "REPLICATED", "INTERFACE", "DEF", "EVENT", "COMMAND", 
		"FROM", "IMPORT", "AS", "ASSERT", "RETURN", "TRUE", "FALSE", "IF", "ELIF", 
		"ELSE", "FOR", "WHILE", "AND", "NOT", "OR", "IN", "IS", "DELETE", "PASS", 
		"CONTINUE", "BREAK", "NONE", "NEWLINE", "STRING_LITERAL", "STRING_ESCAPE_SEQ", 
		"DECIMAL_INTEGER", "OCT_INTEGER", "HEX_INTEGER", "BIN_INTEGER", "NON_ZERO_DIGIT", 
		"DIGIT", "OCT_DIGIT", "HEX_DIGIT", "BIN_DIGIT", "FLOAT_NUMBER", "POINT_FLOAT", 
		"EXPONENT_FLOAT", "INT_PART", "FRACTION", "EXPONENT", "NullLiteral", "DOT", 
		"ELLIPSIS", "STAR", "OPEN_PAREN", "CLOSE_PAREN", "COMMA", "COLON", "SEMI_COLON", 
		"POWER", "ASSIGN", "OPEN_BRACK", "CLOSE_BRACK", "OR_OP", "XOR", "AND_OP", 
		"LEFT_SHIFT", "RIGHT_SHIFT", "ADD", "MINUS", "DIV", "MOD", "IDIV", "NOT_OP", 
		"OPEN_BRACE", "CLOSE_BRACE", "LT", "GT", "EQUAL", "GE", "LE", "NOTEQUAL", 
		"AT", "ARROW", "ADD_ASSIGN", "SUB_ASSIGN", "MULT_ASSIGN", "AT_ASSIGN", 
		"DIV_ASSIGN", "MOD_ASSIGN", "AND_ASSIGN", "OR_ASSIGN", "XOR_ASSIGN", "LEFT_SHIFT_ASSIGN", 
		"RIGHT_SHIFT_ASSIGN", "POWER_ASSIGN", "IDIV_ASSIGN", "Identifier", "ID_START", 
		"ID_CONTINUE", "SKIP_", "UNKNOWN_CHAR", "SPACES", "COMMENT", "LINE_JOINING"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'platform:'", "'models:'", "'controllers:'", "'interfaces:'", "'views:'", 
		"'implementation:'", "'configuration:'", "'use:'", "'properties:'", "'schema:'", 
		"'to'", "'step'", "'model'", "'space'", "'controller'", "'view'", "'flow'", 
		"'local'", "'streaming'", "'replicated'", "'interface'", "'def'", "'event'", 
		"'command'", "'from'", "'import'", "'as'", "'assert'", "'return'", "'True'", 
		"'False'", "'if'", "'elif'", "'else'", "'for'", "'while'", "'and'", "'not'", 
		"'or'", "'in'", "'is'", "'del'", "'pass'", "'continue'", "'break'", "'None'", 
		null, null, null, null, null, null, null, "'null'", "'.'", "'...'", "'*'", 
		"'('", "')'", "','", "':'", "';'", "'**'", "'='", "'['", "']'", "'|'", 
		"'^'", "'&'", "'<<'", "'>>'", "'+'", "'-'", "'/'", "'%'", "'//'", "'~'", 
		"'{'", "'}'", "'<'", "'>'", "'=='", "'>='", "'<='", null, "'@'", "'->'", 
		"'+='", "'-='", "'*='", "'@='", "'/='", "'%='", "'&='", "'|='", "'^='", 
		"'<<='", "'>>='", "'**='", "'//='"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, "MODEL", "SPACE", "CONTROLLER", "VIEW", "FLOW", "LOCAL", "STREAMING", 
		"REPLICATED", "INTERFACE", "DEF", "EVENT", "COMMAND", "FROM", "IMPORT", 
		"AS", "ASSERT", "RETURN", "TRUE", "FALSE", "IF", "ELIF", "ELSE", "FOR", 
		"WHILE", "AND", "NOT", "OR", "IN", "IS", "DELETE", "PASS", "CONTINUE", 
		"BREAK", "NONE", "NEWLINE", "STRING_LITERAL", "DECIMAL_INTEGER", "OCT_INTEGER", 
		"HEX_INTEGER", "BIN_INTEGER", "FLOAT_NUMBER", "NullLiteral", "DOT", "ELLIPSIS", 
		"STAR", "OPEN_PAREN", "CLOSE_PAREN", "COMMA", "COLON", "SEMI_COLON", "POWER", 
		"ASSIGN", "OPEN_BRACK", "CLOSE_BRACK", "OR_OP", "XOR", "AND_OP", "LEFT_SHIFT", 
		"RIGHT_SHIFT", "ADD", "MINUS", "DIV", "MOD", "IDIV", "NOT_OP", "OPEN_BRACE", 
		"CLOSE_BRACE", "LT", "GT", "EQUAL", "GE", "LE", "NOTEQUAL", "AT", "ARROW", 
		"ADD_ASSIGN", "SUB_ASSIGN", "MULT_ASSIGN", "AT_ASSIGN", "DIV_ASSIGN", 
		"MOD_ASSIGN", "AND_ASSIGN", "OR_ASSIGN", "XOR_ASSIGN", "LEFT_SHIFT_ASSIGN", 
		"RIGHT_SHIFT_ASSIGN", "POWER_ASSIGN", "IDIV_ASSIGN", "Identifier", "SKIP_", 
		"UNKNOWN_CHAR"
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
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 46:
			NEWLINE_action((RuleContext)_localctx, actionIndex);
			break;
		case 68:
			OPEN_PAREN_action((RuleContext)_localctx, actionIndex);
			break;
		case 69:
			CLOSE_PAREN_action((RuleContext)_localctx, actionIndex);
			break;
		case 75:
			OPEN_BRACK_action((RuleContext)_localctx, actionIndex);
			break;
		case 76:
			CLOSE_BRACK_action((RuleContext)_localctx, actionIndex);
			break;
		case 88:
			OPEN_BRACE_action((RuleContext)_localctx, actionIndex);
			break;
		case 89:
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
		case 46:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2i\u0357\b\1\4\2\t"+
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
		"w\tw\4x\tx\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t"+
		"\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22"+
		"\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27"+
		"\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31"+
		"\3\31\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\34"+
		"\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36"+
		"\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3!\3!\3!\3\"\3\""+
		"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3$\3$\3$\3$\3%\3%\3%\3%\3%\3%\3&\3&\3&\3&"+
		"\3\'\3\'\3\'\3\'\3(\3(\3(\3)\3)\3)\3*\3*\3*\3+\3+\3+\3+\3,\3,\3,\3,\3"+
		",\3-\3-\3-\3-\3-\3-\3-\3-\3-\3.\3.\3.\3.\3.\3.\3/\3/\3/\3/\3/\3\60\3\60"+
		"\3\60\5\60\u022c\n\60\3\60\3\60\5\60\u0230\n\60\3\60\5\60\u0233\n\60\5"+
		"\60\u0235\n\60\3\60\3\60\3\61\3\61\3\61\7\61\u023c\n\61\f\61\16\61\u023f"+
		"\13\61\3\61\3\61\3\61\3\61\7\61\u0245\n\61\f\61\16\61\u0248\13\61\3\61"+
		"\5\61\u024b\n\61\3\62\3\62\3\62\3\63\3\63\7\63\u0252\n\63\f\63\16\63\u0255"+
		"\13\63\3\63\6\63\u0258\n\63\r\63\16\63\u0259\5\63\u025c\n\63\3\64\3\64"+
		"\3\64\6\64\u0261\n\64\r\64\16\64\u0262\3\65\3\65\3\65\6\65\u0268\n\65"+
		"\r\65\16\65\u0269\3\66\3\66\3\66\6\66\u026f\n\66\r\66\16\66\u0270\3\67"+
		"\3\67\38\38\39\39\3:\3:\3;\3;\3<\3<\5<\u027f\n<\3=\5=\u0282\n=\3=\3=\3"+
		"=\3=\5=\u0288\n=\3>\3>\5>\u028c\n>\3>\3>\3?\6?\u0291\n?\r?\16?\u0292\3"+
		"@\3@\6@\u0297\n@\r@\16@\u0298\3A\3A\5A\u029d\nA\3A\6A\u02a0\nA\rA\16A"+
		"\u02a1\3B\3B\3B\3B\3B\3C\3C\3D\3D\3D\3D\3E\3E\3F\3F\3F\3G\3G\3G\3H\3H"+
		"\3I\3I\3J\3J\3K\3K\3K\3L\3L\3M\3M\3M\3N\3N\3N\3O\3O\3P\3P\3Q\3Q\3R\3R"+
		"\3R\3S\3S\3S\3T\3T\3U\3U\3V\3V\3W\3W\3X\3X\3X\3Y\3Y\3Z\3Z\3Z\3[\3[\3["+
		"\3\\\3\\\3]\3]\3^\3^\3^\3_\3_\3_\3`\3`\3`\3a\3a\3a\3a\5a\u02f8\na\3b\3"+
		"b\3c\3c\3c\3d\3d\3d\3e\3e\3e\3f\3f\3f\3g\3g\3g\3h\3h\3h\3i\3i\3i\3j\3"+
		"j\3j\3k\3k\3k\3l\3l\3l\3m\3m\3m\3m\3n\3n\3n\3n\3o\3o\3o\3o\3p\3p\3p\3"+
		"p\3q\3q\7q\u032c\nq\fq\16q\u032f\13q\3r\5r\u0332\nr\3s\3s\5s\u0336\ns"+
		"\3t\3t\3t\5t\u033b\nt\3t\3t\3u\3u\3v\6v\u0342\nv\rv\16v\u0343\3w\3w\7"+
		"w\u0348\nw\fw\16w\u034b\13w\3x\3x\5x\u034f\nx\3x\5x\u0352\nx\3x\3x\5x"+
		"\u0356\nx\2\2y\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16"+
		"\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34"+
		"\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\2e\63g\64"+
		"i\65k\66m\2o\2q\2s\2u\2w\67y\2{\2}\2\177\2\u0081\2\u00838\u00859\u0087"+
		":\u0089;\u008b<\u008d=\u008f>\u0091?\u0093@\u0095A\u0097B\u0099C\u009b"+
		"D\u009dE\u009fF\u00a1G\u00a3H\u00a5I\u00a7J\u00a9K\u00abL\u00adM\u00af"+
		"N\u00b1O\u00b3P\u00b5Q\u00b7R\u00b9S\u00bbT\u00bdU\u00bfV\u00c1W\u00c3"+
		"X\u00c5Y\u00c7Z\u00c9[\u00cb\\\u00cd]\u00cf^\u00d1_\u00d3`\u00d5a\u00d7"+
		"b\u00d9c\u00dbd\u00dde\u00dff\u00e1g\u00e3\2\u00e5\2\u00e7h\u00e9i\u00eb"+
		"\2\u00ed\2\u00ef\2\3\2\21\6\2\f\f\17\17))^^\6\2\f\f\17\17$$^^\4\2QQqq"+
		"\4\2ZZzz\4\2DDdd\3\2\63;\3\2\62;\3\2\629\5\2\62;CHch\3\2\62\63\4\2GGg"+
		"g\4\2--//\5\2C\\aac|\4\2\13\13\"\"\4\2\f\f\17\17\2\u0367\2\3\3\2\2\2\2"+
		"\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2"+
		"\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2"+
		"\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2"+
		"\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2"+
		"\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2"+
		"\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2"+
		"K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3"+
		"\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2e\3\2\2"+
		"\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2w\3\2\2\2\2\u0083\3\2\2\2\2\u0085"+
		"\3\2\2\2\2\u0087\3\2\2\2\2\u0089\3\2\2\2\2\u008b\3\2\2\2\2\u008d\3\2\2"+
		"\2\2\u008f\3\2\2\2\2\u0091\3\2\2\2\2\u0093\3\2\2\2\2\u0095\3\2\2\2\2\u0097"+
		"\3\2\2\2\2\u0099\3\2\2\2\2\u009b\3\2\2\2\2\u009d\3\2\2\2\2\u009f\3\2\2"+
		"\2\2\u00a1\3\2\2\2\2\u00a3\3\2\2\2\2\u00a5\3\2\2\2\2\u00a7\3\2\2\2\2\u00a9"+
		"\3\2\2\2\2\u00ab\3\2\2\2\2\u00ad\3\2\2\2\2\u00af\3\2\2\2\2\u00b1\3\2\2"+
		"\2\2\u00b3\3\2\2\2\2\u00b5\3\2\2\2\2\u00b7\3\2\2\2\2\u00b9\3\2\2\2\2\u00bb"+
		"\3\2\2\2\2\u00bd\3\2\2\2\2\u00bf\3\2\2\2\2\u00c1\3\2\2\2\2\u00c3\3\2\2"+
		"\2\2\u00c5\3\2\2\2\2\u00c7\3\2\2\2\2\u00c9\3\2\2\2\2\u00cb\3\2\2\2\2\u00cd"+
		"\3\2\2\2\2\u00cf\3\2\2\2\2\u00d1\3\2\2\2\2\u00d3\3\2\2\2\2\u00d5\3\2\2"+
		"\2\2\u00d7\3\2\2\2\2\u00d9\3\2\2\2\2\u00db\3\2\2\2\2\u00dd\3\2\2\2\2\u00df"+
		"\3\2\2\2\2\u00e1\3\2\2\2\2\u00e7\3\2\2\2\2\u00e9\3\2\2\2\3\u00f1\3\2\2"+
		"\2\5\u00fb\3\2\2\2\7\u0103\3\2\2\2\t\u0110\3\2\2\2\13\u011c\3\2\2\2\r"+
		"\u0123\3\2\2\2\17\u0133\3\2\2\2\21\u0142\3\2\2\2\23\u0147\3\2\2\2\25\u0153"+
		"\3\2\2\2\27\u015b\3\2\2\2\31\u015e\3\2\2\2\33\u0163\3\2\2\2\35\u0169\3"+
		"\2\2\2\37\u016f\3\2\2\2!\u017a\3\2\2\2#\u017f\3\2\2\2%\u0184\3\2\2\2\'"+
		"\u018a\3\2\2\2)\u0194\3\2\2\2+\u019f\3\2\2\2-\u01a9\3\2\2\2/\u01ad\3\2"+
		"\2\2\61\u01b3\3\2\2\2\63\u01bb\3\2\2\2\65\u01c0\3\2\2\2\67\u01c7\3\2\2"+
		"\29\u01ca\3\2\2\2;\u01d1\3\2\2\2=\u01d8\3\2\2\2?\u01dd\3\2\2\2A\u01e3"+
		"\3\2\2\2C\u01e6\3\2\2\2E\u01eb\3\2\2\2G\u01f0\3\2\2\2I\u01f4\3\2\2\2K"+
		"\u01fa\3\2\2\2M\u01fe\3\2\2\2O\u0202\3\2\2\2Q\u0205\3\2\2\2S\u0208\3\2"+
		"\2\2U\u020b\3\2\2\2W\u020f\3\2\2\2Y\u0214\3\2\2\2[\u021d\3\2\2\2]\u0223"+
		"\3\2\2\2_\u0234\3\2\2\2a\u024a\3\2\2\2c\u024c\3\2\2\2e\u025b\3\2\2\2g"+
		"\u025d\3\2\2\2i\u0264\3\2\2\2k\u026b\3\2\2\2m\u0272\3\2\2\2o\u0274\3\2"+
		"\2\2q\u0276\3\2\2\2s\u0278\3\2\2\2u\u027a\3\2\2\2w\u027e\3\2\2\2y\u0287"+
		"\3\2\2\2{\u028b\3\2\2\2}\u0290\3\2\2\2\177\u0294\3\2\2\2\u0081\u029a\3"+
		"\2\2\2\u0083\u02a3\3\2\2\2\u0085\u02a8\3\2\2\2\u0087\u02aa\3\2\2\2\u0089"+
		"\u02ae\3\2\2\2\u008b\u02b0\3\2\2\2\u008d\u02b3\3\2\2\2\u008f\u02b6\3\2"+
		"\2\2\u0091\u02b8\3\2\2\2\u0093\u02ba\3\2\2\2\u0095\u02bc\3\2\2\2\u0097"+
		"\u02bf\3\2\2\2\u0099\u02c1\3\2\2\2\u009b\u02c4\3\2\2\2\u009d\u02c7\3\2"+
		"\2\2\u009f\u02c9\3\2\2\2\u00a1\u02cb\3\2\2\2\u00a3\u02cd\3\2\2\2\u00a5"+
		"\u02d0\3\2\2\2\u00a7\u02d3\3\2\2\2\u00a9\u02d5\3\2\2\2\u00ab\u02d7\3\2"+
		"\2\2\u00ad\u02d9\3\2\2\2\u00af\u02db\3\2\2\2\u00b1\u02de\3\2\2\2\u00b3"+
		"\u02e0\3\2\2\2\u00b5\u02e3\3\2\2\2\u00b7\u02e6\3\2\2\2\u00b9\u02e8\3\2"+
		"\2\2\u00bb\u02ea\3\2\2\2\u00bd\u02ed\3\2\2\2\u00bf\u02f0\3\2\2\2\u00c1"+
		"\u02f7\3\2\2\2\u00c3\u02f9\3\2\2\2\u00c5\u02fb\3\2\2\2\u00c7\u02fe\3\2"+
		"\2\2\u00c9\u0301\3\2\2\2\u00cb\u0304\3\2\2\2\u00cd\u0307\3\2\2\2\u00cf"+
		"\u030a\3\2\2\2\u00d1\u030d\3\2\2\2\u00d3\u0310\3\2\2\2\u00d5\u0313\3\2"+
		"\2\2\u00d7\u0316\3\2\2\2\u00d9\u0319\3\2\2\2\u00db\u031d\3\2\2\2\u00dd"+
		"\u0321\3\2\2\2\u00df\u0325\3\2\2\2\u00e1\u0329\3\2\2\2\u00e3\u0331\3\2"+
		"\2\2\u00e5\u0335\3\2\2\2\u00e7\u033a\3\2\2\2\u00e9\u033e\3\2\2\2\u00eb"+
		"\u0341\3\2\2\2\u00ed\u0345\3\2\2\2\u00ef\u034c\3\2\2\2\u00f1\u00f2\7r"+
		"\2\2\u00f2\u00f3\7n\2\2\u00f3\u00f4\7c\2\2\u00f4\u00f5\7v\2\2\u00f5\u00f6"+
		"\7h\2\2\u00f6\u00f7\7q\2\2\u00f7\u00f8\7t\2\2\u00f8\u00f9\7o\2\2\u00f9"+
		"\u00fa\7<\2\2\u00fa\4\3\2\2\2\u00fb\u00fc\7o\2\2\u00fc\u00fd\7q\2\2\u00fd"+
		"\u00fe\7f\2\2\u00fe\u00ff\7g\2\2\u00ff\u0100\7n\2\2\u0100\u0101\7u\2\2"+
		"\u0101\u0102\7<\2\2\u0102\6\3\2\2\2\u0103\u0104\7e\2\2\u0104\u0105\7q"+
		"\2\2\u0105\u0106\7p\2\2\u0106\u0107\7v\2\2\u0107\u0108\7t\2\2\u0108\u0109"+
		"\7q\2\2\u0109\u010a\7n\2\2\u010a\u010b\7n\2\2\u010b\u010c\7g\2\2\u010c"+
		"\u010d\7t\2\2\u010d\u010e\7u\2\2\u010e\u010f\7<\2\2\u010f\b\3\2\2\2\u0110"+
		"\u0111\7k\2\2\u0111\u0112\7p\2\2\u0112\u0113\7v\2\2\u0113\u0114\7g\2\2"+
		"\u0114\u0115\7t\2\2\u0115\u0116\7h\2\2\u0116\u0117\7c\2\2\u0117\u0118"+
		"\7e\2\2\u0118\u0119\7g\2\2\u0119\u011a\7u\2\2\u011a\u011b\7<\2\2\u011b"+
		"\n\3\2\2\2\u011c\u011d\7x\2\2\u011d\u011e\7k\2\2\u011e\u011f\7g\2\2\u011f"+
		"\u0120\7y\2\2\u0120\u0121\7u\2\2\u0121\u0122\7<\2\2\u0122\f\3\2\2\2\u0123"+
		"\u0124\7k\2\2\u0124\u0125\7o\2\2\u0125\u0126\7r\2\2\u0126\u0127\7n\2\2"+
		"\u0127\u0128\7g\2\2\u0128\u0129\7o\2\2\u0129\u012a\7g\2\2\u012a\u012b"+
		"\7p\2\2\u012b\u012c\7v\2\2\u012c\u012d\7c\2\2\u012d\u012e\7v\2\2\u012e"+
		"\u012f\7k\2\2\u012f\u0130\7q\2\2\u0130\u0131\7p\2\2\u0131\u0132\7<\2\2"+
		"\u0132\16\3\2\2\2\u0133\u0134\7e\2\2\u0134\u0135\7q\2\2\u0135\u0136\7"+
		"p\2\2\u0136\u0137\7h\2\2\u0137\u0138\7k\2\2\u0138\u0139\7i\2\2\u0139\u013a"+
		"\7w\2\2\u013a\u013b\7t\2\2\u013b\u013c\7c\2\2\u013c\u013d\7v\2\2\u013d"+
		"\u013e\7k\2\2\u013e\u013f\7q\2\2\u013f\u0140\7p\2\2\u0140\u0141\7<\2\2"+
		"\u0141\20\3\2\2\2\u0142\u0143\7w\2\2\u0143\u0144\7u\2\2\u0144\u0145\7"+
		"g\2\2\u0145\u0146\7<\2\2\u0146\22\3\2\2\2\u0147\u0148\7r\2\2\u0148\u0149"+
		"\7t\2\2\u0149\u014a\7q\2\2\u014a\u014b\7r\2\2\u014b\u014c\7g\2\2\u014c"+
		"\u014d\7t\2\2\u014d\u014e\7v\2\2\u014e\u014f\7k\2\2\u014f\u0150\7g\2\2"+
		"\u0150\u0151\7u\2\2\u0151\u0152\7<\2\2\u0152\24\3\2\2\2\u0153\u0154\7"+
		"u\2\2\u0154\u0155\7e\2\2\u0155\u0156\7j\2\2\u0156\u0157\7g\2\2\u0157\u0158"+
		"\7o\2\2\u0158\u0159\7c\2\2\u0159\u015a\7<\2\2\u015a\26\3\2\2\2\u015b\u015c"+
		"\7v\2\2\u015c\u015d\7q\2\2\u015d\30\3\2\2\2\u015e\u015f\7u\2\2\u015f\u0160"+
		"\7v\2\2\u0160\u0161\7g\2\2\u0161\u0162\7r\2\2\u0162\32\3\2\2\2\u0163\u0164"+
		"\7o\2\2\u0164\u0165\7q\2\2\u0165\u0166\7f\2\2\u0166\u0167\7g\2\2\u0167"+
		"\u0168\7n\2\2\u0168\34\3\2\2\2\u0169\u016a\7u\2\2\u016a\u016b\7r\2\2\u016b"+
		"\u016c\7c\2\2\u016c\u016d\7e\2\2\u016d\u016e\7g\2\2\u016e\36\3\2\2\2\u016f"+
		"\u0170\7e\2\2\u0170\u0171\7q\2\2\u0171\u0172\7p\2\2\u0172\u0173\7v\2\2"+
		"\u0173\u0174\7t\2\2\u0174\u0175\7q\2\2\u0175\u0176\7n\2\2\u0176\u0177"+
		"\7n\2\2\u0177\u0178\7g\2\2\u0178\u0179\7t\2\2\u0179 \3\2\2\2\u017a\u017b"+
		"\7x\2\2\u017b\u017c\7k\2\2\u017c\u017d\7g\2\2\u017d\u017e\7y\2\2\u017e"+
		"\"\3\2\2\2\u017f\u0180\7h\2\2\u0180\u0181\7n\2\2\u0181\u0182\7q\2\2\u0182"+
		"\u0183\7y\2\2\u0183$\3\2\2\2\u0184\u0185\7n\2\2\u0185\u0186\7q\2\2\u0186"+
		"\u0187\7e\2\2\u0187\u0188\7c\2\2\u0188\u0189\7n\2\2\u0189&\3\2\2\2\u018a"+
		"\u018b\7u\2\2\u018b\u018c\7v\2\2\u018c\u018d\7t\2\2\u018d\u018e\7g\2\2"+
		"\u018e\u018f\7c\2\2\u018f\u0190\7o\2\2\u0190\u0191\7k\2\2\u0191\u0192"+
		"\7p\2\2\u0192\u0193\7i\2\2\u0193(\3\2\2\2\u0194\u0195\7t\2\2\u0195\u0196"+
		"\7g\2\2\u0196\u0197\7r\2\2\u0197\u0198\7n\2\2\u0198\u0199\7k\2\2\u0199"+
		"\u019a\7e\2\2\u019a\u019b\7c\2\2\u019b\u019c\7v\2\2\u019c\u019d\7g\2\2"+
		"\u019d\u019e\7f\2\2\u019e*\3\2\2\2\u019f\u01a0\7k\2\2\u01a0\u01a1\7p\2"+
		"\2\u01a1\u01a2\7v\2\2\u01a2\u01a3\7g\2\2\u01a3\u01a4\7t\2\2\u01a4\u01a5"+
		"\7h\2\2\u01a5\u01a6\7c\2\2\u01a6\u01a7\7e\2\2\u01a7\u01a8\7g\2\2\u01a8"+
		",\3\2\2\2\u01a9\u01aa\7f\2\2\u01aa\u01ab\7g\2\2\u01ab\u01ac\7h\2\2\u01ac"+
		".\3\2\2\2\u01ad\u01ae\7g\2\2\u01ae\u01af\7x\2\2\u01af\u01b0\7g\2\2\u01b0"+
		"\u01b1\7p\2\2\u01b1\u01b2\7v\2\2\u01b2\60\3\2\2\2\u01b3\u01b4\7e\2\2\u01b4"+
		"\u01b5\7q\2\2\u01b5\u01b6\7o\2\2\u01b6\u01b7\7o\2\2\u01b7\u01b8\7c\2\2"+
		"\u01b8\u01b9\7p\2\2\u01b9\u01ba\7f\2\2\u01ba\62\3\2\2\2\u01bb\u01bc\7"+
		"h\2\2\u01bc\u01bd\7t\2\2\u01bd\u01be\7q\2\2\u01be\u01bf\7o\2\2\u01bf\64"+
		"\3\2\2\2\u01c0\u01c1\7k\2\2\u01c1\u01c2\7o\2\2\u01c2\u01c3\7r\2\2\u01c3"+
		"\u01c4\7q\2\2\u01c4\u01c5\7t\2\2\u01c5\u01c6\7v\2\2\u01c6\66\3\2\2\2\u01c7"+
		"\u01c8\7c\2\2\u01c8\u01c9\7u\2\2\u01c98\3\2\2\2\u01ca\u01cb\7c\2\2\u01cb"+
		"\u01cc\7u\2\2\u01cc\u01cd\7u\2\2\u01cd\u01ce\7g\2\2\u01ce\u01cf\7t\2\2"+
		"\u01cf\u01d0\7v\2\2\u01d0:\3\2\2\2\u01d1\u01d2\7t\2\2\u01d2\u01d3\7g\2"+
		"\2\u01d3\u01d4\7v\2\2\u01d4\u01d5\7w\2\2\u01d5\u01d6\7t\2\2\u01d6\u01d7"+
		"\7p\2\2\u01d7<\3\2\2\2\u01d8\u01d9\7V\2\2\u01d9\u01da\7t\2\2\u01da\u01db"+
		"\7w\2\2\u01db\u01dc\7g\2\2\u01dc>\3\2\2\2\u01dd\u01de\7H\2\2\u01de\u01df"+
		"\7c\2\2\u01df\u01e0\7n\2\2\u01e0\u01e1\7u\2\2\u01e1\u01e2\7g\2\2\u01e2"+
		"@\3\2\2\2\u01e3\u01e4\7k\2\2\u01e4\u01e5\7h\2\2\u01e5B\3\2\2\2\u01e6\u01e7"+
		"\7g\2\2\u01e7\u01e8\7n\2\2\u01e8\u01e9\7k\2\2\u01e9\u01ea\7h\2\2\u01ea"+
		"D\3\2\2\2\u01eb\u01ec\7g\2\2\u01ec\u01ed\7n\2\2\u01ed\u01ee\7u\2\2\u01ee"+
		"\u01ef\7g\2\2\u01efF\3\2\2\2\u01f0\u01f1\7h\2\2\u01f1\u01f2\7q\2\2\u01f2"+
		"\u01f3\7t\2\2\u01f3H\3\2\2\2\u01f4\u01f5\7y\2\2\u01f5\u01f6\7j\2\2\u01f6"+
		"\u01f7\7k\2\2\u01f7\u01f8\7n\2\2\u01f8\u01f9\7g\2\2\u01f9J\3\2\2\2\u01fa"+
		"\u01fb\7c\2\2\u01fb\u01fc\7p\2\2\u01fc\u01fd\7f\2\2\u01fdL\3\2\2\2\u01fe"+
		"\u01ff\7p\2\2\u01ff\u0200\7q\2\2\u0200\u0201\7v\2\2\u0201N\3\2\2\2\u0202"+
		"\u0203\7q\2\2\u0203\u0204\7t\2\2\u0204P\3\2\2\2\u0205\u0206\7k\2\2\u0206"+
		"\u0207\7p\2\2\u0207R\3\2\2\2\u0208\u0209\7k\2\2\u0209\u020a\7u\2\2\u020a"+
		"T\3\2\2\2\u020b\u020c\7f\2\2\u020c\u020d\7g\2\2\u020d\u020e\7n\2\2\u020e"+
		"V\3\2\2\2\u020f\u0210\7r\2\2\u0210\u0211\7c\2\2\u0211\u0212\7u\2\2\u0212"+
		"\u0213\7u\2\2\u0213X\3\2\2\2\u0214\u0215\7e\2\2\u0215\u0216\7q\2\2\u0216"+
		"\u0217\7p\2\2\u0217\u0218\7v\2\2\u0218\u0219\7k\2\2\u0219\u021a\7p\2\2"+
		"\u021a\u021b\7w\2\2\u021b\u021c\7g\2\2\u021cZ\3\2\2\2\u021d\u021e\7d\2"+
		"\2\u021e\u021f\7t\2\2\u021f\u0220\7g\2\2\u0220\u0221\7c\2\2\u0221\u0222"+
		"\7m\2\2\u0222\\\3\2\2\2\u0223\u0224\7P\2\2\u0224\u0225\7q\2\2\u0225\u0226"+
		"\7p\2\2\u0226\u0227\7g\2\2\u0227^\3\2\2\2\u0228\u0229\6\60\2\2\u0229\u0235"+
		"\5\u00ebv\2\u022a\u022c\7\17\2\2\u022b\u022a\3\2\2\2\u022b\u022c\3\2\2"+
		"\2\u022c\u022d\3\2\2\2\u022d\u0230\7\f\2\2\u022e\u0230\7\17\2\2\u022f"+
		"\u022b\3\2\2\2\u022f\u022e\3\2\2\2\u0230\u0232\3\2\2\2\u0231\u0233\5\u00eb"+
		"v\2\u0232\u0231\3\2\2\2\u0232\u0233\3\2\2\2\u0233\u0235\3\2\2\2\u0234"+
		"\u0228\3\2\2\2\u0234\u022f\3\2\2\2\u0235\u0236\3\2\2\2\u0236\u0237\b\60"+
		"\2\2\u0237`\3\2\2\2\u0238\u023d\7)\2\2\u0239\u023c\5c\62\2\u023a\u023c"+
		"\n\2\2\2\u023b\u0239\3\2\2\2\u023b\u023a\3\2\2\2\u023c\u023f\3\2\2\2\u023d"+
		"\u023b\3\2\2\2\u023d\u023e\3\2\2\2\u023e\u0240\3\2\2\2\u023f\u023d\3\2"+
		"\2\2\u0240\u024b\7)\2\2\u0241\u0246\7$\2\2\u0242\u0245\5c\62\2\u0243\u0245"+
		"\n\3\2\2\u0244\u0242\3\2\2\2\u0244\u0243\3\2\2\2\u0245\u0248\3\2\2\2\u0246"+
		"\u0244\3\2\2\2\u0246\u0247\3\2\2\2\u0247\u0249\3\2\2\2\u0248\u0246\3\2"+
		"\2\2\u0249\u024b\7$\2\2\u024a\u0238\3\2\2\2\u024a\u0241\3\2\2\2\u024b"+
		"b\3\2\2\2\u024c\u024d\7^\2\2\u024d\u024e\13\2\2\2\u024ed\3\2\2\2\u024f"+
		"\u0253\5m\67\2\u0250\u0252\5o8\2\u0251\u0250\3\2\2\2\u0252\u0255\3\2\2"+
		"\2\u0253\u0251\3\2\2\2\u0253\u0254\3\2\2\2\u0254\u025c\3\2\2\2\u0255\u0253"+
		"\3\2\2\2\u0256\u0258\7\62\2\2\u0257\u0256\3\2\2\2\u0258\u0259\3\2\2\2"+
		"\u0259\u0257\3\2\2\2\u0259\u025a\3\2\2\2\u025a\u025c\3\2\2\2\u025b\u024f"+
		"\3\2\2\2\u025b\u0257\3\2\2\2\u025cf\3\2\2\2\u025d\u025e\7\62\2\2\u025e"+
		"\u0260\t\4\2\2\u025f\u0261\5q9\2\u0260\u025f\3\2\2\2\u0261\u0262\3\2\2"+
		"\2\u0262\u0260\3\2\2\2\u0262\u0263\3\2\2\2\u0263h\3\2\2\2\u0264\u0265"+
		"\7\62\2\2\u0265\u0267\t\5\2\2\u0266\u0268\5s:\2\u0267\u0266\3\2\2\2\u0268"+
		"\u0269\3\2\2\2\u0269\u0267\3\2\2\2\u0269\u026a\3\2\2\2\u026aj\3\2\2\2"+
		"\u026b\u026c\7\62\2\2\u026c\u026e\t\6\2\2\u026d\u026f\5u;\2\u026e\u026d"+
		"\3\2\2\2\u026f\u0270\3\2\2\2\u0270\u026e\3\2\2\2\u0270\u0271\3\2\2\2\u0271"+
		"l\3\2\2\2\u0272\u0273\t\7\2\2\u0273n\3\2\2\2\u0274\u0275\t\b\2\2\u0275"+
		"p\3\2\2\2\u0276\u0277\t\t\2\2\u0277r\3\2\2\2\u0278\u0279\t\n\2\2\u0279"+
		"t\3\2\2\2\u027a\u027b\t\13\2\2\u027bv\3\2\2\2\u027c\u027f\5y=\2\u027d"+
		"\u027f\5{>\2\u027e\u027c\3\2\2\2\u027e\u027d\3\2\2\2\u027fx\3\2\2\2\u0280"+
		"\u0282\5}?\2\u0281\u0280\3\2\2\2\u0281\u0282\3\2\2\2\u0282\u0283\3\2\2"+
		"\2\u0283\u0288\5\177@\2\u0284\u0285\5}?\2\u0285\u0286\7\60\2\2\u0286\u0288"+
		"\3\2\2\2\u0287\u0281\3\2\2\2\u0287\u0284\3\2\2\2\u0288z\3\2\2\2\u0289"+
		"\u028c\5}?\2\u028a\u028c\5y=\2\u028b\u0289\3\2\2\2\u028b\u028a\3\2\2\2"+
		"\u028c\u028d\3\2\2\2\u028d\u028e\5\u0081A\2\u028e|\3\2\2\2\u028f\u0291"+
		"\5o8\2\u0290\u028f\3\2\2\2\u0291\u0292\3\2\2\2\u0292\u0290\3\2\2\2\u0292"+
		"\u0293\3\2\2\2\u0293~\3\2\2\2\u0294\u0296\7\60\2\2\u0295\u0297\5o8\2\u0296"+
		"\u0295\3\2\2\2\u0297\u0298\3\2\2\2\u0298\u0296\3\2\2\2\u0298\u0299\3\2"+
		"\2\2\u0299\u0080\3\2\2\2\u029a\u029c\t\f\2\2\u029b\u029d\t\r\2\2\u029c"+
		"\u029b\3\2\2\2\u029c\u029d\3\2\2\2\u029d\u029f\3\2\2\2\u029e\u02a0\5o"+
		"8\2\u029f\u029e\3\2\2\2\u02a0\u02a1\3\2\2\2\u02a1\u029f\3\2\2\2\u02a1"+
		"\u02a2\3\2\2\2\u02a2\u0082\3\2\2\2\u02a3\u02a4\7p\2\2\u02a4\u02a5\7w\2"+
		"\2\u02a5\u02a6\7n\2\2\u02a6\u02a7\7n\2\2\u02a7\u0084\3\2\2\2\u02a8\u02a9"+
		"\7\60\2\2\u02a9\u0086\3\2\2\2\u02aa\u02ab\7\60\2\2\u02ab\u02ac\7\60\2"+
		"\2\u02ac\u02ad\7\60\2\2\u02ad\u0088\3\2\2\2\u02ae\u02af\7,\2\2\u02af\u008a"+
		"\3\2\2\2\u02b0\u02b1\7*\2\2\u02b1\u02b2\bF\3\2\u02b2\u008c\3\2\2\2\u02b3"+
		"\u02b4\7+\2\2\u02b4\u02b5\bG\4\2\u02b5\u008e\3\2\2\2\u02b6\u02b7\7.\2"+
		"\2\u02b7\u0090\3\2\2\2\u02b8\u02b9\7<\2\2\u02b9\u0092\3\2\2\2\u02ba\u02bb"+
		"\7=\2\2\u02bb\u0094\3\2\2\2\u02bc\u02bd\7,\2\2\u02bd\u02be\7,\2\2\u02be"+
		"\u0096\3\2\2\2\u02bf\u02c0\7?\2\2\u02c0\u0098\3\2\2\2\u02c1\u02c2\7]\2"+
		"\2\u02c2\u02c3\bM\5\2\u02c3\u009a\3\2\2\2\u02c4\u02c5\7_\2\2\u02c5\u02c6"+
		"\bN\6\2\u02c6\u009c\3\2\2\2\u02c7\u02c8\7~\2\2\u02c8\u009e\3\2\2\2\u02c9"+
		"\u02ca\7`\2\2\u02ca\u00a0\3\2\2\2\u02cb\u02cc\7(\2\2\u02cc\u00a2\3\2\2"+
		"\2\u02cd\u02ce\7>\2\2\u02ce\u02cf\7>\2\2\u02cf\u00a4\3\2\2\2\u02d0\u02d1"+
		"\7@\2\2\u02d1\u02d2\7@\2\2\u02d2\u00a6\3\2\2\2\u02d3\u02d4\7-\2\2\u02d4"+
		"\u00a8\3\2\2\2\u02d5\u02d6\7/\2\2\u02d6\u00aa\3\2\2\2\u02d7\u02d8\7\61"+
		"\2\2\u02d8\u00ac\3\2\2\2\u02d9\u02da\7\'\2\2\u02da\u00ae\3\2\2\2\u02db"+
		"\u02dc\7\61\2\2\u02dc\u02dd\7\61\2\2\u02dd\u00b0\3\2\2\2\u02de\u02df\7"+
		"\u0080\2\2\u02df\u00b2\3\2\2\2\u02e0\u02e1\7}\2\2\u02e1\u02e2\bZ\7\2\u02e2"+
		"\u00b4\3\2\2\2\u02e3\u02e4\7\177\2\2\u02e4\u02e5\b[\b\2\u02e5\u00b6\3"+
		"\2\2\2\u02e6\u02e7\7>\2\2\u02e7\u00b8\3\2\2\2\u02e8\u02e9\7@\2\2\u02e9"+
		"\u00ba\3\2\2\2\u02ea\u02eb\7?\2\2\u02eb\u02ec\7?\2\2\u02ec\u00bc\3\2\2"+
		"\2\u02ed\u02ee\7@\2\2\u02ee\u02ef\7?\2\2\u02ef\u00be\3\2\2\2\u02f0\u02f1"+
		"\7>\2\2\u02f1\u02f2\7?\2\2\u02f2\u00c0\3\2\2\2\u02f3\u02f4\7>\2\2\u02f4"+
		"\u02f8\7@\2\2\u02f5\u02f6\7#\2\2\u02f6\u02f8\7?\2\2\u02f7\u02f3\3\2\2"+
		"\2\u02f7\u02f5\3\2\2\2\u02f8\u00c2\3\2\2\2\u02f9\u02fa\7B\2\2\u02fa\u00c4"+
		"\3\2\2\2\u02fb\u02fc\7/\2\2\u02fc\u02fd\7@\2\2\u02fd\u00c6\3\2\2\2\u02fe"+
		"\u02ff\7-\2\2\u02ff\u0300\7?\2\2\u0300\u00c8\3\2\2\2\u0301\u0302\7/\2"+
		"\2\u0302\u0303\7?\2\2\u0303\u00ca\3\2\2\2\u0304\u0305\7,\2\2\u0305\u0306"+
		"\7?\2\2\u0306\u00cc\3\2\2\2\u0307\u0308\7B\2\2\u0308\u0309\7?\2\2\u0309"+
		"\u00ce\3\2\2\2\u030a\u030b\7\61\2\2\u030b\u030c\7?\2\2\u030c\u00d0\3\2"+
		"\2\2\u030d\u030e\7\'\2\2\u030e\u030f\7?\2\2\u030f\u00d2\3\2\2\2\u0310"+
		"\u0311\7(\2\2\u0311\u0312\7?\2\2\u0312\u00d4\3\2\2\2\u0313\u0314\7~\2"+
		"\2\u0314\u0315\7?\2\2\u0315\u00d6\3\2\2\2\u0316\u0317\7`\2\2\u0317\u0318"+
		"\7?\2\2\u0318\u00d8\3\2\2\2\u0319\u031a\7>\2\2\u031a\u031b\7>\2\2\u031b"+
		"\u031c\7?\2\2\u031c\u00da\3\2\2\2\u031d\u031e\7@\2\2\u031e\u031f\7@\2"+
		"\2\u031f\u0320\7?\2\2\u0320\u00dc\3\2\2\2\u0321\u0322\7,\2\2\u0322\u0323"+
		"\7,\2\2\u0323\u0324\7?\2\2\u0324\u00de\3\2\2\2\u0325\u0326\7\61\2\2\u0326"+
		"\u0327\7\61\2\2\u0327\u0328\7?\2\2\u0328\u00e0\3\2\2\2\u0329\u032d\5\u00e3"+
		"r\2\u032a\u032c\5\u00e5s\2\u032b\u032a\3\2\2\2\u032c\u032f\3\2\2\2\u032d"+
		"\u032b\3\2\2\2\u032d\u032e\3\2\2\2\u032e\u00e2\3\2\2\2\u032f\u032d\3\2"+
		"\2\2\u0330\u0332\t\16\2\2\u0331\u0330\3\2\2\2\u0332\u00e4\3\2\2\2\u0333"+
		"\u0336\5\u00e3r\2\u0334\u0336\t\b\2\2\u0335\u0333\3\2\2\2\u0335\u0334"+
		"\3\2\2\2\u0336\u00e6\3\2\2\2\u0337\u033b\5\u00ebv\2\u0338\u033b\5\u00ed"+
		"w\2\u0339\u033b\5\u00efx\2\u033a\u0337\3\2\2\2\u033a\u0338\3\2\2\2\u033a"+
		"\u0339\3\2\2\2\u033b\u033c\3\2\2\2\u033c\u033d\bt\t\2\u033d\u00e8\3\2"+
		"\2\2\u033e\u033f\13\2\2\2\u033f\u00ea\3\2\2\2\u0340\u0342\t\17\2\2\u0341"+
		"\u0340\3\2\2\2\u0342\u0343\3\2\2\2\u0343\u0341\3\2\2\2\u0343\u0344\3\2"+
		"\2\2\u0344\u00ec\3\2\2\2\u0345\u0349\7%\2\2\u0346\u0348\n\20\2\2\u0347"+
		"\u0346\3\2\2\2\u0348\u034b\3\2\2\2\u0349\u0347\3\2\2\2\u0349\u034a\3\2"+
		"\2\2\u034a\u00ee\3\2\2\2\u034b\u0349\3\2\2\2\u034c\u034e\7^\2\2\u034d"+
		"\u034f\5\u00ebv\2\u034e\u034d\3\2\2\2\u034e\u034f\3\2\2\2\u034f\u0355"+
		"\3\2\2\2\u0350\u0352\7\17\2\2\u0351\u0350\3\2\2\2\u0351\u0352\3\2\2\2"+
		"\u0352\u0353\3\2\2\2\u0353\u0356\7\f\2\2\u0354\u0356\7\17\2\2\u0355\u0351"+
		"\3\2\2\2\u0355\u0354\3\2\2\2\u0356\u00f0\3\2\2\2$\2\u022b\u022f\u0232"+
		"\u0234\u023b\u023d\u0244\u0246\u024a\u0253\u0259\u025b\u0262\u0269\u0270"+
		"\u027e\u0281\u0287\u028b\u0292\u0298\u029c\u02a1\u02f7\u032d\u0331\u0335"+
		"\u033a\u0343\u0349\u034e\u0351\u0355\n\3\60\2\3F\3\3G\4\3M\5\3N\6\3Z\7"+
		"\3[\b\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
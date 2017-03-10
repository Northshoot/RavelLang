// Generated from /home/gcampagn/secureiot/ravellang/Ravel.g4 by ANTLR 4.6
package org.stanford.antlr4;

import org.stanford.ravel.compiler.scope.*;
import org.stanford.ravel.compiler.symbol.*;
import org.stanford.ravel.compiler.types.Type;

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
	static { RuntimeMetaData.checkVersion("4.6", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, MODEL=9, 
		SPACE=10, CONTROLLER=11, VIEW=12, FLOW=13, LOCAL=14, STREAMING=15, REPLICATED=16, 
		INTERFACE=17, DEF=18, EVENT=19, COMMAND=20, ASSERT=21, RETURN=22, TRUE=23, 
		FALSE=24, IF=25, ELIF=26, ELSE=27, FOR=28, WHILE=29, AND=30, NOT=31, OR=32, 
		IN=33, IS=34, DELETE=35, PASS=36, CONTINUE=37, BREAK=38, NONE=39, NEWLINE=40, 
		STRING_LITERAL=41, DECIMAL_INTEGER=42, OCT_INTEGER=43, HEX_INTEGER=44, 
		BIN_INTEGER=45, FLOAT_NUMBER=46, NullLiteral=47, DOT=48, ELLIPSIS=49, 
		STAR=50, OPEN_PAREN=51, CLOSE_PAREN=52, COMMA=53, COLON=54, SEMI_COLON=55, 
		POWER=56, ASSIGN=57, OPEN_BRACK=58, CLOSE_BRACK=59, OR_OP=60, XOR=61, 
		AND_OP=62, LEFT_SHIFT=63, RIGHT_SHIFT=64, ADD=65, MINUS=66, DIV=67, MOD=68, 
		IDIV=69, NOT_OP=70, OPEN_BRACE=71, CLOSE_BRACE=72, LT=73, GT=74, EQUAL=75, 
		GE=76, LE=77, NOTEQUAL=78, AT=79, ARROW=80, ADD_ASSIGN=81, SUB_ASSIGN=82, 
		MULT_ASSIGN=83, AT_ASSIGN=84, DIV_ASSIGN=85, MOD_ASSIGN=86, AND_ASSIGN=87, 
		OR_ASSIGN=88, XOR_ASSIGN=89, LEFT_SHIFT_ASSIGN=90, RIGHT_SHIFT_ASSIGN=91, 
		POWER_ASSIGN=92, IDIV_ASSIGN=93, Identifier=94, SKIP_=95, UNKNOWN_CHAR=96;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "MODEL", 
		"SPACE", "CONTROLLER", "VIEW", "FLOW", "LOCAL", "STREAMING", "REPLICATED", 
		"INTERFACE", "DEF", "EVENT", "COMMAND", "ASSERT", "RETURN", "TRUE", "FALSE", 
		"IF", "ELIF", "ELSE", "FOR", "WHILE", "AND", "NOT", "OR", "IN", "IS", 
		"DELETE", "PASS", "CONTINUE", "BREAK", "NONE", "NEWLINE", "STRING_LITERAL", 
		"STRING_ESCAPE_SEQ", "DECIMAL_INTEGER", "OCT_INTEGER", "HEX_INTEGER", 
		"BIN_INTEGER", "NON_ZERO_DIGIT", "DIGIT", "OCT_DIGIT", "HEX_DIGIT", "BIN_DIGIT", 
		"FLOAT_NUMBER", "POINT_FLOAT", "EXPONENT_FLOAT", "INT_PART", "FRACTION", 
		"EXPONENT", "NullLiteral", "DOT", "ELLIPSIS", "STAR", "OPEN_PAREN", "CLOSE_PAREN", 
		"COMMA", "COLON", "SEMI_COLON", "POWER", "ASSIGN", "OPEN_BRACK", "CLOSE_BRACK", 
		"OR_OP", "XOR", "AND_OP", "LEFT_SHIFT", "RIGHT_SHIFT", "ADD", "MINUS", 
		"DIV", "MOD", "IDIV", "NOT_OP", "OPEN_BRACE", "CLOSE_BRACE", "LT", "GT", 
		"EQUAL", "GE", "LE", "NOTEQUAL", "AT", "ARROW", "ADD_ASSIGN", "SUB_ASSIGN", 
		"MULT_ASSIGN", "AT_ASSIGN", "DIV_ASSIGN", "MOD_ASSIGN", "AND_ASSIGN", 
		"OR_ASSIGN", "XOR_ASSIGN", "LEFT_SHIFT_ASSIGN", "RIGHT_SHIFT_ASSIGN", 
		"POWER_ASSIGN", "IDIV_ASSIGN", "Identifier", "ID_START", "ID_CONTINUE", 
		"SKIP_", "UNKNOWN_CHAR", "SPACES", "COMMENT", "LINE_JOINING"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'platform:'", "'models:'", "'controllers:'", "'interfaces:'", "'implementation:'", 
		"'configuration:'", "'properties:'", "'schema:'", "'model'", "'space'", 
		"'controller'", "'view'", "'flow'", "'local'", "'streaming'", "'replicated'", 
		"'interface'", "'def'", "'event'", "'command'", "'assert'", "'return'", 
		"'True'", "'False'", "'if'", "'elif'", "'else'", "'for'", "'while'", "'and'", 
		"'not'", "'or'", "'in'", "'is'", "'del'", "'pass'", "'continue'", "'break'", 
		"'None'", null, null, null, null, null, null, null, "'null'", "'.'", "'...'", 
		"'*'", "'('", "')'", "','", "':'", "';'", "'**'", "'='", "'['", "']'", 
		"'|'", "'^'", "'&'", "'<<'", "'>>'", "'+'", "'-'", "'/'", "'%'", "'//'", 
		"'~'", "'{'", "'}'", "'<'", "'>'", "'=='", "'>='", "'<='", null, "'@'", 
		"'->'", "'+='", "'-='", "'*='", "'@='", "'/='", "'%='", "'&='", "'|='", 
		"'^='", "'<<='", "'>>='", "'**='", "'//='"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, "MODEL", "SPACE", 
		"CONTROLLER", "VIEW", "FLOW", "LOCAL", "STREAMING", "REPLICATED", "INTERFACE", 
		"DEF", "EVENT", "COMMAND", "ASSERT", "RETURN", "TRUE", "FALSE", "IF", 
		"ELIF", "ELSE", "FOR", "WHILE", "AND", "NOT", "OR", "IN", "IS", "DELETE", 
		"PASS", "CONTINUE", "BREAK", "NONE", "NEWLINE", "STRING_LITERAL", "DECIMAL_INTEGER", 
		"OCT_INTEGER", "HEX_INTEGER", "BIN_INTEGER", "FLOAT_NUMBER", "NullLiteral", 
		"DOT", "ELLIPSIS", "STAR", "OPEN_PAREN", "CLOSE_PAREN", "COMMA", "COLON", 
		"SEMI_COLON", "POWER", "ASSIGN", "OPEN_BRACK", "CLOSE_BRACK", "OR_OP", 
		"XOR", "AND_OP", "LEFT_SHIFT", "RIGHT_SHIFT", "ADD", "MINUS", "DIV", "MOD", 
		"IDIV", "NOT_OP", "OPEN_BRACE", "CLOSE_BRACE", "LT", "GT", "EQUAL", "GE", 
		"LE", "NOTEQUAL", "AT", "ARROW", "ADD_ASSIGN", "SUB_ASSIGN", "MULT_ASSIGN", 
		"AT_ASSIGN", "DIV_ASSIGN", "MOD_ASSIGN", "AND_ASSIGN", "OR_ASSIGN", "XOR_ASSIGN", 
		"LEFT_SHIFT_ASSIGN", "RIGHT_SHIFT_ASSIGN", "POWER_ASSIGN", "IDIV_ASSIGN", 
		"Identifier", "SKIP_", "UNKNOWN_CHAR"
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
		case 39:
			NEWLINE_action((RuleContext)_localctx, actionIndex);
			break;
		case 61:
			OPEN_PAREN_action((RuleContext)_localctx, actionIndex);
			break;
		case 62:
			CLOSE_PAREN_action((RuleContext)_localctx, actionIndex);
			break;
		case 68:
			OPEN_BRACK_action((RuleContext)_localctx, actionIndex);
			break;
		case 69:
			CLOSE_BRACK_action((RuleContext)_localctx, actionIndex);
			break;
		case 81:
			OPEN_BRACE_action((RuleContext)_localctx, actionIndex);
			break;
		case 82:
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
		case 39:
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2b\u0326\b\1\4\2\t"+
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
		"k\4l\tl\4m\tm\4n\tn\4o\to\4p\tp\4q\tq\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3"+
		"\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b"+
		"\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3"+
		"\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3"+
		"\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\24\3"+
		"\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3"+
		"\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3"+
		"\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\33\3"+
		"\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\36\3"+
		"\36\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3!\3!\3!\3\"\3"+
		"\"\3\"\3#\3#\3#\3$\3$\3$\3$\3%\3%\3%\3%\3%\3&\3&\3&\3&\3&\3&\3&\3&\3&"+
		"\3\'\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3(\3)\3)\3)\5)\u01fb\n)\3)\3)\5)"+
		"\u01ff\n)\3)\5)\u0202\n)\5)\u0204\n)\3)\3)\3*\3*\3*\7*\u020b\n*\f*\16"+
		"*\u020e\13*\3*\3*\3*\3*\7*\u0214\n*\f*\16*\u0217\13*\3*\5*\u021a\n*\3"+
		"+\3+\3+\3,\3,\7,\u0221\n,\f,\16,\u0224\13,\3,\6,\u0227\n,\r,\16,\u0228"+
		"\5,\u022b\n,\3-\3-\3-\6-\u0230\n-\r-\16-\u0231\3.\3.\3.\6.\u0237\n.\r"+
		".\16.\u0238\3/\3/\3/\6/\u023e\n/\r/\16/\u023f\3\60\3\60\3\61\3\61\3\62"+
		"\3\62\3\63\3\63\3\64\3\64\3\65\3\65\5\65\u024e\n\65\3\66\5\66\u0251\n"+
		"\66\3\66\3\66\3\66\3\66\5\66\u0257\n\66\3\67\3\67\5\67\u025b\n\67\3\67"+
		"\3\67\38\68\u0260\n8\r8\168\u0261\39\39\69\u0266\n9\r9\169\u0267\3:\3"+
		":\5:\u026c\n:\3:\6:\u026f\n:\r:\16:\u0270\3;\3;\3;\3;\3;\3<\3<\3=\3=\3"+
		"=\3=\3>\3>\3?\3?\3?\3@\3@\3@\3A\3A\3B\3B\3C\3C\3D\3D\3D\3E\3E\3F\3F\3"+
		"F\3G\3G\3G\3H\3H\3I\3I\3J\3J\3K\3K\3K\3L\3L\3L\3M\3M\3N\3N\3O\3O\3P\3"+
		"P\3Q\3Q\3Q\3R\3R\3S\3S\3S\3T\3T\3T\3U\3U\3V\3V\3W\3W\3W\3X\3X\3X\3Y\3"+
		"Y\3Y\3Z\3Z\3Z\3Z\5Z\u02c7\nZ\3[\3[\3\\\3\\\3\\\3]\3]\3]\3^\3^\3^\3_\3"+
		"_\3_\3`\3`\3`\3a\3a\3a\3b\3b\3b\3c\3c\3c\3d\3d\3d\3e\3e\3e\3f\3f\3f\3"+
		"f\3g\3g\3g\3g\3h\3h\3h\3h\3i\3i\3i\3i\3j\3j\7j\u02fb\nj\fj\16j\u02fe\13"+
		"j\3k\5k\u0301\nk\3l\3l\5l\u0305\nl\3m\3m\3m\5m\u030a\nm\3m\3m\3n\3n\3"+
		"o\6o\u0311\no\ro\16o\u0312\3p\3p\7p\u0317\np\fp\16p\u031a\13p\3q\3q\5"+
		"q\u031e\nq\3q\5q\u0321\nq\3q\3q\5q\u0325\nq\2\2r\3\3\5\4\7\5\t\6\13\7"+
		"\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25"+
		")\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O"+
		")Q*S+U\2W,Y-[.]/_\2a\2c\2e\2g\2i\60k\2m\2o\2q\2s\2u\61w\62y\63{\64}\65"+
		"\177\66\u0081\67\u00838\u00859\u0087:\u0089;\u008b<\u008d=\u008f>\u0091"+
		"?\u0093@\u0095A\u0097B\u0099C\u009bD\u009dE\u009fF\u00a1G\u00a3H\u00a5"+
		"I\u00a7J\u00a9K\u00abL\u00adM\u00afN\u00b1O\u00b3P\u00b5Q\u00b7R\u00b9"+
		"S\u00bbT\u00bdU\u00bfV\u00c1W\u00c3X\u00c5Y\u00c7Z\u00c9[\u00cb\\\u00cd"+
		"]\u00cf^\u00d1_\u00d3`\u00d5\2\u00d7\2\u00d9a\u00dbb\u00dd\2\u00df\2\u00e1"+
		"\2\3\2\21\6\2\f\f\17\17))^^\6\2\f\f\17\17$$^^\4\2QQqq\4\2ZZzz\4\2DDdd"+
		"\3\2\63;\3\2\62;\3\2\629\5\2\62;CHch\3\2\62\63\4\2GGgg\4\2--//\5\2C\\"+
		"aac|\4\2\13\13\"\"\4\2\f\f\17\17\u0336\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2"+
		"\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2"+
		"\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3"+
		"\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3"+
		"\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65"+
		"\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3"+
		"\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2"+
		"\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2"+
		"]\3\2\2\2\2i\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3"+
		"\2\2\2\2\177\3\2\2\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2\2\u0085\3\2\2\2\2"+
		"\u0087\3\2\2\2\2\u0089\3\2\2\2\2\u008b\3\2\2\2\2\u008d\3\2\2\2\2\u008f"+
		"\3\2\2\2\2\u0091\3\2\2\2\2\u0093\3\2\2\2\2\u0095\3\2\2\2\2\u0097\3\2\2"+
		"\2\2\u0099\3\2\2\2\2\u009b\3\2\2\2\2\u009d\3\2\2\2\2\u009f\3\2\2\2\2\u00a1"+
		"\3\2\2\2\2\u00a3\3\2\2\2\2\u00a5\3\2\2\2\2\u00a7\3\2\2\2\2\u00a9\3\2\2"+
		"\2\2\u00ab\3\2\2\2\2\u00ad\3\2\2\2\2\u00af\3\2\2\2\2\u00b1\3\2\2\2\2\u00b3"+
		"\3\2\2\2\2\u00b5\3\2\2\2\2\u00b7\3\2\2\2\2\u00b9\3\2\2\2\2\u00bb\3\2\2"+
		"\2\2\u00bd\3\2\2\2\2\u00bf\3\2\2\2\2\u00c1\3\2\2\2\2\u00c3\3\2\2\2\2\u00c5"+
		"\3\2\2\2\2\u00c7\3\2\2\2\2\u00c9\3\2\2\2\2\u00cb\3\2\2\2\2\u00cd\3\2\2"+
		"\2\2\u00cf\3\2\2\2\2\u00d1\3\2\2\2\2\u00d3\3\2\2\2\2\u00d9\3\2\2\2\2\u00db"+
		"\3\2\2\2\3\u00e3\3\2\2\2\5\u00ed\3\2\2\2\7\u00f5\3\2\2\2\t\u0102\3\2\2"+
		"\2\13\u010e\3\2\2\2\r\u011e\3\2\2\2\17\u012d\3\2\2\2\21\u0139\3\2\2\2"+
		"\23\u0141\3\2\2\2\25\u0147\3\2\2\2\27\u014d\3\2\2\2\31\u0158\3\2\2\2\33"+
		"\u015d\3\2\2\2\35\u0162\3\2\2\2\37\u0168\3\2\2\2!\u0172\3\2\2\2#\u017d"+
		"\3\2\2\2%\u0187\3\2\2\2\'\u018b\3\2\2\2)\u0191\3\2\2\2+\u0199\3\2\2\2"+
		"-\u01a0\3\2\2\2/\u01a7\3\2\2\2\61\u01ac\3\2\2\2\63\u01b2\3\2\2\2\65\u01b5"+
		"\3\2\2\2\67\u01ba\3\2\2\29\u01bf\3\2\2\2;\u01c3\3\2\2\2=\u01c9\3\2\2\2"+
		"?\u01cd\3\2\2\2A\u01d1\3\2\2\2C\u01d4\3\2\2\2E\u01d7\3\2\2\2G\u01da\3"+
		"\2\2\2I\u01de\3\2\2\2K\u01e3\3\2\2\2M\u01ec\3\2\2\2O\u01f2\3\2\2\2Q\u0203"+
		"\3\2\2\2S\u0219\3\2\2\2U\u021b\3\2\2\2W\u022a\3\2\2\2Y\u022c\3\2\2\2["+
		"\u0233\3\2\2\2]\u023a\3\2\2\2_\u0241\3\2\2\2a\u0243\3\2\2\2c\u0245\3\2"+
		"\2\2e\u0247\3\2\2\2g\u0249\3\2\2\2i\u024d\3\2\2\2k\u0256\3\2\2\2m\u025a"+
		"\3\2\2\2o\u025f\3\2\2\2q\u0263\3\2\2\2s\u0269\3\2\2\2u\u0272\3\2\2\2w"+
		"\u0277\3\2\2\2y\u0279\3\2\2\2{\u027d\3\2\2\2}\u027f\3\2\2\2\177\u0282"+
		"\3\2\2\2\u0081\u0285\3\2\2\2\u0083\u0287\3\2\2\2\u0085\u0289\3\2\2\2\u0087"+
		"\u028b\3\2\2\2\u0089\u028e\3\2\2\2\u008b\u0290\3\2\2\2\u008d\u0293\3\2"+
		"\2\2\u008f\u0296\3\2\2\2\u0091\u0298\3\2\2\2\u0093\u029a\3\2\2\2\u0095"+
		"\u029c\3\2\2\2\u0097\u029f\3\2\2\2\u0099\u02a2\3\2\2\2\u009b\u02a4\3\2"+
		"\2\2\u009d\u02a6\3\2\2\2\u009f\u02a8\3\2\2\2\u00a1\u02aa\3\2\2\2\u00a3"+
		"\u02ad\3\2\2\2\u00a5\u02af\3\2\2\2\u00a7\u02b2\3\2\2\2\u00a9\u02b5\3\2"+
		"\2\2\u00ab\u02b7\3\2\2\2\u00ad\u02b9\3\2\2\2\u00af\u02bc\3\2\2\2\u00b1"+
		"\u02bf\3\2\2\2\u00b3\u02c6\3\2\2\2\u00b5\u02c8\3\2\2\2\u00b7\u02ca\3\2"+
		"\2\2\u00b9\u02cd\3\2\2\2\u00bb\u02d0\3\2\2\2\u00bd\u02d3\3\2\2\2\u00bf"+
		"\u02d6\3\2\2\2\u00c1\u02d9\3\2\2\2\u00c3\u02dc\3\2\2\2\u00c5\u02df\3\2"+
		"\2\2\u00c7\u02e2\3\2\2\2\u00c9\u02e5\3\2\2\2\u00cb\u02e8\3\2\2\2\u00cd"+
		"\u02ec\3\2\2\2\u00cf\u02f0\3\2\2\2\u00d1\u02f4\3\2\2\2\u00d3\u02f8\3\2"+
		"\2\2\u00d5\u0300\3\2\2\2\u00d7\u0304\3\2\2\2\u00d9\u0309\3\2\2\2\u00db"+
		"\u030d\3\2\2\2\u00dd\u0310\3\2\2\2\u00df\u0314\3\2\2\2\u00e1\u031b\3\2"+
		"\2\2\u00e3\u00e4\7r\2\2\u00e4\u00e5\7n\2\2\u00e5\u00e6\7c\2\2\u00e6\u00e7"+
		"\7v\2\2\u00e7\u00e8\7h\2\2\u00e8\u00e9\7q\2\2\u00e9\u00ea\7t\2\2\u00ea"+
		"\u00eb\7o\2\2\u00eb\u00ec\7<\2\2\u00ec\4\3\2\2\2\u00ed\u00ee\7o\2\2\u00ee"+
		"\u00ef\7q\2\2\u00ef\u00f0\7f\2\2\u00f0\u00f1\7g\2\2\u00f1\u00f2\7n\2\2"+
		"\u00f2\u00f3\7u\2\2\u00f3\u00f4\7<\2\2\u00f4\6\3\2\2\2\u00f5\u00f6\7e"+
		"\2\2\u00f6\u00f7\7q\2\2\u00f7\u00f8\7p\2\2\u00f8\u00f9\7v\2\2\u00f9\u00fa"+
		"\7t\2\2\u00fa\u00fb\7q\2\2\u00fb\u00fc\7n\2\2\u00fc\u00fd\7n\2\2\u00fd"+
		"\u00fe\7g\2\2\u00fe\u00ff\7t\2\2\u00ff\u0100\7u\2\2\u0100\u0101\7<\2\2"+
		"\u0101\b\3\2\2\2\u0102\u0103\7k\2\2\u0103\u0104\7p\2\2\u0104\u0105\7v"+
		"\2\2\u0105\u0106\7g\2\2\u0106\u0107\7t\2\2\u0107\u0108\7h\2\2\u0108\u0109"+
		"\7c\2\2\u0109\u010a\7e\2\2\u010a\u010b\7g\2\2\u010b\u010c\7u\2\2\u010c"+
		"\u010d\7<\2\2\u010d\n\3\2\2\2\u010e\u010f\7k\2\2\u010f\u0110\7o\2\2\u0110"+
		"\u0111\7r\2\2\u0111\u0112\7n\2\2\u0112\u0113\7g\2\2\u0113\u0114\7o\2\2"+
		"\u0114\u0115\7g\2\2\u0115\u0116\7p\2\2\u0116\u0117\7v\2\2\u0117\u0118"+
		"\7c\2\2\u0118\u0119\7v\2\2\u0119\u011a\7k\2\2\u011a\u011b\7q\2\2\u011b"+
		"\u011c\7p\2\2\u011c\u011d\7<\2\2\u011d\f\3\2\2\2\u011e\u011f\7e\2\2\u011f"+
		"\u0120\7q\2\2\u0120\u0121\7p\2\2\u0121\u0122\7h\2\2\u0122\u0123\7k\2\2"+
		"\u0123\u0124\7i\2\2\u0124\u0125\7w\2\2\u0125\u0126\7t\2\2\u0126\u0127"+
		"\7c\2\2\u0127\u0128\7v\2\2\u0128\u0129\7k\2\2\u0129\u012a\7q\2\2\u012a"+
		"\u012b\7p\2\2\u012b\u012c\7<\2\2\u012c\16\3\2\2\2\u012d\u012e\7r\2\2\u012e"+
		"\u012f\7t\2\2\u012f\u0130\7q\2\2\u0130\u0131\7r\2\2\u0131\u0132\7g\2\2"+
		"\u0132\u0133\7t\2\2\u0133\u0134\7v\2\2\u0134\u0135\7k\2\2\u0135\u0136"+
		"\7g\2\2\u0136\u0137\7u\2\2\u0137\u0138\7<\2\2\u0138\20\3\2\2\2\u0139\u013a"+
		"\7u\2\2\u013a\u013b\7e\2\2\u013b\u013c\7j\2\2\u013c\u013d\7g\2\2\u013d"+
		"\u013e\7o\2\2\u013e\u013f\7c\2\2\u013f\u0140\7<\2\2\u0140\22\3\2\2\2\u0141"+
		"\u0142\7o\2\2\u0142\u0143\7q\2\2\u0143\u0144\7f\2\2\u0144\u0145\7g\2\2"+
		"\u0145\u0146\7n\2\2\u0146\24\3\2\2\2\u0147\u0148\7u\2\2\u0148\u0149\7"+
		"r\2\2\u0149\u014a\7c\2\2\u014a\u014b\7e\2\2\u014b\u014c\7g\2\2\u014c\26"+
		"\3\2\2\2\u014d\u014e\7e\2\2\u014e\u014f\7q\2\2\u014f\u0150\7p\2\2\u0150"+
		"\u0151\7v\2\2\u0151\u0152\7t\2\2\u0152\u0153\7q\2\2\u0153\u0154\7n\2\2"+
		"\u0154\u0155\7n\2\2\u0155\u0156\7g\2\2\u0156\u0157\7t\2\2\u0157\30\3\2"+
		"\2\2\u0158\u0159\7x\2\2\u0159\u015a\7k\2\2\u015a\u015b\7g\2\2\u015b\u015c"+
		"\7y\2\2\u015c\32\3\2\2\2\u015d\u015e\7h\2\2\u015e\u015f\7n\2\2\u015f\u0160"+
		"\7q\2\2\u0160\u0161\7y\2\2\u0161\34\3\2\2\2\u0162\u0163\7n\2\2\u0163\u0164"+
		"\7q\2\2\u0164\u0165\7e\2\2\u0165\u0166\7c\2\2\u0166\u0167\7n\2\2\u0167"+
		"\36\3\2\2\2\u0168\u0169\7u\2\2\u0169\u016a\7v\2\2\u016a\u016b\7t\2\2\u016b"+
		"\u016c\7g\2\2\u016c\u016d\7c\2\2\u016d\u016e\7o\2\2\u016e\u016f\7k\2\2"+
		"\u016f\u0170\7p\2\2\u0170\u0171\7i\2\2\u0171 \3\2\2\2\u0172\u0173\7t\2"+
		"\2\u0173\u0174\7g\2\2\u0174\u0175\7r\2\2\u0175\u0176\7n\2\2\u0176\u0177"+
		"\7k\2\2\u0177\u0178\7e\2\2\u0178\u0179\7c\2\2\u0179\u017a\7v\2\2\u017a"+
		"\u017b\7g\2\2\u017b\u017c\7f\2\2\u017c\"\3\2\2\2\u017d\u017e\7k\2\2\u017e"+
		"\u017f\7p\2\2\u017f\u0180\7v\2\2\u0180\u0181\7g\2\2\u0181\u0182\7t\2\2"+
		"\u0182\u0183\7h\2\2\u0183\u0184\7c\2\2\u0184\u0185\7e\2\2\u0185\u0186"+
		"\7g\2\2\u0186$\3\2\2\2\u0187\u0188\7f\2\2\u0188\u0189\7g\2\2\u0189\u018a"+
		"\7h\2\2\u018a&\3\2\2\2\u018b\u018c\7g\2\2\u018c\u018d\7x\2\2\u018d\u018e"+
		"\7g\2\2\u018e\u018f\7p\2\2\u018f\u0190\7v\2\2\u0190(\3\2\2\2\u0191\u0192"+
		"\7e\2\2\u0192\u0193\7q\2\2\u0193\u0194\7o\2\2\u0194\u0195\7o\2\2\u0195"+
		"\u0196\7c\2\2\u0196\u0197\7p\2\2\u0197\u0198\7f\2\2\u0198*\3\2\2\2\u0199"+
		"\u019a\7c\2\2\u019a\u019b\7u\2\2\u019b\u019c\7u\2\2\u019c\u019d\7g\2\2"+
		"\u019d\u019e\7t\2\2\u019e\u019f\7v\2\2\u019f,\3\2\2\2\u01a0\u01a1\7t\2"+
		"\2\u01a1\u01a2\7g\2\2\u01a2\u01a3\7v\2\2\u01a3\u01a4\7w\2\2\u01a4\u01a5"+
		"\7t\2\2\u01a5\u01a6\7p\2\2\u01a6.\3\2\2\2\u01a7\u01a8\7V\2\2\u01a8\u01a9"+
		"\7t\2\2\u01a9\u01aa\7w\2\2\u01aa\u01ab\7g\2\2\u01ab\60\3\2\2\2\u01ac\u01ad"+
		"\7H\2\2\u01ad\u01ae\7c\2\2\u01ae\u01af\7n\2\2\u01af\u01b0\7u\2\2\u01b0"+
		"\u01b1\7g\2\2\u01b1\62\3\2\2\2\u01b2\u01b3\7k\2\2\u01b3\u01b4\7h\2\2\u01b4"+
		"\64\3\2\2\2\u01b5\u01b6\7g\2\2\u01b6\u01b7\7n\2\2\u01b7\u01b8\7k\2\2\u01b8"+
		"\u01b9\7h\2\2\u01b9\66\3\2\2\2\u01ba\u01bb\7g\2\2\u01bb\u01bc\7n\2\2\u01bc"+
		"\u01bd\7u\2\2\u01bd\u01be\7g\2\2\u01be8\3\2\2\2\u01bf\u01c0\7h\2\2\u01c0"+
		"\u01c1\7q\2\2\u01c1\u01c2\7t\2\2\u01c2:\3\2\2\2\u01c3\u01c4\7y\2\2\u01c4"+
		"\u01c5\7j\2\2\u01c5\u01c6\7k\2\2\u01c6\u01c7\7n\2\2\u01c7\u01c8\7g\2\2"+
		"\u01c8<\3\2\2\2\u01c9\u01ca\7c\2\2\u01ca\u01cb\7p\2\2\u01cb\u01cc\7f\2"+
		"\2\u01cc>\3\2\2\2\u01cd\u01ce\7p\2\2\u01ce\u01cf\7q\2\2\u01cf\u01d0\7"+
		"v\2\2\u01d0@\3\2\2\2\u01d1\u01d2\7q\2\2\u01d2\u01d3\7t\2\2\u01d3B\3\2"+
		"\2\2\u01d4\u01d5\7k\2\2\u01d5\u01d6\7p\2\2\u01d6D\3\2\2\2\u01d7\u01d8"+
		"\7k\2\2\u01d8\u01d9\7u\2\2\u01d9F\3\2\2\2\u01da\u01db\7f\2\2\u01db\u01dc"+
		"\7g\2\2\u01dc\u01dd\7n\2\2\u01ddH\3\2\2\2\u01de\u01df\7r\2\2\u01df\u01e0"+
		"\7c\2\2\u01e0\u01e1\7u\2\2\u01e1\u01e2\7u\2\2\u01e2J\3\2\2\2\u01e3\u01e4"+
		"\7e\2\2\u01e4\u01e5\7q\2\2\u01e5\u01e6\7p\2\2\u01e6\u01e7\7v\2\2\u01e7"+
		"\u01e8\7k\2\2\u01e8\u01e9\7p\2\2\u01e9\u01ea\7w\2\2\u01ea\u01eb\7g\2\2"+
		"\u01ebL\3\2\2\2\u01ec\u01ed\7d\2\2\u01ed\u01ee\7t\2\2\u01ee\u01ef\7g\2"+
		"\2\u01ef\u01f0\7c\2\2\u01f0\u01f1\7m\2\2\u01f1N\3\2\2\2\u01f2\u01f3\7"+
		"P\2\2\u01f3\u01f4\7q\2\2\u01f4\u01f5\7p\2\2\u01f5\u01f6\7g\2\2\u01f6P"+
		"\3\2\2\2\u01f7\u01f8\6)\2\2\u01f8\u0204\5\u00ddo\2\u01f9\u01fb\7\17\2"+
		"\2\u01fa\u01f9\3\2\2\2\u01fa\u01fb\3\2\2\2\u01fb\u01fc\3\2\2\2\u01fc\u01ff"+
		"\7\f\2\2\u01fd\u01ff\7\17\2\2\u01fe\u01fa\3\2\2\2\u01fe\u01fd\3\2\2\2"+
		"\u01ff\u0201\3\2\2\2\u0200\u0202\5\u00ddo\2\u0201\u0200\3\2\2\2\u0201"+
		"\u0202\3\2\2\2\u0202\u0204\3\2\2\2\u0203\u01f7\3\2\2\2\u0203\u01fe\3\2"+
		"\2\2\u0204\u0205\3\2\2\2\u0205\u0206\b)\2\2\u0206R\3\2\2\2\u0207\u020c"+
		"\7)\2\2\u0208\u020b\5U+\2\u0209\u020b\n\2\2\2\u020a\u0208\3\2\2\2\u020a"+
		"\u0209\3\2\2\2\u020b\u020e\3\2\2\2\u020c\u020a\3\2\2\2\u020c\u020d\3\2"+
		"\2\2\u020d\u020f\3\2\2\2\u020e\u020c\3\2\2\2\u020f\u021a\7)\2\2\u0210"+
		"\u0215\7$\2\2\u0211\u0214\5U+\2\u0212\u0214\n\3\2\2\u0213\u0211\3\2\2"+
		"\2\u0213\u0212\3\2\2\2\u0214\u0217\3\2\2\2\u0215\u0213\3\2\2\2\u0215\u0216"+
		"\3\2\2\2\u0216\u0218\3\2\2\2\u0217\u0215\3\2\2\2\u0218\u021a\7$\2\2\u0219"+
		"\u0207\3\2\2\2\u0219\u0210\3\2\2\2\u021aT\3\2\2\2\u021b\u021c\7^\2\2\u021c"+
		"\u021d\13\2\2\2\u021dV\3\2\2\2\u021e\u0222\5_\60\2\u021f\u0221\5a\61\2"+
		"\u0220\u021f\3\2\2\2\u0221\u0224\3\2\2\2\u0222\u0220\3\2\2\2\u0222\u0223"+
		"\3\2\2\2\u0223\u022b\3\2\2\2\u0224\u0222\3\2\2\2\u0225\u0227\7\62\2\2"+
		"\u0226\u0225\3\2\2\2\u0227\u0228\3\2\2\2\u0228\u0226\3\2\2\2\u0228\u0229"+
		"\3\2\2\2\u0229\u022b\3\2\2\2\u022a\u021e\3\2\2\2\u022a\u0226\3\2\2\2\u022b"+
		"X\3\2\2\2\u022c\u022d\7\62\2\2\u022d\u022f\t\4\2\2\u022e\u0230\5c\62\2"+
		"\u022f\u022e\3\2\2\2\u0230\u0231\3\2\2\2\u0231\u022f\3\2\2\2\u0231\u0232"+
		"\3\2\2\2\u0232Z\3\2\2\2\u0233\u0234\7\62\2\2\u0234\u0236\t\5\2\2\u0235"+
		"\u0237\5e\63\2\u0236\u0235\3\2\2\2\u0237\u0238\3\2\2\2\u0238\u0236\3\2"+
		"\2\2\u0238\u0239\3\2\2\2\u0239\\\3\2\2\2\u023a\u023b\7\62\2\2\u023b\u023d"+
		"\t\6\2\2\u023c\u023e\5g\64\2\u023d\u023c\3\2\2\2\u023e\u023f\3\2\2\2\u023f"+
		"\u023d\3\2\2\2\u023f\u0240\3\2\2\2\u0240^\3\2\2\2\u0241\u0242\t\7\2\2"+
		"\u0242`\3\2\2\2\u0243\u0244\t\b\2\2\u0244b\3\2\2\2\u0245\u0246\t\t\2\2"+
		"\u0246d\3\2\2\2\u0247\u0248\t\n\2\2\u0248f\3\2\2\2\u0249\u024a\t\13\2"+
		"\2\u024ah\3\2\2\2\u024b\u024e\5k\66\2\u024c\u024e\5m\67\2\u024d\u024b"+
		"\3\2\2\2\u024d\u024c\3\2\2\2\u024ej\3\2\2\2\u024f\u0251\5o8\2\u0250\u024f"+
		"\3\2\2\2\u0250\u0251\3\2\2\2\u0251\u0252\3\2\2\2\u0252\u0257\5q9\2\u0253"+
		"\u0254\5o8\2\u0254\u0255\7\60\2\2\u0255\u0257\3\2\2\2\u0256\u0250\3\2"+
		"\2\2\u0256\u0253\3\2\2\2\u0257l\3\2\2\2\u0258\u025b\5o8\2\u0259\u025b"+
		"\5k\66\2\u025a\u0258\3\2\2\2\u025a\u0259\3\2\2\2\u025b\u025c\3\2\2\2\u025c"+
		"\u025d\5s:\2\u025dn\3\2\2\2\u025e\u0260\5a\61\2\u025f\u025e\3\2\2\2\u0260"+
		"\u0261\3\2\2\2\u0261\u025f\3\2\2\2\u0261\u0262\3\2\2\2\u0262p\3\2\2\2"+
		"\u0263\u0265\7\60\2\2\u0264\u0266\5a\61\2\u0265\u0264\3\2\2\2\u0266\u0267"+
		"\3\2\2\2\u0267\u0265\3\2\2\2\u0267\u0268\3\2\2\2\u0268r\3\2\2\2\u0269"+
		"\u026b\t\f\2\2\u026a\u026c\t\r\2\2\u026b\u026a\3\2\2\2\u026b\u026c\3\2"+
		"\2\2\u026c\u026e\3\2\2\2\u026d\u026f\5a\61\2\u026e\u026d\3\2\2\2\u026f"+
		"\u0270\3\2\2\2\u0270\u026e\3\2\2\2\u0270\u0271\3\2\2\2\u0271t\3\2\2\2"+
		"\u0272\u0273\7p\2\2\u0273\u0274\7w\2\2\u0274\u0275\7n\2\2\u0275\u0276"+
		"\7n\2\2\u0276v\3\2\2\2\u0277\u0278\7\60\2\2\u0278x\3\2\2\2\u0279\u027a"+
		"\7\60\2\2\u027a\u027b\7\60\2\2\u027b\u027c\7\60\2\2\u027cz\3\2\2\2\u027d"+
		"\u027e\7,\2\2\u027e|\3\2\2\2\u027f\u0280\7*\2\2\u0280\u0281\b?\3\2\u0281"+
		"~\3\2\2\2\u0282\u0283\7+\2\2\u0283\u0284\b@\4\2\u0284\u0080\3\2\2\2\u0285"+
		"\u0286\7.\2\2\u0286\u0082\3\2\2\2\u0287\u0288\7<\2\2\u0288\u0084\3\2\2"+
		"\2\u0289\u028a\7=\2\2\u028a\u0086\3\2\2\2\u028b\u028c\7,\2\2\u028c\u028d"+
		"\7,\2\2\u028d\u0088\3\2\2\2\u028e\u028f\7?\2\2\u028f\u008a\3\2\2\2\u0290"+
		"\u0291\7]\2\2\u0291\u0292\bF\5\2\u0292\u008c\3\2\2\2\u0293\u0294\7_\2"+
		"\2\u0294\u0295\bG\6\2\u0295\u008e\3\2\2\2\u0296\u0297\7~\2\2\u0297\u0090"+
		"\3\2\2\2\u0298\u0299\7`\2\2\u0299\u0092\3\2\2\2\u029a\u029b\7(\2\2\u029b"+
		"\u0094\3\2\2\2\u029c\u029d\7>\2\2\u029d\u029e\7>\2\2\u029e\u0096\3\2\2"+
		"\2\u029f\u02a0\7@\2\2\u02a0\u02a1\7@\2\2\u02a1\u0098\3\2\2\2\u02a2\u02a3"+
		"\7-\2\2\u02a3\u009a\3\2\2\2\u02a4\u02a5\7/\2\2\u02a5\u009c\3\2\2\2\u02a6"+
		"\u02a7\7\61\2\2\u02a7\u009e\3\2\2\2\u02a8\u02a9\7\'\2\2\u02a9\u00a0\3"+
		"\2\2\2\u02aa\u02ab\7\61\2\2\u02ab\u02ac\7\61\2\2\u02ac\u00a2\3\2\2\2\u02ad"+
		"\u02ae\7\u0080\2\2\u02ae\u00a4\3\2\2\2\u02af\u02b0\7}\2\2\u02b0\u02b1"+
		"\bS\7\2\u02b1\u00a6\3\2\2\2\u02b2\u02b3\7\177\2\2\u02b3\u02b4\bT\b\2\u02b4"+
		"\u00a8\3\2\2\2\u02b5\u02b6\7>\2\2\u02b6\u00aa\3\2\2\2\u02b7\u02b8\7@\2"+
		"\2\u02b8\u00ac\3\2\2\2\u02b9\u02ba\7?\2\2\u02ba\u02bb\7?\2\2\u02bb\u00ae"+
		"\3\2\2\2\u02bc\u02bd\7@\2\2\u02bd\u02be\7?\2\2\u02be\u00b0\3\2\2\2\u02bf"+
		"\u02c0\7>\2\2\u02c0\u02c1\7?\2\2\u02c1\u00b2\3\2\2\2\u02c2\u02c3\7>\2"+
		"\2\u02c3\u02c7\7@\2\2\u02c4\u02c5\7#\2\2\u02c5\u02c7\7?\2\2\u02c6\u02c2"+
		"\3\2\2\2\u02c6\u02c4\3\2\2\2\u02c7\u00b4\3\2\2\2\u02c8\u02c9\7B\2\2\u02c9"+
		"\u00b6\3\2\2\2\u02ca\u02cb\7/\2\2\u02cb\u02cc\7@\2\2\u02cc\u00b8\3\2\2"+
		"\2\u02cd\u02ce\7-\2\2\u02ce\u02cf\7?\2\2\u02cf\u00ba\3\2\2\2\u02d0\u02d1"+
		"\7/\2\2\u02d1\u02d2\7?\2\2\u02d2\u00bc\3\2\2\2\u02d3\u02d4\7,\2\2\u02d4"+
		"\u02d5\7?\2\2\u02d5\u00be\3\2\2\2\u02d6\u02d7\7B\2\2\u02d7\u02d8\7?\2"+
		"\2\u02d8\u00c0\3\2\2\2\u02d9\u02da\7\61\2\2\u02da\u02db\7?\2\2\u02db\u00c2"+
		"\3\2\2\2\u02dc\u02dd\7\'\2\2\u02dd\u02de\7?\2\2\u02de\u00c4\3\2\2\2\u02df"+
		"\u02e0\7(\2\2\u02e0\u02e1\7?\2\2\u02e1\u00c6\3\2\2\2\u02e2\u02e3\7~\2"+
		"\2\u02e3\u02e4\7?\2\2\u02e4\u00c8\3\2\2\2\u02e5\u02e6\7`\2\2\u02e6\u02e7"+
		"\7?\2\2\u02e7\u00ca\3\2\2\2\u02e8\u02e9\7>\2\2\u02e9\u02ea\7>\2\2\u02ea"+
		"\u02eb\7?\2\2\u02eb\u00cc\3\2\2\2\u02ec\u02ed\7@\2\2\u02ed\u02ee\7@\2"+
		"\2\u02ee\u02ef\7?\2\2\u02ef\u00ce\3\2\2\2\u02f0\u02f1\7,\2\2\u02f1\u02f2"+
		"\7,\2\2\u02f2\u02f3\7?\2\2\u02f3\u00d0\3\2\2\2\u02f4\u02f5\7\61\2\2\u02f5"+
		"\u02f6\7\61\2\2\u02f6\u02f7\7?\2\2\u02f7\u00d2\3\2\2\2\u02f8\u02fc\5\u00d5"+
		"k\2\u02f9\u02fb\5\u00d7l\2\u02fa\u02f9\3\2\2\2\u02fb\u02fe\3\2\2\2\u02fc"+
		"\u02fa\3\2\2\2\u02fc\u02fd\3\2\2\2\u02fd\u00d4\3\2\2\2\u02fe\u02fc\3\2"+
		"\2\2\u02ff\u0301\t\16\2\2\u0300\u02ff\3\2\2\2\u0301\u00d6\3\2\2\2\u0302"+
		"\u0305\5\u00d5k\2\u0303\u0305\t\b\2\2\u0304\u0302\3\2\2\2\u0304\u0303"+
		"\3\2\2\2\u0305\u00d8\3\2\2\2\u0306\u030a\5\u00ddo\2\u0307\u030a\5\u00df"+
		"p\2\u0308\u030a\5\u00e1q\2\u0309\u0306\3\2\2\2\u0309\u0307\3\2\2\2\u0309"+
		"\u0308\3\2\2\2\u030a\u030b\3\2\2\2\u030b\u030c\bm\t\2\u030c\u00da\3\2"+
		"\2\2\u030d\u030e\13\2\2\2\u030e\u00dc\3\2\2\2\u030f\u0311\t\17\2\2\u0310"+
		"\u030f\3\2\2\2\u0311\u0312\3\2\2\2\u0312\u0310\3\2\2\2\u0312\u0313\3\2"+
		"\2\2\u0313\u00de\3\2\2\2\u0314\u0318\7%\2\2\u0315\u0317\n\20\2\2\u0316"+
		"\u0315\3\2\2\2\u0317\u031a\3\2\2\2\u0318\u0316\3\2\2\2\u0318\u0319\3\2"+
		"\2\2\u0319\u00e0\3\2\2\2\u031a\u0318\3\2\2\2\u031b\u031d\7^\2\2\u031c"+
		"\u031e\5\u00ddo\2\u031d\u031c\3\2\2\2\u031d\u031e\3\2\2\2\u031e\u0324"+
		"\3\2\2\2\u031f\u0321\7\17\2\2\u0320\u031f\3\2\2\2\u0320\u0321\3\2\2\2"+
		"\u0321\u0322\3\2\2\2\u0322\u0325\7\f\2\2\u0323\u0325\7\17\2\2\u0324\u0320"+
		"\3\2\2\2\u0324\u0323\3\2\2\2\u0325\u00e2\3\2\2\2$\2\u01fa\u01fe\u0201"+
		"\u0203\u020a\u020c\u0213\u0215\u0219\u0222\u0228\u022a\u0231\u0238\u023f"+
		"\u024d\u0250\u0256\u025a\u0261\u0267\u026b\u0270\u02c6\u02fc\u0300\u0304"+
		"\u0309\u0312\u0318\u031d\u0320\u0324\n\3)\2\3?\3\3@\4\3F\5\3G\6\3S\7\3"+
		"T\b\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
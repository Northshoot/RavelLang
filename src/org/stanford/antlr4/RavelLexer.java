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
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, MODEL=12, SPACE=13, CONTROLLER=14, VIEW=15, FLOW=16, 
		LOCAL=17, STREAMING=18, REPLICATED=19, INTERFACE=20, DEF=21, EVENT=22, 
		COMMAND=23, ASSERT=24, RETURN=25, TRUE=26, FALSE=27, IF=28, ELIF=29, ELSE=30, 
		FOR=31, WHILE=32, AND=33, NOT=34, OR=35, IN=36, IS=37, DELETE=38, PASS=39, 
		CONTINUE=40, BREAK=41, NONE=42, NEWLINE=43, STRING_LITERAL=44, DECIMAL_INTEGER=45, 
		OCT_INTEGER=46, HEX_INTEGER=47, BIN_INTEGER=48, FLOAT_NUMBER=49, NullLiteral=50, 
		DOT=51, ELLIPSIS=52, STAR=53, OPEN_PAREN=54, CLOSE_PAREN=55, COMMA=56, 
		COLON=57, SEMI_COLON=58, POWER=59, ASSIGN=60, OPEN_BRACK=61, CLOSE_BRACK=62, 
		OR_OP=63, XOR=64, AND_OP=65, LEFT_SHIFT=66, RIGHT_SHIFT=67, ADD=68, MINUS=69, 
		DIV=70, MOD=71, IDIV=72, NOT_OP=73, OPEN_BRACE=74, CLOSE_BRACE=75, LT=76, 
		GT=77, EQUAL=78, GE=79, LE=80, NOTEQUAL=81, AT=82, ARROW=83, ADD_ASSIGN=84, 
		SUB_ASSIGN=85, MULT_ASSIGN=86, AT_ASSIGN=87, DIV_ASSIGN=88, MOD_ASSIGN=89, 
		AND_ASSIGN=90, OR_ASSIGN=91, XOR_ASSIGN=92, LEFT_SHIFT_ASSIGN=93, RIGHT_SHIFT_ASSIGN=94, 
		POWER_ASSIGN=95, IDIV_ASSIGN=96, Identifier=97, SKIP_=98, UNKNOWN_CHAR=99;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "MODEL", "SPACE", "CONTROLLER", "VIEW", "FLOW", "LOCAL", 
		"STREAMING", "REPLICATED", "INTERFACE", "DEF", "EVENT", "COMMAND", "ASSERT", 
		"RETURN", "TRUE", "FALSE", "IF", "ELIF", "ELSE", "FOR", "WHILE", "AND", 
		"NOT", "OR", "IN", "IS", "DELETE", "PASS", "CONTINUE", "BREAK", "NONE", 
		"NEWLINE", "STRING_LITERAL", "STRING_ESCAPE_SEQ", "DECIMAL_INTEGER", "OCT_INTEGER", 
		"HEX_INTEGER", "BIN_INTEGER", "NON_ZERO_DIGIT", "DIGIT", "OCT_DIGIT", 
		"HEX_DIGIT", "BIN_DIGIT", "FLOAT_NUMBER", "POINT_FLOAT", "EXPONENT_FLOAT", 
		"INT_PART", "FRACTION", "EXPONENT", "NullLiteral", "DOT", "ELLIPSIS", 
		"STAR", "OPEN_PAREN", "CLOSE_PAREN", "COMMA", "COLON", "SEMI_COLON", "POWER", 
		"ASSIGN", "OPEN_BRACK", "CLOSE_BRACK", "OR_OP", "XOR", "AND_OP", "LEFT_SHIFT", 
		"RIGHT_SHIFT", "ADD", "MINUS", "DIV", "MOD", "IDIV", "NOT_OP", "OPEN_BRACE", 
		"CLOSE_BRACE", "LT", "GT", "EQUAL", "GE", "LE", "NOTEQUAL", "AT", "ARROW", 
		"ADD_ASSIGN", "SUB_ASSIGN", "MULT_ASSIGN", "AT_ASSIGN", "DIV_ASSIGN", 
		"MOD_ASSIGN", "AND_ASSIGN", "OR_ASSIGN", "XOR_ASSIGN", "LEFT_SHIFT_ASSIGN", 
		"RIGHT_SHIFT_ASSIGN", "POWER_ASSIGN", "IDIV_ASSIGN", "Identifier", "ID_START", 
		"ID_CONTINUE", "SKIP_", "UNKNOWN_CHAR", "SPACES", "COMMENT", "LINE_JOINING"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'platform:'", "'models:'", "'controllers:'", "'interfaces:'", "'implementation:'", 
		"'configuration:'", "'use:'", "'properties:'", "'schema:'", "'to'", "'step'", 
		"'model'", "'space'", "'controller'", "'view'", "'flow'", "'local'", "'streaming'", 
		"'replicated'", "'interface'", "'def'", "'event'", "'command'", "'assert'", 
		"'return'", "'True'", "'False'", "'if'", "'elif'", "'else'", "'for'", 
		"'while'", "'and'", "'not'", "'or'", "'in'", "'is'", "'del'", "'pass'", 
		"'continue'", "'break'", "'None'", null, null, null, null, null, null, 
		null, "'null'", "'.'", "'...'", "'*'", "'('", "')'", "','", "':'", "';'", 
		"'**'", "'='", "'['", "']'", "'|'", "'^'", "'&'", "'<<'", "'>>'", "'+'", 
		"'-'", "'/'", "'%'", "'//'", "'~'", "'{'", "'}'", "'<'", "'>'", "'=='", 
		"'>='", "'<='", null, "'@'", "'->'", "'+='", "'-='", "'*='", "'@='", "'/='", 
		"'%='", "'&='", "'|='", "'^='", "'<<='", "'>>='", "'**='", "'//='"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		"MODEL", "SPACE", "CONTROLLER", "VIEW", "FLOW", "LOCAL", "STREAMING", 
		"REPLICATED", "INTERFACE", "DEF", "EVENT", "COMMAND", "ASSERT", "RETURN", 
		"TRUE", "FALSE", "IF", "ELIF", "ELSE", "FOR", "WHILE", "AND", "NOT", "OR", 
		"IN", "IS", "DELETE", "PASS", "CONTINUE", "BREAK", "NONE", "NEWLINE", 
		"STRING_LITERAL", "DECIMAL_INTEGER", "OCT_INTEGER", "HEX_INTEGER", "BIN_INTEGER", 
		"FLOAT_NUMBER", "NullLiteral", "DOT", "ELLIPSIS", "STAR", "OPEN_PAREN", 
		"CLOSE_PAREN", "COMMA", "COLON", "SEMI_COLON", "POWER", "ASSIGN", "OPEN_BRACK", 
		"CLOSE_BRACK", "OR_OP", "XOR", "AND_OP", "LEFT_SHIFT", "RIGHT_SHIFT", 
		"ADD", "MINUS", "DIV", "MOD", "IDIV", "NOT_OP", "OPEN_BRACE", "CLOSE_BRACE", 
		"LT", "GT", "EQUAL", "GE", "LE", "NOTEQUAL", "AT", "ARROW", "ADD_ASSIGN", 
		"SUB_ASSIGN", "MULT_ASSIGN", "AT_ASSIGN", "DIV_ASSIGN", "MOD_ASSIGN", 
		"AND_ASSIGN", "OR_ASSIGN", "XOR_ASSIGN", "LEFT_SHIFT_ASSIGN", "RIGHT_SHIFT_ASSIGN", 
		"POWER_ASSIGN", "IDIV_ASSIGN", "Identifier", "SKIP_", "UNKNOWN_CHAR"
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
		case 42:
			NEWLINE_action((RuleContext)_localctx, actionIndex);
			break;
		case 64:
			OPEN_PAREN_action((RuleContext)_localctx, actionIndex);
			break;
		case 65:
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
		case 42:
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2e\u0339\b\1\4\2\t"+
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
		"k\4l\tl\4m\tm\4n\tn\4o\to\4p\tp\4q\tq\4r\tr\4s\ts\4t\tt\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3"+
		"\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\21\3"+
		"\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3"+
		"\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3"+
		"\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3"+
		"\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3"+
		"\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3"+
		"\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\34\3"+
		"\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3 \3 "+
		"\3 \3 \3!\3!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3$\3$\3$\3%\3%\3"+
		"%\3&\3&\3&\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3(\3)\3)\3)\3)\3)\3)\3)\3)\3)\3"+
		"*\3*\3*\3*\3*\3*\3+\3+\3+\3+\3+\3,\3,\3,\5,\u020e\n,\3,\3,\5,\u0212\n"+
		",\3,\5,\u0215\n,\5,\u0217\n,\3,\3,\3-\3-\3-\7-\u021e\n-\f-\16-\u0221\13"+
		"-\3-\3-\3-\3-\7-\u0227\n-\f-\16-\u022a\13-\3-\5-\u022d\n-\3.\3.\3.\3/"+
		"\3/\7/\u0234\n/\f/\16/\u0237\13/\3/\6/\u023a\n/\r/\16/\u023b\5/\u023e"+
		"\n/\3\60\3\60\3\60\6\60\u0243\n\60\r\60\16\60\u0244\3\61\3\61\3\61\6\61"+
		"\u024a\n\61\r\61\16\61\u024b\3\62\3\62\3\62\6\62\u0251\n\62\r\62\16\62"+
		"\u0252\3\63\3\63\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3\67\38\38\58\u0261"+
		"\n8\39\59\u0264\n9\39\39\39\39\59\u026a\n9\3:\3:\5:\u026e\n:\3:\3:\3;"+
		"\6;\u0273\n;\r;\16;\u0274\3<\3<\6<\u0279\n<\r<\16<\u027a\3=\3=\5=\u027f"+
		"\n=\3=\6=\u0282\n=\r=\16=\u0283\3>\3>\3>\3>\3>\3?\3?\3@\3@\3@\3@\3A\3"+
		"A\3B\3B\3B\3C\3C\3C\3D\3D\3E\3E\3F\3F\3G\3G\3G\3H\3H\3I\3I\3I\3J\3J\3"+
		"J\3K\3K\3L\3L\3M\3M\3N\3N\3N\3O\3O\3O\3P\3P\3Q\3Q\3R\3R\3S\3S\3T\3T\3"+
		"T\3U\3U\3V\3V\3V\3W\3W\3W\3X\3X\3Y\3Y\3Z\3Z\3Z\3[\3[\3[\3\\\3\\\3\\\3"+
		"]\3]\3]\3]\5]\u02da\n]\3^\3^\3_\3_\3_\3`\3`\3`\3a\3a\3a\3b\3b\3b\3c\3"+
		"c\3c\3d\3d\3d\3e\3e\3e\3f\3f\3f\3g\3g\3g\3h\3h\3h\3i\3i\3i\3i\3j\3j\3"+
		"j\3j\3k\3k\3k\3k\3l\3l\3l\3l\3m\3m\7m\u030e\nm\fm\16m\u0311\13m\3n\5n"+
		"\u0314\nn\3o\3o\5o\u0318\no\3p\3p\3p\5p\u031d\np\3p\3p\3q\3q\3r\6r\u0324"+
		"\nr\rr\16r\u0325\3s\3s\7s\u032a\ns\fs\16s\u032d\13s\3t\3t\5t\u0331\nt"+
		"\3t\5t\u0334\nt\3t\3t\5t\u0338\nt\2\2u\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21"+
		"\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30"+
		"/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.["+
		"\2]/_\60a\61c\62e\2g\2i\2k\2m\2o\63q\2s\2u\2w\2y\2{\64}\65\177\66\u0081"+
		"\67\u00838\u00859\u0087:\u0089;\u008b<\u008d=\u008f>\u0091?\u0093@\u0095"+
		"A\u0097B\u0099C\u009bD\u009dE\u009fF\u00a1G\u00a3H\u00a5I\u00a7J\u00a9"+
		"K\u00abL\u00adM\u00afN\u00b1O\u00b3P\u00b5Q\u00b7R\u00b9S\u00bbT\u00bd"+
		"U\u00bfV\u00c1W\u00c3X\u00c5Y\u00c7Z\u00c9[\u00cb\\\u00cd]\u00cf^\u00d1"+
		"_\u00d3`\u00d5a\u00d7b\u00d9c\u00db\2\u00dd\2\u00dfd\u00e1e\u00e3\2\u00e5"+
		"\2\u00e7\2\3\2\21\6\2\f\f\17\17))^^\6\2\f\f\17\17$$^^\4\2QQqq\4\2ZZzz"+
		"\4\2DDdd\3\2\63;\3\2\62;\3\2\629\5\2\62;CHch\3\2\62\63\4\2GGgg\4\2--/"+
		"/\5\2C\\aac|\4\2\13\13\"\"\4\2\f\f\17\17\u0349\2\3\3\2\2\2\2\5\3\2\2\2"+
		"\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3"+
		"\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2"+
		"\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2"+
		"\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2"+
		"\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2"+
		"\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2"+
		"\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y"+
		"\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2o\3\2\2\2\2{\3\2"+
		"\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2\2\u0085"+
		"\3\2\2\2\2\u0087\3\2\2\2\2\u0089\3\2\2\2\2\u008b\3\2\2\2\2\u008d\3\2\2"+
		"\2\2\u008f\3\2\2\2\2\u0091\3\2\2\2\2\u0093\3\2\2\2\2\u0095\3\2\2\2\2\u0097"+
		"\3\2\2\2\2\u0099\3\2\2\2\2\u009b\3\2\2\2\2\u009d\3\2\2\2\2\u009f\3\2\2"+
		"\2\2\u00a1\3\2\2\2\2\u00a3\3\2\2\2\2\u00a5\3\2\2\2\2\u00a7\3\2\2\2\2\u00a9"+
		"\3\2\2\2\2\u00ab\3\2\2\2\2\u00ad\3\2\2\2\2\u00af\3\2\2\2\2\u00b1\3\2\2"+
		"\2\2\u00b3\3\2\2\2\2\u00b5\3\2\2\2\2\u00b7\3\2\2\2\2\u00b9\3\2\2\2\2\u00bb"+
		"\3\2\2\2\2\u00bd\3\2\2\2\2\u00bf\3\2\2\2\2\u00c1\3\2\2\2\2\u00c3\3\2\2"+
		"\2\2\u00c5\3\2\2\2\2\u00c7\3\2\2\2\2\u00c9\3\2\2\2\2\u00cb\3\2\2\2\2\u00cd"+
		"\3\2\2\2\2\u00cf\3\2\2\2\2\u00d1\3\2\2\2\2\u00d3\3\2\2\2\2\u00d5\3\2\2"+
		"\2\2\u00d7\3\2\2\2\2\u00d9\3\2\2\2\2\u00df\3\2\2\2\2\u00e1\3\2\2\2\3\u00e9"+
		"\3\2\2\2\5\u00f3\3\2\2\2\7\u00fb\3\2\2\2\t\u0108\3\2\2\2\13\u0114\3\2"+
		"\2\2\r\u0124\3\2\2\2\17\u0133\3\2\2\2\21\u0138\3\2\2\2\23\u0144\3\2\2"+
		"\2\25\u014c\3\2\2\2\27\u014f\3\2\2\2\31\u0154\3\2\2\2\33\u015a\3\2\2\2"+
		"\35\u0160\3\2\2\2\37\u016b\3\2\2\2!\u0170\3\2\2\2#\u0175\3\2\2\2%\u017b"+
		"\3\2\2\2\'\u0185\3\2\2\2)\u0190\3\2\2\2+\u019a\3\2\2\2-\u019e\3\2\2\2"+
		"/\u01a4\3\2\2\2\61\u01ac\3\2\2\2\63\u01b3\3\2\2\2\65\u01ba\3\2\2\2\67"+
		"\u01bf\3\2\2\29\u01c5\3\2\2\2;\u01c8\3\2\2\2=\u01cd\3\2\2\2?\u01d2\3\2"+
		"\2\2A\u01d6\3\2\2\2C\u01dc\3\2\2\2E\u01e0\3\2\2\2G\u01e4\3\2\2\2I\u01e7"+
		"\3\2\2\2K\u01ea\3\2\2\2M\u01ed\3\2\2\2O\u01f1\3\2\2\2Q\u01f6\3\2\2\2S"+
		"\u01ff\3\2\2\2U\u0205\3\2\2\2W\u0216\3\2\2\2Y\u022c\3\2\2\2[\u022e\3\2"+
		"\2\2]\u023d\3\2\2\2_\u023f\3\2\2\2a\u0246\3\2\2\2c\u024d\3\2\2\2e\u0254"+
		"\3\2\2\2g\u0256\3\2\2\2i\u0258\3\2\2\2k\u025a\3\2\2\2m\u025c\3\2\2\2o"+
		"\u0260\3\2\2\2q\u0269\3\2\2\2s\u026d\3\2\2\2u\u0272\3\2\2\2w\u0276\3\2"+
		"\2\2y\u027c\3\2\2\2{\u0285\3\2\2\2}\u028a\3\2\2\2\177\u028c\3\2\2\2\u0081"+
		"\u0290\3\2\2\2\u0083\u0292\3\2\2\2\u0085\u0295\3\2\2\2\u0087\u0298\3\2"+
		"\2\2\u0089\u029a\3\2\2\2\u008b\u029c\3\2\2\2\u008d\u029e\3\2\2\2\u008f"+
		"\u02a1\3\2\2\2\u0091\u02a3\3\2\2\2\u0093\u02a6\3\2\2\2\u0095\u02a9\3\2"+
		"\2\2\u0097\u02ab\3\2\2\2\u0099\u02ad\3\2\2\2\u009b\u02af\3\2\2\2\u009d"+
		"\u02b2\3\2\2\2\u009f\u02b5\3\2\2\2\u00a1\u02b7\3\2\2\2\u00a3\u02b9\3\2"+
		"\2\2\u00a5\u02bb\3\2\2\2\u00a7\u02bd\3\2\2\2\u00a9\u02c0\3\2\2\2\u00ab"+
		"\u02c2\3\2\2\2\u00ad\u02c5\3\2\2\2\u00af\u02c8\3\2\2\2\u00b1\u02ca\3\2"+
		"\2\2\u00b3\u02cc\3\2\2\2\u00b5\u02cf\3\2\2\2\u00b7\u02d2\3\2\2\2\u00b9"+
		"\u02d9\3\2\2\2\u00bb\u02db\3\2\2\2\u00bd\u02dd\3\2\2\2\u00bf\u02e0\3\2"+
		"\2\2\u00c1\u02e3\3\2\2\2\u00c3\u02e6\3\2\2\2\u00c5\u02e9\3\2\2\2\u00c7"+
		"\u02ec\3\2\2\2\u00c9\u02ef\3\2\2\2\u00cb\u02f2\3\2\2\2\u00cd\u02f5\3\2"+
		"\2\2\u00cf\u02f8\3\2\2\2\u00d1\u02fb\3\2\2\2\u00d3\u02ff\3\2\2\2\u00d5"+
		"\u0303\3\2\2\2\u00d7\u0307\3\2\2\2\u00d9\u030b\3\2\2\2\u00db\u0313\3\2"+
		"\2\2\u00dd\u0317\3\2\2\2\u00df\u031c\3\2\2\2\u00e1\u0320\3\2\2\2\u00e3"+
		"\u0323\3\2\2\2\u00e5\u0327\3\2\2\2\u00e7\u032e\3\2\2\2\u00e9\u00ea\7r"+
		"\2\2\u00ea\u00eb\7n\2\2\u00eb\u00ec\7c\2\2\u00ec\u00ed\7v\2\2\u00ed\u00ee"+
		"\7h\2\2\u00ee\u00ef\7q\2\2\u00ef\u00f0\7t\2\2\u00f0\u00f1\7o\2\2\u00f1"+
		"\u00f2\7<\2\2\u00f2\4\3\2\2\2\u00f3\u00f4\7o\2\2\u00f4\u00f5\7q\2\2\u00f5"+
		"\u00f6\7f\2\2\u00f6\u00f7\7g\2\2\u00f7\u00f8\7n\2\2\u00f8\u00f9\7u\2\2"+
		"\u00f9\u00fa\7<\2\2\u00fa\6\3\2\2\2\u00fb\u00fc\7e\2\2\u00fc\u00fd\7q"+
		"\2\2\u00fd\u00fe\7p\2\2\u00fe\u00ff\7v\2\2\u00ff\u0100\7t\2\2\u0100\u0101"+
		"\7q\2\2\u0101\u0102\7n\2\2\u0102\u0103\7n\2\2\u0103\u0104\7g\2\2\u0104"+
		"\u0105\7t\2\2\u0105\u0106\7u\2\2\u0106\u0107\7<\2\2\u0107\b\3\2\2\2\u0108"+
		"\u0109\7k\2\2\u0109\u010a\7p\2\2\u010a\u010b\7v\2\2\u010b\u010c\7g\2\2"+
		"\u010c\u010d\7t\2\2\u010d\u010e\7h\2\2\u010e\u010f\7c\2\2\u010f\u0110"+
		"\7e\2\2\u0110\u0111\7g\2\2\u0111\u0112\7u\2\2\u0112\u0113\7<\2\2\u0113"+
		"\n\3\2\2\2\u0114\u0115\7k\2\2\u0115\u0116\7o\2\2\u0116\u0117\7r\2\2\u0117"+
		"\u0118\7n\2\2\u0118\u0119\7g\2\2\u0119\u011a\7o\2\2\u011a\u011b\7g\2\2"+
		"\u011b\u011c\7p\2\2\u011c\u011d\7v\2\2\u011d\u011e\7c\2\2\u011e\u011f"+
		"\7v\2\2\u011f\u0120\7k\2\2\u0120\u0121\7q\2\2\u0121\u0122\7p\2\2\u0122"+
		"\u0123\7<\2\2\u0123\f\3\2\2\2\u0124\u0125\7e\2\2\u0125\u0126\7q\2\2\u0126"+
		"\u0127\7p\2\2\u0127\u0128\7h\2\2\u0128\u0129\7k\2\2\u0129\u012a\7i\2\2"+
		"\u012a\u012b\7w\2\2\u012b\u012c\7t\2\2\u012c\u012d\7c\2\2\u012d\u012e"+
		"\7v\2\2\u012e\u012f\7k\2\2\u012f\u0130\7q\2\2\u0130\u0131\7p\2\2\u0131"+
		"\u0132\7<\2\2\u0132\16\3\2\2\2\u0133\u0134\7w\2\2\u0134\u0135\7u\2\2\u0135"+
		"\u0136\7g\2\2\u0136\u0137\7<\2\2\u0137\20\3\2\2\2\u0138\u0139\7r\2\2\u0139"+
		"\u013a\7t\2\2\u013a\u013b\7q\2\2\u013b\u013c\7r\2\2\u013c\u013d\7g\2\2"+
		"\u013d\u013e\7t\2\2\u013e\u013f\7v\2\2\u013f\u0140\7k\2\2\u0140\u0141"+
		"\7g\2\2\u0141\u0142\7u\2\2\u0142\u0143\7<\2\2\u0143\22\3\2\2\2\u0144\u0145"+
		"\7u\2\2\u0145\u0146\7e\2\2\u0146\u0147\7j\2\2\u0147\u0148\7g\2\2\u0148"+
		"\u0149\7o\2\2\u0149\u014a\7c\2\2\u014a\u014b\7<\2\2\u014b\24\3\2\2\2\u014c"+
		"\u014d\7v\2\2\u014d\u014e\7q\2\2\u014e\26\3\2\2\2\u014f\u0150\7u\2\2\u0150"+
		"\u0151\7v\2\2\u0151\u0152\7g\2\2\u0152\u0153\7r\2\2\u0153\30\3\2\2\2\u0154"+
		"\u0155\7o\2\2\u0155\u0156\7q\2\2\u0156\u0157\7f\2\2\u0157\u0158\7g\2\2"+
		"\u0158\u0159\7n\2\2\u0159\32\3\2\2\2\u015a\u015b\7u\2\2\u015b\u015c\7"+
		"r\2\2\u015c\u015d\7c\2\2\u015d\u015e\7e\2\2\u015e\u015f\7g\2\2\u015f\34"+
		"\3\2\2\2\u0160\u0161\7e\2\2\u0161\u0162\7q\2\2\u0162\u0163\7p\2\2\u0163"+
		"\u0164\7v\2\2\u0164\u0165\7t\2\2\u0165\u0166\7q\2\2\u0166\u0167\7n\2\2"+
		"\u0167\u0168\7n\2\2\u0168\u0169\7g\2\2\u0169\u016a\7t\2\2\u016a\36\3\2"+
		"\2\2\u016b\u016c\7x\2\2\u016c\u016d\7k\2\2\u016d\u016e\7g\2\2\u016e\u016f"+
		"\7y\2\2\u016f \3\2\2\2\u0170\u0171\7h\2\2\u0171\u0172\7n\2\2\u0172\u0173"+
		"\7q\2\2\u0173\u0174\7y\2\2\u0174\"\3\2\2\2\u0175\u0176\7n\2\2\u0176\u0177"+
		"\7q\2\2\u0177\u0178\7e\2\2\u0178\u0179\7c\2\2\u0179\u017a\7n\2\2\u017a"+
		"$\3\2\2\2\u017b\u017c\7u\2\2\u017c\u017d\7v\2\2\u017d\u017e\7t\2\2\u017e"+
		"\u017f\7g\2\2\u017f\u0180\7c\2\2\u0180\u0181\7o\2\2\u0181\u0182\7k\2\2"+
		"\u0182\u0183\7p\2\2\u0183\u0184\7i\2\2\u0184&\3\2\2\2\u0185\u0186\7t\2"+
		"\2\u0186\u0187\7g\2\2\u0187\u0188\7r\2\2\u0188\u0189\7n\2\2\u0189\u018a"+
		"\7k\2\2\u018a\u018b\7e\2\2\u018b\u018c\7c\2\2\u018c\u018d\7v\2\2\u018d"+
		"\u018e\7g\2\2\u018e\u018f\7f\2\2\u018f(\3\2\2\2\u0190\u0191\7k\2\2\u0191"+
		"\u0192\7p\2\2\u0192\u0193\7v\2\2\u0193\u0194\7g\2\2\u0194\u0195\7t\2\2"+
		"\u0195\u0196\7h\2\2\u0196\u0197\7c\2\2\u0197\u0198\7e\2\2\u0198\u0199"+
		"\7g\2\2\u0199*\3\2\2\2\u019a\u019b\7f\2\2\u019b\u019c\7g\2\2\u019c\u019d"+
		"\7h\2\2\u019d,\3\2\2\2\u019e\u019f\7g\2\2\u019f\u01a0\7x\2\2\u01a0\u01a1"+
		"\7g\2\2\u01a1\u01a2\7p\2\2\u01a2\u01a3\7v\2\2\u01a3.\3\2\2\2\u01a4\u01a5"+
		"\7e\2\2\u01a5\u01a6\7q\2\2\u01a6\u01a7\7o\2\2\u01a7\u01a8\7o\2\2\u01a8"+
		"\u01a9\7c\2\2\u01a9\u01aa\7p\2\2\u01aa\u01ab\7f\2\2\u01ab\60\3\2\2\2\u01ac"+
		"\u01ad\7c\2\2\u01ad\u01ae\7u\2\2\u01ae\u01af\7u\2\2\u01af\u01b0\7g\2\2"+
		"\u01b0\u01b1\7t\2\2\u01b1\u01b2\7v\2\2\u01b2\62\3\2\2\2\u01b3\u01b4\7"+
		"t\2\2\u01b4\u01b5\7g\2\2\u01b5\u01b6\7v\2\2\u01b6\u01b7\7w\2\2\u01b7\u01b8"+
		"\7t\2\2\u01b8\u01b9\7p\2\2\u01b9\64\3\2\2\2\u01ba\u01bb\7V\2\2\u01bb\u01bc"+
		"\7t\2\2\u01bc\u01bd\7w\2\2\u01bd\u01be\7g\2\2\u01be\66\3\2\2\2\u01bf\u01c0"+
		"\7H\2\2\u01c0\u01c1\7c\2\2\u01c1\u01c2\7n\2\2\u01c2\u01c3\7u\2\2\u01c3"+
		"\u01c4\7g\2\2\u01c48\3\2\2\2\u01c5\u01c6\7k\2\2\u01c6\u01c7\7h\2\2\u01c7"+
		":\3\2\2\2\u01c8\u01c9\7g\2\2\u01c9\u01ca\7n\2\2\u01ca\u01cb\7k\2\2\u01cb"+
		"\u01cc\7h\2\2\u01cc<\3\2\2\2\u01cd\u01ce\7g\2\2\u01ce\u01cf\7n\2\2\u01cf"+
		"\u01d0\7u\2\2\u01d0\u01d1\7g\2\2\u01d1>\3\2\2\2\u01d2\u01d3\7h\2\2\u01d3"+
		"\u01d4\7q\2\2\u01d4\u01d5\7t\2\2\u01d5@\3\2\2\2\u01d6\u01d7\7y\2\2\u01d7"+
		"\u01d8\7j\2\2\u01d8\u01d9\7k\2\2\u01d9\u01da\7n\2\2\u01da\u01db\7g\2\2"+
		"\u01dbB\3\2\2\2\u01dc\u01dd\7c\2\2\u01dd\u01de\7p\2\2\u01de\u01df\7f\2"+
		"\2\u01dfD\3\2\2\2\u01e0\u01e1\7p\2\2\u01e1\u01e2\7q\2\2\u01e2\u01e3\7"+
		"v\2\2\u01e3F\3\2\2\2\u01e4\u01e5\7q\2\2\u01e5\u01e6\7t\2\2\u01e6H\3\2"+
		"\2\2\u01e7\u01e8\7k\2\2\u01e8\u01e9\7p\2\2\u01e9J\3\2\2\2\u01ea\u01eb"+
		"\7k\2\2\u01eb\u01ec\7u\2\2\u01ecL\3\2\2\2\u01ed\u01ee\7f\2\2\u01ee\u01ef"+
		"\7g\2\2\u01ef\u01f0\7n\2\2\u01f0N\3\2\2\2\u01f1\u01f2\7r\2\2\u01f2\u01f3"+
		"\7c\2\2\u01f3\u01f4\7u\2\2\u01f4\u01f5\7u\2\2\u01f5P\3\2\2\2\u01f6\u01f7"+
		"\7e\2\2\u01f7\u01f8\7q\2\2\u01f8\u01f9\7p\2\2\u01f9\u01fa\7v\2\2\u01fa"+
		"\u01fb\7k\2\2\u01fb\u01fc\7p\2\2\u01fc\u01fd\7w\2\2\u01fd\u01fe\7g\2\2"+
		"\u01feR\3\2\2\2\u01ff\u0200\7d\2\2\u0200\u0201\7t\2\2\u0201\u0202\7g\2"+
		"\2\u0202\u0203\7c\2\2\u0203\u0204\7m\2\2\u0204T\3\2\2\2\u0205\u0206\7"+
		"P\2\2\u0206\u0207\7q\2\2\u0207\u0208\7p\2\2\u0208\u0209\7g\2\2\u0209V"+
		"\3\2\2\2\u020a\u020b\6,\2\2\u020b\u0217\5\u00e3r\2\u020c\u020e\7\17\2"+
		"\2\u020d\u020c\3\2\2\2\u020d\u020e\3\2\2\2\u020e\u020f\3\2\2\2\u020f\u0212"+
		"\7\f\2\2\u0210\u0212\7\17\2\2\u0211\u020d\3\2\2\2\u0211\u0210\3\2\2\2"+
		"\u0212\u0214\3\2\2\2\u0213\u0215\5\u00e3r\2\u0214\u0213\3\2\2\2\u0214"+
		"\u0215\3\2\2\2\u0215\u0217\3\2\2\2\u0216\u020a\3\2\2\2\u0216\u0211\3\2"+
		"\2\2\u0217\u0218\3\2\2\2\u0218\u0219\b,\2\2\u0219X\3\2\2\2\u021a\u021f"+
		"\7)\2\2\u021b\u021e\5[.\2\u021c\u021e\n\2\2\2\u021d\u021b\3\2\2\2\u021d"+
		"\u021c\3\2\2\2\u021e\u0221\3\2\2\2\u021f\u021d\3\2\2\2\u021f\u0220\3\2"+
		"\2\2\u0220\u0222\3\2\2\2\u0221\u021f\3\2\2\2\u0222\u022d\7)\2\2\u0223"+
		"\u0228\7$\2\2\u0224\u0227\5[.\2\u0225\u0227\n\3\2\2\u0226\u0224\3\2\2"+
		"\2\u0226\u0225\3\2\2\2\u0227\u022a\3\2\2\2\u0228\u0226\3\2\2\2\u0228\u0229"+
		"\3\2\2\2\u0229\u022b\3\2\2\2\u022a\u0228\3\2\2\2\u022b\u022d\7$\2\2\u022c"+
		"\u021a\3\2\2\2\u022c\u0223\3\2\2\2\u022dZ\3\2\2\2\u022e\u022f\7^\2\2\u022f"+
		"\u0230\13\2\2\2\u0230\\\3\2\2\2\u0231\u0235\5e\63\2\u0232\u0234\5g\64"+
		"\2\u0233\u0232\3\2\2\2\u0234\u0237\3\2\2\2\u0235\u0233\3\2\2\2\u0235\u0236"+
		"\3\2\2\2\u0236\u023e\3\2\2\2\u0237\u0235\3\2\2\2\u0238\u023a\7\62\2\2"+
		"\u0239\u0238\3\2\2\2\u023a\u023b\3\2\2\2\u023b\u0239\3\2\2\2\u023b\u023c"+
		"\3\2\2\2\u023c\u023e\3\2\2\2\u023d\u0231\3\2\2\2\u023d\u0239\3\2\2\2\u023e"+
		"^\3\2\2\2\u023f\u0240\7\62\2\2\u0240\u0242\t\4\2\2\u0241\u0243\5i\65\2"+
		"\u0242\u0241\3\2\2\2\u0243\u0244\3\2\2\2\u0244\u0242\3\2\2\2\u0244\u0245"+
		"\3\2\2\2\u0245`\3\2\2\2\u0246\u0247\7\62\2\2\u0247\u0249\t\5\2\2\u0248"+
		"\u024a\5k\66\2\u0249\u0248\3\2\2\2\u024a\u024b\3\2\2\2\u024b\u0249\3\2"+
		"\2\2\u024b\u024c\3\2\2\2\u024cb\3\2\2\2\u024d\u024e\7\62\2\2\u024e\u0250"+
		"\t\6\2\2\u024f\u0251\5m\67\2\u0250\u024f\3\2\2\2\u0251\u0252\3\2\2\2\u0252"+
		"\u0250\3\2\2\2\u0252\u0253\3\2\2\2\u0253d\3\2\2\2\u0254\u0255\t\7\2\2"+
		"\u0255f\3\2\2\2\u0256\u0257\t\b\2\2\u0257h\3\2\2\2\u0258\u0259\t\t\2\2"+
		"\u0259j\3\2\2\2\u025a\u025b\t\n\2\2\u025bl\3\2\2\2\u025c\u025d\t\13\2"+
		"\2\u025dn\3\2\2\2\u025e\u0261\5q9\2\u025f\u0261\5s:\2\u0260\u025e\3\2"+
		"\2\2\u0260\u025f\3\2\2\2\u0261p\3\2\2\2\u0262\u0264\5u;\2\u0263\u0262"+
		"\3\2\2\2\u0263\u0264\3\2\2\2\u0264\u0265\3\2\2\2\u0265\u026a\5w<\2\u0266"+
		"\u0267\5u;\2\u0267\u0268\7\60\2\2\u0268\u026a\3\2\2\2\u0269\u0263\3\2"+
		"\2\2\u0269\u0266\3\2\2\2\u026ar\3\2\2\2\u026b\u026e\5u;\2\u026c\u026e"+
		"\5q9\2\u026d\u026b\3\2\2\2\u026d\u026c\3\2\2\2\u026e\u026f\3\2\2\2\u026f"+
		"\u0270\5y=\2\u0270t\3\2\2\2\u0271\u0273\5g\64\2\u0272\u0271\3\2\2\2\u0273"+
		"\u0274\3\2\2\2\u0274\u0272\3\2\2\2\u0274\u0275\3\2\2\2\u0275v\3\2\2\2"+
		"\u0276\u0278\7\60\2\2\u0277\u0279\5g\64\2\u0278\u0277\3\2\2\2\u0279\u027a"+
		"\3\2\2\2\u027a\u0278\3\2\2\2\u027a\u027b\3\2\2\2\u027bx\3\2\2\2\u027c"+
		"\u027e\t\f\2\2\u027d\u027f\t\r\2\2\u027e\u027d\3\2\2\2\u027e\u027f\3\2"+
		"\2\2\u027f\u0281\3\2\2\2\u0280\u0282\5g\64\2\u0281\u0280\3\2\2\2\u0282"+
		"\u0283\3\2\2\2\u0283\u0281\3\2\2\2\u0283\u0284\3\2\2\2\u0284z\3\2\2\2"+
		"\u0285\u0286\7p\2\2\u0286\u0287\7w\2\2\u0287\u0288\7n\2\2\u0288\u0289"+
		"\7n\2\2\u0289|\3\2\2\2\u028a\u028b\7\60\2\2\u028b~\3\2\2\2\u028c\u028d"+
		"\7\60\2\2\u028d\u028e\7\60\2\2\u028e\u028f\7\60\2\2\u028f\u0080\3\2\2"+
		"\2\u0290\u0291\7,\2\2\u0291\u0082\3\2\2\2\u0292\u0293\7*\2\2\u0293\u0294"+
		"\bB\3\2\u0294\u0084\3\2\2\2\u0295\u0296\7+\2\2\u0296\u0297\bC\4\2\u0297"+
		"\u0086\3\2\2\2\u0298\u0299\7.\2\2\u0299\u0088\3\2\2\2\u029a\u029b\7<\2"+
		"\2\u029b\u008a\3\2\2\2\u029c\u029d\7=\2\2\u029d\u008c\3\2\2\2\u029e\u029f"+
		"\7,\2\2\u029f\u02a0\7,\2\2\u02a0\u008e\3\2\2\2\u02a1\u02a2\7?\2\2\u02a2"+
		"\u0090\3\2\2\2\u02a3\u02a4\7]\2\2\u02a4\u02a5\bI\5\2\u02a5\u0092\3\2\2"+
		"\2\u02a6\u02a7\7_\2\2\u02a7\u02a8\bJ\6\2\u02a8\u0094\3\2\2\2\u02a9\u02aa"+
		"\7~\2\2\u02aa\u0096\3\2\2\2\u02ab\u02ac\7`\2\2\u02ac\u0098\3\2\2\2\u02ad"+
		"\u02ae\7(\2\2\u02ae\u009a\3\2\2\2\u02af\u02b0\7>\2\2\u02b0\u02b1\7>\2"+
		"\2\u02b1\u009c\3\2\2\2\u02b2\u02b3\7@\2\2\u02b3\u02b4\7@\2\2\u02b4\u009e"+
		"\3\2\2\2\u02b5\u02b6\7-\2\2\u02b6\u00a0\3\2\2\2\u02b7\u02b8\7/\2\2\u02b8"+
		"\u00a2\3\2\2\2\u02b9\u02ba\7\61\2\2\u02ba\u00a4\3\2\2\2\u02bb\u02bc\7"+
		"\'\2\2\u02bc\u00a6\3\2\2\2\u02bd\u02be\7\61\2\2\u02be\u02bf\7\61\2\2\u02bf"+
		"\u00a8\3\2\2\2\u02c0\u02c1\7\u0080\2\2\u02c1\u00aa\3\2\2\2\u02c2\u02c3"+
		"\7}\2\2\u02c3\u02c4\bV\7\2\u02c4\u00ac\3\2\2\2\u02c5\u02c6\7\177\2\2\u02c6"+
		"\u02c7\bW\b\2\u02c7\u00ae\3\2\2\2\u02c8\u02c9\7>\2\2\u02c9\u00b0\3\2\2"+
		"\2\u02ca\u02cb\7@\2\2\u02cb\u00b2\3\2\2\2\u02cc\u02cd\7?\2\2\u02cd\u02ce"+
		"\7?\2\2\u02ce\u00b4\3\2\2\2\u02cf\u02d0\7@\2\2\u02d0\u02d1\7?\2\2\u02d1"+
		"\u00b6\3\2\2\2\u02d2\u02d3\7>\2\2\u02d3\u02d4\7?\2\2\u02d4\u00b8\3\2\2"+
		"\2\u02d5\u02d6\7>\2\2\u02d6\u02da\7@\2\2\u02d7\u02d8\7#\2\2\u02d8\u02da"+
		"\7?\2\2\u02d9\u02d5\3\2\2\2\u02d9\u02d7\3\2\2\2\u02da\u00ba\3\2\2\2\u02db"+
		"\u02dc\7B\2\2\u02dc\u00bc\3\2\2\2\u02dd\u02de\7/\2\2\u02de\u02df\7@\2"+
		"\2\u02df\u00be\3\2\2\2\u02e0\u02e1\7-\2\2\u02e1\u02e2\7?\2\2\u02e2\u00c0"+
		"\3\2\2\2\u02e3\u02e4\7/\2\2\u02e4\u02e5\7?\2\2\u02e5\u00c2\3\2\2\2\u02e6"+
		"\u02e7\7,\2\2\u02e7\u02e8\7?\2\2\u02e8\u00c4\3\2\2\2\u02e9\u02ea\7B\2"+
		"\2\u02ea\u02eb\7?\2\2\u02eb\u00c6\3\2\2\2\u02ec\u02ed\7\61\2\2\u02ed\u02ee"+
		"\7?\2\2\u02ee\u00c8\3\2\2\2\u02ef\u02f0\7\'\2\2\u02f0\u02f1\7?\2\2\u02f1"+
		"\u00ca\3\2\2\2\u02f2\u02f3\7(\2\2\u02f3\u02f4\7?\2\2\u02f4\u00cc\3\2\2"+
		"\2\u02f5\u02f6\7~\2\2\u02f6\u02f7\7?\2\2\u02f7\u00ce\3\2\2\2\u02f8\u02f9"+
		"\7`\2\2\u02f9\u02fa\7?\2\2\u02fa\u00d0\3\2\2\2\u02fb\u02fc\7>\2\2\u02fc"+
		"\u02fd\7>\2\2\u02fd\u02fe\7?\2\2\u02fe\u00d2\3\2\2\2\u02ff\u0300\7@\2"+
		"\2\u0300\u0301\7@\2\2\u0301\u0302\7?\2\2\u0302\u00d4\3\2\2\2\u0303\u0304"+
		"\7,\2\2\u0304\u0305\7,\2\2\u0305\u0306\7?\2\2\u0306\u00d6\3\2\2\2\u0307"+
		"\u0308\7\61\2\2\u0308\u0309\7\61\2\2\u0309\u030a\7?\2\2\u030a\u00d8\3"+
		"\2\2\2\u030b\u030f\5\u00dbn\2\u030c\u030e\5\u00ddo\2\u030d\u030c\3\2\2"+
		"\2\u030e\u0311\3\2\2\2\u030f\u030d\3\2\2\2\u030f\u0310\3\2\2\2\u0310\u00da"+
		"\3\2\2\2\u0311\u030f\3\2\2\2\u0312\u0314\t\16\2\2\u0313\u0312\3\2\2\2"+
		"\u0314\u00dc\3\2\2\2\u0315\u0318\5\u00dbn\2\u0316\u0318\t\b\2\2\u0317"+
		"\u0315\3\2\2\2\u0317\u0316\3\2\2\2\u0318\u00de\3\2\2\2\u0319\u031d\5\u00e3"+
		"r\2\u031a\u031d\5\u00e5s\2\u031b\u031d\5\u00e7t\2\u031c\u0319\3\2\2\2"+
		"\u031c\u031a\3\2\2\2\u031c\u031b\3\2\2\2\u031d\u031e\3\2\2\2\u031e\u031f"+
		"\bp\t\2\u031f\u00e0\3\2\2\2\u0320\u0321\13\2\2\2\u0321\u00e2\3\2\2\2\u0322"+
		"\u0324\t\17\2\2\u0323\u0322\3\2\2\2\u0324\u0325\3\2\2\2\u0325\u0323\3"+
		"\2\2\2\u0325\u0326\3\2\2\2\u0326\u00e4\3\2\2\2\u0327\u032b\7%\2\2\u0328"+
		"\u032a\n\20\2\2\u0329\u0328\3\2\2\2\u032a\u032d\3\2\2\2\u032b\u0329\3"+
		"\2\2\2\u032b\u032c\3\2\2\2\u032c\u00e6\3\2\2\2\u032d\u032b\3\2\2\2\u032e"+
		"\u0330\7^\2\2\u032f\u0331\5\u00e3r\2\u0330\u032f\3\2\2\2\u0330\u0331\3"+
		"\2\2\2\u0331\u0337\3\2\2\2\u0332\u0334\7\17\2\2\u0333\u0332\3\2\2\2\u0333"+
		"\u0334\3\2\2\2\u0334\u0335\3\2\2\2\u0335\u0338\7\f\2\2\u0336\u0338\7\17"+
		"\2\2\u0337\u0333\3\2\2\2\u0337\u0336\3\2\2\2\u0338\u00e8\3\2\2\2$\2\u020d"+
		"\u0211\u0214\u0216\u021d\u021f\u0226\u0228\u022c\u0235\u023b\u023d\u0244"+
		"\u024b\u0252\u0260\u0263\u0269\u026d\u0274\u027a\u027e\u0283\u02d9\u030f"+
		"\u0313\u0317\u031c\u0325\u032b\u0330\u0333\u0337\n\3,\2\3B\3\3C\4\3I\5"+
		"\3J\6\3V\7\3W\b\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
// Generated from /home/gcampagn/secureiot/ravellang/Ravel.g4 by ANTLR 4.6
package org.stanford.antlr4;

import org.stanford.ravel.compiler.scope.*;
import org.stanford.ravel.compiler.symbol.*;

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
		INTERFACE=17, DEF=18, EVENT=19, COMMAND=20, T_BYTE_FIELD=21, T_STRING_FIELD=22, 
		T_BOOLEAN_FIELD=23, T_INTEGER_FIELD=24, T_NUMBER_FIELD=25, T_DATE_FIELD=26, 
		T_DATE_TIME_FIELD=27, T_TIME_STAMP_FIELD=28, T_CONTEXT_FIELD=29, T_MODEL_FIELD=30, 
		ASSERT=31, RETURN=32, TRUE=33, FALSE=34, IF=35, ELIF=36, ELSE=37, FOR=38, 
		WHILE=39, AND=40, NOT=41, OR=42, IN=43, IS=44, DELETE=45, PASS=46, CONTINUE=47, 
		BREAK=48, NONE=49, NEWLINE=50, STRING_LITERAL=51, DECIMAL_INTEGER=52, 
		OCT_INTEGER=53, HEX_INTEGER=54, BIN_INTEGER=55, FLOAT_NUMBER=56, NullLiteral=57, 
		DOT=58, ELLIPSIS=59, STAR=60, OPEN_PAREN=61, CLOSE_PAREN=62, COMMA=63, 
		COLON=64, SEMI_COLON=65, POWER=66, ASSIGN=67, OPEN_BRACK=68, CLOSE_BRACK=69, 
		OR_OP=70, XOR=71, AND_OP=72, LEFT_SHIFT=73, RIGHT_SHIFT=74, ADD=75, MINUS=76, 
		DIV=77, MOD=78, IDIV=79, NOT_OP=80, OPEN_BRACE=81, CLOSE_BRACE=82, LT=83, 
		GT=84, EQUAL=85, GE=86, LE=87, NOTEQUAL=88, AT=89, ARROW=90, ADD_ASSIGN=91, 
		SUB_ASSIGN=92, MULT_ASSIGN=93, AT_ASSIGN=94, DIV_ASSIGN=95, MOD_ASSIGN=96, 
		AND_ASSIGN=97, OR_ASSIGN=98, XOR_ASSIGN=99, LEFT_SHIFT_ASSIGN=100, RIGHT_SHIFT_ASSIGN=101, 
		POWER_ASSIGN=102, IDIV_ASSIGN=103, Identifier=104, SKIP_=105, UNKNOWN_CHAR=106;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "MODEL", "SPACE", "CONTROLLER", "VIEW", "FLOW", "INTERFACE", 
		"DEF", "EVENT", "COMMAND", "T_BYTE_FIELD", "T_STRING_FIELD", "T_BOOLEAN_FIELD", 
		"T_INTEGER_FIELD", "T_NUMBER_FIELD", "T_DATE_FIELD", "T_DATE_TIME_FIELD", 
		"T_TIME_STAMP_FIELD", "T_CONTEXT_FIELD", "T_MODEL_FIELD", "ASSERT", "RETURN", 
		"TRUE", "FALSE", "IF", "ELIF", "ELSE", "FOR", "WHILE", "AND", "NOT", "OR", 
		"IN", "IS", "DELETE", "PASS", "CONTINUE", "BREAK", "NONE", "NEWLINE", 
		"STRING_LITERAL", "STRING_ESCAPE_SEQ", "DECIMAL_INTEGER", "OCT_INTEGER", 
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
		"'configuration:'", "'local'", "'streaming'", "'replicated'", "'properties:'", 
		"'schema:'", "'model'", "'space'", "'controller'", "'view'", "'flow'", 
		"'interface'", "'def'", "'event'", "'command'", "'ByteField'", "'StringField'", 
		"'BooleanField'", "'IntegerField'", "'NumberField'", "'DateField'", "'DateTimeField'", 
		"'TimeStampField'", "'ContextField'", "'ModelField'", "'assert'", "'return'", 
		"'True'", "'False'", "'if'", "'else if'", "'else'", "'for'", "'while'", 
		"'and'", "'not'", "'or'", "'in'", "'is'", "'delete'", "'pass'", "'continue'", 
		"'break'", "'none'", null, null, null, null, null, null, null, "'null'", 
		"'.'", "'...'", "'*'", "'('", "')'", "','", "':'", "';'", "'**'", "'='", 
		"'['", "']'", "'|'", "'^'", "'&'", "'<<'", "'>>'", "'+'", "'-'", "'/'", 
		"'%'", "'//'", "'~'", "'{'", "'}'", "'<'", "'>'", "'=='", "'>='", "'<='", 
		null, "'@'", "'->'", "'+='", "'-='", "'*='", "'@='", "'/='", "'%='", "'&='", 
		"'|='", "'^='", "'<<='", "'>>='", "'**='", "'//='"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		"MODEL", "SPACE", "CONTROLLER", "VIEW", "FLOW", "INTERFACE", "DEF", "EVENT", 
		"COMMAND", "T_BYTE_FIELD", "T_STRING_FIELD", "T_BOOLEAN_FIELD", "T_INTEGER_FIELD", 
		"T_NUMBER_FIELD", "T_DATE_FIELD", "T_DATE_TIME_FIELD", "T_TIME_STAMP_FIELD", 
		"T_CONTEXT_FIELD", "T_MODEL_FIELD", "ASSERT", "RETURN", "TRUE", "FALSE", 
		"IF", "ELIF", "ELSE", "FOR", "WHILE", "AND", "NOT", "OR", "IN", "IS", 
		"DELETE", "PASS", "CONTINUE", "BREAK", "NONE", "NEWLINE", "STRING_LITERAL", 
		"DECIMAL_INTEGER", "OCT_INTEGER", "HEX_INTEGER", "BIN_INTEGER", "FLOAT_NUMBER", 
		"NullLiteral", "DOT", "ELLIPSIS", "STAR", "OPEN_PAREN", "CLOSE_PAREN", 
		"COMMA", "COLON", "SEMI_COLON", "POWER", "ASSIGN", "OPEN_BRACK", "CLOSE_BRACK", 
		"OR_OP", "XOR", "AND_OP", "LEFT_SHIFT", "RIGHT_SHIFT", "ADD", "MINUS", 
		"DIV", "MOD", "IDIV", "NOT_OP", "OPEN_BRACE", "CLOSE_BRACE", "LT", "GT", 
		"EQUAL", "GE", "LE", "NOTEQUAL", "AT", "ARROW", "ADD_ASSIGN", "SUB_ASSIGN", 
		"MULT_ASSIGN", "AT_ASSIGN", "DIV_ASSIGN", "MOD_ASSIGN", "AND_ASSIGN", 
		"OR_ASSIGN", "XOR_ASSIGN", "LEFT_SHIFT_ASSIGN", "RIGHT_SHIFT_ASSIGN", 
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
		case 49:
			NEWLINE_action((RuleContext)_localctx, actionIndex);
			break;
		case 71:
			OPEN_PAREN_action((RuleContext)_localctx, actionIndex);
			break;
		case 72:
			CLOSE_PAREN_action((RuleContext)_localctx, actionIndex);
			break;
		case 78:
			OPEN_BRACK_action((RuleContext)_localctx, actionIndex);
			break;
		case 79:
			CLOSE_BRACK_action((RuleContext)_localctx, actionIndex);
			break;
		case 91:
			OPEN_BRACE_action((RuleContext)_localctx, actionIndex);
			break;
		case 92:
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
		case 49:
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2l\u03bb\b\1\4\2\t"+
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
		"w\tw\4x\tx\4y\ty\4z\tz\4{\t{\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3"+
		"\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27"+
		"\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30"+
		"\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31"+
		"\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32"+
		"\3\32\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35"+
		"\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\36\3\36"+
		"\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3 \3!\3!\3"+
		"!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3#\3$\3$\3$\3%\3%\3%"+
		"\3%\3%\3%\3%\3%\3&\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3(\3(\3)\3"+
		")\3)\3)\3*\3*\3*\3*\3+\3+\3+\3,\3,\3,\3-\3-\3-\3.\3.\3.\3.\3.\3.\3.\3"+
		"/\3/\3/\3/\3/\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\61\3\61\3"+
		"\61\3\61\3\61\3\61\3\62\3\62\3\62\3\62\3\62\3\63\3\63\3\63\5\63\u0290"+
		"\n\63\3\63\3\63\5\63\u0294\n\63\3\63\5\63\u0297\n\63\5\63\u0299\n\63\3"+
		"\63\3\63\3\64\3\64\3\64\7\64\u02a0\n\64\f\64\16\64\u02a3\13\64\3\64\3"+
		"\64\3\64\3\64\7\64\u02a9\n\64\f\64\16\64\u02ac\13\64\3\64\5\64\u02af\n"+
		"\64\3\65\3\65\3\65\3\66\3\66\7\66\u02b6\n\66\f\66\16\66\u02b9\13\66\3"+
		"\66\6\66\u02bc\n\66\r\66\16\66\u02bd\5\66\u02c0\n\66\3\67\3\67\3\67\6"+
		"\67\u02c5\n\67\r\67\16\67\u02c6\38\38\38\68\u02cc\n8\r8\168\u02cd\39\3"+
		"9\39\69\u02d3\n9\r9\169\u02d4\3:\3:\3;\3;\3<\3<\3=\3=\3>\3>\3?\3?\5?\u02e3"+
		"\n?\3@\5@\u02e6\n@\3@\3@\3@\3@\5@\u02ec\n@\3A\3A\5A\u02f0\nA\3A\3A\3B"+
		"\6B\u02f5\nB\rB\16B\u02f6\3C\3C\6C\u02fb\nC\rC\16C\u02fc\3D\3D\5D\u0301"+
		"\nD\3D\6D\u0304\nD\rD\16D\u0305\3E\3E\3E\3E\3E\3F\3F\3G\3G\3G\3G\3H\3"+
		"H\3I\3I\3I\3J\3J\3J\3K\3K\3L\3L\3M\3M\3N\3N\3N\3O\3O\3P\3P\3P\3Q\3Q\3"+
		"Q\3R\3R\3S\3S\3T\3T\3U\3U\3U\3V\3V\3V\3W\3W\3X\3X\3Y\3Y\3Z\3Z\3[\3[\3"+
		"[\3\\\3\\\3]\3]\3]\3^\3^\3^\3_\3_\3`\3`\3a\3a\3a\3b\3b\3b\3c\3c\3c\3d"+
		"\3d\3d\3d\5d\u035c\nd\3e\3e\3f\3f\3f\3g\3g\3g\3h\3h\3h\3i\3i\3i\3j\3j"+
		"\3j\3k\3k\3k\3l\3l\3l\3m\3m\3m\3n\3n\3n\3o\3o\3o\3p\3p\3p\3p\3q\3q\3q"+
		"\3q\3r\3r\3r\3r\3s\3s\3s\3s\3t\3t\7t\u0390\nt\ft\16t\u0393\13t\3u\5u\u0396"+
		"\nu\3v\3v\5v\u039a\nv\3w\3w\3w\5w\u039f\nw\3w\3w\3x\3x\3y\6y\u03a6\ny"+
		"\ry\16y\u03a7\3z\3z\7z\u03ac\nz\fz\16z\u03af\13z\3{\3{\5{\u03b3\n{\3{"+
		"\5{\u03b6\n{\3{\3{\5{\u03ba\n{\2\2|\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n"+
		"\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30"+
		"/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.["+
		"/]\60_\61a\62c\63e\64g\65i\2k\66m\67o8q9s\2u\2w\2y\2{\2}:\177\2\u0081"+
		"\2\u0083\2\u0085\2\u0087\2\u0089;\u008b<\u008d=\u008f>\u0091?\u0093@\u0095"+
		"A\u0097B\u0099C\u009bD\u009dE\u009fF\u00a1G\u00a3H\u00a5I\u00a7J\u00a9"+
		"K\u00abL\u00adM\u00afN\u00b1O\u00b3P\u00b5Q\u00b7R\u00b9S\u00bbT\u00bd"+
		"U\u00bfV\u00c1W\u00c3X\u00c5Y\u00c7Z\u00c9[\u00cb\\\u00cd]\u00cf^\u00d1"+
		"_\u00d3`\u00d5a\u00d7b\u00d9c\u00dbd\u00dde\u00dff\u00e1g\u00e3h\u00e5"+
		"i\u00e7j\u00e9\2\u00eb\2\u00edk\u00efl\u00f1\2\u00f3\2\u00f5\2\3\2\21"+
		"\6\2\f\f\17\17))^^\6\2\f\f\17\17$$^^\4\2QQqq\4\2ZZzz\4\2DDdd\3\2\63;\3"+
		"\2\62;\3\2\629\5\2\62;CHch\3\2\62\63\4\2GGgg\4\2--//\5\2C\\aac|\4\2\13"+
		"\13\"\"\4\2\f\f\17\17\u03cb\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3"+
		"\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2"+
		"\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37"+
		"\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3"+
		"\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2"+
		"\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C"+
		"\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2"+
		"\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2"+
		"\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2k"+
		"\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2}\3\2\2\2\2\u0089\3\2\2\2\2"+
		"\u008b\3\2\2\2\2\u008d\3\2\2\2\2\u008f\3\2\2\2\2\u0091\3\2\2\2\2\u0093"+
		"\3\2\2\2\2\u0095\3\2\2\2\2\u0097\3\2\2\2\2\u0099\3\2\2\2\2\u009b\3\2\2"+
		"\2\2\u009d\3\2\2\2\2\u009f\3\2\2\2\2\u00a1\3\2\2\2\2\u00a3\3\2\2\2\2\u00a5"+
		"\3\2\2\2\2\u00a7\3\2\2\2\2\u00a9\3\2\2\2\2\u00ab\3\2\2\2\2\u00ad\3\2\2"+
		"\2\2\u00af\3\2\2\2\2\u00b1\3\2\2\2\2\u00b3\3\2\2\2\2\u00b5\3\2\2\2\2\u00b7"+
		"\3\2\2\2\2\u00b9\3\2\2\2\2\u00bb\3\2\2\2\2\u00bd\3\2\2\2\2\u00bf\3\2\2"+
		"\2\2\u00c1\3\2\2\2\2\u00c3\3\2\2\2\2\u00c5\3\2\2\2\2\u00c7\3\2\2\2\2\u00c9"+
		"\3\2\2\2\2\u00cb\3\2\2\2\2\u00cd\3\2\2\2\2\u00cf\3\2\2\2\2\u00d1\3\2\2"+
		"\2\2\u00d3\3\2\2\2\2\u00d5\3\2\2\2\2\u00d7\3\2\2\2\2\u00d9\3\2\2\2\2\u00db"+
		"\3\2\2\2\2\u00dd\3\2\2\2\2\u00df\3\2\2\2\2\u00e1\3\2\2\2\2\u00e3\3\2\2"+
		"\2\2\u00e5\3\2\2\2\2\u00e7\3\2\2\2\2\u00ed\3\2\2\2\2\u00ef\3\2\2\2\3\u00f7"+
		"\3\2\2\2\5\u0101\3\2\2\2\7\u0109\3\2\2\2\t\u0116\3\2\2\2\13\u0122\3\2"+
		"\2\2\r\u0132\3\2\2\2\17\u0141\3\2\2\2\21\u0147\3\2\2\2\23\u0151\3\2\2"+
		"\2\25\u015c\3\2\2\2\27\u0168\3\2\2\2\31\u0170\3\2\2\2\33\u0176\3\2\2\2"+
		"\35\u017c\3\2\2\2\37\u0187\3\2\2\2!\u018c\3\2\2\2#\u0191\3\2\2\2%\u019b"+
		"\3\2\2\2\'\u019f\3\2\2\2)\u01a5\3\2\2\2+\u01ad\3\2\2\2-\u01b7\3\2\2\2"+
		"/\u01c3\3\2\2\2\61\u01d0\3\2\2\2\63\u01dd\3\2\2\2\65\u01e9\3\2\2\2\67"+
		"\u01f3\3\2\2\29\u0201\3\2\2\2;\u0210\3\2\2\2=\u021d\3\2\2\2?\u0228\3\2"+
		"\2\2A\u022f\3\2\2\2C\u0236\3\2\2\2E\u023b\3\2\2\2G\u0241\3\2\2\2I\u0244"+
		"\3\2\2\2K\u024c\3\2\2\2M\u0251\3\2\2\2O\u0255\3\2\2\2Q\u025b\3\2\2\2S"+
		"\u025f\3\2\2\2U\u0263\3\2\2\2W\u0266\3\2\2\2Y\u0269\3\2\2\2[\u026c\3\2"+
		"\2\2]\u0273\3\2\2\2_\u0278\3\2\2\2a\u0281\3\2\2\2c\u0287\3\2\2\2e\u0298"+
		"\3\2\2\2g\u02ae\3\2\2\2i\u02b0\3\2\2\2k\u02bf\3\2\2\2m\u02c1\3\2\2\2o"+
		"\u02c8\3\2\2\2q\u02cf\3\2\2\2s\u02d6\3\2\2\2u\u02d8\3\2\2\2w\u02da\3\2"+
		"\2\2y\u02dc\3\2\2\2{\u02de\3\2\2\2}\u02e2\3\2\2\2\177\u02eb\3\2\2\2\u0081"+
		"\u02ef\3\2\2\2\u0083\u02f4\3\2\2\2\u0085\u02f8\3\2\2\2\u0087\u02fe\3\2"+
		"\2\2\u0089\u0307\3\2\2\2\u008b\u030c\3\2\2\2\u008d\u030e\3\2\2\2\u008f"+
		"\u0312\3\2\2\2\u0091\u0314\3\2\2\2\u0093\u0317\3\2\2\2\u0095\u031a\3\2"+
		"\2\2\u0097\u031c\3\2\2\2\u0099\u031e\3\2\2\2\u009b\u0320\3\2\2\2\u009d"+
		"\u0323\3\2\2\2\u009f\u0325\3\2\2\2\u00a1\u0328\3\2\2\2\u00a3\u032b\3\2"+
		"\2\2\u00a5\u032d\3\2\2\2\u00a7\u032f\3\2\2\2\u00a9\u0331\3\2\2\2\u00ab"+
		"\u0334\3\2\2\2\u00ad\u0337\3\2\2\2\u00af\u0339\3\2\2\2\u00b1\u033b\3\2"+
		"\2\2\u00b3\u033d\3\2\2\2\u00b5\u033f\3\2\2\2\u00b7\u0342\3\2\2\2\u00b9"+
		"\u0344\3\2\2\2\u00bb\u0347\3\2\2\2\u00bd\u034a\3\2\2\2\u00bf\u034c\3\2"+
		"\2\2\u00c1\u034e\3\2\2\2\u00c3\u0351\3\2\2\2\u00c5\u0354\3\2\2\2\u00c7"+
		"\u035b\3\2\2\2\u00c9\u035d\3\2\2\2\u00cb\u035f\3\2\2\2\u00cd\u0362\3\2"+
		"\2\2\u00cf\u0365\3\2\2\2\u00d1\u0368\3\2\2\2\u00d3\u036b\3\2\2\2\u00d5"+
		"\u036e\3\2\2\2\u00d7\u0371\3\2\2\2\u00d9\u0374\3\2\2\2\u00db\u0377\3\2"+
		"\2\2\u00dd\u037a\3\2\2\2\u00df\u037d\3\2\2\2\u00e1\u0381\3\2\2\2\u00e3"+
		"\u0385\3\2\2\2\u00e5\u0389\3\2\2\2\u00e7\u038d\3\2\2\2\u00e9\u0395\3\2"+
		"\2\2\u00eb\u0399\3\2\2\2\u00ed\u039e\3\2\2\2\u00ef\u03a2\3\2\2\2\u00f1"+
		"\u03a5\3\2\2\2\u00f3\u03a9\3\2\2\2\u00f5\u03b0\3\2\2\2\u00f7\u00f8\7r"+
		"\2\2\u00f8\u00f9\7n\2\2\u00f9\u00fa\7c\2\2\u00fa\u00fb\7v\2\2\u00fb\u00fc"+
		"\7h\2\2\u00fc\u00fd\7q\2\2\u00fd\u00fe\7t\2\2\u00fe\u00ff\7o\2\2\u00ff"+
		"\u0100\7<\2\2\u0100\4\3\2\2\2\u0101\u0102\7o\2\2\u0102\u0103\7q\2\2\u0103"+
		"\u0104\7f\2\2\u0104\u0105\7g\2\2\u0105\u0106\7n\2\2\u0106\u0107\7u\2\2"+
		"\u0107\u0108\7<\2\2\u0108\6\3\2\2\2\u0109\u010a\7e\2\2\u010a\u010b\7q"+
		"\2\2\u010b\u010c\7p\2\2\u010c\u010d\7v\2\2\u010d\u010e\7t\2\2\u010e\u010f"+
		"\7q\2\2\u010f\u0110\7n\2\2\u0110\u0111\7n\2\2\u0111\u0112\7g\2\2\u0112"+
		"\u0113\7t\2\2\u0113\u0114\7u\2\2\u0114\u0115\7<\2\2\u0115\b\3\2\2\2\u0116"+
		"\u0117\7k\2\2\u0117\u0118\7p\2\2\u0118\u0119\7v\2\2\u0119\u011a\7g\2\2"+
		"\u011a\u011b\7t\2\2\u011b\u011c\7h\2\2\u011c\u011d\7c\2\2\u011d\u011e"+
		"\7e\2\2\u011e\u011f\7g\2\2\u011f\u0120\7u\2\2\u0120\u0121\7<\2\2\u0121"+
		"\n\3\2\2\2\u0122\u0123\7k\2\2\u0123\u0124\7o\2\2\u0124\u0125\7r\2\2\u0125"+
		"\u0126\7n\2\2\u0126\u0127\7g\2\2\u0127\u0128\7o\2\2\u0128\u0129\7g\2\2"+
		"\u0129\u012a\7p\2\2\u012a\u012b\7v\2\2\u012b\u012c\7c\2\2\u012c\u012d"+
		"\7v\2\2\u012d\u012e\7k\2\2\u012e\u012f\7q\2\2\u012f\u0130\7p\2\2\u0130"+
		"\u0131\7<\2\2\u0131\f\3\2\2\2\u0132\u0133\7e\2\2\u0133\u0134\7q\2\2\u0134"+
		"\u0135\7p\2\2\u0135\u0136\7h\2\2\u0136\u0137\7k\2\2\u0137\u0138\7i\2\2"+
		"\u0138\u0139\7w\2\2\u0139\u013a\7t\2\2\u013a\u013b\7c\2\2\u013b\u013c"+
		"\7v\2\2\u013c\u013d\7k\2\2\u013d\u013e\7q\2\2\u013e\u013f\7p\2\2\u013f"+
		"\u0140\7<\2\2\u0140\16\3\2\2\2\u0141\u0142\7n\2\2\u0142\u0143\7q\2\2\u0143"+
		"\u0144\7e\2\2\u0144\u0145\7c\2\2\u0145\u0146\7n\2\2\u0146\20\3\2\2\2\u0147"+
		"\u0148\7u\2\2\u0148\u0149\7v\2\2\u0149\u014a\7t\2\2\u014a\u014b\7g\2\2"+
		"\u014b\u014c\7c\2\2\u014c\u014d\7o\2\2\u014d\u014e\7k\2\2\u014e\u014f"+
		"\7p\2\2\u014f\u0150\7i\2\2\u0150\22\3\2\2\2\u0151\u0152\7t\2\2\u0152\u0153"+
		"\7g\2\2\u0153\u0154\7r\2\2\u0154\u0155\7n\2\2\u0155\u0156\7k\2\2\u0156"+
		"\u0157\7e\2\2\u0157\u0158\7c\2\2\u0158\u0159\7v\2\2\u0159\u015a\7g\2\2"+
		"\u015a\u015b\7f\2\2\u015b\24\3\2\2\2\u015c\u015d\7r\2\2\u015d\u015e\7"+
		"t\2\2\u015e\u015f\7q\2\2\u015f\u0160\7r\2\2\u0160\u0161\7g\2\2\u0161\u0162"+
		"\7t\2\2\u0162\u0163\7v\2\2\u0163\u0164\7k\2\2\u0164\u0165\7g\2\2\u0165"+
		"\u0166\7u\2\2\u0166\u0167\7<\2\2\u0167\26\3\2\2\2\u0168\u0169\7u\2\2\u0169"+
		"\u016a\7e\2\2\u016a\u016b\7j\2\2\u016b\u016c\7g\2\2\u016c\u016d\7o\2\2"+
		"\u016d\u016e\7c\2\2\u016e\u016f\7<\2\2\u016f\30\3\2\2\2\u0170\u0171\7"+
		"o\2\2\u0171\u0172\7q\2\2\u0172\u0173\7f\2\2\u0173\u0174\7g\2\2\u0174\u0175"+
		"\7n\2\2\u0175\32\3\2\2\2\u0176\u0177\7u\2\2\u0177\u0178\7r\2\2\u0178\u0179"+
		"\7c\2\2\u0179\u017a\7e\2\2\u017a\u017b\7g\2\2\u017b\34\3\2\2\2\u017c\u017d"+
		"\7e\2\2\u017d\u017e\7q\2\2\u017e\u017f\7p\2\2\u017f\u0180\7v\2\2\u0180"+
		"\u0181\7t\2\2\u0181\u0182\7q\2\2\u0182\u0183\7n\2\2\u0183\u0184\7n\2\2"+
		"\u0184\u0185\7g\2\2\u0185\u0186\7t\2\2\u0186\36\3\2\2\2\u0187\u0188\7"+
		"x\2\2\u0188\u0189\7k\2\2\u0189\u018a\7g\2\2\u018a\u018b\7y\2\2\u018b "+
		"\3\2\2\2\u018c\u018d\7h\2\2\u018d\u018e\7n\2\2\u018e\u018f\7q\2\2\u018f"+
		"\u0190\7y\2\2\u0190\"\3\2\2\2\u0191\u0192\7k\2\2\u0192\u0193\7p\2\2\u0193"+
		"\u0194\7v\2\2\u0194\u0195\7g\2\2\u0195\u0196\7t\2\2\u0196\u0197\7h\2\2"+
		"\u0197\u0198\7c\2\2\u0198\u0199\7e\2\2\u0199\u019a\7g\2\2\u019a$\3\2\2"+
		"\2\u019b\u019c\7f\2\2\u019c\u019d\7g\2\2\u019d\u019e\7h\2\2\u019e&\3\2"+
		"\2\2\u019f\u01a0\7g\2\2\u01a0\u01a1\7x\2\2\u01a1\u01a2\7g\2\2\u01a2\u01a3"+
		"\7p\2\2\u01a3\u01a4\7v\2\2\u01a4(\3\2\2\2\u01a5\u01a6\7e\2\2\u01a6\u01a7"+
		"\7q\2\2\u01a7\u01a8\7o\2\2\u01a8\u01a9\7o\2\2\u01a9\u01aa\7c\2\2\u01aa"+
		"\u01ab\7p\2\2\u01ab\u01ac\7f\2\2\u01ac*\3\2\2\2\u01ad\u01ae\7D\2\2\u01ae"+
		"\u01af\7{\2\2\u01af\u01b0\7v\2\2\u01b0\u01b1\7g\2\2\u01b1\u01b2\7H\2\2"+
		"\u01b2\u01b3\7k\2\2\u01b3\u01b4\7g\2\2\u01b4\u01b5\7n\2\2\u01b5\u01b6"+
		"\7f\2\2\u01b6,\3\2\2\2\u01b7\u01b8\7U\2\2\u01b8\u01b9\7v\2\2\u01b9\u01ba"+
		"\7t\2\2\u01ba\u01bb\7k\2\2\u01bb\u01bc\7p\2\2\u01bc\u01bd\7i\2\2\u01bd"+
		"\u01be\7H\2\2\u01be\u01bf\7k\2\2\u01bf\u01c0\7g\2\2\u01c0\u01c1\7n\2\2"+
		"\u01c1\u01c2\7f\2\2\u01c2.\3\2\2\2\u01c3\u01c4\7D\2\2\u01c4\u01c5\7q\2"+
		"\2\u01c5\u01c6\7q\2\2\u01c6\u01c7\7n\2\2\u01c7\u01c8\7g\2\2\u01c8\u01c9"+
		"\7c\2\2\u01c9\u01ca\7p\2\2\u01ca\u01cb\7H\2\2\u01cb\u01cc\7k\2\2\u01cc"+
		"\u01cd\7g\2\2\u01cd\u01ce\7n\2\2\u01ce\u01cf\7f\2\2\u01cf\60\3\2\2\2\u01d0"+
		"\u01d1\7K\2\2\u01d1\u01d2\7p\2\2\u01d2\u01d3\7v\2\2\u01d3\u01d4\7g\2\2"+
		"\u01d4\u01d5\7i\2\2\u01d5\u01d6\7g\2\2\u01d6\u01d7\7t\2\2\u01d7\u01d8"+
		"\7H\2\2\u01d8\u01d9\7k\2\2\u01d9\u01da\7g\2\2\u01da\u01db\7n\2\2\u01db"+
		"\u01dc\7f\2\2\u01dc\62\3\2\2\2\u01dd\u01de\7P\2\2\u01de\u01df\7w\2\2\u01df"+
		"\u01e0\7o\2\2\u01e0\u01e1\7d\2\2\u01e1\u01e2\7g\2\2\u01e2\u01e3\7t\2\2"+
		"\u01e3\u01e4\7H\2\2\u01e4\u01e5\7k\2\2\u01e5\u01e6\7g\2\2\u01e6\u01e7"+
		"\7n\2\2\u01e7\u01e8\7f\2\2\u01e8\64\3\2\2\2\u01e9\u01ea\7F\2\2\u01ea\u01eb"+
		"\7c\2\2\u01eb\u01ec\7v\2\2\u01ec\u01ed\7g\2\2\u01ed\u01ee\7H\2\2\u01ee"+
		"\u01ef\7k\2\2\u01ef\u01f0\7g\2\2\u01f0\u01f1\7n\2\2\u01f1\u01f2\7f\2\2"+
		"\u01f2\66\3\2\2\2\u01f3\u01f4\7F\2\2\u01f4\u01f5\7c\2\2\u01f5\u01f6\7"+
		"v\2\2\u01f6\u01f7\7g\2\2\u01f7\u01f8\7V\2\2\u01f8\u01f9\7k\2\2\u01f9\u01fa"+
		"\7o\2\2\u01fa\u01fb\7g\2\2\u01fb\u01fc\7H\2\2\u01fc\u01fd\7k\2\2\u01fd"+
		"\u01fe\7g\2\2\u01fe\u01ff\7n\2\2\u01ff\u0200\7f\2\2\u02008\3\2\2\2\u0201"+
		"\u0202\7V\2\2\u0202\u0203\7k\2\2\u0203\u0204\7o\2\2\u0204\u0205\7g\2\2"+
		"\u0205\u0206\7U\2\2\u0206\u0207\7v\2\2\u0207\u0208\7c\2\2\u0208\u0209"+
		"\7o\2\2\u0209\u020a\7r\2\2\u020a\u020b\7H\2\2\u020b\u020c\7k\2\2\u020c"+
		"\u020d\7g\2\2\u020d\u020e\7n\2\2\u020e\u020f\7f\2\2\u020f:\3\2\2\2\u0210"+
		"\u0211\7E\2\2\u0211\u0212\7q\2\2\u0212\u0213\7p\2\2\u0213\u0214\7v\2\2"+
		"\u0214\u0215\7g\2\2\u0215\u0216\7z\2\2\u0216\u0217\7v\2\2\u0217\u0218"+
		"\7H\2\2\u0218\u0219\7k\2\2\u0219\u021a\7g\2\2\u021a\u021b\7n\2\2\u021b"+
		"\u021c\7f\2\2\u021c<\3\2\2\2\u021d\u021e\7O\2\2\u021e\u021f\7q\2\2\u021f"+
		"\u0220\7f\2\2\u0220\u0221\7g\2\2\u0221\u0222\7n\2\2\u0222\u0223\7H\2\2"+
		"\u0223\u0224\7k\2\2\u0224\u0225\7g\2\2\u0225\u0226\7n\2\2\u0226\u0227"+
		"\7f\2\2\u0227>\3\2\2\2\u0228\u0229\7c\2\2\u0229\u022a\7u\2\2\u022a\u022b"+
		"\7u\2\2\u022b\u022c\7g\2\2\u022c\u022d\7t\2\2\u022d\u022e\7v\2\2\u022e"+
		"@\3\2\2\2\u022f\u0230\7t\2\2\u0230\u0231\7g\2\2\u0231\u0232\7v\2\2\u0232"+
		"\u0233\7w\2\2\u0233\u0234\7t\2\2\u0234\u0235\7p\2\2\u0235B\3\2\2\2\u0236"+
		"\u0237\7V\2\2\u0237\u0238\7t\2\2\u0238\u0239\7w\2\2\u0239\u023a\7g\2\2"+
		"\u023aD\3\2\2\2\u023b\u023c\7H\2\2\u023c\u023d\7c\2\2\u023d\u023e\7n\2"+
		"\2\u023e\u023f\7u\2\2\u023f\u0240\7g\2\2\u0240F\3\2\2\2\u0241\u0242\7"+
		"k\2\2\u0242\u0243\7h\2\2\u0243H\3\2\2\2\u0244\u0245\7g\2\2\u0245\u0246"+
		"\7n\2\2\u0246\u0247\7u\2\2\u0247\u0248\7g\2\2\u0248\u0249\7\"\2\2\u0249"+
		"\u024a\7k\2\2\u024a\u024b\7h\2\2\u024bJ\3\2\2\2\u024c\u024d\7g\2\2\u024d"+
		"\u024e\7n\2\2\u024e\u024f\7u\2\2\u024f\u0250\7g\2\2\u0250L\3\2\2\2\u0251"+
		"\u0252\7h\2\2\u0252\u0253\7q\2\2\u0253\u0254\7t\2\2\u0254N\3\2\2\2\u0255"+
		"\u0256\7y\2\2\u0256\u0257\7j\2\2\u0257\u0258\7k\2\2\u0258\u0259\7n\2\2"+
		"\u0259\u025a\7g\2\2\u025aP\3\2\2\2\u025b\u025c\7c\2\2\u025c\u025d\7p\2"+
		"\2\u025d\u025e\7f\2\2\u025eR\3\2\2\2\u025f\u0260\7p\2\2\u0260\u0261\7"+
		"q\2\2\u0261\u0262\7v\2\2\u0262T\3\2\2\2\u0263\u0264\7q\2\2\u0264\u0265"+
		"\7t\2\2\u0265V\3\2\2\2\u0266\u0267\7k\2\2\u0267\u0268\7p\2\2\u0268X\3"+
		"\2\2\2\u0269\u026a\7k\2\2\u026a\u026b\7u\2\2\u026bZ\3\2\2\2\u026c\u026d"+
		"\7f\2\2\u026d\u026e\7g\2\2\u026e\u026f\7n\2\2\u026f\u0270\7g\2\2\u0270"+
		"\u0271\7v\2\2\u0271\u0272\7g\2\2\u0272\\\3\2\2\2\u0273\u0274\7r\2\2\u0274"+
		"\u0275\7c\2\2\u0275\u0276\7u\2\2\u0276\u0277\7u\2\2\u0277^\3\2\2\2\u0278"+
		"\u0279\7e\2\2\u0279\u027a\7q\2\2\u027a\u027b\7p\2\2\u027b\u027c\7v\2\2"+
		"\u027c\u027d\7k\2\2\u027d\u027e\7p\2\2\u027e\u027f\7w\2\2\u027f\u0280"+
		"\7g\2\2\u0280`\3\2\2\2\u0281\u0282\7d\2\2\u0282\u0283\7t\2\2\u0283\u0284"+
		"\7g\2\2\u0284\u0285\7c\2\2\u0285\u0286\7m\2\2\u0286b\3\2\2\2\u0287\u0288"+
		"\7p\2\2\u0288\u0289\7q\2\2\u0289\u028a\7p\2\2\u028a\u028b\7g\2\2\u028b"+
		"d\3\2\2\2\u028c\u028d\6\63\2\2\u028d\u0299\5\u00f1y\2\u028e\u0290\7\17"+
		"\2\2\u028f\u028e\3\2\2\2\u028f\u0290\3\2\2\2\u0290\u0291\3\2\2\2\u0291"+
		"\u0294\7\f\2\2\u0292\u0294\7\17\2\2\u0293\u028f\3\2\2\2\u0293\u0292\3"+
		"\2\2\2\u0294\u0296\3\2\2\2\u0295\u0297\5\u00f1y\2\u0296\u0295\3\2\2\2"+
		"\u0296\u0297\3\2\2\2\u0297\u0299\3\2\2\2\u0298\u028c\3\2\2\2\u0298\u0293"+
		"\3\2\2\2\u0299\u029a\3\2\2\2\u029a\u029b\b\63\2\2\u029bf\3\2\2\2\u029c"+
		"\u02a1\7)\2\2\u029d\u02a0\5i\65\2\u029e\u02a0\n\2\2\2\u029f\u029d\3\2"+
		"\2\2\u029f\u029e\3\2\2\2\u02a0\u02a3\3\2\2\2\u02a1\u029f\3\2\2\2\u02a1"+
		"\u02a2\3\2\2\2\u02a2\u02a4\3\2\2\2\u02a3\u02a1\3\2\2\2\u02a4\u02af\7)"+
		"\2\2\u02a5\u02aa\7$\2\2\u02a6\u02a9\5i\65\2\u02a7\u02a9\n\3\2\2\u02a8"+
		"\u02a6\3\2\2\2\u02a8\u02a7\3\2\2\2\u02a9\u02ac\3\2\2\2\u02aa\u02a8\3\2"+
		"\2\2\u02aa\u02ab\3\2\2\2\u02ab\u02ad\3\2\2\2\u02ac\u02aa\3\2\2\2\u02ad"+
		"\u02af\7$\2\2\u02ae\u029c\3\2\2\2\u02ae\u02a5\3\2\2\2\u02afh\3\2\2\2\u02b0"+
		"\u02b1\7^\2\2\u02b1\u02b2\13\2\2\2\u02b2j\3\2\2\2\u02b3\u02b7\5s:\2\u02b4"+
		"\u02b6\5u;\2\u02b5\u02b4\3\2\2\2\u02b6\u02b9\3\2\2\2\u02b7\u02b5\3\2\2"+
		"\2\u02b7\u02b8\3\2\2\2\u02b8\u02c0\3\2\2\2\u02b9\u02b7\3\2\2\2\u02ba\u02bc"+
		"\7\62\2\2\u02bb\u02ba\3\2\2\2\u02bc\u02bd\3\2\2\2\u02bd\u02bb\3\2\2\2"+
		"\u02bd\u02be\3\2\2\2\u02be\u02c0\3\2\2\2\u02bf\u02b3\3\2\2\2\u02bf\u02bb"+
		"\3\2\2\2\u02c0l\3\2\2\2\u02c1\u02c2\7\62\2\2\u02c2\u02c4\t\4\2\2\u02c3"+
		"\u02c5\5w<\2\u02c4\u02c3\3\2\2\2\u02c5\u02c6\3\2\2\2\u02c6\u02c4\3\2\2"+
		"\2\u02c6\u02c7\3\2\2\2\u02c7n\3\2\2\2\u02c8\u02c9\7\62\2\2\u02c9\u02cb"+
		"\t\5\2\2\u02ca\u02cc\5y=\2\u02cb\u02ca\3\2\2\2\u02cc\u02cd\3\2\2\2\u02cd"+
		"\u02cb\3\2\2\2\u02cd\u02ce\3\2\2\2\u02cep\3\2\2\2\u02cf\u02d0\7\62\2\2"+
		"\u02d0\u02d2\t\6\2\2\u02d1\u02d3\5{>\2\u02d2\u02d1\3\2\2\2\u02d3\u02d4"+
		"\3\2\2\2\u02d4\u02d2\3\2\2\2\u02d4\u02d5\3\2\2\2\u02d5r\3\2\2\2\u02d6"+
		"\u02d7\t\7\2\2\u02d7t\3\2\2\2\u02d8\u02d9\t\b\2\2\u02d9v\3\2\2\2\u02da"+
		"\u02db\t\t\2\2\u02dbx\3\2\2\2\u02dc\u02dd\t\n\2\2\u02ddz\3\2\2\2\u02de"+
		"\u02df\t\13\2\2\u02df|\3\2\2\2\u02e0\u02e3\5\177@\2\u02e1\u02e3\5\u0081"+
		"A\2\u02e2\u02e0\3\2\2\2\u02e2\u02e1\3\2\2\2\u02e3~\3\2\2\2\u02e4\u02e6"+
		"\5\u0083B\2\u02e5\u02e4\3\2\2\2\u02e5\u02e6\3\2\2\2\u02e6\u02e7\3\2\2"+
		"\2\u02e7\u02ec\5\u0085C\2\u02e8\u02e9\5\u0083B\2\u02e9\u02ea\7\60\2\2"+
		"\u02ea\u02ec\3\2\2\2\u02eb\u02e5\3\2\2\2\u02eb\u02e8\3\2\2\2\u02ec\u0080"+
		"\3\2\2\2\u02ed\u02f0\5\u0083B\2\u02ee\u02f0\5\177@\2\u02ef\u02ed\3\2\2"+
		"\2\u02ef\u02ee\3\2\2\2\u02f0\u02f1\3\2\2\2\u02f1\u02f2\5\u0087D\2\u02f2"+
		"\u0082\3\2\2\2\u02f3\u02f5\5u;\2\u02f4\u02f3\3\2\2\2\u02f5\u02f6\3\2\2"+
		"\2\u02f6\u02f4\3\2\2\2\u02f6\u02f7\3\2\2\2\u02f7\u0084\3\2\2\2\u02f8\u02fa"+
		"\7\60\2\2\u02f9\u02fb\5u;\2\u02fa\u02f9\3\2\2\2\u02fb\u02fc\3\2\2\2\u02fc"+
		"\u02fa\3\2\2\2\u02fc\u02fd\3\2\2\2\u02fd\u0086\3\2\2\2\u02fe\u0300\t\f"+
		"\2\2\u02ff\u0301\t\r\2\2\u0300\u02ff\3\2\2\2\u0300\u0301\3\2\2\2\u0301"+
		"\u0303\3\2\2\2\u0302\u0304\5u;\2\u0303\u0302\3\2\2\2\u0304\u0305\3\2\2"+
		"\2\u0305\u0303\3\2\2\2\u0305\u0306\3\2\2\2\u0306\u0088\3\2\2\2\u0307\u0308"+
		"\7p\2\2\u0308\u0309\7w\2\2\u0309\u030a\7n\2\2\u030a\u030b\7n\2\2\u030b"+
		"\u008a\3\2\2\2\u030c\u030d\7\60\2\2\u030d\u008c\3\2\2\2\u030e\u030f\7"+
		"\60\2\2\u030f\u0310\7\60\2\2\u0310\u0311\7\60\2\2\u0311\u008e\3\2\2\2"+
		"\u0312\u0313\7,\2\2\u0313\u0090\3\2\2\2\u0314\u0315\7*\2\2\u0315\u0316"+
		"\bI\3\2\u0316\u0092\3\2\2\2\u0317\u0318\7+\2\2\u0318\u0319\bJ\4\2\u0319"+
		"\u0094\3\2\2\2\u031a\u031b\7.\2\2\u031b\u0096\3\2\2\2\u031c\u031d\7<\2"+
		"\2\u031d\u0098\3\2\2\2\u031e\u031f\7=\2\2\u031f\u009a\3\2\2\2\u0320\u0321"+
		"\7,\2\2\u0321\u0322\7,\2\2\u0322\u009c\3\2\2\2\u0323\u0324\7?\2\2\u0324"+
		"\u009e\3\2\2\2\u0325\u0326\7]\2\2\u0326\u0327\bP\5\2\u0327\u00a0\3\2\2"+
		"\2\u0328\u0329\7_\2\2\u0329\u032a\bQ\6\2\u032a\u00a2\3\2\2\2\u032b\u032c"+
		"\7~\2\2\u032c\u00a4\3\2\2\2\u032d\u032e\7`\2\2\u032e\u00a6\3\2\2\2\u032f"+
		"\u0330\7(\2\2\u0330\u00a8\3\2\2\2\u0331\u0332\7>\2\2\u0332\u0333\7>\2"+
		"\2\u0333\u00aa\3\2\2\2\u0334\u0335\7@\2\2\u0335\u0336\7@\2\2\u0336\u00ac"+
		"\3\2\2\2\u0337\u0338\7-\2\2\u0338\u00ae\3\2\2\2\u0339\u033a\7/\2\2\u033a"+
		"\u00b0\3\2\2\2\u033b\u033c\7\61\2\2\u033c\u00b2\3\2\2\2\u033d\u033e\7"+
		"\'\2\2\u033e\u00b4\3\2\2\2\u033f\u0340\7\61\2\2\u0340\u0341\7\61\2\2\u0341"+
		"\u00b6\3\2\2\2\u0342\u0343\7\u0080\2\2\u0343\u00b8\3\2\2\2\u0344\u0345"+
		"\7}\2\2\u0345\u0346\b]\7\2\u0346\u00ba\3\2\2\2\u0347\u0348\7\177\2\2\u0348"+
		"\u0349\b^\b\2\u0349\u00bc\3\2\2\2\u034a\u034b\7>\2\2\u034b\u00be\3\2\2"+
		"\2\u034c\u034d\7@\2\2\u034d\u00c0\3\2\2\2\u034e\u034f\7?\2\2\u034f\u0350"+
		"\7?\2\2\u0350\u00c2\3\2\2\2\u0351\u0352\7@\2\2\u0352\u0353\7?\2\2\u0353"+
		"\u00c4\3\2\2\2\u0354\u0355\7>\2\2\u0355\u0356\7?\2\2\u0356\u00c6\3\2\2"+
		"\2\u0357\u0358\7>\2\2\u0358\u035c\7@\2\2\u0359\u035a\7#\2\2\u035a\u035c"+
		"\7?\2\2\u035b\u0357\3\2\2\2\u035b\u0359\3\2\2\2\u035c\u00c8\3\2\2\2\u035d"+
		"\u035e\7B\2\2\u035e\u00ca\3\2\2\2\u035f\u0360\7/\2\2\u0360\u0361\7@\2"+
		"\2\u0361\u00cc\3\2\2\2\u0362\u0363\7-\2\2\u0363\u0364\7?\2\2\u0364\u00ce"+
		"\3\2\2\2\u0365\u0366\7/\2\2\u0366\u0367\7?\2\2\u0367\u00d0\3\2\2\2\u0368"+
		"\u0369\7,\2\2\u0369\u036a\7?\2\2\u036a\u00d2\3\2\2\2\u036b\u036c\7B\2"+
		"\2\u036c\u036d\7?\2\2\u036d\u00d4\3\2\2\2\u036e\u036f\7\61\2\2\u036f\u0370"+
		"\7?\2\2\u0370\u00d6\3\2\2\2\u0371\u0372\7\'\2\2\u0372\u0373\7?\2\2\u0373"+
		"\u00d8\3\2\2\2\u0374\u0375\7(\2\2\u0375\u0376\7?\2\2\u0376\u00da\3\2\2"+
		"\2\u0377\u0378\7~\2\2\u0378\u0379\7?\2\2\u0379\u00dc\3\2\2\2\u037a\u037b"+
		"\7`\2\2\u037b\u037c\7?\2\2\u037c\u00de\3\2\2\2\u037d\u037e\7>\2\2\u037e"+
		"\u037f\7>\2\2\u037f\u0380\7?\2\2\u0380\u00e0\3\2\2\2\u0381\u0382\7@\2"+
		"\2\u0382\u0383\7@\2\2\u0383\u0384\7?\2\2\u0384\u00e2\3\2\2\2\u0385\u0386"+
		"\7,\2\2\u0386\u0387\7,\2\2\u0387\u0388\7?\2\2\u0388\u00e4\3\2\2\2\u0389"+
		"\u038a\7\61\2\2\u038a\u038b\7\61\2\2\u038b\u038c\7?\2\2\u038c\u00e6\3"+
		"\2\2\2\u038d\u0391\5\u00e9u\2\u038e\u0390\5\u00ebv\2\u038f\u038e\3\2\2"+
		"\2\u0390\u0393\3\2\2\2\u0391\u038f\3\2\2\2\u0391\u0392\3\2\2\2\u0392\u00e8"+
		"\3\2\2\2\u0393\u0391\3\2\2\2\u0394\u0396\t\16\2\2\u0395\u0394\3\2\2\2"+
		"\u0396\u00ea\3\2\2\2\u0397\u039a\5\u00e9u\2\u0398\u039a\t\b\2\2\u0399"+
		"\u0397\3\2\2\2\u0399\u0398\3\2\2\2\u039a\u00ec\3\2\2\2\u039b\u039f\5\u00f1"+
		"y\2\u039c\u039f\5\u00f3z\2\u039d\u039f\5\u00f5{\2\u039e\u039b\3\2\2\2"+
		"\u039e\u039c\3\2\2\2\u039e\u039d\3\2\2\2\u039f\u03a0\3\2\2\2\u03a0\u03a1"+
		"\bw\t\2\u03a1\u00ee\3\2\2\2\u03a2\u03a3\13\2\2\2\u03a3\u00f0\3\2\2\2\u03a4"+
		"\u03a6\t\17\2\2\u03a5\u03a4\3\2\2\2\u03a6\u03a7\3\2\2\2\u03a7\u03a5\3"+
		"\2\2\2\u03a7\u03a8\3\2\2\2\u03a8\u00f2\3\2\2\2\u03a9\u03ad\7%\2\2\u03aa"+
		"\u03ac\n\20\2\2\u03ab\u03aa\3\2\2\2\u03ac\u03af\3\2\2\2\u03ad\u03ab\3"+
		"\2\2\2\u03ad\u03ae\3\2\2\2\u03ae\u00f4\3\2\2\2\u03af\u03ad\3\2\2\2\u03b0"+
		"\u03b2\7^\2\2\u03b1\u03b3\5\u00f1y\2\u03b2\u03b1\3\2\2\2\u03b2\u03b3\3"+
		"\2\2\2\u03b3\u03b9\3\2\2\2\u03b4\u03b6\7\17\2\2\u03b5\u03b4\3\2\2\2\u03b5"+
		"\u03b6\3\2\2\2\u03b6\u03b7\3\2\2\2\u03b7\u03ba\7\f\2\2\u03b8\u03ba\7\17"+
		"\2\2\u03b9\u03b5\3\2\2\2\u03b9\u03b8\3\2\2\2\u03ba\u00f6\3\2\2\2$\2\u028f"+
		"\u0293\u0296\u0298\u029f\u02a1\u02a8\u02aa\u02ae\u02b7\u02bd\u02bf\u02c6"+
		"\u02cd\u02d4\u02e2\u02e5\u02eb\u02ef\u02f6\u02fc\u0300\u0305\u035b\u0391"+
		"\u0395\u0399\u039e\u03a7\u03ad\u03b2\u03b5\u03b9\n\3\63\2\3I\3\3J\4\3"+
		"P\5\3Q\6\3]\7\3^\b\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
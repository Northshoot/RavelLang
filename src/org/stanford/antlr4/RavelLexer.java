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
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, MODEL=23, SPACE=24, 
		CONTROLLER=25, VIEW=26, FLOW=27, INTERFACE=28, DEF=29, EVENT=30, COMMAND=31, 
		T_BYTE_FIELD=32, T_STRING_FIELD=33, T_BOOLEAN_FIELD=34, T_INTEGER_FIELD=35, 
		T_NUMBER_FIELD=36, T_DATE_FIELD=37, T_DATE_TIME_FIELD=38, T_TIME_STAMP_FIELD=39, 
		T_CONTEXT_FIELD=40, T_MODEL_FIELD=41, ASSERT=42, RETURN=43, TRUE=44, FALSE=45, 
		IF=46, ELIF=47, ELSE=48, FOR=49, WHILE=50, AND=51, NOT=52, OR=53, IN=54, 
		IS=55, DELETE=56, PASS=57, CONTINUE=58, BREAK=59, NONE=60, NEWLINE=61, 
		STRING_LITERAL=62, DECIMAL_INTEGER=63, FLOAT_NUMBER=64, NullLiteral=65, 
		ASSIGN=66, GT=67, LT=68, BANG=69, TILDE=70, QUESTION=71, COLON=72, EQUAL=73, 
		LE=74, GE=75, NOTEQUAL=76, AND_S=77, OR_S=78, INC=79, DEC=80, ADD=81, 
		SUB=82, MUL=83, DIV=84, BITAND=85, BITOR=86, CARET=87, MOD=88, ADD_ASSIGN=89, 
		SUB_ASSIGN=90, MUL_ASSIGN=91, DIV_ASSIGN=92, AND_ASSIGN=93, OR_ASSIGN=94, 
		XOR_ASSIGN=95, MOD_ASSIGN=96, LSHIFT_ASSIGN=97, RSHIFT_ASSIGN=98, URSHIFT_ASSIGN=99, 
		Identifier=100, SKIP_=101;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
		"T__17", "T__18", "T__19", "T__20", "T__21", "MODEL", "SPACE", "CONTROLLER", 
		"VIEW", "FLOW", "INTERFACE", "DEF", "EVENT", "COMMAND", "T_BYTE_FIELD", 
		"T_STRING_FIELD", "T_BOOLEAN_FIELD", "T_INTEGER_FIELD", "T_NUMBER_FIELD", 
		"T_DATE_FIELD", "T_DATE_TIME_FIELD", "T_TIME_STAMP_FIELD", "T_CONTEXT_FIELD", 
		"T_MODEL_FIELD", "ASSERT", "RETURN", "TRUE", "FALSE", "IF", "ELIF", "ELSE", 
		"FOR", "WHILE", "AND", "NOT", "OR", "IN", "IS", "DELETE", "PASS", "CONTINUE", 
		"BREAK", "NONE", "NEWLINE", "STRING_LITERAL", "STRING_ESCAPE_SEQ", "DECIMAL_INTEGER", 
		"NON_ZERO_DIGIT", "DIGIT", "FLOAT_NUMBER", "POINT_FLOAT", "INT_PART", 
		"FRACTION", "NullLiteral", "ASSIGN", "GT", "LT", "BANG", "TILDE", "QUESTION", 
		"COLON", "EQUAL", "LE", "GE", "NOTEQUAL", "AND_S", "OR_S", "INC", "DEC", 
		"ADD", "SUB", "MUL", "DIV", "BITAND", "BITOR", "CARET", "MOD", "ADD_ASSIGN", 
		"SUB_ASSIGN", "MUL_ASSIGN", "DIV_ASSIGN", "AND_ASSIGN", "OR_ASSIGN", "XOR_ASSIGN", 
		"MOD_ASSIGN", "LSHIFT_ASSIGN", "RSHIFT_ASSIGN", "URSHIFT_ASSIGN", "Identifier", 
		"ID_START", "ID_CONTINUE", "SKIP_", "SPACES", "COMMENT", "LINE_JOINING"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'platform:'", "'models:'", "'('", "')'", "','", "'controllers:'", 
		"'interfaces:'", "'implementation:'", "'local'", "'streaming'", "'replicated'", 
		"'properties:'", "'['", "']'", "'schema:'", "'.'", "'**'", "'//'", "'<<'", 
		"'>>'", "'{'", "'}'", "'model'", "'space'", "'controller'", "'view'", 
		"'flow'", "'interface'", "'def'", "'event'", "'command'", "'ByteField'", 
		"'StringField'", "'BooleanField'", "'IntegerField'", "'NumberField'", 
		"'DateField'", "'DateTimeField'", "'TimeStampField'", "'ContextField'", 
		"'ModelField'", "'assert'", "'return'", "'True'", "'False'", "'if'", "'else if'", 
		"'else'", "'for'", "'while'", "'and'", "'not'", "'or'", "'in'", "'is'", 
		"'delete'", "'pass'", "'continue'", "'break'", "'none'", null, null, null, 
		null, "'null'", "'='", "'>'", "'<'", "'!'", "'~'", "'?'", "':'", "'=='", 
		"'<='", "'>='", "'!='", "'&&'", "'||'", "'++'", "'--'", "'+'", "'-'", 
		"'*'", "'/'", "'&'", "'|'", "'^'", "'%'", "'+='", "'-='", "'*='", "'/='", 
		"'&='", "'|='", "'^='", "'%='", "'<<='", "'>>='", "'>>>='"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, "MODEL", 
		"SPACE", "CONTROLLER", "VIEW", "FLOW", "INTERFACE", "DEF", "EVENT", "COMMAND", 
		"T_BYTE_FIELD", "T_STRING_FIELD", "T_BOOLEAN_FIELD", "T_INTEGER_FIELD", 
		"T_NUMBER_FIELD", "T_DATE_FIELD", "T_DATE_TIME_FIELD", "T_TIME_STAMP_FIELD", 
		"T_CONTEXT_FIELD", "T_MODEL_FIELD", "ASSERT", "RETURN", "TRUE", "FALSE", 
		"IF", "ELIF", "ELSE", "FOR", "WHILE", "AND", "NOT", "OR", "IN", "IS", 
		"DELETE", "PASS", "CONTINUE", "BREAK", "NONE", "NEWLINE", "STRING_LITERAL", 
		"DECIMAL_INTEGER", "FLOAT_NUMBER", "NullLiteral", "ASSIGN", "GT", "LT", 
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
		case 60:
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
		case 60:
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2g\u0360\b\1\4\2\t"+
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
		"\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3"+
		"\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b"+
		"\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3"+
		"\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\17\3\17\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\22\3\22\3\22\3\23\3\23"+
		"\3\23\3\24\3\24\3\24\3\25\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\30"+
		"\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32"+
		"\3\32\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34"+
		"\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\36\3\36"+
		"\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3 \3 \3!\3"+
		"!\3!\3!\3!\3!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\""+
		"\3\"\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3$\3$\3$\3$\3$\3$\3$\3$\3"+
		"$\3$\3$\3$\3$\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3&\3&\3&\3&\3&\3&\3"+
		"&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3("+
		"\3(\3(\3(\3(\3(\3(\3(\3(\3(\3(\3(\3(\3(\3(\3)\3)\3)\3)\3)\3)\3)\3)\3)"+
		"\3)\3)\3)\3)\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3+\3+\3+\3+\3+\3+\3+\3,"+
		"\3,\3,\3,\3,\3,\3,\3-\3-\3-\3-\3-\3.\3.\3.\3.\3.\3.\3/\3/\3/\3\60\3\60"+
		"\3\60\3\60\3\60\3\60\3\60\3\60\3\61\3\61\3\61\3\61\3\61\3\62\3\62\3\62"+
		"\3\62\3\63\3\63\3\63\3\63\3\63\3\63\3\64\3\64\3\64\3\64\3\65\3\65\3\65"+
		"\3\65\3\66\3\66\3\66\3\67\3\67\3\67\38\38\38\39\39\39\39\39\39\39\3:\3"+
		":\3:\3:\3:\3;\3;\3;\3;\3;\3;\3;\3;\3;\3<\3<\3<\3<\3<\3<\3=\3=\3=\3=\3"+
		"=\3>\3>\3>\5>\u0289\n>\3>\3>\5>\u028d\n>\3>\5>\u0290\n>\5>\u0292\n>\3"+
		">\3>\3?\3?\3?\7?\u0299\n?\f?\16?\u029c\13?\3?\3?\3?\3?\7?\u02a2\n?\f?"+
		"\16?\u02a5\13?\3?\5?\u02a8\n?\3@\3@\3@\3A\3A\7A\u02af\nA\fA\16A\u02b2"+
		"\13A\3A\6A\u02b5\nA\rA\16A\u02b6\5A\u02b9\nA\3B\3B\3C\3C\3D\3D\3E\5E\u02c2"+
		"\nE\3E\3E\3E\3E\5E\u02c8\nE\3F\6F\u02cb\nF\rF\16F\u02cc\3G\3G\6G\u02d1"+
		"\nG\rG\16G\u02d2\3H\3H\3H\3H\3H\3I\3I\3J\3J\3K\3K\3L\3L\3M\3M\3N\3N\3"+
		"O\3O\3P\3P\3P\3Q\3Q\3Q\3R\3R\3R\3S\3S\3S\3T\3T\3T\3U\3U\3U\3V\3V\3V\3"+
		"W\3W\3W\3X\3X\3Y\3Y\3Z\3Z\3[\3[\3\\\3\\\3]\3]\3^\3^\3_\3_\3`\3`\3`\3a"+
		"\3a\3a\3b\3b\3b\3c\3c\3c\3d\3d\3d\3e\3e\3e\3f\3f\3f\3g\3g\3g\3h\3h\3h"+
		"\3h\3i\3i\3i\3i\3j\3j\3j\3j\3j\3k\3k\7k\u0337\nk\fk\16k\u033a\13k\3l\5"+
		"l\u033d\nl\3m\3m\5m\u0341\nm\3n\3n\3n\5n\u0346\nn\3n\3n\3o\6o\u034b\n"+
		"o\ro\16o\u034c\3p\3p\7p\u0351\np\fp\16p\u0354\13p\3q\3q\5q\u0358\nq\3"+
		"q\5q\u035b\nq\3q\3q\5q\u035f\nq\2\2r\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21"+
		"\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30"+
		"/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.["+
		"/]\60_\61a\62c\63e\64g\65i\66k\67m8o9q:s;u<w=y>{?}@\177\2\u0081A\u0083"+
		"\2\u0085\2\u0087B\u0089\2\u008b\2\u008d\2\u008fC\u0091D\u0093E\u0095F"+
		"\u0097G\u0099H\u009bI\u009dJ\u009fK\u00a1L\u00a3M\u00a5N\u00a7O\u00a9"+
		"P\u00abQ\u00adR\u00afS\u00b1T\u00b3U\u00b5V\u00b7W\u00b9X\u00bbY\u00bd"+
		"Z\u00bf[\u00c1\\\u00c3]\u00c5^\u00c7_\u00c9`\u00cba\u00cdb\u00cfc\u00d1"+
		"d\u00d3e\u00d5f\u00d7\2\u00d9\2\u00dbg\u00dd\2\u00df\2\u00e1\2\3\2\t\6"+
		"\2\f\f\17\17))^^\6\2\f\f\17\17$$^^\3\2\63;\3\2\62;\5\2C\\aac|\4\2\13\13"+
		"\"\"\4\2\f\f\17\17\u036d\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2"+
		"\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25"+
		"\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2"+
		"\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2"+
		"\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3"+
		"\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2"+
		"\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2"+
		"Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3"+
		"\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2"+
		"\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2"+
		"w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\u0081\3\2\2\2\2\u0087\3\2"+
		"\2\2\2\u008f\3\2\2\2\2\u0091\3\2\2\2\2\u0093\3\2\2\2\2\u0095\3\2\2\2\2"+
		"\u0097\3\2\2\2\2\u0099\3\2\2\2\2\u009b\3\2\2\2\2\u009d\3\2\2\2\2\u009f"+
		"\3\2\2\2\2\u00a1\3\2\2\2\2\u00a3\3\2\2\2\2\u00a5\3\2\2\2\2\u00a7\3\2\2"+
		"\2\2\u00a9\3\2\2\2\2\u00ab\3\2\2\2\2\u00ad\3\2\2\2\2\u00af\3\2\2\2\2\u00b1"+
		"\3\2\2\2\2\u00b3\3\2\2\2\2\u00b5\3\2\2\2\2\u00b7\3\2\2\2\2\u00b9\3\2\2"+
		"\2\2\u00bb\3\2\2\2\2\u00bd\3\2\2\2\2\u00bf\3\2\2\2\2\u00c1\3\2\2\2\2\u00c3"+
		"\3\2\2\2\2\u00c5\3\2\2\2\2\u00c7\3\2\2\2\2\u00c9\3\2\2\2\2\u00cb\3\2\2"+
		"\2\2\u00cd\3\2\2\2\2\u00cf\3\2\2\2\2\u00d1\3\2\2\2\2\u00d3\3\2\2\2\2\u00d5"+
		"\3\2\2\2\2\u00db\3\2\2\2\3\u00e3\3\2\2\2\5\u00ed\3\2\2\2\7\u00f5\3\2\2"+
		"\2\t\u00f7\3\2\2\2\13\u00f9\3\2\2\2\r\u00fb\3\2\2\2\17\u0108\3\2\2\2\21"+
		"\u0114\3\2\2\2\23\u0124\3\2\2\2\25\u012a\3\2\2\2\27\u0134\3\2\2\2\31\u013f"+
		"\3\2\2\2\33\u014b\3\2\2\2\35\u014d\3\2\2\2\37\u014f\3\2\2\2!\u0157\3\2"+
		"\2\2#\u0159\3\2\2\2%\u015c\3\2\2\2\'\u015f\3\2\2\2)\u0162\3\2\2\2+\u0165"+
		"\3\2\2\2-\u0167\3\2\2\2/\u0169\3\2\2\2\61\u016f\3\2\2\2\63\u0175\3\2\2"+
		"\2\65\u0180\3\2\2\2\67\u0185\3\2\2\29\u018a\3\2\2\2;\u0194\3\2\2\2=\u0198"+
		"\3\2\2\2?\u019e\3\2\2\2A\u01a6\3\2\2\2C\u01b0\3\2\2\2E\u01bc\3\2\2\2G"+
		"\u01c9\3\2\2\2I\u01d6\3\2\2\2K\u01e2\3\2\2\2M\u01ec\3\2\2\2O\u01fa\3\2"+
		"\2\2Q\u0209\3\2\2\2S\u0216\3\2\2\2U\u0221\3\2\2\2W\u0228\3\2\2\2Y\u022f"+
		"\3\2\2\2[\u0234\3\2\2\2]\u023a\3\2\2\2_\u023d\3\2\2\2a\u0245\3\2\2\2c"+
		"\u024a\3\2\2\2e\u024e\3\2\2\2g\u0254\3\2\2\2i\u0258\3\2\2\2k\u025c\3\2"+
		"\2\2m\u025f\3\2\2\2o\u0262\3\2\2\2q\u0265\3\2\2\2s\u026c\3\2\2\2u\u0271"+
		"\3\2\2\2w\u027a\3\2\2\2y\u0280\3\2\2\2{\u0291\3\2\2\2}\u02a7\3\2\2\2\177"+
		"\u02a9\3\2\2\2\u0081\u02b8\3\2\2\2\u0083\u02ba\3\2\2\2\u0085\u02bc\3\2"+
		"\2\2\u0087\u02be\3\2\2\2\u0089\u02c7\3\2\2\2\u008b\u02ca\3\2\2\2\u008d"+
		"\u02ce\3\2\2\2\u008f\u02d4\3\2\2\2\u0091\u02d9\3\2\2\2\u0093\u02db\3\2"+
		"\2\2\u0095\u02dd\3\2\2\2\u0097\u02df\3\2\2\2\u0099\u02e1\3\2\2\2\u009b"+
		"\u02e3\3\2\2\2\u009d\u02e5\3\2\2\2\u009f\u02e7\3\2\2\2\u00a1\u02ea\3\2"+
		"\2\2\u00a3\u02ed\3\2\2\2\u00a5\u02f0\3\2\2\2\u00a7\u02f3\3\2\2\2\u00a9"+
		"\u02f6\3\2\2\2\u00ab\u02f9\3\2\2\2\u00ad\u02fc\3\2\2\2\u00af\u02ff\3\2"+
		"\2\2\u00b1\u0301\3\2\2\2\u00b3\u0303\3\2\2\2\u00b5\u0305\3\2\2\2\u00b7"+
		"\u0307\3\2\2\2\u00b9\u0309\3\2\2\2\u00bb\u030b\3\2\2\2\u00bd\u030d\3\2"+
		"\2\2\u00bf\u030f\3\2\2\2\u00c1\u0312\3\2\2\2\u00c3\u0315\3\2\2\2\u00c5"+
		"\u0318\3\2\2\2\u00c7\u031b\3\2\2\2\u00c9\u031e\3\2\2\2\u00cb\u0321\3\2"+
		"\2\2\u00cd\u0324\3\2\2\2\u00cf\u0327\3\2\2\2\u00d1\u032b\3\2\2\2\u00d3"+
		"\u032f\3\2\2\2\u00d5\u0334\3\2\2\2\u00d7\u033c\3\2\2\2\u00d9\u0340\3\2"+
		"\2\2\u00db\u0345\3\2\2\2\u00dd\u034a\3\2\2\2\u00df\u034e\3\2\2\2\u00e1"+
		"\u0355\3\2\2\2\u00e3\u00e4\7r\2\2\u00e4\u00e5\7n\2\2\u00e5\u00e6\7c\2"+
		"\2\u00e6\u00e7\7v\2\2\u00e7\u00e8\7h\2\2\u00e8\u00e9\7q\2\2\u00e9\u00ea"+
		"\7t\2\2\u00ea\u00eb\7o\2\2\u00eb\u00ec\7<\2\2\u00ec\4\3\2\2\2\u00ed\u00ee"+
		"\7o\2\2\u00ee\u00ef\7q\2\2\u00ef\u00f0\7f\2\2\u00f0\u00f1\7g\2\2\u00f1"+
		"\u00f2\7n\2\2\u00f2\u00f3\7u\2\2\u00f3\u00f4\7<\2\2\u00f4\6\3\2\2\2\u00f5"+
		"\u00f6\7*\2\2\u00f6\b\3\2\2\2\u00f7\u00f8\7+\2\2\u00f8\n\3\2\2\2\u00f9"+
		"\u00fa\7.\2\2\u00fa\f\3\2\2\2\u00fb\u00fc\7e\2\2\u00fc\u00fd\7q\2\2\u00fd"+
		"\u00fe\7p\2\2\u00fe\u00ff\7v\2\2\u00ff\u0100\7t\2\2\u0100\u0101\7q\2\2"+
		"\u0101\u0102\7n\2\2\u0102\u0103\7n\2\2\u0103\u0104\7g\2\2\u0104\u0105"+
		"\7t\2\2\u0105\u0106\7u\2\2\u0106\u0107\7<\2\2\u0107\16\3\2\2\2\u0108\u0109"+
		"\7k\2\2\u0109\u010a\7p\2\2\u010a\u010b\7v\2\2\u010b\u010c\7g\2\2\u010c"+
		"\u010d\7t\2\2\u010d\u010e\7h\2\2\u010e\u010f\7c\2\2\u010f\u0110\7e\2\2"+
		"\u0110\u0111\7g\2\2\u0111\u0112\7u\2\2\u0112\u0113\7<\2\2\u0113\20\3\2"+
		"\2\2\u0114\u0115\7k\2\2\u0115\u0116\7o\2\2\u0116\u0117\7r\2\2\u0117\u0118"+
		"\7n\2\2\u0118\u0119\7g\2\2\u0119\u011a\7o\2\2\u011a\u011b\7g\2\2\u011b"+
		"\u011c\7p\2\2\u011c\u011d\7v\2\2\u011d\u011e\7c\2\2\u011e\u011f\7v\2\2"+
		"\u011f\u0120\7k\2\2\u0120\u0121\7q\2\2\u0121\u0122\7p\2\2\u0122\u0123"+
		"\7<\2\2\u0123\22\3\2\2\2\u0124\u0125\7n\2\2\u0125\u0126\7q\2\2\u0126\u0127"+
		"\7e\2\2\u0127\u0128\7c\2\2\u0128\u0129\7n\2\2\u0129\24\3\2\2\2\u012a\u012b"+
		"\7u\2\2\u012b\u012c\7v\2\2\u012c\u012d\7t\2\2\u012d\u012e\7g\2\2\u012e"+
		"\u012f\7c\2\2\u012f\u0130\7o\2\2\u0130\u0131\7k\2\2\u0131\u0132\7p\2\2"+
		"\u0132\u0133\7i\2\2\u0133\26\3\2\2\2\u0134\u0135\7t\2\2\u0135\u0136\7"+
		"g\2\2\u0136\u0137\7r\2\2\u0137\u0138\7n\2\2\u0138\u0139\7k\2\2\u0139\u013a"+
		"\7e\2\2\u013a\u013b\7c\2\2\u013b\u013c\7v\2\2\u013c\u013d\7g\2\2\u013d"+
		"\u013e\7f\2\2\u013e\30\3\2\2\2\u013f\u0140\7r\2\2\u0140\u0141\7t\2\2\u0141"+
		"\u0142\7q\2\2\u0142\u0143\7r\2\2\u0143\u0144\7g\2\2\u0144\u0145\7t\2\2"+
		"\u0145\u0146\7v\2\2\u0146\u0147\7k\2\2\u0147\u0148\7g\2\2\u0148\u0149"+
		"\7u\2\2\u0149\u014a\7<\2\2\u014a\32\3\2\2\2\u014b\u014c\7]\2\2\u014c\34"+
		"\3\2\2\2\u014d\u014e\7_\2\2\u014e\36\3\2\2\2\u014f\u0150\7u\2\2\u0150"+
		"\u0151\7e\2\2\u0151\u0152\7j\2\2\u0152\u0153\7g\2\2\u0153\u0154\7o\2\2"+
		"\u0154\u0155\7c\2\2\u0155\u0156\7<\2\2\u0156 \3\2\2\2\u0157\u0158\7\60"+
		"\2\2\u0158\"\3\2\2\2\u0159\u015a\7,\2\2\u015a\u015b\7,\2\2\u015b$\3\2"+
		"\2\2\u015c\u015d\7\61\2\2\u015d\u015e\7\61\2\2\u015e&\3\2\2\2\u015f\u0160"+
		"\7>\2\2\u0160\u0161\7>\2\2\u0161(\3\2\2\2\u0162\u0163\7@\2\2\u0163\u0164"+
		"\7@\2\2\u0164*\3\2\2\2\u0165\u0166\7}\2\2\u0166,\3\2\2\2\u0167\u0168\7"+
		"\177\2\2\u0168.\3\2\2\2\u0169\u016a\7o\2\2\u016a\u016b\7q\2\2\u016b\u016c"+
		"\7f\2\2\u016c\u016d\7g\2\2\u016d\u016e\7n\2\2\u016e\60\3\2\2\2\u016f\u0170"+
		"\7u\2\2\u0170\u0171\7r\2\2\u0171\u0172\7c\2\2\u0172\u0173\7e\2\2\u0173"+
		"\u0174\7g\2\2\u0174\62\3\2\2\2\u0175\u0176\7e\2\2\u0176\u0177\7q\2\2\u0177"+
		"\u0178\7p\2\2\u0178\u0179\7v\2\2\u0179\u017a\7t\2\2\u017a\u017b\7q\2\2"+
		"\u017b\u017c\7n\2\2\u017c\u017d\7n\2\2\u017d\u017e\7g\2\2\u017e\u017f"+
		"\7t\2\2\u017f\64\3\2\2\2\u0180\u0181\7x\2\2\u0181\u0182\7k\2\2\u0182\u0183"+
		"\7g\2\2\u0183\u0184\7y\2\2\u0184\66\3\2\2\2\u0185\u0186\7h\2\2\u0186\u0187"+
		"\7n\2\2\u0187\u0188\7q\2\2\u0188\u0189\7y\2\2\u01898\3\2\2\2\u018a\u018b"+
		"\7k\2\2\u018b\u018c\7p\2\2\u018c\u018d\7v\2\2\u018d\u018e\7g\2\2\u018e"+
		"\u018f\7t\2\2\u018f\u0190\7h\2\2\u0190\u0191\7c\2\2\u0191\u0192\7e\2\2"+
		"\u0192\u0193\7g\2\2\u0193:\3\2\2\2\u0194\u0195\7f\2\2\u0195\u0196\7g\2"+
		"\2\u0196\u0197\7h\2\2\u0197<\3\2\2\2\u0198\u0199\7g\2\2\u0199\u019a\7"+
		"x\2\2\u019a\u019b\7g\2\2\u019b\u019c\7p\2\2\u019c\u019d\7v\2\2\u019d>"+
		"\3\2\2\2\u019e\u019f\7e\2\2\u019f\u01a0\7q\2\2\u01a0\u01a1\7o\2\2\u01a1"+
		"\u01a2\7o\2\2\u01a2\u01a3\7c\2\2\u01a3\u01a4\7p\2\2\u01a4\u01a5\7f\2\2"+
		"\u01a5@\3\2\2\2\u01a6\u01a7\7D\2\2\u01a7\u01a8\7{\2\2\u01a8\u01a9\7v\2"+
		"\2\u01a9\u01aa\7g\2\2\u01aa\u01ab\7H\2\2\u01ab\u01ac\7k\2\2\u01ac\u01ad"+
		"\7g\2\2\u01ad\u01ae\7n\2\2\u01ae\u01af\7f\2\2\u01afB\3\2\2\2\u01b0\u01b1"+
		"\7U\2\2\u01b1\u01b2\7v\2\2\u01b2\u01b3\7t\2\2\u01b3\u01b4\7k\2\2\u01b4"+
		"\u01b5\7p\2\2\u01b5\u01b6\7i\2\2\u01b6\u01b7\7H\2\2\u01b7\u01b8\7k\2\2"+
		"\u01b8\u01b9\7g\2\2\u01b9\u01ba\7n\2\2\u01ba\u01bb\7f\2\2\u01bbD\3\2\2"+
		"\2\u01bc\u01bd\7D\2\2\u01bd\u01be\7q\2\2\u01be\u01bf\7q\2\2\u01bf\u01c0"+
		"\7n\2\2\u01c0\u01c1\7g\2\2\u01c1\u01c2\7c\2\2\u01c2\u01c3\7p\2\2\u01c3"+
		"\u01c4\7H\2\2\u01c4\u01c5\7k\2\2\u01c5\u01c6\7g\2\2\u01c6\u01c7\7n\2\2"+
		"\u01c7\u01c8\7f\2\2\u01c8F\3\2\2\2\u01c9\u01ca\7K\2\2\u01ca\u01cb\7p\2"+
		"\2\u01cb\u01cc\7v\2\2\u01cc\u01cd\7g\2\2\u01cd\u01ce\7i\2\2\u01ce\u01cf"+
		"\7g\2\2\u01cf\u01d0\7t\2\2\u01d0\u01d1\7H\2\2\u01d1\u01d2\7k\2\2\u01d2"+
		"\u01d3\7g\2\2\u01d3\u01d4\7n\2\2\u01d4\u01d5\7f\2\2\u01d5H\3\2\2\2\u01d6"+
		"\u01d7\7P\2\2\u01d7\u01d8\7w\2\2\u01d8\u01d9\7o\2\2\u01d9\u01da\7d\2\2"+
		"\u01da\u01db\7g\2\2\u01db\u01dc\7t\2\2\u01dc\u01dd\7H\2\2\u01dd\u01de"+
		"\7k\2\2\u01de\u01df\7g\2\2\u01df\u01e0\7n\2\2\u01e0\u01e1\7f\2\2\u01e1"+
		"J\3\2\2\2\u01e2\u01e3\7F\2\2\u01e3\u01e4\7c\2\2\u01e4\u01e5\7v\2\2\u01e5"+
		"\u01e6\7g\2\2\u01e6\u01e7\7H\2\2\u01e7\u01e8\7k\2\2\u01e8\u01e9\7g\2\2"+
		"\u01e9\u01ea\7n\2\2\u01ea\u01eb\7f\2\2\u01ebL\3\2\2\2\u01ec\u01ed\7F\2"+
		"\2\u01ed\u01ee\7c\2\2\u01ee\u01ef\7v\2\2\u01ef\u01f0\7g\2\2\u01f0\u01f1"+
		"\7V\2\2\u01f1\u01f2\7k\2\2\u01f2\u01f3\7o\2\2\u01f3\u01f4\7g\2\2\u01f4"+
		"\u01f5\7H\2\2\u01f5\u01f6\7k\2\2\u01f6\u01f7\7g\2\2\u01f7\u01f8\7n\2\2"+
		"\u01f8\u01f9\7f\2\2\u01f9N\3\2\2\2\u01fa\u01fb\7V\2\2\u01fb\u01fc\7k\2"+
		"\2\u01fc\u01fd\7o\2\2\u01fd\u01fe\7g\2\2\u01fe\u01ff\7U\2\2\u01ff\u0200"+
		"\7v\2\2\u0200\u0201\7c\2\2\u0201\u0202\7o\2\2\u0202\u0203\7r\2\2\u0203"+
		"\u0204\7H\2\2\u0204\u0205\7k\2\2\u0205\u0206\7g\2\2\u0206\u0207\7n\2\2"+
		"\u0207\u0208\7f\2\2\u0208P\3\2\2\2\u0209\u020a\7E\2\2\u020a\u020b\7q\2"+
		"\2\u020b\u020c\7p\2\2\u020c\u020d\7v\2\2\u020d\u020e\7g\2\2\u020e\u020f"+
		"\7z\2\2\u020f\u0210\7v\2\2\u0210\u0211\7H\2\2\u0211\u0212\7k\2\2\u0212"+
		"\u0213\7g\2\2\u0213\u0214\7n\2\2\u0214\u0215\7f\2\2\u0215R\3\2\2\2\u0216"+
		"\u0217\7O\2\2\u0217\u0218\7q\2\2\u0218\u0219\7f\2\2\u0219\u021a\7g\2\2"+
		"\u021a\u021b\7n\2\2\u021b\u021c\7H\2\2\u021c\u021d\7k\2\2\u021d\u021e"+
		"\7g\2\2\u021e\u021f\7n\2\2\u021f\u0220\7f\2\2\u0220T\3\2\2\2\u0221\u0222"+
		"\7c\2\2\u0222\u0223\7u\2\2\u0223\u0224\7u\2\2\u0224\u0225\7g\2\2\u0225"+
		"\u0226\7t\2\2\u0226\u0227\7v\2\2\u0227V\3\2\2\2\u0228\u0229\7t\2\2\u0229"+
		"\u022a\7g\2\2\u022a\u022b\7v\2\2\u022b\u022c\7w\2\2\u022c\u022d\7t\2\2"+
		"\u022d\u022e\7p\2\2\u022eX\3\2\2\2\u022f\u0230\7V\2\2\u0230\u0231\7t\2"+
		"\2\u0231\u0232\7w\2\2\u0232\u0233\7g\2\2\u0233Z\3\2\2\2\u0234\u0235\7"+
		"H\2\2\u0235\u0236\7c\2\2\u0236\u0237\7n\2\2\u0237\u0238\7u\2\2\u0238\u0239"+
		"\7g\2\2\u0239\\\3\2\2\2\u023a\u023b\7k\2\2\u023b\u023c\7h\2\2\u023c^\3"+
		"\2\2\2\u023d\u023e\7g\2\2\u023e\u023f\7n\2\2\u023f\u0240\7u\2\2\u0240"+
		"\u0241\7g\2\2\u0241\u0242\7\"\2\2\u0242\u0243\7k\2\2\u0243\u0244\7h\2"+
		"\2\u0244`\3\2\2\2\u0245\u0246\7g\2\2\u0246\u0247\7n\2\2\u0247\u0248\7"+
		"u\2\2\u0248\u0249\7g\2\2\u0249b\3\2\2\2\u024a\u024b\7h\2\2\u024b\u024c"+
		"\7q\2\2\u024c\u024d\7t\2\2\u024dd\3\2\2\2\u024e\u024f\7y\2\2\u024f\u0250"+
		"\7j\2\2\u0250\u0251\7k\2\2\u0251\u0252\7n\2\2\u0252\u0253\7g\2\2\u0253"+
		"f\3\2\2\2\u0254\u0255\7c\2\2\u0255\u0256\7p\2\2\u0256\u0257\7f\2\2\u0257"+
		"h\3\2\2\2\u0258\u0259\7p\2\2\u0259\u025a\7q\2\2\u025a\u025b\7v\2\2\u025b"+
		"j\3\2\2\2\u025c\u025d\7q\2\2\u025d\u025e\7t\2\2\u025el\3\2\2\2\u025f\u0260"+
		"\7k\2\2\u0260\u0261\7p\2\2\u0261n\3\2\2\2\u0262\u0263\7k\2\2\u0263\u0264"+
		"\7u\2\2\u0264p\3\2\2\2\u0265\u0266\7f\2\2\u0266\u0267\7g\2\2\u0267\u0268"+
		"\7n\2\2\u0268\u0269\7g\2\2\u0269\u026a\7v\2\2\u026a\u026b\7g\2\2\u026b"+
		"r\3\2\2\2\u026c\u026d\7r\2\2\u026d\u026e\7c\2\2\u026e\u026f\7u\2\2\u026f"+
		"\u0270\7u\2\2\u0270t\3\2\2\2\u0271\u0272\7e\2\2\u0272\u0273\7q\2\2\u0273"+
		"\u0274\7p\2\2\u0274\u0275\7v\2\2\u0275\u0276\7k\2\2\u0276\u0277\7p\2\2"+
		"\u0277\u0278\7w\2\2\u0278\u0279\7g\2\2\u0279v\3\2\2\2\u027a\u027b\7d\2"+
		"\2\u027b\u027c\7t\2\2\u027c\u027d\7g\2\2\u027d\u027e\7c\2\2\u027e\u027f"+
		"\7m\2\2\u027fx\3\2\2\2\u0280\u0281\7p\2\2\u0281\u0282\7q\2\2\u0282\u0283"+
		"\7p\2\2\u0283\u0284\7g\2\2\u0284z\3\2\2\2\u0285\u0286\6>\2\2\u0286\u0292"+
		"\5\u00ddo\2\u0287\u0289\7\17\2\2\u0288\u0287\3\2\2\2\u0288\u0289\3\2\2"+
		"\2\u0289\u028a\3\2\2\2\u028a\u028d\7\f\2\2\u028b\u028d\7\17\2\2\u028c"+
		"\u0288\3\2\2\2\u028c\u028b\3\2\2\2\u028d\u028f\3\2\2\2\u028e\u0290\5\u00dd"+
		"o\2\u028f\u028e\3\2\2\2\u028f\u0290\3\2\2\2\u0290\u0292\3\2\2\2\u0291"+
		"\u0285\3\2\2\2\u0291\u028c\3\2\2\2\u0292\u0293\3\2\2\2\u0293\u0294\b>"+
		"\2\2\u0294|\3\2\2\2\u0295\u029a\7)\2\2\u0296\u0299\5\177@\2\u0297\u0299"+
		"\n\2\2\2\u0298\u0296\3\2\2\2\u0298\u0297\3\2\2\2\u0299\u029c\3\2\2\2\u029a"+
		"\u0298\3\2\2\2\u029a\u029b\3\2\2\2\u029b\u029d\3\2\2\2\u029c\u029a\3\2"+
		"\2\2\u029d\u02a8\7)\2\2\u029e\u02a3\7$\2\2\u029f\u02a2\5\177@\2\u02a0"+
		"\u02a2\n\3\2\2\u02a1\u029f\3\2\2\2\u02a1\u02a0\3\2\2\2\u02a2\u02a5\3\2"+
		"\2\2\u02a3\u02a1\3\2\2\2\u02a3\u02a4\3\2\2\2\u02a4\u02a6\3\2\2\2\u02a5"+
		"\u02a3\3\2\2\2\u02a6\u02a8\7$\2\2\u02a7\u0295\3\2\2\2\u02a7\u029e\3\2"+
		"\2\2\u02a8~\3\2\2\2\u02a9\u02aa\7^\2\2\u02aa\u02ab\13\2\2\2\u02ab\u0080"+
		"\3\2\2\2\u02ac\u02b0\5\u0083B\2\u02ad\u02af\5\u0085C\2\u02ae\u02ad\3\2"+
		"\2\2\u02af\u02b2\3\2\2\2\u02b0\u02ae\3\2\2\2\u02b0\u02b1\3\2\2\2\u02b1"+
		"\u02b9\3\2\2\2\u02b2\u02b0\3\2\2\2\u02b3\u02b5\7\62\2\2\u02b4\u02b3\3"+
		"\2\2\2\u02b5\u02b6\3\2\2\2\u02b6\u02b4\3\2\2\2\u02b6\u02b7\3\2\2\2\u02b7"+
		"\u02b9\3\2\2\2\u02b8\u02ac\3\2\2\2\u02b8\u02b4\3\2\2\2\u02b9\u0082\3\2"+
		"\2\2\u02ba\u02bb\t\4\2\2\u02bb\u0084\3\2\2\2\u02bc\u02bd\t\5\2\2\u02bd"+
		"\u0086\3\2\2\2\u02be\u02bf\5\u0089E\2\u02bf\u0088\3\2\2\2\u02c0\u02c2"+
		"\5\u008bF\2\u02c1\u02c0\3\2\2\2\u02c1\u02c2\3\2\2\2\u02c2\u02c3\3\2\2"+
		"\2\u02c3\u02c8\5\u008dG\2\u02c4\u02c5\5\u008bF\2\u02c5\u02c6\7\60\2\2"+
		"\u02c6\u02c8\3\2\2\2\u02c7\u02c1\3\2\2\2\u02c7\u02c4\3\2\2\2\u02c8\u008a"+
		"\3\2\2\2\u02c9\u02cb\5\u0085C\2\u02ca\u02c9\3\2\2\2\u02cb\u02cc\3\2\2"+
		"\2\u02cc\u02ca\3\2\2\2\u02cc\u02cd\3\2\2\2\u02cd\u008c\3\2\2\2\u02ce\u02d0"+
		"\7\60\2\2\u02cf\u02d1\5\u0085C\2\u02d0\u02cf\3\2\2\2\u02d1\u02d2\3\2\2"+
		"\2\u02d2\u02d0\3\2\2\2\u02d2\u02d3\3\2\2\2\u02d3\u008e\3\2\2\2\u02d4\u02d5"+
		"\7p\2\2\u02d5\u02d6\7w\2\2\u02d6\u02d7\7n\2\2\u02d7\u02d8\7n\2\2\u02d8"+
		"\u0090\3\2\2\2\u02d9\u02da\7?\2\2\u02da\u0092\3\2\2\2\u02db\u02dc\7@\2"+
		"\2\u02dc\u0094\3\2\2\2\u02dd\u02de\7>\2\2\u02de\u0096\3\2\2\2\u02df\u02e0"+
		"\7#\2\2\u02e0\u0098\3\2\2\2\u02e1\u02e2\7\u0080\2\2\u02e2\u009a\3\2\2"+
		"\2\u02e3\u02e4\7A\2\2\u02e4\u009c\3\2\2\2\u02e5\u02e6\7<\2\2\u02e6\u009e"+
		"\3\2\2\2\u02e7\u02e8\7?\2\2\u02e8\u02e9\7?\2\2\u02e9\u00a0\3\2\2\2\u02ea"+
		"\u02eb\7>\2\2\u02eb\u02ec\7?\2\2\u02ec\u00a2\3\2\2\2\u02ed\u02ee\7@\2"+
		"\2\u02ee\u02ef\7?\2\2\u02ef\u00a4\3\2\2\2\u02f0\u02f1\7#\2\2\u02f1\u02f2"+
		"\7?\2\2\u02f2\u00a6\3\2\2\2\u02f3\u02f4\7(\2\2\u02f4\u02f5\7(\2\2\u02f5"+
		"\u00a8\3\2\2\2\u02f6\u02f7\7~\2\2\u02f7\u02f8\7~\2\2\u02f8\u00aa\3\2\2"+
		"\2\u02f9\u02fa\7-\2\2\u02fa\u02fb\7-\2\2\u02fb\u00ac\3\2\2\2\u02fc\u02fd"+
		"\7/\2\2\u02fd\u02fe\7/\2\2\u02fe\u00ae\3\2\2\2\u02ff\u0300\7-\2\2\u0300"+
		"\u00b0\3\2\2\2\u0301\u0302\7/\2\2\u0302\u00b2\3\2\2\2\u0303\u0304\7,\2"+
		"\2\u0304\u00b4\3\2\2\2\u0305\u0306\7\61\2\2\u0306\u00b6\3\2\2\2\u0307"+
		"\u0308\7(\2\2\u0308\u00b8\3\2\2\2\u0309\u030a\7~\2\2\u030a\u00ba\3\2\2"+
		"\2\u030b\u030c\7`\2\2\u030c\u00bc\3\2\2\2\u030d\u030e\7\'\2\2\u030e\u00be"+
		"\3\2\2\2\u030f\u0310\7-\2\2\u0310\u0311\7?\2\2\u0311\u00c0\3\2\2\2\u0312"+
		"\u0313\7/\2\2\u0313\u0314\7?\2\2\u0314\u00c2\3\2\2\2\u0315\u0316\7,\2"+
		"\2\u0316\u0317\7?\2\2\u0317\u00c4\3\2\2\2\u0318\u0319\7\61\2\2\u0319\u031a"+
		"\7?\2\2\u031a\u00c6\3\2\2\2\u031b\u031c\7(\2\2\u031c\u031d\7?\2\2\u031d"+
		"\u00c8\3\2\2\2\u031e\u031f\7~\2\2\u031f\u0320\7?\2\2\u0320\u00ca\3\2\2"+
		"\2\u0321\u0322\7`\2\2\u0322\u0323\7?\2\2\u0323\u00cc\3\2\2\2\u0324\u0325"+
		"\7\'\2\2\u0325\u0326\7?\2\2\u0326\u00ce\3\2\2\2\u0327\u0328\7>\2\2\u0328"+
		"\u0329\7>\2\2\u0329\u032a\7?\2\2\u032a\u00d0\3\2\2\2\u032b\u032c\7@\2"+
		"\2\u032c\u032d\7@\2\2\u032d\u032e\7?\2\2\u032e\u00d2\3\2\2\2\u032f\u0330"+
		"\7@\2\2\u0330\u0331\7@\2\2\u0331\u0332\7@\2\2\u0332\u0333\7?\2\2\u0333"+
		"\u00d4\3\2\2\2\u0334\u0338\5\u00d7l\2\u0335\u0337\5\u00d9m\2\u0336\u0335"+
		"\3\2\2\2\u0337\u033a\3\2\2\2\u0338\u0336\3\2\2\2\u0338\u0339\3\2\2\2\u0339"+
		"\u00d6\3\2\2\2\u033a\u0338\3\2\2\2\u033b\u033d\t\6\2\2\u033c\u033b\3\2"+
		"\2\2\u033d\u00d8\3\2\2\2\u033e\u0341\5\u00d7l\2\u033f\u0341\t\5\2\2\u0340"+
		"\u033e\3\2\2\2\u0340\u033f\3\2\2\2\u0341\u00da\3\2\2\2\u0342\u0346\5\u00dd"+
		"o\2\u0343\u0346\5\u00dfp\2\u0344\u0346\5\u00e1q\2\u0345\u0342\3\2\2\2"+
		"\u0345\u0343\3\2\2\2\u0345\u0344\3\2\2\2\u0346\u0347\3\2\2\2\u0347\u0348"+
		"\bn\3\2\u0348\u00dc\3\2\2\2\u0349\u034b\t\7\2\2\u034a\u0349\3\2\2\2\u034b"+
		"\u034c\3\2\2\2\u034c\u034a\3\2\2\2\u034c\u034d\3\2\2\2\u034d\u00de\3\2"+
		"\2\2\u034e\u0352\7%\2\2\u034f\u0351\n\b\2\2\u0350\u034f\3\2\2\2\u0351"+
		"\u0354\3\2\2\2\u0352\u0350\3\2\2\2\u0352\u0353\3\2\2\2\u0353\u00e0\3\2"+
		"\2\2\u0354\u0352\3\2\2\2\u0355\u0357\7^\2\2\u0356\u0358\5\u00ddo\2\u0357"+
		"\u0356\3\2\2\2\u0357\u0358\3\2\2\2\u0358\u035e\3\2\2\2\u0359\u035b\7\17"+
		"\2\2\u035a\u0359\3\2\2\2\u035a\u035b\3\2\2\2\u035b\u035c\3\2\2\2\u035c"+
		"\u035f\7\f\2\2\u035d\u035f\7\17\2\2\u035e\u035a\3\2\2\2\u035e\u035d\3"+
		"\2\2\2\u035f\u00e2\3\2\2\2\34\2\u0288\u028c\u028f\u0291\u0298\u029a\u02a1"+
		"\u02a3\u02a7\u02b0\u02b6\u02b8\u02c1\u02c7\u02cc\u02d2\u0338\u033c\u0340"+
		"\u0345\u034c\u0352\u0357\u035a\u035e\4\3>\2\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
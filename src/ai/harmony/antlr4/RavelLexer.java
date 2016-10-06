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
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, SELF=8, MODEL=9, 
		SPACE=10, CONTROLLER=11, VIEW=12, FLOW=13, LOCAL=14, STREAMING=15, REPLICATED=16, 
		PROPERTIES=17, SCHEMA=18, EVENT=19, COMMAND=20, T_BYTE_FIELD=21, T_STRING_FIELD=22, 
		T_BOOLEAN_FIELD=23, T_INTEGER_FIELD=24, T_NUMBER_FIELD=25, T_DATE_FIELD=26, 
		T_DATE_TIME_FIELD=27, T_TIME_STAMP_FIELD=28, T_CONTEXT_FIELD=29, T_MODEL_FIELD=30, 
		ASSERT=31, RETURN=32, TRUE=33, FALSE=34, IF=35, ELIF=36, ELSE=37, FOR=38, 
		WHILE=39, AND=40, NOT=41, OR=42, IN=43, IS=44, DELETE=45, PASS=46, CONTINUE=47, 
		BREAK=48, NONE=49, NEWLINE=50, IntegerLiteral=51, FloatingPointLiteral=52, 
		CharacterLiteral=53, StringLiteral=54, NullLiteral=55, LPAREN=56, RPAREN=57, 
		LBRACE=58, RBRACE=59, LBRACK=60, RBRACK=61, SEMI=62, COMMA=63, DOT=64, 
		ASSIGN=65, GT=66, LT=67, BANG=68, TILDE=69, QUESTION=70, COLON=71, EQUAL=72, 
		LE=73, GE=74, NOTEQUAL=75, AND_S=76, OR_S=77, INC=78, DEC=79, ADD=80, 
		SUB=81, MUL=82, DIV=83, BITAND=84, BITOR=85, CARET=86, MOD=87, ADD_ASSIGN=88, 
		SUB_ASSIGN=89, MUL_ASSIGN=90, DIV_ASSIGN=91, AND_ASSIGN=92, OR_ASSIGN=93, 
		XOR_ASSIGN=94, MOD_ASSIGN=95, LSHIFT_ASSIGN=96, RSHIFT_ASSIGN=97, URSHIFT_ASSIGN=98, 
		Identifier=99, SKIP_=100;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "SELF", "MODEL", 
		"SPACE", "CONTROLLER", "VIEW", "FLOW", "LOCAL", "STREAMING", "REPLICATED", 
		"PROPERTIES", "SCHEMA", "EVENT", "COMMAND", "T_BYTE_FIELD", "T_STRING_FIELD", 
		"T_BOOLEAN_FIELD", "T_INTEGER_FIELD", "T_NUMBER_FIELD", "T_DATE_FIELD", 
		"T_DATE_TIME_FIELD", "T_TIME_STAMP_FIELD", "T_CONTEXT_FIELD", "T_MODEL_FIELD", 
		"ASSERT", "RETURN", "TRUE", "FALSE", "IF", "ELIF", "ELSE", "FOR", "WHILE", 
		"AND", "NOT", "OR", "IN", "IS", "DELETE", "PASS", "CONTINUE", "BREAK", 
		"NONE", "NEWLINE", "IntegerLiteral", "DecimalIntegerLiteral", "HexIntegerLiteral", 
		"OctalIntegerLiteral", "BinaryIntegerLiteral", "IntegerTypeSuffix", "DecimalNumeral", 
		"Digits", "Digit", "NonZeroDigit", "DigitOrUnderscore", "Underscores", 
		"HexNumeral", "HexDigits", "HexDigit", "HexDigitOrUnderscore", "OctalNumeral", 
		"OctalDigits", "OctalDigit", "OctalDigitOrUnderscore", "BinaryNumeral", 
		"BinaryDigits", "BinaryDigit", "BinaryDigitOrUnderscore", "FloatingPointLiteral", 
		"DecimalFloatingPointLiteral", "ExponentPart", "ExponentIndicator", "SignedInteger", 
		"Sign", "FloatTypeSuffix", "HexadecimalFloatingPointLiteral", "HexSignificand", 
		"BinaryExponent", "BinaryExponentIndicator", "CharacterLiteral", "SingleCharacter", 
		"StringLiteral", "StringCharacters", "StringCharacter", "OctalEscape", 
		"UnicodeEscape", "ZeroToThree", "NullLiteral", "LPAREN", "RPAREN", "LBRACE", 
		"RBRACE", "LBRACK", "RBRACK", "SEMI", "COMMA", "DOT", "ASSIGN", "GT", 
		"LT", "BANG", "TILDE", "QUESTION", "COLON", "EQUAL", "LE", "GE", "NOTEQUAL", 
		"AND_S", "OR_S", "INC", "DEC", "ADD", "SUB", "MUL", "DIV", "BITAND", "BITOR", 
		"CARET", "MOD", "ADD_ASSIGN", "SUB_ASSIGN", "MUL_ASSIGN", "DIV_ASSIGN", 
		"AND_ASSIGN", "OR_ASSIGN", "XOR_ASSIGN", "MOD_ASSIGN", "LSHIFT_ASSIGN", 
		"RSHIFT_ASSIGN", "URSHIFT_ASSIGN", "Identifier", "JavaLetter", "JavaLetterOrDigit", 
		"SKIP_", "SPACES", "COMMENT", "LINE_JOINING"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'platform:'", "'models:'", "'controllers:'", "'sinks:'", "'sources:'", 
		"'properties:'", "'schema:'", "'self'", "'model'", "'space'", "'controller'", 
		"'view'", "'flow'", "'local'", "'streaming'", "'replicated'", "'properties'", 
		"'schema'", "'event'", "'command'", "'ByteField'", "'StringField'", "'BooleanField'", 
		"'IntegerField'", "'NumberField'", "'DateField'", "'DateTimeField'", "'TimeStampField'", 
		"'ContextField'", "'ModelField'", "'assert'", "'return'", "'True'", "'False'", 
		"'if'", "'else if'", "'else'", "'for'", "'while'", "'and'", "'not'", "'or'", 
		"'in'", "'is'", "'delete'", "'pass'", "'continue'", "'break'", "'none'", 
		null, null, null, null, null, "'null'", "'('", "')'", "'{'", "'}'", "'['", 
		"']'", "';'", "','", "'.'", "'='", "'>'", "'<'", "'!'", "'~'", "'?'", 
		"':'", "'=='", "'<='", "'>='", "'!='", "'&&'", "'||'", "'++'", "'--'", 
		"'+'", "'-'", "'*'", "'/'", "'&'", "'|'", "'^'", "'%'", "'+='", "'-='", 
		"'*='", "'/='", "'&='", "'|='", "'^='", "'%='", "'<<='", "'>>='", "'>>>='"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, "SELF", "MODEL", "SPACE", 
		"CONTROLLER", "VIEW", "FLOW", "LOCAL", "STREAMING", "REPLICATED", "PROPERTIES", 
		"SCHEMA", "EVENT", "COMMAND", "T_BYTE_FIELD", "T_STRING_FIELD", "T_BOOLEAN_FIELD", 
		"T_INTEGER_FIELD", "T_NUMBER_FIELD", "T_DATE_FIELD", "T_DATE_TIME_FIELD", 
		"T_TIME_STAMP_FIELD", "T_CONTEXT_FIELD", "T_MODEL_FIELD", "ASSERT", "RETURN", 
		"TRUE", "FALSE", "IF", "ELIF", "ELSE", "FOR", "WHILE", "AND", "NOT", "OR", 
		"IN", "IS", "DELETE", "PASS", "CONTINUE", "BREAK", "NONE", "NEWLINE", 
		"IntegerLiteral", "FloatingPointLiteral", "CharacterLiteral", "StringLiteral", 
		"NullLiteral", "LPAREN", "RPAREN", "LBRACE", "RBRACE", "LBRACK", "RBRACK", 
		"SEMI", "COMMA", "DOT", "ASSIGN", "GT", "LT", "BANG", "TILDE", "QUESTION", 
		"COLON", "EQUAL", "LE", "GE", "NOTEQUAL", "AND_S", "OR_S", "INC", "DEC", 
		"ADD", "SUB", "MUL", "DIV", "BITAND", "BITOR", "CARET", "MOD", "ADD_ASSIGN", 
		"SUB_ASSIGN", "MUL_ASSIGN", "DIV_ASSIGN", "AND_ASSIGN", "OR_ASSIGN", "XOR_ASSIGN", 
		"MOD_ASSIGN", "LSHIFT_ASSIGN", "RSHIFT_ASSIGN", "URSHIFT_ASSIGN", "Identifier", 
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
		case 49:
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
		case 49:
			return NEWLINE_sempred((RuleContext)_localctx, predIndex);
		case 138:
			return JavaLetter_sempred((RuleContext)_localctx, predIndex);
		case 139:
			return JavaLetterOrDigit_sempred((RuleContext)_localctx, predIndex);
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
	private boolean JavaLetter_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return Character.isJavaIdentifierStart(_input.LA(-1));
		case 2:
			return Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)));
		}
		return true;
	}
	private boolean JavaLetterOrDigit_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3:
			return Character.isJavaIdentifierPart(_input.LA(-1));
		case 4:
			return Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)));
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2f\u0447\b\1\4\2\t"+
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
		"\t\u0085\4\u0086\t\u0086\4\u0087\t\u0087\4\u0088\t\u0088\4\u0089\t\u0089"+
		"\4\u008a\t\u008a\4\u008b\t\u008b\4\u008c\t\u008c\4\u008d\t\u008d\4\u008e"+
		"\t\u008e\4\u008f\t\u008f\4\u0090\t\u0090\4\u0091\t\u0091\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3"+
		"\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3"+
		"\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3"+
		"\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3"+
		"\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3"+
		"\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3"+
		"\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3"+
		"\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3"+
		"\32\3\32\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3"+
		"\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3"+
		"\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3"+
		"\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3"+
		"\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3"+
		" \3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3#\3#\3#"+
		"\3#\3#\3#\3$\3$\3$\3%\3%\3%\3%\3%\3%\3%\3%\3&\3&\3&\3&\3&\3\'\3\'\3\'"+
		"\3\'\3(\3(\3(\3(\3(\3(\3)\3)\3)\3)\3*\3*\3*\3*\3+\3+\3+\3,\3,\3,\3-\3"+
		"-\3-\3.\3.\3.\3.\3.\3.\3.\3/\3/\3/\3/\3/\3\60\3\60\3\60\3\60\3\60\3\60"+
		"\3\60\3\60\3\60\3\61\3\61\3\61\3\61\3\61\3\61\3\62\3\62\3\62\3\62\3\62"+
		"\3\63\3\63\3\63\5\63\u02aa\n\63\3\63\3\63\5\63\u02ae\n\63\3\63\5\63\u02b1"+
		"\n\63\5\63\u02b3\n\63\3\63\3\63\3\64\3\64\3\64\3\64\5\64\u02bb\n\64\3"+
		"\65\3\65\5\65\u02bf\n\65\3\66\3\66\5\66\u02c3\n\66\3\67\3\67\5\67\u02c7"+
		"\n\67\38\38\58\u02cb\n8\39\39\3:\3:\3:\5:\u02d2\n:\3:\3:\3:\5:\u02d7\n"+
		":\5:\u02d9\n:\3;\3;\7;\u02dd\n;\f;\16;\u02e0\13;\3;\5;\u02e3\n;\3<\3<"+
		"\5<\u02e7\n<\3=\3=\3>\3>\5>\u02ed\n>\3?\6?\u02f0\n?\r?\16?\u02f1\3@\3"+
		"@\3@\3@\3A\3A\7A\u02fa\nA\fA\16A\u02fd\13A\3A\5A\u0300\nA\3B\3B\3C\3C"+
		"\5C\u0306\nC\3D\3D\5D\u030a\nD\3D\3D\3E\3E\7E\u0310\nE\fE\16E\u0313\13"+
		"E\3E\5E\u0316\nE\3F\3F\3G\3G\5G\u031c\nG\3H\3H\3H\3H\3I\3I\7I\u0324\n"+
		"I\fI\16I\u0327\13I\3I\5I\u032a\nI\3J\3J\3K\3K\5K\u0330\nK\3L\3L\5L\u0334"+
		"\nL\3M\3M\3M\5M\u0339\nM\3M\5M\u033c\nM\3M\5M\u033f\nM\3M\3M\3M\5M\u0344"+
		"\nM\3M\5M\u0347\nM\3M\3M\3M\5M\u034c\nM\3M\3M\3M\5M\u0351\nM\3N\3N\3N"+
		"\3O\3O\3P\5P\u0359\nP\3P\3P\3Q\3Q\3R\3R\3S\3S\3S\5S\u0364\nS\3T\3T\5T"+
		"\u0368\nT\3T\3T\3T\5T\u036d\nT\3T\3T\5T\u0371\nT\3U\3U\3U\3V\3V\3W\3W"+
		"\3W\3W\3X\3X\3Y\3Y\5Y\u0380\nY\3Y\3Y\3Z\6Z\u0385\nZ\rZ\16Z\u0386\3[\3"+
		"[\3\\\3\\\3\\\3\\\3\\\3\\\3\\\3\\\3\\\3\\\3\\\5\\\u0396\n\\\3]\3]\3]\3"+
		"]\3]\3]\3]\3^\3^\3_\3_\3_\3_\3_\3`\3`\3a\3a\3b\3b\3c\3c\3d\3d\3e\3e\3"+
		"f\3f\3g\3g\3h\3h\3i\3i\3j\3j\3k\3k\3l\3l\3m\3m\3n\3n\3o\3o\3p\3p\3p\3"+
		"q\3q\3q\3r\3r\3r\3s\3s\3s\3t\3t\3t\3u\3u\3u\3v\3v\3v\3w\3w\3w\3x\3x\3"+
		"y\3y\3z\3z\3{\3{\3|\3|\3}\3}\3~\3~\3\177\3\177\3\u0080\3\u0080\3\u0080"+
		"\3\u0081\3\u0081\3\u0081\3\u0082\3\u0082\3\u0082\3\u0083\3\u0083\3\u0083"+
		"\3\u0084\3\u0084\3\u0084\3\u0085\3\u0085\3\u0085\3\u0086\3\u0086\3\u0086"+
		"\3\u0087\3\u0087\3\u0087\3\u0088\3\u0088\3\u0088\3\u0088\3\u0089\3\u0089"+
		"\3\u0089\3\u0089\3\u008a\3\u008a\3\u008a\3\u008a\3\u008a\3\u008b\3\u008b"+
		"\7\u008b\u0415\n\u008b\f\u008b\16\u008b\u0418\13\u008b\3\u008c\3\u008c"+
		"\3\u008c\3\u008c\3\u008c\3\u008c\5\u008c\u0420\n\u008c\3\u008d\3\u008d"+
		"\3\u008d\3\u008d\3\u008d\3\u008d\5\u008d\u0428\n\u008d\3\u008e\3\u008e"+
		"\3\u008e\5\u008e\u042d\n\u008e\3\u008e\3\u008e\3\u008f\6\u008f\u0432\n"+
		"\u008f\r\u008f\16\u008f\u0433\3\u0090\3\u0090\7\u0090\u0438\n\u0090\f"+
		"\u0090\16\u0090\u043b\13\u0090\3\u0091\3\u0091\5\u0091\u043f\n\u0091\3"+
		"\u0091\5\u0091\u0442\n\u0091\3\u0091\3\u0091\5\u0091\u0446\n\u0091\2\2"+
		"\u0092\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17"+
		"\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\35"+
		"9\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g\65i\2"+
		"k\2m\2o\2q\2s\2u\2w\2y\2{\2}\2\177\2\u0081\2\u0083\2\u0085\2\u0087\2\u0089"+
		"\2\u008b\2\u008d\2\u008f\2\u0091\2\u0093\2\u0095\2\u0097\66\u0099\2\u009b"+
		"\2\u009d\2\u009f\2\u00a1\2\u00a3\2\u00a5\2\u00a7\2\u00a9\2\u00ab\2\u00ad"+
		"\67\u00af\2\u00b18\u00b3\2\u00b5\2\u00b7\2\u00b9\2\u00bb\2\u00bd9\u00bf"+
		":\u00c1;\u00c3<\u00c5=\u00c7>\u00c9?\u00cb@\u00cdA\u00cfB\u00d1C\u00d3"+
		"D\u00d5E\u00d7F\u00d9G\u00dbH\u00ddI\u00dfJ\u00e1K\u00e3L\u00e5M\u00e7"+
		"N\u00e9O\u00ebP\u00edQ\u00efR\u00f1S\u00f3T\u00f5U\u00f7V\u00f9W\u00fb"+
		"X\u00fdY\u00ffZ\u0101[\u0103\\\u0105]\u0107^\u0109_\u010b`\u010da\u010f"+
		"b\u0111c\u0113d\u0115e\u0117\2\u0119\2\u011bf\u011d\2\u011f\2\u0121\2"+
		"\3\2\27\4\2NNnn\3\2\63;\4\2ZZzz\5\2\62;CHch\3\2\629\4\2DDdd\3\2\62\63"+
		"\4\2GGgg\4\2--//\6\2FFHHffhh\4\2RRrr\4\2))^^\4\2$$^^\3\2\62\65\6\2&&C"+
		"\\aac|\4\2\2\u0081\ud802\udc01\3\2\ud802\udc01\3\2\udc02\ue001\7\2&&\62"+
		";C\\aac|\4\2\13\13\"\"\4\2\f\f\17\17\u0456\2\3\3\2\2\2\2\5\3\2\2\2\2\7"+
		"\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2"+
		"\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2"+
		"\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2"+
		"\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2"+
		"\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2"+
		"\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M"+
		"\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2"+
		"\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2"+
		"\2g\3\2\2\2\2\u0097\3\2\2\2\2\u00ad\3\2\2\2\2\u00b1\3\2\2\2\2\u00bd\3"+
		"\2\2\2\2\u00bf\3\2\2\2\2\u00c1\3\2\2\2\2\u00c3\3\2\2\2\2\u00c5\3\2\2\2"+
		"\2\u00c7\3\2\2\2\2\u00c9\3\2\2\2\2\u00cb\3\2\2\2\2\u00cd\3\2\2\2\2\u00cf"+
		"\3\2\2\2\2\u00d1\3\2\2\2\2\u00d3\3\2\2\2\2\u00d5\3\2\2\2\2\u00d7\3\2\2"+
		"\2\2\u00d9\3\2\2\2\2\u00db\3\2\2\2\2\u00dd\3\2\2\2\2\u00df\3\2\2\2\2\u00e1"+
		"\3\2\2\2\2\u00e3\3\2\2\2\2\u00e5\3\2\2\2\2\u00e7\3\2\2\2\2\u00e9\3\2\2"+
		"\2\2\u00eb\3\2\2\2\2\u00ed\3\2\2\2\2\u00ef\3\2\2\2\2\u00f1\3\2\2\2\2\u00f3"+
		"\3\2\2\2\2\u00f5\3\2\2\2\2\u00f7\3\2\2\2\2\u00f9\3\2\2\2\2\u00fb\3\2\2"+
		"\2\2\u00fd\3\2\2\2\2\u00ff\3\2\2\2\2\u0101\3\2\2\2\2\u0103\3\2\2\2\2\u0105"+
		"\3\2\2\2\2\u0107\3\2\2\2\2\u0109\3\2\2\2\2\u010b\3\2\2\2\2\u010d\3\2\2"+
		"\2\2\u010f\3\2\2\2\2\u0111\3\2\2\2\2\u0113\3\2\2\2\2\u0115\3\2\2\2\2\u011b"+
		"\3\2\2\2\3\u0123\3\2\2\2\5\u012d\3\2\2\2\7\u0135\3\2\2\2\t\u0142\3\2\2"+
		"\2\13\u0149\3\2\2\2\r\u0152\3\2\2\2\17\u015e\3\2\2\2\21\u0166\3\2\2\2"+
		"\23\u016b\3\2\2\2\25\u0171\3\2\2\2\27\u0177\3\2\2\2\31\u0182\3\2\2\2\33"+
		"\u0187\3\2\2\2\35\u018c\3\2\2\2\37\u0192\3\2\2\2!\u019c\3\2\2\2#\u01a7"+
		"\3\2\2\2%\u01b2\3\2\2\2\'\u01b9\3\2\2\2)\u01bf\3\2\2\2+\u01c7\3\2\2\2"+
		"-\u01d1\3\2\2\2/\u01dd\3\2\2\2\61\u01ea\3\2\2\2\63\u01f7\3\2\2\2\65\u0203"+
		"\3\2\2\2\67\u020d\3\2\2\29\u021b\3\2\2\2;\u022a\3\2\2\2=\u0237\3\2\2\2"+
		"?\u0242\3\2\2\2A\u0249\3\2\2\2C\u0250\3\2\2\2E\u0255\3\2\2\2G\u025b\3"+
		"\2\2\2I\u025e\3\2\2\2K\u0266\3\2\2\2M\u026b\3\2\2\2O\u026f\3\2\2\2Q\u0275"+
		"\3\2\2\2S\u0279\3\2\2\2U\u027d\3\2\2\2W\u0280\3\2\2\2Y\u0283\3\2\2\2["+
		"\u0286\3\2\2\2]\u028d\3\2\2\2_\u0292\3\2\2\2a\u029b\3\2\2\2c\u02a1\3\2"+
		"\2\2e\u02b2\3\2\2\2g\u02ba\3\2\2\2i\u02bc\3\2\2\2k\u02c0\3\2\2\2m\u02c4"+
		"\3\2\2\2o\u02c8\3\2\2\2q\u02cc\3\2\2\2s\u02d8\3\2\2\2u\u02da\3\2\2\2w"+
		"\u02e6\3\2\2\2y\u02e8\3\2\2\2{\u02ec\3\2\2\2}\u02ef\3\2\2\2\177\u02f3"+
		"\3\2\2\2\u0081\u02f7\3\2\2\2\u0083\u0301\3\2\2\2\u0085\u0305\3\2\2\2\u0087"+
		"\u0307\3\2\2\2\u0089\u030d\3\2\2\2\u008b\u0317\3\2\2\2\u008d\u031b\3\2"+
		"\2\2\u008f\u031d\3\2\2\2\u0091\u0321\3\2\2\2\u0093\u032b\3\2\2\2\u0095"+
		"\u032f\3\2\2\2\u0097\u0333\3\2\2\2\u0099\u0350\3\2\2\2\u009b\u0352\3\2"+
		"\2\2\u009d\u0355\3\2\2\2\u009f\u0358\3\2\2\2\u00a1\u035c\3\2\2\2\u00a3"+
		"\u035e\3\2\2\2\u00a5\u0360\3\2\2\2\u00a7\u0370\3\2\2\2\u00a9\u0372\3\2"+
		"\2\2\u00ab\u0375\3\2\2\2\u00ad\u0377\3\2\2\2\u00af\u037b\3\2\2\2\u00b1"+
		"\u037d\3\2\2\2\u00b3\u0384\3\2\2\2\u00b5\u0388\3\2\2\2\u00b7\u0395\3\2"+
		"\2\2\u00b9\u0397\3\2\2\2\u00bb\u039e\3\2\2\2\u00bd\u03a0\3\2\2\2\u00bf"+
		"\u03a5\3\2\2\2\u00c1\u03a7\3\2\2\2\u00c3\u03a9\3\2\2\2\u00c5\u03ab\3\2"+
		"\2\2\u00c7\u03ad\3\2\2\2\u00c9\u03af\3\2\2\2\u00cb\u03b1\3\2\2\2\u00cd"+
		"\u03b3\3\2\2\2\u00cf\u03b5\3\2\2\2\u00d1\u03b7\3\2\2\2\u00d3\u03b9\3\2"+
		"\2\2\u00d5\u03bb\3\2\2\2\u00d7\u03bd\3\2\2\2\u00d9\u03bf\3\2\2\2\u00db"+
		"\u03c1\3\2\2\2\u00dd\u03c3\3\2\2\2\u00df\u03c5\3\2\2\2\u00e1\u03c8\3\2"+
		"\2\2\u00e3\u03cb\3\2\2\2\u00e5\u03ce\3\2\2\2\u00e7\u03d1\3\2\2\2\u00e9"+
		"\u03d4\3\2\2\2\u00eb\u03d7\3\2\2\2\u00ed\u03da\3\2\2\2\u00ef\u03dd\3\2"+
		"\2\2\u00f1\u03df\3\2\2\2\u00f3\u03e1\3\2\2\2\u00f5\u03e3\3\2\2\2\u00f7"+
		"\u03e5\3\2\2\2\u00f9\u03e7\3\2\2\2\u00fb\u03e9\3\2\2\2\u00fd\u03eb\3\2"+
		"\2\2\u00ff\u03ed\3\2\2\2\u0101\u03f0\3\2\2\2\u0103\u03f3\3\2\2\2\u0105"+
		"\u03f6\3\2\2\2\u0107\u03f9\3\2\2\2\u0109\u03fc\3\2\2\2\u010b\u03ff\3\2"+
		"\2\2\u010d\u0402\3\2\2\2\u010f\u0405\3\2\2\2\u0111\u0409\3\2\2\2\u0113"+
		"\u040d\3\2\2\2\u0115\u0412\3\2\2\2\u0117\u041f\3\2\2\2\u0119\u0427\3\2"+
		"\2\2\u011b\u042c\3\2\2\2\u011d\u0431\3\2\2\2\u011f\u0435\3\2\2\2\u0121"+
		"\u043c\3\2\2\2\u0123\u0124\7r\2\2\u0124\u0125\7n\2\2\u0125\u0126\7c\2"+
		"\2\u0126\u0127\7v\2\2\u0127\u0128\7h\2\2\u0128\u0129\7q\2\2\u0129\u012a"+
		"\7t\2\2\u012a\u012b\7o\2\2\u012b\u012c\7<\2\2\u012c\4\3\2\2\2\u012d\u012e"+
		"\7o\2\2\u012e\u012f\7q\2\2\u012f\u0130\7f\2\2\u0130\u0131\7g\2\2\u0131"+
		"\u0132\7n\2\2\u0132\u0133\7u\2\2\u0133\u0134\7<\2\2\u0134\6\3\2\2\2\u0135"+
		"\u0136\7e\2\2\u0136\u0137\7q\2\2\u0137\u0138\7p\2\2\u0138\u0139\7v\2\2"+
		"\u0139\u013a\7t\2\2\u013a\u013b\7q\2\2\u013b\u013c\7n\2\2\u013c\u013d"+
		"\7n\2\2\u013d\u013e\7g\2\2\u013e\u013f\7t\2\2\u013f\u0140\7u\2\2\u0140"+
		"\u0141\7<\2\2\u0141\b\3\2\2\2\u0142\u0143\7u\2\2\u0143\u0144\7k\2\2\u0144"+
		"\u0145\7p\2\2\u0145\u0146\7m\2\2\u0146\u0147\7u\2\2\u0147\u0148\7<\2\2"+
		"\u0148\n\3\2\2\2\u0149\u014a\7u\2\2\u014a\u014b\7q\2\2\u014b\u014c\7w"+
		"\2\2\u014c\u014d\7t\2\2\u014d\u014e\7e\2\2\u014e\u014f\7g\2\2\u014f\u0150"+
		"\7u\2\2\u0150\u0151\7<\2\2\u0151\f\3\2\2\2\u0152\u0153\7r\2\2\u0153\u0154"+
		"\7t\2\2\u0154\u0155\7q\2\2\u0155\u0156\7r\2\2\u0156\u0157\7g\2\2\u0157"+
		"\u0158\7t\2\2\u0158\u0159\7v\2\2\u0159\u015a\7k\2\2\u015a\u015b\7g\2\2"+
		"\u015b\u015c\7u\2\2\u015c\u015d\7<\2\2\u015d\16\3\2\2\2\u015e\u015f\7"+
		"u\2\2\u015f\u0160\7e\2\2\u0160\u0161\7j\2\2\u0161\u0162\7g\2\2\u0162\u0163"+
		"\7o\2\2\u0163\u0164\7c\2\2\u0164\u0165\7<\2\2\u0165\20\3\2\2\2\u0166\u0167"+
		"\7u\2\2\u0167\u0168\7g\2\2\u0168\u0169\7n\2\2\u0169\u016a\7h\2\2\u016a"+
		"\22\3\2\2\2\u016b\u016c\7o\2\2\u016c\u016d\7q\2\2\u016d\u016e\7f\2\2\u016e"+
		"\u016f\7g\2\2\u016f\u0170\7n\2\2\u0170\24\3\2\2\2\u0171\u0172\7u\2\2\u0172"+
		"\u0173\7r\2\2\u0173\u0174\7c\2\2\u0174\u0175\7e\2\2\u0175\u0176\7g\2\2"+
		"\u0176\26\3\2\2\2\u0177\u0178\7e\2\2\u0178\u0179\7q\2\2\u0179\u017a\7"+
		"p\2\2\u017a\u017b\7v\2\2\u017b\u017c\7t\2\2\u017c\u017d\7q\2\2\u017d\u017e"+
		"\7n\2\2\u017e\u017f\7n\2\2\u017f\u0180\7g\2\2\u0180\u0181\7t\2\2\u0181"+
		"\30\3\2\2\2\u0182\u0183\7x\2\2\u0183\u0184\7k\2\2\u0184\u0185\7g\2\2\u0185"+
		"\u0186\7y\2\2\u0186\32\3\2\2\2\u0187\u0188\7h\2\2\u0188\u0189\7n\2\2\u0189"+
		"\u018a\7q\2\2\u018a\u018b\7y\2\2\u018b\34\3\2\2\2\u018c\u018d\7n\2\2\u018d"+
		"\u018e\7q\2\2\u018e\u018f\7e\2\2\u018f\u0190\7c\2\2\u0190\u0191\7n\2\2"+
		"\u0191\36\3\2\2\2\u0192\u0193\7u\2\2\u0193\u0194\7v\2\2\u0194\u0195\7"+
		"t\2\2\u0195\u0196\7g\2\2\u0196\u0197\7c\2\2\u0197\u0198\7o\2\2\u0198\u0199"+
		"\7k\2\2\u0199\u019a\7p\2\2\u019a\u019b\7i\2\2\u019b \3\2\2\2\u019c\u019d"+
		"\7t\2\2\u019d\u019e\7g\2\2\u019e\u019f\7r\2\2\u019f\u01a0\7n\2\2\u01a0"+
		"\u01a1\7k\2\2\u01a1\u01a2\7e\2\2\u01a2\u01a3\7c\2\2\u01a3\u01a4\7v\2\2"+
		"\u01a4\u01a5\7g\2\2\u01a5\u01a6\7f\2\2\u01a6\"\3\2\2\2\u01a7\u01a8\7r"+
		"\2\2\u01a8\u01a9\7t\2\2\u01a9\u01aa\7q\2\2\u01aa\u01ab\7r\2\2\u01ab\u01ac"+
		"\7g\2\2\u01ac\u01ad\7t\2\2\u01ad\u01ae\7v\2\2\u01ae\u01af\7k\2\2\u01af"+
		"\u01b0\7g\2\2\u01b0\u01b1\7u\2\2\u01b1$\3\2\2\2\u01b2\u01b3\7u\2\2\u01b3"+
		"\u01b4\7e\2\2\u01b4\u01b5\7j\2\2\u01b5\u01b6\7g\2\2\u01b6\u01b7\7o\2\2"+
		"\u01b7\u01b8\7c\2\2\u01b8&\3\2\2\2\u01b9\u01ba\7g\2\2\u01ba\u01bb\7x\2"+
		"\2\u01bb\u01bc\7g\2\2\u01bc\u01bd\7p\2\2\u01bd\u01be\7v\2\2\u01be(\3\2"+
		"\2\2\u01bf\u01c0\7e\2\2\u01c0\u01c1\7q\2\2\u01c1\u01c2\7o\2\2\u01c2\u01c3"+
		"\7o\2\2\u01c3\u01c4\7c\2\2\u01c4\u01c5\7p\2\2\u01c5\u01c6\7f\2\2\u01c6"+
		"*\3\2\2\2\u01c7\u01c8\7D\2\2\u01c8\u01c9\7{\2\2\u01c9\u01ca\7v\2\2\u01ca"+
		"\u01cb\7g\2\2\u01cb\u01cc\7H\2\2\u01cc\u01cd\7k\2\2\u01cd\u01ce\7g\2\2"+
		"\u01ce\u01cf\7n\2\2\u01cf\u01d0\7f\2\2\u01d0,\3\2\2\2\u01d1\u01d2\7U\2"+
		"\2\u01d2\u01d3\7v\2\2\u01d3\u01d4\7t\2\2\u01d4\u01d5\7k\2\2\u01d5\u01d6"+
		"\7p\2\2\u01d6\u01d7\7i\2\2\u01d7\u01d8\7H\2\2\u01d8\u01d9\7k\2\2\u01d9"+
		"\u01da\7g\2\2\u01da\u01db\7n\2\2\u01db\u01dc\7f\2\2\u01dc.\3\2\2\2\u01dd"+
		"\u01de\7D\2\2\u01de\u01df\7q\2\2\u01df\u01e0\7q\2\2\u01e0\u01e1\7n\2\2"+
		"\u01e1\u01e2\7g\2\2\u01e2\u01e3\7c\2\2\u01e3\u01e4\7p\2\2\u01e4\u01e5"+
		"\7H\2\2\u01e5\u01e6\7k\2\2\u01e6\u01e7\7g\2\2\u01e7\u01e8\7n\2\2\u01e8"+
		"\u01e9\7f\2\2\u01e9\60\3\2\2\2\u01ea\u01eb\7K\2\2\u01eb\u01ec\7p\2\2\u01ec"+
		"\u01ed\7v\2\2\u01ed\u01ee\7g\2\2\u01ee\u01ef\7i\2\2\u01ef\u01f0\7g\2\2"+
		"\u01f0\u01f1\7t\2\2\u01f1\u01f2\7H\2\2\u01f2\u01f3\7k\2\2\u01f3\u01f4"+
		"\7g\2\2\u01f4\u01f5\7n\2\2\u01f5\u01f6\7f\2\2\u01f6\62\3\2\2\2\u01f7\u01f8"+
		"\7P\2\2\u01f8\u01f9\7w\2\2\u01f9\u01fa\7o\2\2\u01fa\u01fb\7d\2\2\u01fb"+
		"\u01fc\7g\2\2\u01fc\u01fd\7t\2\2\u01fd\u01fe\7H\2\2\u01fe\u01ff\7k\2\2"+
		"\u01ff\u0200\7g\2\2\u0200\u0201\7n\2\2\u0201\u0202\7f\2\2\u0202\64\3\2"+
		"\2\2\u0203\u0204\7F\2\2\u0204\u0205\7c\2\2\u0205\u0206\7v\2\2\u0206\u0207"+
		"\7g\2\2\u0207\u0208\7H\2\2\u0208\u0209\7k\2\2\u0209\u020a\7g\2\2\u020a"+
		"\u020b\7n\2\2\u020b\u020c\7f\2\2\u020c\66\3\2\2\2\u020d\u020e\7F\2\2\u020e"+
		"\u020f\7c\2\2\u020f\u0210\7v\2\2\u0210\u0211\7g\2\2\u0211\u0212\7V\2\2"+
		"\u0212\u0213\7k\2\2\u0213\u0214\7o\2\2\u0214\u0215\7g\2\2\u0215\u0216"+
		"\7H\2\2\u0216\u0217\7k\2\2\u0217\u0218\7g\2\2\u0218\u0219\7n\2\2\u0219"+
		"\u021a\7f\2\2\u021a8\3\2\2\2\u021b\u021c\7V\2\2\u021c\u021d\7k\2\2\u021d"+
		"\u021e\7o\2\2\u021e\u021f\7g\2\2\u021f\u0220\7U\2\2\u0220\u0221\7v\2\2"+
		"\u0221\u0222\7c\2\2\u0222\u0223\7o\2\2\u0223\u0224\7r\2\2\u0224\u0225"+
		"\7H\2\2\u0225\u0226\7k\2\2\u0226\u0227\7g\2\2\u0227\u0228\7n\2\2\u0228"+
		"\u0229\7f\2\2\u0229:\3\2\2\2\u022a\u022b\7E\2\2\u022b\u022c\7q\2\2\u022c"+
		"\u022d\7p\2\2\u022d\u022e\7v\2\2\u022e\u022f\7g\2\2\u022f\u0230\7z\2\2"+
		"\u0230\u0231\7v\2\2\u0231\u0232\7H\2\2\u0232\u0233\7k\2\2\u0233\u0234"+
		"\7g\2\2\u0234\u0235\7n\2\2\u0235\u0236\7f\2\2\u0236<\3\2\2\2\u0237\u0238"+
		"\7O\2\2\u0238\u0239\7q\2\2\u0239\u023a\7f\2\2\u023a\u023b\7g\2\2\u023b"+
		"\u023c\7n\2\2\u023c\u023d\7H\2\2\u023d\u023e\7k\2\2\u023e\u023f\7g\2\2"+
		"\u023f\u0240\7n\2\2\u0240\u0241\7f\2\2\u0241>\3\2\2\2\u0242\u0243\7c\2"+
		"\2\u0243\u0244\7u\2\2\u0244\u0245\7u\2\2\u0245\u0246\7g\2\2\u0246\u0247"+
		"\7t\2\2\u0247\u0248\7v\2\2\u0248@\3\2\2\2\u0249\u024a\7t\2\2\u024a\u024b"+
		"\7g\2\2\u024b\u024c\7v\2\2\u024c\u024d\7w\2\2\u024d\u024e\7t\2\2\u024e"+
		"\u024f\7p\2\2\u024fB\3\2\2\2\u0250\u0251\7V\2\2\u0251\u0252\7t\2\2\u0252"+
		"\u0253\7w\2\2\u0253\u0254\7g\2\2\u0254D\3\2\2\2\u0255\u0256\7H\2\2\u0256"+
		"\u0257\7c\2\2\u0257\u0258\7n\2\2\u0258\u0259\7u\2\2\u0259\u025a\7g\2\2"+
		"\u025aF\3\2\2\2\u025b\u025c\7k\2\2\u025c\u025d\7h\2\2\u025dH\3\2\2\2\u025e"+
		"\u025f\7g\2\2\u025f\u0260\7n\2\2\u0260\u0261\7u\2\2\u0261\u0262\7g\2\2"+
		"\u0262\u0263\7\"\2\2\u0263\u0264\7k\2\2\u0264\u0265\7h\2\2\u0265J\3\2"+
		"\2\2\u0266\u0267\7g\2\2\u0267\u0268\7n\2\2\u0268\u0269\7u\2\2\u0269\u026a"+
		"\7g\2\2\u026aL\3\2\2\2\u026b\u026c\7h\2\2\u026c\u026d\7q\2\2\u026d\u026e"+
		"\7t\2\2\u026eN\3\2\2\2\u026f\u0270\7y\2\2\u0270\u0271\7j\2\2\u0271\u0272"+
		"\7k\2\2\u0272\u0273\7n\2\2\u0273\u0274\7g\2\2\u0274P\3\2\2\2\u0275\u0276"+
		"\7c\2\2\u0276\u0277\7p\2\2\u0277\u0278\7f\2\2\u0278R\3\2\2\2\u0279\u027a"+
		"\7p\2\2\u027a\u027b\7q\2\2\u027b\u027c\7v\2\2\u027cT\3\2\2\2\u027d\u027e"+
		"\7q\2\2\u027e\u027f\7t\2\2\u027fV\3\2\2\2\u0280\u0281\7k\2\2\u0281\u0282"+
		"\7p\2\2\u0282X\3\2\2\2\u0283\u0284\7k\2\2\u0284\u0285\7u\2\2\u0285Z\3"+
		"\2\2\2\u0286\u0287\7f\2\2\u0287\u0288\7g\2\2\u0288\u0289\7n\2\2\u0289"+
		"\u028a\7g\2\2\u028a\u028b\7v\2\2\u028b\u028c\7g\2\2\u028c\\\3\2\2\2\u028d"+
		"\u028e\7r\2\2\u028e\u028f\7c\2\2\u028f\u0290\7u\2\2\u0290\u0291\7u\2\2"+
		"\u0291^\3\2\2\2\u0292\u0293\7e\2\2\u0293\u0294\7q\2\2\u0294\u0295\7p\2"+
		"\2\u0295\u0296\7v\2\2\u0296\u0297\7k\2\2\u0297\u0298\7p\2\2\u0298\u0299"+
		"\7w\2\2\u0299\u029a\7g\2\2\u029a`\3\2\2\2\u029b\u029c\7d\2\2\u029c\u029d"+
		"\7t\2\2\u029d\u029e\7g\2\2\u029e\u029f\7c\2\2\u029f\u02a0\7m\2\2\u02a0"+
		"b\3\2\2\2\u02a1\u02a2\7p\2\2\u02a2\u02a3\7q\2\2\u02a3\u02a4\7p\2\2\u02a4"+
		"\u02a5\7g\2\2\u02a5d\3\2\2\2\u02a6\u02a7\6\63\2\2\u02a7\u02b3\5\u011d"+
		"\u008f\2\u02a8\u02aa\7\17\2\2\u02a9\u02a8\3\2\2\2\u02a9\u02aa\3\2\2\2"+
		"\u02aa\u02ab\3\2\2\2\u02ab\u02ae\7\f\2\2\u02ac\u02ae\7\17\2\2\u02ad\u02a9"+
		"\3\2\2\2\u02ad\u02ac\3\2\2\2\u02ae\u02b0\3\2\2\2\u02af\u02b1\5\u011d\u008f"+
		"\2\u02b0\u02af\3\2\2\2\u02b0\u02b1\3\2\2\2\u02b1\u02b3\3\2\2\2\u02b2\u02a6"+
		"\3\2\2\2\u02b2\u02ad\3\2\2\2\u02b3\u02b4\3\2\2\2\u02b4\u02b5\b\63\2\2"+
		"\u02b5f\3\2\2\2\u02b6\u02bb\5i\65\2\u02b7\u02bb\5k\66\2\u02b8\u02bb\5"+
		"m\67\2\u02b9\u02bb\5o8\2\u02ba\u02b6\3\2\2\2\u02ba\u02b7\3\2\2\2\u02ba"+
		"\u02b8\3\2\2\2\u02ba\u02b9\3\2\2\2\u02bbh\3\2\2\2\u02bc\u02be\5s:\2\u02bd"+
		"\u02bf\5q9\2\u02be\u02bd\3\2\2\2\u02be\u02bf\3\2\2\2\u02bfj\3\2\2\2\u02c0"+
		"\u02c2\5\177@\2\u02c1\u02c3\5q9\2\u02c2\u02c1\3\2\2\2\u02c2\u02c3\3\2"+
		"\2\2\u02c3l\3\2\2\2\u02c4\u02c6\5\u0087D\2\u02c5\u02c7\5q9\2\u02c6\u02c5"+
		"\3\2\2\2\u02c6\u02c7\3\2\2\2\u02c7n\3\2\2\2\u02c8\u02ca\5\u008fH\2\u02c9"+
		"\u02cb\5q9\2\u02ca\u02c9\3\2\2\2\u02ca\u02cb\3\2\2\2\u02cbp\3\2\2\2\u02cc"+
		"\u02cd\t\2\2\2\u02cdr\3\2\2\2\u02ce\u02d9\7\62\2\2\u02cf\u02d6\5y=\2\u02d0"+
		"\u02d2\5u;\2\u02d1\u02d0\3\2\2\2\u02d1\u02d2\3\2\2\2\u02d2\u02d7\3\2\2"+
		"\2\u02d3\u02d4\5}?\2\u02d4\u02d5\5u;\2\u02d5\u02d7\3\2\2\2\u02d6\u02d1"+
		"\3\2\2\2\u02d6\u02d3\3\2\2\2\u02d7\u02d9\3\2\2\2\u02d8\u02ce\3\2\2\2\u02d8"+
		"\u02cf\3\2\2\2\u02d9t\3\2\2\2\u02da\u02e2\5w<\2\u02db\u02dd\5{>\2\u02dc"+
		"\u02db\3\2\2\2\u02dd\u02e0\3\2\2\2\u02de\u02dc\3\2\2\2\u02de\u02df\3\2"+
		"\2\2\u02df\u02e1\3\2\2\2\u02e0\u02de\3\2\2\2\u02e1\u02e3\5w<\2\u02e2\u02de"+
		"\3\2\2\2\u02e2\u02e3\3\2\2\2\u02e3v\3\2\2\2\u02e4\u02e7\7\62\2\2\u02e5"+
		"\u02e7\5y=\2\u02e6\u02e4\3\2\2\2\u02e6\u02e5\3\2\2\2\u02e7x\3\2\2\2\u02e8"+
		"\u02e9\t\3\2\2\u02e9z\3\2\2\2\u02ea\u02ed\5w<\2\u02eb\u02ed\7a\2\2\u02ec"+
		"\u02ea\3\2\2\2\u02ec\u02eb\3\2\2\2\u02ed|\3\2\2\2\u02ee\u02f0\7a\2\2\u02ef"+
		"\u02ee\3\2\2\2\u02f0\u02f1\3\2\2\2\u02f1\u02ef\3\2\2\2\u02f1\u02f2\3\2"+
		"\2\2\u02f2~\3\2\2\2\u02f3\u02f4\7\62\2\2\u02f4\u02f5\t\4\2\2\u02f5\u02f6"+
		"\5\u0081A\2\u02f6\u0080\3\2\2\2\u02f7\u02ff\5\u0083B\2\u02f8\u02fa\5\u0085"+
		"C\2\u02f9\u02f8\3\2\2\2\u02fa\u02fd\3\2\2\2\u02fb\u02f9\3\2\2\2\u02fb"+
		"\u02fc\3\2\2\2\u02fc\u02fe\3\2\2\2\u02fd\u02fb\3\2\2\2\u02fe\u0300\5\u0083"+
		"B\2\u02ff\u02fb\3\2\2\2\u02ff\u0300\3\2\2\2\u0300\u0082\3\2\2\2\u0301"+
		"\u0302\t\5\2\2\u0302\u0084\3\2\2\2\u0303\u0306\5\u0083B\2\u0304\u0306"+
		"\7a\2\2\u0305\u0303\3\2\2\2\u0305\u0304\3\2\2\2\u0306\u0086\3\2\2\2\u0307"+
		"\u0309\7\62\2\2\u0308\u030a\5}?\2\u0309\u0308\3\2\2\2\u0309\u030a\3\2"+
		"\2\2\u030a\u030b\3\2\2\2\u030b\u030c\5\u0089E\2\u030c\u0088\3\2\2\2\u030d"+
		"\u0315\5\u008bF\2\u030e\u0310\5\u008dG\2\u030f\u030e\3\2\2\2\u0310\u0313"+
		"\3\2\2\2\u0311\u030f\3\2\2\2\u0311\u0312\3\2\2\2\u0312\u0314\3\2\2\2\u0313"+
		"\u0311\3\2\2\2\u0314\u0316\5\u008bF\2\u0315\u0311\3\2\2\2\u0315\u0316"+
		"\3\2\2\2\u0316\u008a\3\2\2\2\u0317\u0318\t\6\2\2\u0318\u008c\3\2\2\2\u0319"+
		"\u031c\5\u008bF\2\u031a\u031c\7a\2\2\u031b\u0319\3\2\2\2\u031b\u031a\3"+
		"\2\2\2\u031c\u008e\3\2\2\2\u031d\u031e\7\62\2\2\u031e\u031f\t\7\2\2\u031f"+
		"\u0320\5\u0091I\2\u0320\u0090\3\2\2\2\u0321\u0329\5\u0093J\2\u0322\u0324"+
		"\5\u0095K\2\u0323\u0322\3\2\2\2\u0324\u0327\3\2\2\2\u0325\u0323\3\2\2"+
		"\2\u0325\u0326\3\2\2\2\u0326\u0328\3\2\2\2\u0327\u0325\3\2\2\2\u0328\u032a"+
		"\5\u0093J\2\u0329\u0325\3\2\2\2\u0329\u032a\3\2\2\2\u032a\u0092\3\2\2"+
		"\2\u032b\u032c\t\b\2\2\u032c\u0094\3\2\2\2\u032d\u0330\5\u0093J\2\u032e"+
		"\u0330\7a\2\2\u032f\u032d\3\2\2\2\u032f\u032e\3\2\2\2\u0330\u0096\3\2"+
		"\2\2\u0331\u0334\5\u0099M\2\u0332\u0334\5\u00a5S\2\u0333\u0331\3\2\2\2"+
		"\u0333\u0332\3\2\2\2\u0334\u0098\3\2\2\2\u0335\u0336\5u;\2\u0336\u0338"+
		"\7\60\2\2\u0337\u0339\5u;\2\u0338\u0337\3\2\2\2\u0338\u0339\3\2\2\2\u0339"+
		"\u033b\3\2\2\2\u033a\u033c\5\u009bN\2\u033b\u033a\3\2\2\2\u033b\u033c"+
		"\3\2\2\2\u033c\u033e\3\2\2\2\u033d\u033f\5\u00a3R\2\u033e\u033d\3\2\2"+
		"\2\u033e\u033f\3\2\2\2\u033f\u0351\3\2\2\2\u0340\u0341\7\60\2\2\u0341"+
		"\u0343\5u;\2\u0342\u0344\5\u009bN\2\u0343\u0342\3\2\2\2\u0343\u0344\3"+
		"\2\2\2\u0344\u0346\3\2\2\2\u0345\u0347\5\u00a3R\2\u0346\u0345\3\2\2\2"+
		"\u0346\u0347\3\2\2\2\u0347\u0351\3\2\2\2\u0348\u0349\5u;\2\u0349\u034b"+
		"\5\u009bN\2\u034a\u034c\5\u00a3R\2\u034b\u034a\3\2\2\2\u034b\u034c\3\2"+
		"\2\2\u034c\u0351\3\2\2\2\u034d\u034e\5u;\2\u034e\u034f\5\u00a3R\2\u034f"+
		"\u0351\3\2\2\2\u0350\u0335\3\2\2\2\u0350\u0340\3\2\2\2\u0350\u0348\3\2"+
		"\2\2\u0350\u034d\3\2\2\2\u0351\u009a\3\2\2\2\u0352\u0353\5\u009dO\2\u0353"+
		"\u0354\5\u009fP\2\u0354\u009c\3\2\2\2\u0355\u0356\t\t\2\2\u0356\u009e"+
		"\3\2\2\2\u0357\u0359\5\u00a1Q\2\u0358\u0357\3\2\2\2\u0358\u0359\3\2\2"+
		"\2\u0359\u035a\3\2\2\2\u035a\u035b\5u;\2\u035b\u00a0\3\2\2\2\u035c\u035d"+
		"\t\n\2\2\u035d\u00a2\3\2\2\2\u035e\u035f\t\13\2\2\u035f\u00a4\3\2\2\2"+
		"\u0360\u0361\5\u00a7T\2\u0361\u0363\5\u00a9U\2\u0362\u0364\5\u00a3R\2"+
		"\u0363\u0362\3\2\2\2\u0363\u0364\3\2\2\2\u0364\u00a6\3\2\2\2\u0365\u0367"+
		"\5\177@\2\u0366\u0368\7\60\2\2\u0367\u0366\3\2\2\2\u0367\u0368\3\2\2\2"+
		"\u0368\u0371\3\2\2\2\u0369\u036a\7\62\2\2\u036a\u036c\t\4\2\2\u036b\u036d"+
		"\5\u0081A\2\u036c\u036b\3\2\2\2\u036c\u036d\3\2\2\2\u036d\u036e\3\2\2"+
		"\2\u036e\u036f\7\60\2\2\u036f\u0371\5\u0081A\2\u0370\u0365\3\2\2\2\u0370"+
		"\u0369\3\2\2\2\u0371\u00a8\3\2\2\2\u0372\u0373\5\u00abV\2\u0373\u0374"+
		"\5\u009fP\2\u0374\u00aa\3\2\2\2\u0375\u0376\t\f\2\2\u0376\u00ac\3\2\2"+
		"\2\u0377\u0378\7)\2\2\u0378\u0379\5\u00afX\2\u0379\u037a\7)\2\2\u037a"+
		"\u00ae\3\2\2\2\u037b\u037c\n\r\2\2\u037c\u00b0\3\2\2\2\u037d\u037f\7$"+
		"\2\2\u037e\u0380\5\u00b3Z\2\u037f\u037e\3\2\2\2\u037f\u0380\3\2\2\2\u0380"+
		"\u0381\3\2\2\2\u0381\u0382\7$\2\2\u0382\u00b2\3\2\2\2\u0383\u0385\5\u00b5"+
		"[\2\u0384\u0383\3\2\2\2\u0385\u0386\3\2\2\2\u0386\u0384\3\2\2\2\u0386"+
		"\u0387\3\2\2\2\u0387\u00b4\3\2\2\2\u0388\u0389\n\16\2\2\u0389\u00b6\3"+
		"\2\2\2\u038a\u038b\7^\2\2\u038b\u0396\5\u008bF\2\u038c\u038d\7^\2\2\u038d"+
		"\u038e\5\u008bF\2\u038e\u038f\5\u008bF\2\u038f\u0396\3\2\2\2\u0390\u0391"+
		"\7^\2\2\u0391\u0392\5\u00bb^\2\u0392\u0393\5\u008bF\2\u0393\u0394\5\u008b"+
		"F\2\u0394\u0396\3\2\2\2\u0395\u038a\3\2\2\2\u0395\u038c\3\2\2\2\u0395"+
		"\u0390\3\2\2\2\u0396\u00b8\3\2\2\2\u0397\u0398\7^\2\2\u0398\u0399\7w\2"+
		"\2\u0399\u039a\5\u0083B\2\u039a\u039b\5\u0083B\2\u039b\u039c\5\u0083B"+
		"\2\u039c\u039d\5\u0083B\2\u039d\u00ba\3\2\2\2\u039e\u039f\t\17\2\2\u039f"+
		"\u00bc\3\2\2\2\u03a0\u03a1\7p\2\2\u03a1\u03a2\7w\2\2\u03a2\u03a3\7n\2"+
		"\2\u03a3\u03a4\7n\2\2\u03a4\u00be\3\2\2\2\u03a5\u03a6\7*\2\2\u03a6\u00c0"+
		"\3\2\2\2\u03a7\u03a8\7+\2\2\u03a8\u00c2\3\2\2\2\u03a9\u03aa\7}\2\2\u03aa"+
		"\u00c4\3\2\2\2\u03ab\u03ac\7\177\2\2\u03ac\u00c6\3\2\2\2\u03ad\u03ae\7"+
		"]\2\2\u03ae\u00c8\3\2\2\2\u03af\u03b0\7_\2\2\u03b0\u00ca\3\2\2\2\u03b1"+
		"\u03b2\7=\2\2\u03b2\u00cc\3\2\2\2\u03b3\u03b4\7.\2\2\u03b4\u00ce\3\2\2"+
		"\2\u03b5\u03b6\7\60\2\2\u03b6\u00d0\3\2\2\2\u03b7\u03b8\7?\2\2\u03b8\u00d2"+
		"\3\2\2\2\u03b9\u03ba\7@\2\2\u03ba\u00d4\3\2\2\2\u03bb\u03bc\7>\2\2\u03bc"+
		"\u00d6\3\2\2\2\u03bd\u03be\7#\2\2\u03be\u00d8\3\2\2\2\u03bf\u03c0\7\u0080"+
		"\2\2\u03c0\u00da\3\2\2\2\u03c1\u03c2\7A\2\2\u03c2\u00dc\3\2\2\2\u03c3"+
		"\u03c4\7<\2\2\u03c4\u00de\3\2\2\2\u03c5\u03c6\7?\2\2\u03c6\u03c7\7?\2"+
		"\2\u03c7\u00e0\3\2\2\2\u03c8\u03c9\7>\2\2\u03c9\u03ca\7?\2\2\u03ca\u00e2"+
		"\3\2\2\2\u03cb\u03cc\7@\2\2\u03cc\u03cd\7?\2\2\u03cd\u00e4\3\2\2\2\u03ce"+
		"\u03cf\7#\2\2\u03cf\u03d0\7?\2\2\u03d0\u00e6\3\2\2\2\u03d1\u03d2\7(\2"+
		"\2\u03d2\u03d3\7(\2\2\u03d3\u00e8\3\2\2\2\u03d4\u03d5\7~\2\2\u03d5\u03d6"+
		"\7~\2\2\u03d6\u00ea\3\2\2\2\u03d7\u03d8\7-\2\2\u03d8\u03d9\7-\2\2\u03d9"+
		"\u00ec\3\2\2\2\u03da\u03db\7/\2\2\u03db\u03dc\7/\2\2\u03dc\u00ee\3\2\2"+
		"\2\u03dd\u03de\7-\2\2\u03de\u00f0\3\2\2\2\u03df\u03e0\7/\2\2\u03e0\u00f2"+
		"\3\2\2\2\u03e1\u03e2\7,\2\2\u03e2\u00f4\3\2\2\2\u03e3\u03e4\7\61\2\2\u03e4"+
		"\u00f6\3\2\2\2\u03e5\u03e6\7(\2\2\u03e6\u00f8\3\2\2\2\u03e7\u03e8\7~\2"+
		"\2\u03e8\u00fa\3\2\2\2\u03e9\u03ea\7`\2\2\u03ea\u00fc\3\2\2\2\u03eb\u03ec"+
		"\7\'\2\2\u03ec\u00fe\3\2\2\2\u03ed\u03ee\7-\2\2\u03ee\u03ef\7?\2\2\u03ef"+
		"\u0100\3\2\2\2\u03f0\u03f1\7/\2\2\u03f1\u03f2\7?\2\2\u03f2\u0102\3\2\2"+
		"\2\u03f3\u03f4\7,\2\2\u03f4\u03f5\7?\2\2\u03f5\u0104\3\2\2\2\u03f6\u03f7"+
		"\7\61\2\2\u03f7\u03f8\7?\2\2\u03f8\u0106\3\2\2\2\u03f9\u03fa\7(\2\2\u03fa"+
		"\u03fb\7?\2\2\u03fb\u0108\3\2\2\2\u03fc\u03fd\7~\2\2\u03fd\u03fe\7?\2"+
		"\2\u03fe\u010a\3\2\2\2\u03ff\u0400\7`\2\2\u0400\u0401\7?\2\2\u0401\u010c"+
		"\3\2\2\2\u0402\u0403\7\'\2\2\u0403\u0404\7?\2\2\u0404\u010e\3\2\2\2\u0405"+
		"\u0406\7>\2\2\u0406\u0407\7>\2\2\u0407\u0408\7?\2\2\u0408\u0110\3\2\2"+
		"\2\u0409\u040a\7@\2\2\u040a\u040b\7@\2\2\u040b\u040c\7?\2\2\u040c\u0112"+
		"\3\2\2\2\u040d\u040e\7@\2\2\u040e\u040f\7@\2\2\u040f\u0410\7@\2\2\u0410"+
		"\u0411\7?\2\2\u0411\u0114\3\2\2\2\u0412\u0416\5\u0117\u008c\2\u0413\u0415"+
		"\5\u0119\u008d\2\u0414\u0413\3\2\2\2\u0415\u0418\3\2\2\2\u0416\u0414\3"+
		"\2\2\2\u0416\u0417\3\2\2\2\u0417\u0116\3\2\2\2\u0418\u0416\3\2\2\2\u0419"+
		"\u0420\t\20\2\2\u041a\u041b\n\21\2\2\u041b\u0420\6\u008c\3\2\u041c\u041d"+
		"\t\22\2\2\u041d\u041e\t\23\2\2\u041e\u0420\6\u008c\4\2\u041f\u0419\3\2"+
		"\2\2\u041f\u041a\3\2\2\2\u041f\u041c\3\2\2\2\u0420\u0118\3\2\2\2\u0421"+
		"\u0428\t\24\2\2\u0422\u0423\n\21\2\2\u0423\u0428\6\u008d\5\2\u0424\u0425"+
		"\t\22\2\2\u0425\u0426\t\23\2\2\u0426\u0428\6\u008d\6\2\u0427\u0421\3\2"+
		"\2\2\u0427\u0422\3\2\2\2\u0427\u0424\3\2\2\2\u0428\u011a\3\2\2\2\u0429"+
		"\u042d\5\u011d\u008f\2\u042a\u042d\5\u011f\u0090\2\u042b\u042d\5\u0121"+
		"\u0091\2\u042c\u0429\3\2\2\2\u042c\u042a\3\2\2\2\u042c\u042b\3\2\2\2\u042d"+
		"\u042e\3\2\2\2\u042e\u042f\b\u008e\3\2\u042f\u011c\3\2\2\2\u0430\u0432"+
		"\t\25\2\2\u0431\u0430\3\2\2\2\u0432\u0433\3\2\2\2\u0433\u0431\3\2\2\2"+
		"\u0433\u0434\3\2\2\2\u0434\u011e\3\2\2\2\u0435\u0439\7%\2\2\u0436\u0438"+
		"\n\26\2\2\u0437\u0436\3\2\2\2\u0438\u043b\3\2\2\2\u0439\u0437\3\2\2\2"+
		"\u0439\u043a\3\2\2\2\u043a\u0120\3\2\2\2\u043b\u0439\3\2\2\2\u043c\u043e"+
		"\7^\2\2\u043d\u043f\5\u011d\u008f\2\u043e\u043d\3\2\2\2\u043e\u043f\3"+
		"\2\2\2\u043f\u0445\3\2\2\2\u0440\u0442\7\17\2\2\u0441\u0440\3\2\2\2\u0441"+
		"\u0442\3\2\2\2\u0442\u0443\3\2\2\2\u0443\u0446\7\f\2\2\u0444\u0446\7\17"+
		"\2\2\u0445\u0441\3\2\2\2\u0445\u0444\3\2\2\2\u0446\u0122\3\2\2\2\67\2"+
		"\u02a9\u02ad\u02b0\u02b2\u02ba\u02be\u02c2\u02c6\u02ca\u02d1\u02d6\u02d8"+
		"\u02de\u02e2\u02e6\u02ec\u02f1\u02fb\u02ff\u0305\u0309\u0311\u0315\u031b"+
		"\u0325\u0329\u032f\u0333\u0338\u033b\u033e\u0343\u0346\u034b\u0350\u0358"+
		"\u0363\u0367\u036c\u0370\u037f\u0386\u0395\u0416\u041f\u0427\u042c\u0433"+
		"\u0439\u043e\u0441\u0445\4\3\63\2\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
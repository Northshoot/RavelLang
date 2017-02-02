// Generated from /Users/lauril/workspace/01-ravel/RavelLang/Ravel.g4 by ANTLR 4.6

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
		T__17=18, T__18=19, T__19=20, T__20=21, MODEL=22, SPACE=23, CONTROLLER=24, 
		VIEW=25, FLOW=26, INTERFACE=27, DEF=28, EVENT=29, COMMAND=30, T_BYTE_FIELD=31, 
		T_STRING_FIELD=32, T_BOOLEAN_FIELD=33, T_INTEGER_FIELD=34, T_NUMBER_FIELD=35, 
		T_DATE_FIELD=36, T_DATE_TIME_FIELD=37, T_TIME_STAMP_FIELD=38, T_CONTEXT_FIELD=39, 
		T_MODEL_FIELD=40, ASSERT=41, RETURN=42, TRUE=43, FALSE=44, IF=45, ELIF=46, 
		ELSE=47, FOR=48, WHILE=49, AND=50, NOT=51, OR=52, IN=53, IS=54, DELETE=55, 
		PASS=56, CONTINUE=57, BREAK=58, NONE=59, NEWLINE=60, STRING_LITERAL=61, 
		DECIMAL_INTEGER=62, FLOAT_NUMBER=63, NullLiteral=64, ASSIGN=65, GT=66, 
		LT=67, BANG=68, TILDE=69, QUESTION=70, COLON=71, EQUAL=72, LE=73, GE=74, 
		NOTEQUAL=75, AND_S=76, OR_S=77, INC=78, DEC=79, ADD=80, SUB=81, MUL=82, 
		DIV=83, BITAND=84, BITOR=85, CARET=86, MOD=87, ADD_ASSIGN=88, SUB_ASSIGN=89, 
		MUL_ASSIGN=90, DIV_ASSIGN=91, AND_ASSIGN=92, OR_ASSIGN=93, XOR_ASSIGN=94, 
		MOD_ASSIGN=95, LSHIFT_ASSIGN=96, RSHIFT_ASSIGN=97, URSHIFT_ASSIGN=98, 
		Identifier=99, SKIP_=100;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
		"T__17", "T__18", "T__19", "T__20", "MODEL", "SPACE", "CONTROLLER", "VIEW", 
		"FLOW", "INTERFACE", "DEF", "EVENT", "COMMAND", "T_BYTE_FIELD", "T_STRING_FIELD", 
		"T_BOOLEAN_FIELD", "T_INTEGER_FIELD", "T_NUMBER_FIELD", "T_DATE_FIELD", 
		"T_DATE_TIME_FIELD", "T_TIME_STAMP_FIELD", "T_CONTEXT_FIELD", "T_MODEL_FIELD", 
		"ASSERT", "RETURN", "TRUE", "FALSE", "IF", "ELIF", "ELSE", "FOR", "WHILE", 
		"AND", "NOT", "OR", "IN", "IS", "DELETE", "PASS", "CONTINUE", "BREAK", 
		"NONE", "NEWLINE", "STRING_LITERAL", "STRING_ESCAPE_SEQ", "DECIMAL_INTEGER", 
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
		"'interfaces:'", "'implementation:'", "'configuration:'", "'local'", "'streaming'", 
		"'replicated'", "'properties:'", "'schema:'", "'.'", "'['", "']'", "'**'", 
		"'//'", "'<<'", "'>>'", "'model'", "'space'", "'controller'", "'view'", 
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
		null, null, null, null, null, null, null, null, null, null, "MODEL", "SPACE", 
		"CONTROLLER", "VIEW", "FLOW", "INTERFACE", "DEF", "EVENT", "COMMAND", 
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
		case 59:
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
		case 59:
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2f\u0369\b\1\4\2\t"+
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
		"k\4l\tl\4m\tm\4n\tn\4o\to\4p\tp\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3"+
		"\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\24\3\25"+
		"\3\25\3\25\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30"+
		"\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31"+
		"\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36"+
		"\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3"+
		" \3 \3 \3 \3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\""+
		"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3"+
		"$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3&\3"+
		"&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'"+
		"\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3(\3(\3(\3(\3(\3(\3(\3(\3(\3"+
		")\3)\3)\3)\3)\3)\3)\3)\3)\3)\3)\3*\3*\3*\3*\3*\3*\3*\3+\3+\3+\3+\3+\3"+
		"+\3+\3,\3,\3,\3,\3,\3-\3-\3-\3-\3-\3-\3.\3.\3.\3/\3/\3/\3/\3/\3/\3/\3"+
		"/\3\60\3\60\3\60\3\60\3\60\3\61\3\61\3\61\3\61\3\62\3\62\3\62\3\62\3\62"+
		"\3\62\3\63\3\63\3\63\3\63\3\64\3\64\3\64\3\64\3\65\3\65\3\65\3\66\3\66"+
		"\3\66\3\67\3\67\3\67\38\38\38\38\38\38\38\39\39\39\39\39\3:\3:\3:\3:\3"+
		":\3:\3:\3:\3:\3;\3;\3;\3;\3;\3;\3<\3<\3<\3<\3<\3=\3=\3=\5=\u0292\n=\3"+
		"=\3=\5=\u0296\n=\3=\5=\u0299\n=\5=\u029b\n=\3=\3=\3>\3>\3>\7>\u02a2\n"+
		">\f>\16>\u02a5\13>\3>\3>\3>\3>\7>\u02ab\n>\f>\16>\u02ae\13>\3>\5>\u02b1"+
		"\n>\3?\3?\3?\3@\3@\7@\u02b8\n@\f@\16@\u02bb\13@\3@\6@\u02be\n@\r@\16@"+
		"\u02bf\5@\u02c2\n@\3A\3A\3B\3B\3C\3C\3D\5D\u02cb\nD\3D\3D\3D\3D\5D\u02d1"+
		"\nD\3E\6E\u02d4\nE\rE\16E\u02d5\3F\3F\6F\u02da\nF\rF\16F\u02db\3G\3G\3"+
		"G\3G\3G\3H\3H\3I\3I\3J\3J\3K\3K\3L\3L\3M\3M\3N\3N\3O\3O\3O\3P\3P\3P\3"+
		"Q\3Q\3Q\3R\3R\3R\3S\3S\3S\3T\3T\3T\3U\3U\3U\3V\3V\3V\3W\3W\3X\3X\3Y\3"+
		"Y\3Z\3Z\3[\3[\3\\\3\\\3]\3]\3^\3^\3_\3_\3_\3`\3`\3`\3a\3a\3a\3b\3b\3b"+
		"\3c\3c\3c\3d\3d\3d\3e\3e\3e\3f\3f\3f\3g\3g\3g\3g\3h\3h\3h\3h\3i\3i\3i"+
		"\3i\3i\3j\3j\7j\u0340\nj\fj\16j\u0343\13j\3k\5k\u0346\nk\3l\3l\5l\u034a"+
		"\nl\3m\3m\3m\5m\u034f\nm\3m\3m\3n\6n\u0354\nn\rn\16n\u0355\3o\3o\7o\u035a"+
		"\no\fo\16o\u035d\13o\3p\3p\5p\u0361\np\3p\5p\u0364\np\3p\3p\5p\u0368\n"+
		"p\2\2q\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17"+
		"\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\35"+
		"9\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g\65i\66"+
		"k\67m8o9q:s;u<w=y>{?}\2\177@\u0081\2\u0083\2\u0085A\u0087\2\u0089\2\u008b"+
		"\2\u008dB\u008fC\u0091D\u0093E\u0095F\u0097G\u0099H\u009bI\u009dJ\u009f"+
		"K\u00a1L\u00a3M\u00a5N\u00a7O\u00a9P\u00abQ\u00adR\u00afS\u00b1T\u00b3"+
		"U\u00b5V\u00b7W\u00b9X\u00bbY\u00bdZ\u00bf[\u00c1\\\u00c3]\u00c5^\u00c7"+
		"_\u00c9`\u00cba\u00cdb\u00cfc\u00d1d\u00d3e\u00d5\2\u00d7\2\u00d9f\u00db"+
		"\2\u00dd\2\u00df\2\3\2\t\6\2\f\f\17\17))^^\6\2\f\f\17\17$$^^\3\2\63;\3"+
		"\2\62;\5\2C\\aac|\4\2\13\13\"\"\4\2\f\f\17\17\u0376\2\3\3\2\2\2\2\5\3"+
		"\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2"+
		"\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3"+
		"\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'"+
		"\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63"+
		"\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2"+
		"?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3"+
		"\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2"+
		"\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2"+
		"e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3"+
		"\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2\177\3"+
		"\2\2\2\2\u0085\3\2\2\2\2\u008d\3\2\2\2\2\u008f\3\2\2\2\2\u0091\3\2\2\2"+
		"\2\u0093\3\2\2\2\2\u0095\3\2\2\2\2\u0097\3\2\2\2\2\u0099\3\2\2\2\2\u009b"+
		"\3\2\2\2\2\u009d\3\2\2\2\2\u009f\3\2\2\2\2\u00a1\3\2\2\2\2\u00a3\3\2\2"+
		"\2\2\u00a5\3\2\2\2\2\u00a7\3\2\2\2\2\u00a9\3\2\2\2\2\u00ab\3\2\2\2\2\u00ad"+
		"\3\2\2\2\2\u00af\3\2\2\2\2\u00b1\3\2\2\2\2\u00b3\3\2\2\2\2\u00b5\3\2\2"+
		"\2\2\u00b7\3\2\2\2\2\u00b9\3\2\2\2\2\u00bb\3\2\2\2\2\u00bd\3\2\2\2\2\u00bf"+
		"\3\2\2\2\2\u00c1\3\2\2\2\2\u00c3\3\2\2\2\2\u00c5\3\2\2\2\2\u00c7\3\2\2"+
		"\2\2\u00c9\3\2\2\2\2\u00cb\3\2\2\2\2\u00cd\3\2\2\2\2\u00cf\3\2\2\2\2\u00d1"+
		"\3\2\2\2\2\u00d3\3\2\2\2\2\u00d9\3\2\2\2\3\u00e1\3\2\2\2\5\u00eb\3\2\2"+
		"\2\7\u00f3\3\2\2\2\t\u00f5\3\2\2\2\13\u00f7\3\2\2\2\r\u00f9\3\2\2\2\17"+
		"\u0106\3\2\2\2\21\u0112\3\2\2\2\23\u0122\3\2\2\2\25\u0131\3\2\2\2\27\u0137"+
		"\3\2\2\2\31\u0141\3\2\2\2\33\u014c\3\2\2\2\35\u0158\3\2\2\2\37\u0160\3"+
		"\2\2\2!\u0162\3\2\2\2#\u0164\3\2\2\2%\u0166\3\2\2\2\'\u0169\3\2\2\2)\u016c"+
		"\3\2\2\2+\u016f\3\2\2\2-\u0172\3\2\2\2/\u0178\3\2\2\2\61\u017e\3\2\2\2"+
		"\63\u0189\3\2\2\2\65\u018e\3\2\2\2\67\u0193\3\2\2\29\u019d\3\2\2\2;\u01a1"+
		"\3\2\2\2=\u01a7\3\2\2\2?\u01af\3\2\2\2A\u01b9\3\2\2\2C\u01c5\3\2\2\2E"+
		"\u01d2\3\2\2\2G\u01df\3\2\2\2I\u01eb\3\2\2\2K\u01f5\3\2\2\2M\u0203\3\2"+
		"\2\2O\u0212\3\2\2\2Q\u021f\3\2\2\2S\u022a\3\2\2\2U\u0231\3\2\2\2W\u0238"+
		"\3\2\2\2Y\u023d\3\2\2\2[\u0243\3\2\2\2]\u0246\3\2\2\2_\u024e\3\2\2\2a"+
		"\u0253\3\2\2\2c\u0257\3\2\2\2e\u025d\3\2\2\2g\u0261\3\2\2\2i\u0265\3\2"+
		"\2\2k\u0268\3\2\2\2m\u026b\3\2\2\2o\u026e\3\2\2\2q\u0275\3\2\2\2s\u027a"+
		"\3\2\2\2u\u0283\3\2\2\2w\u0289\3\2\2\2y\u029a\3\2\2\2{\u02b0\3\2\2\2}"+
		"\u02b2\3\2\2\2\177\u02c1\3\2\2\2\u0081\u02c3\3\2\2\2\u0083\u02c5\3\2\2"+
		"\2\u0085\u02c7\3\2\2\2\u0087\u02d0\3\2\2\2\u0089\u02d3\3\2\2\2\u008b\u02d7"+
		"\3\2\2\2\u008d\u02dd\3\2\2\2\u008f\u02e2\3\2\2\2\u0091\u02e4\3\2\2\2\u0093"+
		"\u02e6\3\2\2\2\u0095\u02e8\3\2\2\2\u0097\u02ea\3\2\2\2\u0099\u02ec\3\2"+
		"\2\2\u009b\u02ee\3\2\2\2\u009d\u02f0\3\2\2\2\u009f\u02f3\3\2\2\2\u00a1"+
		"\u02f6\3\2\2\2\u00a3\u02f9\3\2\2\2\u00a5\u02fc\3\2\2\2\u00a7\u02ff\3\2"+
		"\2\2\u00a9\u0302\3\2\2\2\u00ab\u0305\3\2\2\2\u00ad\u0308\3\2\2\2\u00af"+
		"\u030a\3\2\2\2\u00b1\u030c\3\2\2\2\u00b3\u030e\3\2\2\2\u00b5\u0310\3\2"+
		"\2\2\u00b7\u0312\3\2\2\2\u00b9\u0314\3\2\2\2\u00bb\u0316\3\2\2\2\u00bd"+
		"\u0318\3\2\2\2\u00bf\u031b\3\2\2\2\u00c1\u031e\3\2\2\2\u00c3\u0321\3\2"+
		"\2\2\u00c5\u0324\3\2\2\2\u00c7\u0327\3\2\2\2\u00c9\u032a\3\2\2\2\u00cb"+
		"\u032d\3\2\2\2\u00cd\u0330\3\2\2\2\u00cf\u0334\3\2\2\2\u00d1\u0338\3\2"+
		"\2\2\u00d3\u033d\3\2\2\2\u00d5\u0345\3\2\2\2\u00d7\u0349\3\2\2\2\u00d9"+
		"\u034e\3\2\2\2\u00db\u0353\3\2\2\2\u00dd\u0357\3\2\2\2\u00df\u035e\3\2"+
		"\2\2\u00e1\u00e2\7r\2\2\u00e2\u00e3\7n\2\2\u00e3\u00e4\7c\2\2\u00e4\u00e5"+
		"\7v\2\2\u00e5\u00e6\7h\2\2\u00e6\u00e7\7q\2\2\u00e7\u00e8\7t\2\2\u00e8"+
		"\u00e9\7o\2\2\u00e9\u00ea\7<\2\2\u00ea\4\3\2\2\2\u00eb\u00ec\7o\2\2\u00ec"+
		"\u00ed\7q\2\2\u00ed\u00ee\7f\2\2\u00ee\u00ef\7g\2\2\u00ef\u00f0\7n\2\2"+
		"\u00f0\u00f1\7u\2\2\u00f1\u00f2\7<\2\2\u00f2\6\3\2\2\2\u00f3\u00f4\7*"+
		"\2\2\u00f4\b\3\2\2\2\u00f5\u00f6\7+\2\2\u00f6\n\3\2\2\2\u00f7\u00f8\7"+
		".\2\2\u00f8\f\3\2\2\2\u00f9\u00fa\7e\2\2\u00fa\u00fb\7q\2\2\u00fb\u00fc"+
		"\7p\2\2\u00fc\u00fd\7v\2\2\u00fd\u00fe\7t\2\2\u00fe\u00ff\7q\2\2\u00ff"+
		"\u0100\7n\2\2\u0100\u0101\7n\2\2\u0101\u0102\7g\2\2\u0102\u0103\7t\2\2"+
		"\u0103\u0104\7u\2\2\u0104\u0105\7<\2\2\u0105\16\3\2\2\2\u0106\u0107\7"+
		"k\2\2\u0107\u0108\7p\2\2\u0108\u0109\7v\2\2\u0109\u010a\7g\2\2\u010a\u010b"+
		"\7t\2\2\u010b\u010c\7h\2\2\u010c\u010d\7c\2\2\u010d\u010e\7e\2\2\u010e"+
		"\u010f\7g\2\2\u010f\u0110\7u\2\2\u0110\u0111\7<\2\2\u0111\20\3\2\2\2\u0112"+
		"\u0113\7k\2\2\u0113\u0114\7o\2\2\u0114\u0115\7r\2\2\u0115\u0116\7n\2\2"+
		"\u0116\u0117\7g\2\2\u0117\u0118\7o\2\2\u0118\u0119\7g\2\2\u0119\u011a"+
		"\7p\2\2\u011a\u011b\7v\2\2\u011b\u011c\7c\2\2\u011c\u011d\7v\2\2\u011d"+
		"\u011e\7k\2\2\u011e\u011f\7q\2\2\u011f\u0120\7p\2\2\u0120\u0121\7<\2\2"+
		"\u0121\22\3\2\2\2\u0122\u0123\7e\2\2\u0123\u0124\7q\2\2\u0124\u0125\7"+
		"p\2\2\u0125\u0126\7h\2\2\u0126\u0127\7k\2\2\u0127\u0128\7i\2\2\u0128\u0129"+
		"\7w\2\2\u0129\u012a\7t\2\2\u012a\u012b\7c\2\2\u012b\u012c\7v\2\2\u012c"+
		"\u012d\7k\2\2\u012d\u012e\7q\2\2\u012e\u012f\7p\2\2\u012f\u0130\7<\2\2"+
		"\u0130\24\3\2\2\2\u0131\u0132\7n\2\2\u0132\u0133\7q\2\2\u0133\u0134\7"+
		"e\2\2\u0134\u0135\7c\2\2\u0135\u0136\7n\2\2\u0136\26\3\2\2\2\u0137\u0138"+
		"\7u\2\2\u0138\u0139\7v\2\2\u0139\u013a\7t\2\2\u013a\u013b\7g\2\2\u013b"+
		"\u013c\7c\2\2\u013c\u013d\7o\2\2\u013d\u013e\7k\2\2\u013e\u013f\7p\2\2"+
		"\u013f\u0140\7i\2\2\u0140\30\3\2\2\2\u0141\u0142\7t\2\2\u0142\u0143\7"+
		"g\2\2\u0143\u0144\7r\2\2\u0144\u0145\7n\2\2\u0145\u0146\7k\2\2\u0146\u0147"+
		"\7e\2\2\u0147\u0148\7c\2\2\u0148\u0149\7v\2\2\u0149\u014a\7g\2\2\u014a"+
		"\u014b\7f\2\2\u014b\32\3\2\2\2\u014c\u014d\7r\2\2\u014d\u014e\7t\2\2\u014e"+
		"\u014f\7q\2\2\u014f\u0150\7r\2\2\u0150\u0151\7g\2\2\u0151\u0152\7t\2\2"+
		"\u0152\u0153\7v\2\2\u0153\u0154\7k\2\2\u0154\u0155\7g\2\2\u0155\u0156"+
		"\7u\2\2\u0156\u0157\7<\2\2\u0157\34\3\2\2\2\u0158\u0159\7u\2\2\u0159\u015a"+
		"\7e\2\2\u015a\u015b\7j\2\2\u015b\u015c\7g\2\2\u015c\u015d\7o\2\2\u015d"+
		"\u015e\7c\2\2\u015e\u015f\7<\2\2\u015f\36\3\2\2\2\u0160\u0161\7\60\2\2"+
		"\u0161 \3\2\2\2\u0162\u0163\7]\2\2\u0163\"\3\2\2\2\u0164\u0165\7_\2\2"+
		"\u0165$\3\2\2\2\u0166\u0167\7,\2\2\u0167\u0168\7,\2\2\u0168&\3\2\2\2\u0169"+
		"\u016a\7\61\2\2\u016a\u016b\7\61\2\2\u016b(\3\2\2\2\u016c\u016d\7>\2\2"+
		"\u016d\u016e\7>\2\2\u016e*\3\2\2\2\u016f\u0170\7@\2\2\u0170\u0171\7@\2"+
		"\2\u0171,\3\2\2\2\u0172\u0173\7o\2\2\u0173\u0174\7q\2\2\u0174\u0175\7"+
		"f\2\2\u0175\u0176\7g\2\2\u0176\u0177\7n\2\2\u0177.\3\2\2\2\u0178\u0179"+
		"\7u\2\2\u0179\u017a\7r\2\2\u017a\u017b\7c\2\2\u017b\u017c\7e\2\2\u017c"+
		"\u017d\7g\2\2\u017d\60\3\2\2\2\u017e\u017f\7e\2\2\u017f\u0180\7q\2\2\u0180"+
		"\u0181\7p\2\2\u0181\u0182\7v\2\2\u0182\u0183\7t\2\2\u0183\u0184\7q\2\2"+
		"\u0184\u0185\7n\2\2\u0185\u0186\7n\2\2\u0186\u0187\7g\2\2\u0187\u0188"+
		"\7t\2\2\u0188\62\3\2\2\2\u0189\u018a\7x\2\2\u018a\u018b\7k\2\2\u018b\u018c"+
		"\7g\2\2\u018c\u018d\7y\2\2\u018d\64\3\2\2\2\u018e\u018f\7h\2\2\u018f\u0190"+
		"\7n\2\2\u0190\u0191\7q\2\2\u0191\u0192\7y\2\2\u0192\66\3\2\2\2\u0193\u0194"+
		"\7k\2\2\u0194\u0195\7p\2\2\u0195\u0196\7v\2\2\u0196\u0197\7g\2\2\u0197"+
		"\u0198\7t\2\2\u0198\u0199\7h\2\2\u0199\u019a\7c\2\2\u019a\u019b\7e\2\2"+
		"\u019b\u019c\7g\2\2\u019c8\3\2\2\2\u019d\u019e\7f\2\2\u019e\u019f\7g\2"+
		"\2\u019f\u01a0\7h\2\2\u01a0:\3\2\2\2\u01a1\u01a2\7g\2\2\u01a2\u01a3\7"+
		"x\2\2\u01a3\u01a4\7g\2\2\u01a4\u01a5\7p\2\2\u01a5\u01a6\7v\2\2\u01a6<"+
		"\3\2\2\2\u01a7\u01a8\7e\2\2\u01a8\u01a9\7q\2\2\u01a9\u01aa\7o\2\2\u01aa"+
		"\u01ab\7o\2\2\u01ab\u01ac\7c\2\2\u01ac\u01ad\7p\2\2\u01ad\u01ae\7f\2\2"+
		"\u01ae>\3\2\2\2\u01af\u01b0\7D\2\2\u01b0\u01b1\7{\2\2\u01b1\u01b2\7v\2"+
		"\2\u01b2\u01b3\7g\2\2\u01b3\u01b4\7H\2\2\u01b4\u01b5\7k\2\2\u01b5\u01b6"+
		"\7g\2\2\u01b6\u01b7\7n\2\2\u01b7\u01b8\7f\2\2\u01b8@\3\2\2\2\u01b9\u01ba"+
		"\7U\2\2\u01ba\u01bb\7v\2\2\u01bb\u01bc\7t\2\2\u01bc\u01bd\7k\2\2\u01bd"+
		"\u01be\7p\2\2\u01be\u01bf\7i\2\2\u01bf\u01c0\7H\2\2\u01c0\u01c1\7k\2\2"+
		"\u01c1\u01c2\7g\2\2\u01c2\u01c3\7n\2\2\u01c3\u01c4\7f\2\2\u01c4B\3\2\2"+
		"\2\u01c5\u01c6\7D\2\2\u01c6\u01c7\7q\2\2\u01c7\u01c8\7q\2\2\u01c8\u01c9"+
		"\7n\2\2\u01c9\u01ca\7g\2\2\u01ca\u01cb\7c\2\2\u01cb\u01cc\7p\2\2\u01cc"+
		"\u01cd\7H\2\2\u01cd\u01ce\7k\2\2\u01ce\u01cf\7g\2\2\u01cf\u01d0\7n\2\2"+
		"\u01d0\u01d1\7f\2\2\u01d1D\3\2\2\2\u01d2\u01d3\7K\2\2\u01d3\u01d4\7p\2"+
		"\2\u01d4\u01d5\7v\2\2\u01d5\u01d6\7g\2\2\u01d6\u01d7\7i\2\2\u01d7\u01d8"+
		"\7g\2\2\u01d8\u01d9\7t\2\2\u01d9\u01da\7H\2\2\u01da\u01db\7k\2\2\u01db"+
		"\u01dc\7g\2\2\u01dc\u01dd\7n\2\2\u01dd\u01de\7f\2\2\u01deF\3\2\2\2\u01df"+
		"\u01e0\7P\2\2\u01e0\u01e1\7w\2\2\u01e1\u01e2\7o\2\2\u01e2\u01e3\7d\2\2"+
		"\u01e3\u01e4\7g\2\2\u01e4\u01e5\7t\2\2\u01e5\u01e6\7H\2\2\u01e6\u01e7"+
		"\7k\2\2\u01e7\u01e8\7g\2\2\u01e8\u01e9\7n\2\2\u01e9\u01ea\7f\2\2\u01ea"+
		"H\3\2\2\2\u01eb\u01ec\7F\2\2\u01ec\u01ed\7c\2\2\u01ed\u01ee\7v\2\2\u01ee"+
		"\u01ef\7g\2\2\u01ef\u01f0\7H\2\2\u01f0\u01f1\7k\2\2\u01f1\u01f2\7g\2\2"+
		"\u01f2\u01f3\7n\2\2\u01f3\u01f4\7f\2\2\u01f4J\3\2\2\2\u01f5\u01f6\7F\2"+
		"\2\u01f6\u01f7\7c\2\2\u01f7\u01f8\7v\2\2\u01f8\u01f9\7g\2\2\u01f9\u01fa"+
		"\7V\2\2\u01fa\u01fb\7k\2\2\u01fb\u01fc\7o\2\2\u01fc\u01fd\7g\2\2\u01fd"+
		"\u01fe\7H\2\2\u01fe\u01ff\7k\2\2\u01ff\u0200\7g\2\2\u0200\u0201\7n\2\2"+
		"\u0201\u0202\7f\2\2\u0202L\3\2\2\2\u0203\u0204\7V\2\2\u0204\u0205\7k\2"+
		"\2\u0205\u0206\7o\2\2\u0206\u0207\7g\2\2\u0207\u0208\7U\2\2\u0208\u0209"+
		"\7v\2\2\u0209\u020a\7c\2\2\u020a\u020b\7o\2\2\u020b\u020c\7r\2\2\u020c"+
		"\u020d\7H\2\2\u020d\u020e\7k\2\2\u020e\u020f\7g\2\2\u020f\u0210\7n\2\2"+
		"\u0210\u0211\7f\2\2\u0211N\3\2\2\2\u0212\u0213\7E\2\2\u0213\u0214\7q\2"+
		"\2\u0214\u0215\7p\2\2\u0215\u0216\7v\2\2\u0216\u0217\7g\2\2\u0217\u0218"+
		"\7z\2\2\u0218\u0219\7v\2\2\u0219\u021a\7H\2\2\u021a\u021b\7k\2\2\u021b"+
		"\u021c\7g\2\2\u021c\u021d\7n\2\2\u021d\u021e\7f\2\2\u021eP\3\2\2\2\u021f"+
		"\u0220\7O\2\2\u0220\u0221\7q\2\2\u0221\u0222\7f\2\2\u0222\u0223\7g\2\2"+
		"\u0223\u0224\7n\2\2\u0224\u0225\7H\2\2\u0225\u0226\7k\2\2\u0226\u0227"+
		"\7g\2\2\u0227\u0228\7n\2\2\u0228\u0229\7f\2\2\u0229R\3\2\2\2\u022a\u022b"+
		"\7c\2\2\u022b\u022c\7u\2\2\u022c\u022d\7u\2\2\u022d\u022e\7g\2\2\u022e"+
		"\u022f\7t\2\2\u022f\u0230\7v\2\2\u0230T\3\2\2\2\u0231\u0232\7t\2\2\u0232"+
		"\u0233\7g\2\2\u0233\u0234\7v\2\2\u0234\u0235\7w\2\2\u0235\u0236\7t\2\2"+
		"\u0236\u0237\7p\2\2\u0237V\3\2\2\2\u0238\u0239\7V\2\2\u0239\u023a\7t\2"+
		"\2\u023a\u023b\7w\2\2\u023b\u023c\7g\2\2\u023cX\3\2\2\2\u023d\u023e\7"+
		"H\2\2\u023e\u023f\7c\2\2\u023f\u0240\7n\2\2\u0240\u0241\7u\2\2\u0241\u0242"+
		"\7g\2\2\u0242Z\3\2\2\2\u0243\u0244\7k\2\2\u0244\u0245\7h\2\2\u0245\\\3"+
		"\2\2\2\u0246\u0247\7g\2\2\u0247\u0248\7n\2\2\u0248\u0249\7u\2\2\u0249"+
		"\u024a\7g\2\2\u024a\u024b\7\"\2\2\u024b\u024c\7k\2\2\u024c\u024d\7h\2"+
		"\2\u024d^\3\2\2\2\u024e\u024f\7g\2\2\u024f\u0250\7n\2\2\u0250\u0251\7"+
		"u\2\2\u0251\u0252\7g\2\2\u0252`\3\2\2\2\u0253\u0254\7h\2\2\u0254\u0255"+
		"\7q\2\2\u0255\u0256\7t\2\2\u0256b\3\2\2\2\u0257\u0258\7y\2\2\u0258\u0259"+
		"\7j\2\2\u0259\u025a\7k\2\2\u025a\u025b\7n\2\2\u025b\u025c\7g\2\2\u025c"+
		"d\3\2\2\2\u025d\u025e\7c\2\2\u025e\u025f\7p\2\2\u025f\u0260\7f\2\2\u0260"+
		"f\3\2\2\2\u0261\u0262\7p\2\2\u0262\u0263\7q\2\2\u0263\u0264\7v\2\2\u0264"+
		"h\3\2\2\2\u0265\u0266\7q\2\2\u0266\u0267\7t\2\2\u0267j\3\2\2\2\u0268\u0269"+
		"\7k\2\2\u0269\u026a\7p\2\2\u026al\3\2\2\2\u026b\u026c\7k\2\2\u026c\u026d"+
		"\7u\2\2\u026dn\3\2\2\2\u026e\u026f\7f\2\2\u026f\u0270\7g\2\2\u0270\u0271"+
		"\7n\2\2\u0271\u0272\7g\2\2\u0272\u0273\7v\2\2\u0273\u0274\7g\2\2\u0274"+
		"p\3\2\2\2\u0275\u0276\7r\2\2\u0276\u0277\7c\2\2\u0277\u0278\7u\2\2\u0278"+
		"\u0279\7u\2\2\u0279r\3\2\2\2\u027a\u027b\7e\2\2\u027b\u027c\7q\2\2\u027c"+
		"\u027d\7p\2\2\u027d\u027e\7v\2\2\u027e\u027f\7k\2\2\u027f\u0280\7p\2\2"+
		"\u0280\u0281\7w\2\2\u0281\u0282\7g\2\2\u0282t\3\2\2\2\u0283\u0284\7d\2"+
		"\2\u0284\u0285\7t\2\2\u0285\u0286\7g\2\2\u0286\u0287\7c\2\2\u0287\u0288"+
		"\7m\2\2\u0288v\3\2\2\2\u0289\u028a\7p\2\2\u028a\u028b\7q\2\2\u028b\u028c"+
		"\7p\2\2\u028c\u028d\7g\2\2\u028dx\3\2\2\2\u028e\u028f\6=\2\2\u028f\u029b"+
		"\5\u00dbn\2\u0290\u0292\7\17\2\2\u0291\u0290\3\2\2\2\u0291\u0292\3\2\2"+
		"\2\u0292\u0293\3\2\2\2\u0293\u0296\7\f\2\2\u0294\u0296\7\17\2\2\u0295"+
		"\u0291\3\2\2\2\u0295\u0294\3\2\2\2\u0296\u0298\3\2\2\2\u0297\u0299\5\u00db"+
		"n\2\u0298\u0297\3\2\2\2\u0298\u0299\3\2\2\2\u0299\u029b\3\2\2\2\u029a"+
		"\u028e\3\2\2\2\u029a\u0295\3\2\2\2\u029b\u029c\3\2\2\2\u029c\u029d\b="+
		"\2\2\u029dz\3\2\2\2\u029e\u02a3\7)\2\2\u029f\u02a2\5}?\2\u02a0\u02a2\n"+
		"\2\2\2\u02a1\u029f\3\2\2\2\u02a1\u02a0\3\2\2\2\u02a2\u02a5\3\2\2\2\u02a3"+
		"\u02a1\3\2\2\2\u02a3\u02a4\3\2\2\2\u02a4\u02a6\3\2\2\2\u02a5\u02a3\3\2"+
		"\2\2\u02a6\u02b1\7)\2\2\u02a7\u02ac\7$\2\2\u02a8\u02ab\5}?\2\u02a9\u02ab"+
		"\n\3\2\2\u02aa\u02a8\3\2\2\2\u02aa\u02a9\3\2\2\2\u02ab\u02ae\3\2\2\2\u02ac"+
		"\u02aa\3\2\2\2\u02ac\u02ad\3\2\2\2\u02ad\u02af\3\2\2\2\u02ae\u02ac\3\2"+
		"\2\2\u02af\u02b1\7$\2\2\u02b0\u029e\3\2\2\2\u02b0\u02a7\3\2\2\2\u02b1"+
		"|\3\2\2\2\u02b2\u02b3\7^\2\2\u02b3\u02b4\13\2\2\2\u02b4~\3\2\2\2\u02b5"+
		"\u02b9\5\u0081A\2\u02b6\u02b8\5\u0083B\2\u02b7\u02b6\3\2\2\2\u02b8\u02bb"+
		"\3\2\2\2\u02b9\u02b7\3\2\2\2\u02b9\u02ba\3\2\2\2\u02ba\u02c2\3\2\2\2\u02bb"+
		"\u02b9\3\2\2\2\u02bc\u02be\7\62\2\2\u02bd\u02bc\3\2\2\2\u02be\u02bf\3"+
		"\2\2\2\u02bf\u02bd\3\2\2\2\u02bf\u02c0\3\2\2\2\u02c0\u02c2\3\2\2\2\u02c1"+
		"\u02b5\3\2\2\2\u02c1\u02bd\3\2\2\2\u02c2\u0080\3\2\2\2\u02c3\u02c4\t\4"+
		"\2\2\u02c4\u0082\3\2\2\2\u02c5\u02c6\t\5\2\2\u02c6\u0084\3\2\2\2\u02c7"+
		"\u02c8\5\u0087D\2\u02c8\u0086\3\2\2\2\u02c9\u02cb\5\u0089E\2\u02ca\u02c9"+
		"\3\2\2\2\u02ca\u02cb\3\2\2\2\u02cb\u02cc\3\2\2\2\u02cc\u02d1\5\u008bF"+
		"\2\u02cd\u02ce\5\u0089E\2\u02ce\u02cf\7\60\2\2\u02cf\u02d1\3\2\2\2\u02d0"+
		"\u02ca\3\2\2\2\u02d0\u02cd\3\2\2\2\u02d1\u0088\3\2\2\2\u02d2\u02d4\5\u0083"+
		"B\2\u02d3\u02d2\3\2\2\2\u02d4\u02d5\3\2\2\2\u02d5\u02d3\3\2\2\2\u02d5"+
		"\u02d6\3\2\2\2\u02d6\u008a\3\2\2\2\u02d7\u02d9\7\60\2\2\u02d8\u02da\5"+
		"\u0083B\2\u02d9\u02d8\3\2\2\2\u02da\u02db\3\2\2\2\u02db\u02d9\3\2\2\2"+
		"\u02db\u02dc\3\2\2\2\u02dc\u008c\3\2\2\2\u02dd\u02de\7p\2\2\u02de\u02df"+
		"\7w\2\2\u02df\u02e0\7n\2\2\u02e0\u02e1\7n\2\2\u02e1\u008e\3\2\2\2\u02e2"+
		"\u02e3\7?\2\2\u02e3\u0090\3\2\2\2\u02e4\u02e5\7@\2\2\u02e5\u0092\3\2\2"+
		"\2\u02e6\u02e7\7>\2\2\u02e7\u0094\3\2\2\2\u02e8\u02e9\7#\2\2\u02e9\u0096"+
		"\3\2\2\2\u02ea\u02eb\7\u0080\2\2\u02eb\u0098\3\2\2\2\u02ec\u02ed\7A\2"+
		"\2\u02ed\u009a\3\2\2\2\u02ee\u02ef\7<\2\2\u02ef\u009c\3\2\2\2\u02f0\u02f1"+
		"\7?\2\2\u02f1\u02f2\7?\2\2\u02f2\u009e\3\2\2\2\u02f3\u02f4\7>\2\2\u02f4"+
		"\u02f5\7?\2\2\u02f5\u00a0\3\2\2\2\u02f6\u02f7\7@\2\2\u02f7\u02f8\7?\2"+
		"\2\u02f8\u00a2\3\2\2\2\u02f9\u02fa\7#\2\2\u02fa\u02fb\7?\2\2\u02fb\u00a4"+
		"\3\2\2\2\u02fc\u02fd\7(\2\2\u02fd\u02fe\7(\2\2\u02fe\u00a6\3\2\2\2\u02ff"+
		"\u0300\7~\2\2\u0300\u0301\7~\2\2\u0301\u00a8\3\2\2\2\u0302\u0303\7-\2"+
		"\2\u0303\u0304\7-\2\2\u0304\u00aa\3\2\2\2\u0305\u0306\7/\2\2\u0306\u0307"+
		"\7/\2\2\u0307\u00ac\3\2\2\2\u0308\u0309\7-\2\2\u0309\u00ae\3\2\2\2\u030a"+
		"\u030b\7/\2\2\u030b\u00b0\3\2\2\2\u030c\u030d\7,\2\2\u030d\u00b2\3\2\2"+
		"\2\u030e\u030f\7\61\2\2\u030f\u00b4\3\2\2\2\u0310\u0311\7(\2\2\u0311\u00b6"+
		"\3\2\2\2\u0312\u0313\7~\2\2\u0313\u00b8\3\2\2\2\u0314\u0315\7`\2\2\u0315"+
		"\u00ba\3\2\2\2\u0316\u0317\7\'\2\2\u0317\u00bc\3\2\2\2\u0318\u0319\7-"+
		"\2\2\u0319\u031a\7?\2\2\u031a\u00be\3\2\2\2\u031b\u031c\7/\2\2\u031c\u031d"+
		"\7?\2\2\u031d\u00c0\3\2\2\2\u031e\u031f\7,\2\2\u031f\u0320\7?\2\2\u0320"+
		"\u00c2\3\2\2\2\u0321\u0322\7\61\2\2\u0322\u0323\7?\2\2\u0323\u00c4\3\2"+
		"\2\2\u0324\u0325\7(\2\2\u0325\u0326\7?\2\2\u0326\u00c6\3\2\2\2\u0327\u0328"+
		"\7~\2\2\u0328\u0329\7?\2\2\u0329\u00c8\3\2\2\2\u032a\u032b\7`\2\2\u032b"+
		"\u032c\7?\2\2\u032c\u00ca\3\2\2\2\u032d\u032e\7\'\2\2\u032e\u032f\7?\2"+
		"\2\u032f\u00cc\3\2\2\2\u0330\u0331\7>\2\2\u0331\u0332\7>\2\2\u0332\u0333"+
		"\7?\2\2\u0333\u00ce\3\2\2\2\u0334\u0335\7@\2\2\u0335\u0336\7@\2\2\u0336"+
		"\u0337\7?\2\2\u0337\u00d0\3\2\2\2\u0338\u0339\7@\2\2\u0339\u033a\7@\2"+
		"\2\u033a\u033b\7@\2\2\u033b\u033c\7?\2\2\u033c\u00d2\3\2\2\2\u033d\u0341"+
		"\5\u00d5k\2\u033e\u0340\5\u00d7l\2\u033f\u033e\3\2\2\2\u0340\u0343\3\2"+
		"\2\2\u0341\u033f\3\2\2\2\u0341\u0342\3\2\2\2\u0342\u00d4\3\2\2\2\u0343"+
		"\u0341\3\2\2\2\u0344\u0346\t\6\2\2\u0345\u0344\3\2\2\2\u0346\u00d6\3\2"+
		"\2\2\u0347\u034a\5\u00d5k\2\u0348\u034a\t\5\2\2\u0349\u0347\3\2\2\2\u0349"+
		"\u0348\3\2\2\2\u034a\u00d8\3\2\2\2\u034b\u034f\5\u00dbn\2\u034c\u034f"+
		"\5\u00ddo\2\u034d\u034f\5\u00dfp\2\u034e\u034b\3\2\2\2\u034e\u034c\3\2"+
		"\2\2\u034e\u034d\3\2\2\2\u034f\u0350\3\2\2\2\u0350\u0351\bm\3\2\u0351"+
		"\u00da\3\2\2\2\u0352\u0354\t\7\2\2\u0353\u0352\3\2\2\2\u0354\u0355\3\2"+
		"\2\2\u0355\u0353\3\2\2\2\u0355\u0356\3\2\2\2\u0356\u00dc\3\2\2\2\u0357"+
		"\u035b\7%\2\2\u0358\u035a\n\b\2\2\u0359\u0358\3\2\2\2\u035a\u035d\3\2"+
		"\2\2\u035b\u0359\3\2\2\2\u035b\u035c\3\2\2\2\u035c\u00de\3\2\2\2\u035d"+
		"\u035b\3\2\2\2\u035e\u0360\7^\2\2\u035f\u0361\5\u00dbn\2\u0360\u035f\3"+
		"\2\2\2\u0360\u0361\3\2\2\2\u0361\u0367\3\2\2\2\u0362\u0364\7\17\2\2\u0363"+
		"\u0362\3\2\2\2\u0363\u0364\3\2\2\2\u0364\u0365\3\2\2\2\u0365\u0368\7\f"+
		"\2\2\u0366\u0368\7\17\2\2\u0367\u0363\3\2\2\2\u0367\u0366\3\2\2\2\u0368"+
		"\u00e0\3\2\2\2\34\2\u0291\u0295\u0298\u029a\u02a1\u02a3\u02aa\u02ac\u02b0"+
		"\u02b9\u02bf\u02c1\u02ca\u02d0\u02d5\u02db\u0341\u0345\u0349\u034e\u0355"+
		"\u035b\u0360\u0363\u0367\4\3=\2\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
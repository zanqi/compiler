/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */
    // Max size of string constants
    static int MAX_STR_CONST = 1025;
    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();
    private int curr_lineno = 1;
    int get_curr_lineno() {
	    return yyline + 1;
    }
    private AbstractSymbol filename;
    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }
    AbstractSymbol curr_filename() {
	return filename;
    }
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int STRING = 2;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int yy_state_dtrans[] = {
		0,
		54,
		58
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NOT_ACCEPT,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NOT_ACCEPT,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"41:9,35,38,41:2,40,41:18,35,41,36,41:5,18,19,29,26,25,23,24,30,33:10,20,21," +
"22,27,42,41:2,32:26,41,37,41:2,34,41,3,39,1,31,8,11,31,7,5,31:2,2,31,6,13,1" +
"4,31,9,4,10,15,31,12,31:3,16,41,17,28,41,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,95,
"0,1,2,1:2,3,1:3,4,5,1:3,6,1:3,7,8,1,9,1,10,11:2,1:3,12,1,11:12,1:11,13,14,1" +
"5,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,4" +
"0,41,42,43,44,45,11,46,47,48,49,50,11,51")[0];

	private int yy_nxt[][] = unpackFromString(52,43,
"1,2,72,93:2,55,73,93,83,93,84,59,94,93,85,93,3,4,5,6,7,8,9,10,11,12,13,14,1" +
"5,16,17,93,18,19,20,21,22,20,21,93,-1,20:2,-1:44,93,86,93:13,-1:15,93:2,87:" +
"2,-1:4,93,-1:32,26,-1:36,27,-1:3,28,-1:38,29,-1:61,30,-1,18:15,-1:15,18:4,-" +
"1:4,18,-1:36,19,-1:44,21,-1:2,21,-1:5,93:6,90,93:8,-1:15,93:2,87:2,-1:4,93," +
"-1:4,93:15,-1:15,93:2,87:2,-1:4,93,-1:4,29:37,-1,29,-1,29:2,1,43:28,56,43:8" +
",44,43,-1,43:2,-1,93:5,23,93:4,24,93:4,-1:15,93:2,87:2,-1:4,93,-1:22,45,-1:" +
"24,48:5,49,48:3,50,51,48:26,52,53,48:3,1,46:35,47,57,-1,46,-1,46:2,-1,93:2," +
"88,93,25,93:10,-1:15,93:2,87:2,-1:4,93,-1:4,93:9,31,93:5,-1:15,93:2,87:2,-1" +
":4,93,-1:4,93:11,32,93:3,-1:15,93:2,87:2,-1:4,93,-1:4,93:9,33,93:5,-1:15,93" +
":2,87:2,-1:4,93,-1:4,93:13,34,93,-1:15,93:2,87:2,-1:4,93,-1:4,93:7,35,93:7," +
"-1:15,93:2,87:2,-1:4,93,-1:4,93:5,36,93:9,-1:15,93:2,87:2,-1:4,93,-1:4,93:7" +
",37,93:7,-1:15,93:2,87:2,-1:4,93,-1:4,93,38,93:13,-1:15,93:2,87:2,-1:4,93,-" +
"1:4,93:3,39,93:11,-1:15,93:2,87:2,-1:4,93,-1:4,93:7,40,93:7,-1:15,93:2,87:2" +
",-1:4,93,-1:4,93:7,41,93:7,-1:15,93:2,87:2,-1:4,93,-1:4,93:3,42,93:11,-1:15" +
",93:2,87:2,-1:4,93,-1:4,93:7,60,93:4,74,93:2,-1:15,93:2,87:2,-1:4,93,-1:4,9" +
"3:7,61,93:4,62,93:2,-1:15,93:2,87:2,-1:4,93,-1:4,93:12,63,93:2,-1:15,93:2,8" +
"7:2,-1:4,93,-1:4,93:3,64,93:11,-1:15,93:2,87:2,-1:4,93,-1:4,93:7,65,93:7,-1" +
":15,93:2,87:2,-1:4,93,-1:4,93:14,66,-1:15,93:2,87:2,-1:4,93,-1:4,93:12,67,9" +
"3:2,-1:15,93:2,87:2,-1:4,93,-1:4,93:3,68,93:11,-1:15,93:2,87:2,-1:4,93,-1:4" +
",93:3,69,93:11,-1:15,93:2,87:2,-1:4,93,-1:4,93,70,93:13,-1:15,93:2,87:2,-1:" +
"4,93,-1:4,93:9,71,93:5,-1:15,93:2,87:2,-1:4,93,-1:4,93,75,93:13,-1:15,93:2," +
"87:2,-1:4,93,-1:4,93:6,76,93,77,93:6,-1:15,93:2,87:2,-1:4,93,-1:4,93:12,78," +
"93:2,-1:15,93:2,87:2,-1:4,93,-1:4,93:2,79,93:12,-1:15,93:2,87:2,-1:4,93,-1:" +
"4,93,80,93:13,-1:15,93:2,87:2,-1:4,93,-1:4,93:4,81,93:10,-1:15,93:2,87:2,-1" +
":4,93,-1:4,93:7,91,93:7,-1:15,93:2,87:2,-1:4,93,-1:4,93:8,92,93:6,-1:15,93:" +
"2,87:2,-1:4,93,-1:4,93:4,82,93:10,-1:15,93:2,87:2,-1:4,93,-1:4,93:6,89,93:8" +
",-1:15,93:2,87:2,-1:4,93,-1:3");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */
    switch(yy_lexical_state) {
    case YYINITIAL:
	/* nothing special to do in the initial state */
	break;
	/* If necessary, add code for other states here, e.g:
	   case COMMENT:
	   ...
	   break;
	*/
    case COMMENT:
        System.err.println("#" + (yyline+1) + " LEXER BUG - EOF in comment: " + yytext());
        return new Symbol(TokenConstants.EOF);
    case STRING:
        System.err.println("#" + (yyline+1) + " LEXER BUG - EOF in string: " + string_buf.toString());
        return new Symbol(TokenConstants.EOF);
    }
    return new Symbol(TokenConstants.EOF);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -3:
						break;
					case 3:
						{ return new Symbol(TokenConstants.LBRACE); }
					case -4:
						break;
					case 4:
						{ return new Symbol(TokenConstants.RBRACE); }
					case -5:
						break;
					case 5:
						{ return new Symbol(TokenConstants.LPAREN); }
					case -6:
						break;
					case 6:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -7:
						break;
					case 7:
						{ return new Symbol(TokenConstants.COLON); }
					case -8:
						break;
					case 8:
						{ return new Symbol(TokenConstants.SEMI); }
					case -9:
						break;
					case 9:
						{ return new Symbol(TokenConstants.LT); }
					case -10:
						break;
					case 10:
						{ return new Symbol(TokenConstants.MINUS); }
					case -11:
						break;
					case 11:
						{ return new Symbol(TokenConstants.DOT); }
					case -12:
						break;
					case 12:
						{ return new Symbol(TokenConstants.COMMA); }
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.PLUS); }
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.EQ); }
					case -15:
						break;
					case 15:
						{ return new Symbol(TokenConstants.NEG); }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.MULT); }
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.DIV); }
					case -18:
						break;
					case 18:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -19:
						break;
					case 19:
						{ 
    IntSymbol sym = (IntSymbol)AbstractTable.inttable.addString(yytext());
    return new Symbol(TokenConstants.INT_CONST, sym);
}
					case -20:
						break;
					case 20:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("#" + (yyline+1) + " LEXER BUG - UNMATCHED: " + yytext()); }
					case -21:
						break;
					case 21:
						{ /* eat whitespace */ }
					case -22:
						break;
					case 22:
						{ yybegin(STRING); string_buf.setLength(0); }
					case -23:
						break;
					case 23:
						{ return new Symbol(TokenConstants.IN); }
					case -24:
						break;
					case 24:
						{ return new Symbol(TokenConstants.IF); }
					case -25:
						break;
					case 25:
						{ return new Symbol(TokenConstants.FI); }
					case -26:
						break;
					case 26:
						{ yybegin(COMMENT); }
					case -27:
						break;
					case 27:
						{ return new Symbol(TokenConstants.ASSIGN); }
					case -28:
						break;
					case 28:
						{ return new Symbol(TokenConstants.LE); }
					case -29:
						break;
					case 29:
						{ /* eat comments */ }
					case -30:
						break;
					case 30:
						{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }
					case -31:
						break;
					case 31:
						{ return new Symbol(TokenConstants.LET); }
					case -32:
						break;
					case 32:
						{ return new Symbol(TokenConstants.NEW); }
					case -33:
						break;
					case 33:
						{ return new Symbol(TokenConstants.NOT); }
					case -34:
						break;
					case 34:
						{ return new Symbol(TokenConstants.LOOP); }
					case -35:
						break;
					case 35:
						{ return new Symbol(TokenConstants.ELSE); }
					case -36:
						break;
					case 36:
						{ return new Symbol(TokenConstants.THEN); }
					case -37:
						break;
					case 37:
						{ return new Symbol(TokenConstants.BOOL_CONST, Boolean.TRUE); }
					case -38:
						break;
					case 38:
						{ return new Symbol(TokenConstants.POOL); }
					case -39:
						break;
					case 39:
						{ return new Symbol(TokenConstants.CLASS); }
					case -40:
						break;
					case 40:
						{ return new Symbol(TokenConstants.BOOL_CONST, Boolean.FALSE); }
					case -41:
						break;
					case 41:
						{ return new Symbol(TokenConstants.WHILE); }
					case -42:
						break;
					case 42:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -43:
						break;
					case 43:
						{ /* eat comments */ }
					case -44:
						break;
					case 44:
						{ }
					case -45:
						break;
					case 45:
						{ yybegin(YYINITIAL); }
					case -46:
						break;
					case 46:
						{ 
    string_buf.append(yytext()); 
}
					case -47:
						break;
					case 47:
						{ yybegin(YYINITIAL);
                                AbstractSymbol sym = AbstractTable.stringtable.addString(string_buf.toString());
                                return new Symbol(TokenConstants.STR_CONST, sym); }
					case -48:
						break;
					case 48:
						{ string_buf.append(yytext().charAt(1));  }
					case -49:
						break;
					case 49:
						{ string_buf.append("\n"); }
					case -50:
						break;
					case 50:
						{ string_buf.append("\t"); }
					case -51:
						break;
					case 51:
						{ string_buf.append("\f"); }
					case -52:
						break;
					case 52:
						{ string_buf.append("\n"); curr_lineno++; }
					case -53:
						break;
					case 53:
						{ string_buf.append("\b"); }
					case -54:
						break;
					case 55:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -55:
						break;
					case 56:
						{ /* eat comments */ }
					case -56:
						break;
					case 57:
						{ 
    string_buf.append(yytext()); 
}
					case -57:
						break;
					case 59:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -58:
						break;
					case 60:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -59:
						break;
					case 61:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -60:
						break;
					case 62:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -61:
						break;
					case 63:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -62:
						break;
					case 64:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -63:
						break;
					case 65:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -64:
						break;
					case 66:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -65:
						break;
					case 67:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -66:
						break;
					case 68:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -67:
						break;
					case 69:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -68:
						break;
					case 70:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -69:
						break;
					case 71:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -70:
						break;
					case 72:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -71:
						break;
					case 73:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -72:
						break;
					case 74:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -73:
						break;
					case 75:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -74:
						break;
					case 76:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -75:
						break;
					case 77:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -76:
						break;
					case 78:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -77:
						break;
					case 79:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -78:
						break;
					case 80:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -79:
						break;
					case 81:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -80:
						break;
					case 82:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -81:
						break;
					case 83:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -82:
						break;
					case 84:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -83:
						break;
					case 85:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -84:
						break;
					case 86:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -85:
						break;
					case 87:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -86:
						break;
					case 88:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -87:
						break;
					case 89:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -88:
						break;
					case 90:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -89:
						break;
					case 91:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -90:
						break;
					case 92:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -91:
						break;
					case 93:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -92:
						break;
					case 94:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -93:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}

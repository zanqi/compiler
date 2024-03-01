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
	return curr_lineno;
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
		18,
		28
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
		/* 28 */ YY_NOT_ACCEPT,
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
		/* 41 */ YY_NOT_ACCEPT,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NOT_ACCEPT,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NOT_ACCEPT,
		/* 51 */ YY_NOT_ACCEPT,
		/* 52 */ YY_NOT_ACCEPT,
		/* 53 */ YY_NOT_ACCEPT,
		/* 54 */ YY_NOT_ACCEPT,
		/* 55 */ YY_NOT_ACCEPT,
		/* 56 */ YY_NOT_ACCEPT,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
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
		/* 77 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"28:9,21,24,28:2,27,28:18,21,28,22,28:5,13,14,29,28:5,19:10,15,16,28,30,31,2" +
"8:2,18:26,28,23,28:2,20,28,3,25,1,17,8,26,17,7,5,17:2,2,17,6,17:3,9,4,10,17" +
":6,11,28,12,28:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,78,
"0,1,2,1:2,3,1:3,4,5,1,6,1:3,7:2,8,1,9,1:7,10,11,12:2,13,12:2,14,12:3,15,1,1" +
"6,17,1:2,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,7,36,37,12,3" +
"8,39,40,41,42,43,7,12,44,45,46")[0];

	private int yy_nxt[][] = unpackFromString(47,32,
"1,2,73:3,75,73:5,3,4,5,6,7,8,73,9,10,11,12,13,11,12,73:2,-1,11:2,35,11,-1:3" +
"3,73,61,73:8,-1:6,73:2,63:2,-1:4,73:2,-1:34,14,-1:3,9:10,-1:6,9:4,-1:4,9:2," +
"-1:24,10,-1:33,12,-1:2,12,-1:8,73:10,-1:6,73:2,63:2,-1:4,73:2,-1:5,1,39,74:" +
"3,76,74:5,30,31,32,33,34,74:8,19,74:4,77,74:2,-1:2,41,-1:29,1,20,40:3,46,40" +
":5,3,4,5,6,7,40:6,21,49,-1,40:2,-1,40:4,-1,73:3,16,73:6,-1:6,73:2,63:2,-1:4" +
",73:2,-1:6,74:23,-1,74:7,-1,74:23,-1,74:4,36,74:2,-1:31,15,-1,74,62,74:21,-" +
"1,74:7,-1:3,50,-1:29,73:3,17,73:6,-1:6,73:2,63:2,-1:4,73:2,-1:6,74:3,37,74:" +
"19,-1,74:7,-1:6,47,-1:32,51,-1:25,74:3,38,74:19,-1,74:7,-1,22:5,23,22:3,24," +
"22:13,25,26,27,22:5,-1:4,52,-1:35,53,-1:27,43,-1:36,54,-1:27,55,-1:36,56,-1" +
":25,44,-1:28,73:3,29,73:6,-1:6,73:2,63:2,-1:4,73:2,-1:6,74:3,45,74:19,-1,74" +
":7,-1,73:9,42,-1:6,73:2,63:2,-1:4,73:2,-1:6,74:9,48,74:13,-1,74:7,-1,73:2,5" +
"7,73:7,-1:6,73:2,63:2,-1:4,73:2,-1:6,74:2,58,74:20,-1,74:7,-1,74:6,68,74:16" +
",-1,74:7,-1,73:6,67,73:3,-1:6,73:2,63:2,-1:4,73:2,-1:6,73:7,69,73:2,-1:6,73" +
":2,63:2,-1:4,73:2,-1:6,74:7,70,74:15,-1,74:7,-1,73:8,71,73,-1:6,73:2,63:2,-" +
"1:4,73:2,-1:6,74:8,72,74:14,-1,74:7,-1,73:4,59,73:5,-1:6,73:2,63:2,-1:4,73:" +
"2,-1:6,74:4,60,74:18,-1,74:7,-1,73:5,65,73:4,-1:6,73:2,63:2,-1:4,73:2,-1:6," +
"74:5,64,74:17,-1,74:7,-1,74:13,66,74:9,-1,74:7");

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
        return new Symbol(TokenConstants.ERROR, "EOF in comment");
    case STRING:
        return new Symbol(TokenConstants.ERROR, "EOF in string: " + string_buf.toString());
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
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -10:
						break;
					case 10:
						{ 
    IntSymbol sym = (IntSymbol)AbstractTable.inttable.lookup(yytext());
    if (sym == null) {
        sym = (IntSymbol)AbstractTable.inttable.addString(yytext());
    }
    return new Symbol(TokenConstants.INT_CONST, sym);
}
					case -11:
						break;
					case 11:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -12:
						break;
					case 12:
						{ /* eat whitespace */ }
					case -13:
						break;
					case 13:
						{ yybegin(STRING); }
					case -14:
						break;
					case 14:
						{ yybegin(COMMENT); }
					case -15:
						break;
					case 15:
						{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.CLASS); }
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -18:
						break;
					case 18:
						{ /* eat comments */ }
					case -19:
						break;
					case 19:
						{ curr_lineno++; }
					case -20:
						break;
					case 20:
						{ 
    string_buf.append(yytext()); 
}
					case -21:
						break;
					case 21:
						{ yybegin(YYINITIAL);
                    AbstractSymbol sym = AbstractTable.stringtable.addString(string_buf.toString());
                    return new Symbol(TokenConstants.STR_CONST, sym); }
					case -22:
						break;
					case 22:
						{ string_buf.append(yytext().charAt(1));  }
					case -23:
						break;
					case 23:
						{ string_buf.append("\n"); }
					case -24:
						break;
					case 24:
						{ string_buf.append("\t"); }
					case -25:
						break;
					case 25:
						{ string_buf.append("\n"); curr_lineno++; }
					case -26:
						break;
					case 26:
						{ string_buf.append("\b"); }
					case -27:
						break;
					case 27:
						{ string_buf.append("\f"); }
					case -28:
						break;
					case 29:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -29:
						break;
					case 30:
						{ return new Symbol(TokenConstants.LBRACE); }
					case -30:
						break;
					case 31:
						{ return new Symbol(TokenConstants.RBRACE); }
					case -31:
						break;
					case 32:
						{ return new Symbol(TokenConstants.LPAREN); }
					case -32:
						break;
					case 33:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -33:
						break;
					case 34:
						{ return new Symbol(TokenConstants.COLON); }
					case -34:
						break;
					case 35:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -35:
						break;
					case 36:
						{ yybegin(COMMENT); }
					case -36:
						break;
					case 37:
						{ return new Symbol(TokenConstants.CLASS); }
					case -37:
						break;
					case 38:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -38:
						break;
					case 39:
						{ /* eat comments */ }
					case -39:
						break;
					case 40:
						{ 
    string_buf.append(yytext()); 
}
					case -40:
						break;
					case 42:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -41:
						break;
					case 43:
						{ return new Symbol(TokenConstants.CLASS); }
					case -42:
						break;
					case 44:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -43:
						break;
					case 45:
						{ /* eat comments */ }
					case -44:
						break;
					case 46:
						{ 
    string_buf.append(yytext()); 
}
					case -45:
						break;
					case 48:
						{ /* eat comments */ }
					case -46:
						break;
					case 49:
						{ 
    string_buf.append(yytext()); 
}
					case -47:
						break;
					case 57:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -48:
						break;
					case 58:
						{ /* eat comments */ }
					case -49:
						break;
					case 59:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -50:
						break;
					case 60:
						{ /* eat comments */ }
					case -51:
						break;
					case 61:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -52:
						break;
					case 62:
						{ /* eat comments */ }
					case -53:
						break;
					case 63:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -54:
						break;
					case 64:
						{ /* eat comments */ }
					case -55:
						break;
					case 65:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -56:
						break;
					case 66:
						{ /* eat comments */ }
					case -57:
						break;
					case 67:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -58:
						break;
					case 68:
						{ /* eat comments */ }
					case -59:
						break;
					case 69:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -60:
						break;
					case 70:
						{ /* eat comments */ }
					case -61:
						break;
					case 71:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -62:
						break;
					case 72:
						{ /* eat comments */ }
					case -63:
						break;
					case 73:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -64:
						break;
					case 74:
						{ /* eat comments */ }
					case -65:
						break;
					case 75:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -66:
						break;
					case 76:
						{ /* eat comments */ }
					case -67:
						break;
					case 77:
						{ /* eat comments */ }
					case -68:
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

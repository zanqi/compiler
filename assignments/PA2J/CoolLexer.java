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
		59,
		81
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
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NOT_ACCEPT,
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
		/* 81 */ YY_NOT_ACCEPT,
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
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"61:9,55,58,61:2,60,61:18,55,61,56,61:5,23,24,34,31,30,28,29,35,53:10,25,26," +
"27,32,62,61,17,3,52,1,16,8,11,52,7,5,52:2,2,52,6,13,14,52,9,4,10,19,15,12,5" +
"2:3,61,57,61:2,54,61,36,59,38,39,40,20,37,41,42,37:2,43,37,44,45,46,37,47,4" +
"8,18,49,50,51,37:3,21,61,22,33,61,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,167,
"0,1,2,1,3,1:2,4,1:3,5,6,1:3,7,1:3,8,1,9,1,10,11:3,1:3,12,1,11:9,13,11:2,13," +
"11:2,1:11,14,15,16,17,13:16,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33" +
",34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58" +
",11,59,60,13,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81" +
",82,83,84,85,86,87,88,89,90,91,92,11,13,93,94,95,96,97,98,99,100,101")[0];

	private int yy_nxt[][] = unpackFromString(102,63,
"1,2,114,156:2,60,116,156,158,156,160,82,162,84,164,156:2,3,4,156,61,5,6,7,8" +
",9,10,11,12,13,14,15,16,17,18,19,157:2,159,157,161,157,83,115,117,85,163,15" +
"7:4,165,156,20,21,22,23,21,22,157,-1,21:2,-1:64,156,166,118,156:13,-1,156:3" +
",-1:15,118,156:6,166,156:9,120:2,-1:4,156,-1:4,157:6,119,157,121,157:7,-1,1" +
"57:3,-1:15,157:5,119,157:5,121,157:5,123:2,-1:4,157,-1:37,28,-1:56,29,-1:3," +
"30,-1:58,31,-1:96,32,-1:53,20,-1:64,22,-1:2,22,-1:5,156:6,140,156:9,-1,156:" +
"3,-1:15,156:5,140,156:11,120:2,-1:4,156,-1:4,156:16,-1,156:3,-1:15,156:17,1" +
"20:2,-1:4,156,-1:4,31:57,-1,31,-1,31:2,-1,157:16,-1,157:3,-1:15,157:17,123:" +
"2,-1:4,157,-1:3,1,48:33,79,48:23,49,48,-1,48:2,-1,156:3,124,156,24,156:4,25" +
",156:5,-1,156:2,25,-1:15,156:8,24,156:3,124,156:4,120:2,-1:4,156,-1:4,157:2" +
",125,157,64,157:11,-1,157:3,-1:15,125,157:5,64,157:10,123:2,-1:4,157,-1:4,1" +
"57:6,149,157:9,-1,157:3,-1:15,157:5,149,157:11,123:2,-1:4,157,-1:27,50,-1:3" +
"9,53:17,54,53,55,53:23,56,53:13,57,58,53:3,1,51:55,52,80,-1,51,-1,51:2,-1,1" +
"56:4,26,156:11,-1,156:3,-1:15,156:6,26,156:10,120:2,-1:4,156,-1:4,157:3,135" +
",157,62,157:4,63,157:5,-1,157:2,63,-1:15,157:8,62,157:3,135,157:4,123:2,-1:" +
"4,157,-1:4,156:10,27,156:5,-1,156:2,27,-1:15,156:17,120:2,-1:4,156,-1:4,157" +
":10,65,157:5,-1,157:2,65,-1:15,157:17,123:2,-1:4,157,-1:4,156:9,33,156:6,-1" +
",33,156:2,-1:15,156:17,120:2,-1:4,156,-1:4,157:9,66,157:6,-1,66,157:2,-1:15" +
",157:17,123:2,-1:4,157,-1:4,156:11,34,156:4,-1,156:3,-1:15,156:15,34,156,12" +
"0:2,-1:4,156,-1:4,157:11,67,157:4,-1,157:3,-1:15,157:15,67,157,123:2,-1:4,1" +
"57,-1:4,156:9,35,156:6,-1,35,156:2,-1:15,156:17,120:2,-1:4,156,-1:4,157:9,6" +
"8,157:6,-1,68,157:2,-1:15,157:17,123:2,-1:4,157,-1:4,156:7,36,156:8,-1,156:" +
"3,-1:15,156:4,36,156:12,120:2,-1:4,156,-1:4,157:5,73,157:10,-1,157:3,-1:15," +
"157:8,73,157:8,123:2,-1:4,157,-1:4,156:13,37,156:2,-1,156:3,-1:15,156:10,37" +
",156:6,120:2,-1:4,156,-1:4,157:7,42,157:8,-1,157:3,-1:15,157:4,42,157:12,12" +
"3:2,-1:4,157,-1:4,156:7,38,156:8,-1,156:3,-1:15,156:4,38,156:12,120:2,-1:4," +
"156,-1:4,157:7,69,157:8,-1,157:3,-1:15,157:4,69,157:12,123:2,-1:4,157,-1:4," +
"39,156:15,-1,156:3,-1:15,156:2,39,156:14,120:2,-1:4,156,-1:4,157:7,71,157:8" +
",-1,157:3,-1:15,157:4,71,157:12,123:2,-1:4,157,-1:4,156:5,40,156:10,-1,156:" +
"3,-1:15,156:8,40,156:8,120:2,-1:4,156,-1:4,72,157:15,-1,157:3,-1:15,157:2,7" +
"2,157:14,123:2,-1:4,157,-1:4,156,41,156:14,-1,156:3,-1:15,156:7,41,156:9,12" +
"0:2,-1:4,156,-1:4,157:13,70,157:2,-1,157:3,-1:15,157:10,70,157:6,123:2,-1:4" +
",157,-1:4,156:3,43,156:12,-1,156:3,-1:15,156:12,43,156:4,120:2,-1:4,156,-1:" +
"4,157,74,157:14,-1,157:3,-1:15,157:7,74,157:9,123:2,-1:4,157,-1:4,156:7,44," +
"156:8,-1,156:3,-1:15,156:4,44,156:12,120:2,-1:4,156,-1:4,157:7,45,157:8,-1," +
"157:3,-1:15,157:4,45,157:12,123:2,-1:4,157,-1:4,156:15,46,-1,156:3,-1:15,15" +
"6:3,46,156:13,120:2,-1:4,156,-1:4,157:3,75,157:12,-1,157:3,-1:15,157:12,75," +
"157:4,123:2,-1:4,157,-1:4,156:3,47,156:12,-1,156:3,-1:15,156:12,47,156:4,12" +
"0:2,-1:4,156,-1:4,157:7,76,157:8,-1,157:3,-1:15,157:4,76,157:12,123:2,-1:4," +
"157,-1:4,157:15,77,-1,157:3,-1:15,157:3,77,157:13,123:2,-1:4,157,-1:4,157:3" +
",78,157:12,-1,157:3,-1:15,157:12,78,157:4,123:2,-1:4,157,-1:4,156:7,86,156:" +
"4,122,156:3,-1,156:3,-1:15,156:4,86,156:4,122,156:7,120:2,-1:4,156,-1:4,157" +
":7,87,157:4,137,157:3,-1,157:3,-1:15,157:4,87,157:4,137,157:7,123:2,-1:4,15" +
"7,-1:4,156:7,88,156:4,90,156:3,-1,156:3,-1:15,156:4,88,156:4,90,156:7,120:2" +
",-1:4,156,-1:4,157:7,89,157:4,91,157:3,-1,157:3,-1:15,157:4,89,157:4,91,157" +
":7,123:2,-1:4,157,-1:4,156:3,92,156:12,-1,156:3,-1:15,156:12,92,156:4,120:2" +
",-1:4,156,-1:4,157:7,93,157:8,-1,157:3,-1:15,157:4,93,157:12,123:2,-1:4,157" +
",-1:4,157:16,-1,157,95,157,-1:15,157:13,95,157:3,123:2,-1:4,157,-1:4,156:12" +
",94,156:3,-1,156:3,-1:15,156:9,94,156:7,120:2,-1:4,156,-1:4,156:14,138,156," +
"-1,156:3,-1:15,156:14,138,156:2,120:2,-1:4,156,-1:4,157,143,157:14,-1,157:3" +
",-1:15,157:7,143,157:9,123:2,-1:4,157,-1:4,156:3,96,156:12,-1,156:3,-1:15,1" +
"56:12,96,156:4,120:2,-1:4,156,-1:4,157:2,145,157:13,-1,157:3,-1:15,145,157:" +
"16,123:2,-1:4,157,-1:4,156:2,98,156:13,-1,156:3,-1:15,98,156:16,120:2,-1:4," +
"156,-1:4,157:3,97,157:12,-1,157:3,-1:15,157:12,97,157:4,123:2,-1:4,157,-1:4" +
",156:7,100,156:8,-1,156:3,-1:15,156:4,100,156:12,120:2,-1:4,156,-1:4,157:3," +
"99,157:12,-1,157:3,-1:15,157:12,99,157:4,123:2,-1:4,157,-1:4,156:4,142,156:" +
"11,-1,156:3,-1:15,156:6,142,156:10,120:2,-1:4,156,-1:4,157:2,101,157:13,-1," +
"157:3,-1:15,101,157:16,123:2,-1:4,157,-1:4,156:12,102,156:3,-1,156:3,-1:15," +
"156:9,102,156:7,120:2,-1:4,156,-1:4,157:14,147,157,-1,157:3,-1:15,157:14,14" +
"7,157:2,123:2,-1:4,157,-1:4,156:3,104,156:12,-1,156:3,-1:15,156:12,104,156:" +
"4,120:2,-1:4,156,-1:4,157:12,103,157:3,-1,157:3,-1:15,157:9,103,157:7,123:2" +
",-1:4,157,-1:4,156:12,144,156:3,-1,156:3,-1:15,156:9,144,156:7,120:2,-1:4,1" +
"56,-1:4,157:12,105,157:3,-1,157:3,-1:15,157:9,105,157:7,123:2,-1:4,157,-1:4" +
",156:7,146,156:8,-1,156:3,-1:15,156:4,146,156:12,120:2,-1:4,156,-1:4,157:4," +
"151,157:11,-1,157:3,-1:15,157:6,151,157:10,123:2,-1:4,157,-1:4,156,106,156:" +
"14,-1,156:3,-1:15,156:7,106,156:9,120:2,-1:4,156,-1:4,157:3,107,157:12,-1,1" +
"57:3,-1:15,157:12,107,157:4,123:2,-1:4,157,-1:4,156:4,108,156:11,-1,156:3,-" +
"1:15,156:6,108,156:10,120:2,-1:4,156,-1:4,157:3,109,157:12,-1,157:3,-1:15,1" +
"57:12,109,157:4,123:2,-1:4,157,-1:4,156:8,148,156:7,-1,156:3,-1:15,156:11,1" +
"48,156:5,120:2,-1:4,156,-1:4,157:12,152,157:3,-1,157:3,-1:15,157:9,152,157:" +
"7,123:2,-1:4,157,-1:4,156:4,150,156:11,-1,156:3,-1:15,156:6,150,156:10,120:" +
"2,-1:4,156,-1:4,157:7,153,157:8,-1,157:3,-1:15,157:4,153,157:12,123:2,-1:4," +
"157,-1:4,156:9,110,156:6,-1,110,156:2,-1:15,156:17,120:2,-1:4,156,-1:4,157," +
"111,157:14,-1,157:3,-1:15,157:7,111,157:9,123:2,-1:4,157,-1:4,157:4,112,157" +
":11,-1,157:3,-1:15,157:6,112,157:10,123:2,-1:4,157,-1:4,157:8,154,157:7,-1," +
"157:3,-1:15,157:11,154,157:5,123:2,-1:4,157,-1:4,157:4,155,157:11,-1,157:3," +
"-1:15,157:6,155,157:10,123:2,-1:4,157,-1:4,157:9,113,157:6,-1,113,157:2,-1:" +
"15,157:17,123:2,-1:4,157,-1:4,156,126,156,128,156:12,-1,156:3,-1:15,156:7,1" +
"26,156:4,128,156:4,120:2,-1:4,156,-1:4,157,127,129,157:13,-1,157:3,-1:15,12" +
"9,157:6,127,157:9,123:2,-1:4,157,-1:4,156:6,130,156:9,-1,156:3,-1:15,156:5," +
"130,156:11,120:2,-1:4,156,-1:4,157,131,157,133,157:12,-1,157:3,-1:15,157:7," +
"131,157:4,133,157:4,123:2,-1:4,157,-1:4,156:6,132,156:9,-1,156:3,-1:15,156:" +
"5,132,156:11,120:2,-1:4,156,-1:4,157:12,139,157:3,-1,157:3,-1:15,157:9,139," +
"157:7,123:2,-1:4,157,-1:4,156:12,134,156:3,-1,156:3,-1:15,156:9,134,156:7,1" +
"20:2,-1:4,156,-1:4,157:6,141,157:9,-1,157:3,-1:15,157:5,141,157:11,123:2,-1" +
":4,157,-1:4,156:2,136,156:13,-1,156:3,-1:15,136,156:16,120:2,-1:4,156,-1:3");

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
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -3:
						break;
					case 3:
						{ return new Symbol(TokenConstants.AT); }
					case -4:
						break;
					case 4:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -5:
						break;
					case 5:
						{ return new Symbol(TokenConstants.LBRACE); }
					case -6:
						break;
					case 6:
						{ return new Symbol(TokenConstants.RBRACE); }
					case -7:
						break;
					case 7:
						{ return new Symbol(TokenConstants.LPAREN); }
					case -8:
						break;
					case 8:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -9:
						break;
					case 9:
						{ return new Symbol(TokenConstants.COLON); }
					case -10:
						break;
					case 10:
						{ return new Symbol(TokenConstants.SEMI); }
					case -11:
						break;
					case 11:
						{ return new Symbol(TokenConstants.LT); }
					case -12:
						break;
					case 12:
						{ return new Symbol(TokenConstants.MINUS); }
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.DOT); }
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.COMMA); }
					case -15:
						break;
					case 15:
						{ return new Symbol(TokenConstants.PLUS); }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.EQ); }
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.NEG); }
					case -18:
						break;
					case 18:
						{ return new Symbol(TokenConstants.MULT); }
					case -19:
						break;
					case 19:
						{ return new Symbol(TokenConstants.DIV); }
					case -20:
						break;
					case 20:
						{ 
    IntSymbol sym = (IntSymbol)AbstractTable.inttable.addString(yytext());
    return new Symbol(TokenConstants.INT_CONST, sym);
}
					case -21:
						break;
					case 21:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("#" + (yyline+1) + " LEXER BUG - UNMATCHED: " + yytext()); }
					case -22:
						break;
					case 22:
						{ /* eat whitespace */ }
					case -23:
						break;
					case 23:
						{ yybegin(STRING); string_buf.setLength(0); }
					case -24:
						break;
					case 24:
						{ return new Symbol(TokenConstants.IN); }
					case -25:
						break;
					case 25:
						{ return new Symbol(TokenConstants.IF); }
					case -26:
						break;
					case 26:
						{ return new Symbol(TokenConstants.FI); }
					case -27:
						break;
					case 27:
						{ return new Symbol(TokenConstants.OF); }
					case -28:
						break;
					case 28:
						{ yybegin(COMMENT); }
					case -29:
						break;
					case 29:
						{ return new Symbol(TokenConstants.ASSIGN); }
					case -30:
						break;
					case 30:
						{ return new Symbol(TokenConstants.LE); }
					case -31:
						break;
					case 31:
						{ /* eat comments */ }
					case -32:
						break;
					case 32:
						{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }
					case -33:
						break;
					case 33:
						{ return new Symbol(TokenConstants.LET); }
					case -34:
						break;
					case 34:
						{ return new Symbol(TokenConstants.NEW); }
					case -35:
						break;
					case 35:
						{ return new Symbol(TokenConstants.NOT); }
					case -36:
						break;
					case 36:
						{ return new Symbol(TokenConstants.CASE); }
					case -37:
						break;
					case 37:
						{ return new Symbol(TokenConstants.LOOP); }
					case -38:
						break;
					case 38:
						{ return new Symbol(TokenConstants.ELSE); }
					case -39:
						break;
					case 39:
						{ return new Symbol(TokenConstants.ESAC); }
					case -40:
						break;
					case 40:
						{ return new Symbol(TokenConstants.THEN); }
					case -41:
						break;
					case 41:
						{ return new Symbol(TokenConstants.POOL); }
					case -42:
						break;
					case 42:
						{ return new Symbol(TokenConstants.BOOL_CONST, Boolean.TRUE); }
					case -43:
						break;
					case 43:
						{ return new Symbol(TokenConstants.CLASS); }
					case -44:
						break;
					case 44:
						{ return new Symbol(TokenConstants.WHILE); }
					case -45:
						break;
					case 45:
						{ return new Symbol(TokenConstants.BOOL_CONST, Boolean.FALSE); }
					case -46:
						break;
					case 46:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -47:
						break;
					case 47:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -48:
						break;
					case 48:
						{ /* eat comments */ }
					case -49:
						break;
					case 49:
						{ }
					case -50:
						break;
					case 50:
						{ yybegin(YYINITIAL); }
					case -51:
						break;
					case 51:
						{ 
    string_buf.append(yytext()); 
}
					case -52:
						break;
					case 52:
						{ yybegin(YYINITIAL);
                                AbstractSymbol sym = AbstractTable.stringtable.addString(string_buf.toString());
                                return new Symbol(TokenConstants.STR_CONST, sym); }
					case -53:
						break;
					case 53:
						{ string_buf.append(yytext().charAt(1));  }
					case -54:
						break;
					case 54:
						{ string_buf.append("\t"); }
					case -55:
						break;
					case 55:
						{ string_buf.append("\f"); }
					case -56:
						break;
					case 56:
						{ string_buf.append("\n"); }
					case -57:
						break;
					case 57:
						{ string_buf.append("\n"); curr_lineno++; }
					case -58:
						break;
					case 58:
						{ string_buf.append("\b"); }
					case -59:
						break;
					case 60:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -60:
						break;
					case 61:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -61:
						break;
					case 62:
						{ return new Symbol(TokenConstants.IN); }
					case -62:
						break;
					case 63:
						{ return new Symbol(TokenConstants.IF); }
					case -63:
						break;
					case 64:
						{ return new Symbol(TokenConstants.FI); }
					case -64:
						break;
					case 65:
						{ return new Symbol(TokenConstants.OF); }
					case -65:
						break;
					case 66:
						{ return new Symbol(TokenConstants.LET); }
					case -66:
						break;
					case 67:
						{ return new Symbol(TokenConstants.NEW); }
					case -67:
						break;
					case 68:
						{ return new Symbol(TokenConstants.NOT); }
					case -68:
						break;
					case 69:
						{ return new Symbol(TokenConstants.CASE); }
					case -69:
						break;
					case 70:
						{ return new Symbol(TokenConstants.LOOP); }
					case -70:
						break;
					case 71:
						{ return new Symbol(TokenConstants.ELSE); }
					case -71:
						break;
					case 72:
						{ return new Symbol(TokenConstants.ESAC); }
					case -72:
						break;
					case 73:
						{ return new Symbol(TokenConstants.THEN); }
					case -73:
						break;
					case 74:
						{ return new Symbol(TokenConstants.POOL); }
					case -74:
						break;
					case 75:
						{ return new Symbol(TokenConstants.CLASS); }
					case -75:
						break;
					case 76:
						{ return new Symbol(TokenConstants.WHILE); }
					case -76:
						break;
					case 77:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -77:
						break;
					case 78:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -78:
						break;
					case 79:
						{ /* eat comments */ }
					case -79:
						break;
					case 80:
						{ 
    string_buf.append(yytext()); 
}
					case -80:
						break;
					case 82:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
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
    return new Symbol(TokenConstants.TYPEID, sym);
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
    return new Symbol(TokenConstants.TYPEID, sym);
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
    return new Symbol(TokenConstants.TYPEID, sym);
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
    return new Symbol(TokenConstants.TYPEID, sym);
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
    return new Symbol(TokenConstants.TYPEID, sym);
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
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -93:
						break;
					case 95:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -94:
						break;
					case 96:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -95:
						break;
					case 97:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -96:
						break;
					case 98:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -97:
						break;
					case 99:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -98:
						break;
					case 100:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -99:
						break;
					case 101:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -100:
						break;
					case 102:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -101:
						break;
					case 103:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -102:
						break;
					case 104:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -103:
						break;
					case 105:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -104:
						break;
					case 106:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -105:
						break;
					case 107:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -106:
						break;
					case 108:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -107:
						break;
					case 109:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -108:
						break;
					case 110:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -109:
						break;
					case 111:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -110:
						break;
					case 112:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -111:
						break;
					case 113:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -112:
						break;
					case 114:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -113:
						break;
					case 115:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -114:
						break;
					case 116:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -115:
						break;
					case 117:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -116:
						break;
					case 118:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -117:
						break;
					case 119:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -118:
						break;
					case 120:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -119:
						break;
					case 121:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -120:
						break;
					case 122:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -121:
						break;
					case 123:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -122:
						break;
					case 124:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -123:
						break;
					case 125:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -124:
						break;
					case 126:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -125:
						break;
					case 127:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -126:
						break;
					case 128:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -127:
						break;
					case 129:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -128:
						break;
					case 130:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -129:
						break;
					case 131:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -130:
						break;
					case 132:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -131:
						break;
					case 133:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -132:
						break;
					case 134:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -133:
						break;
					case 135:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -134:
						break;
					case 136:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -135:
						break;
					case 137:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -136:
						break;
					case 138:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -137:
						break;
					case 139:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -138:
						break;
					case 140:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -139:
						break;
					case 141:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -140:
						break;
					case 142:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -141:
						break;
					case 143:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -142:
						break;
					case 144:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -143:
						break;
					case 145:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -144:
						break;
					case 146:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -145:
						break;
					case 147:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -146:
						break;
					case 148:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -147:
						break;
					case 149:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -148:
						break;
					case 150:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -149:
						break;
					case 151:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -150:
						break;
					case 152:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -151:
						break;
					case 153:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -152:
						break;
					case 154:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -153:
						break;
					case 155:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -154:
						break;
					case 156:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -155:
						break;
					case 157:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -156:
						break;
					case 158:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -157:
						break;
					case 159:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -158:
						break;
					case 160:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -159:
						break;
					case 161:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -160:
						break;
					case 162:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -161:
						break;
					case 163:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -162:
						break;
					case 164:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -163:
						break;
					case 165:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -164:
						break;
					case 166:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -165:
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

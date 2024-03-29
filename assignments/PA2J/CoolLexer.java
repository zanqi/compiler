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
    boolean eof_flag = false;
    boolean null_char_error = false;
    int comment_lvl = 0;
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
		63,
		85
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
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NOT_ACCEPT,
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
		/* 85 */ YY_NOT_ACCEPT,
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
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"61:8,55:2,58,55:2,59,61:18,55,61,56,61:5,23,24,34,31,30,28,29,35,53:10,25,2" +
"6,27,32,62,61,17,3,52,1,16,8,11,52,7,5,52:2,2,52,6,13,14,52,9,4,10,19,15,12" +
",52:3,61,57,61:2,54,61,36,60,38,39,40,20,37,41,42,37:2,43,37,44,45,46,37,47" +
",48,18,49,50,51,37:3,21,61,22,33,61,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,172,
"0,1,2,1,3,1:2,4,1:3,5,6,1:3,7,1,8,1,9,1,10,1,11,12:3,1:3,13,1:2,12:9,14,12:" +
"2,14,12:2,1:14,15,16,17,18,14:16,19,20,21,22,23,24,25,26,27,28,29,30,31,32," +
"33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57," +
"58,59,60,12,61,62,14,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80," +
"81,82,83,84,85,86,87,88,89,90,91,92,93,94,12,14,95,96,97,98,99,100,101,102," +
"103")[0];

	private int yy_nxt[][] = unpackFromString(104,63,
"1,2,119,161:2,64,121,161,163,161,165,86,167,89,169,161:2,3,4,161,65,5,6,7,8" +
",9,10,11,12,13,14,15,16,17,18,19,162:2,164,162,166,162,87,120,122,90,168,16" +
"2:4,170,161,20,21,22,23,21,22:2,162,21:2,-1:64,161,171,123,161:13,-1,161:3," +
"-1:15,123,161:6,171,161:9,125:2,-1:5,161,-1:3,162:6,124,162,126,162:7,-1,16" +
"2:3,-1:15,162:5,124,162:5,126,162:5,128:2,-1:5,162,-1:36,28,-1:56,29,-1:3,3" +
"0,-1:58,31,-1:96,32,-1:24,33,-1:91,20,-1:64,22,-1:2,22:2,-1:4,161:6,145,161" +
":9,-1,161:3,-1:15,161:5,145,161:11,125:2,-1:5,161,-1:3,161:16,-1,161:3,-1:1" +
"5,161:17,125:2,-1:5,161,-1:3,31:57,-1:2,31:3,-1,162:16,-1,162:3,-1:15,162:1" +
"7,128:2,-1:5,162,-1:2,1,49:22,83,49:10,88,49:23,50:2,49:3,-1,161:3,129,161," +
"24,161:4,25,161:5,-1,161:2,25,-1:15,161:8,24,161:3,129,161:4,125:2,-1:5,161" +
",-1:3,162:2,130,162,68,162:11,-1,162:3,-1:15,130,162:5,68,162:10,128:2,-1:5" +
",162,-1:3,162:6,154,162:9,-1,162:3,-1:15,162:5,154,162:11,128:2,-1:5,162,-1" +
":36,51,-1:29,57:17,58,57,59,57:23,60,57:13,61,57,62,57:2,1,53:55,54,84,55,5" +
"6,53:3,-1,161:4,26,161:11,-1,161:3,-1:15,161:6,26,161:10,125:2,-1:5,161,-1:" +
"3,162:3,140,162,66,162:4,67,162:5,-1,162:2,67,-1:15,162:8,66,162:3,140,162:" +
"4,128:2,-1:5,162,-1:26,52,-1:39,161:10,27,161:5,-1,161:2,27,-1:15,161:17,12" +
"5:2,-1:5,161,-1:3,162:10,69,162:5,-1,162:2,69,-1:15,162:17,128:2,-1:5,162,-" +
"1:3,161:9,34,161:6,-1,34,161:2,-1:15,161:17,125:2,-1:5,161,-1:3,162:9,70,16" +
"2:6,-1,70,162:2,-1:15,162:17,128:2,-1:5,162,-1:3,161:11,35,161:4,-1,161:3,-" +
"1:15,161:15,35,161,125:2,-1:5,161,-1:3,162:11,71,162:4,-1,162:3,-1:15,162:1" +
"5,71,162,128:2,-1:5,162,-1:3,161:9,36,161:6,-1,36,161:2,-1:15,161:17,125:2," +
"-1:5,161,-1:3,162:9,72,162:6,-1,72,162:2,-1:15,162:17,128:2,-1:5,162,-1:3,1" +
"61:7,37,161:8,-1,161:3,-1:15,161:4,37,161:12,125:2,-1:5,161,-1:3,162:5,77,1" +
"62:10,-1,162:3,-1:15,162:8,77,162:8,128:2,-1:5,162,-1:3,161:13,38,161:2,-1," +
"161:3,-1:15,161:10,38,161:6,125:2,-1:5,161,-1:3,162:7,43,162:8,-1,162:3,-1:" +
"15,162:4,43,162:12,128:2,-1:5,162,-1:3,161:7,39,161:8,-1,161:3,-1:15,161:4," +
"39,161:12,125:2,-1:5,161,-1:3,162:7,73,162:8,-1,162:3,-1:15,162:4,73,162:12" +
",128:2,-1:5,162,-1:3,40,161:15,-1,161:3,-1:15,161:2,40,161:14,125:2,-1:5,16" +
"1,-1:3,162:7,75,162:8,-1,162:3,-1:15,162:4,75,162:12,128:2,-1:5,162,-1:3,16" +
"1:5,41,161:10,-1,161:3,-1:15,161:8,41,161:8,125:2,-1:5,161,-1:3,76,162:15,-" +
"1,162:3,-1:15,162:2,76,162:14,128:2,-1:5,162,-1:3,161,42,161:14,-1,161:3,-1" +
":15,161:7,42,161:9,125:2,-1:5,161,-1:3,162:13,74,162:2,-1,162:3,-1:15,162:1" +
"0,74,162:6,128:2,-1:5,162,-1:3,161:3,44,161:12,-1,161:3,-1:15,161:12,44,161" +
":4,125:2,-1:5,161,-1:3,162,78,162:14,-1,162:3,-1:15,162:7,78,162:9,128:2,-1" +
":5,162,-1:3,161:7,45,161:8,-1,161:3,-1:15,161:4,45,161:12,125:2,-1:5,161,-1" +
":3,162:7,46,162:8,-1,162:3,-1:15,162:4,46,162:12,128:2,-1:5,162,-1:3,161:15" +
",47,-1,161:3,-1:15,161:3,47,161:13,125:2,-1:5,161,-1:3,162:3,79,162:12,-1,1" +
"62:3,-1:15,162:12,79,162:4,128:2,-1:5,162,-1:3,161:3,48,161:12,-1,161:3,-1:" +
"15,161:12,48,161:4,125:2,-1:5,161,-1:3,162:7,80,162:8,-1,162:3,-1:15,162:4," +
"80,162:12,128:2,-1:5,162,-1:3,162:15,81,-1,162:3,-1:15,162:3,81,162:13,128:" +
"2,-1:5,162,-1:3,162:3,82,162:12,-1,162:3,-1:15,162:12,82,162:4,128:2,-1:5,1" +
"62,-1:3,161:7,91,161:4,127,161:3,-1,161:3,-1:15,161:4,91,161:4,127,161:7,12" +
"5:2,-1:5,161,-1:3,162:7,92,162:4,142,162:3,-1,162:3,-1:15,162:4,92,162:4,14" +
"2,162:7,128:2,-1:5,162,-1:3,161:7,93,161:4,95,161:3,-1,161:3,-1:15,161:4,93" +
",161:4,95,161:7,125:2,-1:5,161,-1:3,162:7,94,162:4,96,162:3,-1,162:3,-1:15," +
"162:4,94,162:4,96,162:7,128:2,-1:5,162,-1:3,161:3,97,161:12,-1,161:3,-1:15," +
"161:12,97,161:4,125:2,-1:5,161,-1:3,162:7,98,162:8,-1,162:3,-1:15,162:4,98," +
"162:12,128:2,-1:5,162,-1:3,162:16,-1,162,100,162,-1:15,162:13,100,162:3,128" +
":2,-1:5,162,-1:3,161:12,99,161:3,-1,161:3,-1:15,161:9,99,161:7,125:2,-1:5,1" +
"61,-1:3,161:14,143,161,-1,161:3,-1:15,161:14,143,161:2,125:2,-1:5,161,-1:3," +
"162,148,162:14,-1,162:3,-1:15,162:7,148,162:9,128:2,-1:5,162,-1:3,161:3,101" +
",161:12,-1,161:3,-1:15,161:12,101,161:4,125:2,-1:5,161,-1:3,162:2,150,162:1" +
"3,-1,162:3,-1:15,150,162:16,128:2,-1:5,162,-1:3,161:2,103,161:13,-1,161:3,-" +
"1:15,103,161:16,125:2,-1:5,161,-1:3,162:3,102,162:12,-1,162:3,-1:15,162:12," +
"102,162:4,128:2,-1:5,162,-1:3,161:7,105,161:8,-1,161:3,-1:15,161:4,105,161:" +
"12,125:2,-1:5,161,-1:3,162:3,104,162:12,-1,162:3,-1:15,162:12,104,162:4,128" +
":2,-1:5,162,-1:3,161:4,147,161:11,-1,161:3,-1:15,161:6,147,161:10,125:2,-1:" +
"5,161,-1:3,162:2,106,162:13,-1,162:3,-1:15,106,162:16,128:2,-1:5,162,-1:3,1" +
"61:12,107,161:3,-1,161:3,-1:15,161:9,107,161:7,125:2,-1:5,161,-1:3,162:14,1" +
"52,162,-1,162:3,-1:15,162:14,152,162:2,128:2,-1:5,162,-1:3,161:3,109,161:12" +
",-1,161:3,-1:15,161:12,109,161:4,125:2,-1:5,161,-1:3,162:12,108,162:3,-1,16" +
"2:3,-1:15,162:9,108,162:7,128:2,-1:5,162,-1:3,161:12,149,161:3,-1,161:3,-1:" +
"15,161:9,149,161:7,125:2,-1:5,161,-1:3,162:12,110,162:3,-1,162:3,-1:15,162:" +
"9,110,162:7,128:2,-1:5,162,-1:3,161:7,151,161:8,-1,161:3,-1:15,161:4,151,16" +
"1:12,125:2,-1:5,161,-1:3,162:4,156,162:11,-1,162:3,-1:15,162:6,156,162:10,1" +
"28:2,-1:5,162,-1:3,161,111,161:14,-1,161:3,-1:15,161:7,111,161:9,125:2,-1:5" +
",161,-1:3,162:3,112,162:12,-1,162:3,-1:15,162:12,112,162:4,128:2,-1:5,162,-" +
"1:3,161:4,113,161:11,-1,161:3,-1:15,161:6,113,161:10,125:2,-1:5,161,-1:3,16" +
"2:3,114,162:12,-1,162:3,-1:15,162:12,114,162:4,128:2,-1:5,162,-1:3,161:8,15" +
"3,161:7,-1,161:3,-1:15,161:11,153,161:5,125:2,-1:5,161,-1:3,162:12,157,162:" +
"3,-1,162:3,-1:15,162:9,157,162:7,128:2,-1:5,162,-1:3,161:4,155,161:11,-1,16" +
"1:3,-1:15,161:6,155,161:10,125:2,-1:5,161,-1:3,162:7,158,162:8,-1,162:3,-1:" +
"15,162:4,158,162:12,128:2,-1:5,162,-1:3,161:9,115,161:6,-1,115,161:2,-1:15," +
"161:17,125:2,-1:5,161,-1:3,162,116,162:14,-1,162:3,-1:15,162:7,116,162:9,12" +
"8:2,-1:5,162,-1:3,162:4,117,162:11,-1,162:3,-1:15,162:6,117,162:10,128:2,-1" +
":5,162,-1:3,162:8,159,162:7,-1,162:3,-1:15,162:11,159,162:5,128:2,-1:5,162," +
"-1:3,162:4,160,162:11,-1,162:3,-1:15,162:6,160,162:10,128:2,-1:5,162,-1:3,1" +
"62:9,118,162:6,-1,118,162:2,-1:15,162:17,128:2,-1:5,162,-1:3,161,131,161,13" +
"3,161:12,-1,161:3,-1:15,161:7,131,161:4,133,161:4,125:2,-1:5,161,-1:3,162,1" +
"32,134,162:13,-1,162:3,-1:15,134,162:6,132,162:9,128:2,-1:5,162,-1:3,161:6," +
"135,161:9,-1,161:3,-1:15,161:5,135,161:11,125:2,-1:5,161,-1:3,162,136,162,1" +
"38,162:12,-1,162:3,-1:15,162:7,136,162:4,138,162:4,128:2,-1:5,162,-1:3,161:" +
"6,137,161:9,-1,161:3,-1:15,161:5,137,161:11,125:2,-1:5,161,-1:3,162:12,144," +
"162:3,-1,162:3,-1:15,162:9,144,162:7,128:2,-1:5,162,-1:3,161:12,139,161:3,-" +
"1,161:3,-1:15,161:9,139,161:7,125:2,-1:5,161,-1:3,162:6,146,162:9,-1,162:3," +
"-1:15,162:5,146,162:11,128:2,-1:5,162,-1:3,161:2,141,161:13,-1,161:3,-1:15," +
"141,161:16,125:2,-1:5,161,-1:2");

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
    if (eof_flag) {
        return new Symbol(TokenConstants.EOF);
    }
    eof_flag = true;
    switch(yy_lexical_state) {
    case YYINITIAL:
	/* nothing special to do in the initial state */
	break;
	/* If necessary, add code for other states here, e.g:
	   case COMMENT:
	   ...
	   break;
	*/
    case STRING:
        return new Symbol(TokenConstants.ERROR, "EOF in string constant");
    case COMMENT:
        return new Symbol(TokenConstants.ERROR, "EOF in comment");
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
                                  return new Symbol(TokenConstants.ERROR, yytext()); }
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
						{ return new Symbol(TokenConstants.ERROR, "Unmatched *)");}
					case -34:
						break;
					case 34:
						{ return new Symbol(TokenConstants.LET); }
					case -35:
						break;
					case 35:
						{ return new Symbol(TokenConstants.NEW); }
					case -36:
						break;
					case 36:
						{ return new Symbol(TokenConstants.NOT); }
					case -37:
						break;
					case 37:
						{ return new Symbol(TokenConstants.CASE); }
					case -38:
						break;
					case 38:
						{ return new Symbol(TokenConstants.LOOP); }
					case -39:
						break;
					case 39:
						{ return new Symbol(TokenConstants.ELSE); }
					case -40:
						break;
					case 40:
						{ return new Symbol(TokenConstants.ESAC); }
					case -41:
						break;
					case 41:
						{ return new Symbol(TokenConstants.THEN); }
					case -42:
						break;
					case 42:
						{ return new Symbol(TokenConstants.POOL); }
					case -43:
						break;
					case 43:
						{ return new Symbol(TokenConstants.BOOL_CONST, Boolean.TRUE); }
					case -44:
						break;
					case 44:
						{ return new Symbol(TokenConstants.CLASS); }
					case -45:
						break;
					case 45:
						{ return new Symbol(TokenConstants.WHILE); }
					case -46:
						break;
					case 46:
						{ return new Symbol(TokenConstants.BOOL_CONST, Boolean.FALSE); }
					case -47:
						break;
					case 47:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -48:
						break;
					case 48:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -49:
						break;
					case 49:
						{ /* eat comments */ }
					case -50:
						break;
					case 50:
						{ }
					case -51:
						break;
					case 51:
						{ comment_lvl++; }
					case -52:
						break;
					case 52:
						{ 
    if (comment_lvl == 0) {
        yybegin(YYINITIAL);
    } else {
        comment_lvl--;
    }
}
					case -53:
						break;
					case 53:
						{ 
    if (yytext().charAt(0) == '\0') {
        null_char_error = true;
    }
    else {
        string_buf.append(yytext()); 
    }
}
					case -54:
						break;
					case 54:
						{ 
    yybegin(YYINITIAL);
    if (null_char_error) {
        null_char_error = false;
        return new Symbol(TokenConstants.ERROR, "String contains null character");
    }
    if (string_buf.length() >= MAX_STR_CONST) {
        return new Symbol(TokenConstants.ERROR, "String constant too long");
    }
    AbstractSymbol sym = AbstractTable.stringtable.addString(string_buf.toString());
    return new Symbol(TokenConstants.STR_CONST, sym); 
}
					case -55:
						break;
					case 55:
						{ yybegin(YYINITIAL); return new Symbol(TokenConstants.ERROR, "Unterminated string constant"); }
					case -56:
						break;
					case 56:
						{ string_buf.append("\r"); }
					case -57:
						break;
					case 57:
						{ 
    if (yytext().charAt(1) == '\0') {
        null_char_error = true;
    }
    else {
        string_buf.append(yytext().charAt(1));
    }
}
					case -58:
						break;
					case 58:
						{ string_buf.append("\t"); }
					case -59:
						break;
					case 59:
						{ string_buf.append("\f"); }
					case -60:
						break;
					case 60:
						{ string_buf.append("\n"); }
					case -61:
						break;
					case 61:
						{ string_buf.append("\n"); }
					case -62:
						break;
					case 62:
						{ string_buf.append("\b"); }
					case -63:
						break;
					case 64:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -64:
						break;
					case 65:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -65:
						break;
					case 66:
						{ return new Symbol(TokenConstants.IN); }
					case -66:
						break;
					case 67:
						{ return new Symbol(TokenConstants.IF); }
					case -67:
						break;
					case 68:
						{ return new Symbol(TokenConstants.FI); }
					case -68:
						break;
					case 69:
						{ return new Symbol(TokenConstants.OF); }
					case -69:
						break;
					case 70:
						{ return new Symbol(TokenConstants.LET); }
					case -70:
						break;
					case 71:
						{ return new Symbol(TokenConstants.NEW); }
					case -71:
						break;
					case 72:
						{ return new Symbol(TokenConstants.NOT); }
					case -72:
						break;
					case 73:
						{ return new Symbol(TokenConstants.CASE); }
					case -73:
						break;
					case 74:
						{ return new Symbol(TokenConstants.LOOP); }
					case -74:
						break;
					case 75:
						{ return new Symbol(TokenConstants.ELSE); }
					case -75:
						break;
					case 76:
						{ return new Symbol(TokenConstants.ESAC); }
					case -76:
						break;
					case 77:
						{ return new Symbol(TokenConstants.THEN); }
					case -77:
						break;
					case 78:
						{ return new Symbol(TokenConstants.POOL); }
					case -78:
						break;
					case 79:
						{ return new Symbol(TokenConstants.CLASS); }
					case -79:
						break;
					case 80:
						{ return new Symbol(TokenConstants.WHILE); }
					case -80:
						break;
					case 81:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -81:
						break;
					case 82:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -82:
						break;
					case 83:
						{ /* eat comments */ }
					case -83:
						break;
					case 84:
						{ 
    if (yytext().charAt(0) == '\0') {
        null_char_error = true;
    }
    else {
        string_buf.append(yytext()); 
    }
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
						{ /* eat comments */ }
					case -87:
						break;
					case 89:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
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
    return new Symbol(TokenConstants.TYPEID, sym);
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
    return new Symbol(TokenConstants.TYPEID, sym);
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
					case 95:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -94:
						break;
					case 96:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -95:
						break;
					case 97:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -96:
						break;
					case 98:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -97:
						break;
					case 99:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -98:
						break;
					case 100:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -99:
						break;
					case 101:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -100:
						break;
					case 102:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -101:
						break;
					case 103:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -102:
						break;
					case 104:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -103:
						break;
					case 105:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -104:
						break;
					case 106:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -105:
						break;
					case 107:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -106:
						break;
					case 108:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -107:
						break;
					case 109:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -108:
						break;
					case 110:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -109:
						break;
					case 111:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
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
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -112:
						break;
					case 114:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -113:
						break;
					case 115:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -114:
						break;
					case 116:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
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
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -117:
						break;
					case 119:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -118:
						break;
					case 120:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -119:
						break;
					case 121:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -120:
						break;
					case 122:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -121:
						break;
					case 123:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -122:
						break;
					case 124:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -123:
						break;
					case 125:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -124:
						break;
					case 126:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -125:
						break;
					case 127:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -126:
						break;
					case 128:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -127:
						break;
					case 129:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -128:
						break;
					case 130:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -129:
						break;
					case 131:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -130:
						break;
					case 132:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -131:
						break;
					case 133:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -132:
						break;
					case 134:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -133:
						break;
					case 135:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -134:
						break;
					case 136:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -135:
						break;
					case 137:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -136:
						break;
					case 138:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -137:
						break;
					case 139:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -138:
						break;
					case 140:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -139:
						break;
					case 141:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -140:
						break;
					case 142:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -141:
						break;
					case 143:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -142:
						break;
					case 144:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -143:
						break;
					case 145:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -144:
						break;
					case 146:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -145:
						break;
					case 147:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -146:
						break;
					case 148:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -147:
						break;
					case 149:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -148:
						break;
					case 150:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -149:
						break;
					case 151:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
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
    return new Symbol(TokenConstants.TYPEID, sym);
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
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -154:
						break;
					case 156:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
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
    return new Symbol(TokenConstants.OBJECTID, sym);
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
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -159:
						break;
					case 161:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -160:
						break;
					case 162:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -161:
						break;
					case 163:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -162:
						break;
					case 164:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -163:
						break;
					case 165:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -164:
						break;
					case 166:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -165:
						break;
					case 167:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -166:
						break;
					case 168:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -167:
						break;
					case 169:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -168:
						break;
					case 170:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
					case -169:
						break;
					case 171:
						{ 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
					case -170:
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

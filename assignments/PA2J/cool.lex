/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%{

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
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

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
%eofval}

%class CoolLexer
%cup
%state COMMENT
%state STRING
%line

digit = [0-9]
letter = [a-zA-Z]
lowercase = [a-z]
capital = [A-Z]
space = [ ]
underscore = [_]
newline = [\n]
whitespace = [ \t\n]
backslash = [\\]
quote = [\"]

%%

<YYINITIAL>"class"         { return new Symbol(TokenConstants.CLASS); }
<YYINITIAL>"inherits"      { return new Symbol(TokenConstants.INHERITS); }
<YYINITIAL>"if"            { return new Symbol(TokenConstants.IF); }
<YYINITIAL>"fi"            { return new Symbol(TokenConstants.FI); }
<YYINITIAL>"then"          { return new Symbol(TokenConstants.THEN); }
<YYINITIAL>"else"          { return new Symbol(TokenConstants.ELSE); }
<YYINITIAL>"new"           { return new Symbol(TokenConstants.NEW); }
<YYINITIAL>"while"         { return new Symbol(TokenConstants.WHILE); }
<YYINITIAL>"not"           { return new Symbol(TokenConstants.NOT); }
<YYINITIAL>"loop"           { return new Symbol(TokenConstants.LOOP); }
<YYINITIAL>"pool"           { return new Symbol(TokenConstants.POOL); }
<YYINITIAL>"let"           { return new Symbol(TokenConstants.LET); }
<YYINITIAL>"in"           { return new Symbol(TokenConstants.IN); }
<YYINITIAL>"true"           { return new Symbol(TokenConstants.BOOL_CONST, Boolean.TRUE); }
<YYINITIAL>"false"           { return new Symbol(TokenConstants.BOOL_CONST, Boolean.FALSE); }

<YYINITIAL>"{"             { return new Symbol(TokenConstants.LBRACE); }
<YYINITIAL>"}"             { return new Symbol(TokenConstants.RBRACE); }
<YYINITIAL>"("             { return new Symbol(TokenConstants.LPAREN); }
<YYINITIAL>")"             { return new Symbol(TokenConstants.RPAREN); }
<YYINITIAL>":"             { return new Symbol(TokenConstants.COLON); }
<YYINITIAL>";"             { return new Symbol(TokenConstants.SEMI); }
<YYINITIAL>"<-"            { return new Symbol(TokenConstants.ASSIGN); }
<YYINITIAL>"."             { return new Symbol(TokenConstants.DOT); }
<YYINITIAL>","             { return new Symbol(TokenConstants.COMMA); }
<YYINITIAL>"+"             { return new Symbol(TokenConstants.PLUS); }
<YYINITIAL>"-"             { return new Symbol(TokenConstants.MINUS); }
<YYINITIAL>"="             { return new Symbol(TokenConstants.EQ); }
<YYINITIAL>"<"             { return new Symbol(TokenConstants.LT); }
<YYINITIAL>"<="            { return new Symbol(TokenConstants.LE); }
<YYINITIAL>"~"             { return new Symbol(TokenConstants.NEG); }
<YYINITIAL>"*"             { return new Symbol(TokenConstants.MULT); }
<YYINITIAL>"/"             { return new Symbol(TokenConstants.DIV); }
<YYINITIAL>{lowercase}({letter}|{digit}|{underscore})* { 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.OBJECTID, sym);
}
<YYINITIAL>{capital}({letter}|{digit}|{underscore})* { 
    AbstractSymbol sym = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, sym);
}
<YYINITIAL>{digit}+        { 
    IntSymbol sym = (IntSymbol)AbstractTable.inttable.addString(yytext());
    return new Symbol(TokenConstants.INT_CONST, sym);
}

<YYINITIAL>{whitespace}+   { /* eat whitespace */ }

<YYINITIAL>{quote}              { yybegin(STRING); string_buf.setLength(0); }
<STRING>{backslash}\n           { string_buf.append("\n"); curr_lineno++; }
<STRING>"\n"                    { string_buf.append("\n"); }
<STRING>"\t"                    { string_buf.append("\t"); }
<STRING>"\b"                    { string_buf.append("\b"); }
<STRING>"\f"                    { string_buf.append("\f"); }
<STRING>{backslash}[^ntbf]      { string_buf.append(yytext().charAt(1));  }
<STRING>{quote}                 { yybegin(YYINITIAL);
                                AbstractSymbol sym = AbstractTable.stringtable.addString(string_buf.toString());
                                return new Symbol(TokenConstants.STR_CONST, sym); }
<STRING>.  { 
    string_buf.append(yytext()); 
}

<YYINITIAL>"(*"                 { yybegin(COMMENT); }
<COMMENT>.                      { /* eat comments */ }
<COMMENT>\n                     { }
<COMMENT>"*)"                   { yybegin(YYINITIAL); }
<YYINITIAL>"--".*               { /* eat comments */ }




<YYINITIAL>"=>"			{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }

.                               { /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("#" + (yyline+1) + " LEXER BUG - UNMATCHED: " + yytext()); }

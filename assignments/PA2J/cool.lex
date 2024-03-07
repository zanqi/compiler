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
whitespace = [ \t\n\r\f\b\013]
backslash = [\\]
quote = [\"]
a = [aA]
b = [bB]
c = [cC]
d = [dD]
e = [eE]
f = [fF]
g = [gG]
h = [hH]
i = [iI]
j = [jJ]
k = [kK]
l = [lL]
m = [mM]
n = [nN]
o = [oO]
p = [pP]
q = [qQ]
r = [rR]
s = [sS]
t = [tT]
u = [uU]
v = [vV]
w = [wW]
x = [xX]
y = [yY]
z = [zZ]

%%

<YYINITIAL>{c}{l}{a}{s}{s}         { return new Symbol(TokenConstants.CLASS); }
<YYINITIAL>{i}{n}{h}{e}{r}{i}{t}{s}      { return new Symbol(TokenConstants.INHERITS); }
<YYINITIAL>{i}{f}            { return new Symbol(TokenConstants.IF); }
<YYINITIAL>{f}{i}            { return new Symbol(TokenConstants.FI); }
<YYINITIAL>{t}{h}{e}{n}          { return new Symbol(TokenConstants.THEN); }
<YYINITIAL>{e}{l}{s}{e}          { return new Symbol(TokenConstants.ELSE); }
<YYINITIAL>{n}{e}{w}          { return new Symbol(TokenConstants.NEW); }
<YYINITIAL>{w}{h}{i}{l}{e}         { return new Symbol(TokenConstants.WHILE); }
<YYINITIAL>{n}{o}{t}           { return new Symbol(TokenConstants.NOT); }
<YYINITIAL>{l}{o}{o}{p}          { return new Symbol(TokenConstants.LOOP); }
<YYINITIAL>{p}{o}{o}{l}          { return new Symbol(TokenConstants.POOL); }
<YYINITIAL>{l}{e}{t}           { return new Symbol(TokenConstants.LET); }
<YYINITIAL>{i}{n}            { return new Symbol(TokenConstants.IN); }
<YYINITIAL>{i}{s}{v}{o}{i}{d}        { return new Symbol(TokenConstants.ISVOID); }
<YYINITIAL>{c}{a}{s}{e}        { return new Symbol(TokenConstants.CASE); }
<YYINITIAL>{e}{s}{a}{c}        { return new Symbol(TokenConstants.ESAC); }
<YYINITIAL>{o}{f}        { return new Symbol(TokenConstants.OF); }
<YYINITIAL>"@"        { return new Symbol(TokenConstants.AT); }
<YYINITIAL>t{r}{u}{e}          { return new Symbol(TokenConstants.BOOL_CONST, Boolean.TRUE); }
<YYINITIAL>f{a}{l}{s}{e}           { return new Symbol(TokenConstants.BOOL_CONST, Boolean.FALSE); }

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
<STRING>{backslash}\n           { string_buf.append("\n"); }
<STRING>\r                      { string_buf.append("\r"); }
<STRING>\n                      { yybegin(YYINITIAL); return new Symbol(TokenConstants.ERROR, "Unterminated string constant"); }
<STRING>"\n"                    { string_buf.append("\n"); }
<STRING>"\t"                    { string_buf.append("\t"); }
<STRING>"\b"                    { string_buf.append("\b"); }
<STRING>"\f"                    { string_buf.append("\f"); }
<STRING>{backslash}[^ntbf]      { 
    if (yytext().charAt(1) == '\0') {
        null_char_error = true;
    }
    else {
        string_buf.append(yytext().charAt(1));
    }
}
<STRING>{quote}                 { 
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
<STRING>.  { 
    if (yytext().charAt(0) == '\0') {
        null_char_error = true;
    }
    else {
        string_buf.append(yytext()); 
    }
}

<YYINITIAL>"(*"                 { yybegin(COMMENT); }
<COMMENT>"(*"                   { comment_lvl++; }
<COMMENT>.                      { /* eat comments */ }
<COMMENT>{whitespace}           { }
<COMMENT>"*)"                   { 
    if (comment_lvl == 0) {
        yybegin(YYINITIAL);
    } else {
        comment_lvl--;
    }
}
<YYINITIAL>"--".*               { /* eat comments */ }
<YYINITIAL>"*)"                 { return new Symbol(TokenConstants.ERROR, "Unmatched *)");}




<YYINITIAL>"=>"			{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }

.                               { /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  return new Symbol(TokenConstants.ERROR, yytext()); }

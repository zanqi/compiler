import java_cup.runtime.Symbol;

%%

%class CalcLexer
%cup
%line

%%
"print"             { return new Symbol(sym.PRINT); }
"exit"              { return new Symbol(sym.EXIT_COMMAND); }
[a-zA-z]            { return new Symbol(sym.ID, yytext().charAt(0)); }
[0-9]+              { return new Symbol(sym.NUMBER, Integer.parseInt(yytext())); }
[ \t\n]             { /* ignore whitespace */ }
"+"                 { return new Symbol(sym.PLUS); }
"-"                 { return new Symbol(sym.MINUS); }
"="                 { return new Symbol(sym.EQ); }
";"                 { return new Symbol(sym.SEMI); }
.                   { throw new Error("Illegal character <" + yytext() + ">"); }
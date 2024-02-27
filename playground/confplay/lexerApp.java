package confplay;

import java.io.IOException;
import java.util.HashMap;

public class lexerApp {
    public static void main(String[] args) throws IOException {
        ConfigLexer lexer = new ConfigLexer(System.in);
        Yytoken token = lexer.yylex();
        String[] fields = {"", ":", "db_type", "db_name", "db_table_prefix", "db_port"};

        while (token != null) {
            if (lexer.yylex().field != TokenConstants.COLON) {
                System.out.println("Expected colon");
                return;
            }
            Yytoken value = lexer.yylex();
            switch (token.field) {
                case TokenConstants.TYPE:
                case TokenConstants.NAME:
                case TokenConstants.TABLE_PREFIX:
                    if (value.field != TokenConstants.IDENTIFIER) {
                        System.out.println("Expected identifier");
                        return;
                    }
                    System.out.println(fields[token.field] + " = " + value.text);
                    break;
                case TokenConstants.PORT:
                    if (value.field != TokenConstants.INTEGER) {
                        System.out.println("Expected integer");
                        return;
                    }
                    System.out.println(fields[token.field] + " = " + value.text);
                    break;
                default:
                    break;
            }
            token = lexer.yylex();
        }
    }
}

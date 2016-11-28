package parser;

import java.io.InputStream;

import static parser.Lexer.EOF;
import static parser.Utils.list;

public class Parser {
    Lexer lexer;

    Parser(InputStream in) {
        lexer = new Lexer(in);
    }

    Object parse() {
        try {
            Object token = lexer.nextToken();
            if (token instanceof Character) {
                switch ((char) token) {
                    case '(':
                        Pair head = new Pair(null, null);
                        Pair prev = head;
                        token = parse();
                        while (token != EOF && !(token instanceof Character && (char)token == ')')) {
                            prev.second = new Pair(token, null);
                            prev = (Pair) prev.second;
                            token = parse();
                        }

                        if (token == EOF) {
                            throw new ParseException("expect ),not complete...");
                        } else {
                            lexer.SaveToken(token);
                            return head.second;
                        }
                    case ')':
                        return ')';
                    case '\'':
                        return list("quote", parse());
                    default:
                        throw new ParseException(token+" not supported ...");
                }
            }else{
                return token;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }
}
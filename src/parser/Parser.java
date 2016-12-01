package parser;

import scheme.Pair;

import java.io.InputStream;

import static parser.Lexer.EOF;
import static scheme.Utils.list;

public class Parser {
    Lexer lexer;

    public Parser(InputStream in) {
        lexer = new Lexer(in);
    }

    public Object parse() {
        try {
            Object token = lexer.nextToken();
            if (token instanceof Character) {
                switch ((char) token) {
                    case '(':
                        return readList();
                    case ')':
                        return ')';
                    case '\'':
                        return list("quote", parse());
                    default:
                        throw new ParseException(token + "is not supported ...");
                }
            } else {
                return token;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object readList() throws ParseException {
        Pair head = new Pair(null, null);
        Pair prev = head;
        Object token = parse();
        while (token != EOF && !(token instanceof Character && (char) token == ')')) {
            prev.second = new Pair(token, null);
            prev = (Pair) prev.second;
            token = parse();
        }

        if (token == EOF)
            throw new ParseException("expect ),giving EOF...");

        return head.second;
    }
}

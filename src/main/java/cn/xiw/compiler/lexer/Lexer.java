package cn.xiw.compiler.lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class Lexer {
    public static int line = 1;

    private final BufferedReader reader;
    private char peek = ' ';

    private final Map<String, Integer> keywords = Map.of("break", Tag.BREAK,
            "char", Tag.CHAR, "else", Tag.ELSE, "if", Tag.IF, "int", Tag.INT,
            "while", Tag.WHILE, "float", Tag.FLOAT);

    public Lexer(InputStream in) {
        this.reader = new BufferedReader(new InputStreamReader(in));
    }

    public Token scan() throws IOException {
        // 0. skip spaces
        while (Character.isWhitespace(peek)) {
            if (peek == '\n') {
                line++;
            }
            readch();
        }
        // 1. check eof
        if (peek == 0xffff) {
            return Token.builder().type(TokenType.EOF).build();
        }
        // 2. check keywords and identifiers
        // id: letter(letterordigit)+
        if (Character.isLetter(peek)) {
            return identifier();
        }
        // 3. check constants
        // char: 'letter', escaped char is not supported yet
        // int: (0-9)*
        // float: (int)?.(0-9)*
        if (peek == '\'' || Character.isDigit(peek) || peek == '.') {
            return constant();
        }
        // 4. check string literals
        // string: "(letter)*"
        if (peek == '\"') {
            return stringLiteral();
        }
        // 5. check punctuators
        return punctuator();
    }

    private void readch() throws IOException {
        peek = (char) reader.read();
    }

    // read, match, consume
    private boolean readch(char expected) throws IOException {
        readch();
        if (expected == peek) {
            readch();
            return true;
        }
        return false;
    }

    /**
     * Returns identifiers and keywords.
     */
    private Token identifier() throws IOException {
        var strBuilder = new StringBuilder();
        while (Character.isLetterOrDigit(peek)) {
            strBuilder.append(peek);
            readch();
        }
        var id = strBuilder.toString();
        // 1. keywords
        if (keywords.containsKey(id)) {
            return new Token.TokenBuilder().type(TokenType.KEYWORD)
                    .keywordId(keywords.get(id)).build();
        }
        // 2. id
        return new Token.TokenBuilder().type(TokenType.IDENTIFIER)
                .identifierString(id).build();
    }

    /**
     * Returns constants: character, int, and float.
     */
    private Token constant() throws IOException {
        // char: 'letter', escaped char is not supported yet
        if (peek == '\'') {
            readch();
            var normalChar = new Token.TokenBuilder().type(TokenType.CONSTANT)
                    .constantId(Tag.CHAR).valueChar(peek).build();
            if (!readch('\'')) {
                error("Expect a \'");
            }
            return normalChar;
        }
        // int: (0-9)*
        // float: (int)?.(0-9)*
        if (Character.isDigit(peek) || peek == '.') {
            int value = 0;
            while (Character.isDigit(peek)) {
                value = value * 10 + peek - '0';
                readch();
            }
            // float
            if (peek == '.') {
                double floatValue = value;
                double decimal = .1;
                while (Character.isDigit(peek)) {
                    floatValue += decimal * (peek - '0');
                    decimal /= 10;
                    readch();
                }
                return new Token.TokenBuilder().type(TokenType.CONSTANT)
                        .constantId(Tag.FLOAT).valueFloat(floatValue).build();
            }
            return new Token.TokenBuilder().type(TokenType.CONSTANT)
                    .constantId(Tag.INT).valueInt(value).build();
        }
        error("Invalid number");
        return null;
    }

    /**
     * Returns string literals.
     */
    private Token stringLiteral() throws IOException {
        var strBuilder = new StringBuilder();
        while (peek != '\'') {
            strBuilder.append(peek);
            readch();
        }

        return new Token.TokenBuilder().type(TokenType.STRING)
                .valueString(strBuilder.toString()).build();
    }

    /**
     * Returns punctuators.
     */
    private Token punctuator() throws IOException {
        var tokenBuilder = new Token.TokenBuilder().type(TokenType.PUNCTUATOR);
        int punctuatorId = -1;
        switch (peek) {
        case '[':
        case ']':
        case '(':
        case ')':
        case '{':
        case '}':
        case '*':
        case '/':
        case '%':
        case '+':
        case '-':
            punctuatorId = peek;
            break;
        case '<':
            punctuatorId = readch('=') ? Tag.LE : '<';
            break;
        case '>':
            punctuatorId = readch('=') ? Tag.GE : '>';
            break;
        case '=':
            punctuatorId = readch('=') ? Tag.EQ : '=';
            break;
        case '&':
            punctuatorId = readch('&') ? Tag.AND : '&';
            break;
        case '!':
            punctuatorId = readch('=') ? Tag.NE : '!';
            break;
        case '|':
            punctuatorId = readch('|') ? Tag.OR : '|';
            break;
        default:
            error("Invalid punctuator");
            break;
        }
        return tokenBuilder.punctuatorId(punctuatorId).build();
    }

    private void error(String message) {
        throw new LexException(
                String.format("Lexical error at line %d: %s", line, message));
    }
}

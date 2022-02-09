package cn.xiw.compiler.lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Lexer {
    public static int line = 1;

    private final BufferedReader reader;
    private int peek = ' ';

    private TokenTypeUtil tokenTypeUtil = TokenTypeUtil.instance();

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
        if (peek == -1) {
            return Token.eofTok();
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

    /**
     * Reads in a new character.
     * 
     * @throws IOException
     */
    private void readch() throws IOException {
        peek = reader.read();
    }

    /**
     * Matches the expected char. If matched, consumes it and reads a new one;
     * else does nothing.
     * 
     * @param expected expected char
     * @return true if consumed expected char successfully
     * @throws IOException
     */
    private boolean readch(char expected) throws IOException {
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
        while (Character.isLetterOrDigit((char) peek)) {
            strBuilder.append((char) peek);
            readch();
        }
        var id = strBuilder.toString();
        // 1. keywords
        if (tokenTypeUtil.isKeyword(id)) {
            return Token.keywordTok(id);
        }
        // 2. id
        return Token.identifierTok(id);
    }

    /**
     * Returns constants: character, int, and float.
     */
    private Token constant() throws IOException {
        // char: 'letter', escaped char is not supported yet
        if (readch('\'')) {
            var normalChar = Token.constCharTok((char) peek);
            readch();
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
            if (readch('.')) {
                double floatValue = value;
                double decimal = .1;
                while (Character.isDigit(peek)) {
                    floatValue += decimal * (peek - '0');
                    decimal /= 10;
                    readch();
                }
                return Token.constFloatTok(floatValue);
            }
            return Token.constIntTok(value);
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
            strBuilder.append((char) peek);
            readch();
        }

        return Token.stringLiteralTok(strBuilder.toString());
    }

    /**
     * Returns punctuators.
     */
    private Token punctuator() throws IOException {
        String punctuator = "" + (char) peek;
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
        case ';':
            readch();
            break;
        case '<':
        case '>':
        case '=':
        case '!':
            readch();
            if (readch('='))
                punctuator += '=';
            break;
        case '&':
            readch();
            if (readch('&'))
                punctuator += '&';
            break;
        case '|':
            readch();
            if (readch('|'))
                punctuator += '|';
            break;
        default:
            error("Invalid punctuator");
            break;
        }
        return Token.punctTok(punctuator);
    }

    private void error(String message) {
        throw new LexException(
                String.format("Lexical error at line %d: %s", line, message));
    }
}

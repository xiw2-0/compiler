package cn.xiw.compiler.lexer;

import java.util.ArrayList;

import lombok.Getter;

public class Token {
    @Getter
    private final TokenType type;

    private final int index;

    @Getter
    private final int tokenLine;

    @Getter
    private static int lineNum = 0;

    static void incLineNum() {
        lineNum++;
    }

    private static ArrayList<String> stringTable = new ArrayList<>();

    private static TokenTypeUtil tokenTypeUtil = TokenTypeUtil.instance();

    private Token(TokenType type, int index) {
        this.type = type;
        this.index = index;
        tokenLine = lineNum;
    }

    public static Token eofTok() {
        return new Token(TokenType.EOF, -1);
    }

    public static Token identifierTok(String id) {
        stringTable.add(id);
        return new Token(TokenType.IDENTIFIER, stringTable.size() - 1);
    }

    public static Token constIntTok(int value) {
        stringTable.add(Integer.toString(value));
        return new Token(TokenType.CONST_INT, stringTable.size() - 1);
    }

    public static Token constCharTok(char ch) {
        stringTable.add("" + ch);
        return new Token(TokenType.CONST_CHAR, stringTable.size() - 1);
    }

    public static Token constFloatTok(double value) {
        stringTable.add(Double.toString(value));
        return new Token(TokenType.CONST_CHAR, stringTable.size() - 1);
    }

    public static Token stringLiteralTok(String str) {
        stringTable.add(str);
        return new Token(TokenType.CONST_CHAR, stringTable.size() - 1);
    }

    public static Token punctTok(String punctuator) {
        return new Token(tokenTypeUtil.getTokenType(punctuator), -1);
    }

    public static Token keywordTok(String keyword) {
        return new Token(tokenTypeUtil.getTokenType(keyword), -1);
    }

    // for identifier
    public String identifier() {
        return stringTable.get(index);
    }

    // for constants.
    public int valueInt() {
        return Integer.parseInt(stringTable.get(index));
    }

    public double valueFloat() {
        return Double.parseDouble(stringTable.get(index));
    }

    public char valueChar() {
        return stringTable.get(index).charAt(0);
    }

    // for string literals
    public String valueString() {
        return stringTable.get(index);
    }

    /**
     * Ignores the @attribute tokenLine when comparing.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Token) {
            var tokObj = (Token) obj;
            return tokObj.type == type && (tokObj.index == -1 && index == -1
                    || tokObj.index >= 0 && index >= 0 && stringTable
                            .get(tokObj.index).equals(stringTable.get(index)));
        }
        return false;
    }

    @Override
    public String toString() {
        String value = index == -1 ? "" : stringTable.get(index);
        return String.format("(%s: %s)", type.toString(), value);
    }

}

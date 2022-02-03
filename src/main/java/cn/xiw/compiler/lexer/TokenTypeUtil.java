package cn.xiw.compiler.lexer;

import java.util.HashMap;
import java.util.Map;

public class TokenTypeUtil {
    private final Map<String, TokenType> punctMapping = new HashMap<>();
    private final Map<String, TokenType> keywordMapping = new HashMap<>();

    private static TokenTypeUtil instance = new TokenTypeUtil();

    private TokenTypeUtil() {
        punctMapping.put("[", TokenType.PUNCT_L_SQR);
        punctMapping.put("]", TokenType.PUNCT_R_SQR);
        punctMapping.put("(", TokenType.PUNCT_L_PAR);
        punctMapping.put(")", TokenType.PUNCT_R_PAR);
        punctMapping.put("{", TokenType.PUNCT_L_BAR);
        punctMapping.put("}", TokenType.PUNCT_R_BAR);
        punctMapping.put("*", TokenType.PUNCT_STAR);
        punctMapping.put("+", TokenType.PUNCT_PLUS);
        punctMapping.put("-", TokenType.PUNCT_MINUS);
        punctMapping.put("!", TokenType.PUNCT_NOT);
        punctMapping.put("!=", TokenType.PUNCT_NOT_EQ);
        punctMapping.put("/", TokenType.PUNCT_DIV);
        punctMapping.put("%", TokenType.PUNCT_MOD);
        punctMapping.put("<", TokenType.PUNCT_LT);
        punctMapping.put("<=", TokenType.PUNCT_LE);
        punctMapping.put(">", TokenType.PUNCT_GT);
        punctMapping.put(">=", TokenType.PUNCT_GE);
        punctMapping.put("|", TokenType.PUNCT_BIT_OR);
        punctMapping.put("||", TokenType.PUNCT_OR);
        punctMapping.put("&", TokenType.PUNCT_BIT_AND);
        punctMapping.put("&&", TokenType.PUNCT_AND);
        punctMapping.put(";", TokenType.PUNCT_SEMI);
        punctMapping.put("=", TokenType.PUNCT_EQ);
        punctMapping.put("==", TokenType.PUNCT_EQ);
        keywordMapping.put("break", TokenType.KW_BREAK);
        keywordMapping.put("char", TokenType.KW_CHAR);
        keywordMapping.put("continue", TokenType.KW_CONT);
        keywordMapping.put("else", TokenType.KW_ELSE);
        keywordMapping.put("float", TokenType.KW_FLOAT);
        keywordMapping.put("if", TokenType.KW_IF);
        keywordMapping.put("int", TokenType.KW_INT);
        keywordMapping.put("return", TokenType.KW_RET);
        keywordMapping.put("void", TokenType.KW_VOID);
        keywordMapping.put("while", TokenType.KW_WHILE);
    }

    public static TokenTypeUtil instance() {
        return instance;
    }

    public TokenType getTokenType(String tokString) {
        return punctMapping.containsKey(tokString) ? punctMapping.get(tokString)
                : keywordMapping.get(tokString);
    }

    public boolean isKeyword(String tokString) {
        return keywordMapping.containsKey(tokString);
    }
}

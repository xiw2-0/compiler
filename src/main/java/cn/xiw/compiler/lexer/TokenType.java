package cn.xiw.compiler.lexer;

public enum TokenType {
    UNKNOWN, // bad token
    EOF, // end of file
    IDENTIFIER, // identifier (not keywords), abc123
    CONST_INT, // integer number, 123
    CONST_CHAR, // char, 'a'
    CONST_FLOAT, // floating number, 1.3
    STRING_LITERAL, // string, "xxx"
    PUNCT_L_SQR, // [
    PUNCT_R_SQR, // ]
    PUNCT_L_PAR, // (
    PUNCT_R_PAR, // )
    PUNCT_L_BAR, // {
    PUNCT_R_BAR, // }
    PUNCT_STAR, // *
    PUNCT_PLUS, // +
    PUNCT_MINUS, // -
    PUNCT_NOT, // !
    PUNCT_NOT_EQ, // !=
    PUNCT_DIV, // /
    PUNCT_MOD, // %
    PUNCT_LT, // <
    PUNCT_LE, // <=
    PUNCT_GT, // >
    PUNCT_GE, // >=
    PUNCT_BIT_OR, // |
    PUNCT_OR, // ||
    PUNCT_BIT_AND, // &
    PUNCT_AND, // &&
    PUNCT_SEMI, // ;
    PUNCT_EQ, // =
    PUNCT_EQEQ, // ==
    KW_BREAK, // break
    KW_CHAR, // char
    KW_CONT, // continue
    KW_ELSE, // else
    KW_FLOAT, // float
    KW_IF, // if
    KW_INT, // int
    KW_RET, // return
    KW_VOID, // void
    KW_WHILE, // while
}

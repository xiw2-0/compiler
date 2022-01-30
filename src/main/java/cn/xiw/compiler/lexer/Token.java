package cn.xiw.compiler.lexer;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Token {
    private final TokenType type;

    // for keyword. Tag id
    private int keywordId;

    // for identifier
    private String identifierString;

    // for constants. Tag id
    private int constantId;
    private int valueInt;
    private double valueFloat;
    private char valueChar;

    // for string literals
    private String valueString;

    // for punctuators
    private int punctuatorId;
}

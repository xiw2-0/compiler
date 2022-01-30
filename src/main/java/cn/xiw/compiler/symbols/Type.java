package cn.xiw.compiler.symbols;

import lombok.Getter;

/**
 * Type for char, int, and float. Mainly for type conversion and indicating expr
 * type.
 */

public class Type {
    @Getter
    private final int width;

    Type(int width) {
        this.width = width;
    }

    public static final Type CHAR_TYPE = new Type(1), INT_TYPE = new Type(4),
            FLOAT_TYPE = new Type(8);

    public static Type max(Type t1, Type t2) {
        if (t1 == FLOAT_TYPE || t2 == FLOAT_TYPE) {
            return FLOAT_TYPE;
        } else if (t1 == INT_TYPE || t2 == INT_TYPE) {
            return INT_TYPE;
        }
        return CHAR_TYPE;
    }
}

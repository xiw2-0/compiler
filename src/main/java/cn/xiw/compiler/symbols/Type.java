package cn.xiw.compiler.symbols;

import lombok.Getter;

/**
 * Type for char, int, and float. Mainly for type conversion and indicating expr
 * type.
 */

public abstract class Type {
    @Getter
    private final int width;

    Type(int width) {
        this.width = width;
    }
}

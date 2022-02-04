package cn.xiw.compiler.inter;

import lombok.Getter;

/**
 * Type for char, int, and float. Mainly for type conversion and indicating expr
 * type.
 */
@Getter
public abstract class Type {
    private final String typeName;
    private final int width;

    Type(String typeName, int width) {
        this.typeName = typeName;
        this.width = width;
    }
}

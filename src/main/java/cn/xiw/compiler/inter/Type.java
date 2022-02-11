package cn.xiw.compiler.inter;

import lombok.Getter;

/**
 * Type includes BuiltinType (e.g. void, char, int, float) and StructType.
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

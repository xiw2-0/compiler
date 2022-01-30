package cn.xiw.compiler.symbols;

import lombok.Getter;

@Getter
public class Array extends Type {
    private final Type of;
    private final int size;

    public Array(Type baseType, int size) {
        super(baseType.getWidth() * size);
        of = baseType;
        this.size = size;
    }
}

package cn.xiw.compiler.symbols;

import lombok.Getter;

@Getter
public class ArrayType extends Type {
    private final Type of;
    private final int size;

    public ArrayType(Type baseType, int size) {
        super(baseType.getWidth() * size);
        of = baseType;
        this.size = size;
    }
}

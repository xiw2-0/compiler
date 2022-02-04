package cn.xiw.compiler.inter;

import lombok.Getter;

@Getter
public class ArrayType extends Type {
    private final Type of;
    private final int size;

    public ArrayType(Type baseType, int size) {
        super(baseType.getTypeName() + "[]", baseType.getWidth() * size);
        of = baseType;
        this.size = size;
    }
}

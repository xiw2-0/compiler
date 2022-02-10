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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ArrayType) {
            var arrayTypeObj = (ArrayType) obj;
            return arrayTypeObj.of.equals(of) && arrayTypeObj.size == size
                    && arrayTypeObj.getTypeName().equals(getTypeName());
        }
        return false;
    }
}

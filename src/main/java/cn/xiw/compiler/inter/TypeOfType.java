package cn.xiw.compiler.inter;

public class TypeOfType extends Type {
    private static TypeOfType instance = new TypeOfType();

    public static TypeOfType instance() {
        return instance;
    }

    private TypeOfType() {
        super("TypeOfType", 0);
    }

}

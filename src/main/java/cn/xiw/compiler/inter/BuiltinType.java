package cn.xiw.compiler.inter;

public class BuiltinType extends Type {

    private BuiltinType(String typeName, int width) {
        super(typeName, width);
    }

    public static final BuiltinType VOID_TYPE = new BuiltinType("void", 0),
            CHAR_TYPE = new BuiltinType("char", 1),
            INT_TYPE = new BuiltinType("int", 4),
            FLOAT_TYPE = new BuiltinType("float", 8);

    public static BuiltinType max(BuiltinType t1, BuiltinType t2) {
        if (t1 == FLOAT_TYPE || t2 == FLOAT_TYPE) {
            return FLOAT_TYPE;
        } else if (t1 == INT_TYPE || t2 == INT_TYPE) {
            return INT_TYPE;
        }
        return CHAR_TYPE;
    }
}

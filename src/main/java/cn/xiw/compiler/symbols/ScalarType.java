package cn.xiw.compiler.symbols;

public class ScalarType extends Type {

    private ScalarType(int width) {
        super(width);
    }

    public static final ScalarType VOID_TYPE = new ScalarType(0),
            CHAR_TYPE = new ScalarType(1), INT_TYPE = new ScalarType(4),
            FLOAT_TYPE = new ScalarType(8);

    public static ScalarType max(ScalarType t1, ScalarType t2) {
        if (t1 == FLOAT_TYPE || t2 == FLOAT_TYPE) {
            return FLOAT_TYPE;
        } else if (t1 == INT_TYPE || t2 == INT_TYPE) {
            return INT_TYPE;
        }
        return CHAR_TYPE;
    }
}

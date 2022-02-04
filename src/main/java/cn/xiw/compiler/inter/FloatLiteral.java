package cn.xiw.compiler.inter;

import lombok.Getter;

public class FloatLiteral extends ExprAst {
    @Getter
    private final double value;

    public FloatLiteral(double value) {
        super(BuiltinType.FLOAT_TYPE);
        this.value = value;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

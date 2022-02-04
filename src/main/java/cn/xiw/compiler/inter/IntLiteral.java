package cn.xiw.compiler.inter;

import lombok.Getter;

public class IntLiteral extends ExprAst {
    @Getter
    private final int value;

    public IntLiteral(int value) {
        super(BuiltinType.INT_TYPE);
        this.value = value;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

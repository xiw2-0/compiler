package cn.xiw.compiler.inter;

import cn.xiw.compiler.symbols.Type;
import lombok.Getter;

public class FloatLiteral extends ExprAst {
    @Getter
    private final double value;

    FloatLiteral(double value) {
        super(Type.FLOAT_TYPE);
        this.value = value;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

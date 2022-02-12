package cn.xiw.compiler.inter;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FloatLiteral extends ExprAst {
    private final double value;

    @Builder
    FloatLiteral(double value) {
        setTypeId("float");
        this.value = value;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

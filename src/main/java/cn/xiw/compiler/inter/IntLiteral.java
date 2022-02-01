package cn.xiw.compiler.inter;

import cn.xiw.compiler.symbols.Type;
import lombok.Getter;

public class IntLiteral extends ExprAst {
    @Getter
    private final int value;

    IntLiteral(int value) {
        super(Type.INT_TYPE);
        this.value = value;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

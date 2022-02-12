package cn.xiw.compiler.inter;

import lombok.Builder;
import lombok.Getter;

@Getter
public class IntLiteral extends ExprAst {
    private final int value;

    @Builder
    IntLiteral(int value) {
        setTypeId("int");
        this.value = value;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

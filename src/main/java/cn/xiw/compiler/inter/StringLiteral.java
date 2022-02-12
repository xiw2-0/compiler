package cn.xiw.compiler.inter;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StringLiteral extends ExprAst {
    private final String value;

    @Builder
    StringLiteral(String value) {
        setTypeId("char[]");
        this.value = value;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

package cn.xiw.compiler.inter;

import cn.xiw.compiler.lexer.Token;
import lombok.Getter;

public class RelLogical extends LogicalExpr {
    @Getter
    private final ExprAst expr1, expr2;

    RelLogical(Token relationToken, ExprAst expr1, ExprAst expr2) {
        super(relationToken);
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

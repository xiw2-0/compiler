package cn.xiw.compiler.inter;

import cn.xiw.compiler.lexer.TokenType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BinaryOp extends ExprAst {
    private final TokenType op;
    private final ExprAst expr1, expr2;

    @Builder
    BinaryOp(TokenType op, ExprAst expr1, ExprAst expr2) {
        this.op = op;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

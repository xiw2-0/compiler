package cn.xiw.compiler.inter;

import lombok.Getter;

@Getter
public class BinaryOp extends ExprAst {
    private final OpType op;
    private final ExprAst expr1, expr2;

    BinaryOp(OpType op, ExprAst expr1, ExprAst expr2) {
        super(expr1.type);
        this.op = op;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

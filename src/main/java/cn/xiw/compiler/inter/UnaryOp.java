package cn.xiw.compiler.inter;

import cn.xiw.compiler.symbols.Type;
import lombok.Getter;

@Getter
public class UnaryOp extends ExprAst {
    private final OpType op;
    private final ExprAst expr;

    UnaryOp(Type type, OpType operator, ExprAst expr) {
        super(expr.type);
        op = operator;
        this.expr = expr;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

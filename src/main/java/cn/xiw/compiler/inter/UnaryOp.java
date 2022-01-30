package cn.xiw.compiler.inter;

import cn.xiw.compiler.lexer.Token;
import cn.xiw.compiler.symbols.Type;
import lombok.Getter;

public class UnaryOp extends OpExpr {
    @Getter
    private final ExprAst expr;

    UnaryOp(Type type, Token operator, ExprAst expr) {
        super(type, operator);
        this.expr = expr;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

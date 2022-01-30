package cn.xiw.compiler.inter;

import cn.xiw.compiler.lexer.Token;
import cn.xiw.compiler.symbols.Type;
import lombok.Getter;

public class ArithOp extends OpExpr {
    @Getter
    private final ExprAst expr1, expr2;

    ArithOp(Type type, Token operator, ExprAst expr1, ExprAst expr2) {
        super(type, operator);
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

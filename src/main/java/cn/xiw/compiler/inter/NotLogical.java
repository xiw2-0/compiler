package cn.xiw.compiler.inter;

import cn.xiw.compiler.lexer.Token;
import cn.xiw.compiler.lexer.TokenType;
import lombok.Getter;

public class NotLogical extends LogicalExpr {
    @Getter
    private final ExprAst expr;

    NotLogical(ExprAst expr) {
        super(Token.builder().type(TokenType.PUNCTUATOR).punctuatorId('!')
                .build());
        this.expr = expr;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

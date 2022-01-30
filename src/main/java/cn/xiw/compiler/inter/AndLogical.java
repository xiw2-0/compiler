package cn.xiw.compiler.inter;

import cn.xiw.compiler.lexer.Tag;
import cn.xiw.compiler.lexer.Token;
import cn.xiw.compiler.lexer.TokenType;
import lombok.Getter;

public class AndLogical extends LogicalExpr {
    @Getter
    private final ExprAst expr1, expr2;

    AndLogical(ExprAst expr1, ExprAst expr2) {
        super(Token.builder().type(TokenType.PUNCTUATOR).punctuatorId(Tag.AND)
                .build());
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

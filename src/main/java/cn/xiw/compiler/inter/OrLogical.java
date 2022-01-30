package cn.xiw.compiler.inter;

import cn.xiw.compiler.lexer.Tag;
import cn.xiw.compiler.lexer.Token;
import cn.xiw.compiler.lexer.TokenType;
import lombok.Getter;

public class OrLogical extends LogicalExpr {
    @Getter
    private final ExprAst expr1, expr2;

    OrLogical(ExprAst expr1, ExprAst expr2) {
        super(Token.builder().type(TokenType.PUNCTUATOR).punctuatorId(Tag.OR)
                .build());
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

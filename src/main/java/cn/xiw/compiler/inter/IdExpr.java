package cn.xiw.compiler.inter;

import cn.xiw.compiler.lexer.Token;
import cn.xiw.compiler.symbols.Type;
import lombok.Getter;

public class IdExpr extends ExprAst {
    @Getter
    private final Token identifierToken;

    IdExpr(Type type, Token identifierToken) {
        super(type);
        this.identifierToken = identifierToken;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

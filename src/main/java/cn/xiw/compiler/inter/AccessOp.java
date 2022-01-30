package cn.xiw.compiler.inter;

import cn.xiw.compiler.lexer.Token;
import cn.xiw.compiler.lexer.TokenType;
import cn.xiw.compiler.symbols.Type;
import lombok.Getter;

/**
 * Array accessor.
 */
@Getter
public class AccessOp extends OpExpr {
    private final IdExpr arrayId;
    private final ExprAst index;

    AccessOp(Type type, IdExpr id, ExprAst index) {
        super(type, Token.builder().type(TokenType.PUNCTUATOR).punctuatorId('[')
                .build());
        arrayId = id;
        this.index = index;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

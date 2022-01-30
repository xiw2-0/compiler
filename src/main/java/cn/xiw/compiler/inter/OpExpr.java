package cn.xiw.compiler.inter;

import cn.xiw.compiler.lexer.Token;
import cn.xiw.compiler.symbols.Type;
import lombok.Getter;

public abstract class OpExpr extends ExprAst {
    @Getter
    private final Token opToken;

    OpExpr(Type type, Token opToken) {
        super(type);
        this.opToken = opToken;
    }

}

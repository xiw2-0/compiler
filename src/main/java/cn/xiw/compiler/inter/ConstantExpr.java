package cn.xiw.compiler.inter;

import ch.qos.logback.core.subst.Token;
import cn.xiw.compiler.symbols.Type;
import lombok.Getter;

public class ConstantExpr extends ExprAst {
    @Getter
    private final Token constantValue;

    ConstantExpr(Type type, Token constanToken) {
        super(type);
        constantValue = constanToken;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

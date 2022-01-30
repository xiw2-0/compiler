package cn.xiw.compiler.inter;

import cn.xiw.compiler.lexer.Token;
import cn.xiw.compiler.symbols.Type;
import lombok.Getter;

public abstract class LogicalExpr extends ExprAst {

    @Getter
    private final Token logicalOp;

    LogicalExpr(Token logicalOp) {
        super(Type.INT_TYPE);
        this.logicalOp = logicalOp;
    }
}

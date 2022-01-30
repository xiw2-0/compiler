package cn.xiw.compiler.inter;

import cn.xiw.compiler.symbols.Type;

public abstract class ExprAst extends AstNode {
    protected final Type type;

    ExprAst(Type type) {
        this.type = type;
    }
}

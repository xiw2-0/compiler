package cn.xiw.compiler.inter;

public abstract class ExprAst extends AstNode {
    protected final Type type;

    ExprAst(Type type) {
        this.type = type;
    }
}

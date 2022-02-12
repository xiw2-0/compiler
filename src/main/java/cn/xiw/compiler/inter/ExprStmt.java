package cn.xiw.compiler.inter;

import lombok.Getter;

/**
 * ExprStmt: expr;
 */
@Getter
public class ExprStmt extends StmtAst {
    private final ExprAst expr;

    public ExprStmt(ExprAst expr) {
        this.expr = expr;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

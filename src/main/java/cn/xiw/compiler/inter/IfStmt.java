package cn.xiw.compiler.inter;

import lombok.Getter;

/**
 * IfStmt: if (expr) {stmt}
 */
@Getter
public class IfStmt extends StmtAst {
    private final ExprAst expr;
    private final StmtAst stmt;

    IfStmt(ExprAst expr, StmtAst stmt) {
        this.expr = expr;
        this.stmt = stmt;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }

}
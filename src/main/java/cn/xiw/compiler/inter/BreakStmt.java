package cn.xiw.compiler.inter;

import lombok.Getter;

/**
 * WhileStmt: while (expr) {stmt}
 */
@Getter
public class BreakStmt extends StmtAst {
    private final StmtAst enclosingStmt;

    BreakStmt(ExprAst expr, StmtAst stmt) {
        this.expr = expr;
        this.stmt = stmt;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

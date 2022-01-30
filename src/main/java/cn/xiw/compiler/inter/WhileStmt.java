package cn.xiw.compiler.inter;

import lombok.Getter;

/**
 * WhileStmt: while (expr) {stmt}
 */
@Getter
public class WhileStmt extends StmtAst {
    private final ExprAst expr;
    private final StmtAst stmt;

    WhileStmt(ExprAst expr, StmtAst stmt) {
        this.expr = expr;
        this.stmt = stmt;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

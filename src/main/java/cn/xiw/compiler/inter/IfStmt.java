package cn.xiw.compiler.inter;

import lombok.Getter;

/**
 * IfStmt: if (expr) {ifStmt} else {elseStmt}
 */
@Getter
public class IfStmt extends StmtAst {
    private boolean hasElse = false;
    private final ExprAst expr;
    private StmtAst ifStmt, elseStmt;

    IfStmt(ExprAst expr, StmtAst ifStmt, StmtAst elseStmt) {
        this.expr = expr;
        this.ifStmt = ifStmt;
        this.elseStmt = elseStmt;
        if (elseStmt != null) {
            hasElse = true;
        }
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
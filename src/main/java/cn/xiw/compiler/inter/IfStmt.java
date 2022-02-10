package cn.xiw.compiler.inter;

import lombok.Getter;

/**
 * IfStmt: if (expr) {ifStmt} else {elseStmt}
 */
@Getter
public class IfStmt extends StmtAst {
    private boolean containingElse = false;
    private final ExprAst expr;
    private StmtAst ifStmt, elseStmt;

    public IfStmt(ExprAst expr, StmtAst ifStmt, StmtAst elseStmt) {
        this.expr = expr;
        this.ifStmt = ifStmt;
        this.elseStmt = elseStmt;
        if (elseStmt != null) {
            containingElse = true;
        }
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
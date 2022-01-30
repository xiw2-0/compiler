package cn.xiw.compiler.inter;

import lombok.Getter;

/**
 * IfElseStmt: if (expr) {ifStmt} else {elseStmt}
 */
@Getter
public class IfElseStmt extends StmtAst {
    private final ExprAst expr;
    private final StmtAst ifStmt, elseStmt;

    IfElseStmt(ExprAst expr, StmtAst ifStmt, StmtAst elseStmt) {
        this.expr = expr;
        this.ifStmt = ifStmt;
        this.elseStmt = elseStmt;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
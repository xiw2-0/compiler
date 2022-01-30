package cn.xiw.compiler.inter;

import lombok.Getter;

/**
 * AssignStmt: id = expr;
 */
@Getter
public class AssignStmt extends StmtAst {
    private final IdExpr id;
    private final ExprAst expr;

    AssignStmt(IdExpr id, ExprAst expr) {
        this.id = id;
        this.expr = expr;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

package cn.xiw.compiler.inter;

import lombok.Getter;

/**
 * AssignStmt: id = expr;
 */
@Getter
public class AssignStmt extends StmtAst {
    private final DeclRefExpr id;
    private final ExprAst expr;

    public AssignStmt(DeclRefExpr id, ExprAst expr) {
        this.id = id;
        this.expr = expr;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

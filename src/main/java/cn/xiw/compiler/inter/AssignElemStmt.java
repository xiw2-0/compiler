package cn.xiw.compiler.inter;

import lombok.Getter;

/**
 * AssignElemStmt: id[index] = expr;
 */
@Getter
public class AssignElemStmt extends StmtAst {
    private final IdExpr id;
    private final ExprAst index, expr;

    AssignElemStmt(IdExpr id, ExprAst index, ExprAst expr) {
        this.id = id;
        this.index = index;
        this.expr = expr;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

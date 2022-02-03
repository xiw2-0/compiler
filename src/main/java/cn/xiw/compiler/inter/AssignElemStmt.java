package cn.xiw.compiler.inter;

import lombok.Getter;

/**
 * AssignElemStmt: id[index] = expr;
 */
@Getter
public class AssignElemStmt extends StmtAst {
    private final ElemAccessOp elem;
    private final ExprAst expr;

    public AssignElemStmt(ElemAccessOp elem, ExprAst expr) {
        this.elem = elem;
        this.expr = expr;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

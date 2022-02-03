package cn.xiw.compiler.inter;

import lombok.Getter;
import lombok.Setter;

/**
 * WhileStmt: while (expr) {stmt}
 */
@Getter
public class WhileStmt extends StmtAst {
    private final ExprAst expr;

    @Setter
    private StmtAst stmt;

    public WhileStmt(ExprAst expr) {
        this.expr = expr;
        stmt = NullStmt.instance();
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

package cn.xiw.compiler.inter;

import lombok.Getter;

/**
 * WhileStmt: while (expr) {stmt}
 */
@Getter
public class BreakStmt extends StmtAst {
    private final StmtAst enclosingStmt;

    BreakStmt(StmtAst enclosing) {
        this.enclosingStmt = enclosing;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

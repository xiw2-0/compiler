package cn.xiw.compiler.inter;

import lombok.Builder;
import lombok.Getter;

/**
 * WhileStmt: while (expr) {stmt}
 */
@Getter
@Builder
public class BreakStmt extends StmtAst {
    private final StmtAst enclosingStmt;

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

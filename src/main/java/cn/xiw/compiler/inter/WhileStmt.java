package cn.xiw.compiler.inter;

import lombok.Builder;
import lombok.Getter;

/**
 * WhileStmt: while (expr) stmt
 */
@Getter
@Builder
public class WhileStmt extends StmtAst {
    private final ExprAst expr;
    private final StmtAst stmt;

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

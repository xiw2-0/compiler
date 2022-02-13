package cn.xiw.compiler.inter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * WhileStmt: while (expr) stmt
 */
@Getter
@Builder
@Setter
public class WhileStmt extends StmtAst {
    private final ExprAst expr;
    private StmtAst stmt;

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

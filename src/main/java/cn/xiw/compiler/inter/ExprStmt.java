package cn.xiw.compiler.inter;

import lombok.Builder;
import lombok.Getter;

/**
 * ExprStmt: expr;
 */
@Getter
@Builder
public class ExprStmt extends StmtAst {
    private final ExprAst expr;

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

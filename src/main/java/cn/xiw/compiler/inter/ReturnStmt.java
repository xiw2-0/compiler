package cn.xiw.compiler.inter;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReturnStmt extends StmtAst {
    private final FuncDecl func;
    private final ExprAst retExpr;

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

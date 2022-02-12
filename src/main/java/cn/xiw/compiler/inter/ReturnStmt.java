package cn.xiw.compiler.inter;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReturnStmt extends StmtAst {
    private String funcId;
    private ExprAst retExpr;

    @Builder
    ReturnStmt(String funcId, ExprAst retExpr) {
        this.funcId = funcId;
        this.retExpr = retExpr;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

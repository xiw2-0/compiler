package cn.xiw.compiler.inter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class ReturnStmt extends StmtAst {
    private final String funcId;
    private FuncDecl func;
    private final ExprAst retExpr;

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

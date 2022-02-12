package cn.xiw.compiler.inter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeclRefExpr extends ExprAst {
    private final String id;
    private VarDecl varDecl;

    @Builder
    DeclRefExpr(String id) {
        this.id = id;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

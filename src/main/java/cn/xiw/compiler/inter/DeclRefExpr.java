package cn.xiw.compiler.inter;

import lombok.Getter;

public class DeclRefExpr extends ExprAst {
    @Getter
    private final VarDecl varDecl;

    public DeclRefExpr(VarDecl varDecl) {
        super(varDecl.getType());
        this.varDecl = varDecl;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

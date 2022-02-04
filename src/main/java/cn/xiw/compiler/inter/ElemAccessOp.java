package cn.xiw.compiler.inter;

import lombok.Getter;

/**
 * Array accessor.
 */
@Getter
public class ElemAccessOp extends ExprAst {
    private final DeclRefExpr arrayId;
    private final ExprAst index;

    public ElemAccessOp(DeclRefExpr id, ExprAst index) {
        super(((ArrayType) id.getVarDecl().getType()).getOf());
        arrayId = id;
        this.index = index;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

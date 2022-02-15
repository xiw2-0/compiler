package cn.xiw.compiler.inter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Array accessor.
 */
@Getter
@Setter
public class ElemAccessOp extends ExprAst {
    private final String arrayId;
    private VarDecl arrayDecl;
    private ExprAst index;

    @Builder
    ElemAccessOp(String id, ExprAst index) {
        arrayId = id;
        this.index = index;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

package cn.xiw.compiler.inter;

import cn.xiw.compiler.symbols.Type;
import lombok.Getter;

/**
 * Array accessor.
 */
@Getter
public class AccessOp extends ExprAst {
    private final DeclRefExpr arrayId;
    private final ExprAst index;

    AccessOp(Type type, DeclRefExpr id, ExprAst index) {
        super(type);
        arrayId = id;
        this.index = index;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

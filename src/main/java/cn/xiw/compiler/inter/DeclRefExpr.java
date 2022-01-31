package cn.xiw.compiler.inter;

import cn.xiw.compiler.symbols.Type;
import lombok.Getter;

public class DeclRefExpr extends ExprAst {
    @Getter
    private final String identifier;

    DeclRefExpr(Type type, String id) {
        super(type);
        identifier = id;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

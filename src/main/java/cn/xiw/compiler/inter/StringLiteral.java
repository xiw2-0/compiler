package cn.xiw.compiler.inter;

import cn.xiw.compiler.symbols.Array;
import cn.xiw.compiler.symbols.Type;
import lombok.Getter;

public class StringLiteral extends ExprAst {
    @Getter
    private final String value;

    StringLiteral(String value) {
        super(new Array(Type.CHAR_TYPE, value.length()));
        this.value = value;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

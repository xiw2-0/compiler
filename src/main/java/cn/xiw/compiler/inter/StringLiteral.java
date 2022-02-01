package cn.xiw.compiler.inter;

import cn.xiw.compiler.symbols.ArrayType;
import cn.xiw.compiler.symbols.ScalarType;
import lombok.Getter;

public class StringLiteral extends ExprAst {
    @Getter
    private final String value;

    StringLiteral(String value) {
        super(new ArrayType(ScalarType.CHAR_TYPE, value.length()));
        this.value = value;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

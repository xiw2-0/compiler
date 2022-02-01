package cn.xiw.compiler.inter;

import cn.xiw.compiler.symbols.ScalarType;
import lombok.Getter;

public class CharLiteral extends ExprAst {
    @Getter
    private final char value;

    CharLiteral(char value) {
        super(ScalarType.CHAR_TYPE);
        this.value = value;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

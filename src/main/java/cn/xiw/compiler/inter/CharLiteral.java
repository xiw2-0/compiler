package cn.xiw.compiler.inter;

import cn.xiw.compiler.symbols.Type;
import lombok.Getter;

public class CharLiteral extends ExprAst {
    @Getter
    private final char value;

    CharLiteral(char value) {
        super(Type.CHAR_TYPE);
        this.value = value;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

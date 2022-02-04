package cn.xiw.compiler.inter;

import lombok.Getter;

public class CharLiteral extends ExprAst {
    @Getter
    private final char value;

    public CharLiteral(char value) {
        super(BuiltinType.CHAR_TYPE);
        this.value = value;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

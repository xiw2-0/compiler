package cn.xiw.compiler.inter;

import lombok.Getter;

public class StringLiteral extends ExprAst {
    @Getter
    private final String value;

    StringLiteral(String value) {
        super(new ArrayType(BuiltinType.CHAR_TYPE, value.length()));
        this.value = value;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

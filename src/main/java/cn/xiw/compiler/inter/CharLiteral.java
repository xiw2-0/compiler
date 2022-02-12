package cn.xiw.compiler.inter;

import lombok.Getter;

@Getter
public class CharLiteral extends ExprAst {
    private final char value;

    public CharLiteral(char value) {
        setTypeId("char");
        this.value = value;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

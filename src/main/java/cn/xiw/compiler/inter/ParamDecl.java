package cn.xiw.compiler.inter;

import lombok.Getter;

@Getter
public class ParamDecl extends DeclAst {
    private final Type type;

    ParamDecl(String id, Type type) {
        super(id);
        this.type = type;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

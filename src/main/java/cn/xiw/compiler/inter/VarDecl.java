package cn.xiw.compiler.inter;

import lombok.Getter;

@Getter
public class VarDecl extends DeclAst {
    private final Type type;

    public VarDecl(String id, Type type) {
        super(id);
        this.type = type;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

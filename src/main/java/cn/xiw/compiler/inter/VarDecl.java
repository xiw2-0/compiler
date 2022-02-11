package cn.xiw.compiler.inter;

import lombok.Getter;

/**
 * Variable declaration.
 */
@Getter
public class VarDecl extends DeclAst {
    private final String identifier;

    public VarDecl(String id, Type type) {
        super(type);
        this.identifier = id;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

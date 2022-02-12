package cn.xiw.compiler.inter;

import lombok.Builder;
import lombok.Getter;

/**
 * Variable declaration.
 */
@Getter
public class VarDecl extends DeclAst {
    private final String identifier;

    @Builder
    VarDecl(String identifier, String typeId) {
        super(typeId);
        this.identifier = identifier;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

package cn.xiw.compiler.inter;

import java.util.ArrayList;

import lombok.Getter;

/**
 * Function proto declaration.
 */
@Getter
public class ProtoDecl extends DeclAst {
    private final ArrayList<DeclAst> params = new ArrayList<>();
    private final String identifier;

    ProtoDecl(Type returnType, String identifier) {
        super(returnType);
        this.identifier = identifier;
    }

    void addParam(DeclAst paramDecl) {
        params.add(paramDecl);
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

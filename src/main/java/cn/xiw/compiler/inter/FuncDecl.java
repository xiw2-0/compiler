package cn.xiw.compiler.inter;

import java.util.ArrayList;

import lombok.Getter;

/**
 * Function definition.
 */
@Getter
public class FuncDecl extends DeclAst {
    private final ArrayList<DeclAst> params = new ArrayList<>();
    private final String identifier;
    private final CompoundStmt body;

    FuncDecl(Type returnType, String identifier, CompoundStmt body) {
        super(returnType);
        this.identifier = identifier;
        this.body = body;
    }

    void addParam(DeclAst paramDecl) {
        params.add(paramDecl);
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

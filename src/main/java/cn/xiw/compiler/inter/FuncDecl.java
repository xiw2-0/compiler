package cn.xiw.compiler.inter;

import java.util.ArrayList;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Function definition.
 */
@Getter
@Setter
public class FuncDecl extends DeclAst {
    private final ArrayList<VarDecl> params;
    private final String identifier;
    private CompoundStmt body;

    @Builder
    FuncDecl(String returnType, String identifier, ArrayList<VarDecl> params,
            CompoundStmt body) {
        super(returnType);
        this.identifier = identifier;
        this.params = params;
        this.body = body;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

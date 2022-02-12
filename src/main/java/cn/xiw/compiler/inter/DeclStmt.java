package cn.xiw.compiler.inter;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeclStmt extends StmtAst {
    private final VarDecl varDecl;

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

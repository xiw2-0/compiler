package cn.xiw.compiler.inter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeclStmt extends StmtAst {
    private final VarDecl varDecl;

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

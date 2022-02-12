package cn.xiw.compiler.inter;

import java.util.ArrayList;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompoundStmt extends StmtAst {
    private final ArrayList<StmtAst> stmts;

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

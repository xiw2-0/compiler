package cn.xiw.compiler.inter;

import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BlockStmt extends StmtAst {
    private final ArrayList<StmtAst> stmts = new ArrayList<>();

    void addStmt(StmtAst stmt) {
        stmts.add(stmt);
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

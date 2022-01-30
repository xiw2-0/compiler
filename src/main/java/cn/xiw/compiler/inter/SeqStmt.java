package cn.xiw.compiler.inter;

import lombok.Getter;

@Getter
public class SeqStmt extends StmtAst {
    private final StmtAst stmt1, stmt2;

    SeqStmt(StmtAst stmt1, StmtAst stmt2) {
        this.stmt1 = stmt1;
        this.stmt2 = stmt2;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

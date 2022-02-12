package cn.xiw.compiler.inter;

public class ReturnStmt extends StmtAst {

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }

}

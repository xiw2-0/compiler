package cn.xiw.compiler.inter;

public class NullStmt extends StmtAst {

    private static final NullStmt instance = new NullStmt();

    private NullStmt() {
    }

    public static NullStmt instance() {
        return instance;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

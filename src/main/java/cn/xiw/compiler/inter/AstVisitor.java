package cn.xiw.compiler.inter;

/**
 * AstNode visitor using visitor pattern.
 */
public interface AstVisitor {

    void visit(DeclRefExpr declRefExpr);

    void visit(BinaryOp binaryOp);

    void visit(UnaryOp unaryOp);

    void visit(AccessOp accessOp);

    void visit(ConstantExpr constantExpr);

    void visit(IfStmt ifStmt);

    void visit(WhileStmt whileStmt);

    void visit(AssignStmt assignStmt);

    void visit(AssignElemStmt assignElemStmt);

    void visit(BreakStmt breakStmt);

    void visit(NullStmt nullStmt);

    void visit(BlockStmt blockStmt);

    void visit(DeclStmt declStmt);

    void visit(FuncDecl funcDecl);

    void visit(VarDecl varDecl);

    void visit(ParamDecl paramDecl);
}

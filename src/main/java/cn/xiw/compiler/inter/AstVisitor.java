package cn.xiw.compiler.inter;

/**
 * AstNode visitor using visitor pattern.
 */
public interface AstVisitor {
    void visit(IdExpr idExpr);

    void visit(ArithOp arithOp);

    void visit(UnaryOp unaryOp);

    void visit(AccessOp accessOp);

    void visit(ConstantExpr constantExpr);

    void visit(OrLogical orLogical);

    void visit(AndLogical andLogical);

    void visit(NotLogical notLogical);

    void visit(RelLogical relLogical);

    void visit(IfStmt ifStmt);

    void visit(IfElseStmt ifElseStmt);

    void visit(WhileStmt whileStmt);

    void visit(AssignStmt assignStmt);

    void visit(AssignElemStmt assignElemStmt);

    void visit(BreakStmt breakStmt);

    void visit(SeqStmt seqStmt);

    void visit(NullStmt nullStmt);
}

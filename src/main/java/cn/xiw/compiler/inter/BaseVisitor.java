package cn.xiw.compiler.inter;

public class BaseVisitor implements AstVisitor {

    @Override
    public void visit(DeclRefExpr declRefExpr) {
        // Leaf node. Do nothing.
    }

    @Override
    public void visit(BinaryOp binaryOp) {
        binaryOp.getExpr1().accept(this);
        binaryOp.getExpr2().accept(this);
    }

    @Override
    public void visit(UnaryOp unaryOp) {
        unaryOp.getExpr().accept(this);
    }

    @Override
    public void visit(ElemAccessOp accessOp) {
        accessOp.getIndex().accept(this);
    }

    @Override
    public void visit(CallExpr callExpr) {
        callExpr.getParams().stream().forEach(expr -> expr.accept(this));
    }

    @Override
    public void visit(IntLiteral intLiteral) {
        // Leaf node.
    }

    @Override
    public void visit(CharLiteral charLiteral) {
        // Leaf node.
    }

    @Override
    public void visit(FloatLiteral floatLiteral) {
        // Leaf node.
    }

    @Override
    public void visit(StringLiteral stringLiteral) {
        // Leaf node.
    }

    @Override
    public void visit(IfStmt ifStmt) {
        ifStmt.getExpr().accept(this);
        ifStmt.getIfStmt().accept(this);
        if (ifStmt.isContainingElse()) {
            ifStmt.getElseStmt().accept(this);
        }
    }

    @Override
    public void visit(WhileStmt whileStmt) {
        whileStmt.getExpr().accept(this);
        whileStmt.getStmt().accept(this);
    }

    @Override
    public void visit(ExprStmt exprStmt) {
        exprStmt.getExpr().accept(this);
    }

    @Override
    public void visit(BreakStmt breakStmt) {
        // Leaf node.
    }

    @Override
    public void visit(NullStmt nullStmt) {
        // Leaf node.
    }

    @Override
    public void visit(CompoundStmt blockStmt) {
        blockStmt.getStmts().stream().forEach(stmt -> stmt.accept(this));
    }

    @Override
    public void visit(DeclStmt declStmt) {
        declStmt.getVarDecl().accept(this);
    }

    @Override
    public void visit(ReturnStmt returnStmt) {
        returnStmt.getRetExpr().accept(this);
    }

    @Override
    public void visit(VarDecl varDecl) {
        // Leaf node.
    }

    @Override
    public void visit(FuncDecl funcDecl) {
        // Does not visit parameters.
        funcDecl.getBody().accept(this);
    }

    @Override
    public void visit(TranslationUnitAst translationUnitAst) {
        translationUnitAst.getDeclarations().stream()
                .forEach(decl -> decl.accept(this));
    }

}

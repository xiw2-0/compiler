package cn.xiw.compiler.sema;

import cn.xiw.compiler.inter.AstVisitor;
import cn.xiw.compiler.inter.BinaryOp;
import cn.xiw.compiler.inter.BreakStmt;
import cn.xiw.compiler.inter.CallExpr;
import cn.xiw.compiler.inter.CharLiteral;
import cn.xiw.compiler.inter.CompoundStmt;
import cn.xiw.compiler.inter.DeclRefExpr;
import cn.xiw.compiler.inter.DeclStmt;
import cn.xiw.compiler.inter.ElemAccessOp;
import cn.xiw.compiler.inter.ExprStmt;
import cn.xiw.compiler.inter.FloatLiteral;
import cn.xiw.compiler.inter.FuncDecl;
import cn.xiw.compiler.inter.IfStmt;
import cn.xiw.compiler.inter.IntLiteral;
import cn.xiw.compiler.inter.NullStmt;
import cn.xiw.compiler.inter.ReturnStmt;
import cn.xiw.compiler.inter.StringLiteral;
import cn.xiw.compiler.inter.TranslationUnitAst;
import cn.xiw.compiler.inter.UnaryOp;
import cn.xiw.compiler.inter.VarDecl;
import cn.xiw.compiler.inter.WhileStmt;
import cn.xiw.compiler.symbols.Env;

public class SematicAnalysis implements AstVisitor {
    private Env top = null; // the top symbol table

    private void error(String msg) {
        throw new Error("Sema error: " + msg);
    }

    /**
     * Checked whether symbols are declared or not.
     */
    @Override
    public void visit(DeclRefExpr declRefExpr) {
        var id = top.getSymbol(declRefExpr.getId());
        if (id == null || !(id instanceof VarDecl)) {
            error("Undeclared variable: " + declRefExpr.getId());
        }
        // binding variable
        declRefExpr.setVarDecl((VarDecl) id);
    }

    @Override
    public void visit(BinaryOp binaryOp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(UnaryOp unaryOp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ElemAccessOp accessOp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(CallExpr callExpr) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(IntLiteral intLiteral) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(CharLiteral charLiteral) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(FloatLiteral floatLiteral) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(StringLiteral stringLiteral) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(IfStmt ifStmt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(WhileStmt whileStmt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExprStmt assignStmt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(BreakStmt breakStmt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(NullStmt nullStmt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(CompoundStmt blockStmt) {
        Env savedEnv = top;
        top = new Env(top);
        blockStmt.getStmts().stream().forEach(stmt -> stmt.accept(this));
        top = savedEnv;
    }

    @Override
    public void visit(DeclStmt declStmt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ReturnStmt returnStmt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(VarDecl varDecl) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(FuncDecl funcDecl) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(TranslationUnitAst translationUnitAst) {
        // TODO Auto-generated method stub

    }
}

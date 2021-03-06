package cn.xiw.compiler.codegen;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import cn.xiw.compiler.inter.AstNode;
import cn.xiw.compiler.inter.BaseVisitor;
import cn.xiw.compiler.inter.BinaryOp;
import cn.xiw.compiler.inter.BreakStmt;
import cn.xiw.compiler.inter.CallExpr;
import cn.xiw.compiler.inter.CharLiteral;
import cn.xiw.compiler.inter.DeclRefExpr;
import cn.xiw.compiler.inter.ElemAccessOp;
import cn.xiw.compiler.inter.FloatLiteral;
import cn.xiw.compiler.inter.FuncDecl;
import cn.xiw.compiler.inter.ReturnStmt;
import cn.xiw.compiler.inter.IfStmt;
import cn.xiw.compiler.inter.IntLiteral;
import cn.xiw.compiler.inter.StringLiteral;
import cn.xiw.compiler.inter.UnaryOp;
import cn.xiw.compiler.inter.VarDecl;
import cn.xiw.compiler.inter.WhileStmt;
import cn.xiw.compiler.lexer.TokenType;

public class CodeGenerator extends BaseVisitor {
    private OutputStream out;
    private int tempExprResultCount = 0;
    private Map<AstNode, String> tempExprResult = new HashMap<>();
    private Map<AstNode, Integer> whileOutLabel = new HashMap<>();
    private Map<TokenType, String> operatorCmds = new HashMap<>();

    public CodeGenerator(OutputStream out) {
        this.out = out;
        operatorCmds.put(TokenType.PUNCT_LT, "LT");
        operatorCmds.put(TokenType.PUNCT_LE, "LE");
        operatorCmds.put(TokenType.PUNCT_GT, "GT");
        operatorCmds.put(TokenType.PUNCT_GE, "GE");
        operatorCmds.put(TokenType.PUNCT_STAR, "MUL");
        operatorCmds.put(TokenType.PUNCT_PLUS, "ADD");
        operatorCmds.put(TokenType.PUNCT_MINUS, "MINUS");
        operatorCmds.put(TokenType.PUNCT_DIV, "DIV");
        operatorCmds.put(TokenType.PUNCT_MOD, "MOD");
        operatorCmds.put(TokenType.PUNCT_NOT, "NOT");
        operatorCmds.put(TokenType.PUNCT_OR, "OR");
        operatorCmds.put(TokenType.PUNCT_AND, "AND");
        operatorCmds.put(TokenType.PUNCT_NOT_EQ, "NE");
        operatorCmds.put(TokenType.PUNCT_EQEQ, "EQ");
    }

    private String addTemp(AstNode node) {
        var tempId = "t" + (++tempExprResultCount);
        tempExprResult.put(node, tempId);
        return tempId;
    }

    private void putRef(AstNode node, String ref) {
        tempExprResult.put(node, ref);
    }

    private String getTemp(AstNode node) {
        var id = tempExprResult.get(node);
        tempExprResult.remove(node);
        return id;
    }

    private static int labels = 0;

    private int newLabel() {
        return ++labels;
    }

    private void print(String str) {
        try {
            out.write(str.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void emitLabel(int label) {
        print("L" + label + ":\n");
    }

    private void emitFuncLabel(String funcLabel) {
        print(funcLabel + ":\n");
    }

    private void emit(String line) {
        print("\t" + line + "\n");
    }

    private void emitAssign(String left, String right) {
        emit(left + " = " + right);
    }

    /**
     * Emits left value.
     */
    @Override
    public void visit(DeclRefExpr declRefExpr) {
        putRef(declRefExpr, declRefExpr.getVarDecl().getIdentifier());
    }

    @Override
    public void visit(BinaryOp binaryOp) {
        super.visit(binaryOp);

        var tempId = addTemp(binaryOp);
        // assignment expr
        if (binaryOp.getOp() == TokenType.PUNCT_EQ) {
            var left = getTemp(binaryOp.getExpr1());
            emitAssign(left, getTemp(binaryOp.getExpr2()));
            emitAssign(tempId, left);
        } else {
            emitAssign(tempId,
                    String.format("%s %s %s",
                            operatorCmds.get(binaryOp.getOp()),
                            getTemp(binaryOp.getExpr1()),
                            getTemp(binaryOp.getExpr2())));
        }
    }

    @Override
    public void visit(UnaryOp unaryOp) {
        super.visit(unaryOp);

        var tempId = addTemp(unaryOp);
        emitAssign(tempId, String.format("%s %s",
                operatorCmds.get(unaryOp.getOp()), getTemp(unaryOp.getExpr())));
    }

    /**
     * Emits left value.
     */
    @Override
    public void visit(ElemAccessOp accessOp) {
        super.visit(accessOp);

        putRef(accessOp,
                String.format("%s[ %s ]",
                        accessOp.getArrayDecl().getIdentifier(),
                        getTemp(accessOp.getIndex())));
    }

    @Override
    public void visit(CallExpr callExpr) {
        super.visit(callExpr);

        // emit param
        callExpr.getParams().stream().map(this::getTemp)
                .forEach(param -> emit("alloc para:" + param));
        emitAssign(addTemp(callExpr), "call " + callExpr.getFuncId());
    }

    @Override
    public void visit(IntLiteral intLiteral) {
        var tmpId = addTemp(intLiteral);
        emitAssign(tmpId, Integer.toString(intLiteral.getValue()));
    }

    @Override
    public void visit(CharLiteral charLiteral) {
        var tmpId = addTemp(charLiteral);
        emitAssign(tmpId, Character.toString(charLiteral.getValue()));
    }

    @Override
    public void visit(FloatLiteral floatLiteral) {
        var tmpId = addTemp(floatLiteral);
        emitAssign(tmpId, Double.toString(floatLiteral.getValue()));
    }

    @Override
    public void visit(StringLiteral stringLiteral) {
        var tmpId = addTemp(stringLiteral);
        emitAssign(tmpId, stringLiteral.getValue());
    }

    @Override
    public void visit(IfStmt ifStmt) {
        ifStmt.getExpr().accept(this);
        var falseLabel = newLabel();
        var outLabel = newLabel();

        emit("iffalse " + getTemp(ifStmt.getExpr()) + " goto L" + falseLabel);
        ifStmt.getIfStmt().accept(this);
        emit("goto L" + outLabel);
        emitLabel(falseLabel);
        if (ifStmt.isContainingElse()) {
            ifStmt.getElseStmt().accept(this);
        }
        emitLabel(outLabel);
    }

    @Override
    public void visit(WhileStmt whileStmt) {
        var inLabel = newLabel();
        var outLabel = newLabel();
        whileOutLabel.put(whileStmt, outLabel);

        emitLabel(inLabel);
        whileStmt.getExpr().accept(this);
        emit("iffalse " + getTemp(whileStmt.getExpr()) + " goto L" + outLabel);
        whileStmt.getStmt().accept(this);
        emitLabel(outLabel);
    }

    @Override
    public void visit(BreakStmt breakStmt) {
        emit("goto L" + whileOutLabel.get(breakStmt.getEnclosingStmt()));
    }

    @Override
    public void visit(VarDecl varDecl) {
        emit("alloc \t" + varDecl.getType().getWidth() + "\tBytes: "
                + varDecl.getIdentifier());
    }

    @Override
    public void visit(FuncDecl funcDecl) {
        emitFuncLabel(funcDecl.getIdentifier());
        super.visit(funcDecl);
    }

    @Override
    public void visit(ReturnStmt returnStmt) {
        super.visit(returnStmt);
        if (returnStmt.getRetExpr() != null) {
            emit("SET RETVAL = " + getTemp(returnStmt.getRetExpr()));
        }
        emit("RET");
    }
}

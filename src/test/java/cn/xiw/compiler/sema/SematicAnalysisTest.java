package cn.xiw.compiler.sema;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cn.xiw.compiler.inter.ArrayType;
import cn.xiw.compiler.inter.BinaryOp;
import cn.xiw.compiler.inter.BuiltinType;
import cn.xiw.compiler.inter.DeclRefExpr;
import cn.xiw.compiler.inter.DeclStmt;
import cn.xiw.compiler.inter.ElemAccessOp;
import cn.xiw.compiler.inter.ExprStmt;
import cn.xiw.compiler.inter.FloatLiteral;
import cn.xiw.compiler.inter.IntLiteral;
import cn.xiw.compiler.inter.VarDecl;
import cn.xiw.compiler.lexer.TokenType;

public class SematicAnalysisTest {
    private SematicAnalysis sematicAnalysis = new SematicAnalysis();

    @Test
    void testBasic() {
        // arrange
        // int num; num = 1;
        var varDecl = VarDecl.builder().identifier("num").typeId("int").build();
        var declStmt = DeclStmt.builder().varDecl(varDecl).build();
        var assignment = BinaryOp.builder().op(TokenType.PUNCT_EQ)
                .expr1(DeclRefExpr.builder().id("num").build())
                .expr2(IntLiteral.builder().value(1).build()).build();
        var exprStmt = ExprStmt.builder().expr(assignment).build();
        // act
        declStmt.accept(sematicAnalysis);
        exprStmt.accept(sematicAnalysis);

        // assert
        assertEquals(BuiltinType.INT_TYPE, (BuiltinType) varDecl.getType());
        assertEquals(varDecl,
                ((DeclRefExpr) assignment.getExpr1()).getVarDecl());
    }

    @Test
    void testDupDecl() {
        // arrange
        // int num; float num;
        var varDecl = VarDecl.builder().identifier("num").typeId("int").build();
        var declStmt = DeclStmt.builder().varDecl(varDecl).build();
        var varDecl2 = VarDecl.builder().identifier("num").typeId("int")
                .build();
        var declStmt2 = DeclStmt.builder().varDecl(varDecl2).build();

        // act
        declStmt.accept(sematicAnalysis);

        // assert
        assertThrows(SemaException.class,
                () -> declStmt2.accept(sematicAnalysis));
    }

    @Test
    void testTypeMismatch() {
        // arrange
        // int num; num = 1.0;
        var varDecl = VarDecl.builder().identifier("num").typeId("int").build();
        var declStmt = DeclStmt.builder().varDecl(varDecl).build();
        var assignment = BinaryOp.builder().op(TokenType.PUNCT_EQ)
                .expr1(DeclRefExpr.builder().id("num").build())
                .expr2(FloatLiteral.builder().value(1.0).build()).build();
        var exprStmt = ExprStmt.builder().expr(assignment).build();
        // act
        declStmt.accept(sematicAnalysis);

        // assert
        assertThrows(SemaException.class,
                () -> exprStmt.accept(sematicAnalysis));
    }

    @Test
    void testArrayDeclAndRef() {
        // arrange
        // int[6] nums; nums[1] = 1;
        var varDecl = VarDecl.builder().identifier("nums").typeId("int[6]")
                .build();
        var declStmt = DeclStmt.builder().varDecl(varDecl).build();
        var assignment = BinaryOp.builder().op(TokenType.PUNCT_EQ)
                .expr1(ElemAccessOp.builder().id("nums")
                        .index(IntLiteral.builder().value(1).build()).build())
                .expr2(IntLiteral.builder().value(1).build()).build();
        var exprStmt = ExprStmt.builder().expr(assignment).build();

        // act
        declStmt.accept(sematicAnalysis);
        exprStmt.accept(sematicAnalysis);

        // assert
        assertTrue(varDecl.getType() instanceof ArrayType);
        assertEquals(BuiltinType.INT_TYPE,
                ((ArrayType) varDecl.getType()).getOf());
        assertEquals(6, ((ArrayType) varDecl.getType()).getSize());
        assertEquals(varDecl,
                ((ElemAccessOp) assignment.getExpr1()).getArrayDecl());
    }
}

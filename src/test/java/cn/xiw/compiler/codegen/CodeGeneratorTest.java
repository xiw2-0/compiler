package cn.xiw.compiler.codegen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import cn.xiw.compiler.inter.ArrayType;
import cn.xiw.compiler.inter.ExprStmt;
import cn.xiw.compiler.inter.BinaryOp;
import cn.xiw.compiler.inter.CompoundStmt;
import cn.xiw.compiler.inter.BuiltinType;
import cn.xiw.compiler.inter.DeclRefExpr;
import cn.xiw.compiler.inter.DeclStmt;
import cn.xiw.compiler.inter.ElemAccessOp;
import cn.xiw.compiler.inter.IfStmt;
import cn.xiw.compiler.inter.IntLiteral;
import cn.xiw.compiler.inter.NullStmt;
import cn.xiw.compiler.inter.StmtAst;
import cn.xiw.compiler.inter.VarDecl;
import cn.xiw.compiler.lexer.TokenType;

public class CodeGeneratorTest {
    private ByteArrayOutputStream oStream = new ByteArrayOutputStream();
    private CodeGenerator codeGenerator = new CodeGenerator(oStream);

    @Test
    void testBasic() {
        // arrange
        var nullStmt = NullStmt.instance();
        // act
        nullStmt.accept(codeGenerator);
        // assert
        assertEquals(0, oStream.toString().length());
    }

    @Test
    void testVarDecl() {
        // arrange
        // int num;
        var varDecl = VarDecl.builder().identifier("num").typeId("int").build();
        varDecl.setType(BuiltinType.INT_TYPE);
        var declStmt = DeclStmt.builder().varDecl(varDecl).build();
        // act
        declStmt.accept(codeGenerator);
        // assert
        assertEquals("\talloc \t4\tBytes: num\n", oStream.toString());
    }

    @Test
    void testAssignStmt() {
        // arrange
        // i = 10;
        var varDecl = VarDecl.builder().identifier("i").typeId("int").build();
        varDecl.setType(BuiltinType.INT_TYPE);
        var declRefExpr = DeclRefExpr.builder().id("i").build();
        declRefExpr.setVarDecl(varDecl);
        declRefExpr.setTypeId("int");
        declRefExpr.setType(BuiltinType.INT_TYPE);
        var assignStmt = ExprStmt.builder()
                .expr(BinaryOp.builder().op(TokenType.PUNCT_EQ)
                        .expr1(declRefExpr)
                        .expr2(IntLiteral.builder().value(10).build()).build())
                .build();
        // act
        assignStmt.accept(codeGenerator);
        // assert
        assertEquals("\tt1 = 10\n\ti = t1\n\tt2 = i\n", oStream.toString());
    }

    @Test
    void testExpr() {
        // arrange
        // 10 + i * 9
        var varDecl = VarDecl.builder().identifier("i").typeId("int").build();
        varDecl.setType(BuiltinType.INT_TYPE);
        var declRefExpr = DeclRefExpr.builder().id("i").build();
        declRefExpr.setVarDecl(varDecl);
        declRefExpr.setTypeId("int");
        declRefExpr.setType(BuiltinType.INT_TYPE);
        var binaryOp = BinaryOp.builder().op(TokenType.PUNCT_PLUS)
                .expr1(IntLiteral.builder().value(10).build())
                .expr2(BinaryOp.builder().op(TokenType.PUNCT_STAR)
                        .expr1(declRefExpr)
                        .expr2(IntLiteral.builder().value(9).build()).build())
                .build();
        // act
        binaryOp.accept(codeGenerator);
        // assert
        var codes = List.of("\tt1 = 10", "\tt2 = 9", "\tt3 = MUL i t2",
                "\tt4 = ADD t1 t3", "");
        assertEquals(codes.stream().collect(Collectors.joining("\n")),
                oStream.toString());
    }

    @Test
    void testIfStmt() {
        // arrange
        // if (10 >= 90) i = 90; else i = 10;
        var expr = BinaryOp.builder().op(TokenType.PUNCT_GE)
                .expr1(IntLiteral.builder().value(10).build())
                .expr2(IntLiteral.builder().value(90).build()).build();
        var varDecl = VarDecl.builder().identifier("i").typeId("int").build();
        varDecl.setType(BuiltinType.INT_TYPE);
        var declRefExpr = DeclRefExpr.builder().id("i").build();
        declRefExpr.setVarDecl(varDecl);
        declRefExpr.setTypeId("int");
        declRefExpr.setType(BuiltinType.INT_TYPE);
        var ifSubStmt = ExprStmt.builder()
                .expr(BinaryOp.builder().op(TokenType.PUNCT_EQ)
                        .expr1(declRefExpr)
                        .expr2(IntLiteral.builder().value(90).build()).build())
                .build();
        var elseSubStmt = ExprStmt.builder()
                .expr(BinaryOp.builder().op(TokenType.PUNCT_EQ)
                        .expr1(declRefExpr)
                        .expr2(IntLiteral.builder().value(10).build()).build())
                .build();

        var ifStmt = IfStmt.builder().expr(expr).ifStmt(ifSubStmt)
                .elseStmt(elseSubStmt).build();
        // act
        ifStmt.accept(codeGenerator);
        // assert
        var codes = List.of("\tt1 = 10", "\tt2 = 90", "\tt3 = GE t1 t2",
                "\tiffalse t3 goto L1", "\tt4 = 90", "\ti = t4", "\tt5 = i",
                "\tgoto L2", "L1:", "\tt6 = 10", "\ti = t6", "\tt7 = i", "L2:",
                "");
        assertEquals(codes.stream().collect(Collectors.joining("\n")),
                oStream.toString());
    }

    @Test
    void testElemAccessOp() {
        // arrange
        // { int nums[10]; nums[1] = nums[0] + 1; }
        var varDecl = VarDecl.builder().identifier("nums").typeId("int[ 10 ]")
                .build();
        varDecl.setType(ArrayType.builder().baseType(BuiltinType.INT_TYPE)
                .size(10).build());

        var declStmt = DeclStmt.builder().varDecl(varDecl).build();
        var leftElem = ElemAccessOp.builder().id("nums")
                .index(IntLiteral.builder().value(1).build()).build();
        leftElem.setTypeId("int");
        leftElem.setType(BuiltinType.INT_TYPE);
        leftElem.setIndex(IntLiteral.builder().value(4).build());
        leftElem.setArrayDecl(varDecl);
        var rightElem = ElemAccessOp.builder().id("nums")
                .index(IntLiteral.builder().value(0).build()).build();
        rightElem.setTypeId("int");
        rightElem.setType(BuiltinType.INT_TYPE);
        rightElem.setIndex(IntLiteral.builder().value(0).build());
        rightElem.setArrayDecl(varDecl);
        var assignmentOp = BinaryOp.builder().op(TokenType.PUNCT_EQ)
                .expr1(leftElem)
                .expr2(BinaryOp.builder().op(TokenType.PUNCT_PLUS)
                        .expr1(rightElem)
                        .expr2(IntLiteral.builder().value(1).build()).build())
                .build();
        var stmts = new ArrayList<StmtAst>();
        stmts.add(declStmt);
        stmts.add(ExprStmt.builder().expr(assignmentOp).build());

        // act
        CompoundStmt.builder().stmts(stmts).build().accept(codeGenerator);
        // assert
        var codes = List.of("\talloc \t40\tBytes: nums", "\tt1 = 4", "\tt2 = 0",
                "\tt3 = 1", "\tt4 = ADD nums[ t2 ] t3", "\tnums[ t1 ] = t4",
                "\tt5 = nums[ t1 ]", "");
        assertEquals(codes.stream().collect(Collectors.joining("\n")),
                oStream.toString());
    }
}

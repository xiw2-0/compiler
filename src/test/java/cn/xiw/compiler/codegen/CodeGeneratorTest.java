package cn.xiw.compiler.codegen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
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
        var varDecl = new VarDecl("num", BuiltinType.INT_TYPE);
        var declStmt = new DeclStmt(varDecl);
        // act
        declStmt.accept(codeGenerator);
        // assert
        assertEquals("\talloc \t4\tBytes: num\n", oStream.toString());
    }

    @Test
    void testAssignStmt() {
        // arrange
        // i = 10;
        var varDecl = new VarDecl("i", BuiltinType.INT_TYPE);
        var assignStmt = new ExprStmt(new BinaryOp(TokenType.PUNCT_EQ,
                new DeclRefExpr(varDecl), new IntLiteral(10)));
        // act
        assignStmt.accept(codeGenerator);
        // assert
        assertEquals("\tt1 = 10\n\ti = t1\n\tt2 = i\n", oStream.toString());
    }

    @Test
    void testExpr() {
        // arrange
        // 10 + i * 9
        var varDecl = new VarDecl("i", BuiltinType.INT_TYPE);
        var binaryOp = new BinaryOp(TokenType.PUNCT_PLUS, new IntLiteral(10),
                new BinaryOp(TokenType.PUNCT_STAR, new DeclRefExpr(varDecl),
                        new IntLiteral(9)));
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
        var expr = new BinaryOp(TokenType.PUNCT_GE, new IntLiteral(10),
                new IntLiteral(90));
        var varDecl = new VarDecl("i", BuiltinType.INT_TYPE);
        var ifSubStmt = new ExprStmt(new BinaryOp(TokenType.PUNCT_EQ,
                new DeclRefExpr(varDecl), new IntLiteral(90)));
        var elseSubStmt = new ExprStmt(new BinaryOp(TokenType.PUNCT_EQ,
                new DeclRefExpr(varDecl), new IntLiteral(10)));
        var ifStmt = new IfStmt(expr, ifSubStmt, elseSubStmt);
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
        var varDecl = new VarDecl("nums",
                new ArrayType(BuiltinType.INT_TYPE, 10));
        var declStmt = new DeclStmt(varDecl);
        var leftElem = new ElemAccessOp(new DeclRefExpr(varDecl),
                new IntLiteral(4));
        var rightElem = new ElemAccessOp(new DeclRefExpr(varDecl),
                new IntLiteral(0));
        var assignmentOp = new BinaryOp(TokenType.PUNCT_EQ, leftElem,
                new BinaryOp(TokenType.PUNCT_PLUS, rightElem,
                        new IntLiteral(1)));
        var blockStmt = new CompoundStmt();
        blockStmt.addStmt(declStmt);
        blockStmt.addStmt(new ExprStmt(assignmentOp));

        // act
        blockStmt.accept(codeGenerator);
        // assert
        var codes = List.of("\talloc \t40\tBytes: nums", "\tt1 = 4", "\tt2 = 0",
                "\tt3 = 1", "\tt4 = ADD nums[ t2 ] t3", "\tnums[ t1 ] = t4",
                "\tt5 = nums[ t1 ]", "");
        assertEquals(codes.stream().collect(Collectors.joining("\n")),
                oStream.toString());
    }
}

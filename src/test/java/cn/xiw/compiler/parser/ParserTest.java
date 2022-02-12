package cn.xiw.compiler.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import cn.xiw.compiler.inter.ExprStmt;
import cn.xiw.compiler.inter.ArrayType;
import cn.xiw.compiler.inter.BinaryOp;
import cn.xiw.compiler.inter.CompoundStmt;
import cn.xiw.compiler.inter.DeclRefExpr;
import cn.xiw.compiler.inter.BuiltinType;
import cn.xiw.compiler.inter.DeclStmt;
import cn.xiw.compiler.inter.ElemAccessOp;
import cn.xiw.compiler.inter.IfStmt;
import cn.xiw.compiler.inter.IntLiteral;
import cn.xiw.compiler.inter.NullStmt;
import cn.xiw.compiler.inter.WhileStmt;
import cn.xiw.compiler.lexer.Lexer;
import cn.xiw.compiler.lexer.Token;
import cn.xiw.compiler.lexer.TokenType;

public class ParserTest {
    private Parser parser;

    @Mock
    private Lexer lexer = mock(Lexer.class);

    @Test
    void testBasic() throws IOException {
        // arrange
        when(lexer.scan()).thenReturn(Token.punctTok("{"), Token.punctTok(";"),
                Token.punctTok("}"), Token.eofTok());
        parser = new Parser(lexer);

        // act
        var ast = (CompoundStmt) parser.parse();

        // assert
        var stmts = ast.getStmts();
        assertTrue(stmts.size() == 1);
        assertTrue(stmts.get(0) instanceof NullStmt);
    }

    @Test
    void testDeclStmt() throws IOException {
        // arrange
        when(lexer.scan()).thenReturn(Token.punctTok("{"),
                Token.keywordTok("int"), Token.identifierTok("num"),
                Token.punctTok(";"), Token.punctTok("}"), Token.eofTok());
        parser = new Parser(lexer);

        // act
        var ast = (CompoundStmt) parser.parse();

        // assert
        var stmts = ast.getStmts();
        assertTrue(stmts.size() == 1);
        var varDecl = ((DeclStmt) stmts.get(0)).getVarDecl();
        assertTrue(varDecl.getIdentifier() == "num");
        assertTrue(varDecl.getType() == BuiltinType.INT_TYPE);
    }

    @Test
    void testDeclStmtAndAssignStmt() throws IOException {
        // arrange
        when(lexer.scan()).thenReturn(Token.punctTok("{"),
                Token.keywordTok("int"), Token.identifierTok("num"),
                Token.punctTok(";"), Token.identifierTok("num"),
                Token.punctTok("="), Token.constIntTok(100),
                Token.punctTok(";"), Token.punctTok("}"), Token.eofTok());
        parser = new Parser(lexer);

        // act
        var ast = (CompoundStmt) parser.parse();

        // assert
        var stmts = ast.getStmts();
        assertTrue(stmts.size() == 2);
        var varDecl = ((DeclStmt) stmts.get(0)).getVarDecl();
        assertTrue(varDecl.getIdentifier() == "num");
        assertTrue(varDecl.getType() == BuiltinType.INT_TYPE);

        var assignExpr = (BinaryOp) ((ExprStmt) stmts.get(1)).getExpr();
        assertEquals(TokenType.PUNCT_EQ, assignExpr.getOp());
        assertEquals(varDecl,
                ((DeclRefExpr) assignExpr.getExpr1()).getVarDecl());
        assertEquals(100, ((IntLiteral) assignExpr.getExpr2()).getValue());
    }

    @Test
    void testIfStmt() throws IOException {
        // arrange
        when(lexer.scan()).thenReturn(Token.punctTok("{"),
                Token.keywordTok("if"), Token.punctTok("("),
                Token.constIntTok(1), Token.punctTok(">="),
                Token.constIntTok(10), Token.punctTok(")"), Token.punctTok(";"),
                Token.keywordTok("else"), Token.punctTok("{"),
                Token.punctTok(";"), Token.punctTok("}"), Token.punctTok("}"),
                Token.eofTok());
        parser = new Parser(lexer);

        // act
        var ast = (CompoundStmt) parser.parse();

        // assert
        var stmts = ast.getStmts();
        assertTrue(stmts.size() == 1);
        var ifStmt = (IfStmt) stmts.get(0);
        assertTrue(ifStmt.isContainingElse());

        // expr
        var expr = (BinaryOp) ifStmt.getExpr();
        assertTrue(((IntLiteral) (expr.getExpr1())).getValue() == 1);
        assertTrue(((IntLiteral) (expr.getExpr2())).getValue() == 10);
        assertTrue(expr.getOp() == TokenType.PUNCT_GE);
        // if
        assertTrue(ifStmt.getIfStmt() == NullStmt.instance());
        // else
        var elseSubStmt = (CompoundStmt) ifStmt.getElseStmt();
        assertTrue(elseSubStmt.getStmts().size() == 1);
        assertTrue(elseSubStmt.getStmts().get(0) == NullStmt.instance());
    }

    @Test
    void testWhileStmt() throws IOException {
        // arrange
        when(lexer.scan()).thenReturn(Token.punctTok("{"),
                Token.keywordTok("while"), Token.punctTok("("),
                Token.constIntTok(1), Token.punctTok(">="),
                Token.constIntTok(10), Token.punctTok(")"), Token.punctTok("{"),
                Token.punctTok(";"), Token.punctTok("}"), Token.punctTok("}"),
                Token.eofTok());
        parser = new Parser(lexer);

        // act
        var ast = (CompoundStmt) parser.parse();

        // assert
        var stmts = ast.getStmts();
        assertTrue(stmts.size() == 1);
        var whileStmt = (WhileStmt) stmts.get(0);

        // expr
        var expr = (BinaryOp) whileStmt.getExpr();
        assertTrue(((IntLiteral) (expr.getExpr1())).getValue() == 1);
        assertTrue(((IntLiteral) (expr.getExpr2())).getValue() == 10);
        assertTrue(expr.getOp() == TokenType.PUNCT_GE);

        var bodyStmt = (CompoundStmt) whileStmt.getStmt();
        assertTrue(bodyStmt.getStmts().size() == 1);
        assertTrue(bodyStmt.getStmts().get(0) == NullStmt.instance());
    }

    @Test
    void testArrayDeclAndAssignStmt() throws IOException {
        // arrange
        when(lexer.scan()).thenReturn(Token.punctTok("{"),
                Token.keywordTok("int"), Token.punctTok("["),
                Token.constIntTok(6), Token.punctTok("]"),
                Token.identifierTok("nums"), Token.punctTok(";"),
                Token.identifierTok("nums"), Token.punctTok("["),
                Token.constIntTok(0), Token.punctTok("]"), Token.punctTok("="),
                Token.constIntTok(100), Token.punctTok(";"),
                Token.punctTok("}"), Token.eofTok());
        parser = new Parser(lexer);

        // act
        var ast = (CompoundStmt) parser.parse();

        // assert
        var stmts = ast.getStmts();
        assertTrue(stmts.size() == 2);
        // decl stmt
        var varDecl = ((DeclStmt) stmts.get(0)).getVarDecl();
        assertTrue(varDecl.getIdentifier() == "nums");
        assertTrue(varDecl.getType()
                .equals(new ArrayType(BuiltinType.INT_TYPE, 6)));
        // assign expr stmt
        var elemAssign = (BinaryOp) ((ExprStmt) stmts.get(1)).getExpr();
        assertEquals(TokenType.PUNCT_EQ, elemAssign.getOp());
        // left
        var elemAccessOp = (ElemAccessOp) elemAssign.getExpr1();
        assertEquals(varDecl, elemAccessOp.getArrayId().getVarDecl());
        var indexExpr = (BinaryOp) elemAccessOp.getIndex();
        assertEquals(TokenType.PUNCT_STAR, indexExpr.getOp());
        assertEquals(0, ((IntLiteral) indexExpr.getExpr1()).getValue());
        // right
        assertEquals(100, ((IntLiteral) elemAssign.getExpr2()).getValue());
    }

}

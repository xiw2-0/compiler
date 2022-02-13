package cn.xiw.compiler.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import cn.xiw.compiler.inter.ExprStmt;
import cn.xiw.compiler.inter.FloatLiteral;
import cn.xiw.compiler.inter.FuncDecl;
import cn.xiw.compiler.inter.BinaryOp;
import cn.xiw.compiler.inter.CompoundStmt;
import cn.xiw.compiler.inter.DeclRefExpr;
import cn.xiw.compiler.inter.CallExpr;
import cn.xiw.compiler.inter.DeclStmt;
import cn.xiw.compiler.inter.ElemAccessOp;
import cn.xiw.compiler.inter.IfStmt;
import cn.xiw.compiler.inter.IntLiteral;
import cn.xiw.compiler.inter.NullStmt;
import cn.xiw.compiler.inter.ReturnStmt;
import cn.xiw.compiler.inter.TranslationUnitAst;
import cn.xiw.compiler.inter.VarDecl;
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
        var ast = (CompoundStmt) parser.block();

        // assert
        var stmts = ast.getStmts();
        assertEquals(1, stmts.size());
        assertEquals(NullStmt.instance(), stmts.get(0));
    }

    @Test
    void testDeclStmt() throws IOException {
        // arrange
        when(lexer.scan()).thenReturn(Token.punctTok("{"),
                Token.keywordTok("int"), Token.identifierTok("num"),
                Token.punctTok(";"), Token.punctTok("}"), Token.eofTok());
        parser = new Parser(lexer);

        // act
        var ast = (CompoundStmt) parser.block();

        // assert
        var stmts = ast.getStmts();
        assertEquals(1, stmts.size());
        var varDecl = ((DeclStmt) stmts.get(0)).getVarDecl();
        assertEquals("num", varDecl.getIdentifier());
        assertEquals("int", varDecl.getTypeId());
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
        var ast = (CompoundStmt) parser.block();

        // assert
        var stmts = ast.getStmts();
        assertEquals(2, stmts.size());
        var varDecl = ((DeclStmt) stmts.get(0)).getVarDecl();
        assertEquals("num", varDecl.getIdentifier());
        assertEquals("int", varDecl.getTypeId());

        var assignExpr = (BinaryOp) ((ExprStmt) stmts.get(1)).getExpr();
        assertEquals(TokenType.PUNCT_EQ, assignExpr.getOp());
        assertEquals("num", ((DeclRefExpr) assignExpr.getExpr1()).getId());
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
        var ast = (CompoundStmt) parser.block();

        // assert
        var stmts = ast.getStmts();
        assertEquals(1, stmts.size());
        var ifStmt = (IfStmt) stmts.get(0);
        assertTrue(ifStmt.isContainingElse());

        // expr
        var expr = (BinaryOp) ifStmt.getExpr();
        assertEquals(1, ((IntLiteral) (expr.getExpr1())).getValue());
        assertEquals(10, ((IntLiteral) (expr.getExpr2())).getValue());
        assertEquals(TokenType.PUNCT_GE, expr.getOp());
        // if
        assertEquals(NullStmt.instance(), ifStmt.getIfStmt());
        // else
        var elseSubStmt = (CompoundStmt) ifStmt.getElseStmt();
        assertEquals(1, elseSubStmt.getStmts().size());
        assertEquals(NullStmt.instance(), elseSubStmt.getStmts().get(0));
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
        var ast = (CompoundStmt) parser.block();

        // assert
        var stmts = ast.getStmts();
        assertEquals(1, stmts.size());
        var whileStmt = (WhileStmt) stmts.get(0);

        // expr
        var expr = (BinaryOp) whileStmt.getExpr();
        assertEquals(1, ((IntLiteral) (expr.getExpr1())).getValue());
        assertEquals(10, ((IntLiteral) (expr.getExpr2())).getValue());
        assertEquals(TokenType.PUNCT_GE, expr.getOp());

        var bodyStmt = (CompoundStmt) whileStmt.getStmt();
        assertEquals(1, bodyStmt.getStmts().size());
        assertEquals(NullStmt.instance(), bodyStmt.getStmts().get(0));
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
        var ast = (CompoundStmt) parser.block();

        // assert
        var stmts = ast.getStmts();
        assertEquals(2, stmts.size());
        // decl stmt
        var varDecl = ((DeclStmt) stmts.get(0)).getVarDecl();
        assertEquals("nums", varDecl.getIdentifier());
        assertEquals("int[6]", varDecl.getTypeId());
        // assign expr stmt
        var elemAssign = (BinaryOp) ((ExprStmt) stmts.get(1)).getExpr();
        assertEquals(TokenType.PUNCT_EQ, elemAssign.getOp());
        // left
        var elemAccessOp = (ElemAccessOp) elemAssign.getExpr1();
        assertEquals("nums", elemAccessOp.getArrayId());
        var indexExpr = (IntLiteral) elemAccessOp.getIndex();
        assertEquals(0, indexExpr.getValue());
        // right
        assertEquals(100, ((IntLiteral) elemAssign.getExpr2()).getValue());
    }

    @Test
    void testVarDecl() throws IOException {
        // arrange
        when(lexer.scan()).thenReturn(Token.keywordTok("int"),
                Token.identifierTok("num"), Token.punctTok(";"),
                Token.eofTok());
        parser = new Parser(lexer);

        // act
        var unitAst = (TranslationUnitAst) parser.parse();

        // assert
        var decls = unitAst.getDeclarations();
        assertEquals(1, decls.size());
        var varDecl = (VarDecl) decls.get(0);
        assertEquals("int", varDecl.getTypeId());
        assertEquals("num", varDecl.getIdentifier());
    }

    @Test
    void testFuncDecl() throws IOException {
        // arrange
        // int getnum(float value, int count) {return 1.0;}
        when(lexer.scan()).thenReturn(Token.keywordTok("int"),
                Token.identifierTok("getnum"), Token.punctTok("("),
                Token.keywordTok("float"), Token.identifierTok("value"),
                Token.punctTok(","), Token.keywordTok("int"),
                Token.identifierTok("count"), Token.punctTok(")"),
                Token.punctTok("{"), Token.keywordTok("return"),
                Token.constFloatTok(1.0), Token.punctTok(";"),
                Token.punctTok("}"), Token.eofTok());
        parser = new Parser(lexer);

        // act
        var unitAst = (TranslationUnitAst) parser.parse();

        // assert
        var decls = unitAst.getDeclarations();
        assertEquals(1, decls.size());
        var funcDecl = (FuncDecl) decls.get(0);
        assertEquals("int", funcDecl.getTypeId());
        assertEquals("getnum", funcDecl.getIdentifier());
        var params = funcDecl.getParams();
        assertEquals(2, params.size());
        assertEquals("float", params.get(0).getTypeId());
        assertEquals("value", params.get(0).getIdentifier());
        assertEquals("int", params.get(1).getTypeId());
        assertEquals("count", params.get(1).getIdentifier());
        var body = funcDecl.getBody().getStmts();
        assertEquals(1, body.size());
        assertEquals("getnum", ((ReturnStmt) body.get(0)).getFuncId());
        assertEquals(1.0,
                ((FloatLiteral) ((ReturnStmt) body.get(0)).getRetExpr())
                        .getValue());
    }

    @Test
    void testCallExpr() throws IOException {
        // arrange
        when(lexer.scan()).thenReturn(Token.punctTok("{"),
                Token.keywordTok("int"), Token.identifierTok("num"),
                Token.punctTok(";"), Token.identifierTok("num"),
                Token.punctTok("="), Token.identifierTok("getnum"),
                Token.punctTok("("), Token.constIntTok(100),
                Token.punctTok(")"), Token.punctTok(";"), Token.punctTok("}"),
                Token.eofTok());
        parser = new Parser(lexer);

        // act
        var ast = (CompoundStmt) parser.block();

        // assert
        var stmts = ast.getStmts();
        assertEquals(2, stmts.size());
        var varDecl = ((DeclStmt) stmts.get(0)).getVarDecl();
        assertEquals("num", varDecl.getIdentifier());
        assertEquals("int", varDecl.getTypeId());

        var assignExpr = (BinaryOp) ((ExprStmt) stmts.get(1)).getExpr();
        assertEquals(TokenType.PUNCT_EQ, assignExpr.getOp());
        assertEquals("num", ((DeclRefExpr) assignExpr.getExpr1()).getId());
        var callExpr = (CallExpr) assignExpr.getExpr2();
        assertEquals("getnum", callExpr.getFuncId());
        assertEquals(1, callExpr.getParams().size());
        assertEquals(100,
                ((IntLiteral) callExpr.getParams().get(0)).getValue());
    }

}

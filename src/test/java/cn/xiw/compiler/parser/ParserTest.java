package cn.xiw.compiler.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import cn.xiw.compiler.inter.AssignStmt;
import cn.xiw.compiler.inter.BlockStmt;
import cn.xiw.compiler.inter.BuiltinType;
import cn.xiw.compiler.inter.DeclStmt;
import cn.xiw.compiler.inter.IntLiteral;
import cn.xiw.compiler.inter.NullStmt;
import cn.xiw.compiler.lexer.Lexer;
import cn.xiw.compiler.lexer.Token;

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
        var ast = (BlockStmt) parser.parse();

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
        var ast = (BlockStmt) parser.parse();

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
        var ast = (BlockStmt) parser.parse();

        // assert
        var stmts = ast.getStmts();
        assertTrue(stmts.size() == 2);
        var varDecl = ((DeclStmt) stmts.get(0)).getVarDecl();
        assertTrue(varDecl.getIdentifier() == "num");
        assertTrue(varDecl.getType() == BuiltinType.INT_TYPE);
        var varDeclRef = ((AssignStmt) stmts.get(1)).getId();
        assertTrue(varDeclRef.getVarDecl() == varDecl);
        assertTrue(((IntLiteral) ((AssignStmt) stmts.get(1)).getExpr())
                .getValue() == 100);
    }
}

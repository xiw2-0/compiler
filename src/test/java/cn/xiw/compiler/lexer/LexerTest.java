package cn.xiw.compiler.lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class LexerTest {
    private Lexer lexer;

    @Test
    void testBasic() throws IOException {
        String srcCodes = "{\n};\n";
        var expectedToks = List.of(Token.punctTok("{"), Token.punctTok("}"),
                Token.punctTok(";"), Token.eofTok());
        lexer = new Lexer(new ByteArrayInputStream(srcCodes.getBytes()));

        var toks = new ArrayList<Token>();
        Token curTok;
        do {
            curTok = lexer.scan();
            toks.add(curTok);
        } while (curTok.getType() != TokenType.EOF);

        assertTokens(expectedToks, toks);
    }

    @Test
    void testVarDeclAndAssign() throws IOException {
        String srcCodes = "int num;\nnum = 100;\n";
        var expectedToks = List.of(Token.keywordTok("int"),
                Token.identifierTok("num"), Token.punctTok(";"),
                Token.identifierTok("num"), Token.punctTok("="),
                Token.constIntTok(100), Token.punctTok(";"), Token.eofTok());
        lexer = new Lexer(new ByteArrayInputStream(srcCodes.getBytes()));

        var toks = new ArrayList<Token>();
        Token curTok;
        do {
            curTok = lexer.scan();
            toks.add(curTok);
        } while (curTok.getType() != TokenType.EOF);

        assertTokens(expectedToks, toks);
    }

    @Test
    void testLiterals() throws IOException {
        String srcCodes = "\'a\' 10 1.0 \"str\"\n";
        lexer = new Lexer(new ByteArrayInputStream(srcCodes.getBytes()));

        var toks = new ArrayList<Token>();
        Token curTok;
        do {
            curTok = lexer.scan();
            toks.add(curTok);
        } while (curTok.getType() != TokenType.EOF);

        assertEquals(TokenType.CONST_CHAR, toks.get(0).getType());
        assertEquals('a', toks.get(0).valueChar());
        assertEquals(TokenType.CONST_INT, toks.get(1).getType());
        assertEquals(10, toks.get(1).valueInt());
        assertEquals(TokenType.CONST_FLOAT, toks.get(2).getType());
        assertEquals(1.0, toks.get(2).valueFloat());
        assertEquals(TokenType.STRING_LITERAL, toks.get(3).getType());
        assertEquals("str", toks.get(3).valueString());
    }

    private void assertTokens(List<Token> expected, List<Token> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); ++i) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

}

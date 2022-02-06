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

    private void assertTokens(List<Token> expected, List<Token> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); ++i) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

}

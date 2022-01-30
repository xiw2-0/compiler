package cn.xiw.compiler.symbols;

import java.util.HashMap;
import java.util.Map;

import cn.xiw.compiler.lexer.Token;

/**
 * Stores environment, mainly symbol tables for different scopes. Build symbol
 * table while parsing.
 */
public class Env {
    // Store symbol table for current environment.
    // Mapping from tokens to symbols (ids)
    private final Map<Token, Object> symbolTable = new HashMap<>();

    // Points to previous environment.
    private final Env prev;

    public Env(Env prev) {
        this.prev = prev;
    }

    public void addSymbol(Token token, Object id) {
        symbolTable.put(token, id);
    }

    public Object getSymbol(Token token) {
        for (var env = this; env != null; env = env.prev) {
            if (env.symbolTable.containsKey(token)) {
                return env.symbolTable.get(token);
            }
        }
        return null;
    }
}

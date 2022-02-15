package cn.xiw.compiler.symbols;

import java.util.HashMap;
import java.util.Map;

import cn.xiw.compiler.inter.DeclAst;

/**
 * Stores environment, mainly symbol tables for different scopes. Build symbol
 * table while parsing.
 */
public class Env {
    // Store symbol table for current environment.
    // Mapping from identifier string to decl ast node (ids)
    private final Map<String, DeclAst> symbolTable = new HashMap<>();

    // Points to previous environment.
    private final Env prev;

    public Env(Env prev) {
        this.prev = prev;
    }

    public boolean addSymbol(String id, DeclAst decl) {
        if (symbolTable.containsKey(id)) {
            return false;
        }
        symbolTable.put(id, decl);
        return true;
    }

    public DeclAst getSymbol(String id) {
        for (var env = this; env != null; env = env.prev) {
            if (env.symbolTable.containsKey(id)) {
                return env.symbolTable.get(id);
            }
        }
        return null;
    }
}

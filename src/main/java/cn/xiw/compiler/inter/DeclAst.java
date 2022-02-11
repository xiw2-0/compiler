package cn.xiw.compiler.inter;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A decl will have a type, but may not have an identifier.
 */
@Getter
@AllArgsConstructor
public abstract class DeclAst extends AstNode {
    private final Type type;
}

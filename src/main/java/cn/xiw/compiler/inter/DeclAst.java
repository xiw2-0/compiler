package cn.xiw.compiler.inter;

import lombok.Getter;
import lombok.Setter;

/**
 * A decl will have a type, but may not have an identifier.
 */
@Getter
@Setter
public abstract class DeclAst extends AstNode {
    private final String typeId;
    private Type type;

    DeclAst(String typeId) {
        this.typeId = typeId;
    }
}

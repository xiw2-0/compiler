package cn.xiw.compiler.inter;

import lombok.Builder;
import lombok.Getter;

/**
 * Currently, only for builtin types and array types.
 */
@Getter
public class TypeDecl extends DeclAst {
    private final String identifier;

    // Related type for this type decl.
    private final Type typeForDecl;

    @Builder
    TypeDecl(String identifier, Type typeForDecl) {
        super("TypeOfType");
        setType(TypeOfType.instance());

        this.identifier = identifier;
        this.typeForDecl = typeForDecl;
    }

    @Override
    public void accept(AstVisitor visitor) {
        // Do nothing for builtin types and array types. Will not appear in the
        // AST tree.
    }
}

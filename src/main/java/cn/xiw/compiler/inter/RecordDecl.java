package cn.xiw.compiler.inter;

/**
 * For struct definition.
 */
public class RecordDecl extends DeclAst {

    public RecordDecl(String identifier) {
        super(identifier);

    }

    @Override
    public void accept(AstVisitor visitor) {

    }

}

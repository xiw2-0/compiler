package cn.xiw.compiler.inter;

import java.util.ArrayList;

import lombok.Getter;

/**
 * Array declaration.
 */
@Getter
public class ArrayDecl extends DeclAst {
    private final String identifier;
    private ArrayList<Integer> dims;

    public ArrayDecl(Type type, String identifier) {
        super(type);
        this.identifier = identifier;
        dims = new ArrayList<>();
    }

    public void addDim(int size) {
        dims.add(size);
    }

    @Override
    public void accept(AstVisitor visitor) {
        // TODO Auto-generated method stub

    }

}

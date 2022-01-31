package cn.xiw.compiler.inter;

import java.util.ArrayList;

import ch.qos.logback.core.subst.Token.Type;
import lombok.Getter;

@Getter
public class FuncDecl extends DeclAst {
    private final Type returnType;
    private final ArrayList<ParamDecl> params = new ArrayList<>();

    FuncDecl(Type returnType, String identifier) {
        super(identifier);
        this.returnType = returnType;
    }

    void addParam(ParamDecl paramDecl) {
        params.add(paramDecl);
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

package cn.xiw.compiler.inter;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class CallExpr extends ExprAst {
    private final DeclAst proto;
    private ArrayList<ExprAst> params;

    public CallExpr(DeclAst proto) {
        super(proto.getType());
        this.proto = proto;
        params = new ArrayList<>();
    }

    public void addParam(ExprAst param) {
        params.add(param);
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

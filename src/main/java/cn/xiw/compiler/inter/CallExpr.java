package cn.xiw.compiler.inter;

import java.util.ArrayList;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallExpr extends ExprAst {
    private final String funcId;
    private FuncDecl funcDecl;
    private final ArrayList<ExprAst> params;

    @Builder
    CallExpr(String funcId, ArrayList<ExprAst> params) {
        this.funcId = funcId;
        this.params = params;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

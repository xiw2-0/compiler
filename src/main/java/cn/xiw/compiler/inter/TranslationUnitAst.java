package cn.xiw.compiler.inter;

import java.util.ArrayList;

import lombok.Builder;
import lombok.Getter;

/**
 * Top level of translation.
 */
@Builder
public class TranslationUnitAst extends AstNode {
    @Getter
    private ArrayList<DeclAst> declarations;

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

package cn.xiw.compiler.inter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class ExprAst extends AstNode {
    String typeId;
    Type type;
}

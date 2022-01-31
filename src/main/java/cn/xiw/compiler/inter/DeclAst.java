package cn.xiw.compiler.inter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class DeclAst extends AstNode {
    private final String identifier;
}

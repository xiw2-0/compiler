package cn.xiw.compiler.inter;

/**
 * Node in AST.
 */
public abstract class AstNode {

    public abstract void accept(AstVisitor visitor);
}
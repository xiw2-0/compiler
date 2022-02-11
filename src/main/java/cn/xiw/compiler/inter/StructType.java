package cn.xiw.compiler.inter;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class StructType extends Type {
    private ArrayList<DeclAst> structMemberDecls;

    public StructType(String typeName, int width) {
        super(typeName, width);
    }

    public void addMemberDecl(DeclAst memberDecl) {
        structMemberDecls.add(memberDecl);
    }
}

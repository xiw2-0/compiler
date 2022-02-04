package cn.xiw.compiler.inter;

import lombok.Getter;

@Getter
public class RecordType extends Type {
    private final RecordDecl recordDeclRef;

    RecordType(String typeName, int width, RecordDecl recordDecl) {
        super(recordDecl.getIdentifier(), width);
        this.recordDeclRef = recordDecl;
    }
}

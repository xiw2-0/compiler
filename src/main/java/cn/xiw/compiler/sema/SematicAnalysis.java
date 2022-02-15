package cn.xiw.compiler.sema;

import java.util.ArrayList;

import cn.xiw.compiler.inter.ArrayType;
import cn.xiw.compiler.inter.BaseVisitor;
import cn.xiw.compiler.inter.BinaryOp;
import cn.xiw.compiler.inter.BuiltinType;
import cn.xiw.compiler.inter.CallExpr;
import cn.xiw.compiler.inter.CharLiteral;
import cn.xiw.compiler.inter.CompoundStmt;
import cn.xiw.compiler.inter.DeclRefExpr;
import cn.xiw.compiler.inter.ElemAccessOp;
import cn.xiw.compiler.inter.FloatLiteral;
import cn.xiw.compiler.inter.FuncDecl;
import cn.xiw.compiler.inter.IfStmt;
import cn.xiw.compiler.inter.IntLiteral;
import cn.xiw.compiler.inter.ReturnStmt;
import cn.xiw.compiler.inter.StringLiteral;
import cn.xiw.compiler.inter.TranslationUnitAst;
import cn.xiw.compiler.inter.Type;
import cn.xiw.compiler.inter.TypeDecl;
import cn.xiw.compiler.inter.UnaryOp;
import cn.xiw.compiler.inter.VarDecl;
import cn.xiw.compiler.inter.WhileStmt;
import cn.xiw.compiler.lexer.TokenType;
import cn.xiw.compiler.symbols.Env;

/**
 * #1 Check declarations.
 */
public class SematicAnalysis extends BaseVisitor {
    private Env top = initEnv(); // the top symbol table

    private ArrayList<VarDecl> funcParams = null;

    private static Env initEnv() {
        var top = new Env(null);
        // Add builtin types
        top.addSymbol("int", TypeDecl.builder().identifier("int")
                .typeForDecl(BuiltinType.INT_TYPE).build());
        top.addSymbol("char", TypeDecl.builder().identifier("char")
                .typeForDecl(BuiltinType.CHAR_TYPE).build());
        top.addSymbol("float", TypeDecl.builder().identifier("float")
                .typeForDecl(BuiltinType.FLOAT_TYPE).build());
        top.addSymbol("void", TypeDecl.builder().identifier("void")
                .typeForDecl(BuiltinType.VOID_TYPE).build());
        return top;
    }

    private void error(String format, Object... args) {
        throw new SemaException(
                String.format("Sema error: " + format, args).toString());
    }

    /**
     * Checked whether symbols are declared or not.
     */
    @Override
    public void visit(DeclRefExpr declRefExpr) {
        var id = top.getSymbol(declRefExpr.getId());
        if (id == null || !(id instanceof VarDecl)) {
            error("Undeclared variable %s ", declRefExpr.getId());
        }
        // binding variable
        var varDecl = (VarDecl) id;
        declRefExpr.setVarDecl(varDecl);
        declRefExpr.setTypeId(varDecl.getTypeId());
        declRefExpr.setType(varDecl.getType());
    }

    @Override
    public void visit(BinaryOp binaryOp) {
        super.visit(binaryOp);

        var leftExprType = binaryOp.getExpr1().getType();
        if (!(leftExprType instanceof BuiltinType)) {
            error("Operator %s only supports BuiltinType", binaryOp.getOp());
        }
        var rightExprType = binaryOp.getExpr2().getType();
        if (!(rightExprType instanceof BuiltinType)) {
            error("Operator %s only supports BuiltinType. Got %s",
                    binaryOp.getOp(), rightExprType);
        }

        switch (binaryOp.getOp()) {
        // assign: =
        case PUNCT_EQ:
            if (!leftExprType.getTypeName()
                    .equals(rightExprType.getTypeName())) {
                error("Two sides of assignment should have the same type. LHS %s, RHS %s",
                        leftExprType.getTypeName(),
                        rightExprType.getTypeName());
            }
            binaryOp.setType(leftExprType);
            binaryOp.setTypeId(leftExprType.getTypeName());
            break;
        // arith: + - * / %
        case PUNCT_PLUS:
        case PUNCT_MINUS:
        case PUNCT_STAR:
        case PUNCT_DIV:
        case PUNCT_MOD:
            var maxType = BuiltinType.max((BuiltinType) leftExprType,
                    (BuiltinType) rightExprType);
            binaryOp.setType(maxType);
            binaryOp.setTypeId(maxType.getTypeName());
            break;
        // logical: == != < <= >= >
        default:
            binaryOp.setType(BuiltinType.INT_TYPE);
            binaryOp.setTypeId("int");
            break;
        }
    }

    @Override
    public void visit(UnaryOp unaryOp) {
        super.visit(unaryOp);
        if (unaryOp.getOp() == TokenType.PUNCT_MINUS) {
            unaryOp.setTypeId(unaryOp.getExpr().getTypeId());
            unaryOp.setType(unaryOp.getExpr().getType());
        } else {
            unaryOp.setTypeId("int");
            unaryOp.setType(BuiltinType.INT_TYPE);
        }
    }

    @Override
    public void visit(ElemAccessOp accessOp) {
        super.visit(accessOp);
        var arrayDecl = top.getSymbol(accessOp.getArrayId());
        if (arrayDecl == null || !(arrayDecl instanceof VarDecl)) {
            error("Undeclared variable %s", accessOp.getArrayId());
        }
        // binding variable
        var varDecl = (VarDecl) arrayDecl;
        if (!(varDecl.getType() instanceof ArrayType)) {
            error("Variable %s is not an array", varDecl.getIdentifier());
        }
        accessOp.setArrayDecl(varDecl);
        accessOp.setTypeId(
                ((ArrayType) varDecl.getType()).getOf().getTypeName());
        accessOp.setType(((ArrayType) varDecl.getType()).getOf());
    }

    @Override
    public void visit(CallExpr callExpr) {
        super.visit(callExpr);

        var decl = top.getSymbol(callExpr.getFuncId());
        if (decl == null || !(decl instanceof FuncDecl)) {
            error("Undeclared function %s", callExpr.getFuncId());
        }
        var funcdecl = (FuncDecl) decl;
        // para size checking
        if (funcdecl.getParams().size() != callExpr.getParams().size()) {
            error("Expect %d parameters for function %s",
                    funcdecl.getParams().size(), funcdecl.getIdentifier());
        }
        // para type checking
        for (int i = 0; i < funcdecl.getParams().size(); ++i) {
            if (!funcdecl.getParams().get(i).getTypeId()
                    .equals(callExpr.getParams().get(i).getTypeId())) {
                // no type cast here
                error("Expect a %s, but get a %s for func call",
                        funcdecl.getParams().get(i).getTypeId(),
                        callExpr.getParams().get(i).getTypeId());
            }
        }
        // binding
        callExpr.setFuncDecl(funcdecl);
        callExpr.setTypeId(funcdecl.getTypeId());
        callExpr.setType(funcdecl.getType());
    }

    @Override
    public void visit(IntLiteral intLiteral) {
        intLiteral.setType(getType(intLiteral.getTypeId()));
    }

    @Override
    public void visit(CharLiteral charLiteral) {
        charLiteral.setType(getType(charLiteral.getTypeId()));
    }

    @Override
    public void visit(FloatLiteral floatLiteral) {
        floatLiteral.setType(getType(floatLiteral.getTypeId()));
    }

    @Override
    public void visit(StringLiteral stringLiteral) {
        stringLiteral.setType(getType(stringLiteral.getTypeId()));
    }

    @Override
    public void visit(IfStmt ifStmt) {
        super.visit(ifStmt);

        // check the type of expr.
        if (!ifStmt.getExpr().getTypeId().equals("int")) {
            error("A condition should be an int. Got a %s",
                    ifStmt.getExpr().getTypeId());
        }
    }

    @Override
    public void visit(WhileStmt whileStmt) {
        super.visit(whileStmt);

        // check the type of expr.
        if (!whileStmt.getExpr().getTypeId().equals("int")) {
            error("A condition should be an int. Got a %s",
                    whileStmt.getExpr().getTypeId());
        }
    }

    @Override
    public void visit(CompoundStmt blockStmt) {
        Env savedEnv = top;
        top = new Env(top);
        if (funcParams != null) {
            funcParams.stream().forEach(this::visit);
            funcParams = null;
        }
        super.visit(blockStmt);
        top = savedEnv;
    }

    @Override
    public void visit(ReturnStmt returnStmt) {
        super.visit(returnStmt);

        var funcId = returnStmt.getFuncId();
        if (null == top.getSymbol(funcId)) {
            error("Undeclared function %s", funcId);
        }
        if (!(top.getSymbol(funcId) instanceof FuncDecl)) {
            error("Symbol %s is not a function", funcId);
        }
        returnStmt.setFunc((FuncDecl) top.getSymbol(funcId));
        if (!returnStmt.getFunc().getTypeId()
                .equals(returnStmt.getFunc().getTypeId())) {
            error("Expect %s for return type. Got a %s",
                    returnStmt.getFunc().getTypeId(),
                    returnStmt.getFunc().getTypeId());
        }
    }

    @Override
    public void visit(VarDecl varDecl) {
        varDecl.setType(getType(varDecl.getTypeId()));

        if (!top.addSymbol(varDecl.getIdentifier(), varDecl)) {
            error("Duplicated variable declaration %s",
                    varDecl.getIdentifier());
        }
    }

    @Override
    public void visit(FuncDecl funcDecl) {
        // set return type
        funcDecl.setType(getType(funcDecl.getTypeId()));
        // add function name symbol
        if (!top.addSymbol(funcDecl.getIdentifier(), funcDecl)) {
            error("Duplicated variable declaration %s",
                    funcDecl.getIdentifier());
        }
        // set paramters' types
        funcDecl.getParams().stream().forEach(
                varDecl -> varDecl.setType(getType(varDecl.getTypeId())));
        // ready for keeping parameter's names
        funcParams = funcDecl.getParams();
        // body
        super.visit(funcDecl);
    }

    @Override
    public void visit(TranslationUnitAst translationUnitAst) {
        Env savedEnv = top;
        top = new Env(top);
        super.visit(translationUnitAst);
        top = savedEnv;
    }

    // Deals with array types.
    private Type getType(String typeId) {
        // check array
        int dim = -1;
        if (typeId.contains("[")) {
            dim = Integer.parseInt((typeId.substring(typeId.indexOf("[") + 1,
                    typeId.indexOf("]"))));
            typeId = typeId.substring(0, typeId.indexOf("["));
        }
        if (top.getSymbol(typeId) == null
                || !(top.getSymbol(typeId) instanceof TypeDecl)) {
            error("Undeclared type %s", typeId);
        }
        var baseType = ((TypeDecl) top.getSymbol(typeId)).getTypeForDecl();
        if (dim == -1) {
            return baseType;
        }
        return ArrayType.builder().baseType(baseType).size(dim).build();
    }
}

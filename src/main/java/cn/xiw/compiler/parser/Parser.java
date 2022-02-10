package cn.xiw.compiler.parser;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import cn.xiw.compiler.inter.ArrayType;
import cn.xiw.compiler.inter.AssignElemStmt;
import cn.xiw.compiler.inter.AssignStmt;
import cn.xiw.compiler.inter.AstNode;
import cn.xiw.compiler.inter.BinaryOp;
import cn.xiw.compiler.inter.BlockStmt;
import cn.xiw.compiler.inter.BreakStmt;
import cn.xiw.compiler.inter.CharLiteral;
import cn.xiw.compiler.inter.DeclRefExpr;
import cn.xiw.compiler.inter.DeclStmt;
import cn.xiw.compiler.inter.ElemAccessOp;
import cn.xiw.compiler.inter.ExprAst;
import cn.xiw.compiler.inter.FloatLiteral;
import cn.xiw.compiler.inter.IfStmt;
import cn.xiw.compiler.inter.IntLiteral;
import cn.xiw.compiler.inter.NullStmt;
import cn.xiw.compiler.inter.BuiltinType;
import cn.xiw.compiler.inter.StmtAst;
import cn.xiw.compiler.inter.Type;
import cn.xiw.compiler.inter.UnaryOp;
import cn.xiw.compiler.inter.VarDecl;
import cn.xiw.compiler.inter.WhileStmt;
import cn.xiw.compiler.lexer.Lexer;
import cn.xiw.compiler.lexer.Token;
import cn.xiw.compiler.lexer.TokenType;
import cn.xiw.compiler.symbols.Env;

public class Parser {
    private final Lexer lexer;
    private Token look; // look-forward token

    private Env top = null; // the top symbol table

    private StmtAst currentEnclosingStmt = null;

    private Set<TokenType> relOps;

    public Parser(Lexer lexer) throws IOException {
        this.lexer = lexer;
        relOps = new HashSet<>();
        relOps.add(TokenType.PUNCT_LT);
        relOps.add(TokenType.PUNCT_LE);
        relOps.add(TokenType.PUNCT_GT);
        relOps.add(TokenType.PUNCT_GE);
        move();
    }

    private void error(String msg) {
        throw new Error(msg);
    }

    // move to next
    private void move() throws IOException {
        look = lexer.scan();
    }

    private void eat(TokenType expectedTokenType) throws IOException {
        if (look.getType() != expectedTokenType) {
            error(String.format(
                    "Syntax error at line %d. Expected: %s; actual: %s",
                    look.getTokenLine(), expectedTokenType, look.getType()));
        }
    }

    // eat and move
    private void match(TokenType expectedTokenType) throws IOException {
        eat(expectedTokenType);
        move();
    }

    /**
     * program -> block EOF
     * 
     * @return
     * @throws IOException
     */
    public AstNode parse() throws IOException {
        var program = block();
        eat(TokenType.EOF);
        return program;
    }

    /**
     * block -> {stmts}
     * 
     * @throws IOException
     */
    private BlockStmt block() throws IOException {
        match(TokenType.PUNCT_L_BAR);
        Env savedEnv = top;
        top = new Env(top);
        var blockStmt = new BlockStmt();
        while (look.getType() != TokenType.PUNCT_R_BAR) {
            blockStmt.addStmt(stmt());
        }
        match(TokenType.PUNCT_R_BAR);
        top = savedEnv;
        return blockStmt;
    }

    /**
     * stmt
     * 
     * @return
     * @throws IOException
     */
    private StmtAst stmt() throws IOException {
        switch (look.getType()) {
        case PUNCT_SEMI: // stmt -> ;
            move();
            return NullStmt.instance();
        case KW_IF: // stmt -> if (expr) stmt else stmt
            match(TokenType.KW_IF);
            match(TokenType.PUNCT_L_PAR);
            var boolExpr = bool();
            match(TokenType.PUNCT_R_PAR);
            var ifStmt = stmt();
            StmtAst elseStmt = null;
            if (look.getType() == TokenType.KW_ELSE) {
                match(TokenType.KW_ELSE);
                elseStmt = stmt();
            }
            return new IfStmt(boolExpr, ifStmt, elseStmt);
        case KW_WHILE: // stmt -> while (expr) stmt
            match(TokenType.KW_WHILE);
            match(TokenType.PUNCT_L_PAR);
            boolExpr = bool();
            match(TokenType.PUNCT_R_PAR);
            var whileStmt = new WhileStmt(boolExpr);
            var oldEnclosing = currentEnclosingStmt;
            whileStmt.setStmt(stmt());
            currentEnclosingStmt = oldEnclosing;
            return whileStmt;
        case KW_BREAK: // stmt -> break;
            match(TokenType.KW_BREAK);
            match(TokenType.PUNCT_SEMI);
            return new BreakStmt(currentEnclosingStmt);
        case PUNCT_L_BAR: // stmt -> block
            return block();
        case IDENTIFIER: // stmt -> loc = bool;
            return assignment();
        default: // stmt -> type id;
            return declStmt();
        }
    }

    /**
     * decl -> type id;
     * 
     * @return
     * @throws IOException
     */
    private DeclStmt declStmt() throws IOException {
        Type type = type();

        var idTok = look;
        match(TokenType.IDENTIFIER);
        match(TokenType.PUNCT_SEMI);

        var id = new VarDecl(idTok.identifier(), type);
        // add to env
        top.addSymbol(idTok.identifier(), id);
        return new DeclStmt(id);
    }

    /**
     * type -> int | char | float | type[num]
     * 
     * @return
     * @throws IOException
     */
    private Type type() throws IOException {
        Type baseType = null;
        switch (look.getType()) {
        case KW_INT:
            baseType = BuiltinType.INT_TYPE;
            break;
        case KW_CHAR:
            baseType = BuiltinType.CHAR_TYPE;
            break;
        case KW_FLOAT:
            baseType = BuiltinType.FLOAT_TYPE;
            break;
        default:
            error("Invalid decl type");
        }
        move();
        if (look.getType() == TokenType.PUNCT_L_SQR) {
            match(TokenType.PUNCT_L_SQR);
            var numTok = look;
            match(TokenType.CONST_INT);
            match(TokenType.PUNCT_R_SQR);
            return new ArrayType(baseType, numTok.valueInt());
        }
        return baseType;
    }

    private StmtAst assignment() throws IOException {
        String id = look.identifier();
        match(TokenType.IDENTIFIER);
        if (top.getSymbol(id) == null) {
            error("Undeclared variable: " + id);
        }
        if (!(top.getSymbol(id) instanceof VarDecl)) {
            error("Wrong type: should be a variable");
        }
        // id = bool;
        var declRef = new DeclRefExpr((VarDecl) top.getSymbol(id));
        if (look.getType() == TokenType.PUNCT_EQ) {
            match(TokenType.PUNCT_EQ);
            var expr = bool();
            match(TokenType.PUNCT_SEMI);
            return new AssignStmt(declRef, expr);
        }
        // id[index] = bool;
        match(TokenType.PUNCT_L_SQR);
        var index = bool();
        match(TokenType.PUNCT_R_SQR);

        match(TokenType.PUNCT_EQ);
        var expr = bool();
        match(TokenType.PUNCT_SEMI);
        return new AssignElemStmt(new ElemAccessOp(declRef, index), expr);
    }

    /**
     * bool -> bool || join | join
     * 
     * @return
     * @throws IOException
     */
    private ExprAst bool() throws IOException {
        var boolExpr = join();
        while (look.getType() == TokenType.PUNCT_OR) {
            move();
            boolExpr = new BinaryOp(TokenType.PUNCT_OR, boolExpr, join());
        }
        return boolExpr;
    }

    /**
     * join -> join && equality | equality
     * 
     * @return
     * @throws IOException
     */
    private ExprAst join() throws IOException {
        var joinExpr = equality();
        while (look.getType() == TokenType.PUNCT_AND) {
            move();
            joinExpr = new BinaryOp(TokenType.PUNCT_AND, joinExpr, equality());
        }
        return joinExpr;
    }

    /**
     * equality -> equality [ == | != ] rel | rel
     * 
     * @return
     * @throws IOException
     */
    private ExprAst equality() throws IOException {
        var equExpr = rel();
        while (look.getType() == TokenType.PUNCT_EQEQ
                || look.getType() == TokenType.PUNCT_NOT_EQ) {
            var op = look.getType();
            move();
            equExpr = new BinaryOp(op, equExpr, rel());
        }
        return equExpr;
    }

    /**
     * rel -> expr [ < | <= | > | >= ] expr | expr
     * 
     * @return
     * @throws IOException
     */
    private ExprAst rel() throws IOException {
        var relExpr = expr();
        if (relOps.contains(look.getType())) {
            var op = look.getType();
            move();
            relExpr = new BinaryOp(op, relExpr, expr());
        }
        return relExpr;
    }

    /**
     * expr -> expr [ + | - ] term | term
     * 
     * @return
     * @throws IOException
     */
    private ExprAst expr() throws IOException {
        var expr = term();
        while (look.getType() == TokenType.PUNCT_PLUS
                || look.getType() == TokenType.PUNCT_MINUS) {
            var op = look.getType();
            move();
            expr = new BinaryOp(op, expr, term());
        }
        return expr;
    }

    /**
     * term -> term [ * | / ] unary | unary
     * 
     * @return
     * @throws IOException
     */
    private ExprAst term() throws IOException {
        var term = unary();
        while (look.getType() == TokenType.PUNCT_STAR
                || look.getType() == TokenType.PUNCT_DIV) {
            var op = look.getType();
            move();
            term = new BinaryOp(op, term, unary());
        }
        return term;
    }

    /**
     * unary -> [ ! | - ] unary | factor
     * 
     * @return
     * @throws IOException
     */
    private ExprAst unary() throws IOException {
        if (look.getType() == TokenType.PUNCT_NOT
                || look.getType() == TokenType.PUNCT_MINUS) {
            var op = look.getType();
            move();
            return new UnaryOp(op, unary());
        }
        return factor();
    }

    /**
     * fator -> ( bool ) | declRef | literal
     * 
     * @return
     * @throws IOException
     */
    private ExprAst factor() throws IOException {
        switch (look.getType()) {
        case PUNCT_L_PAR:
            var boolExpr = bool();
            match(TokenType.PUNCT_R_PAR);
            return boolExpr;
        case IDENTIFIER:
            var id = (VarDecl) top.getSymbol(look.identifier());
            if (id == null) {
                error("Undeclared variable");
            }
            match(TokenType.IDENTIFIER);
            var idRef = new DeclRefExpr(id);
            if (look.getType() == TokenType.PUNCT_L_SQR) {
                match(TokenType.PUNCT_L_SQR);
                var index = bool();
                var offsetExpr = new BinaryOp(TokenType.PUNCT_STAR, index,
                        new IntLiteral(
                                ((ArrayType) id.getType()).getOf().getWidth()));
                match(TokenType.PUNCT_R_SQR);
                return new ElemAccessOp(idRef, offsetExpr);
            }
            return idRef;
        case CONST_CHAR:
            var constChar = new CharLiteral(look.valueChar());
            match(TokenType.CONST_CHAR);
            return constChar;
        case CONST_FLOAT:
            var constFloat = new FloatLiteral(look.valueFloat());
            match(TokenType.CONST_FLOAT);
            return constFloat;
        case CONST_INT:
            var constInt = new IntLiteral(look.valueInt());
            match(TokenType.CONST_INT);
            return constInt;
        default:
            error("Syntax error");
            ;
        }
        return null;
    }
}

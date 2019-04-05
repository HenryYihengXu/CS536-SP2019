import java.io.*;
import java.util.*;

// **********************************************************************
// The ASTnode class defines the nodes of the abstract-syntax tree that
// represents a Carrot program.
//
// Internal nodes of the tree contain pointers to children, organized
// either in a list (for nodes that may have a variable number of 
// children) or as a fixed set of fields.
//
// The nodes for literals and ids contain line and character number
// information; for string literals and identifiers, they also contain a
// string; for integer literals, they also contain an integer value.
//
// Here are all the different kinds of AST nodes and what kinds of children
// they have.  All of these kinds of AST nodes are subclasses of "ASTnode".
// Indentation indicates further subclassing:
//
//     Subclass            Kids
//     --------            ----
//     ProgramNode         DeclListNode
//     DeclListNode        linked list of DeclNode
//     DeclNode:
//       VarDeclNode       TypeNode, IdNode, int
//       FnDeclNode        TypeNode, IdNode, FormalsListNode, FnBodyNode
//       FormalDeclNode    TypeNode, IdNode
//       StructDeclNode    IdNode, DeclListNode
//
//     FormalsListNode     linked list of FormalDeclNode
//     FnBodyNode          DeclListNode, StmtListNode
//     StmtListNode        linked list of StmtNode
//     ExpListNode         linked list of ExpNode
//
//     TypeNode:
//       IntNode           -- none --
//       BoolNode          -- none --
//       VoidNode          -- none --
//       StructNode        IdNode
//
//     StmtNode:
//       AssignStmtNode      AssignNode
//       PostIncStmtNode     ExpNode
//       PostDecStmtNode     ExpNode
//       ReadStmtNode        ExpNode
//       WriteStmtNode       ExpNode
//       IfStmtNode          ExpNode, DeclListNode, StmtListNode
//       IfElseStmtNode      ExpNode, DeclListNode, StmtListNode,
//                                    DeclListNode, StmtListNode
//       WhileStmtNode       ExpNode, DeclListNode, StmtListNode
//       RepeatStmtNode      ExpNode, DeclListNode, StmtListNode
//       CallStmtNode        CallExpNode
//       ReturnStmtNode      ExpNode
//
//     ExpNode:
//       IntLitNode          -- none --
//       StrLitNode          -- none --
//       TrueNode            -- none --
//       FalseNode           -- none --
//       IdNode              -- none --
//       DotAccessNode       ExpNode, IdNode
//       AssignNode          ExpNode, ExpNode
//       CallExpNode         IdNode, ExpListNode
//       UnaryExpNode        ExpNode
//         UnaryMinusNode
//         NotNode
//       BinaryExpNode       ExpNode ExpNode
//         PlusNode     
//         MinusNode
//         TimesNode
//         DivideNode
//         AndNode
//         OrNode
//         EqualsNode
//         NotEqualsNode
//         LessNode
//         GreaterNode
//         LessEqNode
//         GreaterEqNode
//
// Here are the different kinds of AST nodes again, organized according to
// whether they are leaves, internal nodes with linked lists of kids, or
// internal nodes with a fixed number of kids:
//
// (1) Leaf nodes:
//        IntNode,   BoolNode,  VoidNode,  IntLitNode,  StrLitNode,
//        TrueNode,  FalseNode, IdNode
//
// (2) Internal nodes with (possibly empty) linked lists of children:
//        DeclListNode, FormalsListNode, StmtListNode, ExpListNode
//
// (3) Internal nodes with fixed numbers of kids:
//        ProgramNode,     VarDeclNode,     FnDeclNode,     FormalDeclNode,
//        StructDeclNode,  FnBodyNode,      StructNode,     AssignStmtNode,
//        PostIncStmtNode, PostDecStmtNode, ReadStmtNode,   WriteStmtNode   
//        IfStmtNode,      IfElseStmtNode,  WhileStmtNode,  RepeatStmtNode,
//        CallStmtNode
//        ReturnStmtNode,  DotAccessNode,   AssignExpNode,  CallExpNode,
//        UnaryExpNode,    BinaryExpNode,   UnaryMinusNode, NotNode,
//        PlusNode,        MinusNode,       TimesNode,      DivideNode,
//        AndNode,         OrNode,          EqualsNode,     NotEqualsNode,
//        LessNode,        GreaterNode,     LessEqNode,     GreaterEqNode
//
// **********************************************************************

// **********************************************************************
// %%%ASTnode class (base class for all other kinds of nodes)
// **********************************************************************

abstract class ASTnode { 
    // every subclass must provide an unparse operation
    abstract public void unparse(PrintWriter p, int indent);

    // this method can be used by the unparse methods to do indenting
    protected void addIndent(PrintWriter p, int indent) {
        for (int k = 0; k < indent; k++) p.print(" ");
    }
}

// **********************************************************************
// ProgramNode,  DeclListNode, FormalsListNode, FnBodyNode,
// StmtListNode, ExpListNode
// **********************************************************************

class ProgramNode extends ASTnode {
    public ProgramNode(DeclListNode L) {
        myDeclList = L;
    }

    public void unparse(PrintWriter p, int indent) {
        myDeclList.unparse(p, indent);
    }

    public void nameAnalyze(SymTable table)
            throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        myDeclList.nameAnalyze(table);
    }

    // 1 kid
    private DeclListNode myDeclList;
}

class DeclListNode extends ASTnode {
    public DeclListNode(List<DeclNode> S) {
        myDecls = S;
    }

    public List<DeclNode> getMyDecls() {
        return myDecls;
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator it = myDecls.iterator();
        try {
            while (it.hasNext()) {
                ((DeclNode)it.next()).unparse(p, indent);
            }
        } catch (NoSuchElementException ex) {
            System.err.println("unexpected NoSuchElementException in DeclListNode.print");
            System.exit(-1);
        }
    }

    public void nameAnalyze(SymTable table)
            throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        Iterator it = myDecls.iterator();
        try {
            while (it.hasNext()) {
                ((DeclNode)it.next()).nameAnalyze(table);
            }
        } catch (NoSuchElementException ex) {
            System.err.println("unexpected NoSuchElementException in DeclListNode.print");
            System.exit(-1);
        }
    }

    // list of kids (DeclNodes)
    private List<DeclNode> myDecls;
}

class FormalsListNode extends ASTnode {
    public FormalsListNode(List<FormalDeclNode> S) {
        myFormals = S;
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<FormalDeclNode> it = myFormals.iterator();
        if (it.hasNext()) { // if there is at least one element
            it.next().unparse(p, indent);
            while (it.hasNext()) {  // print the rest of the list
                p.print(", ");
                it.next().unparse(p, indent);
            }
        } 
    }

    public void nameAnalyze(SymTable table)
            throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        //table.addScope();
        Iterator<FormalDeclNode> it = myFormals.iterator();

        while (it.hasNext()) {  // print the rest of the list
            it.next().nameAnalyze(table);
        }

    }

    public List<FormalDeclNode> getMyFormals() {
        return myFormals;
    }

    // list of kids (FormalDeclNodes)
    private List<FormalDeclNode> myFormals;
}

class FnBodyNode extends ASTnode {
    public FnBodyNode(DeclListNode declList, StmtListNode stmtList) {
        myDeclList = declList;
        myStmtList = stmtList;
    }

    public void unparse(PrintWriter p, int indent) {
        myDeclList.unparse(p, indent);
        myStmtList.unparse(p, indent);
    }

    public void nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        myDeclList.nameAnalyze(table);
        myStmtList.nameAnalyze(table);
    }

    // 2 kids
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class StmtListNode extends ASTnode {
    public StmtListNode(List<StmtNode> S) {
        myStmts = S;
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<StmtNode> it = myStmts.iterator();
        while (it.hasNext()) {
            it.next().unparse(p, indent);
        }
    }

    public void nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        Iterator<StmtNode> it = myStmts.iterator();
        while (it.hasNext()) {
            it.next().nameAnalyze(table);
        }
    }

    // list of kids (StmtNodes)
    private List<StmtNode> myStmts;
}

class ExpListNode extends ASTnode {
    public ExpListNode(List<ExpNode> S) {
        myExps = S;
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<ExpNode> it = myExps.iterator();
        if (it.hasNext()) { // if there is at least one element
            it.next().unparse(p, indent);
            while (it.hasNext()) {  // print the rest of the list
                p.print(", ");
                it.next().unparse(p, indent);
            }
        } 
    }

    public void nameAnalyze(SymTable table)
            throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        Iterator<ExpNode> it = myExps.iterator();
        while (it.hasNext()) {
            it.next().nameAnalyze(table);
        }
    }

    // list of kids (ExpNodes)
    private List<ExpNode> myExps;
}

// **********************************************************************
// DeclNode and its subclasses
// **********************************************************************

abstract class DeclNode extends ASTnode {
    abstract public void nameAnalyze(SymTable table)
            throws DuplicateSymException, EmptySymTableException, WrongArgumentException;
}

class VarDeclNode extends DeclNode {
    public VarDeclNode(TypeNode type, IdNode id, int size) {
        myType = type;
        myId = id;
        mySize = size;
    }

    public String getType() {
        return myType.toString();
    }

    public TypeNode getMyType() {
        return myType;
    }

    public String getName() {
        return myId.getMyStrVal();
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        myType.unparse(p, 0);
        p.print(" ");
        myId.unparse(p, 0);
        p.println(";");
    }

    @Override
    public void nameAnalyze(SymTable table)
            throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        if (myType.toString().equals("void")) {
            ErrMsg.fatal(myId.getMyLineNum(), myId.getMyCharNum(), "Non-function declared void");
            table.setError(true);
            return;
        }
        if (table.lookupLocal(myId.getMyStrVal()) != null) {
            if (!table.lookupLocal(myId.getMyStrVal()).getKind().equals("formal")) {
                ErrMsg.fatal(myId.getMyLineNum(), myId.getMyCharNum(), "Multiply declared identifier");
                table.setError(true);
                return;
            } else {
                return;
            }
        }
        if (myType.toString().equals("struct")) {
            Sym ss = table.lookupGlobal(((StructNode)myType).getMyId().getMyStrVal());
            if (ss == null) {
                ErrMsg.fatal(((StructNode)myType).getMyId().getMyLineNum(),
                        ((StructNode)myType).getMyId().getMyCharNum(),
                        "Undeclared identifier");
                table.setError(true);
                return;
            } else if (!ss.getKind().equals("structDecl")) {
                ErrMsg.fatal(((StructNode)myType).getMyId().getMyLineNum(),
                        ((StructNode)myType).getMyId().getMyCharNum(),
                        "Invalid name of struct type");
                table.setError(true);
                return;
            } else {
                Sym s = new VarSym(((StructNode)myType).getMyId().getMyStrVal(), "struct");
                table.addDecl(myId.getMyStrVal(), s);
            }
        } else {
            Sym s = new VarSym(myType.toString(), "var");
            table.addDecl(myId.getMyStrVal(), s);
        }
    }

    // 3 kids
    private TypeNode myType;
    private IdNode myId;
    private int mySize;  // use value NOT_STRUCT if this is not a struct type

    public static int NOT_STRUCT = -1;
}

class FnDeclNode extends DeclNode {
    public FnDeclNode(TypeNode type,
                      IdNode id,
                      FormalsListNode formalList,
                      FnBodyNode body) {
        myType = type;
        myId = id;
        myFormalsList = formalList;
        myBody = body;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        myType.unparse(p, 0);
        p.print(" ");
        myId.unparse(p, 0);
        p.print("(");
        myFormalsList.unparse(p, 0);
        p.println(") {");
        myBody.unparse(p, indent+4);
        p.println("}\n");
    }

    @Override
    public void nameAnalyze(SymTable table)
            throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        FnSym s = new FnSym(myType.toString(), "func");
        if (table.lookupLocal(myId.getMyStrVal()) != null) {
            ErrMsg.fatal(myId.getMyLineNum(), myId.getMyCharNum(), "Multiply declared identifier");
            table.setError(true);
        } else {
            table.addDecl(myId.getMyStrVal(), s);
        }
        table.addScope();
        Iterator<FormalDeclNode> it = myFormalsList.getMyFormals().iterator();
        while (it.hasNext()) {  // print the rest of the list
            FormalDeclNode n = it.next();
            s.addFormal(n.getName(), n.getType());
        }
        myFormalsList.nameAnalyze(table);
        myBody.nameAnalyze(table);
        table.removeScope();
    }

    // 4 kids
    private TypeNode myType;
    private IdNode myId;
    private FormalsListNode myFormalsList;
    private FnBodyNode myBody;
}

class FormalDeclNode extends DeclNode {
    public FormalDeclNode(TypeNode type, IdNode id) {
        myType = type;
        myId = id;
    }

    public void unparse(PrintWriter p, int indent) {
        myType.unparse(p, 0);
        p.print(" ");
        myId.unparse(p, 0);
    }

    public String getType() {
        return myType.toString();
    }

    public String getName() {
        return myId.getMyStrVal();
    }

    public void nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        if (myType.toString().equals("void")) {
            ErrMsg.fatal(myId.getMyLineNum(), myId.getMyCharNum(), "Non-function declared void");
            table.setError(true);
            return;
        }
        if (table.lookupLocal(myId.getMyStrVal()) != null) {
            ErrMsg.fatal(myId.getMyLineNum(), myId.getMyCharNum(), "Multiply declared identifier");
            table.setError(true);
            return;
        }
        Sym s = new VarSym(myType.toString(), "formal");
        table.addDecl(myId.getMyStrVal(), s);
        //((FnSym)sym).addFormal(myId.getMyStrVal(), myType.toString());
    }

    // 2 kids
    private TypeNode myType;
    private IdNode myId;
}

class StructDeclNode extends DeclNode {
    public StructDeclNode(IdNode id, DeclListNode declList) {
        myId = id;
        myDeclList = declList;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        p.print("struct ");
        myId.unparse(p, 0);
        p.println("{");
        myDeclList.unparse(p, indent+4);
        addIndent(p, indent);
        p.println("};\n");

    }

    public void nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        if (table.lookupLocal(myId.getMyStrVal()) != null) {
            ErrMsg.fatal(myId.getMyLineNum(), myId.getMyCharNum(), "Multiply declared identifier");
            table.setError(true);
            return;
        }
        StructDeclSym s = new StructDeclSym(myId.getMyStrVal(), "structDecl");
        table.addDecl(myId.getMyStrVal(), s);
        table.addScope();
        Iterator<DeclNode> it = myDeclList.getMyDecls().iterator();
        while (it.hasNext()) {
            DeclNode n = it.next();
            if (((VarDeclNode)n).getType().equals("struct")) {
                s.addDecl(((VarDeclNode)n).getName(), new VarSym(((StructNode)(((VarDeclNode)n).getMyType())).getMyId().getMyStrVal(), "struct"));
            } else if (((VarDeclNode)n).getType().equals("void")){
            } else {
                s.addDecl(((VarDeclNode)n).getName(), new VarSym(((VarDeclNode)n).getType(), "var"));
            }
        }
        myDeclList.nameAnalyze(table);
        table.removeScope();
    }

    // 2 kids
    private IdNode myId;
    private DeclListNode myDeclList;
}

// **********************************************************************
// TypeNode and its Subclasses
// **********************************************************************

abstract class TypeNode extends ASTnode {
}

class IntNode extends TypeNode {
    public IntNode() {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("int");
    }

    public String toString() {
        return "int";
    }
}

class BoolNode extends TypeNode {
    public BoolNode() {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("bool");
    }

    public String toString() {
        return "bool";
    }
}

class VoidNode extends TypeNode {
    public VoidNode() {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("void");
    }

    public String toString() {
        return "void";
    }
}

class StructNode extends TypeNode {
    public StructNode(IdNode id) {
        myId = id;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("struct ");
        myId.unparse(p, 0);
    }

    public String toString() {
        return "struct";
    }

    public IdNode getMyId() {
        return myId;
    }

    // 1 kid
    private IdNode myId;
}

// **********************************************************************
// StmtNode and its subclasses
// **********************************************************************

abstract class StmtNode extends ASTnode {
    abstract public void nameAnalyze(SymTable table)
            throws DuplicateSymException, EmptySymTableException, WrongArgumentException;
}

class AssignStmtNode extends StmtNode {
    public AssignStmtNode(AssignNode assign) {
        myAssign = assign;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        myAssign.unparse(p, -1); // no parentheses
        p.println(";");
    }

    @Override
    public void nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        myAssign.nameAnalyze(table);
    }

    // 1 kid
    private AssignNode myAssign;
}

class PostIncStmtNode extends StmtNode {
    public PostIncStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        myExp.unparse(p, 0);
        p.println("++;");
    }

    @Override
    public void nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        myExp.nameAnalyze(table);
    }

    // 1 kid
    private ExpNode myExp;
}

class PostDecStmtNode extends StmtNode {
    public PostDecStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        myExp.unparse(p, 0);
        p.println("--;");
    }

    @Override
    public void nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        myExp.nameAnalyze(table);
    }

    // 1 kid
    private ExpNode myExp;
}

class ReadStmtNode extends StmtNode {
    public ReadStmtNode(ExpNode e) {
        myExp = e;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        p.print("cin >> ");
        myExp.unparse(p, 0);
        p.println(";");
    }

    @Override
    public void nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        myExp.nameAnalyze(table);
    }

    // 1 kid (actually can only be an IdNode or an ArrayExpNode)
    private ExpNode myExp;
}

class WriteStmtNode extends StmtNode {
    public WriteStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        p.print("cout << ");
        myExp.unparse(p, 0);
        p.println(";");
    }

    @Override
    public void nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        myExp.nameAnalyze(table);
    }

    // 1 kid
    private ExpNode myExp;
}

class IfStmtNode extends StmtNode {
    public IfStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myDeclList = dlist;
        myExp = exp;
        myStmtList = slist;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        p.print("if (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent+4);
        myStmtList.unparse(p, indent+4);
        addIndent(p, indent);
        p.println("}");
    }

    @Override
    public void nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        myExp.nameAnalyze(table);
        table.addScope();
        myDeclList.nameAnalyze(table);
        myStmtList.nameAnalyze(table);
        table.removeScope();
    }

    // e kids
    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class IfElseStmtNode extends StmtNode {
    public IfElseStmtNode(ExpNode exp, DeclListNode dlist1,
                          StmtListNode slist1, DeclListNode dlist2,
                          StmtListNode slist2) {
        myExp = exp;
        myThenDeclList = dlist1;
        myThenStmtList = slist1;
        myElseDeclList = dlist2;
        myElseStmtList = slist2;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        p.print("if (");
        myExp.unparse(p, 0);
        p.println(") {");
        myThenDeclList.unparse(p, indent+4);
        myThenStmtList.unparse(p, indent+4);
        addIndent(p, indent);
        p.println("}");
        addIndent(p, indent);
        p.println("else {");
        myElseDeclList.unparse(p, indent+4);
        myElseStmtList.unparse(p, indent+4);
        addIndent(p, indent);
        p.println("}");        
    }

    @Override
    public void nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        myExp.nameAnalyze(table);
        table.addScope();
        myThenDeclList.nameAnalyze(table);
        myThenStmtList.nameAnalyze(table);
        table.removeScope();
        table.addScope();
        myElseDeclList.nameAnalyze(table);
        myElseStmtList.nameAnalyze(table);
        table.removeScope();
    }

    // 5 kids
    private ExpNode myExp;
    private DeclListNode myThenDeclList;
    private StmtListNode myThenStmtList;
    private StmtListNode myElseStmtList;
    private DeclListNode myElseDeclList;
}

class WhileStmtNode extends StmtNode {
    public WhileStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myExp = exp;
        myDeclList = dlist;
        myStmtList = slist;
    }
    
    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        p.print("while (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent+4);
        myStmtList.unparse(p, indent+4);
        addIndent(p, indent);
        p.println("}");
    }

    @Override
    public void nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        myExp.nameAnalyze(table);
        table.addScope();
        myDeclList.nameAnalyze(table);
        myStmtList.nameAnalyze(table);
        table.removeScope();
    }

    // 3 kids
    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class RepeatStmtNode extends StmtNode {
    public RepeatStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myExp = exp;
        myDeclList = dlist;
        myStmtList = slist;
    }
	
    public void unparse(PrintWriter p, int indent) {
	addIndent(p, indent);
        p.print("repeat (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent+4);
        myStmtList.unparse(p, indent+4);
        addIndent(p, indent);
        p.println("}");
    }

    @Override
    public void nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        myExp.nameAnalyze(table);
        table.addScope();
        myDeclList.nameAnalyze(table);
        myStmtList.nameAnalyze(table);
        table.removeScope();
    }

    // 3 kids
    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class CallStmtNode extends StmtNode {
    public CallStmtNode(CallExpNode call) {
        myCall = call;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        myCall.unparse(p, indent);
        p.println(";");
    }

    @Override
    public void nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        myCall.nameAnalyze(table);
    }

    // 1 kid
    private CallExpNode myCall;
}

class ReturnStmtNode extends StmtNode {
    public ReturnStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndent(p, indent);
        p.print("return");
        if (myExp != null) {
            p.print(" ");
            myExp.unparse(p, 0);
        }
        p.println(";");
    }

    @Override
    public void nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        if (myExp != null) {
            myExp.nameAnalyze(table);
        }
    }

    // 1 kid
    private ExpNode myExp; // possibly null
}

// **********************************************************************
// ExpNode and its subclasses
// **********************************************************************

abstract class ExpNode extends ASTnode {
    abstract public Sym nameAnalyze(SymTable table)
            throws DuplicateSymException, EmptySymTableException, WrongArgumentException;
}

class IntLitNode extends ExpNode {
    public IntLitNode(int lineNum, int charNum, int intVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myIntVal = intVal;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(myIntVal);
    }

    @Override
    public Sym nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        return null;
    }

    private int myLineNum;
    private int myCharNum;
    private int myIntVal;
}

class StringLitNode extends ExpNode {
    public StringLitNode(int lineNum, int charNum, String strVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myStrVal = strVal;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(myStrVal);
    }

    @Override
    public Sym nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        return null;
    }

    private int myLineNum;
    private int myCharNum;
    private String myStrVal;
}

class TrueNode extends ExpNode {
    public TrueNode(int lineNum, int charNum) {
        myLineNum = lineNum;
        myCharNum = charNum;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("true");
    }

    @Override
    public Sym nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        return null;
    }

    private int myLineNum;
    private int myCharNum;
}

class FalseNode extends ExpNode {
    public FalseNode(int lineNum, int charNum) {
        myLineNum = lineNum;
        myCharNum = charNum;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("false");
    }

    @Override
    public Sym nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        return null;
    }

    private int myLineNum;
    private int myCharNum;
}

class IdNode extends ExpNode {
    public IdNode(int lineNum, int charNum, String strVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myStrVal = strVal;
    }

    public void unparse(PrintWriter p, int indent) {

        p.print(myStrVal);
        if (mySym != null) {
            p.print("(");
            if (mySym.getKind().equals("func")) {
                Iterator<String> it = ((FnSym)mySym).getFormalList().iterator();
                if (it.hasNext()) {
                    p.print(it.next());
                    while (it.hasNext()) {
                        p.print(", ");
                        p.print(it.next());
                    }
                    p.print(" -> ");
                } else {
                    p.print("-> ");
                }
            }
            p.print(mySym.getType());
            p.print(")");
        }

    }

    @Override
    public Sym nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {

        /*if (sym != null) {
            //if (sym.getKind().equals("structDecl")) {
            Sym s = ((StructDeclSym)sym).lookupGlobal(myStrVal);
            if (s == null) {
                ErrMsg.fatal(myLineNum, myCharNum, "Invalid struct field name");
                return null;
            }
            setMySym(s);
            if (s.getKind().equals("struct")) {
                return (StructDeclSym)s;
            } else {
                Sym ss = new Sym(" ", "var");
                ss.setMyLineNum(myLineNum);
                ss.setMyCharNum(myCharNum);
                return ss;
            }
            } else {
                ErrMsg.fatal(sym.getMyLineNum(), sym.getMyCharNum(), "Dot-access of non-struct type");
                Sym ss = new Sym(" ", " ");
                ss.setMyLineNum(myLineNum);
                ss.setMyCharNum(myCharNum);
                return ss;
            }

            //return null;
        } else {*/
            Sym s = table.lookupGlobal(myStrVal);
            if (s == null) {
                ErrMsg.fatal(myLineNum, myCharNum, "Undeclared identifier");
                table.setError(true);
                return null;
            }
            setMySym(s);
            if (s.getKind().equals("struct")) {
                return s;
            } else {
                Sym ss = new Sym(" ", "var");
                ss.setMyLineNum(myLineNum);
                ss.setMyCharNum(myCharNum);
                return ss;
            }
        //}
    }

    public int getMyLineNum() {
        return myLineNum;
    }

    public int getMyCharNum() {
        return myCharNum;
    }

    public String getMyStrVal() {
        return myStrVal;
    }

    public Sym getMySym() {
        return mySym;
    }

    public void setMySym(Sym mySym) {
        this.mySym = mySym;
    }

    private int myLineNum;
    private int myCharNum;
    private String myStrVal;
    private Sym mySym;
}

class DotAccessExpNode extends ExpNode {
    public DotAccessExpNode(ExpNode loc, IdNode id) {
        myLoc = loc;    
        myId = id;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myLoc.unparse(p, 0);
        p.print(").");
        myId.unparse(p, 0);
    }

    @Override
    public Sym nameAnalyze(SymTable table)
            throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        Sym s = myLoc.nameAnalyze(table);
        if (s == null) {
            return null;
        } else if (s.getKind() != "struct") {
            ErrMsg.fatal(s.getMyLineNum(), s.getMyCharNum(), "Dot-access of non-struct type");
            table.setError(true);
            return null;
        } else {
            //if (sym.getKind().equals("structDecl")) {
            //System.out.println(s.getType());
            Sym sym = ((StructDeclSym)(table.lookupGlobal(s.getType()))).lookupGlobal(myId.getMyStrVal());
            //((StructDeclSym)(table.lookupGlobal(s.getType()))).getMyTable().print();
            if (sym == null) {
                ErrMsg.fatal(myId.getMyLineNum(), myId.getMyCharNum(), "Invalid struct field name");
                table.setError(true);
                return null;
            }
            myId.setMySym(sym);
            if (sym.getKind().equals("struct")) {
                return sym;
            } else {
                Sym ss = new Sym(" ", "var");
                ss.setMyLineNum(myId.getMyLineNum());
                ss.setMyCharNum(myId.getMyCharNum());
                return ss;
            }
            /*} else {
                ErrMsg.fatal(sym.getMyLineNum(), sym.getMyCharNum(), "Dot-access of non-struct type");
                Sym ss = new Sym(" ", " ");
                ss.setMyLineNum(myLineNum);
                ss.setMyCharNum(myCharNum);
                return ss;
            }*/

            //return null;
        }
        //return myId.nameAnalyze(table, s);
    }


    // 2 kids
    private ExpNode myLoc;
    private IdNode myId;
}

class AssignNode extends ExpNode {
    public AssignNode(ExpNode lhs, ExpNode exp) {
        myLhs = lhs;
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        if (indent != -1)  p.print("(");
        myLhs.unparse(p, 0);
        p.print(" = ");
        myExp.unparse(p, 0);
        if (indent != -1)  p.print(")");
    }

    @Override
    public Sym nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        myLhs.nameAnalyze(table);
        myExp.nameAnalyze(table);
        return null;
    }

    // 2 kids
    private ExpNode myLhs;
    private ExpNode myExp;
}

class CallExpNode extends ExpNode {
    public CallExpNode(IdNode name, ExpListNode elist) {
        myId = name;
        myExpList = elist;
    }

    public CallExpNode(IdNode name) {
        myId = name;
        myExpList = new ExpListNode(new LinkedList<ExpNode>());
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
        myId.unparse(p, 0);
        p.print("(");
        if (myExpList != null) {
            myExpList.unparse(p, 0);
        }
        p.print(")");
    }

    @Override
    public Sym nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        myId.nameAnalyze(table);
        if (myExpList != null) {
            myExpList.nameAnalyze(table);
        }
        return null;
    }

    // 2 kids
    private IdNode myId;
    private ExpListNode myExpList;  // possibly null
}

abstract class UnaryExpNode extends ExpNode {
    public UnaryExpNode(ExpNode exp) {
        myExp = exp;
    }

    @Override
    public Sym nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        myExp.nameAnalyze(table);
        return null;
    }

    // one child
    protected ExpNode myExp;
}

abstract class BinaryExpNode extends ExpNode {
    public BinaryExpNode(ExpNode exp1, ExpNode exp2) {
        myExp1 = exp1;
        myExp2 = exp2;
    }

    @Override
    public Sym nameAnalyze(SymTable table) throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        myExp1.nameAnalyze(table);
        myExp2.nameAnalyze(table);
        return null;
    }

    // two kids
    protected ExpNode myExp1;
    protected ExpNode myExp2;
}

// **********************************************************************
// Subclasses of UnaryExpNode
// **********************************************************************

class UnaryMinusNode extends UnaryExpNode {
    public UnaryMinusNode(ExpNode exp) {
        super(exp);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(-");
        myExp.unparse(p, 0);
        p.print(")");
    }
}

class NotNode extends UnaryExpNode {
    public NotNode(ExpNode exp) {
        super(exp);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(!");
        myExp.unparse(p, 0);
        p.print(")");
    }
}

// **********************************************************************
// Subclasses of BinaryExpNode
// **********************************************************************

class PlusNode extends BinaryExpNode {
    public PlusNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" + ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class MinusNode extends BinaryExpNode {
    public MinusNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" - ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class TimesNode extends BinaryExpNode {
    public TimesNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" * ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class DivideNode extends BinaryExpNode {
    public DivideNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" / ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class AndNode extends BinaryExpNode {
    public AndNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" && ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class OrNode extends BinaryExpNode {
    public OrNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" || ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class EqualsNode extends BinaryExpNode {
    public EqualsNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" == ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class NotEqualsNode extends BinaryExpNode {
    public NotEqualsNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" != ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class LessNode extends BinaryExpNode {
    public LessNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" < ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class GreaterNode extends BinaryExpNode {
    public GreaterNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" > ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class LessEqNode extends BinaryExpNode {
    public LessEqNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" <= ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class GreaterEqNode extends BinaryExpNode {
    public GreaterEqNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" >= ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

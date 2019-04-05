import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class Sym {

    private String type;
    private String kind;
    private int myLineNum;
    private int myCharNum;

    public int getMyLineNum() {
        return myLineNum;
    }

    public int getMyCharNum() {
        return myCharNum;
    }

    public void setMyLineNum(int myLineNum) {
        this.myLineNum = myLineNum;
    }

    public void setMyCharNum(int myCharNum) {
        this.myCharNum = myCharNum;
    }
    public Sym(String type, String kind) {
        this.type = type;
        this.kind = kind;
    }

    public String getKind() {
        return kind;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return type;
    }
}

class VarSym extends Sym {
    public VarSym(String type, String kind) {
        super(type, kind);
    }
}

class FnSym extends Sym {
    private LinkedList<String> formalList;
    private LinkedList<String> helpList;

    public FnSym(String type, String kind) {
        super(type, kind);
        formalList = new LinkedList<>();
        helpList = new LinkedList<>();
    }

    public void addFormal(String name, String type) {
        if (!helpList.contains(name)) {
            formalList.addLast(type);
            helpList.addLast(name);
        }
    }

    public LinkedList<String> getFormalList() {
        return formalList;
    }

    public LinkedList<String> getHelpList() {
        return helpList;
    }
}

class StructDeclSym extends Sym {
    private SymTable myTable;


    public void addDecl(String name, Sym sym)
            throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        if (myTable.lookupLocal(name) == null) {
            myTable.addDecl(name, sym);
        }
    }

    public StructDeclSym(String type, String kind) {
        super("struct", "structDecl");
        myTable = new SymTable();
    }

    public Sym lookupLocal(String name) {
        return myTable.lookupLocal(name);
    }

    public Sym lookupGlobal(String name) {
        return myTable.lookupGlobal(name);
    }

    public SymTable getMyTable() {
        return myTable;
    }
}


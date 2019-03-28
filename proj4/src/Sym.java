import java.util.HashMap;

public class Sym {

    private String type;
    private String kind;
    
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
    private HashMap<String, String> formalList;

    public FnSym(String type, String kind) {
        super(type, kind);
        formalList = new HashMap<String, String>();
    }

    public void addFormal(String name, String type) {
        formalList.put(name, type);
    }

}

class StructDeclSym extends Sym {
    private SymTable myTable;

    public void addDecl(String name, Sym sym)
            throws DuplicateSymException, EmptySymTableException, WrongArgumentException {
        myTable.addDecl(name, sym);
    }

    public StructDeclSym(String type, String kind) {
        super("struct", "structDecl");
        myTable = new SymTable();
    }
}

class StructSym extends Sym {

}

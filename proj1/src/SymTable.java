import java.util.HashMap;
import java.util.LinkedList;

/**
 * SymTable
 *
 * A table that contains all symbols seen in the program.
 * Represented by a List of HashMaps which map a string to a sym,
 * i.e. map a name to a type.
 *  @author Henry Xu
 */

public class SymTable {

    private LinkedList<HashMap<String, Sym>> table;

    public SymTable() {
        table = new LinkedList<>();
        table.addFirst(new HashMap<>());
    }


    public void addDecl(String idName, Sym sym)
            throws DuplicateSymException, EmptySymTableException,
            WrongArgumentException {

        if (table.isEmpty()) {
            throw new EmptySymTableException();
        }
        if (idName == null && sym == null) {
            throw new WrongArgumentException("Id name and sym are null.");
        }
        if (idName == null) {
            throw new WrongArgumentException("Id name is null.");
        }
        if (sym == null) {
            throw new WrongArgumentException("Sym is null.");
        }

        HashMap<String, Sym> currentScope = table.getFirst();
        if (currentScope.containsKey(idName)) {
            throw new DuplicateSymException();
        } else {
            currentScope.put(idName, sym);
        }
    }

    public void addScope() {
        table.addFirst(new HashMap<>());
    }

    public Sym lookupLocal(String idName) throws EmptySymTableException {
        if (table.isEmpty()) {
            throw new EmptySymTableException();
        }

        HashMap<String, Sym> currentScope = table.getFirst();
        return currentScope.get(idName);
    }

    public Sym lookupGlobal(String idName) throws EmptySymTableException {
        if (table.isEmpty()) {
            throw new EmptySymTableException();
        }

        for (HashMap<String, Sym> scope : table) {
            if (scope.containsKey(idName)) {
                return scope.get(idName);
            }
        }
        return null;
    }

    public void removeScope() throws EmptySymTableException {
        if (table.isEmpty()) {
            throw new EmptySymTableException();
        } else {
            table.removeFirst();
        }
    }

    public void print() {
        String s = "\n=== Sym Table ===\n";
        for (HashMap<String, Sym> scope : table) {
            s = s + scope.toString() + "\n";
        }
        s += "\n";
        System.out.print(s);
    }
}

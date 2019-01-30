import java.util.HashMap;
import java.util.LinkedList;

/**
 * SymTable
 *
 * A table that contains all symbols seen in the program.
 * Represented by a List of HashMaps which map a string to a sym,
 * i.e. map a name to a type.
 *
 *  @author Henry Xu
 */

public class SymTable {

    /** The LinkedList consists of HashMaps.
     *  Each HashMap represents a scope.
     *  Each element represents a declaration. */
    private LinkedList<HashMap<String, Sym>> table;

    /** Constructor of SymTable.
     *  Construct a SymTable with a LinkedList
     *  which contains one empty HashMap.*/
    public SymTable() {
        table = new LinkedList<>();
        table.addFirst(new HashMap<>());
    }

    /** Add a declaration to the first scope.
     *  @param idName the name of the declaration.
     *  @param sym type of the declaration. */
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

    /** Add a HashMap to the front of the list. */
    public void addScope() {
        table.addFirst(new HashMap<>());
    }

    /** Look up a declaration in the first scope.
     *  @param idName the name of the declaration you are to look up.
     *  @return the type of the found declaration. Or null if not found. */
    public Sym lookupLocal(String idName) throws EmptySymTableException {
        if (table.isEmpty()) {
            throw new EmptySymTableException();
        }

        HashMap<String, Sym> currentScope = table.getFirst();
        return currentScope.get(idName);
    }

    /** Look up a declaration in all scopes.
     *  Return the one whose scope is the closest
     *  one to the front of the list.
     *  @param idName the name of the declaration you are to look up.
     *  @return the type of the found declaration. Or null if not found. */
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

    /** Remove a scope from the front of the list. */
    public void removeScope() throws EmptySymTableException {
        if (table.isEmpty()) {
            throw new EmptySymTableException();
        } else {
            table.removeFirst();
        }
    }

    /** For debugging.
     *  Print the whole SymTable scope by scope from the front.*/
    public void print() {
        String s = "\n=== Sym Table ===\n";
        for (HashMap<String, Sym> scope : table) {
            s = s + scope.toString() + "\n";
        }
        s += "\n";
        System.out.print(s);
    }
}

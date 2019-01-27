/**
 * P1
 *
 * This is a class whose sole purpose is to test the SymTable class, which
 * provides the following operations:
 *       no-arg constructor
 *          -- create a linked list with an empty hash map in it.
 *       void addDecl(String idName, Sym sym)
 *          throws DuplicateSymException, EmptySymTableException,
 *          WrongArgumentException
 *          -- If this SymTable's list is empty, throw an
 *             EmptySymTableException. If either idName or sym (or both) is
 *             null, throw a WrongArgumentException. If the first HashMap
 *             in the list already contains the given id name as a key,
 *             throw a DuplicateSymException. Otherwise, add the given idName
 *             and sym to the first HashMap in the list.
 *       void addScope()
 *       	-- Add a new, empty HashMap to the front of the list.
 *
 *       Sym lookupLocal(String idName) throws EmptySymTableException
 *          -- If this SymTable's list is empty, throw an
 *             EmptySymTableException. Otherwise, if the first
 *             HashMap in the list contains id name as a key,
 *             return the associated Sym; otherwise, return null.
 *
 *       Sym lookupGlobal(String idName) throws EmptySymTableException
 *          -- If this SymTable's list is empty, throw an
 *             EmptySymTableException. If any HashMap in the
 *             list contains idName as a key, return the first
 *             associated Sym (i.e., the one from the HashMap
 *             that is closest to the front of the list);
 *             otherwise, return null.
 *
 *       void removeScope() throws EmptySymTableException
 *          -- If this SymTable's list is empty, throw an
 *             EmptySymTableException; otherwise, remove the
 *             HashMap from the front of the list. To clarify,
 *             throw an exception only if before attempting to remove,
 *             the list is empty (i.e. there are no HashMaps to remove).
 *
 *       void print()
 *          -- This method is for debugging.
 *             First, print “\n=== Sym Table ===\n”.
 *             Then, for each HashMap M in the list, print M.toString()
 *             followed by a newline. Finally, print one more newline.
 *
 * This code tests every List operation, including both correct and
 * bad calls to the operation that can throw an exception.
 * It produces output ONLY if a test fails.
 *
 * @author Henry Xu
 */

public class P1 {
    public static void main(String [] args) {

        /** Test the constructor of SymTable. */
        SymTable table1 = new SymTable();

        try {
            table1.removeScope();
        } catch (EmptySymTableException ex) {
            System.out.println("The constructor failed to initialize an empty HashMap");
        }

        /** Test addScope for Symtable with empty list.
         *  Add 10 new scopes to the list */
        for (int i = 0; i < 10; i++) {
            table1.addScope();
        }

        /** Test removeScope for Symtable.
         *  Remove all 10 scopes from the front of the list
         *  to test if 10 scopes were correctly added.
         *  Then test if all 10 scopes were successfully removed. */
        try {
            for (int i = 0; i < 10; i++) {
                table1.removeScope();
            }
        } catch (EmptySymTableException ex) {
            System.out.println("10 scopes are not all added.");
        }

        try {
            table1.removeScope();
            System.out.println("10 scopes are not all removed, "
                    + "there are still scopes remaining.");
        } catch (EmptySymTableException ex) {
        }

        /** Test attempt to remove with empty list. */
        try {
            table1.removeScope();
            System.out.println("NO exception thrown on attempt to remove "
                    + "scope in an empty list");
        } catch (EmptySymTableException ex) {
        }

        /** Test addDecl */
        SymTable table2 = new SymTable();

        /** Test addDecl with an empty list */
        try {
            table2.removeScope();
            table2.addDecl("var1", new Sym("int"));
            System.out.println("NO exception thrown on attempt to add "
                    + "Decl to a list without any scope.");
        } catch (EmptySymTableException ex) {
        } catch (DuplicateSymException ex) {
        } catch (WrongArgumentException ex) {
        }

        /** Test addDecl with null idName, null sym,
         * both null idName and null sym. */
        table2.addScope();

        try {
            table2.addDecl(null, new Sym("int"));
            System.out.println("NO exception thrown on attempt to add "
                    + "Decl with null idName");
        } catch (EmptySymTableException ex) {
        } catch (DuplicateSymException ex) {
        } catch (WrongArgumentException ex) {
            if (!ex.getMessage().equals("Id name is null.")) {
                System.out.println("Wrong exception message for null idName");
            }
        }

        try {
            table2.addDecl("var1", null);
            System.out.println("NO exception thrown on attempt to add "
                    + "Decl with null sym");
        } catch (EmptySymTableException ex) {
        } catch (DuplicateSymException ex) {
        } catch (WrongArgumentException ex) {
            if (!ex.getMessage().equals("Sym is null.")) {
                System.out.println("Wrong exception message for null sym");
            }
        }

        try {
            table2.addDecl(null, new Sym(null));
            System.out.println("NO exception thrown on attempt to add "
                    + "Decl with both null idName and null sym.");
        } catch (EmptySymTableException ex) {
        } catch (DuplicateSymException ex) {
        } catch (WrongArgumentException ex) {
            if (!ex.getMessage().equals("Id name is null.")) {
                System.out.println("Wrong exception message for" +
                        " both null idName and null sym.");
            }
        }

        /** Test add a Decl to a SymTable with only one empty HashMap,
         * with one non-empty HashMap that does not contain this Decl,
         * and  with one non-empty HashMap that already contains this Decl.*/
        try {
            table2.addDecl("var1", new Sym("int"));
        } catch (Exception ex) {
            System.out.println("Wrong result for adding Decl to " +
                    "a SymTable with only one empty HashMap.");
        }

        try {
            table2.addDecl("var2", new Sym("char"));
        } catch (Exception ex) {
            System.out.println("Wrong result for adding Decl to" +
                    "a SymTable with only one non-empty HashMap" +
                    " that does not contain this Decl.");
        }

        /** Add duplicate Decl and test exception. */
        try {
            table2.addDecl("var2", new Sym("char"));
            System.out.println("NO exception thrown on attempt to add "
                    + "a duplicate Decl to a SymTable with one " +
                    "non-empty HashMap that already contains this Decl.");
        } catch (Exception ex) {
        }

        /** Test add a Decl to a SymTable with 5 empty HashMaps,
         * with 5 non-empty HashMaps that does not contain this Decl,
         * and  with 5 non-empty HashMaps where the last one
         * already contains this Decl.*/
        SymTable table3 = new SymTable();

        try {
            for (int i = 0; i < 4; i++) {
                table3.addScope();
            }
            table3.addDecl("var1", new Sym("int"));
        } catch (Exception ex) {
            System.out.println("Wrong result for adding Decl to" +
                    "a SymTable with 5 empty HashMaps.");
        }

        try {
            for (int i = 0; i < 4; i++) {
                table3.removeScope();
            }
            table3.addDecl("var0", new Sym("int"));
            for (int i = 1; i < 5; i++) {
                table3.addScope();
                table3.addDecl("var" + i, new Sym("int"));
            }
            table3.addDecl("var6", new Sym("char"));
        } catch (Exception ex) {
            System.out.println("Wrong result for adding Decl to" +
                    "a SymTable with 5 non-empty HashMaps" +
                    " that does not contain this Decl.");
        }

        /** Add duplicate Decl to the first scope and test exception. */
        try {
            table3.addDecl("var6", new Sym("char"));
            System.out.println("NO exception thrown on attempt to add "
                    + "a duplicate Decl to a SymTable with 5 " +
                    "non-empty HashMaps where the last one" +
                    " already contains this Decl.");
        } catch (Exception ex) {
        }

        /** Add a Decl that does not exist in the first scope,
         * but exists in the previous scopes. */
        try {
            for (int i = 0; i < 4; i++) {
                table3.addDecl("var" + i, new Sym("int"));
            }
        } catch (Exception ex) {
            System.out.println("Wrong result for adding Decl to" +
                    "a SymTable with 5 HashMaps" +
                    " where only the last one does not contain it.");
        }


        

    }
}

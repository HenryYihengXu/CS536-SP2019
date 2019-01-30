import java.io.*;

/**
 * P1
 *
 * This is a class whose sole purpose is to test the Sym and SymTable class.
 *
 * Sym provides the following operations:
 *       constructor with parameter "type"
 *          -- create a Sym with "type" type.
 *
 *       String getType()
 *          -- Return the type of the Sym.
 *
 *       String toString()
 *          -- Return the type of the Sym.
 *
 *
 * SymTable provides the following operations:
 *       no-arg constructor
 *          -- create a linked list with an empty hash map in it.
 *
 *       void addDecl(String idName, Sym sym)
 *          throws DuplicateSymException, EmptySymTableException,
 *          WrongArgumentException
 *          -- If this SymTable's list is empty, throw an
 *             EmptySymTableException. If either idName or sym (or both) is
 *             null, throw a WrongArgumentException. If the first HashMap
 *             in the list already contains the given id name as a key,
 *             throw a DuplicateSymException. Otherwise, add the given idName
 *             and sym to the first HashMap in the list.
 *
 *       void addScope()
 *         -- Add a new, empty HashMap to the front of the list.
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
 *             First, print "\n=== Sym Table ===\n".
 *             Then, for each HashMap M in the list, print M.toString()
 *             followed by a newline. Finally, print one more newline.
 *
 * This code tests every operation in Symtable and Sym, including both correct and
 * bad calls to the operation that can throw an exception.
 * It produces output ONLY if a test fails.
 *
 * @author Henry Xu
 */

public class P1 {

    /** Thoroughly test SymTable and Sym.
     * Produce output only if a test fails. */
    public static void main(String [] args) {

        if (args.length != 0) {
            System.out.println("No command-line arguments are expected.");
        }

        /** Test the constructor of Sym and getType.*/
        Sym sym = new Sym("int");
        if (!sym.getType().equals("int")) {
            System.out.println("Wrong result of getType," +
                    "or error occurs in constructor.");
        }

        /** Test toString of Sym.*/
        if (!sym.toString().equals("int")) {
            System.out.println("Wrong result of toString.");
        }

        /** Test the constructor of SymTable.
         * See if SymTable has an empty HashMap */
        SymTable table1 = new SymTable();

        try {
            table1.removeScope();
        } catch (EmptySymTableException ex) {
            System.out.println("The constructor failed to " +
                    "initialize an empty HashMap");
        }

        /*
         *  Test addScope for Symtable with empty list.
         *  Add 10 new scopes to the list
         */
        for (int i = 0; i < 10; i++) {
            table1.addScope();
        }

        /* Test removeScope for Symtable.
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

        /* Test attempt to remove with empty list. */
        try {
            table1.removeScope();
            System.out.println("NO exception thrown on attempt to remove "
                    + "scope in an empty list");
        } catch (EmptySymTableException ex) {
        }

        /** Test addDecl */
        SymTable table2 = new SymTable();

        /* Test addDecl with an empty list */
        try {
            table2.removeScope();
            table2.addDecl("var1", new Sym("int"));
            System.out.println("NO exception thrown on attempt to add "
                    + "Decl to a list without any scope.");
        } catch (EmptySymTableException ex) {
        } catch (DuplicateSymException ex) {
        } catch (WrongArgumentException ex) {
        }

        /* Test addDecl with null idName, null sym,
         * both null idName and null sym.
         * Test if it can be added, and test
         * if the message is correct.*/
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

        /* Test add a Decl to a SymTable with only one empty HashMap,
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

        /* Add duplicate Decl and test exception. */
        try {
            table2.addDecl("var2", new Sym("char"));
            System.out.println("NO exception thrown on attempt to add "
                    + "a duplicate Decl to a SymTable with one " +
                    "non-empty HashMap that already contains this Decl.");
        } catch (Exception ex) {
        }

        /* Test add a Decl to a SymTable with 5 empty HashMaps,
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
            for (int i = 0; i < 5; i++) {
                table3.removeScope();
            }
            /* Add var0-var4 to the list, each in a different scope. */
            for (int i = 0; i < 5; i++) {
                table3.addScope();
                table3.addDecl("var" + i, new Sym("int"));
            }
            /* Add var6 to the first scope. */
            table3.addDecl("var6", new Sym("char"));
        } catch (Exception ex) {
            System.out.println("Wrong result for adding Decl to" +
                    "a SymTable with 5 non-empty HashMaps" +
                    " that does not contain this Decl.");
        }

        /* Add duplicate Decl to the first scope and test exception. */
        try {
            table3.addDecl("var6", new Sym("char"));
            System.out.println("NO exception thrown on attempt to add "
                    + "a duplicate Decl to a SymTable with 5 " +
                    "non-empty HashMaps where the last one" +
                    " already contains this Decl.");
        } catch (Exception ex) {
        }

        /* Add a Decl that does not exist in the first scope,
         * but exists in the previous scopes. From var0 to var3. */
        try {
            for (int i = 0; i < 4; i++) {
                table3.addDecl("var" + i, new Sym("int"));
            }
        } catch (Exception ex) {
            System.out.println("Wrong result for adding Decl to" +
                    "a SymTable with 5 HashMaps" +
                    " where only the last one does not contain it.");
        }

        /* Test lookupLocal for empty SymTable, item in the first scope,
         *  item in the list but not in the first scope,
         *  item not in the list at all.*/

        /* Test lookupLocal for empty SymTable. */
        SymTable table4 = new SymTable();
        try {
            table4.removeScope();
            table4.lookupLocal("var0");
            System.out.println("NO exception thrown on attempt to " +
                    "look up a item in an empty list.");
        } catch (EmptySymTableException ex) {
        }

        try {
            /* Add var0-var4 to the list, each in a different scope. */
            table4.addScope();
            table4.addDecl("var0", new Sym("int"));
            table4.addScope();
            table4.addDecl("var1", new Sym("char"));
            table4.addScope();
            table4.addDecl("var2", new Sym("boolean"));
            table4.addScope();
            table4.addDecl("var3", new Sym("String"));
            table4.addScope();
            table4.addDecl("var4", new Sym("Sym"));
            /* Add var5 to the first scope. */
            table4.addDecl("var5", new Sym("SymTable"));

            /* Test lookupLocal for item in the first scope. */
            Sym result1 = table4.lookupLocal("var4");
            if (result1 == null) {
                System.out.println("Wrong result: var4 should have been found.");
            } else if (!result1.getType().equals("Sym")) {
                System.out.println("Wrong Sym was found.");
            }

            /* Test lookupLocal for item in the list
             * but not in the first scope: var0-var3. */
            for (int i = 0; i < 4; i++) {
                Sym result2 = table4.lookupLocal("var" + i);
                if (result2 != null) {
                    System.out.println("Wrong result: var" + i +
                            " shouldn't be found.");
                }
            }

            /* Test lookupLocal for item not in the list at all. */
            Sym result3 = table4.lookupLocal("var6");
            if (result3 != null) {
                System.out.println("Wrong result: var6" +
                        " shouldn't be found.");
            }

        } catch (Exception ex) {
        }

        /* Test lookupGlobal for empty SymTable, item in the first scope,
         *  item in the last scope, item in the middle scope,
         *  item not in the list at all.
         *  Test if it can be found, and if the correct one is found. */

        SymTable table5 = new SymTable();

        /* Test look up a item for empty SymTable. */
        try {
            table5.removeScope();
            table5.lookupGlobal("var0");
            System.out.println("NO exception thrown on attempt to " +
                    "look up a item in an empty list.");
        } catch (EmptySymTableException ex) {
        }

        /* First, test for the item which is
         * the only copy in the whole list. */
        try {
            /* Add var0-var9, var00-var99 to the list, each pair in a different scope. */
            table5.addScope();
            table5.addDecl("var0", new Sym("int"));
            table5.addDecl("var00", new Sym("int[]"));
            table5.addScope();
            table5.addDecl("var1", new Sym("char"));
            table5.addDecl("var11", new Sym("char[]"));
            table5.addScope();
            table5.addDecl("var2", new Sym("boolean"));
            table5.addDecl("var22", new Sym("boolean[]"));
            table5.addScope();
            table5.addDecl("var3", new Sym("String"));
            table5.addDecl("var33", new Sym("String[]"));
            table5.addScope();
            table5.addDecl("var4", new Sym("Sym"));
            table5.addDecl("var44", new Sym("Sym[]"));
            table5.addScope();
            table5.addDecl("var5", new Sym("List"));
            table5.addDecl("var55", new Sym("List[]"));
            table5.addScope();
            table5.addDecl("var6", new Sym("HashMap"));
            table5.addDecl("var66", new Sym("HashMap[]"));
            table5.addScope();
            table5.addDecl("var7", new Sym("Exception"));
            table5.addDecl("var77", new Sym("Exception[]"));
            table5.addScope();
            table5.addDecl("var8", new Sym("Stack"));
            table5.addDecl("var88", new Sym("Stack[]"));
            table5.addScope();
            table5.addDecl("var9", new Sym("Heap"));
            table5.addDecl("var99", new Sym("Heap[]"));

            /* Test lookupGlobal for item in the first scope. */
            Sym result1 = table5.lookupGlobal("var9");
            if (result1 == null) {
                System.out.println("Wrong result: var9 should have been found.");
            } else if (!result1.getType().equals("Heap")) {
                System.out.println("Wrong Sym was found.");
            }

            /* Test lookupGlobal for item in the last scope. */
            Sym result2 = table5.lookupGlobal("var0");
            if (result2 == null) {
                System.out.println("Wrong result: var0 should have been found.");
            } else if (!result2.getType().equals("int")) {
                System.out.println("Wrong Sym was found.");
            }

            /* Test lookupGlobal for item in the middle scope. */
            Sym result3 = table5.lookupGlobal("var4");
            if (result3 == null) {
                System.out.println("Wrong result: var4 should have been found.");
            } else if (!result3.getType().equals("Sym")) {
                System.out.println("Wrong Sym was found.");
            }

            /* Test lookupGlobal for item not in the list at all. */
            Sym result4 = table5.lookupGlobal("var10");
            if (result4 != null) {
                System.out.println("Wrong result: var10" +
                        " shouldn't be found.");
            }
        } catch (Exception ex) {
        }

        /* Now, test for the item which is not
         * the only copy in the whole list.
         * Test if lookupGlobal returns the one
         * which is closest to the front of the list. */
        SymTable table6 = new SymTable();
        try {
            /* Add items with same name to the SymTable:
             * at first, at middle, at the end. */
            table6.addDecl("var0", new Sym("int"));
            table6.addDecl("var00", new Sym("int[]"));
            table6.addScope();
            table6.addDecl("var0", new Sym("char"));
            table6.addDecl("var00", new Sym("char[]"));
            table6.addScope();
            table6.addDecl("var0", new Sym("boolean"));
            table6.addDecl("var00", new Sym("boolean[]"));
            table6.addScope();
            table6.addDecl("var3", new Sym("String"));
            table6.addDecl("var33", new Sym("String[]"));
            table6.addScope();
            table6.addDecl("var3", new Sym("Sym"));
            table6.addDecl("var33", new Sym("Sym[]"));
            table6.addScope();
            table6.addDecl("var3", new Sym("List"));
            table6.addDecl("var33", new Sym("List[]"));
            table6.addScope();
            table6.addDecl("var6", new Sym("HashMap"));
            table6.addDecl("var66", new Sym("HashMap[]"));
            table6.addScope();
            table6.addDecl("var6", new Sym("Exception"));
            table6.addDecl("var66", new Sym("Exception[]"));
            table6.addScope();
            table6.addDecl("var6", new Sym("Stack"));
            table6.addDecl("var66", new Sym("Stack[]"));

            /* Test lookupGlobal for items with same name
             *  at the beginning of the list. */
            Sym result1 = table6.lookupGlobal("var6");
            if (result1 == null) {
                System.out.println("Wrong result: var6 should have been found.");
            } else if (!result1.getType().equals("Stack")) {
                System.out.println("Wrong Sym was found.");
            }

            /* Test lookupGlobal for items with same name
             *  at the middle of the list. */
            Sym result2 = table6.lookupGlobal("var3");
            if (result2 == null) {
                System.out.println("Wrong result: var3 should have been found.");
            } else if (!result2.getType().equals("List")) {
                System.out.println("Wrong Sym was found.");
            }

            /* Test lookupGlobal for items with same name
             *  at the end of the list. */
            Sym result3 = table6.lookupGlobal("var0");
            if (result3 == null) {
                System.out.println("Wrong result: var0 should have been found.");
            } else if (!result3.getType().equals("boolean")) {
                System.out.println("Wrong Sym was found.");
            }
        } catch (Exception ex) {
        }

        /* Test if addScope add scope to the front of the list.
         * Test if addDecl add new item to the first scope. */
        try {
            table6.addScope();
            table6.addDecl("var6", new Sym("Heap"));
            Sym result = table6.lookupGlobal("var6");
            if (!result.getType().equals("Heap")) {
                System.out.println("Scope wasn't added to the front, " +
                        "or the item wasn't added to the first scope.");
            }
        } catch (Exception ex) {
        }

        /* Test if removeScope remove scope from the front of list. */
        try {
            table6.removeScope();
            table6.removeScope();
            Sym result = table6.lookupGlobal("var6");
            if (!result.getType().equals("Exception")) {
                System.out.println("Scope wasn't removed from the front.");
            }
        } catch (Exception ex) {
        }

        /* Test if print works correctly by intercepting out stream. */
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));

        table6.print();

        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));

        String result = buffer.toString();
        buffer.reset();
        String expect = "\n=== Sym Table ===\n" +
                "{var6=Exception, var66=Exception[]}\n" +
                "{var6=HashMap, var66=HashMap[]}\n" +
                "{var3=List, var33=List[]}\n" +
                "{var3=Sym, var33=Sym[]}\n" +
                "{var3=String, var33=String[]}\n" +
                "{var00=boolean[], var0=boolean}\n" +
                "{var00=char[], var0=char}\n" +
                "{var00=int[], var0=int}\n\n";

        if (!result.equals(expect)) {
            System.out.println("Wrong result of print().");
        }
    }
}

/**
 * Sym
 *
 * This is a class that represents a symbol type in the source program.
 *  @author Henry Xu
 */

public class Sym {

    /** The type of this sym. */
    private String type;

    /** Constructor of Sym.
     *  Construct a Sym with given type.
     *  @param t the type to be assigned. */
    public Sym(String t) {
        type = t;
    }

    /** Get the type of this Sym.
     * @return the type of this Sym*/
    public String getType() {
        return type;
    }

    /** Return the string representation of this Sym. */
    @Override
    public String toString() {
        return type;
    }
}

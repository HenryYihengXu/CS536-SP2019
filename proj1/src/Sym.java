/** Represents a symbol type in the source program.
 *  @author Henry Xu
 */

public class Sym {

    /** The type of this sym. */
    private String type;

    /** Constructor of Sym.
     *  Construct a Sym with given type. */
    public Sym(String type) {
        this.type = type;
    }

    /** Get the type of this Sym */
    public String getType() {
        return type;
    }

    /** Return the string representation of this Sym */
    @Override
    public String toString() {
        return type;
    }
}

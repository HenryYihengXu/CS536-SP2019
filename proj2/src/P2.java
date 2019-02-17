import java.util.*;
import java.io.*;
import java_cup.runtime.*;  // defines Symbol

/**
 * This program is to be used to test the Carrot scanner.
 * This version is set up to test all tokens, but more code is needed to test 
 * other aspects of the scanner (e.g., input that causes errors, character 
 * numbers, values associated with tokens).
 */
public class P2 {

    // The buffer for redirect errors to output file.
    // See more details below.
    static ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    public static void main(String[] args) throws IOException {
                                           // exception may be thrown by yylex

        // !!! important !!!!
        // In order to properly test errors, I redirected
        // err to a buffer. And then for each time reading a token,
        // I write the buffer to the .out file.
        //
        // Therefore, in some test cases, if the input can cause errors,
        // you will see error messages in the output file but not on the screen!
        //
        // Also, because when error occurs, Jlex does not create a token.
        // So it cannot be printed out until a legal token is read.
        // Therefore, in output files, the order of error messages seems messed up.
        // But, they are correct if you carefully examine them!!
        System.setErr(new PrintStream(buffer));

        // test all tokens
        testAllTokens();
        CharNum.num = 1;

        // ADD CALLS TO OTHER TEST METHODS HERE

        testLineNumber1();
        CharNum.num = 1;
        testLineNumber2();
        CharNum.num = 1;
        testCharNumber1();
        CharNum.num = 1;
        testCharNumber2();
        CharNum.num = 1;
        testIllegalInt();
        CharNum.num = 1;
        testIllegalStr();
        CharNum.num = 1;
        testComment();
        CharNum.num = 1;
        testEOF();
        CharNum.num = 1;


        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }

    /**
     * testAllTokens
     *
     * Open and read from file allTokens.in (I changed it.)
     * For each token read, write the corresponding string to allTokens.out.
     * Compare allTokens.out with allTokensExpected.out.
     *
     * If the input file contains all tokens, one per line, we can verify
     * correctness of the scanner by comparing the input and output files
     * (e.g., using a 'diff' command).
     *
     * For this test, all possible legal tokens are tested:
     * all legal reserved literals, identifiers, legal digits, symbols.
     *
     * For strings, this test thoroughly tests all possible legal strings:
     * empty string, string with all 6 escaped characters.
     *
     * See allTokens.in for more details.
     *
     */
    private static void testAllTokens() throws IOException {
        // open input and output files
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("allTokens.in");
            outFile = new PrintWriter(new FileWriter("allTokens.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File allTokens.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("allTokens.out cannot be opened.");
            System.exit(-1);
        }

        // create and call the scanner
        Yylex my_scanner = new Yylex(inFile);
        Symbol my_token = my_scanner.next_token();
        while (my_token.sym != sym.EOF) {
            switch (my_token.sym) {
            case sym.BOOL:
                outFile.println("bool"); 
                break;
			case sym.INT:
                outFile.println("int");
                break;
            case sym.VOID:
                outFile.println("void");
                break;
            case sym.TRUE:
                outFile.println("true"); 
                break;
            case sym.FALSE:
                outFile.println("false"); 
                break;
            case sym.STRUCT:
                outFile.println("struct"); 
                break;
            case sym.CIN:
                outFile.println("cin"); 
                break;
            case sym.COUT:
                outFile.println("cout");
                break;				
            case sym.IF:
                outFile.println("if");
                break;
            case sym.ELSE:
                outFile.println("else");
                break;
            case sym.WHILE:
                outFile.println("while");
                break;
            case sym.RETURN:
                outFile.println("return");
                break;
            case sym.ID:
                outFile.println(((IdTokenVal)my_token.value).idVal);
                break;
            case sym.INTLITERAL:  
                outFile.println(((IntLitTokenVal)my_token.value).intVal);
                break;
            case sym.STRINGLITERAL: 
                outFile.println(((StrLitTokenVal)my_token.value).strVal);
                break;    
            case sym.LCURLY:
                outFile.println("{");
                break;
            case sym.RCURLY:
                outFile.println("}");
                break;
            case sym.LPAREN:
                outFile.println("(");
                break;
            case sym.RPAREN:
                outFile.println(")");
                break;
            case sym.SEMICOLON:
                outFile.println(";");
                break;
            case sym.COMMA:
                outFile.println(",");
                break;
            case sym.DOT:
                outFile.println(".");
                break;
            case sym.WRITE:
                outFile.println("<<");
                break;
            case sym.READ:
                outFile.println(">>");
                break;				
            case sym.PLUSPLUS:
                outFile.println("++");
                break;
            case sym.MINUSMINUS:
                outFile.println("--");
                break;	
            case sym.PLUS:
                outFile.println("+");
                break;
            case sym.MINUS:
                outFile.println("-");
                break;
            case sym.TIMES:
                outFile.println("*");
                break;
            case sym.DIVIDE:
                outFile.println("/");
                break;
            case sym.NOT:
                outFile.println("!");
                break;
            case sym.AND:
                outFile.println("&&");
                break;
            case sym.OR:
                outFile.println("||");
                break;
            case sym.EQUALS:
                outFile.println("==");
                break;
            case sym.NOTEQUALS:
                outFile.println("!=");
                break;
            case sym.LESS:
                outFile.println("<");
                break;
            case sym.GREATER:
                outFile.println(">");
                break;
            case sym.LESSEQ:
                outFile.println("<=");
                break;
            case sym.GREATEREQ:
                outFile.println(">=");
                break;
			case sym.ASSIGN:
                outFile.println("=");
                break;
			default:
				outFile.println("UNKNOWN TOKEN");
            } // end switch

            my_token = my_scanner.next_token();
        } // end while
        outFile.close();
    }

    /**
     * testLineNumber1
     *
     * Open and read from file lineNumber1.in.
     * Write to file lineNumber1.out
     * Compare lineNumber1.out with lineNumber1Expected.out
     *
     * This test tests line numbers for a file without
     * illegal literals (No line is ignored).
     *
     * The file contains empty lines, lines consisting of whitespaces and tabs,
     * and lines that have many literals in it.
     *
     * See lineNumber1.in for more details.
     */
    private static void testLineNumber1() throws IOException {
        // open input and output files
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("lineNumber1.in");
            outFile = new PrintWriter(new FileWriter("lineNumber1.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File lineNumber1.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("lineNumber1.out cannot be opened.");
            System.exit(-1);
        }

        // create and call the scanner
        Yylex my_scanner = new Yylex(inFile);
        Symbol my_token = my_scanner.next_token();

        while (my_token.sym != sym.EOF) {
            outFile.println(((TokenVal)(my_token.value)).linenum);
            my_token = my_scanner.next_token();
        }

        // end while
        outFile.close();
    }

    /**
     * testLineNumber2
     *
     * Open and read from file lineNumber2.in
     * Write to file lineNumber2.out
     * Compare lineNumber2.out with lineNumber2Expected.out
     *
     * This test tests line numbers for a file with
     * illegal literals (Some lines are ignored).
     *
     * The file contains empty lines, lines consisting of whitespaces and tabs,
     * and lines that have many literals in it.
     *
     * See lineNumber2.in for more details.
     */
    private static void testLineNumber2() throws IOException {
        // open input and output files
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("lineNumber2.in");
            outFile = new PrintWriter(new FileWriter("lineNumber2.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File lineNumber2.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("lineNumber2.out cannot be opened.");
            System.exit(-1);
        }

        // create and call the scanner
        Yylex my_scanner = new Yylex(inFile);
        Symbol my_token = my_scanner.next_token();

        while (my_token.sym != sym.EOF) {

            // !!! important !!!
            // Here is the first time I use buffer to redirect errors
            // to output files. After a token is read, it prints all error messages
            // in the buffer. In order to avoid duplicate error message,
            // each time it is printed, I reset the buffer.
            // Same for the rest of my program, so I won't explain it
            // again in the rest of my program.
            outFile.print(buffer);
            buffer.reset();
            outFile.println(((TokenVal)(my_token.value)).linenum);
            my_token = my_scanner.next_token();
        }

        // end while
        outFile.close();
    }

    /**
     * testCharNumber1
     *
     * Open and read from file charNumber1.in.
     * Write to file charNumber1.out
     * Compare charNumber1.out with charNumber1Expected.out
     *
     * This test tests char numbers for a file without
     * illegal literals (No literal is ignored).
     *
     * The file contains empty lines, lines consisting of whitespaces and tabs,
     * and literals that are separated by whitespaces and tabs.
     *
     * See charNumber1.in for more details.
     */
    private static void testCharNumber1() throws IOException {
        // open input and output files
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("charNumber1.in");
            outFile = new PrintWriter(new FileWriter("charNumber1.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File charNumber1.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("charNumber1.out cannot be opened.");
            System.exit(-1);
        }

        // create and call the scanner
        Yylex my_scanner = new Yylex(inFile);
        Symbol my_token = my_scanner.next_token();

        while (my_token.sym != sym.EOF) {
            outFile.println(((TokenVal)(my_token.value)).charnum);
            my_token = my_scanner.next_token();
        }

        // end while
        outFile.close();
    }

    /**
     * testCharNumber2
     *
     * Open and read from file charNumber2.in.
     * Write to file charNumber2.out
     * Compare charNumber2.out with charNumber2Expected.out
     *
     * This test tests char numbers for a file with
     * illegal literals (some literals are ignored).
     *
     * The file contains empty lines, lines consisting of whitespaces and tabs,
     * and literals (legal and illegal) that are separated by whitespaces and tabs.
     *
     * See charNumber1.in for more details.
     */
    private static void testCharNumber2() throws IOException {
        // open input and output files
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("charNumber2.in");
            outFile = new PrintWriter(new FileWriter("charNumber2.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File charNumber2.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("charNumber2.out cannot be opened.");
            System.exit(-1);
        }

        // create and call the scanner
        Yylex my_scanner = new Yylex(inFile);
        Symbol my_token = my_scanner.next_token();

        while (my_token.sym != sym.EOF) {

            // write error message.
            outFile.print(buffer);
            buffer.reset();
            outFile.println(((TokenVal)(my_token.value)).charnum);
            my_token = my_scanner.next_token();
        }

        // end while
        outFile.close();
    }
    /**
     * testIllegalInt
     *
     * Open and read from file illegalInt.in.
     * Write to file illegalInt.out
     * Compare illegalInt.out with illegalIntExpected.out
     *
     * This test tests integers that are greater than Integer.MAX_VALUE.
     * Legal integers are tested in the first test case.
     *
     * For illegal integers, it first writes its value, which will
     * be Integer.MAX_VALUE. Then it writes the warning message to the file.
     *
     * See illegalInt.in for more details.
     */
    private static void testIllegalInt() throws IOException {
        // open input and output files
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("illegalInt.in");
            outFile = new PrintWriter(new FileWriter("illegalInt.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File illegalInt.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("illegalInt.out cannot be opened.");
            System.exit(-1);
        }

        // create and call the scanner
        Yylex my_scanner = new Yylex(inFile);
        Symbol my_token = my_scanner.next_token();

        while (my_token.sym != sym.EOF) {

            // write warning message.
            outFile.print(buffer);
            buffer.reset();
            outFile.println(((IntLitTokenVal)my_token.value).intVal);
            my_token = my_scanner.next_token();
        }

        // end while
        outFile.close();
    }

    /**
     * testIllegalStr
     *
     * Open and read from file illegalStr.in.
     * Write to file illegalStr.out
     * Compare illegalInt.out with illegalStrExpected.out
     *
     * This test tests all three kinds of illegal string:
     * unterminated, bad, and unterminated & bad.
     *
     * Legal strings are tested in the first test case.
     *
     * For illegal strings, it writes the warning message to the file
     * as soon as the next token is read (Same reason of buffer mentioned above).
     *
     * See illegalStr.in for more details.
     */
    private static void testIllegalStr() throws IOException {
        // open input and output files
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("illegalStr.in");
            outFile = new PrintWriter(new FileWriter("illegalStr.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File illegalStr.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("illegalStr.out cannot be opened.");
            System.exit(-1);
        }

        // create and call the scanner
        Yylex my_scanner = new Yylex(inFile);
        Symbol my_token = my_scanner.next_token();

        while (my_token.sym != sym.EOF) {

            // write error message.
            outFile.print(buffer);
            buffer.reset();
            if (my_token.sym == sym.STRINGLITERAL) {
                outFile.println(((StrLitTokenVal)my_token.value).strVal);
            } else {
                outFile.println("test failed");
            }
            //
            my_token = my_scanner.next_token();
        }
        // end while
        outFile.close();
    }

    /**
     * testComment
     *
     * Open and read from file comment.in.
     * Write to file comment.out
     * Compare illegalInt.out with commentExpected.out
     *
     * This test tests two kinds of comment.
     * Test if the comment is ignored but counts for lines.
     * i.e. test linenum with comments.
     *
     * See comment.in for more details.
     */
    private static void testComment() throws IOException {
        // open input and output files
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("comment.in");
            outFile = new PrintWriter(new FileWriter("comment.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File comment.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("comment.out cannot be opened.");
            System.exit(-1);
        }

        // create and call the scanner
        Yylex my_scanner = new Yylex(inFile);
        Symbol my_token = my_scanner.next_token();

        while (my_token.sym != sym.EOF) {
            switch (my_token.sym) {
                case sym.BOOL:
                    outFile.println("bool, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.INT:
                    outFile.println("int, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.VOID:
                    outFile.println("void, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.TRUE:
                    outFile.println("true, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.FALSE:
                    outFile.println("false, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.STRUCT:
                    outFile.println("struct, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.CIN:
                    outFile.println("cin, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.COUT:
                    outFile.println("cout, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.IF:
                    outFile.println("if, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.ELSE:
                    outFile.println("else, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.WHILE:
                    outFile.println("while, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.RETURN:
                    outFile.println("return, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.ID:
                    outFile.println(((IdTokenVal)my_token.value).idVal + ", line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.INTLITERAL:
                    outFile.println(((IntLitTokenVal)my_token.value).intVal + ", line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.STRINGLITERAL:
                    outFile.println(((StrLitTokenVal)my_token.value).strVal + ", line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.LCURLY:
                    outFile.println("{, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.RCURLY:
                    outFile.println("}, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.LPAREN:
                    outFile.println("(, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.RPAREN:
                    outFile.println("), line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.SEMICOLON:
                    outFile.println(";, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.COMMA:
                    outFile.println(",, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.DOT:
                    outFile.println("., line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.WRITE:
                    outFile.println("<<, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.READ:
                    outFile.println(">>, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.PLUSPLUS:
                    outFile.println("++, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.MINUSMINUS:
                    outFile.println("--, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.PLUS:
                    outFile.println("+, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.MINUS:
                    outFile.println("-, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.TIMES:
                    outFile.println("*, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.DIVIDE:
                    outFile.println("/, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.NOT:
                    outFile.println("!, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.AND:
                    outFile.println("&&, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.OR:
                    outFile.println("||, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.EQUALS:
                    outFile.println("==, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.NOTEQUALS:
                    outFile.println("!=, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.LESS:
                    outFile.println("<, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.GREATER:
                    outFile.println(">, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.LESSEQ:
                    outFile.println("<=, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.GREATEREQ:
                    outFile.println(">=, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                case sym.ASSIGN:
                    outFile.println("=, line " + ((TokenVal)(my_token.value)).linenum);
                    break;
                default:
                    outFile.println("UNKNOWN TOKEN");
            } // end switch
            my_token = my_scanner.next_token();
        }
        // end while
        outFile.close();
    }

    /**
     * testEOF
     *
     * Open and read from file eof.txt.
     * Write to file eof.out
     * Compare eof.out with eof Expected.out
     *
     * This test specifically tests unterminated string with
     * eof before the closing quote.
     *
     * See eof.txt for more details.
     *
     */
    private static void testEOF() throws IOException {
        // open input and output files
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("eof.txt");
            outFile = new PrintWriter(new FileWriter("eof.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File eof.txt not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("eof.out cannot be opened.");
            System.exit(-1);
        }

        // create and call the scanner
        Yylex my_scanner = new Yylex(inFile);
        Symbol my_token = my_scanner.next_token();

        if (my_token.sym != sym.EOF) {
            outFile.println("test failed");
        } else {
            outFile.print("unterminated string literal with bad escaped character ignored");
        }
        // end while
        outFile.close();
    }
}

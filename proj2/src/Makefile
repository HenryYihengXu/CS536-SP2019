###
# This Makefile can be used to make a scanner for the Carrot language
# (Yylex.class) and to make a program that tests the scanner (P2.class).
#
# The default makes both the scanner and the test program.
#
# make clean removes all generated files.
#
# Note: P2.java will not compile unless Yylex.class exists.
#
###

# define the java compiler to be used and the flags
JC = javac
FLAGS = -g -cp $(CP)
CP = ./deps:.

P2.class: P2.java Yylex.class sym.class
	$(JC) $(FLAGS) P2.java

Yylex.class: Carrot.jlex.java ErrMsg.class sym.class
	$(JC) $(FLAGS) Carrot.jlex.java

Carrot.jlex.java: Carrot.jlex sym.class
	java -cp $(CP) JLex.Main Carrot.jlex

sym.class: sym.java
	$(JC) $(FLAGS) sym.java

ErrMsg.class: ErrMsg.java
	$(JC) $(FLAGS) ErrMsg.java

	
###
# testing - add more here to run your tester and compare its results
# to expected results
###
test:
	java -cp $(CP) P2 
	diff allTokens.out allTokensExpected.out
	diff lineNumber1.out lineNumber1Expected.out
	diff lineNumber2.out lineNumber2Expected.out
	diff charNumber1.out charNumber1Expected.out
	diff charNumber2.out charNumber2Expected.out
	diff illegalInt.out illegalIntExpected.out
	diff illegalStr.out illegalStrExpected.out
	diff comment.out commentExpected.out
	diff eof.out eofExpected.out

###
# clean up
###

clean:
	rm -f *~ *.class Carrot.jlex.java

cleantest:
	rm -f allTokens.out
	rm -f lineNumber1.out
	rm -f lineNumber2.out
	rm -f charNumber1.out
	rm -f charNumber2.out
	rm -f illegalInt.out
	rm -f illegalStr.out
	rm -f comment.out
	rm -f eof.out	

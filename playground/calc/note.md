java_cup2 -interface -parser Parser calc.cup

java -classpath ../../lib/jlex.jar JLex.Main calc.lex

javac -cp .:/workspaces/compiler/lib2/java-cup-11b-runtime.jar *.java

java -cp .:/workspaces/compiler/lib2/java-cup-11b-runtime.jar Parser
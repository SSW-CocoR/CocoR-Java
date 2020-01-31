javac -source 1.4 -target 1.4 -d . Trace.java Scanner.java Tab.java DFA.java ParserGen.java Parser.java Coco.java
jar cfm Coco.jar Coco.manifest Coco/*.class
del Coco\*.class
rd Coco

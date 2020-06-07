# Javamm Compiler

## GROUP 3C

### Members
 - Andreia Barreto Gouveia, up201706430@fe.up.pt
 - Carlos Eduardo da Nova Duarte, up201708804@fe.up.pt
 - Simão Pereira de Oliveira, up201603173@fe.up.pt
 - Sofia de Araújo Lajes, up201704066@fe.up.pt

### Self Assessment

|       Name      | Student Number | Grade | Contribution |
| --------------- | -------------- | ----- | ------------ |
| Andreia Gouveia | up201706430    |  18   |     25 %     |
| Carlos Duarte   | up201708804    |  18   |     25 %     |
| Simão Oliveira  | up201603173    |  18   |     25 %     |
| Sofia Lajes     | up201704066    |  18   |     25 %     |

---

## Summary
  This project was developed for the Compilers course, third year unit of MIEIC@FEUP.
  The goal is to implement a Java-- (a subset of the Java language) compiler, capable of performing syntatic and semantic analysis as well as code generation.
  
---

## Execute

  To run the JAR, do the following command:

```cmd
java -jar <jar filename> <arguments>
```

  Where ``<jar filename>`` is the name of the JAR file that has been copied to the root folder, and ``<arguments>`` are the arguments to be passed to ``main()``.

  Possible arguments:
    - `-a` or `--ast` to print the AST
    - `-t` or `--table` to print the SymbolTable 
    - `-o` for optimizations
    
---

## Dealing with Syntatic Error
  When an error is encountered, during syntatic analysis, the execution is immediately aborted. However, when the error is in the condition of a while statement, a message containing detailed information about it is displayed; then, following tokens are consumed until it reaches a safe token to continue analysing from. In Javamm class, a limit number of errors, MAX_NUM_ERRORS, allowed to be reported before aborting the program is defined.

---

## Semantic Analysis
  The semantic analysis is started by calling checkSemantic method (of SimpleNode class) upon the root node. This will then iterate through every node calling the checkNodeSemantics method, which is overrided in several AST node classes in order to fulfill the specified node type semantic rules.
  
  Some of the rules defined in this stage are:
    
    - in the same scope, a local variable overlaps a parameter with the same name, as the last overlaps a global with the same name in that method;
    - in assignments, the assigned and the assignee must be of same type;
    - a method's return value shall match its signature;
    - all variables are declared before any statement (globals are declared before methods are defined);
    - '&&' and '!' operators' children must be of type boolean or return boolean;
    - '+', '-', '*', '/' and '<' operators' children must be of type integer or return integer;
    - a non-static class must be called by initializing a variable of the same class -- 'this' can only be used when within a static method of the class;
    - when a class A extends class B, A may import and use methods of B using the same linguo or creating a object of type B and calling the function (as long as it is imported)
    - when a static method of class B is imported ('import B.method(<type>) <type>'), this should be used calling: B.method(<args>);
    - 'length' property is only applicable to arrays;
    - conditions in 'while' and 'if' statements must return boolean;
    - the index of an array must be an integer or return an integer;
    - a method call must have a corresponding definition (same name and arguments must match parameters' types)
    
   Furthermore, it was possible to conceive interesting features:
   - Overloading
   - Only issuing a warning about a non initialized variable in an if statement if its the said variable is initialized on the then clause or in the else clause;
   - Calling methods of classes (extended or current) by declaring an object of that class.

---

## Intermidiate Representations (IRs)

  The only IR implemented was the representation through the symbol table. This module consists on four classes: SymbolTable, ImportMethod, Method and Symbol.
  Symbol represents a variable saving the variables' type, id (name), index, value, and two flags: is_initialized and constant.
  ImportMethod represents a method that was imported, storing the class' name, the method's name, the parameters' type, the return type, if it is static and how many overloads it has (which is initialized with value 0). 
  Method class hoards the name, return type, how many overloads it has (which is initialized with value 0), an invalid flag to identify if an overload method is invalid, an ordered list of parameters' types, an hashtable to keep the variables that represent the parameters as well as an hashtable that represents the local variables registered inside the method's body.
  SymbolTable has three hashtables: one for global variables only - having the variable's name as key and Symbol as the value - other to save the methods - having the method's name as key and Method as the value - and, finally, one for the imports - having a string composed by class name + "." + method name as key and Method as the value.
  In order to allow function overloads, everytime a method is being analysed (during syntatic analysis) the symbol table verifies if the method already exists; if positive, then it compares the arguments, if they are of same type in the same order, then the method is stored in the methods hashtable, however, the "invalid" flag of the method is set to true; otherwise, the method is stored correctly and registered as an overload. The distinction between the overloads (as well as the original method) is done by concatenating the number of overloads registered by the original method, and using the resulting string as the key to that entry in the hashtable. Note that, when searching if a method is already defined, the number of overloads must be retrieved from the method of the same name, and we must search all keys between the "original" and the "original + overloads". 

---

## Code Generation:

  A very modular approach was taken when implementing the code generation, making it easier to debug and add more features, like the templates that were added later. The AST is recursively traversed, each node generating its corresponding Jasmin code.  

  Lower cost instructions were used:
    - When dealing with integers we used the appropriate instruction, `iconst`, `bipush`, `sipush` and `ldc`
    - When dealing with comparissons we used comparissons with 0, `ifeq`, `ifne` and `ifge`
    - Code in the form `i = i+1` was replaced with a single `iinc` instruction

---

## Optimizations

### -o 

#### Constant Propagation
  After the semantic analysis, the tree is visited again, this time, when an integer or boolean variable is initialized with a constant value, it is registered, in the symbol table, that that variable is considered a constant as well as its value. 
  Then, every statement that uses a variable marked as constant gets that node (ASTId) changed to one referring the value of the it (ASTTrue or ASTFalse if boolean, depending on the value; ASTInteger if integer, with assigning the value to the "identity" attribute).
  If the variable is assigned a new value within an If or While statement, it is no longer considered a constant.
  Furthermore, constant folding is implemented in order to satisfy a larger condition:
  "	c = 3; //c is a constant with value 3
  	b = 4 + c; //b will now be a constant with value 4 + 3 = 7
  "

#### While templates

  Before generating the while loop code, the while condition was checked. If it was constituted by a single variable, constant or logic operation in which both the right and left are simple variables or constants, a template was used, as follow:

```
iconst_1
ifeq WCONT0
;...
ifne WHILE0
WCONT0:
```

  This allowed us to reduce the number of goto calls in the code, when this simple while loop is present.


---

## Tests
  We have created 5 tests in order to cover edge cases supported by our tool. These can be found in folder: test > fixtures > public > own.
  
---

## Overview
  The project is divided in five modules (eventhough they are not physically divided into folders or packages):
    - Parser: syntatic analyser in "Javamm.jjt" file
    - Symbol Table: representation of global, parameter and local variables as well as methods (imported or not)
    - Semantic Analyses: done specially by calling "checkSemantics" (SimpleNode's method) upon the root node
    - Code Generation: generation of .j files
    - Optimization: while template is applied during code generation phase, whilst constant propagation is applied between semantic analysis and code generation phases, using a class called "Optimization"

### Task Distribution
>Identify the set of tasks done by each member of the project.
#### Checkpoint 1:
   Lexical and syntatic analyses were made by the four members collectively.
#### Checkpoint 2:
   Creation of the symbol table and implementation of the semantic analyses were done by Andreia and Sofia.
   Carlos and Simão handled code generation.
#### Checkpoint 3:
   Carlos and Simão finished code generation.
   Andreia and Sofia did hard testing and created 5 new tests.
#### Final Delivery:
   Simão implemented "iinc" instruction and "while templates" optimization.
   Andreia and Sofia implemented "constant propagation" optimization, tested and improved the base tool by completing issues that were not fulfilled in previous checkpoints.

### Pros:
> Implemented constant folding
> If statement only initializes a variable in either then or else's body 

### Cons:
> The -r flag optimization was not implemented
> During constant propagation optimization, if a variable A might change value inside an if statement, then it is not considered a constant inside it. Observe the following example:
"	c = 0;
	if( c < 3){ //c = 0
		c = 3; // loses "constant" property
		f = c + 1; //can not substitute c by value 3
	}
"


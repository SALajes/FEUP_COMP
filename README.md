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

## Summary
>Describe what your tool does and its main features.

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

## Dealing with Syntatic Error
>Describe how the syntatic error recovery of your tool does work. Does it exit after the first error?

## Semantic Analysis
>Refer the semantic rules implemented by your tool.

## Intermidiate Representations (IRs)
> For example, when applicable, briefly describe the HLIR (high-level IR) and the LLIR (low-level IR) used, if your tool includes an LLIR wiht structure different from the HLIR

## Code Generation:

A very modular approach was taken when implementing the code generation, making it easier to debug and add more features, like the templates that were added later. The AST is recursively traversed, each node generating its corresponding Jasmin code.  

Lower cost instructions were used:
- When dealing with integers we used the appropriate instruction, `iconst`, `bipush`, `sipush` and `ldc`
- When dealing with comparissons we used comparissons with 0, `ifeq`, `ifne` and `ifge`
- Code in the form `i = i+1` was replaced with a single `iinc` instruction

### -o 

#### While templates

Before generating the while loop code, the while condition was checked. If it was constituted by a single variable, constant or logic operation in which both the right and left are simple variables or constants, a template was used, as follow:

```
iconst_1
ifeq WCONT0
;...
ifne WHILE0
WCONT0:
```

This allowed us to reduce the number of goto calls in the code, when thiis simple while loop is present.

---

## Task Dsitribution
>Identify the set of tasks done by each member of the project. You can divide this by checkpoint if it helps

## Pros:
> Identify the most positive aspects of your tool

## Cons:
> Identify the most negative aspects of your tool

---
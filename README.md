# Javamm Compiler

## GROUP 3C

### Members
 - Andreia Barreto Gouveia, up201706430@fe.up.pt
 - Carlos Eduardo da Nova Duarte, up201708804@fe.up.pt
 - Simão Pereira de Oliveira, up201603173@fe.up.pt
 - Sofia de Araújo Lajes, up201704066@fe.up.pt

### Seflf Assessment

| Name | Student Number | Grade | Contribution |
| --- | --- | --- | --- |
| Andreia Gouveia | up201706430 |  |  |
| Carlos Duarte | up201708804 |  |  |
| Simão Oliveira | up201603173 |  |  |
| Sofia Lajes | up201704066 |   |   |

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
>Identify the set of tasks done by each memeber of the project. You can divide this by checkpoin if it helps

## Pros:
> Identify the most positive aspects of your tool

## Cons:
> Identify the most negative aspects of your tool

---
# Checklists

## Semantic Analysis  - CP2 

*Todas as verificações feitas na análise semantica devem reportar erro excepto a verificação de inicialização de variáveis que deverá apenas dar um warning*  

- **Symbol Table**  
    - [X] global: inclui info de imports e a classe declarada
    - [X] classe-specific: inclui info de extends, fields e methods
    - [X] method-specific: inclui info dos arguments e local variables
    - Sub topics:
       - [X] Tem de permitir method overload (i.e. métodos com mesmo nome mas assinatura de parâmetros diferente)
       - [X] Tem de permitir consulta da tabela por parte da análise semantica (e geração de código)
       - [X] Tem de permitir ligar e desligar a sua impressão para fins de debug (neste caso para fins de avaliação)  
       
- **Type Verification**
    - [X] Verificar se operações são efetuadas com o mesmo tipo (e.g. int + boolean tem de dar erro)
    - [X] Não é possível utilizar arrays diretamente para operações aritmeticas (e.g. array1 + array2)
    - [X] Verificar se um array access é de facto feito sobre um array
    - [X] Verificar se o indice do array access é um inteiro
    - [X] Verificar se valor do assignee é igual ao do assigned (a_int = b_boolean não é permitido!)
    - [X] Verificar se operação booleana é efetuada só com booleanos
    - [X] Verificar se conditional expressions (if e while) resulta num booleano
    - [X] Verificar se variáveis são inicializadas, dando um WARNING em vez de ERRO
       - [X] Parametros são assumidos como inicializados
       - [ ] Devem fazer uma análise através do control flow, i.e., se há um if e a variável só é inicializada dentro de ou o then ou o else, deve-se dar um warning a indicar que poderá não estar inicializada
       - [ ] Será considerado bónus a quem resolver esta verificação usando erros em vez de warning.
            - Cuidado que se a analise não estiver bem feita os erros vão fazer com que o vosso compilador não passe para a geração de código!
			- Caso pretendam fazer esta abordagem com erros adicionem uma forma de ativar/desativar o erro para facilitar no caso de haver problemas.  
			
- **Function Verification**
	- [X] Verificar se o "target" do método existe, e se este contém o método (e.g. a.foo, ver se 'a' existe e se tem um método 'foo')
	    - Caso seja do tipo da classe declarada (e.g. a usar o this), verificar se é método do extends olhando para o que foi importado (isto se a classe fizer extends de outra classe importada)
	- [X] Caso o método não seja da classe declarada, isto é importada, verificar se método foi importado
	- [X] Verificar se o número de argumentos na invocação é igual ao número de parâmetros da declaração
	- [X] Verificar se o tipo dos parâmetros coincide com o tipo dos argumentos
	    - Não esquecer que existe method overloading  
	    
- **Code Generation**
    - [X] estrutura básica de classe (incluindo construtor <init>)
	- [X] estrutura básica de fields
	- [X] estrutura básica de métodos (podem desconsiderar os limites neste checkpoint: limit_stack 99, limit_locals 99)
	- [X] assignments
	- [X] operações aritméticas (com prioridade de operações correta)
		- neste checkpoint não é necessário a seleção das operações mais eficientes mas isto será considerado no CP3 e versão final
	- [X] invocação de métodos

## Code generation - CP3

- **Generate JVM code accepted by jasmin for loops:**
    - [X] similar to if statemnt, but with extra jumps

- **Generate JVM code accepted by jasmin to deal with arrays:**
    - [X] array initialization
    - [X] array store (astore)
    - [X] array access (aload)
    - [X] array position store
    - [X] array postition access
# COMP - GROUP 3C

### Members
 - Andreia Barreto Gouveia, up201706430@fe.up.pt
 - Carlos Eduardo da Nova Duarte, up201708804@fe.up.pt
 - Simão Pereira de Oliveira, up201603173@fe.up.pt
 - Sofia de Araújo Lajes, up201704066@fe.up.pt

#### Run ``.jar``

To run the JAR, do the following command:

```cmd
java -jar <jar filename> <arguments>
```

Where ``<jar filename>`` is the name of the JAR file that has been copied to the root folder, and ``<arguments>`` are the arguments to be passed to ``main()``.

<br/>

Possible arguments:
 - `-a` or `--ast` to print the AST
 - `-t` or `--table` to print the SymbolTable   

<br/>

---

## Semantic Analysis  

*Todas as verificações feitas na análise semantica devem reportar erro excepto a verificação de inicialização de variáveis que deverá apenas dar um warning*  
<br/>

- **Symbol Table**  
    - [X] global: inclui info de imports e a classe declarada
    - [X] classe-specific: inclui info de extends, fields e methods
    - [X] method-specific: inclui info dos arguments e local variables
    - Sub topics:
       - [ ] Tem de permitir method overload (i.e. métodos com mesmo nome mas assinatura de parâmetros diferente)
       - [X] Tem de permitir consulta da tabela por parte da análise semantica (e geração de código)
       - [ ] Tem de permitir ligar e desligar a sua impressão para fins de debug (neste caso para fins de avaliação)  
       
- **Type Verification**
    - [X] Verificar se operações são efetuadas com o mesmo tipo (e.g. int + boolean tem de dar erro)
    - [X] Não é possível utilizar arrays diretamente para operações aritmeticas (e.g. array1 + array2)
    - [ ] Verificar se um array access é de facto feito sobre um array
    - [ ] Verificar se o indice do array access é um inteiro
    - [ ] Verificar se valor do assignee é igual ao do assigned (a_int = b_boolean não é permitido!)
    - [X] Verificar se operação booleana é efetuada só com booleanos
    - [ ] Verificar se conditional expressions (if e while) resulta num booleano
    - [ ] Verificar se variáveis são inicializadas, dando um WARNING em vez de ERRO
       - Parametros são assumidos como inicializados
       - Devem fazer uma análise através do control flow, i.e., se há um if e a variável só é inicializada dentro de ou o then ou o else, deve-se dar um warning a indicar que poderá não estar inicializada
       - Será considerado bónus a quem resolver esta verificação usando erros em vez de warning.
            - Cuidado que se a analise não estiver bem feita os erros vão fazer com que o vosso compilador não passe para a geração de código!
			- Caso pretendam fazer esta abordagem com erros adicionem uma forma de ativar/desativar o erro para facilitar no caso de haver problemas.  
			
- **Function Verification**
	- [X] Verificar se o "target" do método existe, e se este contém o método (e.g. a.foo, ver se 'a' existe e se tem um método 'foo')
	    - Caso seja do tipo da classe declarada (e.g. a usar o this), verificar se é método do extends olhando para o que foi importado (isto se a classe fizer extends de outra classe importada)
	- [X] Caso o método não seja da classe declarada, isto é importada, verificar se método foi importado
	- [ ] Verificar se o número de argumentos na invocação é igual ao número de parâmetros da declaração
	- [ ] Verificar se o tipo dos parâmetros coincide com o tipo dos argumentos
	    - Não esquecer que existe method overloading  
	    
- **Code Generation**
    - [X] estrutura básica de classe (incluindo construtor <init>)
	- [X] estrutura básica de fields
	- [X] estrutura básica de métodos (podem desconsiderar os limites neste checkpoint: limit_stack 99, limit_locals 99)
	- [X] assignments
	- [X] operações aritméticas (com prioridade de operações correta)
		- neste checkpoint não é necessário a seleção das operações mais eficientes mas isto será considerado no CP3 e versão final
	- [ ] invocação de métodos
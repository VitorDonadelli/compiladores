# Trabalho 1 - Analisador Léxico
#####   Alunos:
- Vitor Donadelli  - 760650
- Reynold Navarro Mazo - 756188

#####   Trabalho:
Este trabalho consiste em implementar um analisador léxico para a linguagem LA (Linguagem Algorítmica) desenvolvida pelo prof. Jander, no âmbito do DC/UFSCar. O analisador léxico deve ler um programa-fonte e produzir uma lista de tokens identificados. 

##### Clone
Para realizar a copia do arquivo é  necessário realizar o clone do repositório, isto pode ser feito através do comando:
> \> git clone https://github.com/VitorDonadelli/compiladores/
##### Build
É necessário que o projeto clonado seja compilado antes de executar os testes, para isso, podemos abrir o projeto em uma IDE, como exemplo Netbeans, e rodamos um *Build*
##### Testes
Para rodar os testes através dos comandos a seguir, devemos estar no diretório clonado. Portanto, no diretório atual, mudamos ao novo diretório através do comando: 
> \> cd compiladores

Utilizaremos do corretor automático para a realização dos testes. Para isso iremos utilizar um padrão de comando que consiste em:

> java -jar ARG1 ARG2 ARG3 ARG4 ARG5 ARG6 ARG7
- ARG1 = Path do corretor
- ARG2 = Path do executável do compilador ("entre aspas") 
- ARG3 = Compilador GCC
- ARG4 = Path para uma pasta de saida
- ARG5 =  Path para a pasta onde estão os casos de teste
- ARG6 = RAs dos membros do grupos ("entre aspas") 
- ARG7 = Índice do trabalho (t1, t2, t3, t4, t5) 

Transformamos a estrutura a cima com os nossos diretórios, levando em consideração que estamos no diretório clonado, desta forma iremos executar o seguinte comando: 
> \> java -jar Sources/compiladores-corretor-automatico-1.0-SNAPSHOT-jar-with-dependencies.jar "java -jar T1/la-lexico/target/la-lexico-1.0-SNAPSHOT-jar-with-dependencies.jar" gcc Sources/saidas-geradas Sources/casos-de-teste "760650, 756188" t1

Ao finalizar a execução de todos os testes temos como resultado : 
```
Execução finalizada
Verificando resultado
Resultado verificado


==================================
Nota do grupo "760650, 756188":
CT 1 = 10.0 (37/37)
CT 2 = 0.0 (0/62)
CT 3 = 0.0 (0/9)
CT 4 = 0.0 (0/9)
CT 5 = 0.0 (0/20)
==================================
```

# Trabalho 4 - Analisador Semântico 2/2

###   Alunos:
<table>
  <tr>
    <td align="center">
        <a href="#">
            <img style="border-radius: 25%" src="https://avatars.githubusercontent.com/u/37456066?v=4" width="75px;" alt="Reynold Mazo"/><br>
        <sub>
           <a href="https://github.com/reynold125"><b>Reynold N. Mazo</b></a><br>
           <b>RA: 756188</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="#">
        <img style="border-radius: 25%" src="https://avatars.githubusercontent.com/u/40279487?v=4" width="75px;" alt="Vitor Donadelli Rodrigues"/><br>
        <sub>
          <a href="https://github.com/VitorDonadelli"><b>Vitor Donadelli</b></a><br>
           <b>RA: 760650</b>
        </sub>
      </a>
    </td>
  </tr>
</table>

### Trabalho:
Este trabalho consiste em implementar uma segunda parte de um analisador semântico para a linguagem LA (Linguagem Algorítmica) desenvolvida pelo prof. Jander, no âmbito do DC/UFSCar. O analisador semântico deve detectar, além daqueles do T3, outros 5 tipos de erros: 
1. Identificador (variável, constante, procedimento, função, tipo) já declarado anteriormente no escopo, mas agora envolvendo também ponteiros, registros, funções 
  - O mesmo identificador não pode ser usado novamente no mesmo escopo mesmo
que para categorias diferentes
2. Identificador (variável, constante, procedimento, função) não declarado, mas agora envolvendo também ponteiros, registros, funções
3. Incompatibilidade entre argumentos e parâmetros formais (número, ordem e tipo) na chamada de um procedimento ou uma função
4. Atribuição não compatível com o tipo declarado, agora envolvendo ponteiros e registros
5. Uso do comando 'retorne' em um escopo não permitido



As especificações do trabalho estão no pdf : **T4 - especificação e critérios.pdf**


### Clone:
Para realizar a copia do arquivo é  necessário realizar o clone do repositório, isto pode ser feito através do comando:
> \> git clone https://github.com/VitorDonadelli/compiladores/

Em seguida entramos no diretório clonado através do comando:
> \> cd compiladores

### Build:
É necessário que o projeto clonado seja compilado antes de executar os testes, para isso podemos ter diferentes formas de realizar o *Build*, a seguir será explicado como compilar utilizando Windows ou utilizando Linux.

#####  Build on Windows:
Para dar *Build* em nosso projeto, utilizando o sistema operacional Windows, podemos abrir o projeto em uma IDE, como exemplo Netbeans, e rodamos um *Build*, com isso o arquivos ```.jar``` serão gerados.

#####  Build on Linux:
Para dar *Build* em nosso projeto no Linux, podemos fazer isso utilizando a ferramenta *Maven*, neste roteiro iremos considerar que já possui esta ferramenta instalada e configurada.  
Caso não tenha instalado: [How to Install Apache Maven on Ubuntu 20.04](https://www.rosehosting.com/blog/how-to-install-apache-maven-on-ubuntu-20-04/)

Para buildar utilizando *Maven* precisamos ir até o diretório onde temos o arquivo ```.pom```, para isso rodamos o comando: 
> \> cd  T4/la-semantico2

Após isso rodamos os seguintes comandos:
> \> mvn compile  

>\> mvn package

Para voltarmos até o diretório inicial utilizamos o comando: 
>\> cd ../../

Com o projeto compilado e no diretório correto, podemos prosseguir com os testes.

### Testes:
Para rodar os testes através dos comandos a seguir, devemos estar no diretório clonado. Portanto, verifique que está no diretório : ```/compiladores>```

A maneira de executar testes individuais com o ```.jar``` gerado, utilizamos o seguinte comando: 

> \> java -jar T4/la-semantico2/target/la-semantico2-1.0-SNAPSHOT-jar-with-dependencies.jar <arquivo_de_entrada> <arquivo_de_saida>

Para finalidade avaliativa, utilizaremos do corretor automático proposto pelo Professor para a realização dos testes. Para isso iremos utilizar um padrão de comando que consiste em:

> java -jar ARG1 ARG2 ARG3 ARG4 ARG5 ARG6 ARG7  

Sendo:  
- ARG1 = Path do corretor
- ARG2 = Path do executável do compilador ("entre aspas") 
- ARG3 = Compilador GCC
- ARG4 = Path para uma pasta de saida
- ARG5 = Path para a pasta onde estão os casos de teste
- ARG6 = RA dos membros do grupos ("entre aspas") 
- ARG7 = Índice do trabalho (t1, t2, t3, t4, t5) 

Transformamos a estrutura a cima com os nossos diretórios, levando em consideração que estamos no diretório clonado, desta forma iremos executar o seguinte comando: 
> \> java -jar Sources/compiladores-corretor-automatico-1.0-SNAPSHOT-jar-with-dependencies.jar "java -jar T4/la-semantico2/target/la-semantico2-1.0-SNAPSHOT-jar-with-dependencies.jar" gcc Sources/saidas-geradas Sources/casos-de-teste "756188, 760650" t4

Ao finalizar a execução de todos os testes temos como resultado : 
```
Execução finalizada
Verificando resultado
Resultado verificado


==================================
Nota do grupo "756188, 760650":
CT 1 = 0.0 (0/37)
CT 2 = 0.0 (0/62)
CT 3 = 0.0 (0/9)
CT 4 = 2.2222223 (2/9)
CT 5 = 0.0 (0/20)
==================================
```

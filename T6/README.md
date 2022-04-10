# Trabalho 6 - Findinator

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

---
### Trabalho:
Este trabalho consiste em implementar um compilador que tem como finalidade converter frases em linguagem natural, seguindo algumas regras, para comandos que sigam a estrutura presente no comando **FIND**.

**Video de Apresentação**: URL

As especificações de regras e requisitos que o trabalho deve seguir estão no pdf : **T6 - especificação e critérios.pdf**

---
### Estrutura:
Para que o compilador funcione, é necessário que algumas regras a respeito do input sejam seguidas.
A estrutura do comando que deve ser passado deve conter o seguinte formato:

```yaml
(Encontre | Debug | Delete | Liste)
(
    em PATH ( ( , | e ) PATH )*
)?
(
    os ( arquivos | diretorios ) ( ( e | ou ) ( arquivos | diretorios ) )? 
)
(
    (
        (
            CONECTOR 
                  foi ( acessado | alterado ) 
                    a ( mais de | menos de | exatemente )? 
                  NUM ( minuto | minutos | hora | horas | dia | dias)
        )
      | (
            ( vazio | executavel )
        )
      | (
            CONECTOR (
               ( nome ( que começa com | que termina com )? FILE 
               ( ou   ( que começa com | que termina com )? FILE (...)? )? )
                
             | ( path ( que começa com | que termina com )? PATH 
               ( ou   ( que começa com | que termina com )? PATH (...)? )? )
            )
        )
      | (
            CONECTOR permição NUM
        )
      | (
            CONECTOR pode ser ( lido | escrito )
        )
      | (
            do usuário IDENT
        )
      | (
            CONECTOR 
               tamanho ( maior que | menor que | exatamente )? NUM ( b | Kb | Mb | Gb )
        )
    )
    (
        ( , | e ) (...)
    )*
)
```

Como exemplo de uma instrução que segue a estrutura de entrada, temos: 

**Entrada**:
>  Encontre os arquivos com nome teste.txt ou bola.txt ou cavalo.txt, do usuário vitor, que pode ser lido e que tem tamanho menor que 1 Mb

**Saida**:
> find -type f \\( -name "teste.txt" -o -name "bola.txt" -o -name "cavalo.txt" \\) -user vitor -readable -size -1M

---
### Detalhes:
Em casos de utilização de vários argumentos, deve-se utilizar separadores entre eles, para isso temos os separadores virgula **[ , ]** e o caractere **[ e ]**. Assim como na língua portuguesa, a virgula é utilizada para separar os argumento ao decorrer do texto, e ao final utilizamos o separado **e**, parar separar  apenas os penúltimo e ultimo argumentos.

Exemplificando, a entrada incorreta:
> Encontre os arquivos com nome teste.txt **e** do usuário vitor **e** que pode ser lido **e** que tem tamanho menor que 1 Mb

Deveria ser:
> Encontre os arquivos com nome teste.txt **,** do usuário vitor **,** que pode ser lido **e** que tem tamanho menor que 1 Mb

---
### Clone:
Agora que ja sabemos sobre o trabalho em si, podemos passar para a parte de testa-lo, para isso é necessário realizar o clone do repositório, isto pode ser feito através do comando:
> \> git clone https://github.com/VitorDonadelli/compiladores/

Em seguida entramos no diretório clonado através do comando:
> \> cd compiladores

---
### Build:
É necessário que o projeto clonado seja compilado antes de executar os testes, para isso podemos ter diferentes formas de realizar o *Build*, a seguir será explicado como compilar utilizando Windows ou utilizando Linux.

#####  Build on Windows:
Para dar *Build* em nosso projeto, utilizando o sistema operacional Windows, podemos abrir o projeto em uma IDE, como exemplo Netbeans, e rodamos um *Build*, com isso o arquivos ```.jar``` serão gerados.

#####  Build on Linux:
Para dar *Build* em nosso projeto no Linux, podemos fazer isso utilizando a ferramenta *Maven*, neste roteiro iremos considerar que já possui esta ferramenta instalada e configurada.  
Caso não tenha instalado pose-se seguir este tutorial: [How to Install Apache Maven on Ubuntu 20.04](https://www.rosehosting.com/blog/how-to-install-apache-maven-on-ubuntu-20-04/)

Para buildar utilizando *Maven* precisamos ir até o diretório onde temos o arquivo ```.pom```, para isso rodamos o comando: 
> \> cd  T6/findinator

Após isso rodamos os seguintes comandos:
> \> mvn compile  

>\> mvn package

Para voltarmos até o diretório inicial utilizamos o comando: 
>\> cd ../../

Com o projeto compilado e no diretório correto, podemos prosseguir com os testes.

---
### Testes:
Para rodar os testes através dos comandos a seguir, devemos estar no diretório clonado. Portanto, verifique que está no diretório : ```/compiladores>```

A maneira de executar testes individuais com o ```.jar``` gerado, utilizamos o seguinte comando: 

> \> java -jar T6/findinator/target/findinator-1.0-SNAPSHOT-jar-with-dependencies.jar <arquivo_de_entrada> <arquivo_de_saida>

Para finalidade avaliativa, estarei disponibilizando alguns casos de testes para serem executados e verificar a saída obtida. Para isso iremos utilizar um padrão de comando que consiste em:

> java -jar T6/findinator/target/findinator-1.0-SNAPSHOT-jar-with-dependencies.jar T6/findinator/testes/programa.txt T6/findinator/testes/saida.txt

Onde o "programa.txt" pode ser substituído por:  

- "teste1.txt": Erro Léxico (comando 'Acha' não existe)
---> Acha em ./pasta o arquivo com nome teste.txt

- "teste2.txt": Erro Léxico (ArquivoDiretorio 'programa' não existe)
---> Encontre os programa com nome teste.txt

- "teste3.txt": Erro Sintático (faltando o "nome")
---> Encontre os programa com nome 

- "teste4.txt": Erro Sintático (faltando a permissão)
---> Encontre os programa com nome teste.txt com permissao

- "teste5.txt": Erro Semântico (Separador ',' invalido ao posicionamento, esperado: 'e')
---> Encontre os arquivos com nome teste.txt ou cavalo.txt, do usuário vitor

- "teste6.txt": Erro Semântico (Separador 'e' invalido aos posicionamento, esperado: ',')
---> Encontre os arquivos com nome teste.txt ou bola.txt ou cavalo.txt e do usuário vitor e que pode ser lido e que tem tamanho menor que 1 Mb

- "teste7.txt": Erro Semântico (Conector 'que' errado, esperado: 'com')
---> Encontre os arquivos com nome teste.txt e que permissão 777

- "teste8.txt": Geração de código 
---> Encontre em ./T6 os arquivos com nome teste8.txt que pode ser lido

- "teste9.txt": Geração de código 
---> Encontre os arquivos com nome teste.txt ou bola.txt ou cavalo.txt, do usuário vitor, que pode ser lido e que tem tamanho menor que 1 Mb

Ao finalizar a execução de cada um desses comandos, é esperado que o arquivo **saidas.txt** seja preenchido ou em caso de erro, com um debug do erro, ou em caso de sucesso com o comando **find** em sua versão final e utilizável. 

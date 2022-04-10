grammar Findinator;

TIME_UNIT : 'minuto'
          | 'minutos'
          | 'hora'
          | 'horas'
          | 'dia'
          | 'dias'
          ;

SIZE_UNIT : 'b'
          | 'Kb'
          | 'Mb'
          | 'Gb'
          ;

COMANDO : 'Encontre'
        | 'Debug'
        | 'Delete'
        | 'Liste'
        ;

CONECTOR : 'que'
         | 'o qual'
         | 'com'
         | 'tal que'
         | 'que tem'
         | 'de'
         ;

TIPO : 'arquivos'
     | 'diretorios'
     ;

VERBO : 'acessado'
      | 'alterado'
      ;

MODIFICADOR_VERBO : 'mais de'
                  | 'menos de'
                  | 'exatemente'
                  ;

CARACTERISTICA : 'vazio'
               | 'executavel'
               ;

MODIFICADOR_NOME_PATH : 'que começa com'
                      | 'que termina com'
                      ;
                            
LER_ESCREVER : 'lido'
             | 'escrito'
             ;

MODIFICADOR_TAMANHO : 'maior que'
                    | 'menor que'
                    | 'exatamente'
                    ;

SEPARADOR : ', ' | ' e ' ;

NUM : ('0'..'9')+ ;

IDENT : ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-')+;

FILE : IDENT ( '.' IDENT )? ;

PATH : '.'? ('/' IDENT)+ ;

WS : ( ' ' | '\n' | '\r' | '\t' ) -> skip;

//

find : COMANDO lugar? arquivo_diretorio testes ;

lugar : 'em' PATH ( SEPARADOR PATH )*;

arquivo_diretorio : 'os' TIPO ( ( 'e' | 'ou' ) TIPO )? ;

testes : teste ( SEPARADOR teste )* ;

teste : verbo
      | caracteristica
      | nome_path
      | permissao
      | ler_escrever
      | usuario
      | tamanho
      ;

// que, o qual
verbo : CONECTOR 'foi' VERBO 'a' MODIFICADOR_VERBO? NUM TIME_UNIT ;

//
caracteristica : CARACTERISTICA ;

//
nome_tail : 'ou' MODIFICADOR_NOME_PATH? ( IDENT | FILE ) nome_tail? ;

//
path_tail : 'ou' MODIFICADOR_NOME_PATH? PATH path_tail? ;

// com
nome_path : CONECTOR (
      ( 'nome' MODIFICADOR_NOME_PATH? ( IDENT | FILE ) nome_tail? )
    | ( 'path' MODIFICADOR_NOME_PATH? PATH path_tail? )
) ;

// com
permissao : CONECTOR 'permissão' NUM ;

// que, o qual, tal que
ler_escrever : CONECTOR 'pode ser' LER_ESCREVER ;

//
usuario : 'do usuário' IDENT ;

// que tem, de
tamanho : CONECTOR 'tamanho' MODIFICADOR_TAMANHO? NUM SIZE_UNIT ;

// 

// ( Encontre | Delete | Liste )
// (
//       (um (arquivo  | diretorio  | arquivo ou diretorio))
//     | (os (arquivos | diretorios | arquivos ou diretorios ))
// )
// (
//     | (
//            ( conector? ) que, o qual
//            (
//                   foi ( acessado | alterado )
//                   ( ah mais de | ah menos de | ah | ah exatamente )
//                   ( NUM TIME_UNIT )
//            )
//     )
//     | (
//            (
//                   vazio | executavel
//            )
//     )
//     | (
//            ( conector? ) com
//            (
//                     (nome | path) do tipo PATTERN
//                   | (nome | path) que começa com PATTERN
//                   | (nome | path) que termina com PATTERN
//            )
//     )
//     | (
//            ( conector? ) com
//            (
//                   permissão PATTERN
//            )
//     )
//     | (
//            ( conector? ) que, o qual, tal que
//            (
//                     pode ser lido
//                   | pode ser escrito
//            )
//     )
//     | (
//            (
//                   do usuário NAME
//            )
//     )
//     | (
//            ( conector? ) que tem, de
//            (
//                   tamanho ( maior que | menor que | exatamente | "" ) NUM SIZE_UNIT 
//            )
//     )
// )

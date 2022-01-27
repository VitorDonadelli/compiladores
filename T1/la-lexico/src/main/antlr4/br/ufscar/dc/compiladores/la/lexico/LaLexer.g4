lexer grammar LaLexer;

/* Generalização das palavras reservadas*/ 
TOKEN
        : PALAVRA_CHAVE
        | DECLARACAO
        | OPERADOR_LOGICO
        | VALOR_BOOLEANO
        | OPERADOR_COMPARACAO
        | OPERADOR_ARITMETICO
        | CARACTERES;

/* Palavras-chaves gerais*/ 
fragment
PALAVRA_CHAVE
	: 'algoritmo'
	| 'fim_algoritmo'
	| 'ate'
	| 'escreva'
	| 'leia'
	| 'faca'
	| 'seja'
	| 'logico'
	| 'retorne'
	| 'entao'
	| 'enquanto'
	| 'fim_enquanto'
	| 'para'
	| 'fim_para'
	| 'caso'
	| 'fim_caso'
	| 'se'
	| 'senao'
	| 'fim_se'
	| 'funcao'
	| 'fim_funcao'
	| 'registro'
	| 'fim_registro'
	| 'procedimento'
	| 'fim_procedimento';

/* Declarações e tipos de variaveis*/ 
fragment
DECLARACAO
	: 'declare'
	| 'tipo'
	| 'var'
	| 'literal'
	| 'constante'
	| 'inteiro'
	| 'real';

/* Operadores logicos*/ 
fragment
OPERADOR_LOGICO
	: 'e'
	| 'ou'
	| 'nao';

/* Valores booleanos */ 
fragment
VALOR_BOOLEANO
        : 'verdadeiro'
	| 'falso';

/* Operadores relacionais*/ 
fragment
OPERADOR_COMPARACAO
	: '='
	| '<>'
	| '<'
	| '<='
	| '>'
	| '>='
	| '<-';

/* Operadores aritméticos*/ 
fragment
OPERADOR_ARITMETICO
	: '+'
	| '-'
	| '/'
	| '%'
	| '*'
	| '^'
	| '&';

/* Caracteres especiais*/ 
fragment
CARACTERES
	: '.'
	| '..'
	| ':'
	| ','
	| '('
	| ')'
	| '['
	| ']';

/* Identificadores, numeros reais e numeros inteiros*/ 
IDENT: ('a'..'z'|'A'..'Z'|'_')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;
NUM_INT: ('0'..'9')+;
NUM_REAL: ('0'..'9')+ '.' ('0'..'9')+;

/* Simbolos a serem ignorados*/ 
WS: ( ' ' | '\t' | '\r' | '\n' );

/* Comentários que leva em consideração somente texto dentro de um par de chaves { ... } */ 
COMENTARIO:             '{' ( ~( '{' | '}' | '\n' ) )* '}';
COMENTARIO_NAO_FECHADO: '{' ( ~( '{' | '}'        ) )* '\n';

/* Cadeia de caracteres */ 
CADEIA:             '"' ( '\\"' | ~( '"' | '\n' ) )* '"';
CADEIA_NAO_FECHADA: '"' ( '\\"' | ~( '"'        ) )* '\n';

/* Caso não seja identificado como alguma outra definição acima, cai aqui*/ 
SIMBOLO_NAO_IDENTIFICADO: .;
lexer grammar LaLexer;

TOKEN
        : PALAVRA_CHAVE
        | DECLARACAO
        | OPERADOR_LOGICO
        | VALOR_BOOLEANO
        | OPERADOR_COMPARACAO
        | OPERADOR_ARITMETICO
        | CARACTERES;

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

fragment
DECLARACAO
	: 'declare'
	| 'tipo'
	| 'var'
	| 'literal'
	| 'constante'
	| 'inteiro'
	| 'real';

fragment
OPERADOR_LOGICO
	: 'e'
	| 'ou'
	| 'nao';

fragment
VALOR_BOOLEANO
        : 'verdadeiro'
	| 'falso';

fragment
OPERADOR_COMPARACAO
	: '='
	| '<>'
	| '<'
	| '<='
	| '>'
	| '>='
	| '<-';

fragment
OPERADOR_ARITMETICO
	: '+'
	| '-'
	| '/'
	| '%'
	| '*'
	| '^'
	| '&';

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

IDENT: ('a'..'z'|'A'..'Z'|'_')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;

NUM_INT: ('0'..'9')+;

NUM_REAL: ('0'..'9')+ '.' ('0'..'9')+;

WS: ( ' ' | '\t' | '\r' | '\n' );

COMENTARIO:             '{' ( ~( '{' | '}' | '\n' ) )* '}';
COMENTARIO_NAO_FECHADO: '{' ( ~( '{' | '}'        ) )* '\n';

CADEIA:             '"' ( '\\"' | ~( '"' | '\n' ) )* '"';
CADEIA_NAO_FECHADA: '"' ( '\\"' | ~( '"'        ) )* '\n';

SIMBOLO_NAO_IDENTIFICADO: .;
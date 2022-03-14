grammar La;

/* Lexico */

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
WS: ( ' ' | '\n' | '\r' | '\t' ) -> skip;

/* Comentários que leva em consideração somente texto dentro de um par de chaves { ... } */ 
COMENTARIO:             '{' ( ~( '{' | '}' | '\n' ) )* '}' -> skip;
COMENTARIO_NAO_FECHADO: '{' ( ~( '{' | '}'        ) )* '\n';

/* Cadeia de caracteres */ 
CADEIA:             '"' ( '\\"' | ~( '"' | '\n' ) )* '"';
CADEIA_NAO_FECHADA: '"' ( '\\"' | ~( '"'        ) )* '\n';

/* Caso não seja identificado como alguma outra definição acima, cai aqui*/ 
SIMBOLO_NAO_IDENTIFICADO: .;


/* Sintatico */


/* Regra principal */
programa: declaracoes 'algoritmo' corpo 'fim_algoritmo' EOF;

/* Declarações */
declaracoes: (decl_local_global)*;

decl_local_global
        :   declaracao_local 
        |   declaracao_global;

declaracao_local
        :   'declare' variavel
        |   'constante' identConstante=IDENT ':' tipo_basico '=' valor_constante
        |   'tipo' identTipo=IDENT ':' tipo;

/* Variavel e identificador */
variavel: identificador (',' identificador)*  ':' tipo;       

identificador: ident1=IDENT ('.' ident2+=IDENT)* dimensao;

/* Dimensão */
dimensao: ('[' exp_aritmetica ']')*;

/* Regras sobre o tipo e valor de constantes */
tipo
        : registro 
        | tipo_estendido;

tipo_basico
        :   'literal' 
        |   'inteiro' 
        |   'real' 
        |   'logico';

tipo_basico_ident
        :   tipo_basico 
        |   IDENT;

tipo_estendido: ponteiro='^'? tipo_basico_ident;

valor_constante
        :   CADEIA 
        |   NUM_INT 
        |   NUM_REAL 
        |   'verdadeiro' 
        |   'falso';

registro: 'registro' (variavel)* 'fim_registro';

/* Declarações Global */
declaracao_global
        :   'procedimento' IDENT '(' (parametros)? ')' (declaracao_local)* (cmd)* 'fim_procedimento'
        |   'funcao' IDENT '(' (parametros)? ')' ':' tipo_estendido (declaracao_local)* (cmd)* 'fim_funcao';

/* Parametros */
parametro: ('var')? identificador (',' identificador)* ':' tipo_estendido;

parametros: parametro (',' parametro)*;

/* Corpo */
corpo: (declaracao_local)* (cmd)*;

/* Comandos */
cmd
        :   cmdLeia 
        |   cmdEscreva
        |   cmdSe
        |   cmdCaso
        |   cmdPara
        |   cmdEnquanto
        |   cmdFaca
        |   cmdAtribuicao
        |   cmdChamada
        |   cmdRetorne;

cmdLeia: 'leia' '(' ('^')? identificador (',' ('^')? identificador)* ')';

cmdEscreva: 'escreva' '(' expressao (',' expressao)* ')';

cmdSe: 'se' expressao 'entao' cmdIf+=cmd* ('senao' cmdElse+=cmd*)? 'fim_se';

cmdCaso: 'caso' exp_aritmetica 'seja' selecao ('senao' (cmd)*)? 'fim_caso';

cmdPara: 'para' IDENT '<-' exp_aritmetica 'ate' exp_aritmetica 'faca' (cmd)* 'fim_para';

cmdEnquanto: 'enquanto' expressao 'faca' (cmd)* 'fim_enquanto';

cmdFaca: 'faca' (cmd)* 'ate' expressao;

cmdAtribuicao: (ponteiro='^')? identificador '<-' expressao;

cmdChamada: IDENT '(' expressao (',' expressao)* ')';

cmdRetorne: 'retorne' expressao;


/* Seleção */
selecao: (item_selecao)*;

item_selecao: constantes ':' (cmd)*;


/* Expressões e operadores */
constantes: constantesIntervalo=numero_intervalo (',' constantesIntervalo2=numero_intervalo)*;

numero_intervalo: (op_unario)? intervaloInicio=NUM_INT ('..' (op_unario)? intervaloFim=NUM_INT)?;

op_unario: '-';

exp_aritmetica: termo (op1 termo)*;

termo: fator (op2 fator)*;

fator: parcela (op3 parcela)*;

op1
        : '+' 
        | '-';

op2
        : '*' 
        | '/';

op3: '%';

/* Parcela */
parcela: (op_unario)? parcela_unario | parcela_nao_unario;

parcela_unario
        :   ('^')? identificador
        |   IDENT '(' expressao (',' expressao)* ')'
        |   NUM_INT
        |   NUM_REAL
        | '(' parcelaUnarioExp=expressao ')';

parcela_nao_unario: endereco='&' identificador | CADEIA;

/* Expressões e operadores relacionais */
exp_relacional: exp_aritmetica (op_relacional exp_aritmetica)?;

op_relacional
        :   '='
        |   '<>'
        |   '>='
        |   '<='
        |   '>'
        |   '<';

expressao: termo_logico (op_logico_1 termo_logico)*;

termo_logico: fator_logico (op_logico_2 fator_logico)*;

fator_logico: 'nao'? parcela_logica;

parcela_logica
        :   'verdadeiro'
        |   'falso'
        |   exp_relacional;

op_logico_1: 'ou';

op_logico_2: 'e';
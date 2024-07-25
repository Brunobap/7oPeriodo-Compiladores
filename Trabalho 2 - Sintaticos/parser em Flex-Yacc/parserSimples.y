%token T_PROGRAMA T_INICIO T_FIM T_LEIA T_ESCREVA T_SE T_ENTAO T_SENAO T_FIMSE T_ENQTO T_FACA T_FIMENQTO T_MAIS T_MENOS T_VEZES T_DIV T_MAIOR T_MENOR T_IGUAL T_E T_OU T_NAO T_ATRIB T_FECHA T_INTEIRO T_LOGICO T_V T_F T_IDENTIF T_NUMERO T_ABRE

%{
	int yylex(void);
	void  yyerror(char *);
	#include <stdio.h>
%}

%%
	program:
		cabecalho variaveis T_INICIO lista_comandos T_FIM { printf("O arquivo esta correto"); } ;
	
	cabecalho: T_PROGRAMA T_IDENTIF ;

	variaveis: declaracao_variaveis | ;

	declaracao_variaveis:
		tipo lista_variaveis declaracao_variaveis
		| tipo lista_variaveis
		;

	tipo: T_LOGICO | T_INTEIRO

	lista_variaveis:
		T_IDENTIF lista_variaveis
		| T_IDENTIF

	lista_comandos: comando lista_comandos | ;

	comando:
		entrada_saida | repeticao |
		selecao | atribuicao

	entrada_saida: leitura | escrita ;

	leitura: T_LEIA T_IDENTIF ;

	escrita: T_ESCREVA expressao ;

	repeticao:
		T_ENQTO expressao T_FACA 
		lista_comandos T_FIMENQTO ;

	selecao:
		T_SE expressao T_ENTAO lista_comandos
		T_SENAO lista_comandos T_FIMSE ;

	atribuicao: T_IDENTIF T_ATRIB expressao ;

	expressao:
		expressao T_VEZES expressao
		| expressao T_DIV expressao
		| expressao T_MAIS expressao
		| expressao T_MENOS expressao
		| expressao T_MAIOR expressao
		| expressao T_MENOR expressao
		| expressao T_IGUAL expressao
		| expressao T_E expressao
		| expressao T_OU expressao
		| termo
		;

	termo:
		T_IDENTIF | T_NUMERO | T_V | T_F
		| T_NAO termo | T_ABRE expressao T_FECHA
		;
%%


void yyerror(char *s) {
	printf("%s\n",s);
}

int main(void) {
	yyparse();
	return 0;
}
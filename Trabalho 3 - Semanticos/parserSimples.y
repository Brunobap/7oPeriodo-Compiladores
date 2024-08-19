%token T_PROGRAMA T_INICIO T_FIM T_LEIA T_ESCREVA T_SE T_ENTAO T_SENAO T_FIMSE T_ENQTO T_FACA T_FIMENQTO T_MAIS T_MENOS T_VEZES T_DIV T_MAIOR T_MENOR T_IGUAL T_E T_OU T_NAO T_ATRIB T_FECHA T_INTEIRO T_LOGICO T_V T_F T_IDENTIF T_NUMERO T_ABRE

// Precedência e associatividade
%left T_E T_OU
%left T_IGUAL
%left T_MAIOR T_MENOR
%left T_MAIS T_MENOS
%left T_VEZES T_DIV

%{
	#include <stdlib.h>
	#include <stdio.h>
	#include "utilitarias.h"
	#include "analisadorSimples.c"

    void yyerror(char *);
    int yylex(void);

    extern FILE* yyin;

%}

%%
	program:
		cabecalho { printf("\tINPP\n"); }
		variaveis { aux2 = -1; if (CONTA_VARS != 0) printf("\tAMEM\t%d\n",CONTA_VARS-1); }
		T_INICIO lista_comandos
		T_FIM { printf("\tFIMP\n"); return 1; }
		;
	
	cabecalho: T_PROGRAMA T_IDENTIF { insere_variavel(atomo[--aux1]); empilha(T_PROGRAMA); } ;

	variaveis: declaracao_variaveis | ;

	declaracao_variaveis:
		tipo lista_variaveis declaracao_variaveis
		| tipo lista_variaveis
		;

	tipo: 
		T_LOGICO { aux2 = T_LOGICO; }
		| T_INTEIRO { aux2 = T_NUMERO; }
		;

	lista_variaveis:
		T_IDENTIF {	insere_variavel(atomo[--aux1]); empilha(aux2); } lista_variaveis
		| T_IDENTIF { insere_variavel(atomo[--aux1]); empilha(aux2); }
		;

	lista_comandos: comando lista_comandos | ;

	comando:
		entrada_saida | repeticao | selecao | atribuicao ;

	entrada_saida: leitura | escrita ;

	leitura: 
		T_LEIA 	{ printf("\tLEIA\n"); }
		T_IDENTIF {
			int deslocamento = busca_retorna_simbolo(atomo[--aux1]).desloca;
			printf("\tARZG\t%d\n",deslocamento);
		};

	escrita: 
		T_ESCREVA expressao { printf("\tESCR\n"); } ;

	repeticao:
		T_ENQTO { 
			aux2++;
			aux4[aux2] = ROTULO;
			printf("L%d\tNADA\n",aux4[aux2]); 
			ROTULO += 2; }
		expressao { 
			int tipoExp = desempilha();
			if (tipoExp != T_LOGICO) ERRO("Operação lógica com tipos numéricos");
			printf("\tDSVF\tL%d\n",aux4[aux2]+1); }
		T_FACA lista_comandos 
		T_FIMENQTO { 
			printf("\tDSVS\tL%d\n",aux4[aux2]);
			printf("L%d\tNADA\n",aux4[aux2]+1);
			aux2--; }
		;

	selecao:
		T_SE { aux2++; } 
		expressao { 
			int tipoExp = desempilha();
			if (tipoExp != T_LOGICO) ERRO("Operação lógica com tipos numéricos");
			aux4[aux2] = ROTULO; 
			ROTULO+=2;
			printf("\tDSVF\tL%d\n",aux4[aux2]); }
		T_ENTAO lista_comandos T_SENAO { 
			printf("\tDSVS\tL%d\n",aux4[aux2]+1);
			printf("L%d\tNADA\n",aux4[aux2]); }
		lista_comandos T_FIMSE { 
			printf("L%d\tNADA\n",aux4[aux2]+1); 
			aux2--; }
		;

	atribuicao: 
		T_IDENTIF T_ATRIB expressao {
			int deslocamento = busca_retorna_simbolo(atomo[--aux1]).desloca;
			printf("\tARZG\t%d\n",deslocamento);
		} ;

	expressao:
		expressao T_VEZES expressao {
			int t1 = desempilha(), t2 = desempilha();
			if (t1 != t2) ERRO("Operação com tipos diferentes");
			if (t1 != T_NUMERO) ERRO("Operação aritmética com tipos estranhos");
			printf("\tMULT\n");
			empilha(T_NUMERO); }

		| expressao T_DIV expressao {
			int t1 = desempilha(), t2 = desempilha();
			if (t1 != t2) ERRO("Operação com tipos diferentes");
			if (t1 != T_NUMERO) ERRO("Operação aritmética com tipos estranhos");
			printf("\tDIVI\n");
			empilha(T_NUMERO); }

		| expressao T_MAIS expressao {
			int t1 = desempilha(), t2 = desempilha();
			if (t1 != t2) ERRO("Operação com tipos diferentes");
			if (t1 != T_NUMERO) ERRO("Operação aritmética com tipos estranhos");
			printf("\tSOMA\n");
			empilha(T_NUMERO); }

		| expressao T_MENOS expressao {
			int t1 = desempilha(), t2 = desempilha();
			if (t1 != t2) ERRO("Operação com tipos diferentes");
			if (t1 != T_NUMERO) ERRO("Operação aritmética com tipos estranhos");
			printf("\tSUBT\n");
			empilha(T_NUMERO); }

		| expressao T_MAIOR expressao {
			int t1 = desempilha(), t2 = desempilha();
			if (t1 != t2) ERRO("Operação com tipos diferentes");
			if (t1 != T_NUMERO) ERRO("Comparação com tipos estranhos");
			printf("\tCMMA\n");
			empilha(T_LOGICO); }

		| expressao T_MENOR expressao {
			int t1 = desempilha(), t2 = desempilha();
			if (t1 != t2) ERRO("Operação com tipos diferentes");
			if (t1 != T_NUMERO) ERRO("Comparação com tipos estranhos");
			printf("\tCMME\n");
			empilha(T_LOGICO); }

		| expressao T_IGUAL expressao {
			int t1 = desempilha(), t2 = desempilha();
			if (t1 != t2) ERRO("Operação com tipos diferentes");
			if (t1 != T_NUMERO) ERRO("Comparação com tipos estranhos");
			printf("\tCMIG\n");
			empilha(T_LOGICO); }

		| expressao T_E expressao {
			int t1 = desempilha(), t2 = desempilha();
			if (t1 != t2) ERRO("Operação com tipos diferentes");
			if (t1 != T_LOGICO) ERRO("Operação lógica com tipos estranhos");
			printf("\tCONJ\n");
			empilha(T_LOGICO); }

		| expressao T_OU expressao {
			int t1 = desempilha(), t2 = desempilha();
			if (t1 != t2) ERRO("Operação com tipos diferentes");
			if (t1 != T_LOGICO) ERRO("Operação lógica com tipos numéricos");
			printf("\tDISJ\n");
			empilha(T_LOGICO); }

		| termo ;

	termo:
		T_IDENTIF {
			int var = busca_retorna_simbolo(atomo[--aux1]).desloca;
			printf("\tCRVG\t%d\n",var);
			empilha(PSEMA[var]); }

		| T_NUMERO { 
			printf("\tCRCT\t%d\n",aux3); 
			empilha(T_NUMERO); }

		| T_V { 
			printf("\tCRCT\t1\n"); 
			empilha(T_LOGICO); }

		| T_F { 
			printf("\tCRCT\t0\n"); 
			empilha(T_LOGICO); }

		| T_NAO termo { 
			int t1 = desempilha();
			if (t1 != T_LOGICO) ERRO("Negação com tipos estranhos");
			empilha(T_LOGICO);
			printf("\tNEGA\n"); }

		| T_ABRE expressao T_FECHA ;
%%


void yyerror(char *s) {
	printf("%s\n",s);
}

void main(int argc, char **argv) {
  yyin = fopen(argv[1], "r");
  yyparse();
  fclose(yyin);
}
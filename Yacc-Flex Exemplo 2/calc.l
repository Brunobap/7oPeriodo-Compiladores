%{
	#include <stdlib.h>
	#include "y.tab.h"
	void yyerror(char *);
%}

%%

	/* variáveis */
[a-z]	{
		yylval = *yytext -'a';
		return VARIABLE;
	}


	/* inteiros */
[0-9]+	{
		yylval = atoi(yytext);
		return INTEGER;
	}


	/* operadores */
[-+()/*\n]	{ return *yytext; }


	/* pular espaços em branco */
[\t]	;


	/* qualquer coisa diferente dá erro */
.	yyerror("caracter invalido");

%%

int yywrap(void) {
	return 1;
}

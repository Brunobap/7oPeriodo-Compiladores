identificador [a-zA-Z][a-zA-Z0-9]*
numero [0-9]*

%{
	int conta = 0;
%}

%%
{identificador}	printf("count = %d\n", ++conta);
%%

int yywrap(void) {
	return 1;
}

int main(char *argv[]) {
	//yyin = fopen(argv[1], "r");
	yylex();
	//printf("Numero de IDs = %d\n", count);
	//fclose(yyin);
	return 0;
}

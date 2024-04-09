identificador   [a-zA-Z]([a-zA-Z0-9])*
numero          [0-9]+
espaco          [\t ]+
novalinha       [\n\r]

/* comentario de bloco */
comentarioBlc	"/*"([^*]|"*"+[^*/])*"*"+"/"

/* comentario de uma linha */
comentarioLin	"//"(.)*(\n|\r)+

%{
	int T_PROGRAMA = 1, T_INICIO = 2, T_FIM = 3, T_LEIA = 4, T_ESCREVA = 5, T_SE = 6, T_ENTAO = 7, T_SENAO = 8, T_FIMSE = 9, T_ENQTO = 10, T_FACA = 11, T_FIMENQTO = 12, T_MAIS = 13, T_MENOS = 14, T_VEZES = 15, T_DIV = 16, T_MAIOR = 17, T_MENOR = 18, T_IGUAL = 19, T_E = 20, T_OU = 21, T_NAO = 22, T_ATRIB = 23, T_FECHA = 24, T_INTEIRO = 25, T_LOGICO = 26, T_V = 27, T_F = 28, T_IDENTIF = 29, T_NUMERO = 30, T_ABRE = 31;
%}

%%
programa      	{printf("%11s: T_PROGRAMA\n", yytext); return T_PROGRAMA;}
inicio          {printf("%11s: T_INICIO\n", yytext); return T_INICIO;}
fimprograma     {printf("%11s: T_FIM\n", yytext); return T_FIM;}

leia            {printf("%11s: T_LEIA\n", yytext); return T_LEIA;}
escreva         {printf("%11s: T_ESCREVA\n", yytext); return T_ESCREVA;}

se              {printf("%11s: T_SE\n", yytext); return T_SE;}
entao           {printf("%11s: T_ENTAO\n", yytext); return T_ENTAO;}
senao           {printf("%11s: T_SENAO\n", yytext); return T_SENAO;}
fimse           {printf("%11s: T_FIMSE\n", yytext); return T_FIMSE;}

enquanto        {printf("%11s: T_ENQTO\n", yytext); return T_ENQTO;}
faca            {printf("%11s: T_FACA\n", yytext); return T_FACA;}
fimenquanto     {printf("%11s: T_FIMENQTO\n", yytext); return T_FIMENQTO;}

"+"             {printf("%11s: T_MAIS\n", yytext); return T_MAIS;}
"-"             {printf("%11s: T_MENOS\n", yytext); return T_MENOS;}
"*"             {printf("%11s: T_VEZES\n", yytext); return T_VEZES;}
div             {printf("%11s: T_DIV\n", yytext); return T_DIV;}

">"             {printf("%11s: T_MAIOR\n", yytext); return T_MAIOR;}
"<"             {printf("%11s: T_MENOR\n", yytext); return T_MENOR;}
"="             {printf("%11s: T_IGUAL\n", yytext); return T_IGUAL;}

"e"             {printf("%11s: T_E\n", yytext); return T_E;}
"ou"            {printf("%11s: T_OU\n", yytext); return T_OU;}
"nao"           {printf("%11s: T_NAO\n", yytext); return T_NAO;}

"<-"            {printf("%11s: T_ATRIB\n", yytext); return T_ATRIB;}
"("             {printf("%11s: T_ABRE\n", yytext); return T_ABRE;}
")"             {printf("%11s: T_FECHA\n", yytext); return T_FECHA;}

inteiro         {printf("%11s: T_INTEIRO\n", yytext); return T_INTEIRO;}
logico          {printf("%11s: T_LOGICO\n", yytext); return T_LOGICO;}
V               {printf("%11s: T_V\n", yytext); return T_V;}
F               {printf("%11s: T_F\n", yytext); return T_F;}

{identificador} {printf("%11s: T_IDENTIF\n", yytext); return T_IDENTIF;}
{numero}        {printf("%11s: T_NUMERO\n", yytext); return T_NUMERO;}
{espaco}        /* nao faz nada */
{novalinha}     /* nao faz nada */

{comentarioBlc}	/* ignorar */
{comentarioLin}	/* ignorar */

.               {printf("%d ERRO LEXICO\n", yytext[0]); return -1;}

%%

int yywrap(void) { return 1; }

int main() {
    yyin = fopen("../avaliacao.simples","r");
    int lastTag = 0;
    do {
    	lastTag = yylex();
    	if (lastTag) printf("\t\tTag: %d\n",lastTag);
    } while (lastTag);
    fclose(yyin);
    return 0;
}

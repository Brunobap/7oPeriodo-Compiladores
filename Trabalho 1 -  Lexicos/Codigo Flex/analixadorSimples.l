identificador   [a-zA-Z]([a-zA-Z0-9])*
numero          [0-9]+
espaco          [\t ]+
novalinha       [\n]

/* comentario de bloco */
comentarioBlc	[/][*](.|\n)*[*][/]

/* comentario de uma linha */
comentarioLin	[/][/](.)*[\n]

%%

programa        printf("%11s: T_PROGRAMA\n", yytext);
inicio          printf("%11s: T_INICIO\n", yytext);
fimprograma     printf("%11s: T_FIM\n", yytext);

leia            printf("%11s: T_LEIA\n", yytext);
escreva         printf("%11s: T_ESCREVA\n", yytext);

se              printf("%11s: T_SE\n", yytext);
entao           printf("%11s: T_ENTAO\n", yytext);
senao           printf("%11s: T_SENAO\n", yytext);
fimse           printf("%11s: T_FIMSE\n", yytext);

enquanto        printf("%11s: T_ENQTO\n", yytext);
faca            printf("%11s: T_FACA\n", yytext);
fimenquanto     printf("%11s: T_FIMENQTO\n", yytext);

"+"             printf("%11s: T_MAIS\n", yytext);
"-"             printf("%11s: T_MENOS\n", yytext);
"*"             printf("%11s: T_VEZES\n", yytext);
div             printf("%11s: T_DIV\n", yytext);

">"             printf("%11s: T_MAIOR\n", yytext);
"<"             printf("%11s: T_MENOR\n", yytext);
"="             printf("%11s: T_IGUAL\n", yytext);

"e"             printf("%11s: T_E\n", yytext);
"ou"            printf("%11s: T_OU\n", yytext);
"nao"           printf("%11s: T_NAO\n", yytext);

"<-"            printf("%11s: T_ATRIB\n", yytext);
"("             printf("%11s: T_ABRE\n", yytext);
")"             printf("%11s: T_FECHA\n", yytext);

inteiro         printf("%11s: T_INTEIRO\n", yytext);
logico          printf("%11s: T_LOGICO\n", yytext);
V               printf("%11s: T_V\n", yytext);
F               printf("%11s: T_F\n", yytext);

{identificador} printf("%11s: T_IDENTIF\n", yytext);
{numero}        printf("%11s: T_NUMERO\n", yytext);
{espaco}        /* nao faz nada */
{novalinha}     /* nao faz nada */
.               printf("%s ERRO LEXICO\n", yytext);

{comentarioBlc}	/* ignorar */
{comentarioLin}	/* ignorar */

%%

int yywrap(void) { return 1; }

int main(void) {
    yyin = fopen("../teste.simples","r");
    yylex();
    fclose(yyin);
    return 0;
}

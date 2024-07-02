#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define NSIMBOLOS 7
#define NREGRAS 4
#define NESTADOS 9
char alfabeto[NSIMBOLOS+1] = "SLa[];#";
char *regras[NREGRAS] = {
    "S::=a",
    "S::=[L]",
    "S::=S",
    "S::=L;S",
};

struct {
    char acao;
    int indice;
} TabSint [NESTADOS][NSIMBOLOS] = {
    'e',1, ' ',0, 'e',2, 'e',3, ' ',0, ' ',0, ' ',0,
    ' ',0, ' ',0, ' ',0, ' ',0, ' ',0, ' ',0, ' ',0,
    ' ',0, ' ',0, 'r',1, 'r',1, 'r',1, 'r',1, 'r',1,
    'e',4, 'e',5, 'e',2, 'e',3, ' ',0, ' ',0, ' ',0,
    ' ',0, ' ',0, 'r',3, 'r',3, 'r',3, 'r',3, 'r',3,
    ' ',0, ' ',0, ' ',0, ' ',0, 'e',6, 'e',7, ' ',0,
    ' ',0, ' ',0, 'r',2, 'r',2, 'r',2, 'r',2, 'r',2,
    'e',8, ' ',0, 'e',2, 'e',3, ' ',0, ' ',0, ' ',0,
    ' ',0, ' ',0, 'r',4, 'r',4, 'r',4, 'r',4, 'r',4,
}

struct {
    char elem;
    int ind;
} P[20];

void traco(int);
void strins(char *, int, char *, int);

int main() {
    int i,j,k, termino, reduzido, indice, ind, tam, passo, nreducao, indreduz = -1, reducoes[50];
    char sentenca[40], pilha[60], cadeia[40], str[40];
    char s, simboloreduzido = ' ', acao;

    P[0].elem = 'e';
    P[0].ind = 0;
    i = termino = reduzido = 0;

    printf("\nDigite a sentenca: ");
    gets(sentenca);

    strcat(sentenca,"#");
    indice = 0;
    passo = 0;
    printf("PASSO %-30s S.R. %-15s  %s\n","PILHA", "SENTENCA", "ACAO");

    traco(79);
    while(!termino) {
        if (reduzido) s =simboloreduzido;
        else s = sentenca[indice];

        for (j=0; alfabeto[j] != s && j <= strlen(alfabeto); j++);

        if (alfabeto[j] != s) {
            printf("\nERRO: o simbolo <%c> nao e reconhecido nesta linguagem", s);
            printf("\n\nDigite '.' para terminar!");
            while (getchar() != '.');
            exit(10);
        }

        acao = TabSint[P[i].ind][j].acao;
        ind = TabSint[P[i].ind][j].indice;

        for (j=0, k=indice; sentenca[k]; j++, k++) cadeia[j] = sentenca[k];

        cadeia[j] = '\0';
        strcpy(pilha, "");
        for (k=0; k <= i; k++) {
            sprintf(str, "%c%d", P[k].elem, P[k].ind);
            strcat(pilha, str);
        }
        printf("%3d   %-30s %c   %-15s  %c%d\n", passo++, pilha, simboloreduzido, cadeia, acao, ind);

        switch(acao){
            case 'e':
                i++;
                P[i].elem = s;
                P[i].ind = ind;
                if (reduzido) {
                    reduzido = 0;
                    simboloreduzido = ' ';
                } else indice++;
                break;

            case 'r':
                tam = strlen(regras[ind-1]);
                i = i - tam +4;
                reduzido = 1;
                reducoes[++indreduz] = ind-1;
                simboloreduzido = regras[ind-1][0];
                break;

            case 'a':
                termino = 1;
                printf("\nA sentenca <%s> esta correta",sentenca);
                break;

            case ' ':
                printf("\nA sentenca <%s> NAO e reconhecida", sentenca);
                printf("\n\nDigite '.' para terminar!");
                while (getchar() != '.');
                exit(1);
        }
    }
}

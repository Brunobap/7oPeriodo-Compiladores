#include <stdio.h>
#include <string.h>
#include <stdarg.h>
#include <stdlib.h>

// Limites das estruturas
#define TAM_TSIMB 100 // Tamanho da tabela de simbolos
#define TAM_PSEMA 100 // Tamanho da pilha semantica

// Variáveis globais
int TOPO_TSIMB = 0; // TOPO da tabela de simbolos
int TOPO_PSEMA = 0; // TOPO da pilha semantica
int ROTULO = 0; // Proximo numero de rotulo
int CONTA_VARS = 0; // Núm. de variáveis
int POS_SIMB; // Pos. na tabela de simbolos
int aux1 = 0, aux2 = 0, aux3 = 0; // Variáveis auxiliares
int numLinha = 1; // Núm. da linha no programa
char atomo[255][30]; // nome de um ident. ou numero

// Rotina geral de tratamento de erro
void ERRO (char *msg, ...){
    char formato[255];
    va_list arg;
    va_start(arg,msg);
    sprintf(formato, "\n%d: ", numLinha);
    strcat(formato, msg);
    strcat(formato, "\n\n");
    printf("\nERRO no programa");
    vprintf(formato, arg);
    va_end(arg);
    exit(1);
}

// Tabela de Simbolos
struct simbolo{
    char id[30];
    int tipo;
    int desloca;
} TSIMB [TAM_TSIMB], elem_tab;

// Pilha Semantica
int PSEMA[TAM_PSEMA];

/*
Função que BUSCA um simbolo na tabela de simbolos.
    Retorna -1 se o simbolo não está na tabela
    Retorna i, onde i é o indice do simbolo na tabela se o simbolo está na tabela
*/
int busca_simbolo(char *ident){
    int i = TOPO_TSIMB-1;
    for (; strcmp(TSIMB[i].id, ident) && i >= 0; i--);
    return i;
}
struct simbolo busca_retorna_simbolo(char *ident){
    int i = TOPO_TSIMB-1;
    for (; strcmp(TSIMB[i].id, ident) && i >= 0; i--);

    if (i!=-1) return TSIMB[i];
    else ERRO("Simbolo desconhecido: [%s] ",ident);
}

/*
Função que INSERE um simbolo na tabela de simbolos.
    Se já existe um simbolo com mesmo nome e mesmo nível uma mensagem de erro é apresentada e o programa é interrompido.
*/
void insere_simbolo(struct simbolo *elem){
    if (TOPO_TSIMB == TAM_TSIMB)
        ERRO("OVERFLOW - tabela de simbolos");
    else {
        POS_SIMB = busca_simbolo(elem->id);
        if (POS_SIMB != -1)
            ERRO("Identificador [%s] duplicado", elem->id);
        TSIMB[TOPO_TSIMB] = *elem;
        TOPO_TSIMB++;
    }
}

// Função de inserção de uma variável na tabela de simbolos
void insere_variavel(char *ident, int tipo){
    strcpy(elem_tab.id, ident);
    elem_tab.desloca = CONTA_VARS;
    elem_tab.tipo = tipo;
    insere_simbolo(&elem_tab);
    CONTA_VARS++;
}

// Rotinas para manutenção da PILHA SEMANTICA
void empilha(int n) {
    if (TOPO_PSEMA == TAM_PSEMA)
        ERRO("OVERFLOW - Pilha Semantica");
    PSEMA[TOPO_PSEMA++] = n;
}
int desempilha() {
    if (TOPO_PSEMA == 0)
        ERRO("UNDERFLOW - Pilha Semantica");
    return PSEMA[--TOPO_PSEMA];
}
#!bin/bash/

yacc -d parserSimples.y
echo "Linha yacc executada"
echo ""

flex analizadorSimples.l
echo "Linha flex executada"
echo ""

gcc -c lex.yy.c y.tab.c
echo "Linha gcc -c executada"
echo ""

gcc -o parser lex.yy.o y.tab.o
echo "Linha gcc -o executada"
echo "Parser gerado com sucesso"
echo ""

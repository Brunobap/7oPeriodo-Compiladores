#!/bin/bash
flex -t analisadorSimples.l > analisadorSimples.c;
echo "Linha flex executada";

bison -v -d parserSimples.y -o parserSimples.c;
echo "Linha bison executada";

gcc parserSimples.c -o parserFinal;
echo "Linha gcc executada
";
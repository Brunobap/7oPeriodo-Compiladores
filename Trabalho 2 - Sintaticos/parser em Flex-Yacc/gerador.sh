#!/bin/bash
yacc -d parserSimples.y
flex analisadorSimples.l
gcc -c y.tab.c lex.yy.c
gcc -o parser.out y.tab.o lex.yy.o
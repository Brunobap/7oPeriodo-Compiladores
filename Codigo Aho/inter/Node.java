package inter;

import lexer.*;

public class Node {
	int lexline = 0;
	Node() {this.lexline = Lexer.line;}
}

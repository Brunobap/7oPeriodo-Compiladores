package inter;

import lexer.*;

public class Node {
	int lexline = 0;
	public Node() {
		this.lexline = Lexer.line;
	}
	void error(String s) {
		throw new Error("near line "+this.lexline+": "+s);
	}
	static int labels = 0;
	public int newlabel() {
		return ++this.labels;
	}
	public void emitlabel(int i) {
		System.out.print("L"+i+":");
	}
	public void emit(String s) {
		System.out.println("\t"+s);
	}
}

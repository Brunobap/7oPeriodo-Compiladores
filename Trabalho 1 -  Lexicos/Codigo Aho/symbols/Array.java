package symbols;

import lexer.*;

public class Array extends Type {
	public Type of;		// array *of* type
	public int size = 1;// number of elements
	public Array(int sz, Type p) {
		super("[]", Tag.NUM, sz*p.width);
		this.size = sz;
		this.of = p;
	}
	public String toString() {
		return "["+this.size+"] "+this.of.toString();
	}	
}

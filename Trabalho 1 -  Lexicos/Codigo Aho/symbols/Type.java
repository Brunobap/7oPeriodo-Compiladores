package symbols;

import lexer.*;

public class Type extends Word {
	public int width = 0;
	public Type(String s, int tag, int w) {
		super(s,tag);
		this.width = w;
	}
	public static final Type 
		Int = new Type("inteiro", Tag.INT, 4),
		Bool = new Type("logico", Tag.LOGIC, 1);
	
	public static boolean numeric(Type p) {
		if (p == Type.Int) return true;
		else return false;
	}
	
	public static Type max(Type p1, Type p2) {
		if (!numeric(p1) || !numeric(p2)) return null;
		else if (p1 == Type.Int || p2 == Type.Int) return Type.Int;
		else return null;
	}
}

package inter;


import lexer.*;
import symbols.*;

public class Rel extends Logical {
	public Rel(Token tok, Expr x1, Expr x2) {
		super(tok, x1, x2);
	}
	
	public Type check(Type t1, Type t2) {
		if (t1 instanceof Array || t2 instanceof Array) return null;
		else if (t1 == t2) return Type.Bool;
		else return null;
	}
	
	public void jumping(int t, int f) {
		Expr a = expr1.reduce(), b = expr2.reduce();
		String test = a.toString()+" "+op.toString()+" "+b.toString();
		emitjumps(test, t, f);
	}
}

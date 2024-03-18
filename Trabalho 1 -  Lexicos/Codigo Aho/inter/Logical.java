package inter;

import lexer.*;
import symbols.*;

public class Logical extends Expr {
	public Expr expr1, expr2;
	
	public Logical(Token tok, Expr x1, Expr x2) {
		super(tok,null);	// null type for start
		this.expr1 = x1;
		this.expr2= x2;
		this.type = check(this.expr1.type, this.expr2.type);
		if (type == null) error("type error");
	}
	
	public Type check(Type t1, Type t2) {
		if (t1 == Type.Bool && t2 == Type.Bool) return Type.Bool;
		else return null;
	}
	
	public Expr gen() {
		int f = newlabel(), a = newlabel();
		Temp temp = new Temp(type);
		this.jumping(0, f);
		emit(temp.toString() + " = true");
		emit("goto L"+a);
		emitlabel(f);
		emit(temp.toString() + "= false");
		emitlabel(a);
		return temp;
	}
	
	public String toString() {
		return expr1.toString()+" "+op.toString()+" "+expr2.toString();
	}
}

package inter;

import lexer.*;
import symbols.*;

public class Unary extends Op {
	public Expr expr;
	
	public Unary(Token tok, Expr x) {	// handles minus, for ! see Not
		super(tok, null);
		this.expr = x;
		this.type = Type.max(Type.Int, expr.type);
		if (type == null) error("type error");
	}
	
	public Expr gen() {
		return new Unary(this.op, this.expr.reduce());
	}
	
	public String toString() {
		return op.toString()+" "+expr.toString();
	}
}

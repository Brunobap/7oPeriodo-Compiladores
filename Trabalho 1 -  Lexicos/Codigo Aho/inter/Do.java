package inter;

import symbols.*;

public class Do extends Stmt {
	Expr expr;
	Stmt stmt;
	
	public Do() {
		this.expr = null;
		this.stmt = null;
	}
	
	public void init(Expr x, Stmt s) {
		this.expr = x;
		this.stmt = s;
		if (expr.type != Type.Bool) expr.error("boolean required in do");
	}

	public void gen(int b, int a) {
		after = a;
		int label = newlabel();	// label for expr
		stmt.gen(b, label);
		emitlabel(label);
		expr.jumping(b, 0);
	}
}

package inter;

import symbols.*;

public class While extends Stmt {
	Expr expr;
	Stmt stmt;
	
	public While() {
		this.expr=null;
		this.stmt=null;
	}
	
	public void init(Expr x, Stmt s) {
		this.expr = x;
		this.stmt = s;
		if (expr.type != Type.Bool)
			expr.error("boolean required in while");
	}
	
	public void gen(int b, int a) {
		after = a;	// save label a
		expr.jumping(0, a);
		int label = newlabel();	//label for stmt
		emitlabel(label);
		stmt.gen(label, b);
		emit("goto L"+b);
		
	}
}

package inter;

import symbols.*;

public class Else extends Stmt {
	Expr expr;
	Stmt stmt1, stmt2;
	
	public Else(Expr x, Stmt s1, Stmt s2) {
		this.expr = x;
		this.stmt1 = s1;
		this.stmt2 = s2;
		if (expr.type != Type.Bool) expr.error("boolean required in if");
	}
	
	public void gen(int b, int a) {
		int label1 = newlabel(), label2 = newlabel();	// label1-2 for stmt1-2
		expr.jumping(0, label2);	// fall through to stmt1 on true
		
		emitlabel(label1);
		stmt1.gen(label1, a);
		emit("goto L"+a);
		
		emitlabel(label2);
		stmt2.gen(label2, a);
	}
}

package inter;

public class Stmt extends Node {
	public Stmt() {}
	
	public static Stmt
		Null = new Stmt(),
		Enclosing = Stmt.Null;	// used for break stmt's
	
	private void gen() {}	// called with labels begin and after
	
	int after = 0;	// saves label after
	
}

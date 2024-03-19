package inter;

import lexer.*;
import symbols.*;

public class Access extends Op {
	public Id array;
	public Expr index;
	
	public Access(Id a, Expr i, Type t) {	// p is element type array after
		super(new Word("[]", Tag.NUM), t);// flattening the array
		this.array = a;
		this.index = i;
	}
	
	public Expr gen() {
		return new Access(this.array, this.index.reduce(), this.type);
	}
	
	public void jumping(int t, int f) {
		emitjumps(reduce().toString(), t, f);
	}
	
	public String toString() {
		return array.toString()+" [ "+index.toString()+" ] "; 
	}
}

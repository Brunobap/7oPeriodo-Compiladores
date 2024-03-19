package parser;

import java.io.*;
import lexer.*;
import symbols.*;
import inter.*;

public class Parser {
	private Lexer lex;	// lexical analyzer for this parser
	private Token look;	// lookhead token
	Env top = null;		// current or top symbol table
	int used = 0;		// storage used for declarations
	
	public Parser(Lexer l) throws IOException {
		this.lex = l;
		move();
	}
	
	void move() throws IOException {
		this.look = lex.scan();
	}
	
	void error(String s) {
		throw new Error("near line "+lex.line+": "+s);
	}
	
	void match(int t) throws IOException {
		if (look.tag == t) move();
		else error("syntax error");
	}
	
	public void program() throws IOException {	// program -> block
		Stmt s = new Stmt();
		/*Stmt s = block();
		int begin = s.newlabel();
		int after = s.newlabel();
		s.emitlabel(begin);
		s.gen(begin, after);
		s.emitlabel(after);*/
		boolean locked = true;
		while (locked) {
			switch(look.tag) {
				// PALAVRAS RESERVADAS 
				case Tag.PROGRAMA:
					s.emit("programa: T_PROGRAMA");
					break;
				case Tag.INICIO:
					s.emit("inicio: T_INICIO");
					break;
				case Tag.FIMPROGRAMA:
					s.emit("fimprograma: T_FIM");
					break;
				case Tag.LEIA:
					s.emit("leia: T_LEIA");
					break;
				case Tag.ESCREVA:
					s.emit("escreva: T_ESCREVA");
					break;
				case Tag.SE:
					s.emit("se: T_SE");
					break;
				case Tag.ENTAO:
					s.emit("entao: T_ENTAO");
					break;
				case Tag.SENAO:
					s.emit("senao: T_SENAO");
					break;
				case Tag.FIMSE:
					s.emit("fimse: T_FIMSE");
					break;
				case Tag.ENQUANTO:
					s.emit("enquanto: T_ENQTO");
					break;
				case Tag.FACA:
					s.emit("faca: T_FACA");
					break;
				case Tag.FIMENQUANTO:
					s.emit("fimenquanto: T_FIMENQTO");
					break;
					
				// COMPARADORES
				case Tag.MENOR:
					s.emit("< : T_MENOR");
					break;
				case Tag.MAIOR:
					s.emit("> : T_MAIOR");
					break;
				case Tag.IGUAL:
					s.emit("= : T_IGUAL");
					break;
					
				// OPERADORES ARITMETICOS
				case '+':
					s.emit("+ : T_MAIS");
					break;
				case '-':
					s.emit("- : T_MENOS");
					break;
				case '/':
					s.emit("* : T_VEZES");
					break;
				case Tag.DIV:
					s.emit("div : T_DIV");
					break;
					
				// OPERADORES BOOLEANOS
				case Tag.E:
					s.emit("e : T_E");
					break;
				case Tag.OU:
					s.emit("ou : T_OU");
					break;
				case Tag.NAO:
					s.emit("nao : T_E");
					break;				
					
				// CARACTERES ESPECIAIS
				case Tag.ATRIB:
					s.emit("<- : T_ATRIB");
					break;
				case Tag.ABREPAR:
					s.emit("( : T_ABRE");
					break;
				case Tag.FECHAPAR:
					s.emit(") : T_FECHA");
					break;
					
				// TIPOS / CONSTANTES
				case Tag.INT:
					s.emit("inteiro : T_INTEIRO");
					break;
				case Tag.LOGIC:
					s.emit("logico : T_LOGIC");
					break;
				case Tag.TRUE:
					s.emit("V : T_V");
					break;
				case Tag.FALSE:
					s.emit("F : T_F");
					break;
					
				// TIPOS DE VARIAVEIS
				case Tag.ID:
					s.emit(look.toString()+": T_IDENTIF");
					break;
				case Tag.NUM:
					s.emit(look.toString()+": T_NUMERO");
					break;
					
			}
			move();
		}
	}
	
	/*Stmt block() throws IOException {	// block -> { decls stmts }
		match('{');
		Env savedEnv = top;
		top = new Env(top);
		decls();
		Stmt s = stmts();
		match('}');
		top = savedEnv;
		return s;
	}
	
	void decls() throws IOException {
		while(look.tag == Tag.BASIC) {	// D -> type ID
			Type t = type();
			Token tok = look;
			match(Tag.ID);
			match(';');
			Id id = new Id((Word)tok, t, used);
			top.put(tok, id);
			used += t.width;
		}
	}
	
	Type type() throws IOException {
		Type t = (Type) look;	// expect look.tag == Tag.BASIC
		match(Tag.BASIC);
		
		if (look.tag != '[') return t;	// T -> basic
		else return dims(t);			// return array type
	}
	
	Type dims(Type t) throws IOException {
		match('[');
		Token tok = look;
		match(Tag.NUM);
		match(']');
		
		if (look.tag == '[') t = dims(t);
		
		return new Array(((Num)tok).value, t);
	}
	
	Stmt stmts() throws IOException {
		if (look.tag == '}') return Stmt.Null;
		else return new Seq(stmt(),stmts());
	}
	
	Stmt stmt() throws IOException {
		Expr x;
		Stmt s, s1, s2;
		Stmt savedStmt;		// save enclosing loop for brackets
		
		switch(look.tag) {
			case ';':
				move();
				return Stmt.Null;
			case Tag.IF:
				match(Tag.IF); match('('); 
				x = bool(); match(')');
				s1 = stmt();
				if (look.tag != Tag.ELSE) return new If(x, s1);
				match(Tag.ELSE);
				s2 = stmt();
				return new Else(x, s1, s2);
			case Tag.WHILE:
				While whilenode = new While();
				savedStmt = Stmt.Enclosing;
				Stmt.Enclosing = whilenode;
				match(Tag.WHILE); match('('); 
				x = bool(); match(')');
				s1 = stmt();
				whilenode.init(x, s1);
				Stmt.Enclosing = savedStmt;	// reset Stmt.Enclosing
				return whilenode;
			case Tag.DO:
				Do donode = new Do();
				savedStmt = Stmt.Enclosing;
				Stmt.Enclosing = donode;
				match(Tag.DO);
				s1 = stmt();
				match(Tag.WHILE); match('('); 
				x = bool(); match(')');
				match(';');
				donode.init(x, s1);
				Stmt.Enclosing = savedStmt;	// reset Stmt.Enclosing
				return donode;
			case Tag.BREAK:
				match(Tag.BREAK);
				match(';');
				return new Break();
			case '{':
				return block();
			default:
				return assign();
		}
	}
	
	Stmt assign() throws IOException {
		Stmt stmt;
		Token t = look;
		
		match(Tag.ID);
		Id id = top.get(t);
		if (id == null)
			error(t.toString()+" undeclared");
		if (look.tag == '=') {	// S -> id = E
			move();
			stmt = new Set(id,bool());
			
		} else {				// S -> L = E				
			Access x = offset(id);
			match('=');
			stmt = new SetElem(x, bool());
		}
		
		match(';');
		return stmt;
	}
	
	Expr bool() throws IOException {
		Expr x = join();
		while (look.tag == Tag.OR) {
			Token tok = look;
			move();
			x = new Or(tok,x,join());
		}
		return x;
	}
	
	Expr join() throws IOException {
		Expr x = equality();
		while (look.tag == Tag.AND) {
			Token tok = look;
			move();
			x = new And(tok,x,equality());
		}
		return x;
	}
	
	Expr equality() throws IOException {
		Expr x = rel();
		while(look.tag == Tag.EQ || look.tag == Tag.NE) {
			Token tok = look;
			move();
			x = new Rel(tok,x,rel());
		}
		return x;
	}
	
	Expr rel() throws IOException {
		Expr x = expr();
		switch (look.tag) {
			case '<':
			case Tag.LE:
			case Tag.GE:
			case '>':
				Token tok = look;
				move();
				return new Rel(tok,x,expr());
			default:
				return x;
		}
	}
	
	Expr expr() throws IOException {
		Expr x = term();
		while (look.tag == '+' || look.tag == '-') {
			Token tok = look;
			move();
			x = new Arith(tok,x,term());
		}
		return x;
	}
	
	Expr term() throws IOException {
		Expr x = unary();
		while (look.tag == '*' || look.tag == '/') {
			Token tok = look;
			move();
			x = new Arith(tok,x,unary());
		}
		return x;
	}

	Expr unary() throws IOException {
		if (look.tag == '-') {
			move();
			return new Unary(Word.minus, unary());
		} else if (look.tag == '!') {
			Token tok = look;
			move();
			return new Not(tok,unary());
		}
		else return factor();
	}
	
	Expr factor() throws IOException {
		Expr x = null;
		switch (look.tag) {
			case '(':
				move();
				x = bool();
				match(')');
				return x;
			case Tag.NUM:
				x = new Constant(look,Type.Int);
				move();
				return x;
			case Tag.REAL:
				x = new Constant(look,Type.Float);
				move();
				return x;
			case Tag.TRUE:
				x = Constant.True;
				move();
				return x;
			case Tag.FALSE:
				x = Constant.False;
				move();
				return x;
			default:
				error("syntax error");
				return x;
			case Tag.ID:
				String s = look.toString();
				Id id = top.get(look);
				if (id == null)
					error(look.toString()+" undeclared");
				move();
				if (look.tag != '[') return id;
				else return offset(id);
		}
	}
	
	Access offset(Id a) throws IOException {	// I -> [E] | [E] I
		Expr i, w, t1, t2, loc;		// inherit id
		Type type = a.type;
		match('['); i = bool(); match(']');	// first index, I -> [E]
		type = ((Array)type).of;
		w = new Constant(type.width);
		t1 = new Arith(new Token('*'), i, w);
		loc = t1;
		while(look.tag == '[') {	// multi-dimensional I -> [E] I
			match('[');
			i = bool();
			match(']');
			type = ((Array)type).of;
			w = new Constant(type.width);
			t1 = new Arith(new Token('*'), i, w);
			t2 = new Arith(new Token('+'), loc, t1);
			loc = t2;
		}
		return new Access(a, loc, type);
	}*/
}

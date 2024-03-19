package lexer;

import java.io.*;
import java.util.*;
import symbols.*;

public class Lexer {
	public static int line = 1;
	char peek = ' ';
	Hashtable words = new Hashtable();
	void reserve(Word w) { 
		words.put(w.lexeme, w);
	}
	public Lexer() {
		reserve(new Word("se", Tag.SE));
		reserve(new Word("senao", Tag.SENAO));
		reserve(new Word("enquanto", Tag.ENQUANTO));
		reserve(new Word("faca", Tag.FACA));
		reserve(new Word("inicio", Tag.INICIO));
		reserve(new Word("programa", Tag.PROGRAMA));
		reserve(new Word("fimprograma", Tag.FIMPROGRAMA));
		reserve(new Word("leia", Tag.LEIA));
		reserve(new Word("escreva", Tag.ESCREVA));
		reserve(new Word("entao", Tag.ENTAO));
		reserve(new Word("fimenquanto", Tag.FIMENQUANTO));
		reserve(new Word("div", Tag.DIV));
		reserve(new Word("e", Tag.E));
		reserve(new Word("ou", Tag.OU));
		reserve(new Word("nao", Tag.NAO));
		reserve(Word.True);
		reserve(Word.False);
		reserve(Type.Int);
		reserve(Type.Bool);
	}
	
	void readch() throws IOException {
		this.peek = (char)System.in.read();
	}
	boolean readch(char c) throws IOException {
		readch();
		if (this.peek != c) return false;
		this.peek = ' ';
		return true;
	}
	
	public Token scan() throws IOException {
		for( ; ; readch()) {
			if (peek == ' ' || peek == '\t') continue;
			else if (peek == '\n' || peek == '\r') line++;
			else break;
		}
		
		switch(peek) {
			case '<':
				if (readch('-')) return Word.atrib;
				else return Word.menor;
			case '>':
				return Word.maior;				
		}
		
		
		if (Character.isDigit(peek)) {
			int v = 0;
			do {
				v = 10*v + Character.digit(peek,10);
				readch();
			} while (Character.isDigit(peek));
			
			if (peek != '.') return new Num(v);
			
			float x = v, d = 10;
			while(true) {
				readch();
				if (!Character.isDigit(peek)) break;
				x = x + Character.digit(peek, 10)/d;
				d = d*10;
			}
			return new Real(x);
		}
		
		if (Character.isLetter(peek)) {
			StringBuffer b = new StringBuffer();
			do {
				b.append(peek);
				readch();
			} while (Character.isLetterOrDigit(peek));
			
			String s = b.toString();
			Word w = (Word) words.get(s);
			if (w != null) return w;
			w = new Word(s, Tag.ID);
			words.put(s, w);
			return w;
		}
		
		Token tok = new Token(peek);
		peek = ' ';
		return tok;
	}
}

package lexer;
public class Word extends Token {
	public String lexeme = "";
	public Word(String s, int tag) {
		super(tag);
		this.lexeme = s;
	}
	public String toString() {
		return this.lexeme;
	}
	public static final Word
		e = new Word("&&", Tag.E),
		ou = new Word("ou", Tag.OU),
		atrib = new Word("<-", Tag.ATRIB),
		nao = new Word("nao", Tag.NAO),
		menor = new Word("<", Tag.MENOR),
		maior = new Word(">", Tag.MAIOR),
		True = new Word("V", Tag.TRUE),
		False = new Word("F", Tag.FALSE);
}

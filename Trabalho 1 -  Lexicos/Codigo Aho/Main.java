import java.io.*;
import java.util.Scanner;

import lexer.*;
import parser.*;

public class Main {
	public static void main(String[] args) throws IOException {
		InputStream input = null;
		try {
			File file = new File("../avaliacao.simples");
		    input = new FileInputStream(file);

			
			System.setIn(input);
			Lexer lex = new Lexer();
			Parser parse = new Parser(lex);
			parse.program();
			System.out.write('\n');
		}     
		finally {
		    if (input != null) {
		        try {
		            input.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		}
	}
}

package org.kaivos.fortob;

import java.io.File;
import java.io.IOException;

import org.kaivos.nept.parser.TokenList;
import org.kaivos.nept.parser.TokenScanner;

/**
 * A command line Fortob interpreter
 * 
 * @author Iikka Hauhio
 */
public class FortobInterpreter {

	static final TokenScanner scanner = new TokenScanner()
		.separateIdentifiersAndPunctuation(true)
		.addCommentRule("#", "\n");
	
	/**
	 * Reads a file and interprets it using FortobReadcom.
	 * 
	 * @param args Should contain only one element, the name of the file
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("usage: fortob <file>");
			System.exit(1);
			return;
		}
		
		String file = args[0];
		
		try {
			TokenList tl = scanner.tokenize(new File(file));
			
			FortobEnvironment env = new FortobEnvironment();
			
			FortobReadcom.READCOM.proceed(tl, env);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

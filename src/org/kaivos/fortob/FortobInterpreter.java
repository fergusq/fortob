package org.kaivos.fortob;

import java.io.File;
import java.io.IOException;

import org.kaivos.fortob.annotation.NonNullByDefault;
import org.kaivos.fortob.environment.FortobEnvironment;
import org.kaivos.fortob.util.Checker;
import org.kaivos.nept.parser.TokenList;
import org.kaivos.nept.parser.TokenScanner;

/**
 * A command line Fortob interpreter
 * 
 * @author Iikka Hauhio
 */
@NonNullByDefault
public class FortobInterpreter {

	/**
	 * The default scanner used to tokenize Fortob code
	 */
	public static final TokenScanner scanner = new TokenScanner()
		.separateIdentifiersAndPunctuation(true)
		.addStringRule('"', '"', '\\')
		.addCommentRule("#", "\n");
	
	/**
	 * Reads a file and interprets it using FortobReadcom.
	 * 
	 * @param args Should contain only one element, the name of the file
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("usage: fortob [-d] <file>");
			System.exit(1);
			return;
		}
		
		int i = 0;
		for (; i < args.length-1; i++) {
			switch (args[i]) {
			case "-d":
				FortobReadcom.DEBUG = true;
				break;
			default:
				System.err.println("Unknown option `" + args[i] + "'");
				System.exit(1);
				return;
			}
		}
		
		String file = args[i];
		
		try {
			TokenList tl = scanner.tokenize(new File(file));
			
			FortobEnvironment env = new FortobEnvironment();
			
			FortobReadcom.READCOM.proceed(Checker.check(tl), env);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

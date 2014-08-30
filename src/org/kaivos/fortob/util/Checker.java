package org.kaivos.fortob.util;

import org.kaivos.fortob.annotation.NonNull;
import org.kaivos.fortob.annotation.Nullable;

/**
 * Contains various static methods used agains the null
 * 
 * @author Iikka Hauhio
 *
 */
public class Checker {

	private Checker() {}
	
	/**
	 * Checks if a value if null
	 * 
	 * @param t The value
	 * @return The value
	 * @throws NullPointerException if the value is null
	 */
	public static <T> @NonNull T check(@Nullable T t) throws NullPointerException {
		if (t != null) return t;
		else throw new NullPointerException();
	}
	
}

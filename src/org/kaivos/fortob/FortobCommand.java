package org.kaivos.fortob;

import org.kaivos.fortob.environment.FortobEnvironment;
import org.kaivos.nept.parser.TokenList;

/**
 * Represents a Fortob command
 * 
 * @author Iikka Hauhio
 *
 */
public interface FortobCommand {
	
	/**
	 * Executes command in specific environment
	 * 
	 * @param tl The input queue
	 * @param env The environment
	 */
	public void proceed(TokenList tl, FortobEnvironment env);
	
}

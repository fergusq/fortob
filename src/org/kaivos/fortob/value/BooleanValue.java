package org.kaivos.fortob.value;

import org.kaivos.fortob.annotation.NonNullByDefault;

/**
 * Represents a boolean type
 * 
 * @author Iikka Hauhio
 *
 */
@NonNullByDefault
public interface BooleanValue {

	/**
	 * The value
	 * 
	 * @return the value
	 */
	public boolean value();
	
}

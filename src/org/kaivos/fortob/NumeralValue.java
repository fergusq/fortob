package org.kaivos.fortob;

import java.math.BigDecimal;

/**
 * Represents a numeral type
 * 
 * @author Iikka Hauhio
 *
 */
public interface NumeralValue {

	/**
	 * The value
	 * 
	 * @return the value
	 */
	public BigDecimal value();
	
}

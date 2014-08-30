package org.kaivos.fortob.value;

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

package org.kaivos.fortob.value;

import java.math.BigDecimal;

import org.kaivos.fortob.annotation.NonNullByDefault;

/**
 * Represents a numeral type
 * 
 * @author Iikka Hauhio
 *
 */
@NonNullByDefault
public interface NumeralValue {

	/**
	 * The value
	 * 
	 * @return the value
	 */
	public BigDecimal value();
	
}

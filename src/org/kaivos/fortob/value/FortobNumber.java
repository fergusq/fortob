package org.kaivos.fortob.value;

import java.math.BigDecimal;

import org.kaivos.fortob.environment.FortobEnvironment;

/**
 * Represents a floating point number
 * 
 * @author Iikka Hauhio
 *
 */
public class FortobNumber implements FortobValue, NumeralValue {

	private BigDecimal num;
	
	/**
	 * The default constructor
	 * 
	 * @param val
	 */
	public FortobNumber(double val) {
		this.num = new BigDecimal(val);
	}
	
	private FortobNumber(BigDecimal val) {
		this.num = val;
	}
	
	@Override
	public FortobValue invokeMethod(FortobEnvironment env, String name, FortobValue... args) {
		
		if (args.length == 0) {
			switch (name) {
			case "!":
				return new FortobNumber(num.negate());

			default:
				break;
			}
		}
		
		if (args.length == 1 && args[0] instanceof NumeralValue) {
		
			BigDecimal a = this.num;
			BigDecimal b =  ((NumeralValue) args[0]).value();
		
			switch (name) {
			case "+":
				return new FortobNumber(a.add(b));
			case "-":
				return new FortobNumber(a.subtract(b));
			case "*":
				return new FortobNumber(a.multiply(b));
			case "/":
				return new FortobNumber(a.divide(b));
			case "=":
				return new FortobBoolean(a.compareTo(b) == 0);
			case "<":
				return new FortobBoolean(a.compareTo(b) == -1);
			case ">":
				return new FortobBoolean(a.compareTo(b) == 1);
				
			default:
				break;
			}
		
		}
		
		throw FortobValue.unknownMethod(this, name, args);
	}
	
	@Override
	public FortobType getType() {
		return FortobType.NUMBER;
	}
	
	@Override
	public BigDecimal value() {
		return num;
	}
	
	@Override
	public String toString() {
		return num + "";
	}

}

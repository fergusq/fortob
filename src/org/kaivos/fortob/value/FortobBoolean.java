package org.kaivos.fortob.value;

import org.kaivos.fortob.FortobInterpreter;
import org.kaivos.fortob.FortobReadcom;
import org.kaivos.fortob.annotation.NonNull;
import org.kaivos.fortob.annotation.NonNullByDefault;
import org.kaivos.fortob.environment.FortobEnvironment;

/**
 * Represents a boolean
 * 
 * @author Iikka Hauhio
 *
 */
@NonNullByDefault
public class FortobBoolean implements FortobValue, BooleanValue {
	
	private boolean bool;
	
	/**
	 * The default constructor
	 * 
	 * @param val
	 */
	public FortobBoolean(boolean val) {
		this.bool = val;
	}

	@Override
	public @NonNull FortobValue invokeMethod(@NonNull FortobEnvironment env, @NonNull String name, @NonNull FortobValue... args) {
		
		if (args.length == 0) {
			switch (name) {
			case "!":
				return new FortobBoolean(!bool);

			default:
				break;
			}
		}
		
		if (args.length == 1) {
			switch (name) {
			case "if":
				if (bool)
					return FortobReadcom.READCOM.eval(FortobInterpreter.scanner.tokenize(args[0].toString(), "<snippet>"), env.sub());
				else
					return new FortobBoolean(false);
			default:
				break;
			}
		}
		
		if (args.length == 2) {
			switch (name) {
			case "if":
				if (bool)
					return FortobReadcom.READCOM.eval(FortobInterpreter.scanner.tokenize(args[0].toString(), "<snippet>"), env.sub());
				else
					return FortobReadcom.READCOM.eval(FortobInterpreter.scanner.tokenize(args[1].toString(), "<snippet>"), env.sub());
			default:
				break;
			}
		}
		
		throw FortobValue.unknownMethod(this, name, args);
	}
	
	@Override
	public FortobType getType() {
		return FortobType.BOOLEAN;
	}
	
	@Override
	public boolean value() {
		return bool;
	}
	
	@Override
	public String toString() {
		return bool + "";
	}

}

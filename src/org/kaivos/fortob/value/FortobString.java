package org.kaivos.fortob.value;

import org.kaivos.fortob.FortobInterpreter;
import org.kaivos.fortob.FortobReadcom;
import org.kaivos.fortob.annotation.NonNull;
import org.kaivos.fortob.annotation.NonNullByDefault;
import org.kaivos.fortob.environment.FortobEnvironment;

import static org.kaivos.fortob.util.Checker.check;

/**
 * Represents a string
 * 
 * @author Iikka Hauhio
 *
 */
@NonNullByDefault
public class FortobString implements FortobValue {

	private @NonNull String str;
	
	/**
	 * The default constructor
	 * @param val
	 */
	public FortobString(@NonNull String val) {
		this.str = val;
	}
	
	@Override
	public @NonNull FortobValue invokeMethod(@NonNull FortobEnvironment env, @NonNull String name, @NonNull FortobValue... args) {
		if (args.length == 0) {
			switch (name) {
			case "print":
				System.out.print(str);
				return this;
			case "println":
				System.out.println(str);
				return this;

			default:
				break;
			}
		}
		if (args.length == 1) {
			switch (name) {
			case "+":
				return new FortobString(str + args[0].toString());
			case "=":
				return new FortobBoolean(str.equals(args[0].toString()));
				
			case "while": {
				boolean flag = false;
				while (true) {
					FortobValue val = FortobReadcom.READCOM.eval(check(FortobInterpreter.scanner.tokenize(str, "<snippet>")), env.sub());
					if (val.getType() != FortobType.BOOLEAN || !(val instanceof BooleanValue)) {
						throw new RuntimeException("Unknown method `" + name + "'");
					}
					boolean cond = ((BooleanValue) val).value();
					if (cond) {
						flag = true;
						FortobReadcom.READCOM.proceed(check(FortobInterpreter.scanner.tokenize(args[0].toString(), "<snippet>")), env.sub());
					} else {
						break;
					}
				}
				return new FortobBoolean(flag);
			}
				
			default:
				break;
			}
		}
		
		if (args.length == 1 || args.length == 2) {
			switch (name) {
			case "if": {
				FortobValue val = FortobReadcom.READCOM.eval(check(FortobInterpreter.scanner.tokenize(str, "<snippet>")), env.sub());
				if (val.getType() != FortobType.BOOLEAN || !(val instanceof BooleanValue)) {
					throw new RuntimeException("Unknown method `" + name + "'");
				}
				boolean cond = ((BooleanValue) val).value();
				if (cond)
					return FortobReadcom.READCOM.eval(check(FortobInterpreter.scanner.tokenize(args[0].toString(), "<snippet>")), env.sub());
				else {
					if (args.length == 1)
						return new FortobBoolean(false);
					else
						return FortobReadcom.READCOM.eval(check(FortobInterpreter.scanner.tokenize(args[1].toString(), "<snippet>")), env.sub());
				}
			}
	
			default:
				break;
			}
		}
		
		switch (name) {
		case "apply":{
			FortobEnvironment senv = env.sub();
			for (int i = args.length-1; i >= 0; i--) senv.push(args[i]);
			return FortobReadcom.READCOM.eval(check(FortobInterpreter.scanner.tokenize(str, "<function `" + str + "'>")), senv);
		}
		case "proceed":{
			FortobEnvironment senv = env.sub();
			for (int i = args.length-1; i >= 0; i--) senv.push(args[i]);
			FortobReadcom.READCOM.proceed(check(FortobInterpreter.scanner.tokenize(str, "<function `" + str + "'>")), senv);
			return new FortobBoolean(true);
		}
		default:
			break;
		}
		
		throw FortobValue.unknownMethod(this, name, args);
	}
	
	@Override
	public FortobType getType() {
		return FortobType.STRING;
	}
	
	@Override
	public String toString() {
		return str;
	}

}

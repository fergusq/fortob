package org.kaivos.fortob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.kaivos.fortob.annotation.NonNull;
import org.kaivos.fortob.annotation.NonNullByDefault;
import org.kaivos.fortob.environment.FortobEnvironment;
import org.kaivos.fortob.value.FortobBoolean;
import org.kaivos.fortob.value.FortobNumber;
import org.kaivos.fortob.value.FortobString;
import org.kaivos.fortob.value.FortobValue;
import org.kaivos.nept.parser.Token;
import org.kaivos.nept.parser.TokenList;

import static org.kaivos.fortob.util.Checker.check;

/**
 * FortobReadcom is a class that reads and interprets commands.
 * 
 * @author Iikka Hauhio
 */
@NonNullByDefault
public class FortobReadcom implements FortobCommand {

	private static int ilevel = 0;
	private static String i() {
		return IntStream.range(0, ilevel).mapToObj(i -> "\t").collect(Collectors.joining());
	}
	private static boolean DEBUG = false;
	
	/**
	 * FortobReadcom. The original and the best.
	 */
	public static final FortobReadcom READCOM = new FortobReadcom();
	
	private Map<String, @NonNull FortobCommand> commandMap = new HashMap<>();
	
	private FortobReadcom() {
		
		commandMap.put(";", (tl, env) -> env.pop());
		
		commandMap.put("[", (tl, env) -> {
			String str = "";
			
			int i = 0;
			while (true) {
				if (tl.isNext("[")) i++;
				if (tl.isNext("]")) i--;
				if (i < 0) break;
				
				str += " " + tl.nextString();
			}
			
			tl.accept("]");
			
			env.push(new FortobString(check(str.trim())));
		});
		
		commandMap.put("\"", (tl, env) -> {
			String str = tl.nextString();
			
			env.push(new FortobString(check(str.trim())));
			
			tl.accept("\"");
		});
		
		commandMap.put("_", (tl, env) -> env.push(new FortobString(" ")));
		
		commandMap.put("'", (tl, env) -> env.push(new FortobNumber(0)));
		
		IntStream.range(0, 9).forEach(i ->
			commandMap.put(""+i, (tl, env) -> env.push(env.pop().invokeMethod(env, "*", new FortobNumber(10)).invokeMethod(env, "+", new FortobNumber(i)))));
		
		commandMap.put(".", (tl, env) -> {
			FortobValue obj = env.pop();
			String method = tl.nextString();
			List<FortobValue> args = new ArrayList<>();
			
			if (tl.isNext(":")) {
				do {
					tl.next();
					args.add(eval(tl, env));
				} while (tl.isNext(","));
			}
			
			if (DEBUG) System.err.println(i() + obj + "." + method + args);
			ilevel++;
			env.push(obj.invokeMethod(env, check(method), args.toArray(new @NonNull FortobValue[0])));
			ilevel--;
			if (DEBUG) System.err.println(i() + "= " + env.peek());
		});
		
		for (String operator : new @NonNull String[] {"+", "-", "*", "/", "=", "<", ">"})
			commandMap.put(operator, (tl, env) -> env.push(env.pop().invokeMethod(env, operator, eval(tl, env))));
		commandMap.put("(", (tl, env) -> {
			proceed(tl, env);
			tl.accept(")");
		});
		
		commandMap.put("$", (tl, env) -> {
			String name = tl.nextString();
			if (tl.isNext("=")) {
				tl.accept("=");
				env.put(name, eval(tl, env));
			}
			env.push(env.get(name));
		});
		
		commandMap.put("@", (tl, env) -> {
			String name = tl.nextString();
			FortobValue val = eval(tl, env);
			commandMap.put(name, (stl, senv) -> {
				FortobEnvironment cenv = senv.sub();
				cenv.put("!", tokenListObj(stl, senv));
				cenv.put("\\", envObj(senv));
				val.invokeMethod(cenv, "proceed");
			});
			env.push(val);
		});
		
		commandMap.put("p", (tl, env) -> System.out.println(env.peek()));
	}
	
	private @NonNull FortobValue tokenListObj(@NonNull TokenList tl, @NonNull FortobEnvironment env) {
		FortobValue val = new FortobValue() {
			@Override
			public @NonNull FortobValue invokeMethod(@NonNull FortobEnvironment env, @NonNull String name,
					@NonNull FortobValue... args) {
				if (args.length == 0) {
					switch (name) {
					case "nextString":
						return new FortobString(check(tl.nextString()));
					case "nextNumber":
						return new FortobNumber(Double.parseDouble(tl.nextString()));
					case "seekString":
						return new FortobString(check(tl.seekString()));
					case "seekNumber":
						return new FortobNumber(Double.parseDouble(tl.seekString()));
					case "readcom":
						return eval(tl, env);
					}
				}
				
				if (args.length == 1) {
					switch (name) {
					case "accept":
						tl.accept(args[0].toString());
						return new FortobBoolean(true);
					}
				}
				
				throw FortobValue.unknownMethod(this, name, args);
			}
		};
		return val;
	}
	
	private @NonNull FortobValue envObj(@NonNull FortobEnvironment env) {
		FortobValue val = new FortobValue() {
			@Override
			public @NonNull FortobValue invokeMethod(@NonNull FortobEnvironment e, @NonNull String name,
					@NonNull FortobValue... args) {
				if (args.length == 0) {
					switch (name) {
					case "pop":
						return env.pop();
					case "drop":
						env.drop();
						return this;
					case "dup":
						env.dup();
						return this;
					}
				}
				
				if (args.length == 1) {
					switch (name) {
					case "push":
						env.push(args[0]);
						return this;
					case "get":
						return env.get(args[0].toString());
					}
				}
				
				if (args.length == 2) {
					switch (name) {
					case "put":
						env.put(args[0].toString(), args[1]);
						return this;
					}
				}
				
				throw FortobValue.unknownMethod(this, name, args);
			}
		};
		return val;
	}
	
	@Override
	public void proceed(@NonNull TokenList tl, @NonNull FortobEnvironment env) {
		env.putLocal("?", tokenListObj(tl, env));
		
		if (tl.isNext("\\")) {
			tl.accept("\\");
			return;
		}
		
		while (tl.hasNext() ? commandMap.containsKey(tl.seekString()) : false) {
			proceedOnce(tl, env);
			if (tl.isNext("\\")) {
				tl.accept("\\");
				break;
			}
		}
	}
	
	private void proceedOnce(@NonNull TokenList tl, @NonNull FortobEnvironment env) {
		Token next = tl.next();
		String command = next.getToken();
		int line = next.getLine();
		
		
		if (DEBUG) System.err.println(i() + command + "@" + line);
		
		ilevel++;
			
		commandMap.get(command).proceed(tl, env);
				
		ilevel--;
			
		if (DEBUG) System.err.println(i() + env);
	}
	
	/**
	 * Same as proceed, but returns the top value of the stack
	 * 
	 * @param tl The token list
	 * @param env The environment
	 * @return the top value of the stack
	 */
	public @NonNull FortobValue eval(@NonNull TokenList tl, @NonNull FortobEnvironment env) {
		proceed(tl, env);
		if (DEBUG) System.err.println(i() + "= " + env.peek());
		return env.pop();
	}

}
